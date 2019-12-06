/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.cocoversion;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.screens.utility.RALog;

import java.util.ArrayList;

/**
 * Created by philips on 4/18/17.
 */

public class CocoVersionAdapter extends RecyclerView.Adapter<CocoVersionAdapter.CocoInfoViewHolder> {
    public static final String TAG =CocoVersionAdapter.class.getSimpleName();

    private ArrayList<CocoVersionItem> cocoVersionsItemList = null;
    private Context context;
    private int clickedPos = -1;


    public CocoVersionAdapter(Context context, ArrayList<CocoVersionItem> cocoItemList) {
        this.cocoVersionsItemList = cocoItemList;
        this.context = context;
    }

    public void setClickedPosition(int clickedPos) {
        this.clickedPos = clickedPos;
    }

    @Override
    public CocoInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.coco_version_listitem, parent, false);
        return new CocoInfoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CocoInfoViewHolder holder, int position) {
        RALog.d(TAG, " onBindViewHolder called  ");
        final CocoVersionItem cocos = cocoVersionsItemList.get(position);
        holder.CocoName.setText(cocos.getTitle());
        holder.CocoVersion.setText(cocos.getVersion());
        holder.cocoDescription.setTag(position);
        holder.cocoDescription.setText(cocos.getDescription());
        if (position == clickedPos && !cocos.isDescriptionShowing()) {
            cocos.setDescriptionShowing(true);

            animation.setAnimationListener(new Animation.AnimationListener(){

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    RALog.d(TAG, "onAnimationEnd");
                    holder.cocoDescription.setVisibility(View.VISIBLE);
                    holder.seperator.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            holder.cocoDescription.startAnimation(animation);
        } else {
            if (cocos.isDescriptionShowing()) {
                holder.cocoDescription.animate().translationYBy(0);
                holder.cocoDescription.clearAnimation();
            }
            cocos.setDescriptionShowing(false);
            holder.cocoDescription.setVisibility(View.GONE);
            holder.seperator.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() {
        if (cocoVersionsItemList != null) {
            return cocoVersionsItemList.size();
        }
        return 0;
    }
    protected class CocoInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView CocoName;
        public TextView CocoVersion;
        public TextView cocoDescription;
        public View seperator;

        public CocoInfoViewHolder(View itemView) {
            super(itemView);
            CocoName = (TextView) itemView.findViewById(R.id.coco_name);
            CocoVersion = (TextView) itemView.findViewById(R.id.coco_version);
            cocoDescription = (TextView) itemView.findViewById(R.id.coco_description);
            seperator = itemView.findViewById(R.id.description_seperator);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            setClickedPosition((int) cocoDescription.getTag());
            notifyDataSetChanged();
        }
    }

    Animation animation = new Animation() {
        @Override
        public void setDuration(long durationMillis) {
            super.setDuration(200);
        }
    };
}
