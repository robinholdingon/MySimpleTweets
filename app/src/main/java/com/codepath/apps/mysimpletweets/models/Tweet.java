package com.codepath.apps.mysimpletweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by jian_feng on 4/9/17.
 */

@Parcel
public class Tweet {
    public static String BODY_KEY = "text";
    public static String ID_KEY = "id";
    public static String CREATE_AT_KEY = "created_at";
    public static String USER_KEY = "user";

    public String body;
    public long id;

    public User user;
    public String createAt;

    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString(BODY_KEY);
            tweet.id = jsonObject.getLong(ID_KEY);
            tweet.createAt = jsonObject.getString(CREATE_AT_KEY);
            tweet.user = User.fromJson(jsonObject.getJSONObject(USER_KEY));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    public Tweet () {}

    public Tweet(String body) {
        this.body = body;

    }

    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray) {
        ArrayList<Tweet> result = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i ++) {
            try {
                result.add(fromJson(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return result;
    }

    public String getBody() {
        return body;
    }

    public long getId() {
        return id;
    }

    public String getCreateAt() {
        return createAt;
    }

    public User getUser() {
        return user;
    }
}
