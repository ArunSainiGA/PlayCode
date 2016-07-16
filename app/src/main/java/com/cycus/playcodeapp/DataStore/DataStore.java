package com.cycus.playcodeapp.DataStore;

import android.content.Context;
import android.content.SharedPreferences;

import com.cycus.playcodeapp.SetterGetter.UserFacebookBean;
import com.cycus.playcodeapp.SetterGetter.UserGoogleBean;
import com.cycus.playcodeapp.SetterGetter.UserTwitterBean;
import com.cycus.playcodeapp.Utils.UserAccountTypes;

/**
 * Created by Arun_Saini on 28-06-2016.
 */
public class DataStore {
    Context context;
    SharedPreferences preferences;
    final String PREF_NAME = "PLAYCODE_PREF";
    int PREF_MODE = 0;
    SharedPreferences.Editor editor;

    public DataStore(Context context) {
        this.context = context;
        PREF_MODE = Context.MODE_PRIVATE;
        preferences = this.context.getSharedPreferences(PREF_NAME, PREF_MODE);
        editor = preferences.edit();
    }

    public void saveDataFacebook(UserFacebookBean bean) {
        if(bean!=null){
            editor.putString("fb_id", bean.getId());
            editor.putString("fb_name", bean.getName());
            editor.putString("fb_email", bean.getEmail());
            editor.putString("fb_gender", bean.getGender());
            editor.putString("fb_profile_pic", bean.getProfile_pic());
            editor.putString("fb_access_token", bean.getAccessToken());
            editor.putString("is_valid_user", "yes");
            editor.commit();
        }
    }

    public UserFacebookBean getFacebookData() {
        UserFacebookBean bean = new UserFacebookBean();
        bean.setId(preferences.getString("fb_id", ""));
        bean.setName(preferences.getString("fb_name", ""));
        bean.setEmail(preferences.getString("fb_email", ""));
        bean.setGender(preferences.getString("fb_gender", ""));
        bean.setProfile_pic(preferences.getString("fb_profile_pic", ""));
        bean.setAccessToken(preferences.getString("fb_access_token", ""));
        return bean;
    }

    public void flushData(){
        editor.clear();
        editor.commit();
    }

    public String isValid(){
        return preferences.getString("is_valid_user", "");
    }

    public void saveDataGoogle(UserGoogleBean bean){
        editor.putString("g_name", bean.getDisplayName());
        editor.putString("g_id", bean.getId());
        editor.putString("g_email", bean.getEmail());
        editor.putString("g_profile_pic", bean.getProfilePicture());
        editor.putString("is_valid_user", "yes" );
        editor.commit();
    }

    public String getUserAccountType(){
        String type="";
        if(!preferences.getString("is_valid_user", "").equals("")){
            if(!preferences.getString("fb_id", "").equals("")){
                type= UserAccountTypes.FACEBOOK;
            }else if(!preferences.getString("g_id", "").equals("")){
                type=UserAccountTypes.GOOGLE;
            }else if(!preferences.getString("twitter_id", "").equals("")){
                type=UserAccountTypes.TWITTER;
            }
        }
        return type;
    }

    public UserGoogleBean getGoogleData(){
        UserGoogleBean bean = new UserGoogleBean();
        bean.setId(preferences.getString("g_id", ""));
        bean.setEmail(preferences.getString("g_email", ""));
        bean.setDisplayName(preferences.getString("g_name", ""));
        bean.setProfilePicture(preferences.getString("g_profile_pic", ""));
        return bean;
    }

    public void saveDataTwitter(UserTwitterBean bean){
        editor.putString("twitter_name", bean.getUserName());
        editor.putString("twitter_profile_pic", bean.getProfilePicture());
        editor.putString("twitter_id", bean.getId());
        editor.putString("twitter_hash_name", bean.getHashName());
        editor.putString("is_valid_user", "yes" );
        editor.commit();
    }

    public UserTwitterBean getTwitterData(){
        UserTwitterBean bean = new UserTwitterBean();
        bean.setUserName(preferences.getString("twitter_name", ""));
        bean.setProfilePicture(preferences.getString("twitter_profile_pic", ""));
        bean.setId(preferences.getString("twitter_id", ""));
        bean.setHashName(preferences.getString("twitter_hash_name", ""));
        return bean;
    }

}
