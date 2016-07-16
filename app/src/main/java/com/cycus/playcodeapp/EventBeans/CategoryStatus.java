package com.cycus.playcodeapp.EventBeans;

import com.cycus.playcodeapp.SetterGetter.CategoryBean;

/**
 * Created by Arun_Saini on 21-06-2016.
 */
public class CategoryStatus {
    boolean available;

    public CategoryStatus(boolean available){
        this.available=available;
    }
    public boolean isAvailable() {
        return available;
    }

}
