package com.esprit.mycitymystory.Utils;

import java.net.URI;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.utils.URIBuilder;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by Ch on 01/01/2017.
 */

public class getStateOfCity {


    //




    public static String getStateOfCity(String city )
    {
        HttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder("http://elchebbi-ahmed.alwaysdata.net/mycitymystory/getStateOfCountry.php?city="+city);




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
