package com.cycus.playcodeapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cycus.playcodeapp.Activities.GameDescActivity;
import com.cycus.playcodeapp.Activities.LoginActivity;
import com.cycus.playcodeapp.R;
import com.cycus.playcodeapp.SetterGetter.GamesBean;
import com.cycus.playcodeapp.Utils.APIs;
import com.cycus.playcodeapp.Utils.CustomTypeFaces;
import com.cycus.playcodeapp.Utils.DisplayDimension;
import com.cycus.playcodeapp.Utils.DownloadContent;
import com.cycus.playcodeapp.Utils.DynamicSizes;
import com.cycus.playcodeapp.Utils.GridPerRow;
import com.cycus.playcodeapp.Utils.ValidationCheck;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Arun_Saini on 21-06-2016.
 */
public class GridAdapterGameDescription extends BaseAdapter {
    Context context;
    ArrayList<GamesBean> arrayList;
    Typeface tfBold, tfRegular, tfLight;
    int gridCount;
    int spaceUnits;
    GridPerRow objGridPerRow;
    DynamicSizes objDynamicSizes;
    int count=1;
    Activity gameActivity;
    String catIcon;
    String fromMain;

    public GridAdapterGameDescription(Context context, ArrayList<GamesBean> arrayList, Activity gameActivity, String catIcon, String fromMain) {
        this.context = context;
        this.arrayList = arrayList;
        this.fromMain= fromMain;
        tfBold = Typeface.createFromAsset(context.getAssets(), CustomTypeFaces.BASE_FOLDER + CustomTypeFaces.ROBO_BOLD);
        tfRegular = Typeface.createFromAsset(context.getAssets(), CustomTypeFaces.BASE_FOLDER + CustomTypeFaces.ROBO_REGULAR);
        tfLight = Typeface.createFromAsset(context.getAssets(), CustomTypeFaces.BASE_FOLDER + CustomTypeFaces.ROBO_LIGHT);
        this.catIcon= catIcon;
//        objGridPerRow= new GridPerRow();
//        objDynamicSizes= new DynamicSizes();
        this.gameActivity=gameActivity;
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
        Log.i("ARRAY_SIZE_GAME_DESC", getCount() + "_!");
        MyViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_cards_category, parent, false);
            holder = new MyViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.game_icon_category);
            holder.gameName = (TextView) convertView.findViewById(R.id.game_name_category);
            holder.gamePrice = (TextView) convertView.findViewById(R.id.game_price_category);
            holder.gameRating = (RatingBar) convertView.findViewById(R.id.game_rating_category);
            holder.button = (Button) convertView.findViewById(R.id.game_download_button_category);
            holder.ll = (LinearLayout) convertView.findViewById(R.id.ll_grid_category);

            holder.gameName.setText(arrayList.get(position).getGameTitle());
            holder.gameName.setTypeface(tfRegular);
            holder.gameRating.setRating(arrayList.get(position).getGameRating());
            holder.gamePrice.setText(String.valueOf(arrayList.get(position).getGamePrice() == 0.0 ? "Free" : arrayList.get(position).getGamePrice()));
            holder.gamePrice.setTypeface(tfLight);
            holder.button.setTypeface(tfLight);

            final int pos_final = position;
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ValidationCheck.getInstance().isValid()) {
                        new DownloadContent().initiateDownload(context, arrayList.get(pos_final).getCatId(), arrayList.get(pos_final).getGameId(), arrayList.get(pos_final).getGameTitle());
                    }else{
                        Intent intent= new Intent(context, LoginActivity.class);
                        intent.putExtra("first_load", "no");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });

            holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            Picasso.with(context).load(APIs.BASE_THUMBNAILS + arrayList.get(position).getGameThumbnail()).resize((int)new DynamicSizes().dpToPix(150), (int)new DynamicSizes().dpToPix(150)).placeholder(ResourcesCompat.getDrawable(context.getResources(), R.drawable.placeholder, null)).resize((int)new DynamicSizes().dpToPix(130), (int)new DynamicSizes().dpToPix(130)).into(holder.imageView);
//          .resize((int)new DynamicSizes().dpToPix(150), (int)new DynamicSizes().dpToPix(150)).
//            LinearLayout.LayoutParams paramImages= new LinearLayout.LayoutParams(objDynamicSizes.imageWidth(),objDynamicSizes.imageWidth());
//            paramImages.setMargins((int)objDynamicSizes.dpToPix(5),0, (int)objDynamicSizes.dpToPix(5),0);
//            holder.imageView.setLayoutParams(paramImages);
//
//            ViewGroup.MarginLayoutParams mlp= new ViewGroup.MarginLayoutParams(holder.ll.getLayoutParams());
//            mlp.width=objDynamicSizes.gridWidth();
//            mlp.height= objDynamicSizes.getHeightOfEachGrid();

            convertView.setTag(holder);
            final int pos = position;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GameDescActivity.class);
                    intent.putExtra("cat_id", String.valueOf(arrayList.get(pos).getCatId()));
                    intent.putExtra("game_id", String.valueOf(arrayList.get(pos).getGameId()));
                    intent.putExtra("cat_icon", catIcon);
                    intent.putExtra("game_name", arrayList.get(pos).getGameTitle());
                    intent.putExtra("game_price", String.valueOf(arrayList.get(pos).getGamePrice()));
                    if(fromMain!=null)
                        intent.putExtra("from_main", "");
                    else
                        intent.putExtra("from_game_desc", "");
                    intent.putExtra("cat_name", arrayList.get(pos).getCatName());
                    intent.putExtra("game_rating", String.valueOf(arrayList.get(pos).getGameRating()));
                    intent.putExtra("game_desc", arrayList.get(pos).getGameDesc());
                    intent.putExtra("game_thumbnail", arrayList.get(pos).getGameThumbnail());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    gameActivity.finish();
                    context.startActivity(intent);

                }
            });
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }


        return convertView;
    }

    class MyViewHolder {
        ImageView imageView;
        TextView gameName;
        TextView gamePrice;
        RatingBar gameRating;
        Button button;
        LinearLayout ll;
    }
}
