package com.cycus.playcodeapp.Utils;

/**
 * Created by Arun_Saini on 25-06-2016.
 */
public class DisplayDimension {
    private static DisplayDimension displayDimension;
    private float heightInPixels;
    private float widthInPixels;
    private float heightInDP;
    private float widthInDP;
    private int dip;

    public static DisplayDimension getInstance(){
        if(displayDimension==null)
            displayDimension= new DisplayDimension();
        return displayDimension;
    }

    public float getHeightInPixels() {
        return heightInPixels;
    }

    public void setHeightInPixels(float heightInPixels) {
        this.heightInPixels = heightInPixels;
    }

    public float getWidthInPixels() {
        return widthInPixels;
    }

    public void setWidthInPixels(float widthInPixels) {
        this.widthInPixels = widthInPixels;
    }

    public float getHeightInDP() {
        return heightInDP;
    }

    public void setHeightInDP(float heightInDP) {
        this.heightInDP = heightInDP;
    }

    public float getWidthInDP() {
        return widthInDP;
    }

    public void setWidthInDP(float widthInDP) {
        this.widthInDP = widthInDP;
    }

    public int getDip() {
        return dip;
    }

    public void setDip(int dip) {
        this.dip = dip;
    }
}
