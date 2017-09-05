package com.philips.platform.appframework.connectivitypowersleep.insights;

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

public class InsightsAdapter extends RecyclerView.Adapter<InsightsAdapter.InsightsInfoViewHolder> {
    public static final String TAG =InsightsAdapter.class.getSimpleName();

    private ArrayList<String> insightsTitleItemList = null;
    private ArrayList<String> insightsDescItemList = null;
    private Context context;


    public InsightsAdapter(Context context, ArrayList<String> insightsTitleItemList, ArrayList<String> insightsDescItemList) {
        this.insightsTitleItemList = insightsTitleItemList;
        this.insightsDescItemList = insightsDescItemList;
        this.context = context;
    }


    @Override
    public InsightsInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_insights_item, parent, false);
        return new InsightsInfoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final InsightsInfoViewHolder holder, int position) {
        RALog.d(TAG, " onBindViewHolder called  ");
        holder.tvTitle.setText(insightsTitleItemList.get(position));
        holder.tvDetail.setText(insightsDescItemList.get(position));
    }

    @Override
    public int getItemCount() {
        if (insightsTitleItemList != null) {
            return insightsTitleItemList.size();
        }
        return 0;
    }
    public class InsightsInfoViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDetail;
        public TextView tvTitle;

        public InsightsInfoViewHolder(View itemView) {
            super(itemView);
            tvDetail = (TextView) itemView.findViewById(R.id.tips_detail);
            tvTitle = (TextView) itemView.findViewById(R.id.tips_title);
        }
    }

}
