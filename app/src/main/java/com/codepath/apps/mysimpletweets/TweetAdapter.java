package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.utils.TimeUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jian_feng on 4/9/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.TweetViewHolder> {
    public class TweetViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvTime;

        public TweetViewHolder(View itemView) {
            super(itemView);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
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

        ImageView ivProfileImage = holder.ivProfileImage;
        TextView tvBody = holder.tvBody;
        TextView tvUserName = holder.tvUserName;
        TextView tvTime = holder.tvTime;

        ivProfileImage.setImageResource(0);
        Picasso.with(getContext()).load(tweet.getUser().profileImageUrl).into(ivProfileImage);
        tvBody.setText(tweet.getBody());
        tvTime.setText(TimeUtils.getRelativeTimeAgo(tweet.getCreateAt()));
        tvUserName.setText(tweet.getUser().name);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

}
