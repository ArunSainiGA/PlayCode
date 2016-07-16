package com.cycus.playcodeapp.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cycus.playcodeapp.Adapter.GridAdapterGameDescription;
import com.cycus.playcodeapp.Adapter.VPAdapterDash;
import com.cycus.playcodeapp.EventBeans.ConnectionStatus;
import com.cycus.playcodeapp.EventBeans.GameDescGridsStatus;
import com.cycus.playcodeapp.EventBeans.GameDescriptionBean;
import com.cycus.playcodeapp.EventBeans.NotifyPopupClose;
import com.cycus.playcodeapp.InitiateFabric;
import com.cycus.playcodeapp.ModelManagers.ModelManager;
import com.cycus.playcodeapp.R;
import com.cycus.playcodeapp.SetterGetter.GamesBean;
import com.cycus.playcodeapp.Utils.APIs;
import com.cycus.playcodeapp.Utils.Commons;
import com.cycus.playcodeapp.Utils.CustomTypeFaces;
import com.cycus.playcodeapp.Utils.DownloadContent;
import com.cycus.playcodeapp.Utils.DynamicSizes;
import com.cycus.playcodeapp.Utils.RollPagerView;
import com.cycus.playcodeapp.Utils.ValidationCheck;
import com.facebook.FacebookSdk;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by Arun_Saini on 16-06-2016.
 */
public class GameDescActivity extends AppCompatActivity {
    int height, catId, gameId;
    TextView connStatusTV, gameDescriptionTV, gameCategoryTV, gameNameTV, gameNameToolbar, gamePriceTV;
    RatingBar ratingBar;
    ProgressBar progressBar, progressBarCategory;
    boolean isDataAvailable = false, isCategoryDataAvailable = false;
    RollPagerView vp;
    ImageView iconToolbar, parallexedThumbnail;
    EventBus bus = EventBus.getDefault();
    String gameName, gamePrice, catName, gameRating, gameThumbnail, gameDesc;
    LinearLayout llBackButton;
    Typeface roboRegular, roboLight, roboBold;
    RelativeLayout rootLayout;
    GridView grids;
    DynamicSizes sizes;
    int offset = 0;
    Button viewAll;
    Button downloadButton;
    String catIcon;
    boolean initializedViews= false;
    String fromMain=null;
    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.game_desc_layout);

        InitiateFabric application = (InitiateFabric) getApplication();
        tracker = application.getDefaultTracker();

        bus.register(this);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_gamedesc);
        progressBarCategory = (ProgressBar) findViewById(R.id.progress_bar_category_gamedesc);
        processExtras();
        initializeViews();
        setTypeFaces();
    }

    public void setTypeFaces() {
        roboBold = Typeface.createFromAsset(getAssets(), CustomTypeFaces.BASE_FOLDER + CustomTypeFaces.ROBO_BOLD);
        roboLight = Typeface.createFromAsset(getAssets(), CustomTypeFaces.BASE_FOLDER + CustomTypeFaces.ROBO_LIGHT);
        roboRegular = Typeface.createFromAsset(getAssets(), CustomTypeFaces.BASE_FOLDER + CustomTypeFaces.ROBO_REGULAR);
        connStatusTV.setTypeface(roboLight);
        gameDescriptionTV.setTypeface(roboLight);
        gameNameTV.setTypeface(roboRegular);
        gameCategoryTV.setTypeface(roboRegular);
        gamePriceTV.setTypeface(roboLight);
        gameNameToolbar.setTypeface(roboRegular);
    }

    public void initializeViews() {
        connStatusTV = (TextView) findViewById(R.id.conn_status_gamedesc);
        gameDescriptionTV = (TextView) findViewById(R.id.game_desc_gamedesc);
        gameCategoryTV = (TextView) findViewById(R.id.game_desc_cat);
        gameNameTV = (TextView) findViewById(R.id.game_name_gamedesc);
        gameNameToolbar = (TextView) findViewById(R.id.game_name_gamedesc_toolbar);
        gamePriceTV = (TextView) findViewById(R.id.game_desc_price);

        iconToolbar = (ImageView) findViewById(R.id.toolbar_icon_gamedesc);
        parallexedThumbnail = (ImageView) findViewById(R.id.parallaxed_image);
        vp = (RollPagerView) findViewById(R.id.vp_dash);
        ratingBar = (RatingBar) findViewById(R.id.game_rating_gamedesc);
        downloadButton = (Button) findViewById(R.id.download_image_gd);
        llBackButton = (LinearLayout) findViewById(R.id.ll_back_press);
        rootLayout = (RelativeLayout) findViewById(R.id.root_game_desc);
        grids = (GridView) findViewById(R.id.grids_game_desc);

        sizes = new DynamicSizes();
        viewAll = (Button) findViewById(R.id.view_all_desc);

        rootLayout.setOnClickListener(new ClickEventHandler());
        llBackButton.setOnClickListener(new ClickEventHandler());
        downloadButton.setOnClickListener(new ClickEventHandler());
        viewAll.setOnClickListener(new ClickEventHandler());


        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#0085BB"), PorterDuff.Mode.SRC_IN);
        progressBarCategory.getIndeterminateDrawable().setColorFilter(Color.parseColor("#0085BB"), PorterDuff.Mode.SRC_IN);


        gameDescriptionTV.setText(gameDesc);
        gameCategoryTV.setText(catName);
        gameNameTV.setText(gameName);
        gameNameToolbar.setText(gameName);
        gamePriceTV.setText(gamePrice);
        Picasso.with(getBaseContext()).load(APIs.BASE_THUMBNAILS + gameThumbnail).into(iconToolbar);
        Picasso.with(getBaseContext()).load(APIs.BASE_THUMBNAILS + gameThumbnail).into(parallexedThumbnail);
        ratingBar.setRating(Commons.stringToFloat(gameRating));

        ConnectivityManager conn = (ConnectivityManager) getBaseContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        if (info != null) {
            if (info.isAvailable() && info.isConnected()) {
                connStatusTV.setVisibility(View.INVISIBLE);
                if (isDataAvailable) {
                    progressBar.setVisibility(View.INVISIBLE);
                } else
                    progressBar.setVisibility(View.VISIBLE);
                if (isCategoryDataAvailable) {
                    progressBarCategory.setVisibility(View.INVISIBLE);
                } else
                    progressBarCategory.setVisibility(View.VISIBLE);

            } else if (!info.isAvailable() && info.isConnected()) {
                connStatusTV.setVisibility(View.VISIBLE);
            }
        } else {
            connStatusTV.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            progressBarCategory.setVisibility(View.INVISIBLE);
        }
        initializedViews= true;
    }

    public void createView() {
        GamesBean gamesBean = ModelManager.getManager().getGameDescriptionManager().getData();
        String[] screens = {gamesBean.getScreen1(), gamesBean.getScreen2(), gamesBean.getScreen3()};
        vp.setAdapter(new VPAdapterDash(getBaseContext(), screens, gameName, gameThumbnail));
        vp.setHintView(new ColorPointHintView(this, Color.parseColor("#0A7086"), Color.parseColor("#E6E7E9")));
    }

    public void createGrids() {
        if (ModelManager.getManager().getCategoryManager().isMoreDataAvailable()) {
            Log.i("CreateGrids", "YES");
            ArrayList<GamesBean> arrayList = ModelManager.getManager().getCategoryManagerGameDesc().getCategoryData();
            Log.i("ARRAY_SIZE_GAME_DESC", arrayList.size() + "_");
            GridAdapterGameDescription gridAdapter = null;
            ModelManager.getManager().getCategoryManagerGameDesc().flushData();
//            if (grids.getAdapter() == null) {
                gridAdapter = new GridAdapterGameDescription(getBaseContext(), arrayList, this, catIcon, fromMain);
                grids.setAdapter(gridAdapter);
//                grids.setPadding(sizes.getGridPadding(), 0, sizes.getGridPadding(), sizes.getGridPadding());
//                grids.setHorizontalSpacing(sizes.getGridPadding());
//                grids.setVerticalSpacing(sizes.getGridPadding());
//                grids.setNumColumns(new GridPerRow().getGridCount());
//            }
//            else
//                gridAdapter.addAll(arrayList);
        }

    }

    @Subscribe
    public void onEvent(GameDescriptionBean event) {
        if (event.isAvailable()) {
            isDataAvailable = true;
            progressBar.setVisibility(View.INVISIBLE);
            createView();
        } else {
            Log.i("SERVER_FAILURE", "GAME_DESC_ACTIVITY");
        }
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        int orientation = newConfig.orientation;
//        createView();
//    }


    @Subscribe
    public void onEvent(ConnectionStatus event) {
        if (event.isConnected()) {
            connStatusTV.setVisibility(View.INVISIBLE);
            if (isDataAvailable) {
                progressBar.setVisibility(View.INVISIBLE);
                    ModelManager.getManager().getGameDescriptionManager().checkData(getBaseContext(), gameId, catId);
                    ModelManager.getManager().getCategoryManagerGameDesc().checkCategoryData(getBaseContext(), catId, offset);
            } else
                progressBar.setVisibility(View.VISIBLE);
                ModelManager.getManager().getGameDescriptionManager().checkData(getBaseContext(), gameId, catId);
                ModelManager.getManager().getCategoryManagerGameDesc().checkCategoryData(getBaseContext(), catId, offset);
        } else {
            connStatusTV.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Subscribe
    public void onEvent(GameDescGridsStatus event) {
        if (event.isAvailable()) {
            Log.i("GotGameDesc", "Status Success");
            isCategoryDataAvailable = true;
            progressBarCategory.setVisibility(View.INVISIBLE);
            createGrids();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processExtras();
    }

    private void processExtras() {
        Intent intent = getIntent();
        if (intent.hasExtra("cat_id") && intent.hasExtra("game_id")) {
            catId = Commons.stringToInt(intent.getStringExtra("cat_id"));
            gameId = Commons.stringToInt(intent.getStringExtra("game_id"));
        }
        Log.i("cat_icon_gd", intent.getStringExtra("cat_icon"));
        gameName = intent.getStringExtra("game_name");
        gamePrice = Commons.stringToFloat(intent.getStringExtra("game_price")) == 0.0 ? "Free" : intent.getStringExtra("game_price");
        catName = intent.getStringExtra("cat_name");
        catIcon = intent.getStringExtra("cat_icon");
        gameRating = intent.getStringExtra("game_rating");
        gameThumbnail = intent.getStringExtra("game_thumbnail");
        gameDesc = intent.getStringExtra("game_desc");
        if(intent.hasExtra("from_main")){
            fromMain= "TRUE";
        }

        if(initializedViews){
            gameDescriptionTV.setText(gameDesc);
            gameCategoryTV.setText(catName);
            gameNameTV.setText(gameName);
            gameNameToolbar.setText(gameName);
            gamePriceTV.setText(gamePrice);
            Picasso.with(getBaseContext()).load(APIs.BASE_THUMBNAILS + gameThumbnail).into(iconToolbar);
            Picasso.with(getBaseContext()).load(APIs.BASE_THUMBNAILS + gameThumbnail).into(parallexedThumbnail);
            ratingBar.setRating(Commons.stringToFloat(gameRating));
        }

        ModelManager.getManager().getGameDescriptionManager().checkData(getBaseContext(), gameId, catId);
        ModelManager.getManager().getCategoryManagerGameDesc().checkCategoryData(getBaseContext(), catId, offset);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    class ClickEventHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.download_image_gd:
                    if (ValidationCheck.getInstance().isValid()) {
                        new DownloadContent().initiateDownload(getBaseContext(), catId, gameId, gameName);
                    } else {
                        Intent intent = new Intent(GameDescActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("first_load", "no");
                        startActivity(intent);
                    }
                    break;

                case R.id.ll_back_press:
                    finish();
                    break;

                case R.id.root_game_desc:
                    EventBus.getDefault().post(new NotifyPopupClose(true));
                    break;

                case R.id.view_all_desc:
                    Intent intent = new Intent(GameDescActivity.this, CategoryActivity.class);
                    intent.putExtra("cat_id", String.valueOf(catId));
                    intent.putExtra("cat_name", String.valueOf(catName));
                    intent.putExtra("cat_icon", catIcon);
                    if(fromMain!=null)
                        intent.putExtra("from_main", "");
                    else
                        intent.putExtra("from_game_desc", "");
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(intent);
                    break;
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        tracker.setScreenName("GameDescActivity");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

}
