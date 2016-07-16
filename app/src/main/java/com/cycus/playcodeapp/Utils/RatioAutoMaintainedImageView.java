package com.cycus.playcodeapp.Utils;

import android.content.Context;
import android.util.AttributeSet;

import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by Arun_Saini on 30-06-2016.
 */
public class RatioAutoMaintainedImageView extends RoundedImageView {
    public RatioAutoMaintainedImageView(Context context) {
        super(context);
    }

    public RatioAutoMaintainedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatioAutoMaintainedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
