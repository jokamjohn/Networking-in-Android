package com.hanselandpetal.catalog;

import android.net.http.AndroidHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by jokamjohn on 11/14/2015.
 */
public class HttpManager {

    /**
     * Performs an http request and returns a response
     * @param uri Valid Uri
     * @return Http Response
     */
    public static String getData(String uri)
    {
        AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android Agent");
        HttpGet request = new HttpGet(uri);
        HttpResponse response;

        try {
            response = httpClient.execute(request);
            //Getting the contents of the response
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            httpClient.close();
        }
    }
}
