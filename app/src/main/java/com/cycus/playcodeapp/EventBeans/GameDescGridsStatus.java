package com.cycus.playcodeapp.EventBeans;

/**
 * Created by Arun_Saini on 27-06-2016.
 */
public class GameDescGridsStatus {
    boolean available;
    public GameDescGridsStatus (boolean status){
        available = status;
    }

    public boolean isAvailable() {
        return available;
    }
}
