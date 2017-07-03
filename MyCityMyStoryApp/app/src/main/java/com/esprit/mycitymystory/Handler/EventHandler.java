package com.esprit.mycitymystory.Handler;
import com.esprit.mycitymystory.Entities.Event;
import com.esprit.mycitymystory.Entities.Venue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Ch on 16/10/2016.
 */
public class EventHandler {
    public static  ArrayList<Event> getAllFormJson(String json ) {
        String data="";
        ArrayList<Event> events = new ArrayList<>();
        if (json != null) {
            try {
                System.out.print("xxxxxxxxxxxxxxxxx =" + json);
                JSONObject jsonObjectRoot = new JSONObject(json);
                data = jsonObjectRoot.getString("data");
                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    Event p = new Event();
                    p.setEventIdFb(jsonObject.optString("event_id"));
                    p.setEventName(jsonObject.optString("eventname"));
                    p.setThumbUrl(jsonObject.optString("thumb_url"));
                    p.setThumbUrlLarge(jsonObject.optString("thumb_url_large"));
                    p.setBannerUrl(jsonObject.optString("banner_url"));
                    p.setStartTime(new Timestamp(jsonObject.optLong("start_time")));
                    p.setEndTimeDisplay(jsonObject.optString("end_time_display"));
                    p.setStartTimeDisplay(jsonObject.optString("start_time_display"));
                    p.setLocation(jsonObject.optString("location"));
                    // Venue
                    p.setVenue(getVenueJSON(jsonObject.optJSONObject("venue")));
                    p.setLabel(jsonObject.optString("label"));
                    p.setEventUrl(jsonObject.optString("event_url"));
                    p.setShareUrl(jsonObject.optString("share_url"));
                    p.setEndUrl(jsonObject.optString("end_url"));
                    JSONObject jsonVenuObject = jsonObject.optJSONObject("venue");
                    System.out.println("**************Langitude ****************************** API Event : "+jsonVenuObject.optLong("latitude"));
                    p.setLatitude(jsonVenuObject.optDouble("latitude"));
                    p.setLongitude(jsonVenuObject.optDouble("longitude"));
                    events.add(p);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return events;
            }
        }
        return events;
    }
    static Venue getVenueJSON(JSONObject jsonObject){
        Venue venue = new Venue();
        venue.setStreet(jsonObject.optString("street"));
        venue.setCity(jsonObject.optString("city"));
        venue.setState(jsonObject.optString("state"));
        venue.setCountry(jsonObject.optString("country"));
        venue.setLatitude(jsonObject.optLong("latitude"));
        venue.setLatitude(jsonObject.optLong("longitude"));
        venue.setLatitude(jsonObject.optLong("full_address"));
        return venue;
    }
    public static Event getDescriptionEventJSON(Event e,String jsonEventDescription){
        try {
            JSONObject jsonObjectRoot = new JSONObject(jsonEventDescription);
            e.setDescription(jsonObjectRoot.optString("description"));
        } catch (JSONException ee) {
            ee.printStackTrace();
        }
        return e;
    }
}