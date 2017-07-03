package com.esprit.mycitymystory.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.esprit.mycitymystory.Entities.Venue;

import java.sql.Timestamp;

/**
 * Created by Ch on 16/10/2016.
 */

public class Event implements Parcelable{

    /* Start these objects will recover by AllEvents API */
    private String eventIdFb;
    private String eventName;
    private String thumbUrl;
    private String thumbUrlLarge;
    private String bannerUrl;
    private Timestamp startTime;
    private String startTimeDisplay;
    private String endTimeDisplay;
    private String location;
    private String label ;
    private String eventUrl;
    private String shareUrl;
    private String endUrl;
    private Venue venue;

    private Double latitude;
    private Double longitude;
    /* end these objects will recover by AllEvents API */

    /* Start these objects will recover by FB API */
    private String description;
    private Timestamp endTime;
    /* End these objects will recover by FB API */




    /*
    Temperature
     */


    public String getEndTimeDisplay() {
        return endTimeDisplay;
    }

    public Event(){

    }



    public void setEndTimeDisplay(String endTimeDisplay) {
        this.endTimeDisplay = endTimeDisplay;
    }

    public String getEventIdFb() {
        return eventIdFb;
    }

    public void setEventIdFb(String eventIdFb) {
        this.eventIdFb = eventIdFb;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getThumbUrlLarge() {
        return thumbUrlLarge;
    }

    public void setThumbUrlLarge(String thumbUrlLarge) {
        this.thumbUrlLarge = thumbUrlLarge;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public String getStartTimeDisplay() {
        return startTimeDisplay;
    }

    public void setStartTimeDisplay(String startTimeDisplay) {
        this.startTimeDisplay = startTimeDisplay;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getEndUrl() {
        return endUrl;
    }

    public void setEndUrl(String endUrl) {
        this.endUrl = endUrl;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
        // Lorsque on parle de l'heritage , classe mére 0 , classe fille doit contenir 1 ect ..
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(eventName);
        dest.writeString(thumbUrlLarge);
        dest.writeString(startTimeDisplay);
        dest.writeString(endTimeDisplay);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(eventIdFb);


    }

    private Event(Parcel in){
        eventName = in.readString();
        thumbUrlLarge= in.readString();
        startTimeDisplay = in.readString();
        endTimeDisplay = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        eventIdFb = in.readString();
    }
    public static final Creator<Event> CREATOR= new Creator<Event>(){

        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}