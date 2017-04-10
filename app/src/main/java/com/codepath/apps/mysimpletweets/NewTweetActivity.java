package com.codepath.apps.mysimpletweets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewTweetActivity extends AppCompatActivity {

    @BindView(R.id.tweet_body) EditText tweetBody;
    @BindView(R.id.iv_profile_new_tweet) ImageView ivProfile;
    @BindView(R.id.tv_at_user_name_new_tweet) TextView tvUserScreenName;
    @BindView(R.id.tv_user_name_new_tweet) TextView tvUserName;
    @BindView(R.id.btn_tweet) Button createTweet;
    @BindView(R.id.tv_char_count) TextView count;
    @BindView(R.id.iv_cancel) ImageView cancel;

    private static int charCountLimit = 140;
    public static String TWEET_BODY_KEY = "tweet_body";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tweet);
        ButterKnife.bind(this);
        setupView();
    }

    private void setupView() {
        User user = User.getCurrentUser();
        tvUserName.setText(user.name);
        tvUserScreenName.setText(user.screenName);
        Picasso.with(this).load(user.profileImageUrl).into(ivProfile);
        count.setText(Integer.toString(charCountLimit));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tweetBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int textCount) {
                count.setText(Integer.toString(charCountLimit - s.length()));
                if (s.length() > 0) {
                    createTweet.setEnabled(true);
                } else {
                    createTweet.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

             }
        });

        createTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewTweetActivity.this, TimelineActivity.class);
                i.putExtra(TWEET_BODY_KEY, tweetBody.getText().toString());
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });
    }

}
