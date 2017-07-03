package com.esprit.mycitymystory.model;

/**
 * Model used in HomeFragment for displaying nearby venues
 *
 * @author Syrine Dridi
 */
public class NearbyModel {
    String id, image, title, distance, promos, rating;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPromos() {
        return promos;
    }

    public void setPromos(String promos) {
        this.promos = promos;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
