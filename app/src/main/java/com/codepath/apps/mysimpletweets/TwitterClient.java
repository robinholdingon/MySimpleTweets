package com.codepath.apps.mysimpletweets;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "YIWae3JqvDijQbbyvprGYD7yE";       // Change this
	public static final String REST_CONSUMER_SECRET = "IlspJroJCepsOu7LBFujud9zHcg4CB2FTQllmwN0jJReUIO6tc"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)
	public static final int initialFetch = 25;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(AsyncHttpResponseHandler handler) {
		getHomeTimeline(handler, 1, -1, initialFetch);
	}

	public void getHomeTimeline(AsyncHttpResponseHandler handler, int sinceId, long maxId) {
		getHomeTimeline(handler, sinceId, maxId, initialFetch);
	}
	public void getHomeTimeline(AsyncHttpResponseHandler handler, int sinceId, long maxId, int fetchCount) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", fetchCount);
		params.put("since_id", sinceId);
		params.put("include_entities", true);
		if (maxId > 0) {
			params.put("max_id", maxId);
		}
		client.get(apiUrl, params, handler);
	}

	public void getLoggedInUser(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		client.get(apiUrl, handler);
	}

	public void createNewTweet(AsyncHttpResponseHandler handler, String body) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", body);
		client.post(apiUrl, params, handler);
	}

	public void getMentionsTimeline(JsonHttpResponseHandler handler, int sinceId, long maxId) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", initialFetch);
		params.put("since_id", sinceId);
		params.put("include_entities", true);
		if (maxId > 0) {
			params.put("max_id", maxId);
		}
		client.get(apiUrl, params, handler);
	}

	public void getUserTimeline(AsyncHttpResponseHandler handler, String screenName) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", initialFetch);
		params.put("include_entities", true);
		params.put("screen_name", screenName);
		client.get(apiUrl, params, handler);
	}
}
