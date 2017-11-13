/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
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

public class InsightsAdapter extends RecyclerView.Adapter<InsightsAdapter.InsightsInfoViewHolder> {

    interface InsightItemClickListener {
        void onInsightsItemClicked(String momentId);
    }

    public static final String TAG =InsightsAdapter.class.getSimpleName();

    private ArrayList<String> insightsTitleItemList = null;
    private ArrayList<String> insightsDescItemList = null;
    private Context context;
    private InsightItemClickListener insightItemClickListener;

    public void setInsightItemClickListener(InsightItemClickListener insightItemClickListener) {
        this.insightItemClickListener = insightItemClickListener;
    }

    public void removeInsightItemClickListener() {
        this.insightItemClickListener = null;
    }

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
        holder.tvTitle.setTag("momentId");
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (insightItemClickListener != null) {
                        insightItemClickListener.onInsightsItemClicked((String) tvTitle.getTag());
                    }
                }
            });
        }
    }

}
