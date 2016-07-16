package com.cycus.playcodeapp.EventBeans;

/**
 * Created by Arun_Saini on 18-06-2016.
 */
public class HomeDataStatus {
    boolean available;

    public HomeDataStatus(boolean status){
        available= status;
    }

    public boolean isAvailable() {
        return available;
    }
}
