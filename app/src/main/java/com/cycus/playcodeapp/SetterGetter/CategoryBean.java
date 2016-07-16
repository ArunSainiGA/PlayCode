package com.cycus.playcodeapp.SetterGetter;

/**
 * Created by Arun_Saini on 18-06-2016.
 */
public class CategoryBean {
    int catId;
    int catOrder;
    String catName;
    String catDesc;
    String icon;

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public int getCatOrder() {
        return catOrder;
    }

    public void setCatOrder(int catOrder) {
        this.catOrder = catOrder;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatDesc() {
        return catDesc;
    }

    public void setCatDesc(String catDesc) {
        this.catDesc = catDesc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
