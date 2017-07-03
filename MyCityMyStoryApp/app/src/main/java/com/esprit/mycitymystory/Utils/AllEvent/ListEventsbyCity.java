package com.esprit.mycitymystory.Utils.AllEvent;
import java.net.URI;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.utils.URIBuilder;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.util.EntityUtils;
/**
 * Created by Ch on 16/10/2016.
 */
public class ListEventsbyCity {
    //AIO Event listing API, allows you to fetch events by city.
    // You can filter events by date or date range.
    // API allows you to fetch events by category and keyword.
    public static String ListEventsbyCityByCategory(String city,String state,String country ,String Category)
    {
        HttpClient httpclient = HttpClients.createDefault();
        try
        {
            URIBuilder builder = new URIBuilder("https://api.allevents.in/events/list/");
            builder.setParameter("city", city);
            builder.setParameter("state", state);
            builder.setParameter("country", country);
            builder.setParameter("category",Category );
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", "72848bd8222a46e8a7de8626e7c3f9fd");
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null)
            {
                return EntityUtils.toString(entity);
            }else return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static String ListEventsbyCity(String city,String state,String country)
    {
        HttpClient httpclient = HttpClients.createDefault();
        try
        {
            URIBuilder builder = new URIBuilder("https://api.allevents.in/events/list/");
            builder.setParameter("city", city);
            builder.setParameter("state", state);
            builder.setParameter("country", country);
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", "72848bd8222a46e8a7de8626e7c3f9fd");
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null)
            {
                return EntityUtils.toString(entity);
            }else return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static String ListEventsbyCity(String city,String state,String country,String startDate )
    {
        HttpClient httpclient = HttpClients.createDefault();
        try
        {
            URIBuilder builder = new URIBuilder("https://api.allevents.in/events/list/");
            builder.setParameter("city", city);
            builder.setParameter("state", state);
            builder.setParameter("country", country);
            builder.setParameter("sdate", startDate);
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", "72848bd8222a46e8a7de8626e7c3f9fd");
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null)
            {
                return EntityUtils.toString(entity);
            }else return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static String ListEventsbyCity(String city,String state,String country,String startDate, String endDate )
    {
        HttpClient httpclient = HttpClients.createDefault();
        try
        {
            URIBuilder builder = new URIBuilder("https://api.allevents.in/events/list/");
            builder.setParameter("city", city);
            builder.setParameter("state", state);
            builder.setParameter("country", country);
            builder.setParameter("sdate", startDate);
            builder.setParameter("edate", endDate);
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", "72848bd8222a46e8a7de8626e7c3f9fd");
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null)
            {
                return EntityUtils.toString(entity);
            }else return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static String getDescriptionEvent(String idEventFb){
        HttpClient httpclient = HttpClients.createDefault();
        try
        {
            URIBuilder builder = new URIBuilder("http://elchebbi-ahmed.alwaysdata.net/mycitymystory/getDesriptionEvent.php?idEventFb="+idEventFb);
            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null)
            {
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