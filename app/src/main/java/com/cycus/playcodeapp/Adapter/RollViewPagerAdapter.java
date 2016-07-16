package com.cycus.playcodeapp.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cycus.playcodeapp.R;
import com.cycus.playcodeapp.Utils.APIs;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.DynamicPagerAdapter;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.squareup.picasso.Picasso;

/**
 * Created by Arun_Saini on 23-06-2016.
 */
public class RollViewPagerAdapter extends PagerAdapter {
    String[] array;
    Context context;

    public RollViewPagerAdapter(Context context, String[] array) {

        this.context=context;
        this.array= array;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.vp_image_view, container, false);
        ImageView imageView= (ImageView) view.findViewById(R.id.vp_image);

        Picasso.with(context).load(APIs.BASE_THUMBNAILS+array[position]).into(imageView);
        container.addView(view);

        return view;
    }


    @Override
    public int getCount() {
        return array.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
