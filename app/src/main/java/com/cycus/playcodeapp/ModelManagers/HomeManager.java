package com.cycus.playcodeapp.ModelManagers;

import android.content.Context;
import android.util.Log;

import com.cycus.playcodeapp.EventBeans.HomeDataStatus;
import com.cycus.playcodeapp.ModelManagerInterfaces.IHomeManager;
import com.cycus.playcodeapp.SetterGetter.CategoryBean;
import com.cycus.playcodeapp.SetterGetter.GamesBean;
import com.cycus.playcodeapp.Utils.APIs;
import com.cycus.playcodeapp.Utils.Commons;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import de.greenrobot.event.EventBus;

/**
 * Created by Arun_Saini on 18-06-2016.
 */
public class HomeManager implements IHomeManager {
    Map<CategoryBean, ArrayList<GamesBean>> homeDate = new HashMap<CategoryBean, ArrayList<GamesBean>>();
    @Override
    public boolean checkHomeData(Context context) {
        if(homeDate.isEmpty() || homeDate==null) {
            getDataFromServer(context);
        }else
            notifyEvent(true);
        return true;
    }

    @Override
    public void getDataFromServer(Context context) {
        AsyncHttpClient httpClient= new AsyncHttpClient();
        httpClient.setConnectTimeout(20000);
        RequestParams params = new RequestParams();
        params.put("pid", 1);


        httpClient.post(context, APIs.BASE+APIs.HOME_API, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Map<CategoryBean, ArrayList<GamesBean>> hashMap= new HashMap<CategoryBean, ArrayList<GamesBean>>();
                try {
                    if(response.getString("error").equals("200")){
                        Log.i("JSON", response.toString());
                        JSONArray arrayCategories= response.getJSONArray("categories");
                        for(int i=0; i<arrayCategories.length(); i++){
                            CategoryBean catBean= new CategoryBean();
                            JSONObject ob= arrayCategories.getJSONObject(i);

                            catBean.setCatName(ob.getString("cat_name"));
                            catBean.setCatDesc(ob.getString("cat_desc"));
                            catBean.setCatOrder(Commons.stringToInt(ob.getString("cat_order")));
                            catBean.setCatId(Commons.stringToInt(ob.getString("cat_id")));
                            catBean.setIcon(ob.getString("cat_icon"));

                            JSONArray arrayGames= ob.getJSONArray("game");
                            ArrayList<GamesBean> gameBeans= new ArrayList<GamesBean>();
                            for(int j=0;j<arrayGames.length();j++){
                                GamesBean gameBean= new GamesBean();
                                JSONObject gamesObject= arrayGames.getJSONObject(j);
                                gameBean.setGamePrice(Commons.stringToFloat(gamesObject.getString("game_price")));
                                gameBean.setGameThumbnail(gamesObject.getString("game_thumbnail"));
                                gameBean.setGameTitle(gamesObject.getString("game_title"));
                                gameBean.setGameDesc(gamesObject.getString("game_desc"));
                                gameBean.setGameRating(Commons.stringToInt(gamesObject.getString("game_rating")));
                                gameBean.setGameShortDesc(gamesObject.getString("game_short_desc"));
                                gameBean.setGameId(Commons.stringToInt(gamesObject.getString("game_id")));
                                gameBean.setCatId(catBean.getCatId());
                                gameBean.setCatName(catBean.getCatName());

                                gameBeans.add(gameBean);
                            }
                            addHomeData(catBean, gameBeans);
                        }

                        notifyEvent(true);
                    }
                    if(response.getString("error").equals("201")){
                        Log.i("Data Error:", "Home Manager");
                        notifyEvent(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i("Failure: ", "Failed to retrieve data from server");
            }

        });
    }

    @Override
    public void addHomeData(CategoryBean catBean, ArrayList<GamesBean> gameBeans) {
        this.homeDate.put(catBean, gameBeans);
    }

    @Override
    public Map<CategoryBean, ArrayList<GamesBean>> getHomeDate() {
        return this.homeDate;
    }

    @Override
    public void notifyEvent(boolean status) {
        Log.i("DISPATCHING_EVENT", "HOMESTATUS");
        EventBus.getDefault().post(new HomeDataStatus(status));
    }
}
