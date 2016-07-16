package com.cycus.playcodeapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cycus.playcodeapp.Adapter.RecyclerViewMenuList;
import com.cycus.playcodeapp.DataStore.DataStore;
import com.cycus.playcodeapp.EventBeans.ConnectionStatus;
import com.cycus.playcodeapp.EventBeans.HomeDataStatus;
import com.cycus.playcodeapp.Adapter.RecyclerAdapterGridsHome;
import com.cycus.playcodeapp.Adapter.RecyclerAdapterListsHome;
import com.cycus.playcodeapp.EventBeans.UserProfileStatus;
import com.cycus.playcodeapp.InitiateFabric;
import com.cycus.playcodeapp.Listeners.RecyclerOnClickListener;
import com.cycus.playcodeapp.ModelManagers.ModelManager;
import com.cycus.playcodeapp.R;
import com.cycus.playcodeapp.SetterGetter.CategoryBean;
import com.cycus.playcodeapp.SetterGetter.GamesBean;
import com.cycus.playcodeapp.SetterGetter.UserFacebookBean;
import com.cycus.playcodeapp.Utils.CustomTypeFaces;
import com.cycus.playcodeapp.Utils.DisplayDimension;
import com.cycus.playcodeapp.Utils.DynamicSizes;
import com.cycus.playcodeapp.Utils.FontOverride;
import com.cycus.playcodeapp.Utils.LinearLayoutManagerNoScroll;
import com.cycus.playcodeapp.Utils.RedirectionUrl;
import com.cycus.playcodeapp.Utils.SpaceItemDecor;
import com.cycus.playcodeapp.Utils.UserAccountTypes;
import com.cycus.playcodeapp.Utils.ValidationCheck;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    EventBus bus = EventBus.getDefault();
    GridView gridView;
    TextView connStatus, toolbarTV, navTitle, navSubTitle, navCatTV, navEmail, navAbout, navHelpNFeedback, navTNC, navFAQ;
    ProgressBar loadingBar;
    boolean isDataLoaded = false;
    NavigationView navigationView;
    LinearLayout rootLayout;
    Toolbar toolbar;
    Typeface roboRegular, roboLight, roboBold;
    ImageView userProfile;
    boolean userProfileLoading = false;
    Button signButton;
    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ContentView always follows facebook initialization
        setContentView(R.layout.activity_main);
        InitiateFabric application = (InitiateFabric) getApplication();
        tracker = application.getDefaultTracker();

        Log.i("Skip_Layout", "main_activity_onCreate()");

        loadingBar = (ProgressBar) findViewById(R.id.loading_bar);
        loadingBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#0085BB"), PorterDuff.Mode.SRC_IN);
//        MultiDex.install(this);
        if(!EventBus.getDefault().isRegistered(this)){
            bus.register(this);
        }
        String isValid = new DataStore(this).isValid();

        if(isValid.equals("")){
            ValidationCheck.getInstance().setValid(false);
        }else
            ValidationCheck.getInstance().setValid(true);

        overrideFonts();
        initializeViews();
        setTypeFaces();
    }

    public void overrideFonts() {
        try {
            FontOverride.setDefaultFont(this, "MONOSPACE", CustomTypeFaces.ROBO_REGULAR);
            FontOverride.setDefaultFont(this, "SANS", CustomTypeFaces.ROBO_REGULAR);
            FontOverride.setDefaultFont(this, "SERIF", CustomTypeFaces.ROBO_REGULAR);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setTypeFaces() {
        roboRegular = Typeface.createFromAsset(getAssets(), CustomTypeFaces.BASE_FOLDER + CustomTypeFaces.ROBO_REGULAR);
        roboLight = Typeface.createFromAsset(getAssets(), CustomTypeFaces.BASE_FOLDER + CustomTypeFaces.ROBO_LIGHT);
        roboBold = Typeface.createFromAsset(getAssets(), CustomTypeFaces.BASE_FOLDER + CustomTypeFaces.ROBO_BOLD);
        toolbarTV.setTypeface(roboRegular);
        navCatTV.setTypeface(roboRegular);
        navFAQ.setTypeface(roboRegular);
        navTNC.setTypeface(roboRegular);
        navHelpNFeedback.setTypeface(roboRegular);
        navAbout.setTypeface(roboRegular);

        signButton.setTypeface(roboRegular);

        navTitle.setTypeface(roboRegular);
        navEmail.setTypeface(roboRegular);
    }

    private void setUpUserProfile() {
        if (ValidationCheck.getInstance().isValid()) {
            navEmail.setVisibility(View.VISIBLE);
            navTitle.setVisibility(View.VISIBLE);
            DataStore store= new DataStore(getBaseContext());
            if(store.getUserAccountType().equals(UserAccountTypes.FACEBOOK)){
                navTitle.setText(store.getFacebookData().getName());
                navEmail.setText(store.getFacebookData().getEmail());
                new RedirectionUrl(new RedirectionUrl.AsyncResponse() {
                    @Override
                    public void processResponse(String result) {
                        Picasso.with(getBaseContext()).load(result).placeholder(R.drawable.default_user_profile).into(userProfile);
                    }

                }).execute(store.getFacebookData().getProfile_pic());
                userProfileLoading = true;
            }else if(store.getUserAccountType().equals(UserAccountTypes.TWITTER)){
                navTitle.setText(store.getTwitterData().getUserName());
                navEmail.setText("@"+store.getTwitterData().getHashName());
                if(store.getTwitterData().getProfilePicture()!=null && !store.getTwitterData().getProfilePicture().equals(""))
                    Picasso.with(getBaseContext()).load(store.getTwitterData().getProfilePicture()).placeholder(R.drawable.default_user_profile).into(userProfile);
                else
                    Picasso.with(getBaseContext()).load(R.drawable.default_user_profile).placeholder(R.drawable.default_user_profile).into(userProfile);
            }
            signButton.setText("Log Out");
        } else{
            navTitle.setText("Guest");
            navTitle.setVisibility(View.INVISIBLE);
            navEmail.setVisibility(View.INVISIBLE);
            signButton.setText("Log In");
            Picasso.with(getBaseContext()).load(R.drawable.default_user_profile).placeholder(R.drawable.default_user_profile).into(userProfile);
        }
    }

    public void initializeViews() {

        RelativeLayout rl= (RelativeLayout) findViewById(R.id.toolbar_layout);
        rl.bringToFront();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTV = (TextView) findViewById(R.id.toolbar_name_main);
        connStatus = (TextView) findViewById(R.id.conn_status);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        navigationView.setNavigationItemSelectedListener(this);

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navCatTV= (TextView) navView.findViewById(R.id.nav_cat_tv);
        navFAQ= (TextView) navView.findViewById(R.id.nav_faq);
        navTNC= (TextView) navView.findViewById(R.id.nav_tnc);
        navHelpNFeedback= (TextView) navView.findViewById(R.id.nav_help_feedback);
        navAbout= (TextView) navView.findViewById(R.id.nav_about);
        signButton = (Button) navView.findViewById(R.id.sign_button);

        if(ValidationCheck.getInstance().isValid())
            signButton.setText("Log Out");
        else
            signButton.setText("Log In");
        signButton.setOnClickListener(new ClickHandler());

        if (navView == null)
            Log.i("navView", "null");
        View header = navView.getHeaderView(0);
        navTitle = (TextView) header.findViewById(R.id.nav_title);
        navEmail = (TextView) header.findViewById(R.id.nav_title_email);
        userProfile = (ImageView) header.findViewById(R.id.user_profile_image);

        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (intent.hasExtra("DATA_AVAILABLE")) {
            if(intent.getStringExtra("DATA_AVAILABLE")!=null){
                String dataStatus = intent.getStringExtra("DATA_AVAILABLE");
                if (dataStatus.equals("SUCCESS")) {
                    loadingBar.setVisibility(View.INVISIBLE);
                    updateMenu();
                    createView();
                    isDataLoaded = true;
                }
            }
        }

        ConnectivityManager conn = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (conn.getActiveNetworkInfo().isAvailable() && conn.getActiveNetworkInfo().isConnected()) {
                connStatus.setVisibility(View.INVISIBLE);
                setUpUserProfile();
                if (isDataLoaded) {
                    loadingBar.setVisibility(View.INVISIBLE);
                } else {
                    ModelManager.getManager().getHomeManager().checkHomeData(this);
                    Log.i("REQUESTING_EVENT", "YES");
                }
            } else if (!conn.getActiveNetworkInfo().isAvailable() && conn.getActiveNetworkInfo().isConnected()) {
                connStatus.setVisibility(View.VISIBLE);
                loadingBar.setVisibility(View.INVISIBLE);
            }
        } else {
            connStatus.setVisibility(View.VISIBLE);
            loadingBar.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onEvent(ConnectionStatus event) {
        if (event.isConnected() && !isDataLoaded) {
            if (ValidationCheck.getInstance().isValid() && userProfileLoading == false)
                setUpUserProfile();
            loadingBar.setVisibility(View.VISIBLE);
            connStatus.setVisibility(View.INVISIBLE);
            ModelManager.getManager().getHomeManager().checkHomeData(this);
            if(ModelManager.getManager().getHomeManager().getHomeDate()==null)
                ModelManager.getManager().getHomeManager().checkHomeData(this);
//            fetchData();
        } else if (event.isConnected() && isDataLoaded) {
            if (ValidationCheck.getInstance().isValid() && userProfileLoading == false)
                setUpUserProfile();
            loadingBar.setVisibility(View.INVISIBLE);
            connStatus.setVisibility(View.INVISIBLE);
            if(ModelManager.getManager().getHomeManager().getHomeDate()==null)
                ModelManager.getManager().getHomeManager().checkHomeData(this);
        } else {
            connStatus.setVisibility(View.VISIBLE);
            loadingBar.setVisibility(View.INVISIBLE);
        }
    }

    @Subscribe
    public void onEvent(HomeDataStatus event) {
        if (event.isAvailable()) {
            Log.i("GOT_THE_EVENT", "HOMESTATUS");
            loadingBar.setVisibility(View.INVISIBLE);
            if(!isDataLoaded) {
                updateMenu();
                createView();
            }
            isDataLoaded = true;
        } else {
            Log.i("SERVER_FAILURE", "MAIN_ACTIVITY");
        }
    }

    @Subscribe
    public void onEvent(UserProfileStatus status) {
        if (status.isAvailable()) {
            if(!ValidationCheck.getInstance().isValid())
                ValidationCheck.getInstance().setValid(true);
            setUpUserProfile();
        }
        else{
            setUpUserProfile();
        }
    }

    public void updateMenu() {
        Map<CategoryBean, ArrayList<GamesBean>> data = ModelManager.getManager().getHomeManager().getHomeDate();
        final Object[] objArray = data.keySet().toArray();
        RecyclerView rv = (RecyclerView) findViewById(R.id.left_drawer);
        rv.setLayoutManager(new LinearLayoutManagerNoScroll(getBaseContext(), LinearLayoutManager.VERTICAL, false){
            @Override
            public void setScrollEnable(boolean flag) {
                super.setScrollEnable(false);
            }
        });
        rv.setAdapter(new RecyclerViewMenuList(getBaseContext(), objArray));
        rv.addOnItemTouchListener(new RecyclerOnClickListener(getBaseContext(), new RecyclerOnClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                CategoryBean categoryBean = (CategoryBean) objArray[position];
                intent.putExtra("cat_id", String.valueOf(categoryBean.getCatId()));
                intent.putExtra("cat_name", categoryBean.getCatName());
                intent.putExtra("from_main", "");
                Log.i("catIcon", categoryBean.getIcon());
                intent.putExtra("cat_icon", categoryBean.getIcon());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(intent);
            }
        }));
//        Map<CategoryBean, ArrayList<GamesBean>> data= ModelManager.getManager().getHomeManager().getHomeDate();
//        int mapSize= data.size();
//        Set<CategoryBean> keys= data.keySet();
//        Iterator it= keys.iterator();
//
//        Menu menu =navigationView.getMenu();
//        while(it.hasNext()){
//            CategoryBean bean= (CategoryBean)it.next();
//            menu.add(bean.getCatName()).setIcon(R.drawable.deadfly);
//        }

//        for(int i=0; i<5; i++){
////            menu.add("BLAH");
//            menu.add(R.id.group,Menu.NONE,Menu.NONE,"BLAH");
//            menu.getItem(i).setIcon(R.drawable.deadfly);
//        }
    }


    private void createView() {
        final Map<CategoryBean, ArrayList<GamesBean>> data = ModelManager.getManager().getHomeManager().getHomeDate();
        int mapSize = data.size();
        final Object[] catBeanArray = (Object[]) data.keySet().toArray();
        int RIListView = R.layout.list_view_home_base, RIGridView = R.layout.grid_view_home_base;
        int count = 1;

        for (int i = 0; i < mapSize; i++) {
//            if (i % 2 != 0) {
                if (!data.get(catBeanArray[i]).isEmpty()) {
//                    final int valI = i;
                    Log.i("CatDetails", catBeanArray[i].toString());
                    LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View mainView = inflater.inflate(RIGridView, null, false);

                    LinearLayout ll= (LinearLayout) mainView.findViewById(R.id.LL_for_lists);


                    TextView catName = (TextView) mainView.findViewById(R.id.cat_name);
                    catName.setTypeface(roboBold);
                    Button buttonViewAll = (Button) mainView.findViewById(R.id.view_all);
                    buttonViewAll.setTypeface(roboRegular);

                    final CategoryBean catBean = (CategoryBean) catBeanArray[i];
                    String catIcon = catBean.getIcon();
                    buttonViewAll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                            intent.putExtra("cat_id", String.valueOf(catBean.getCatId()));
                            intent.putExtra("cat_name", catBean.getCatName());
                            intent.putExtra("cat_icon", catBean.getIcon());
                            intent.putExtra("from_main", "");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getBaseContext().startActivity(intent);
                        }
                    });

//                    CategoryBean catBean = (CategoryBean) catBeanArray[i];
                    catName.setText(catBean.getCatName());

                    RecyclerView recycleView = (RecyclerView) mainView.findViewById(R.id.recycle_view);
                    recycleView.setLayoutManager(new GridLayoutManager(getBaseContext(), 1, GridLayoutManager.HORIZONTAL, false));

                    recycleView.setAdapter(new RecyclerAdapterGridsHome(getBaseContext(), data.get(catBeanArray[i]), MainActivity.this, catIcon));
                    recycleView.addItemDecoration(new SpaceItemDecor(18, recycleView.getAdapter().getItemCount(), "GRID", getBaseContext()));
                    final int temp = i;
//                    recycleView.addOnItemTouchListener(new RecyclerOnClickListener(getBaseContext(), new RecyclerOnClickListener.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(View view, int position) {
//                                    Log.i("ON_CLICK", "CALLED" + position);
//                                    Intent intent = new Intent(getBaseContext(), GameDescActivity.class);
//                                    intent.putExtra("cat_id", String.valueOf(data.get(catBeanArray[temp]).get(position).getCatId()));
//                                    intent.putExtra("game_id", String.valueOf(data.get(catBeanArray[temp]).get(position).getGameId()));
//                                    intent.putExtra("game_name", data.get(catBeanArray[temp]).get(position).getGameTitle());
//                                    intent.putExtra("game_price", String.valueOf(data.get(catBeanArray[temp]).get(position).getGamePrice()));
//                                    intent.putExtra("cat_name", data.get(catBeanArray[temp]).get(position).getCatName());
//                                    intent.putExtra("game_rating", String.valueOf(data.get(catBeanArray[temp]).get(position).getGameRating()));
//                                    intent.putExtra("game_desc", data.get(catBeanArray[temp]).get(position).getGameDesc());
//                                    intent.putExtra("game_thumbnail", data.get(catBeanArray[temp]).get(position).getGameThumbnail());
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    getBaseContext().startActivity(intent);
//                                }
//                            })
//                    );
                    rootLayout.addView(mainView, count++);
                }
//            }
//            else {
//                if (!data.get(catBeanArray[i]).isEmpty()) {
//                    final int valI = i;
//                    LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//                    View mainView = inflater.inflate(RIListView, null, false);
//
//                    TextView catName = (TextView) mainView.findViewById(R.id.cat_name);
//                    catName.setTypeface(roboBold);
//                    Button buttonViewAll = (Button) mainView.findViewById(R.id.view_all);
//                    buttonViewAll.setTypeface(roboRegular);
//                    buttonViewAll.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            CategoryBean catBean = (CategoryBean) catBeanArray[valI];
//                            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
//                            intent.putExtra("cat_id", String.valueOf(catBean.getCatId()));
//                            intent.putExtra("cat_name", catBean.getCatName());
//                            intent.putExtra("cat_icon", catBean.getIcon());
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            getBaseContext().startActivity(intent);
//                        }
//                    });
//
//                    CategoryBean catBean = (CategoryBean) catBeanArray[i];
//                    catName.setText(catBean.getCatName());
//
//                    RecyclerView recycleView = (RecyclerView) mainView.findViewById(R.id.recycle_view);
//                    recycleView.setLayoutManager(new LinearLayoutManagerNoScroll(getBaseContext(), LinearLayoutManager.VERTICAL, false) {
//                        @Override
//                        public void setScrollEnable(boolean flag) {
//                            super.setScrollEnable(false);
//                        }
//                    });
//                    recycleView.setAdapter(new RecyclerAdapterListsHome(getBaseContext(), data.get(catBeanArray[i])));
//
//                    recycleView.addItemDecoration(new SpaceItemDecor((int)new DynamicSizes().dpToPix(7), recycleView.getAdapter().getItemCount(), "LIST", getBaseContext()));
//                    final int temp = i;
//                    recycleView.addOnItemTouchListener(new RecyclerOnClickListener(getBaseContext(), new RecyclerOnClickListener.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(View view, int position) {
//                                    Log.i("ON_CLICK", "CALLED" + position);
//                                    Intent intent = new Intent(getBaseContext(), GameDescActivity.class);
//                                    intent.putExtra("cat_id", String.valueOf(data.get(catBeanArray[temp]).get(position).getCatId()));
//                                    intent.putExtra("game_id", String.valueOf(data.get(catBeanArray[temp]).get(position).getGameId()));
//                                    intent.putExtra("game_name", data.get(catBeanArray[temp]).get(position).getGameTitle());
//                                    intent.putExtra("game_price", String.valueOf(data.get(catBeanArray[temp]).get(position).getGamePrice()));
//                                    intent.putExtra("cat_name", data.get(catBeanArray[temp]).get(position).getCatName());
//                                    intent.putExtra("game_rating", String.valueOf(data.get(catBeanArray[temp]).get(position).getGameRating()));
//                                    intent.putExtra("game_desc", data.get(catBeanArray[temp]).get(position).getGameDesc());
//                                    intent.putExtra("game_thumbnail", data.get(catBeanArray[temp]).get(position).getGameThumbnail());
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    getBaseContext().startActivity(intent);
//                                }
//                            })
//                    );
//
//                    rootLayout.addView(mainView, count++);
//                }
//            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            System.exit(0);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        CharSequence menuTitle = item.getTitle();
        Map<CategoryBean, ArrayList<GamesBean>> map = ModelManager.getManager().getHomeManager().getHomeDate();
        Object[] objArray = map.keySet().toArray();
        for (int i = 0; i < objArray.length; i++) {
            CategoryBean categoryBean = (CategoryBean) objArray[i];
            if (categoryBean.getCatName().trim().toLowerCase().equals(menuTitle.toString().trim().toLowerCase())) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                intent.putExtra("cat_id", String.valueOf(categoryBean.getCatId()));
                intent.putExtra("cat_name", categoryBean.getCatName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(intent);
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        tracker.setScreenName("MainActivity");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        if(ModelManager.getManager().getHomeManager().getHomeDate()==null)
            ModelManager.getManager().getHomeManager().checkHomeData(this);
    }

    class ClickHandler implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.sign_button:
                    if(!ValidationCheck.getInstance().isValid()) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("first_load", "no");
                        startActivity(intent);
                    }else{
                        if(new DataStore(getBaseContext()).getUserAccountType().equals(UserAccountTypes.FACEBOOK)){
                            new DataStore(getBaseContext()).flushData();
                            ValidationCheck.getInstance().setValid(false);
                            EventBus.getDefault().post(new UserProfileStatus(false));
                        }else if(new DataStore(getBaseContext()).getUserAccountType().equals(UserAccountTypes.TWITTER)){
                            new DataStore(getBaseContext()).flushData();
                            ValidationCheck.getInstance().setValid(false);
                            EventBus.getDefault().post(new UserProfileStatus(false));
                        }
//                            new GraphRequest(AccessToken.getCurrentAccessToken() , "/me/permissions", null, HttpMethod.DELETE, new GraphRequest.Callback() {
//                                @Override
//                                public void onCompleted(GraphResponse response) {
//                                    boolean isSuccess= false;
////                                    try {
//                                        if(response!=null){
//                                            Log.i("FacebookResponse", response.getError().toString());
//                                        }
////                                        else
////                                            isSuccess = response.getJSONObject().getBoolean("success");
////                                    } ca/**/tch (JSONException e) {
////                                        e.printStackTrace();
////                                    }
//
//                                    if(isSuccess && response.getError()==null){
//                                        Log.i("Facebook_Revoke", "Successful");
//                                        new DataStore(getBaseContext()).flushData();
//                                        ValidationCheck.getInstance().setValid(false);
//                                        EventBus.getDefault().post(new UserProfileStatus(false));
//                                    }
//                                }
//                            }).executeAsync();
//                        }
                    }
                    break;
            }
        }
    }
}
