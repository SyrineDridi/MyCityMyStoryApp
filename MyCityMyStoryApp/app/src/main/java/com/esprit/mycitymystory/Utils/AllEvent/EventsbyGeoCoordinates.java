package com.esprit.mycitymystory.Utils.AllEvent;

import java.net.URI;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.utils.URIBuilder;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by Ch on 16/10/2016.
 */

public class EventsbyGeoCoordinates {



    public static String EventsbyGeoCoordinates(String latitude ,String longitude,String radius )
    {
        // Fetch events by Geo coordinates and radius. API can be used for applications where
        // you display nearby events to user's location.

        HttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder("https://api.allevents.in/events/geo/?latitude="+latitude+"&longitude="+longitude+"&radius="+radius);

            //   builder.setParameter("page", "{number}");
            //   builder.setParameter("category", "{string}");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", "72848bd8222a46e8a7de8626e7c3f9fd");



            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();


            if (entity != null)
            {
                // System.out.println(EntityUtils.toString(entity));
                return EntityUtils.toString(entity);
            }else return null;
        }
        catch (Exception e)
        {

            System.out.println(e.getMessage());
            return null;
        }
    }





    public static String EventsbyGeoCoordinates(String latitude ,String longitude,String radius,String category )
    {
        // Fetch events by Geo coordinates and radius. API can be used for applications where
        // you display nearby events to user's location.

        HttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder("https://api.allevents.in/events/geo/?latitude="+latitude+"&longitude="+longitude+"&radius="+radius);

               builder.setParameter("category",category);

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", "72848bd8222a46e8a7de8626e7c3f9fd");



            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();


            if (entity != null)
            {
                // System.out.println(EntityUtils.toString(entity));
                return EntityUtils.toString(entity);
            }else return null;
        }
        catch (Exception e)
        {

            System.out.println(e.getMessage());
            return null;
        }
    }

}
