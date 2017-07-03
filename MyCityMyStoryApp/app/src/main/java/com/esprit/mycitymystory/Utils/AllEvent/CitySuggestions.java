package com.esprit.mycitymystory.Utils.AllEvent;
// // This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
import java.net.URI;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.utils.URIBuilder;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by Ch on 16/10/2016.
 */

public class CitySuggestions {

    // API gives city names suggestions based on keywords. Can be utilised for autocompletion of city fields.
    // Can be used with jQuery Autocomplete.


    public String  CitySuggestions(String city)
    {
        HttpClient httpclient = HttpClients.createDefault();

        try
        {   // {city}
            URIBuilder builder = new URIBuilder("https://api.allevents.in/geo/city_suggestion/"+city);


            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
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
}
