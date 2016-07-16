package com.cycus.playcodeapp.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cycus.playcodeapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Arun_Saini on 22-06-2016.
 */
public class VPAdapter extends PagerAdapter {
    String[] screenShots;
    Context context;

    public VPAdapter(Context context, String[] screenShots){
        this.context=context;
        this.screenShots=screenShots;
    }

    @Override
    public int getCount() {
        return screenShots.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.i("IN_ADAPTER", screenShots[position]);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.vp_image_view, container, false);
        ImageView imageView= (ImageView) view.findViewById(R.id.vp_image);

        Picasso.with(context).load(screenShots[position]).into(imageView);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeViewAt(position);
    }
}
