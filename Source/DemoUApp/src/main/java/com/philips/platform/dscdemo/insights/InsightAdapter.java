/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo.insights;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.R;

import java.util.ArrayList;
import java.util.List;

class InsightAdapter extends RecyclerView.Adapter<InsightAdapter.InsightHolder> {

    private List<? extends Insight> mInsightList;
    private final DBRequestListener<Insight> mDBRequestListener;

    InsightAdapter(ArrayList<? extends Insight> insightList, DBRequestListener<Insight> dbRequestListener) {
        this.mDBRequestListener = dbRequestListener;
        this.mInsightList = insightList;
    }

    @Override
    public InsightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.insight_row_item, null);
        return new InsightHolder(view);
    }

    @Override
    public void onBindViewHolder(final InsightHolder holder, int position) {
        final Insight insight = mInsightList.get(position);
        holder.mInsightID.setText(insight.getGUId());
        holder.mMomentID.setText(insight.getMomentId());
        holder.mLastModified.setText(insight.getLastModified());
        holder.mRuleID.setText(insight.getRuleId());
        holder.mDeleteInsight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Insight> insightsToDelete = new ArrayList<>();
                insightsToDelete.add(insight);
                DataServicesManager.getInstance().deleteInsights(insightsToDelete, mDBRequestListener);
                mInsightList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), getItemCount());
                notifyDataSetChanged();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mInsightList.size();
    }

    void setInsightList(final ArrayList<? extends Insight> insightList) {
        mInsightList = insightList;
    }

    class InsightHolder extends RecyclerView.ViewHolder {
        TextView mInsightID;
        TextView mMomentID;
        TextView mLastModified;
        TextView mRuleID;
        Button mDeleteInsight;

        InsightHolder(final View view) {
            super(view);
            mInsightID = (TextView) view.findViewById(R.id.insight_id);
            mMomentID = (TextView) view.findViewById(R.id.moment_id);
            mLastModified = (TextView) view.findViewById(R.id.last_modified);
            mRuleID = (TextView) view.findViewById(R.id.rule_id);
            mDeleteInsight = (Button) view.findViewById(R.id.btn_delete_insight);
        }
    }
}
