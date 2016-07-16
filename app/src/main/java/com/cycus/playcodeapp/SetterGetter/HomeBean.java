package com.cycus.playcodeapp.SetterGetter;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Arun_Saini on 18-06-2016.
 */
public class HomeBean {
    Map<CategoryBean, ArrayList<GamesBean>>  homeData;

    public Map<CategoryBean, ArrayList<GamesBean>> getHomeData() {
        return homeData;
    }

    public void setHomeData(Map<CategoryBean, ArrayList<GamesBean>> homeData) {
        this.homeData = homeData;
    }
}
