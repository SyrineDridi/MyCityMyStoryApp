package com.esprit.mycitymystory.Entities.Entities;

/**
 * Created by Syrine on 13/11/2016.
 */

public class Participation {
    private String id ;
    private  String user_id ;
    private String event_id ;
    private String date_participation;
    private  String date_event_start ;

    public String getDate_event_start() {
        return date_event_start;
    }

    public void setDate_event_start(String date_event_start) {
        this.date_event_start = date_event_start;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getDate_participation() {
        return date_participation;
    }

    public void setDate_participation(String date_participation) {
        this.date_participation = date_participation;
    }


    public Participation(){

    }
    public Participation(String id, String user_id, String event_id, String date_participation) {

        this.id = id;
        this.user_id = user_id;
        this.event_id = event_id;
        this.date_participation = date_participation;
    }

    @Override
    public String toString() {
        return "Participation{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", event_id=" + event_id +
                ", date_event_start=" + date_event_start +
                ", date_participation='" + date_participation + '\'' +

                '}';
    }
}
