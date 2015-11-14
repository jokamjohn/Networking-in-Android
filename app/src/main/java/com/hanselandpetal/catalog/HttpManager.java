package com.hanselandpetal.catalog;

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

        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

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
}
