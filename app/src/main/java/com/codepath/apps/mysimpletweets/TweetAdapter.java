package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.utils.TimeUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by jian_feng on 4/9/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.TweetViewHolder> {
    public class TweetViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvTime;
        public TextView tvUserScreenName;
        public ImageView ivTweetImage;

        public TweetViewHolder(View itemView) {
            super(itemView);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvUserScreenName = (TextView) itemView.findViewById(R.id.tv_user_screen_name);
            ivTweetImage = (ImageView) itemView.findViewById(R.id.iv_tweet_image);
        }
    }

    private List<Tweet> mTweets;
    private Context mContext;

    public TweetAdapter(Context context, List<Tweet> tweets) {
        mContext = context;
        mTweets = tweets;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_tweet, parent, false);
        TweetViewHolder tweetViewHolder = new TweetViewHolder(itemView);
        return tweetViewHolder;
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        Tweet tweet = mTweets.get(position);
        String tweetImage = tweet.getImageUrl();

        ImageView ivProfileImage = holder.ivProfileImage;
        TextView tvBody = holder.tvBody;
        TextView tvUserName = holder.tvUserName;
        TextView tvTime = holder.tvTime;
        TextView tvUserScreenName = holder.tvUserScreenName;
        ImageView ivTweetImage = holder.ivTweetImage;

        ivProfileImage.setImageResource(0);
        Picasso.with(getContext()).load(tweet.getUser().profileImageUrl).transform(new RoundedCornersTransformation(10,0)).fit().into(ivProfileImage);

        if ( !TextUtils.isEmpty(tweet.getImageUrl()) ) {
            Picasso.with(getContext()).load(tweet.getImageUrl())
                    .transform(new RoundedCornersTransformation(10,0))
                    .resize(ivTweetImage.getWidth(), 600).into(ivTweetImage);
        }

        tvBody.setText(tweet.getBody());
        tvUserScreenName.setText(tweet.getUser().screenName);
        tvTime.setText(TimeUtils.getRelativeTimeAgo(tweet.getCreateAt()));
        tvUserName.setText(tweet.getUser().name);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> tweets) {
        mTweets.addAll(tweets);
        notifyDataSetChanged();
    }

}
