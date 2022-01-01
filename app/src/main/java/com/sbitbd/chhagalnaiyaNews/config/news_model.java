package com.sbitbd.chhagalnaiyaNews.config;

public class news_model {
    String id,title,subtitle;
    String img;

    public news_model(String id, String title, String subtitle, String img) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
