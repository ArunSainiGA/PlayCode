package com.cycus.playcodeapp.ModelManagers;

import android.content.Context;
import android.util.Log;

import com.cycus.playcodeapp.EventBeans.CategoryStatus;
import com.cycus.playcodeapp.EventBeans.GameDescGridsStatus;
import com.cycus.playcodeapp.ModelManagerInterfaces.ICategoryManager;
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

import cz.msebera.android.httpclient.Header;
import de.greenrobot.event.EventBus;

/**
 * Created by Arun_Saini on 21-06-2016.
 */
public class CategoryManagerGameDesc implements ICategoryManager {
    ArrayList<GamesBean> gamesBeanArrayList= new ArrayList<GamesBean>();
    int catId;
    int offset;
    boolean moreDataAvailable=true;

    @Override
    public void checkCategoryData(Context context, int catId, int offset) {
        this.catId= catId;
        this.offset=offset;

        if(gamesBeanArrayList==null || gamesBeanArrayList.isEmpty())
            getDataFromServer(context);
        else
            notifyEvent(true);
//        else if((gamesBeanArrayList!=null || !gamesBeanArrayList.isEmpty()) && gamesBeanArrayList.get(0).getGameId()==catId)
//            notifyEvent(true);
//        else if((gamesBeanArrayList!=null || !gamesBeanArrayList.isEmpty()) && gamesBeanArrayList.get(0).getGameId()!=catId)
//            getDataFromServer(context);
    }

    @Override
    public ArrayList<GamesBean> getCategoryData() {
        Log.i("getCategoryData()", gamesBeanArrayList.size()+"");
        return this.gamesBeanArrayList;
    }

    @Override
    public void notifyEvent(boolean status) {
        EventBus.getDefault().post(new GameDescGridsStatus(status));
    }

    @Override
    public void addCategory(GamesBean gamesBean) {
        this.gamesBeanArrayList.add(gamesBean);
    }

    @Override
    public void getDataFromServer(Context context) {
        AsyncHttpClient httpClient= new AsyncHttpClient();
        httpClient.setConnectTimeout(3000);
        RequestParams params= new RequestParams();
        params.put("cid", catId);
        params.put("pid", 1);
        params.put("limit", 10);
        params.put("offset", offset);

        httpClient.post(context, APIs.BASE+APIs.CATEGORY_API, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    if( response.getString("error").equals("203") ||  response.getString("error").equals("200")){
                        Log.i("JSON_CATEGORY", response.toString());
                        JSONArray gamesArray= response.getJSONArray("games");
                        if(response.getString("error").equals("200")||response.getString("error").equals("203")){
                            for(int i=0; i<gamesArray.length(); i++){
                                Log.i("ARRAY_SIZE", gamesArray.length()+"");
                                JSONObject gameDetails= gamesArray.getJSONObject(i);
                                GamesBean bean = new GamesBean();
                                bean.setCatName(response.getString("cat_name"));
                                bean.setCatId(Commons.stringToInt(response.getString("cat_id")));
                                bean.setGamePrice(Commons.stringToFloat(gameDetails.getString("game_price")));
                                bean.setGameThumbnail(gameDetails.getString("game_thumbnail"));
                                bean.setGameTitle(gameDetails.getString("game_title"));
                                bean.setGameDesc(gameDetails.getString("game_desc"));
                                bean.setGameRating(Commons.stringToInt(gameDetails.getString("game_rating")));
                                bean.setGameShortDesc(gameDetails.getString("game_short_desc"));
                                bean.setGameId(Commons.stringToInt(gameDetails.getString("game_id")));
                                addCategory(bean);
                            }
                            notifyEvent(true);
                        }
                    }else if(response.getString("error").equals("201")){
                        Log.i("Data Error:", "Category Manager");
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

    public boolean isMoreDataAvailable() {
        return moreDataAvailable;
    }

    @Override
    public void flushData(){
        this.gamesBeanArrayList= new ArrayList<GamesBean>();
    }
}
