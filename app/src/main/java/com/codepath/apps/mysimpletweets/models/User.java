package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jian_feng on 4/9/17.
 */

public class User {
    public static String NAME_KEY = "name";
    public static String ID_KEY = "id";
    public static String PROFILE_IMAGE_URL_KEY = "profile_image_url";
    public static String SCREEN_NAME_KEY = "screen_name";

    public String name;
    public long id;
    public String screenName;
    public  String profileImageUrl;

    public static User fromJson(JSONObject jsonObject) {
        User user = new User();
        try {
            user.name = jsonObject.getString(NAME_KEY);
            user.id = jsonObject.getLong(ID_KEY);
            user.screenName = jsonObject.getString(SCREEN_NAME_KEY);
            user.profileImageUrl = jsonObject.getString(PROFILE_IMAGE_URL_KEY);
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}
