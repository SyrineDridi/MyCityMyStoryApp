package com.esprit.mycitymystory.Entities.Entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Syrine on 06/11/2016.
 */

public class User  implements Parcelable {


    private int id ;
    private String firstname ;
    private String lastname ;
    private String city ;
    private String country ;
    private String birthday;
    private String urlImageUser ;
    private String Sexe ;
    private String Phone;
    private String token;
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private String state;
    public User(){
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBirthday() {
        return birthday;
    }
    public String getSexe() {
        return Sexe;
    }

    public void setSexe(String sexe) {
        Sexe = sexe;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
    public String getUrlImageUser() {
        return urlImageUser;
    }

    public void setUrlImageUser(String urlImageUser) {
        this.urlImageUser = urlImageUser;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", birthday=" + birthday + '\'' +
                ", state=" + state +
                '}';
    }
    @Override
    public int describeContents() {
        return 0;
        // Lorsque on parle de l'heritage , classe mére 0 , classe fille doit contenir 1 ect ..
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(birthday);
        dest.writeString(urlImageUser);
        dest.writeString(Phone);
        dest.writeString(Sexe);
        dest.writeString(state);
    }

    private User(Parcel in){
        id = in.readInt();
        firstname= in.readString();
        lastname= in.readString();
        city= in.readString();
        country= in.readString();
        birthday= in.readString();
        urlImageUser= in.readString();
        Sexe= in.readString();
        Phone= in.readString();
        state= in.readString();
    }
    public static final Creator<User> CREATOR= new User.Creator<User>(){

        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

}

