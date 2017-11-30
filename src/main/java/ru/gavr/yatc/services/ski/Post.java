package ru.gavr.yatc.services.ski;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private String url;
    private String title;
    private String description;
    private String user;
    private String userUrl;
    private Integer userAge;
    private List<String> labels = new ArrayList<>();
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> countries = new ArrayList<>();
    private List<String> resorts = new ArrayList<>();
    private String accommodation;
    private String cost;

    public Post(String url, String title) {
        this.url = url;
        this.title = title;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public Integer getUserAge() {
        return userAge;
    }

    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }

    public void addLabel(String label) {
        this.labels.add(label);
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void addCountry(String country) {
        this.countries.add(country);
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public void addResort(String resort) {
        this.resorts.add(resort);
    }

    public List<String> getResorts() {
        return resorts;
    }

    public void setResorts(List<String> resorts) {
        this.resorts = resorts;
    }

    public String getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(String accommodation) {
        this.accommodation = accommodation;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Post{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", user='" + user + '\'' +
                ", userUrl='" + userUrl + '\'' +
                ", userAge=" + userAge +
                ", labels=" + labels +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", countries=" + countries +
                ", resorts=" + resorts +
                ", accommodation='" + accommodation + '\'' +
                ", cost='" + cost + '\'' +
                '}';
    }
}
