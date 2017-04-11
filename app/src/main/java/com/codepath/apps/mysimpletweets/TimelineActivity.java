package com.codepath.apps.mysimpletweets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.mysimpletweets.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
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
    private SwipeRefreshLayout swipeContainer;

    private static final int NEW_TWEET_ACTIVITY_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

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

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeLine();
            }
        });
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_bright);


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
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG", errorResponse.toString());
            }
        }, sinceId, maxId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_timeline_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.miCompose) {
            Intent i = new Intent(this, NewTweetActivity.class);
            // add parameters

            this.startActivityForResult(i, NEW_TWEET_ACTIVITY_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case NEW_TWEET_ACTIVITY_CODE:
                // in this case, we've called FetchExperimentConfigActivity
                // after sign in with email / password under EMAIL_EXIST_WHEN_SOCIAL_SIGNUP situation
                // so need to call googleSmartLockController the same as after normal email signin
                if (resultCode == Activity.RESULT_OK) {
                    String body = data.getStringExtra(NewTweetActivity.TWEET_BODY_KEY);
//                    client.createNewTweet(new AsyncHttpResponseHandler() {
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                            Log.d("DEBUG", responseBody.toString());
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                            Log.d("DEBUG", error.toString());
//                        }
//                    }, body);

                    Tweet newTweet = new Tweet();
                    newTweet.user = User.getCurrentUser();
                    newTweet.body = body;

                    tweets.add(0, newTweet);
                    adapter.notifyItemInserted(0);
                    lvTweets.scrollToPosition(0);
                }
                break;
        }
    }
}
