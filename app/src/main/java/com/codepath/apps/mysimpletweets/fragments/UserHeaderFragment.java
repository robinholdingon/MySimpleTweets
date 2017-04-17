package com.codepath.apps.mysimpletweets.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.ProfileActivity;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by jian_feng on 4/16/17.
 */

public class UserHeaderFragment extends Fragment{
    public static UserHeaderFragment getInstance(Parcelable userParcelable) {
        UserHeaderFragment userHeaderFragment = new UserHeaderFragment();
        Bundle args = new Bundle();
        args.putParcelable(ProfileActivity.USER_KEY, userParcelable);
        userHeaderFragment.setArguments(args);
        return userHeaderFragment;
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    private User user;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        user = Parcels.unwrap(getArguments().getParcelable(ProfileActivity.USER_KEY));
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        populateProfileHeader(v);
        return v;
    }

    private void populateProfileHeader(View v) {
        TextView tvName = (TextView) v.findViewById(R.id.tv_name);
        TextView tvScreenName = (TextView) v.findViewById(R.id.tv_screen_name);
        ImageView ivProfileImage = (ImageView) v.findViewById(R.id.iv_profile_img);
        TextView tvDescription = (TextView) v.findViewById(R.id.tv_description);
        TextView tvFollowing = (TextView) v.findViewById(R.id.tv_following);
        TextView tvFollower = (TextView) v.findViewById(R.id.tv_followers);

        tvName.setText(user.getName());
        tvScreenName.setText(user.getScreenName());
        tvDescription.setText(user.getDescription());
        tvFollower.setText(Integer.toString(user.getFollower()) + " Followers");
        tvFollowing.setText(Integer.toString(user.getFollowing()) + " Following");
        ivProfileImage.setImageResource(0);
        if ( !TextUtils.isEmpty(user.getProfileImageUrl()) ) {
            Picasso.with(mContext).load(user.getProfileImageUrl())
                    .transform(new RoundedCornersTransformation(10,0)).fit().into(ivProfileImage);
        }
    }
}
