package com.esprit.mycitymystory.Handler;

import com.esprit.mycitymystory.Entities.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ch on 01/01/2017.
 */

public class getStateOfCityHandler {



    public static String getAllFormJson(String json  ) {
         ArrayList<Event> events = new ArrayList<>();
        try{
            JSONArray jsonObjectRoot = new JSONArray(json);
            if (jsonObjectRoot.length() != 0){
                JSONObject j = jsonObjectRoot.getJSONObject(0);
                return j.optString("state");
            }
            return "TU";



        }catch(JSONException e){
            e.printStackTrace();
            return "TU";

        }

    }

}
