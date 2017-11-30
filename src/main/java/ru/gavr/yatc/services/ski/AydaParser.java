package ru.gavr.yatc.services.ski;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class AydaParser {

    private static final Logger log = LoggerFactory.getLogger(AydaParser.class);

    public static final String HOST_URL = "http://ayda.ski.ru/index.php";
    public static final String PAGE_URI = "http://ayda.ski.ru/index.php?p=";



    public int getPostsCount() {
        try {
            Document doc = getDocument(HOST_URL);
            String postsCountStr = doc.select("div.all_items").text();
            log.debug(postsCountStr);
            return Integer.parseInt(postsCountStr.substring(7));
        } catch (NumberFormatException e) {
            log.error("Error get {}", HOST_URL);
            return 0;
        }
    }

    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();

        int postsCount = getPostsCount();
        log.debug("Posts count: {}", postsCount);

        int pagesCount = postsCount / 20 + 1;
        log.debug("Page count: {}", pagesCount);

        for (int i = 0; i < 1/*pagesCount*/; i++) {
            Document doc = getDocument(PAGE_URI + i);
            posts = doc.select("div.ayda_list_item")
                    .stream()
                    .map(item ->
                            new Post(item.selectFirst("div.ayda_item_title").selectFirst("a").attr("href"),
                                    item.selectFirst("div.ayda_item_title").selectFirst("a").text()))
                    .collect(Collectors.toList());
            for (Post p : posts) {
                fillPost(p);
            }
        }
        return posts;
    }

    public void fillPost(Post post) {
        Document postDoc = getDocument(post.getUrl());

        // process descriptor
        String descriptorText = postDoc.select("div.contentBlock").text();
        post.setDescription(Jsoup.parse(descriptorText.substring(descriptorText.indexOf("</h3>")+5)).text());

        // process params
        postDoc.select("div.adv_item")
                .stream()
                .forEach(item -> {
                    String label = item.select("div.label").text().replaceAll(":", "");
                    switch (label) {
                        case "Автор" :
                            Element user = item.selectFirst("a");
                            post.setUser(user.text());
                            post.setUserUrl(user.attr("href"));
                            break;
                        case "Метка" :
                            String labels = item.text().substring(label.length()+2);
                            post.setLabels(Arrays.stream(labels.split(","))
                                    .map(String::trim)
                                    .collect(Collectors.toList()));
                            break;
                        case "Возраст" :
                            post.setUserAge(Integer.parseInt(item.text().substring(label.length()+2)));
                            break;
                        case "Начало" :
                            post.setStartDate(LocalDate.parse(item.selectFirst("a").text(), DateTimeFormatter.ofPattern("d MMMM yyyy")
                                    .withLocale(new Locale("ru"))));
                            break;
                        case "Окончание" :
                            post.setEndDate(LocalDate.parse(item.text().substring(label.length()+2), DateTimeFormatter.ofPattern("d MMMM yyyy")
                                    .withLocale(new Locale("ru"))));
                            break;
                        case "Страна(ы)" :
                            post.setCountries(item.select("a").stream().map(Element::text).collect(Collectors.toList()));
                            break;
                        case "Курорт(ы)" :
                            post.setResorts(item.select("a").stream().map(Element::text).collect(Collectors.toList()));
                            break;
                        case "Размещение" :
                            post.setAccommodation(item.text().substring(label.length()+2));
                            break;
                        case "Расходы" :
                            post.setCost(item.text().substring(label.length()+2));
                            break;
                    }
                });
        log.debug(post.toString());
    }

    // Need for tests
    public Document getDocument(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            log.error("Error parse url: {}", url, e);
            return new Document(url);
        }
    }

}
