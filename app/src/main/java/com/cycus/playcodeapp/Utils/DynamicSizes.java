package com.cycus.playcodeapp.Utils;

import android.util.Log;

/**
 * Created by Arun_Saini on 25-06-2016.
 */
public class DynamicSizes {
    private int spacePercentage=1;
    private int unitsOfSpace;
    private  GridPerRow objGrid;
    int widthOfScreenInPixels;
    int heightOfScreenInPixels;

    public DynamicSizes(){
//        Log.i("UNITOFSPACE", unitsOfSpace+"");
//        spacePercentage=unitsOfSpace*5;
//        this.unitsOfSpace= unitsOfSpace;
        widthOfScreenInPixels = (int)DisplayDimension.getInstance().getWidthInPixels();
        heightOfScreenInPixels = (int)DisplayDimension.getInstance().getHeightInPixels();
        objGrid= new GridPerRow();
    }

//    public int getExtraSpace(){
//        double temp2= 0.15* widthOfScreenInPixels;
//        return (int)temp2;
//    }
//
//    public int getUnitExtraSpace(){
//        return (int)(0.05*widthOfScreenInPixels);
//    }
////
//    public int getTotalWidthOfGrids(){
//        return (int)(widthOfScreenInPixels-(getUnitExtraSpace()*unitsOfSpace));
//    }
////
//    public float getGridWidth(){
//        return getTotalWidthOfGrids()/gridCount;
//    }
//
    public int getHeightOfEachGrid(){
        //ration 32/53
        return (int)(gridWidth()/32)*53;
    }

    public int getGridPadding(){
        return (int)((0.03)*widthOfScreenInPixels);
    }

//    public int getGridPaddingPort(){
//        return (int)((0.03)*widthOfScreenInPixels);
//    }
//
//    public int getGridPaddingLand(){
//        return (int)((0.03)*heightOfScreenInPixels);
//    }


    public int gridWidth(){
        int extraSpace= (int)((objGrid.getGridCount()+1)*getGridPadding());
        return (int)((widthOfScreenInPixels-extraSpace-(objGrid.getGridCount()*2))/objGrid.getGridCount());
    }

    public int imageWidth(){
        double pix= dpToPix(5);
        return (int)(gridWidth()-(pix*2));
    }

    public double dpToPix(int dp){
        double pix= dp*(DisplayDimension.getInstance().getDip()/160);
        return pix;
    }
}
