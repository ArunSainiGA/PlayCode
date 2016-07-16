package com.cycus.playcodeapp.EventBeans;

/**
 * Created by Arun_Saini on 23-06-2016.
 */
public class GameDescriptionBean {
    boolean available;
    public GameDescriptionBean(boolean available){
        this.available= available;
    }

    public boolean isAvailable() {
        return available;
    }
}
