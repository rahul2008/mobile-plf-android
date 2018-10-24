/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo.insights;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.R;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
import java.util.List;

class InsightAdapter extends RecyclerView.Adapter<InsightAdapter.InsightHolder> {

    private List<? extends Insight> mInsightList;
    private final DBRequestListener<Insight> mDBRequestListener;
    private final OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClicked(Insight insight);
    }

    InsightAdapter(ArrayList<? extends Insight> insightList, DBRequestListener<Insight> dbRequestListener, OnItemClickListener clickListener) {
        this.mDBRequestListener = dbRequestListener;
        this.mInsightList = insightList;
        this.clickListener = clickListener;
    }

    @Override
    public InsightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.insight_row_item, null);
        return new InsightHolder(view);
    }

    @Override
    public void onBindViewHolder(final InsightHolder holder, int position) {
        final Insight insight = mInsightList.get(position);
        String expirationDateValue = getExpirationDateValue(insight);

        final DateTime lastModified = insight.getSynchronisationData().getLastModified();

        holder.mInsightID.setText(insight.getSynchronisationData().getGuid());
        holder.mMomentID.setText(insight.getMomentId());
        holder.mLastModified.setText(lastModified == null ? "" : lastModified.toString(ISODateTimeFormat.dateTime()));
        holder.mRuleID.setText(insight.getRuleId());
        holder.mExpirationDate.setText(expirationDateValue);

        holder.mDeleteInsight.setOnClickListener(view -> {
            List<Insight> insightsToDelete = new ArrayList<>();
            insightsToDelete.add(insight);
            DataServicesManager.getInstance().deleteInsights(insightsToDelete, mDBRequestListener);
            mInsightList.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
            notifyItemRangeChanged(holder.getAdapterPosition(), getItemCount());
            notifyDataSetChanged();
            notifyDataSetChanged();
        });

        holder.bind(insight, clickListener);
    }

    @NonNull
    private String getExpirationDateValue(Insight insight) {
        return insight.getExpirationDate() == null ?
                "never expires" : insight.getExpirationDate().toString();
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
        TextView mExpirationDate;
        Button mDeleteInsight;

        InsightHolder(final View view) {
            super(view);
            mInsightID = view.findViewById(R.id.insight_id);
            mMomentID = view.findViewById(R.id.moment_id);
            mLastModified = view.findViewById(R.id.last_modified);
            mRuleID = view.findViewById(R.id.rule_id);
            mExpirationDate = view.findViewById(R.id.expiration_date);
            mDeleteInsight = view.findViewById(R.id.btn_delete_insight);
        }

        public void bind(final Insight item, final OnItemClickListener listener) {
            itemView.setOnClickListener(view -> listener.onItemClicked(item));
        }
    }
}
