package com.cycus.playcodeapp.ModelManagerInterfaces;

import android.content.Context;

import com.cycus.playcodeapp.SetterGetter.GamesBean;

import java.util.ArrayList;

/**
 * Created by Arun_Saini on 21-06-2016.
 */
public interface ICategoryManager {
    public void checkCategoryData(Context context, int catId, int offset);
    public ArrayList<GamesBean> getCategoryData();
    public void notifyEvent(boolean status);
    public void addCategory(GamesBean gamesBean);
    public void getDataFromServer(Context context);
    public void flushData();
}
