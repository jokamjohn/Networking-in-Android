package com.hanselandpetal.catalog;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
        BufferedReader reader = null;
        HttpURLConnection con;

        try {
            URL url = new URL(uri);
            con = (HttpURLConnection) url.openConnection();

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while ((line = reader.readLine()) !=null)
            {
                sb.append(line).append("\n");
            }

            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            if (reader != null)
            {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getData(String uri, String username, String password)
    {
        BufferedReader reader = null;
        HttpURLConnection con = null;

        byte[] loginDetails = (username + ":" + password).getBytes();
        StringBuilder loginBuilder = new StringBuilder()
            .append("Basic ")
                .append(Base64.encodeToString(loginDetails, Base64.DEFAULT));

        try {
            URL url = new URL(uri);
            con = (HttpURLConnection) url.openConnection();

            con.addRequestProperty("Authorization",loginBuilder.toString());

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while ((line = reader.readLine()) !=null)
            {
                sb.append(line).append("\n");
            }

            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
            try {
                assert con != null;
                int statusCode = con.getResponseCode();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }finally {
            if (reader != null)
            {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
