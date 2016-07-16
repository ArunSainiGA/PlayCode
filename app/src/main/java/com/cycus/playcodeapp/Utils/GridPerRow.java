package com.cycus.playcodeapp.Utils;

import android.support.annotation.DimenRes;

/**
 * Created by Arun_Saini on 25-06-2016.
 */
public class GridPerRow {
    float screenWidth;
    int gridCount=0;
    int spaceCount;
    float screenHeight;

    public GridPerRow(){
        screenWidth= DisplayDimension.getInstance().getWidthInDP();
        screenHeight = DisplayDimension.getInstance().getHeightInDP();
    }

    public int getGridCount(){
        if(screenWidth<400){
            gridCount=2;
        }else if(screenWidth<600){
            gridCount=3;
        }else if(screenWidth<800){
            gridCount=4;
        }else if(screenWidth<1000){
            gridCount=5;
        }else{
            gridCount=6;
        }
        return gridCount;
    }
//    public int getGridCountPort(){
//        if(screenWidth<400){
//            gridCount=2;
//        }else if(screenWidth<600){
//            gridCount=3;
//        }else if(screenWidth<800){
//            gridCount=4;
//        }else if(screenWidth<1000){
//            gridCount=5;
//        }else{
//            gridCount=6;
//        }
//        return gridCount;
//    }
//    public int getGridCountLand(){
//        if(screenHeight<400){
//            gridCount=2;
//        }else if(screenHeight<600){
//            gridCount=3;
//        }else if(screenHeight<800){
//            gridCount=4;
//        }else if(screenHeight<1000){
//            gridCount=5;
//        }else{
//            gridCount=6;
//        }
//        return gridCount;
//    }

    public int spaceUnitsCount(int count){
        if(count==2){
            spaceCount=3;
        }else if(count==3){
            spaceCount=4;
        }else if(count==4){
            spaceCount=5;
        }else if(count==5){
            spaceCount=6;
        }else if(count==6){
            spaceCount=7;
        }
        return spaceCount;
    }
}
