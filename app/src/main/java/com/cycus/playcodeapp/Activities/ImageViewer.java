package com.cycus.playcodeapp.Activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cycus.playcodeapp.EventBeans.ConnectionStatus;
import com.cycus.playcodeapp.InitiateFabric;
import com.cycus.playcodeapp.ModelManagers.ModelManager;
import com.cycus.playcodeapp.R;
import com.cycus.playcodeapp.Utils.APIs;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Arun_Saini on 26-06-2016.
 */
public class ImageViewer extends AppCompatActivity {
    ImageView imageView, toolbarIcon;
    TextView connTV, toolbarName;
    ProgressBar progressBar;
    PhotoViewAttacher attacher;
    String url, gameName, gameThumbnail;
    LinearLayout llToolbar;
    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_viewer_layout);

        InitiateFabric application = (InitiateFabric) getApplication();
        tracker = application.getDefaultTracker();

        EventBus.getDefault().register(this);
        processExtra();
        initializeViews();
        createView(url);
    }

    public void initializeViews() {
        connTV = (TextView) findViewById(R.id.conn_status_image_viewer);
        imageView = (ImageView) findViewById(R.id.image_view_viewer);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_image_viewer);
        toolbarName= (TextView) findViewById(R.id.toolbar_name_image_viewer);
        toolbarIcon= (ImageView) findViewById(R.id.toolbar_icon_image_viewer);
        llToolbar= (LinearLayout) findViewById(R.id.ll_back_press_image_viewer);

        llToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ConnectivityManager conn = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if(networkInfo!=null){
            if(networkInfo.isConnected() && networkInfo.isAvailable()){
                connTV.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }else if(!networkInfo.isAvailable() && networkInfo.isConnected()){
                connTV.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }else{
            connTV.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Subscribe
    public void onEvent(ConnectionStatus event) {
        if (event.isConnected()) {
            Picasso.with(this).load(APIs.BASE_THUMBNAILS+url).into(imageView);
            Picasso.with(this).load(APIs.BASE_THUMBNAILS+gameThumbnail).into(toolbarIcon);
            connTV.setVisibility(View.INVISIBLE);
        } else {
            connTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processExtra();
    }

    public void createView(String url) {
        Picasso.with(this).load(APIs.BASE_THUMBNAILS+url).into(imageView);
        Picasso.with(this).load(APIs.BASE_THUMBNAILS+gameThumbnail).into(toolbarIcon);
        toolbarName.setText(gameName);
        attacher= new PhotoViewAttacher(imageView);
    }

    public void processExtra() {
        Intent intent = getIntent();
        if (intent.hasExtra("image_url") &&intent.hasExtra("game_name") &&intent.hasExtra("game_thumbnail") ) {
            url = intent.getStringExtra("image_url");
            gameName = intent.getStringExtra("game_name");
            gameThumbnail = intent.getStringExtra("game_thumbnail");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tracker.setScreenName("ImageViewerActivity");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
