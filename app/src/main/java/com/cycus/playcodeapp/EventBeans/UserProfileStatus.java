package com.cycus.playcodeapp.EventBeans;

/**
 * Created by Arun_Saini on 28-06-2016.
 */
public class UserProfileStatus {
    private boolean available;

    public UserProfileStatus(boolean available){
        this.available= available;
    }

    public boolean isAvailable() {
        return available;
    }
}
