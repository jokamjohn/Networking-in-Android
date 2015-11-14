package com.hanselandpetal.catalog.parsers;

import com.hanselandpetal.catalog.model.Flower;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jokamjohn on 11/14/2015.
 */
public class FlowerJsonParser {

    public static List<Flower> parseFeed(String content)
    {

        try {
            JSONArray jsonArray = new JSONArray(content);
            List<Flower> flowerList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject flowerObject = jsonArray.getJSONObject(i);
                Flower flower = new Flower();

                flower.setCategory(flowerObject.getString("category"));
                flower.setInstructions(flowerObject.getString("instructions"));
                flower.setName(flowerObject.getString("name"));
                flower.setPrice(flowerObject.getDouble("price"));
                flower.setPhoto(flowerObject.getString("photo"));
                flower.setProductId(flowerObject.getInt("productId"));

                flowerList.add(flower);
            }
            return flowerList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
