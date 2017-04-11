package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.utils.TimeUtils;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class TweetDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_body) TextView tvBody;
    @BindView(R.id.iv_profile_img) ImageView ivProfileImage;
    @BindView(R.id.tv_name) TextView tvName;
    @BindView(R.id.tweet_image) ImageView tweetImage;
    @BindView(R.id.tv_screen_name) TextView tvScreenName;
    @BindView(R.id.tv_created_at) TextView tvCreatedAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        ButterKnife.bind(this);
        Tweet tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(TweetAdapter.TWEET_DETAIL_KEY));
        setupView(tweet);
    }

    private void setupView(Tweet tweet) {
        tvBody.setText(tweet.getBody());
        Picasso.with(this).load(tweet.getUser().profileImageUrl).transform(new RoundedCornersTransformation(10,0)).fit().into(ivProfileImage);

        if ( !TextUtils.isEmpty(tweet.getImageUrl()) ) {
            Picasso.with(this).load(tweet.getImageUrl())
                    .transform(new RoundedCornersTransformation(20,0)).into(tweetImage);
        }
        tvName.setText(tweet.getUser().name);
        tvScreenName.setText(tweet.getUser().screenName);
        tvCreatedAt.setText(TimeUtils.getRelativeTimeAgo(tweet.getCreateAt()));
    }
}
