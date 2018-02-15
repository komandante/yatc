package ru.gavr.yatc.services.ski;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.mockito.BDDMockito.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AydaParserTest {

    private static final Logger log = LoggerFactory.getLogger(AydaParserTest.class);

    //@Autowired
    @MockBean
    AydaParser aydaParser;

    @Test
    public void getPostsCount() throws Exception {
        doCallRealMethod().when(this.aydaParser).getPostsCount();
        doCallRealMethod().when(this.aydaParser).getDocument(any());
        assertTrue(aydaParser.getPostsCount() > 0);
    }

    @Test
    public void getAllPosts() throws Exception {
        doCallRealMethod().when(this.aydaParser).getAllPosts();

        File file = new File("src/test/resources/item-list.xml");
        Document postDoc = Jsoup.parse(file, "UTF-8");
        given(this.aydaParser.getDocument(any())).willReturn(postDoc);

        List<Post> posts = aydaParser.getAllPosts();
        assertEquals(2, posts.size());
    }

    @Test
    public void getEmptyAllPosts() throws Exception {
        File file = new File("src/test/resources/item-list-empty.xml");
        Document postDoc = Jsoup.parse(file, "UTF-8");
        given(this.aydaParser.getDocument(any())).willReturn(postDoc);

        doCallRealMethod().when(this.aydaParser).getAllPosts();
        List<Post> posts = aydaParser.getAllPosts();
        assertEquals(0, posts.size());
    }

    @Test
    public void clearDescriptor() {
        String descriptor = "<h3>Ищу попутчиков Мин.воды - Терскол, 5.12.2017</h3>\n" +
                "Привет! 5-го декабря прилетаю в аэропорт Мин. воды (примерно в 8 вечера)/<br>\n" +
                "Ищу попутчиков на трансфер Мин.воды - Терскол! <br>\n" +
                "(забронирован авто на 3 чел за 3 тыс р.) Я прилетаю один, буду рад веселой и интересной компании!";

        String des = Jsoup.parse(descriptor.substring(descriptor.indexOf("</h3>")+5)).text();
        assertEquals(-1, des.indexOf("<"));
    }

    @Test
    public void fillPost() throws Exception {
        Post post = new Post("url", "title");
        File postFile = new File("src/test/resources/item.xml");
        Document postDoc = Jsoup.parse(postFile, "UTF-8");

        given(this.aydaParser.getDocument("url")).willReturn(postDoc);
        doCallRealMethod().when(this.aydaParser).fillPost(post);
        aydaParser.fillPost(post);


        assertNotNull(post.getDescription());
        assertEquals("Twim", post.getUser());
        assertEquals("http://forum.ski.ru/index.php?showuser=123212", post.getUserUrl());
        assertTrue(32 == post.getUserAge());
        assertTrue(post.getLabels().size() == 3);
        assertThat(post.getLabels(), containsInAnyOrder("Экскурсии", "Лыжи/сноуборд", "Организую групповую поездку"));
        assertEquals(LocalDate.parse("2017-12-05"), post.getStartDate());
        assertEquals(LocalDate.parse("2018-03-18"), post.getEndDate());
        assertTrue(post.getCountries().size() == 3);
        assertThat(post.getCountries(), containsInAnyOrder("Италия", "Франция", "Швейцария"));
        assertTrue(post.getResorts().size() == 2);
        assertThat(post.getResorts(), containsInAnyOrder("Валь д`Изер", "Тинь"));
        assertEquals("не важно", post.getAccommodation());
        assertEquals("не важно", post.getCost());

    }

    @Test
    public void test() throws Exception {
        LocalDate.parse("5 декабря 2017", DateTimeFormatter.ofPattern("d MMMM yyyy").withLocale(new Locale("ru")));
    }
}