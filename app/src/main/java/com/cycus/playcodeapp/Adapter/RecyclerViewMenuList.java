package com.cycus.playcodeapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cycus.playcodeapp.R;
import com.cycus.playcodeapp.SetterGetter.CategoryBean;
import com.cycus.playcodeapp.Utils.APIs;
import com.cycus.playcodeapp.Utils.CustomTypeFaces;
import com.squareup.picasso.Picasso;

/**
 * Created by Arun_Saini on 25-06-2016.
 */
public class RecyclerViewMenuList extends RecyclerView.Adapter<RecyclerViewMenuList.Holder> {
    Object[] objArray;
    Context context;
    public RecyclerViewMenuList(Context context, Object[] objArray){
        this.context= context;
        this.objArray= objArray;
    }

    @Override
    public RecyclerViewMenuList.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.list_view_menu_items, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerViewMenuList.Holder holder, int position) {
//        holder.image
        holder.tv.setText(((CategoryBean)objArray[position]).getCatName());
        holder.tv.setTextColor(Color.parseColor("#333333"));
        Log.i("CAT_ICON", APIs.CATEGORY_ICON+((CategoryBean)objArray[position]).getIcon());
        Picasso.with(context).load(APIs.CATEGORY_ICON+((CategoryBean)objArray[position]).getIcon()).placeholder(ResourcesCompat.getDrawable(context.getResources(), R.drawable.small_logo_playcode, null)).into(holder.image);
        holder.tv.setTypeface(Typeface.createFromAsset(context.getAssets(), CustomTypeFaces.BASE_FOLDER+CustomTypeFaces.ROBO_LIGHT));
    }

    @Override
    public int getItemCount() {
        return objArray.length;
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tv;

        public Holder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.menu_list_tv);
            image = (ImageView) itemView.findViewById(R.id.menu_list_image);
        }
    }
}
