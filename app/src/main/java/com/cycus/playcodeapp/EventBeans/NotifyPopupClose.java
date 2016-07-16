package com.cycus.playcodeapp.EventBeans;

/**
 * Created by Arun_Saini on 26-06-2016.
 */
public class NotifyPopupClose {
    boolean shouldClose;
    public NotifyPopupClose(boolean status){
        shouldClose= status;
    }

    public boolean isShouldClose() {
        return shouldClose;
    }
}
