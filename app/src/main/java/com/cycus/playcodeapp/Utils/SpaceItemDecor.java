package com.cycus.playcodeapp.Utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Created by Arun_Saini on 20-06-2016.
 */
public class SpaceItemDecor extends RecyclerView.ItemDecoration {
    int space;
    int itemCount;
    String type;
    Context context;

    public SpaceItemDecor(int space, int itemCount, String type, Context context){
        this.space=space;
        this.itemCount=itemCount;
        this.type=type;
        this.context= context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if(type.equals("GRID")){
            if(!(parent.getChildLayoutPosition(view)==itemCount-1)){
                outRect.right=space;
            }

//            if(parent.getChildLayoutPosition(view)==parent.getChildCount()-1){
//                ProgressBar progressBar= new ProgressBar(context);
//                RelativeLayout rl= new RelativeLayout(context);
//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, RelativeLayout.LayoutParams.MATCH_PARENT);
//                progressBar.setIndeterminate(true);
//                params.addRule(RelativeLayout.CENTER_IN_PARENT);
//                progressBar.setVisibility(View.VISIBLE);
//                rl.setLayoutParams(params);
//                rl.addView(progressBar);
//                parent.addView(rl);
//            }
        }else if(type.equals("LIST")){
            if(!(parent.getChildLayoutPosition(view)==itemCount)){
                outRect.bottom=space;
            }
        }

    }
}
