package com.cycus.playcodeapp.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cycus.playcodeapp.DataStore.DataStore;
import com.cycus.playcodeapp.EventBeans.UserProfileStatus;
import com.cycus.playcodeapp.InitiateFabric;
import com.cycus.playcodeapp.R;
import com.cycus.playcodeapp.SetterGetter.UserFacebookBean;
import com.cycus.playcodeapp.SetterGetter.UserTwitterBean;
import com.cycus.playcodeapp.Utils.Commons;
import com.cycus.playcodeapp.Utils.ValidationCheck;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;

//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.common.Scopes;
//import com.google.android.gms.common.api.Scope;
//import com.twitter.sdk.android.core.TwitterAuthConfig;

/**
 * Created by Arun_Saini on 28-06-2016.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    LinearLayout facebookLayout, gmailLayout, skipLayout;
    CallbackManager callbackManager;
    LoginManager loginManager;
    String dataAvailableStatus;
    Intent intent;
    boolean firstLoad = false;
    GoogleApiClient googleApiClient;
    //    GoogleSignInOptions signInOptions;
    String chooseClient = "";
    final int RESPONSE_CODE = 100;
    final String googleServerClientId = "1036371047630-idi7jlrbd6q5gj2agsrspi17605g21id.apps.googleusercontent.com";
    final String appKey = "AIzaSyDVr51So_Jt0ON3Wu0fj-igRRkRXu6KcpA";
    final String twitterApiKey = "ku40nmZgQoCNFzjhB5Icd9ftq";
    final String twitterSecretKey = "Fo1juB1e64g7Up7DNtY2xAMAqKhY7CdltLPgCUiXhUUc2g3SQp";
    //    TwitterAuthClient twitterClient;
    ProgressBar loginProgress;
    Tracker tracker;
    TwitterLoginButton twitterBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.login_activity);
        Log.i("Skip_Layout", "login_onCreate()");

        InitiateFabric application = (InitiateFabric) getApplication();
        tracker = application.getDefaultTracker();

        loginProgress = (ProgressBar) findViewById(R.id.login_progress);
        loginProgress.setVisibility(View.INVISIBLE);
        loginProgress.getIndeterminateDrawable().setColorFilter(Color.parseColor("#0085BB"), PorterDuff.Mode.SRC_IN);

        //Twitter SetUp
//        twitterClient = new TwitterAuthClient();
        twitterBtn = new TwitterLoginButton(this);
        twitterBtn.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                twitterLogin(result);
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });

        //Facebook logger initialization
        AppEventsLogger.activateApp(this);

        //Google Api build up
//        signInOptions = new GoogleSignInOptions.
//                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
//                requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
//                .requestProfile().build();
//        googleApiClient = new GoogleApiClient.Builder(this).
//                enableAutoManage(this, this).
//                addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).
//                build();

        processIntent();
        initializations();
    }

    private void twitterLogin(Result<TwitterSession> result) {
        TwitterSession session = result.data;
        final String userName = session.getUserName();
        final String userId = Commons.longToString(session.getUserId());

        final UserTwitterBean bean = new UserTwitterBean();

        Twitter.getApiClient(session).getAccountService().verifyCredentials(true, false, new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                String profilePic = result.data.profileImageUrl;
                String fullName = result.data.name;
                String highResPic = "";
                if (profilePic.contains("_normal")) {
                    highResPic = profilePic.replace("_normal", "");
                }

                bean.setUserName(fullName);
                bean.setProfilePicture(highResPic);
                bean.setHashName(userName);
                bean.setId(userId);

                new DataStore(getBaseContext()).saveDataTwitter(bean);
                ValidationCheck.getInstance().setValid(true);
                EventBus.getDefault().post(new UserProfileStatus(true));

                logOutTwitter();
                clearCookies();
                if (firstLoad) {
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("DATA_AVAILABLE", dataAvailableStatus);
                    startActivity(intent);
                } else {
                    finish();
                }


                Log.i("Twitter Details", userName + "_" + userId + "_" + profilePic + "_" + fullName);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.i("TwitterException1", exception.toString());
            }
        });
    }


    private void loginManagerSetup() {
        List<String> permissions = Arrays.asList("user_photos", "email", "user_birthday", "user_friends");
        loginManager.logInWithReadPermissions(LoginActivity.this, permissions);
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginProgress.setVisibility(View.VISIBLE);
                final String token = loginResult.getAccessToken().toString();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("JSON_FACEBOOK", response.toString());
                        JSONObject jsonObject = response.getJSONObject();
                        UserFacebookBean bean = new UserFacebookBean();
                        if (jsonObject != null) {
                            try {
                                bean.setId(jsonObject.getString("id"));
                                bean.setName(jsonObject.getString("name"));
                                bean.setEmail(jsonObject.getString("email"));
                                bean.setGender(jsonObject.getString("gender"));
                                bean.setAccessToken(token);
                                bean.setProfile_pic("http://graph.facebook.com/" + jsonObject.getString("id") + "/picture?type=large");

                                new DataStore(getBaseContext()).saveDataFacebook(bean);
                                ValidationCheck.getInstance().setValid(true);
                                EventBus.getDefault().post(new UserProfileStatus(true));

                                logOutFacebook();
                                clearCookies();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (firstLoad) {
                                intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("DATA_AVAILABLE", dataAvailableStatus);
                                startActivity(intent);
                            } else {
                                finish();
                            }
                        } else {
                            Log.i("JsonNullException", "LoginActivity");
                        }
                    }
                });
                Bundle bundle = new Bundle();
                bundle.putString("fields", "id,name,email,gender");
                request.setParameters(bundle);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.i("LoginManager:onCancel", "Canceled");
                facebookLayout.setClickable(true);
                loginProgress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(FacebookException error) {
                Log.i("LoginManager:onError", error.toString());
                facebookLayout.setClickable(true);
                loginProgress.setVisibility(View.INVISIBLE);
            }
        });
    }

//    private void revokeFacebookPermission(){
//        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions", null, HttpMethod.DELETE, new GraphRequest.Callback() {
//            @Override
//            public void onCompleted(GraphResponse response) {
//                boolean isSuccess= false;
//                try {
//                    isSuccess = response.getJSONObject().getBoolean("success");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                if(isSuccess && response.getError()==null){
//                    Log.i("Facebook_Revoke", "Successful");
//                }
//            }
//        }).executeAsync();
//    }

//    private void twitterClientCallbacks() {
//        twitterClient.authorize(LoginActivity.this, new Callback<TwitterSession>() {
//
//            final UserTwitterBean bean = new UserTwitterBean();
//            @Override
//            public void success(Result<TwitterSession> result) {
//                loginProgress.setVisibility(View.VISIBLE);
//                final String twitterId = Commons.longToString(result.data.getUserId());
//                final String twitterName = result.data.getUserName();
//
//                TwitterApiClient apiClient = TwitterCore.getInstance().getApiClient();
//                apiClient.getAccountService().verifyCredentials(false, false, new Callback<User>() {
//                    @Override
//                    public void success(Result<User> result) {
//                        String highResPic= null;
//                        String profilePicture = result.data.profileImageUrl;
//                        if(profilePicture.contains("_normal")) {
//                            Log.i("FOUND_SUBSTRING", "_normal");
//                            highResPic = profilePicture.replace("_normal", "");
//                        }
//                        String fullName = result.data.name;
//
//                        bean.setUserName(fullName);
//                        bean.setProfilePicture(highResPic);
//                        bean.setHashName(twitterName);
//                        bean.setId(twitterId);
//
//                        new DataStore(getBaseContext()).saveDataTwitter(bean);
//                        ValidationCheck.getInstance().setValid(true);
//                        EventBus.getDefault().post(new UserProfileStatus(true));
//
//                        Log.i("TwitterData", highResPic + "_" + fullName + "_" + twitterId+"_"+twitterName);
//                        logOutTwitter();
//                        clearCookies();
//                        if (firstLoad) {
//                            intent = new Intent(LoginActivity.this, MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.putExtra("DATA_AVAILABLE", dataAvailableStatus);
//                            startActivity(intent);
//                        } else {
//                            finish();
//                        }
//                    }
//
//                    @Override
//                    public void failure(TwitterException exception) {
//                        Log.i("TwitterException1", exception.toString());
//                        gmailLayout.setClickable(true);
//                        loginProgress.setVisibility(View.INVISIBLE);
//                    }
//                });
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//                Log.i("TwitterException2", exception.toString());
//                gmailLayout.setClickable(true);
//                loginProgress.setVisibility(View.INVISIBLE);
//            }
//        });
//
//    }

//
//    private  void googleClientSetup(){
//        googleApiClient.connect();
//        googleSignIn();
//    }
//
//    //
//    private void googleSignIn() {
//        Intent googleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
//        if (googleSignInIntent == null)
//            Log.i("googleSignInIntent", "null");
//        startActivityForResult(googleSignInIntent, RESPONSE_CODE);
//    }

    private void initializations() {
        facebookLayout = (LinearLayout) findViewById(R.id.facebook_layout);
        gmailLayout = (LinearLayout) findViewById(R.id.gmail_layout);
        skipLayout = (LinearLayout) findViewById(R.id.skip_layout);

//        loginProgress.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        //Facebook Initialization
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();

        //Setup onClickListener
        facebookLayout.setOnClickListener(new ClickHandler());
        gmailLayout.setOnClickListener(new ClickHandler());
        skipLayout.setOnClickListener(new ClickHandler());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!chooseClient.equals("")) {
            if (chooseClient.equals("FACEBOOK")) {
                callbackManager.onActivityResult(requestCode, resultCode, data);
            } else if (chooseClient.equals("TWITTER")) {
//                twitterClient.onActivityResult(requestCode, resultCode, data);
                twitterBtn.onActivityResult(requestCode, resultCode, data);
//                if (requestCode == RESPONSE_CODE) {
//                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//                    handlerGoogleSignInResult(result);
//                } else {
//                    Log.i("RESPONSE_CODE", "is not 100");
//                }
            }
        }
    }

//    private boolean handlerGoogleSignInResult(GoogleSignInResult result) {
//        if (result != null) {
//            if (result.isSuccess()) {
//                final UserGoogleBean bean = new UserGoogleBean();
//                GoogleSignInAccount account = result.getSignInAccount();
//                bean.setId(account.getId());
//
//
//                bean.setDisplayName(account.getDisplayName());
//                bean.setEmail(account.getEmail());
//                if (account.getPhotoUrl() != null)
//                    bean.setProfilePicture(account.getPhotoUrl().toString());
//                Log.i("GoogleDetails", account.getId() + "_" + account.getDisplayName() + "_" + account.getEmail() + "_" + account.getPhotoUrl());
//
//                new DataStore(getBaseContext()).saveDataGoogle(bean);
//                ValidationCheck.getInstance().setValid(true);
//                EventBus.getDefault().post(new UserProfileStatus(true));
//
//                if (firstLoad) {
//                    intent = new Intent(LoginActivity.this, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra("DATA_AVAILABLE", dataAvailableStatus);
//                    startActivity(intent);
//                } else {
//                    finish();
//                }
//            }
//        } else {
//            Log.i("GoogleResult", "null");
//        }
//        return true;
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processIntent();
    }

    private void processIntent() {
        Intent mintent = getIntent();
        if (mintent.hasExtra("first_load")) {
            Log.i("FirstLoad", "yes");
            firstLoad = false;
        }
        if (mintent.hasExtra("FROM_SPLASH")) {
            firstLoad = true;
            if (mintent.hasExtra("DATA_AVAILABLE"))
                dataAvailableStatus = mintent.getStringExtra("DATA_AVAILABLE");
            Log.i("FirstLoad", "no");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class ClickHandler implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.facebook_layout:
                    makeToast();
                    facebookLayout.setClickable(false);
                    loginManagerSetup();
                    chooseClient = "FACEBOOK";
                    break;
                case R.id.gmail_layout:
                    makeToast();
                    gmailLayout.setClickable(false);
                    twitterBtn.performClick();
//                    twitterClientCallbacks();
                    chooseClient = "TWITTER";
                    break;
                case R.id.skip_layout:
                    if (firstLoad) {
                        Log.i("Skip_Layout", "firstLoad");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("DATA_AVAILABLE", dataAvailableStatus);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.i("Skip_Layout", "calling_finish");
                        finish();
                    }

                    break;

            }
        }
    }

    private boolean isConnected() {
        ConnectivityManager conn = (ConnectivityManager) getBaseContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        if (info != null) {
            if (info.isConnected() && info.isAvailable()) {
                return true;
            } else if (!info.isConnected() && info.isAvailable()) {
                return false;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void makeToast() {
        if (!isConnected())
            Toast.makeText(getBaseContext(), getResources().getString(R.string.connection_toast), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void logOutTwitter() {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (session != null) {
            Twitter.getSessionManager().clearActiveSession();
            Twitter.logOut();
        }
    }

    private void logOutFacebook() {
        LoginManager.getInstance().logOut();
    }

    private void clearCookies() {
        CookieSyncManager.createInstance(getBaseContext());
        android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            cookieManager.removeAllCookies(null);
        } else {
            cookieManager.removeAllCookie();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tracker.setScreenName("LoginActivity");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
