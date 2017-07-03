package com.example.android.pim.pimandroid.entities;

/**
 * Created by Syrine on 28/03/2017.
 */
public class User {

    private int id ;
    private String email;
    private String first_name;
    private String last_name;
    private String grade ;
    private String urlImage ;
    private int Tel ;


    public User() {
    }

    public User(String email, String first_name, String last_name , String urlImage, int tel) {

        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;

        this.urlImage = urlImage;
        Tel = tel;
    }

    public User(int id, String email, String first_name, String last_name, String urlImage, int tel) {
        this.id = id;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.urlImage = urlImage;
        Tel = tel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String prenom) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public int getTel() {
        return Tel;
    }

    public void setTel(int tel) {
        Tel = tel;
    }
}
