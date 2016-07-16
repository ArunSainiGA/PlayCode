package com.cycus.playcodeapp.ModelManagerInterfaces;

import android.content.Context;

import com.cycus.playcodeapp.SetterGetter.GamesBean;

import java.util.ArrayList;

/**
 * Created by Arun_Saini on 23-06-2016.
 */
public interface IGameDescriptionManager {
    public void checkData(Context context, int gamesId, int catId);
    public GamesBean getData();
    public void notifyEvent(boolean status);
    public void getDataFromServer();
    public void flushData();

}
