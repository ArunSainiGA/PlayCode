package com.cycus.playcodeapp.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cycus.playcodeapp.Adapter.GridAdapterCategory;
import com.cycus.playcodeapp.EventBeans.CategoryStatus;
import com.cycus.playcodeapp.EventBeans.ConnectionStatus;
import com.cycus.playcodeapp.InitiateFabric;
import com.cycus.playcodeapp.ModelManagers.ModelManager;
import com.cycus.playcodeapp.R;
import com.cycus.playcodeapp.SetterGetter.GamesBean;
import com.cycus.playcodeapp.Utils.APIs;
import com.cycus.playcodeapp.Utils.Commons;
import com.cycus.playcodeapp.Utils.CustomTypeFaces;
import com.cycus.playcodeapp.Utils.DynamicSizes;
import com.cycus.playcodeapp.Utils.GridPerRow;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by Arun_Saini on 20-06-2016.
 */
public class CategoryActivity extends AppCompatActivity {
    EventBus bus = EventBus.getDefault();
    int catId;
    GridView gridView;
    TextView conStatus, catNameTV;
    ProgressBar progressBar, dataFetchProgressBar;
    boolean isDataLoaded=false;
    int offset=0;
    GridAdapterCategory gridAdapter;
    int gridCount;
    int spaceUnits;
    GridPerRow objGridPerRow;
    DynamicSizes objDynamicSizes;
    ImageView toolbarIcon;
    String catIcon;
    Tracker tracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_layout);

        InitiateFabric application = (InitiateFabric) getApplication();
        tracker = application.getDefaultTracker();

        progressBar= (ProgressBar) findViewById(R.id.progress_bar);
        dataFetchProgressBar= (ProgressBar) findViewById(R.id.fetch_data_progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#0085BB"), PorterDuff.Mode.SRC_IN);
        dataFetchProgressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#0085BB"), PorterDuff.Mode.SRC_IN);

        initializeViews();
        setTypeFaces();
        bus.register(this);

        processExtra();
        objGridPerRow= new GridPerRow();
        gridCount= objGridPerRow.getGridCount();
        spaceUnits= objGridPerRow.spaceUnitsCount(gridCount);
        objDynamicSizes= new DynamicSizes();
    }

    public void setTypeFaces(){
        catNameTV.setTypeface(Typeface.createFromAsset(getAssets(), CustomTypeFaces.BASE_FOLDER+CustomTypeFaces.ROBO_REGULAR));
    }

    public void initializeViews(){
        ImageView backButton= (ImageView) findViewById(R.id.back_button);
        LinearLayout llBackPress= (LinearLayout) findViewById(R.id.ll_back_press_cat);

        llBackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        conStatus = (TextView) findViewById(R.id.conn_status);

        toolbarIcon= (ImageView) findViewById(R.id.cat_toolbar_icon);
        dataFetchProgressBar.setVisibility(View.INVISIBLE);



        catNameTV= (TextView) findViewById(R.id.category_name);

        gridView= (GridView) findViewById(R.id.grid_view);

        ConnectivityManager conn= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info= conn.getActiveNetworkInfo();
        if(info!=null){
            if(info.isConnected() && info.isAvailable()){
                conStatus.setVisibility(View.INVISIBLE);
                if(isDataLoaded)
                    progressBar.setVisibility(View.INVISIBLE);
            }else if(info.isConnected() && !info.isAvailable()){
                conStatus.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }else{
            conStatus.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
    public void createView() {
        if(ModelManager.getManager().getCategoryManager().isMoreDataAvailable()){
            ArrayList<GamesBean> arrayList=null;
            if(gridAdapter==null){
                arrayList = ModelManager.getManager().getCategoryManager().getCategoryData();
                if(arrayList!=null){
                    gridAdapter= new GridAdapterCategory(getBaseContext(), arrayList, catIcon, CategoryActivity.this);
                }else
                    Log.i("NULL_FOUND", "Category_activity");

                gridView.setAdapter(gridAdapter);
//                gridView.setPadding(objDynamicSizes.getGridPadding(), objDynamicSizes.getGridPadding(), objDynamicSizes.getGridPadding() ,objDynamicSizes.getGridPadding());
//                gridView.setNumColumns(objGridPerRow.getGridCount());
//                gridView.setHorizontalSpacing(objDynamicSizes.getGridPadding());
//                gridView.setVerticalSpacing(objDynamicSizes.getGridPadding());
                ModelManager.getManager().getCategoryManager().flushData();
            }else{
                gridAdapter.addAll(ModelManager.getManager().getCategoryManager().getCategoryData());
                ModelManager.getManager().getCategoryManager().flushData();
            }
        }
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int totalItemCount, firstVisibleItem, visibleItemCount;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState==SCROLL_STATE_IDLE ){
                    initiateFetch();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.totalItemCount=totalItemCount;
                this.firstVisibleItem=firstVisibleItem;
                this.visibleItemCount=visibleItemCount;
            }
            public void initiateFetch(){
                if((firstVisibleItem+visibleItemCount)==totalItemCount && !dataFetchProgressBar.isShown()){
                    offset= totalItemCount;
                    ModelManager.getManager().getCategoryManager().checkCategoryData(getBaseContext(), catId, offset);
                    dataFetchProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Subscribe
    public void onEvent(ConnectionStatus event){
        if(event.isConnected() && isDataLoaded){
            conStatus.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            ModelManager.getManager().getCategoryManager().checkCategoryData(getBaseContext(), catId, offset);
        }else if(event.isConnected() && !isDataLoaded){
            conStatus.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            ModelManager.getManager().getCategoryManager().checkCategoryData(getBaseContext(), catId, offset);
        }else{
            conStatus.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Subscribe
    public void onEvent(CategoryStatus event) {
        if (event.isAvailable()) {
            createView();
            isDataLoaded=true;
            progressBar.setVisibility(View.INVISIBLE);
            dataFetchProgressBar.setVisibility(View.INVISIBLE);
        }else{
            Log.i("SERVER_FAILURE", "CATEGORY_ACTIVITY");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
//        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processExtra();
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
////        finish();
//    }

    private void processExtra(){
        Intent intent = getIntent();
        if (intent.hasExtra("cat_id")&& intent.hasExtra("cat_name") && intent.hasExtra("cat_icon")) {
            catId = Commons.stringToInt(intent.getStringExtra("cat_id"));
            catNameTV.setText(intent.getStringExtra("cat_name"));
            catIcon= intent.getStringExtra("cat_icon");
            Picasso.with(getBaseContext()).load(APIs.CATEGORY_ICON+intent.getStringExtra("cat_icon")).placeholder(R.drawable.small_logo_playcode).into(toolbarIcon);
        }else
            catId = 0;
        if(intent.hasExtra("from_main")) {
            offset = 0;
            ModelManager.getManager().getCategoryManager().checkCategoryData(getBaseContext(), catId, offset);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        isDataLoaded=false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        tracker.setScreenName("SplashActivity");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}

