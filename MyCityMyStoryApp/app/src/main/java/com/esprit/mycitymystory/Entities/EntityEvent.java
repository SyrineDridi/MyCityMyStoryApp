package com.esprit.mycitymystory.Entities;



import android.os.Parcel;
import android.os.Parcelable;

import com.esprit.mycitymystory.Entities.Entities.Evaluation;
import com.esprit.mycitymystory.Entities.Entities.User;

import java.util.List;

/**
 * Created by Syrine on 07/11/2016.
 */

public class EntityEvent implements Parcelable {

    String id ;
    private String city ;
    private String country ;
    private   String Title ;


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

    private String Description ;
    private String StartDate ;
    private String EndDate ;
    private String StartTime ;
    private String EndTime ;
    private String Place ;
    private String Category ;
    private Double LongitudeEvent ;
    private Double LatitudeEvent ;
    private  String urlImage ;
    private  String user_id ;
    private int NbPlaces;

    private int NbAvailablePlaces ;
    private User user  ;
    List<User> participants ;

    List <Evaluation> evaluations ;

    public EntityEvent(String title, String startDate, String endDate) {
        Title = title;
        StartDate = startDate;
        EndDate = endDate;
    }

    public EntityEvent() {

    }

    public EntityEvent(String id, String user_id,String title, String description,
                       String startDate, String endDate,
                       String place, String category, int nbPlaces,
                       int nbAvailablePlaces, User user,Double longitudeEvent ,
                       List<Evaluation> evaluations,String urlImage,Double latitudeEvent) {
        this.id = id;
        Title = title;
        Description = description;
        this.user_id=user_id;
        StartDate = startDate;
        EndDate = endDate;
        Place = place;
        this.LatitudeEvent = latitudeEvent;
        this.LongitudeEvent=longitudeEvent;
        Category = category;
        NbPlaces = nbPlaces;
        NbAvailablePlaces = nbAvailablePlaces;
        this.user = user;
        this.participants = participants;
        this.evaluations = evaluations;
        this.urlImage = urlImage ;
    }



    public String getUrlImage() {
        return urlImage;
    }

    public Double getLongitudeEvent() {
        return LongitudeEvent;
    }

    public void setLongitudeEvent(Double longitudeEvent) {
        LongitudeEvent = longitudeEvent;
    }

    public Double getLatitudeEvent() {
        return LatitudeEvent;
    }

    public void setLatitudeEvent(Double latitudeEvent) {
        LatitudeEvent = latitudeEvent;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }



    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public int getNbPlaces() {
        return NbPlaces;
    }

    public void setNbPlaces(int nbPlaces) {
        NbPlaces = nbPlaces;
    }

    public int getNbAvailablePlaces() {
        return NbAvailablePlaces;
    }

    public void setNbAvailablePlaces(int nbAvailablePlaces) {
        NbAvailablePlaces = nbAvailablePlaces;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }


    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }
    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", Title='" + Title + '\'' +
                ", Description='" + Description + '\'' +
                ", StartDate=" + StartDate +
                ", EndDate=" + EndDate +
                ", Place='" + Place + '\'' +
                ", Category='" + Category + '\'' +
                ", NbPlaces=" + NbPlaces +
                ", NbAvailablePlaces=" + NbAvailablePlaces +
                ", user_id=" + user_id +
                ", participants=" + participants +
                ", evaluations=" + evaluations +
                ", urlimage=" + urlImage +
                ", longitude=" + LongitudeEvent +
                ", latitude=" + LatitudeEvent +
                ", city=" + city +
                ", country=" + country +

                '}';
    }
        @Override
        public int describeContents() {
            return 0;
            // Lorsque on parle de l'heritage , classe mére 0 , classe fille doit contenir 1 ect ..
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(Title);
            dest.writeString(Description);
            dest.writeString(Place);
            dest.writeString(user_id);
            dest.writeInt(NbPlaces);
            dest.writeString(Category);
            dest.writeString(StartDate.toString());
            dest.writeString(EndDate.toString());
            dest.writeInt(NbAvailablePlaces);
            dest.writeDouble(LongitudeEvent);
            dest.writeString(urlImage);
            dest.writeString(city);
            dest.writeString(country);
            dest.writeDouble(LatitudeEvent);


        }

        private EntityEvent(Parcel in){
            id = in.readString();
            Title= in.readString();
            user_id= in.readString();
            Description= in.readString();
            Place= in.readString();
            NbPlaces= in.readInt();
            LongitudeEvent= in.readDouble();
            LatitudeEvent= in.readDouble();
            NbAvailablePlaces= in.readInt();
            Category= in.readString();
            urlImage = in.readString();
            StartDate= in.readString();
            EndDate= in.readString();
            city= in.readString();
            country= in.readString();

        }
        public static final Parcelable.Creator<EntityEvent> CREATOR= new EntityEvent.Creator<EntityEvent>(){

            @Override
            public EntityEvent createFromParcel(Parcel source) {
                return new EntityEvent(source);
            }

            @Override
            public EntityEvent[] newArray(int size) {
                return new EntityEvent[size];
            }
        };

}

