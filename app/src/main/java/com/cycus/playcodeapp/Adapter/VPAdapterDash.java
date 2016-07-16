package com.cycus.playcodeapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.cycus.playcodeapp.Activities.ImageViewer;
import com.cycus.playcodeapp.EventBeans.NotifyPopupClose;
import com.cycus.playcodeapp.R;
import com.cycus.playcodeapp.Utils.APIs;
import com.cycus.playcodeapp.Utils.DynamicSizes;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by Arun_Saini on 22-06-2016.
 */
public class VPAdapterDash extends StaticPagerAdapter {
    String[] screenShots;
    Context context;
    String gameName, gameThumbnail;
    PopupWindow mwindow;
    EventBus bus= EventBus.getDefault();

    public VPAdapterDash( Context context, String[] screenShots, String gameName, String gameThumbnail){
        this.context=context;
        this.screenShots=screenShots;
        this.gameName= gameName;
        this.gameThumbnail= gameThumbnail;
        bus.register(this);
    }

    @Override
    public View getView(final ViewGroup container, int position) {
        Log.i("IN_ADAPTER", APIs.BASE_THUMBNAILS+screenShots[position]);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.vp_image_view, container, false);
        ImageView imageView= (ImageView) view.findViewById(R.id.vp_image);

        final int temp= position;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view= inflater.inflate(R.layout.popup_layout, null, false);
                ImageView popupImage= (ImageView) view.findViewById(R.id.popup_image);

                Picasso.with(context).load(APIs.BASE_THUMBNAILS+screenShots[temp]).into(popupImage);
                mwindow= new PopupWindow(view, (int) new DynamicSizes().dpToPix(300),(int) new DynamicSizes().dpToPix(300), false );
                popupImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent(context, ImageViewer.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("image_url",screenShots[temp]);
                        intent.putExtra("game_name",gameName);
                        intent.putExtra("game_thumbnail",gameThumbnail);
                        context.startActivity(intent);
                        mwindow.dismiss();
                    }
                });
                mwindow.setOutsideTouchable(true);
                mwindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mwindow.setAnimationStyle(android.R.style.Animation_Dialog);
                mwindow.showAtLocation(view, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
//        if(position==0)
//            Picasso.with(context).load(R.drawable.tablet_one).placeholder(R.drawable.placeholder).into(imageView);
//        else if(position==1)
//            Picasso.with(context).load(R.drawable.tablet_two).placeholder(R.drawable.placeholder).into(imageView);
//        else if(position==2)
//            Picasso.with(context).load(R.drawable.tablet_three).placeholder(R.drawable.placeholder).into(imageView);
        Picasso.with(context).load(APIs.BASE_THUMBNAILS+screenShots[temp]).placeholder(R.drawable.placeholder).into(imageView);

        return view;
    }

    @Override
    public int getCount() {
            return screenShots.length;
    }

    @Subscribe
    public void onEvent(NotifyPopupClose event){
        if(event.isShouldClose()){
            mwindow.dismiss();
        }
    }
}
