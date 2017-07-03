package com.esprit.mycitymystory.model;

/**
 * Model used in NewsFragment for displaying news items
 *
 * @author luchfilip
 */
public class NewsModel {
    String logoIcon, title, text, image;

    public String getLogoIcon() {
        return logoIcon;
    }

    public void setLogoIcon(String logoIcon) {
        this.logoIcon = logoIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
