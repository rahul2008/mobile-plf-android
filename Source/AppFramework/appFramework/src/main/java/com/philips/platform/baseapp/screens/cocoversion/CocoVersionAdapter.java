package com.philips.platform.baseapp.screens.cocoversion;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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



    public CocoVersionAdapter(Context context, ArrayList<CocoVersionItem> cocoItemList) {
        this.cocoVersionsItemList = cocoItemList;
        this.context = context;
    }

    @Override
    public CocoInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.coco_version_listitem, parent, false);
        return new CocoInfoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CocoInfoViewHolder holder, int position) {
        RALog.d(TAG, " onBindViewHolder called  ");
        CocoVersionItem cocos = cocoVersionsItemList.get(position);
        holder.CocoName.setText(cocos.title);
        holder.CocoVersion.setText(cocos.Version);
    }

    @Override
    public int getItemCount() {
        if (cocoVersionsItemList != null) {
            return cocoVersionsItemList.size();
        }
        return 0;
    }
    public class CocoInfoViewHolder extends RecyclerView.ViewHolder {
        public TextView CocoName;
        public TextView CocoVersion;

        public CocoInfoViewHolder(View itemView) {
            super(itemView);
            CocoName = (TextView) itemView.findViewById(R.id.coco_name);
            CocoVersion = (TextView) itemView.findViewById(R.id.coco_version);

        }
    }

}
