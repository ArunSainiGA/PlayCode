package com.cycus.playcodeapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cycus.playcodeapp.Activities.GameDescActivity;
import com.cycus.playcodeapp.R;
import com.cycus.playcodeapp.SetterGetter.GamesBean;
import com.cycus.playcodeapp.Utils.APIs;
import com.cycus.playcodeapp.Utils.CustomTypeFaces;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Arun_Saini on 21-06-2016.
 */
public class GridAdapter extends BaseAdapter {
    Context context;
    ArrayList<GamesBean> arrayList;
    Typeface tfBold, tfRegular, tfLight;

    public GridAdapter() {

    }

    public GridAdapter(Context context, ArrayList<GamesBean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        tfBold= Typeface.createFromAsset(context.getAssets(), CustomTypeFaces.BASE_FOLDER+CustomTypeFaces.ROBO_BOLD);
        tfRegular= Typeface.createFromAsset(context.getAssets(), CustomTypeFaces.BASE_FOLDER+CustomTypeFaces.ROBO_REGULAR);
        tfLight= Typeface.createFromAsset(context.getAssets(), CustomTypeFaces.BASE_FOLDER+CustomTypeFaces.ROBO_LIGHT);
    }

    public void addAll(ArrayList<GamesBean> result) {
        arrayList.addAll(result);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("IN_adapter", "cputn");
        MyViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_cards_category, parent, false);
            holder = new MyViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.game_icon);
            holder.gameName = (TextView) convertView.findViewById(R.id.game_name);
            holder.gamePrice = (TextView) convertView.findViewById(R.id.game_price);
            holder.gameRating = (RatingBar) convertView.findViewById(R.id.game_rating);
            holder.button = (Button) convertView.findViewById(R.id.game_download_button);

            convertView.setTag(holder);
            final int pos=position;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GameDescActivity.class);
                    intent.putExtra("cat_id", String.valueOf(arrayList.get(pos).getCatId()));
                    intent.putExtra("game_id", String.valueOf(arrayList.get(pos).getGameId()));
                    intent.putExtra("game_name", arrayList.get(pos).getGameTitle());
                    intent.putExtra("game_price", String.valueOf(arrayList.get(pos).getGamePrice()));
                    intent.putExtra("cat_name", arrayList.get(pos).getCatName());
                    intent.putExtra("game_rating", String.valueOf(arrayList.get(pos).getGameRating()));
                    intent.putExtra("game_desc", arrayList.get(pos).getGameDesc());
                    intent.putExtra("game_thumbnail", arrayList.get(pos).getGameThumbnail());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        holder.gameName.setText(arrayList.get(position).getGameTitle());
        holder.gameName.setTypeface(tfRegular);
        holder.gameRating.setRating(arrayList.get(position).getGameRating());
        holder.gamePrice.setText(String.valueOf(arrayList.get(position).getGamePrice() == 0.0 ? "Free" : arrayList.get(position).getGamePrice()));
        holder.gamePrice.setTypeface(tfLight);
        holder.button.setTypeface(tfLight);
        Picasso.with(context).load(APIs.BASE_THUMBNAILS + arrayList.get(position).getGameThumbnail()).into(holder.imageView);
        return convertView;
    }

    class MyViewHolder {
        ImageView imageView;
        TextView gameName;
        TextView gamePrice;
        RatingBar gameRating;
        Button button;

    }
}
