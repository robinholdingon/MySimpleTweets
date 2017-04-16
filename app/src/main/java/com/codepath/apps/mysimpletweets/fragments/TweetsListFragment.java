package com.codepath.apps.mysimpletweets.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TimelineActivity;
import com.codepath.apps.mysimpletweets.TweetAdapter;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jian_feng on 4/15/17.
 */

public abstract class TweetsListFragment extends Fragment{
    public interface TweetListListener {
    }

    protected TwitterClient client;
    private ArrayList<Tweet> aTweets;
    private TweetAdapter adapter;
    private RecyclerView lvTweets;
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeContainer;
    private LinearLayoutManager layoutManager;
    TweetListListener mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof TimelineActivity) {
            try {
                mCallback = (TweetListListener) context;
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        lvTweets = (RecyclerView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(adapter);
        lvTweets.setLayoutManager(layoutManager);
        lvTweets.addOnScrollListener(scrollListener);
        setUpSwipeContainer();
        populateTimeLine();
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        aTweets = new ArrayList<>();
        adapter = new TweetAdapter(getActivity(), aTweets);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int maxId, int totalItemsCount, RecyclerView view) {
                populateTimeLine(false, 1, aTweets.get(aTweets.size() - 1).getId());
            }
        };
    }

    private void setUpSwipeContainer() {
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
    }

    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
        adapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }

    public void clear() {
        aTweets.clear();
        adapter.notifyDataSetChanged();
    }

    public void scrollToPosition(int position) {
        lvTweets.scrollToPosition(position);
    }

    public void addTweetToPosition(int position, Tweet newTweet) {
        aTweets.add(0, newTweet);
        adapter.notifyItemInserted(0);
        lvTweets.scrollToPosition(0);
    }

    public void populateTimeLine() {
        populateTimeLine(true, 1, -1);
    }

    public abstract void populateTimeLine(final boolean reload, final int sinceId, long maxId);

}
