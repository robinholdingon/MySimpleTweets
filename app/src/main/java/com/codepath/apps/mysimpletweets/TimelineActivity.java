package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.apps.mysimpletweets.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetAdapter adapter;
    private RecyclerView lvTweets;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        lvTweets = (RecyclerView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<>();
        adapter = new TweetAdapter(this, tweets);
        lvTweets.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lvTweets.setLayoutManager(layoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int maxId, int totalItemsCount, RecyclerView view) {
                populateTimeLine(false, 1, tweets.get(tweets.size() - 1).getId());
            }
        };
        lvTweets.addOnScrollListener(scrollListener);

        client = TwitterApplication.getRestClient();
        populateTimeLine();
    }

    private void populateTimeLine() {
        populateTimeLine(true, 1, -1);
    }

    private void populateTimeLine(final boolean reload, final int sinceId, long maxId) {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                if (reload) {
                    tweets.clear();
                }
                tweets.addAll(Tweet.fromJsonArray(response));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG", errorResponse.toString());
            }
        }, sinceId, maxId);
    }
}
