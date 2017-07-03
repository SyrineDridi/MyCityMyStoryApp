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

public class EventsSearch {
    //Search events with specific keywords/query globally. Results can be ordered by
    // distance, nearest first by providing lat/long or city.

    public static String  EventSearch(String query,String city)
    {
        HttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder("https://api.allevents.in/events/search/");


            builder.setParameter("query", query);

            builder.setParameter("city",city);


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
    public static String EventSearch(String query,String latitude,String longitude,String city,String page)
    {
        HttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder("https://api.allevents.in/events/search/");


            builder.setParameter("query", query);
            builder.setParameter("latitude", latitude);
            builder.setParameter("longitude", longitude);
            builder.setParameter("city", city);
            builder.setParameter("page",page);

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

    public static String EventSearch(String query,String latitude,String longitude,String city)
    {
        HttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder("https://api.allevents.in/events/search/");


            builder.setParameter("query", query);
            builder.setParameter("latitude", latitude);
            builder.setParameter("longitude", longitude);
            builder.setParameter("city", city);

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

    public static String EventSearch(String query,String latitude,String longitude)
    {
        HttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder("https://api.allevents.in/events/search/");


            builder.setParameter("query", query);
            builder.setParameter("latitude", latitude);
            builder.setParameter("longitude", longitude);


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

    public static String EventSearch(String query)
    {
        HttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder("https://api.allevents.in/events/search/");


            builder.setParameter("query", query);


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
