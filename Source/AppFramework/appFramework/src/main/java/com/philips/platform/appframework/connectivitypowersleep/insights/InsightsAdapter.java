/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivitypowersleep.insights;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.InsightMetadata;

import java.util.ArrayList;
import java.util.List;

public class InsightsAdapter extends RecyclerView.Adapter<InsightsAdapter.InsightsInfoViewHolder> {

    interface InsightItemClickListener {
        void onInsightsItemClicked(String momentId);
    }

    public static final String TAG =InsightsAdapter.class.getSimpleName();
    private Context context;
    private InsightItemClickListener insightItemClickListener;
    private List<Insight> insightList;

    public void setInsightItemClickListener(InsightItemClickListener insightItemClickListener) {
        this.insightItemClickListener = insightItemClickListener;
    }

    public void removeInsightItemClickListener() {
        this.insightItemClickListener = null;
    }

    public InsightsAdapter(Context context, List<Insight> insightList) {
        this.insightList = insightList;
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
        Insight insight = insightList.get(position);
        holder.tvTitle.setText(insight.getRuleId());
        StringBuffer sb = new StringBuffer("");
        sb.append("Date : " + insight.getTimeStamp());
        sb.append("\nMomentId : " + insight.getMomentId());
        holder.tvDetail.setText(sb.toString());

        holder.tvTitle.setTag(insight.getRuleId());
    }

    @Override
    public int getItemCount() {
        if (insightList != null) {
            return insightList.size();
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
