package com.cycus.playcodeapp.ModelManagerInterfaces;

import android.content.Context;

import com.cycus.playcodeapp.SetterGetter.CategoryBean;
import com.cycus.playcodeapp.SetterGetter.GamesBean;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Arun_Saini on 18-06-2016.
 */
public interface IHomeManager {
    public boolean checkHomeData(Context context);
    public void getDataFromServer(Context context);
    public void addHomeData(CategoryBean catBean, ArrayList<GamesBean> gameBeans);
    public Map<CategoryBean, ArrayList<GamesBean>> getHomeDate();
    public void notifyEvent(boolean status);
}
