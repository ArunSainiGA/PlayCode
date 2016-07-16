package com.cycus.playcodeapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.cycus.playcodeapp.Utils.DownloadContent;
import com.cycus.playcodeapp.Utils.ValidationCheck;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Arun_Saini on 20-06-2016.
 */
public class RecyclerAdapterGridsHome extends RecyclerView.Adapter<RecyclerAdapterGridsHome.Holder> {
    Context context;
    Typeface tfBold, tfRegular, tfLight;
    ArrayList<GamesBean> arrayList;
    boolean nearEnd = false;
    Activity activity;
    String catIcon;

    public RecyclerAdapterGridsHome(Context context, ArrayList<GamesBean> arrayList, Activity activity, String catIcon) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.catIcon = catIcon;
        tfBold = Typeface.createFromAsset(context.getAssets(), CustomTypeFaces.BASE_FOLDER + CustomTypeFaces.ROBO_BOLD);
        tfRegular = Typeface.createFromAsset(context.getAssets(), CustomTypeFaces.BASE_FOLDER + CustomTypeFaces.ROBO_REGULAR);
        tfLight = Typeface.createFromAsset(context.getAssets(), CustomTypeFaces.BASE_FOLDER + CustomTypeFaces.ROBO_LIGHT);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_cards, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.gameName.setText(arrayList.get(position).getGameTitle());
        holder.gameName.setTypeface(tfRegular);
        holder.gameRating.setRating(arrayList.get(position).getGameRating());
        holder.gamePrice.setText(String.valueOf(arrayList.get(position).getGamePrice() == 0.0 ? "Free" : arrayList.get(position).getGamePrice()));
        holder.gamePrice.setTypeface(tfLight);
        holder.button.setTypeface(tfLight);

        final int pos = position;
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidationCheck.getInstance().isValid()) {
                    new DownloadContent().initiateDownload(context, arrayList.get(pos).getCatId(), arrayList.get(pos).getGameId(), arrayList.get(pos).getGameTitle());
                }else{
                    Intent intent= new Intent(context, LoginActivity.class);
                    intent.putExtra("first_load", "no");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });

        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GameDescActivity.class);
                intent.putExtra("cat_id", String.valueOf(arrayList.get(pos).getCatId()));
                intent.putExtra("cat_icon", catIcon);
                intent.putExtra("game_id", String.valueOf(arrayList.get(pos).getGameId()));
                intent.putExtra("game_name", arrayList.get(pos).getGameTitle());
                intent.putExtra("game_price", String.valueOf(arrayList.get(pos).getGamePrice()));
                intent.putExtra("cat_name", arrayList.get(pos).getCatName());
                intent.putExtra("game_rating", String.valueOf(arrayList.get(pos).getGameRating()));
                intent.putExtra("game_desc", arrayList.get(pos).getGameDesc());
                intent.putExtra("from_main", "");
                intent.putExtra("game_thumbnail", arrayList.get(pos).getGameThumbnail());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        Picasso.with(context).load(APIs.BASE_THUMBNAILS + arrayList.get(position).getGameThumbnail()).placeholder(R.drawable.placeholder).into(holder.imageView);
        if (position == (getItemCount() - 2))
            nearEnd = true;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        Button buttonDownload;
        TextView gameName;
        TextView gamePrice;
        RatingBar gameRating;
        Button button;
        LinearLayout ll;

        public Holder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.game_icon);
            buttonDownload = (Button) itemView.findViewById(R.id.game_download_button);
            gameName = (TextView) itemView.findViewById(R.id.game_name);
            gamePrice = (TextView) itemView.findViewById(R.id.game_price);
            gameRating = (RatingBar) itemView.findViewById(R.id.game_rating);
            button = (Button) itemView.findViewById(R.id.game_download_button);
            ll= (LinearLayout) itemView.findViewById(R.id.gc_root_layout);
        }

        @Override
        public void onClick(View v) {
            Log.i("HolderListener", getLayoutPosition() + "CALLED");
        }
    }
}
