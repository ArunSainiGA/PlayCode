package com.cycus.playcodeapp.Utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Arun_Saini on 20-06-2016.
 */
public class LinearLayoutManagerNoScroll extends LinearLayoutManager {
    boolean flag;
    public LinearLayoutManagerNoScroll(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public void setScrollEnable(boolean flag){
        this.flag= flag;
    }
    @Override
    public boolean canScrollVertically() {
        return flag && super.canScrollVertically();
    }
}
