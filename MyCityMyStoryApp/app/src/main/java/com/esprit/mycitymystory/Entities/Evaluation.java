package com.esprit.mycitymystory.Entities;

/**
 * Created by Ch on 05/10/2016.
 */

public class Evaluation {

    private String id;
    private float note;
    private String event_id ;
    private String user_id ;

    public Evaluation() {
    }

    public Evaluation(String user_id, float note, String event_id, String id) {
        this.user_id = user_id;
        this.note = note;
        this.event_id = event_id;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getNote() {
        return note;
    }

    public void setNote(float note) {
        this.note = note;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Evaluation{" +
                "id='" + id + '\'' +
                ", note=" + note +
                ", event_id='" + event_id + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
