package com.cycus.playcodeapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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
public class RecyclerAdapterListsHome extends RecyclerView.Adapter<RecyclerAdapterListsHome.Holder> {
    Typeface tfBold, tfRegular, tfLight;
    Context context;
    ArrayList<GamesBean> arrayList;
    public RecyclerAdapterListsHome(Context context, ArrayList<GamesBean> arrayList) {
        this.context = context;
        this.arrayList= arrayList;
        tfBold= Typeface.createFromAsset(context.getAssets(), CustomTypeFaces.BASE_FOLDER+CustomTypeFaces.ROBO_BOLD);
        tfRegular= Typeface.createFromAsset(context.getAssets(), CustomTypeFaces.BASE_FOLDER+CustomTypeFaces.ROBO_REGULAR);
        tfLight= Typeface.createFromAsset(context.getAssets(), CustomTypeFaces.BASE_FOLDER+CustomTypeFaces.ROBO_LIGHT);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_home, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.gameName.setText(arrayList.get(position).getGameTitle());
        holder.gameName.setTypeface(tfRegular);
        holder.gameRating.setRating(arrayList.get(position).getGameRating());
        holder.gamePrice.setText(String.valueOf(arrayList.get(position).getGamePrice()== 0.0? "Free" : arrayList.get(position).getGamePrice()));
        holder.gamePrice.setTypeface(tfLight);

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
        Picasso.with(context).load(APIs.BASE_THUMBNAILS+arrayList.get(position).getGameThumbnail()).placeholder(ResourcesCompat.getDrawable(context.getResources(), R.drawable.placeholder, null)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        ImageView imageView;
        Button buttonDownload;
        TextView gameName;
        TextView gamePrice;
        RatingBar gameRating;
        Button button;

        public Holder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.game_icon);
            buttonDownload= (Button) itemView.findViewById(R.id.game_download_button);
            gameName= (TextView) itemView.findViewById(R.id.game_name);
            gamePrice= (TextView) itemView.findViewById(R.id.game_price);
            gameRating= (RatingBar) itemView.findViewById(R.id.game_rating);
            button = (Button) itemView.findViewById(R.id.game_download_button);
        }
    }
}
