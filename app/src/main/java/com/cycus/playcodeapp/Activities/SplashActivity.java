package com.cycus.playcodeapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.cycus.playcodeapp.DataStore.DataStore;
import com.cycus.playcodeapp.EventBeans.HomeDataStatus;
import com.cycus.playcodeapp.InitiateFabric;
import com.cycus.playcodeapp.ModelManagers.ModelManager;
import com.cycus.playcodeapp.R;
import com.cycus.playcodeapp.SetterGetter.UserFacebookBean;
import com.cycus.playcodeapp.Utils.DisplayDimension;
import com.cycus.playcodeapp.Utils.TestException;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.FirebaseException;
import com.google.firebase.crash.FirebaseCrash;

import junit.framework.Test;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Arun_Saini on 19-06-2016.
 */

public class SplashActivity extends AppCompatActivity {

    EventBus bus = EventBus.getDefault();
    Intent intent;
    Tracker tracker;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        getDisplaySize();

//        FirebaseCrash.report(new FirebaseException("FireBase Exception..."));
//        try{
//            int x =5/0;
//        }catch(TestException ex){
//            new TestException(ex.getMessage());
//        }

        InitiateFabric application = (InitiateFabric) getApplication();
        tracker = application.getDefaultTracker();
        String isValid = new DataStore(this).isValid();

        if (isValid.equals(""))
            intent = new Intent(this, LoginActivity.class);
        else
            intent = new Intent(this, MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("FROM_SPLASH", "YES");

        ModelManager.getManager().getHomeManager().checkHomeData(this);

//        bus.register(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getBaseContext().startActivity(intent);
                finish();
            }
        }, 3000);


    }


    public void getDisplaySize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float divb = displayMetrics.densityDpi / 160;
        DisplayDimension.getInstance().setHeightInPixels(displayMetrics.heightPixels);
        DisplayDimension.getInstance().setWidthInPixels(displayMetrics.widthPixels);
        DisplayDimension.getInstance().setHeightInDP(displayMetrics.heightPixels / (int) divb);
        DisplayDimension.getInstance().setWidthInDP(displayMetrics.widthPixels / (int) divb);
        DisplayDimension.getInstance().setDip(displayMetrics.densityDpi);
        Log.i("DD: ", DisplayDimension.getInstance().getDip()+"");
        Log.i("HEIGHT_WIDTH_PX", DisplayDimension.getInstance().getHeightInPixels() + "_" + DisplayDimension.getInstance().getWidthInPixels());
        Log.i("HEIGHT_WIDTH_DP", DisplayDimension.getInstance().getHeightInDP() + "_" + DisplayDimension.getInstance().getWidthInDP());
        Log.i("HEIGHT_WIDTH_DP", DisplayDimension.getInstance().getHeightInDP() + "_" + DisplayDimension.getInstance().getWidthInDP());
    }
//
//    @Subscribe
//    public void onEvent(HomeDataStatus event) {
//        if (event.isAvailable()) {
//            initiateMainActivity();
//        }
//    }

    private void initiateMainActivity() {
        intent.putExtra("DATA_AVAILABLE", "SUCCESS");
        getBaseContext().startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        bus.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tracker.setScreenName("SplashActivity");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
//        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }
}
