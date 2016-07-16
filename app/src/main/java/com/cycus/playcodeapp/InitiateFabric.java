package com.cycus.playcodeapp;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Arun_Saini on 06-07-2016.
 */
public class InitiateFabric extends Application {
    private Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        TwitterAuthConfig config = new TwitterAuthConfig(getResources().getString(R.string.twitter_api_key), getResources().getString(R.string.twitter_secret_key));
        Fabric.with(this, new Twitter(config));
        Log.i("FABRIC_INITIALIZATION", "done");
    }

    synchronized public Tracker getDefaultTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//            analytics.setLocalDispatchPeriod(1);
            tracker = analytics.newTracker(R.xml.app_tracker);
        }
        return tracker;
    }
}
