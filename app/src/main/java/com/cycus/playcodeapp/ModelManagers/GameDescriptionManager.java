package com.cycus.playcodeapp.ModelManagers;

import android.content.Context;
import android.util.Log;

import com.cycus.playcodeapp.EventBeans.GameDescriptionBean;
import com.cycus.playcodeapp.ModelManagerInterfaces.IGameDescriptionManager;
import com.cycus.playcodeapp.SetterGetter.GamesBean;
import com.cycus.playcodeapp.Utils.APIs;
import com.cycus.playcodeapp.Utils.Commons;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import de.greenrobot.event.EventBus;

/**
 * Created by Arun_Saini on 23-06-2016.
 */
public class GameDescriptionManager implements IGameDescriptionManager {
    GamesBean gamesBean;
    Context context;
    int gamesId;
    int catId;

    @Override
    public void checkData(Context context, int gamesId, int catId) {
        this.context=context;
        this.gamesId=gamesId;
        this.catId=catId;
        gamesBean= new GamesBean();
        Log.i("CAT_ID", catId+"");
        Log.i("GAME_ID", gamesId+"");
        if(gamesBean.getGameTitle().equals(""))
            getDataFromServer();
        else if(gamesBean.getGameId()==gamesId)
            notifyEvent(true);
        else if(gamesBean.getGameId()!=gamesId)
            getDataFromServer();
    }

    @Override
    public GamesBean getData() {
        return this.gamesBean;
    }

    @Override
    public void notifyEvent(boolean status) {
        EventBus.getDefault().post(new GameDescriptionBean(status));
    }
    @Override
    public void flushData(){
        gamesBean= new GamesBean();
    }

    @Override
    public void getDataFromServer() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(3000);
        RequestParams params= new RequestParams();
        params.put("rt", 0);
        params.put("cmid", gamesId);
        params.put("pid", catId);
        params.put("scid", 0);

        client.post(context, APIs.BASE+ APIs.GAME_DESCRIPTION_API, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    if(response.getString("error").equals("200")){
                        Log.i("JSON", response.toString());
                        JSONObject gameObject= response.getJSONObject("game");
//                        gamesBean.setCatName(gameObject.getString("cat_name"));
                        gamesBean.setScreen3(gameObject.getString("game_screen_three"));
//                        gamesBean.setGameTitle(gameObject.getString("game_title"));
//                        gamesBean.setGamePrice(Commons.stringToFloat(gameObject.getString("game_price")));
//                        gamesBean.setGameThumbnail(gameObject.getString("game_thumbnail"));
                        gamesBean.setScreen2(gameObject.getString("game_screen_two"));
                        gamesBean.setScreen1(gameObject.getString("game_screen_one"));
//                        gamesBean.setServiceId(Commons.stringToInt((gameObject.getString("service_id"))==null?"0":(gameObject.getString("service_id"))));
//                        gamesBean.setCatId(Commons.stringToInt(gameObject.getString("cat_id")));
//                        gamesBean.setGameDesc(gameObject.getString("demo_layout"));
//                        gamesBean.setGameRating(Commons.stringToInt(gameObject.getString("game_rating")));
//                        gamesBean.setGameShortDesc(gameObject.getString("game_short_desc"));
//                        gamesBean.setGameId(Commons.stringToInt(gameObject.getString("game_id")));
                        notifyEvent(true);
                    }else if(response.getString("error").equals("201")){
                        notifyEvent(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
