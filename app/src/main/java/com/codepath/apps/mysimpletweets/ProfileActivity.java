package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.mysimpletweets.fragments.UserHeaderFragment;
import com.codepath.apps.mysimpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweets.models.User;

import org.parceler.Parcels;

public class ProfileActivity extends AppCompatActivity {
    public static String USER_KEY = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Parcelable userParcelable = getIntent().getParcelableExtra(USER_KEY);
        User user = Parcels.unwrap(userParcelable);

        getSupportActionBar().setTitle(user.screenName);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            UserHeaderFragment userHeaderFragment = UserHeaderFragment.getInstance(userParcelable);
            fragmentTransaction.replace(R.id.rl_user_header, userHeaderFragment);

            UserTimelineFragment userTimelineFragment = UserTimelineFragment.getInstance(user.screenName);
            fragmentTransaction.replace(R.id.fl_container, userTimelineFragment);
            fragmentTransaction.commit();
        }
    }
}
