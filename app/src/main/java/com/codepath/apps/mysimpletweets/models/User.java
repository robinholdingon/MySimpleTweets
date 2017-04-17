package com.codepath.apps.mysimpletweets.models;

import android.util.Log;

import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jian_feng on 4/9/17.
 */

@Parcel
public class User {
    public static String NAME_KEY = "name";
    public static String ID_KEY = "id";
    public static String PROFILE_IMAGE_URL_KEY = "profile_image_url";
    public static String SCREEN_NAME_KEY = "screen_name";
    public static String DESCRIPTION_KEY = "description";
    public static String FOLLOWER_COUNT_KEY = "followers_count";
    public static String FOLLOWING_COUNT_KEY = "friends_count";

    public String name;
    public long id;
    public String screenName;
    public String profileImageUrl;
    public String description;
    public int followersCount;
    public int followingCount;

    private static User currentUser;

    public static User fromJson(JSONObject jsonObject) {
        User user = new User();
        try {
            user.name = jsonObject.getString(NAME_KEY);
            user.id = jsonObject.getLong(ID_KEY);
            user.screenName = "@" + jsonObject.getString(SCREEN_NAME_KEY);
            user.profileImageUrl = jsonObject.getString(PROFILE_IMAGE_URL_KEY);
            user.description = jsonObject.getString(DESCRIPTION_KEY);
            user.followersCount = jsonObject.getInt(FOLLOWER_COUNT_KEY);
            user.followingCount = jsonObject.getInt(FOLLOWING_COUNT_KEY);
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User getCurrentUser() {
        if (currentUser == null) {
            TwitterClient client = TwitterApplication.getRestClient();
            client.getLoggedInUser(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("DEBUG", response.toString());
                    currentUser = new User();
                    try {
                        currentUser.profileImageUrl = response.getString("profile_image_url");
                        currentUser.id = response.getLong("id");
                        currentUser.name = response.getString("name");
                        currentUser.screenName =  "@" + response.getString("screen_name");
                        currentUser.description = response.getString(DESCRIPTION_KEY);
                        currentUser.followersCount = response.getInt(FOLLOWER_COUNT_KEY);
                        currentUser.followingCount = response.getInt(FOLLOWING_COUNT_KEY);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.d("DEBUG", errorResponse.toString());
                }
            });
        }
        return currentUser;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public int getFollower() {
        return followersCount;
    }

    public int getFollowing() {
        return followingCount;
    }
}
