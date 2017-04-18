package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jian_feng on 4/16/17.
 */

public class UserTimelineFragment extends TweetsListFragment {

    public static String SCREEN_NAME_KEY = "screen_name";
    @Override
    public void populateTimeLine(final boolean reload, int sinceId, long maxId) {
        pd.show();
        client.getUserTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                if (reload) {
                    clear();
                }
                addAll(Tweet.fromJsonArray(response));
                pd.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG", errorResponse.toString());
                pd.dismiss();
            }
        }, getArguments().getString(SCREEN_NAME_KEY));
    }

    public static UserTimelineFragment getInstance(String screen_name) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString(SCREEN_NAME_KEY, screen_name);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }
}
