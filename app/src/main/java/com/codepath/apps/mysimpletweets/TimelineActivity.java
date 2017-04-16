package com.codepath.apps.mysimpletweets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements TweetsListFragment.TweetListListener {

    private TwitterClient client;
    private static final int NEW_TWEET_ACTIVITY_CODE = 101;
    private MentionsTimelineFragment homeTimelineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pagerSlidingTabStrip.setViewPager(vpPager);

        client = TwitterApplication.getRestClient();
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
                if (resultCode == Activity.RESULT_OK) {
                    String body = data.getStringExtra(NewTweetActivity.TWEET_BODY_KEY);
                    client.createNewTweet(new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Log.d("DEBUG", responseBody.toString());
                            homeTimelineFragment.scrollToPosition(0);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.d("DEBUG", error.toString());
                        }
                    }, body);

                    Tweet newTweet = new Tweet();
                    newTweet.user = User.getCurrentUser();
                    newTweet.body = body;
                    // currently not supporting image
                    newTweet.imageUrl = null;

                    homeTimelineFragment.addTweetToPosition(0, newTweet);
                }
                break;
        }
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

    public void onProfileView(MenuItem mi) {

    }
}
