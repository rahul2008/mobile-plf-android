/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package cdp.philips.com.mydemoapp.insights;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.ArrayList;
import java.util.List;

import cdp.philips.com.mydemoapp.R;

public class InsightAdapter extends RecyclerView.Adapter<InsightAdapter.InsightHolder> {

    private List<InsightDisplayModel> mInsightDisplayModelList;
    private List<? extends Insight> mInsightList;
    private final DBRequestListener dbRequestListener;

    public InsightAdapter(final List<InsightDisplayModel> insightDisplayModelList, ArrayList<? extends Insight> insightList, DBRequestListener dbRequestListener) {
        mInsightDisplayModelList = insightDisplayModelList;
        this.dbRequestListener = dbRequestListener;
        this.mInsightList = insightList;
    }

    @Override
    public InsightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.insight_row_item, null);
        return new InsightHolder(view);
    }

    @Override
    public void onBindViewHolder(InsightHolder holder, final int position) {
        final InsightDisplayModel insightDisplayModel = mInsightDisplayModelList.get(position);
        holder.mInsightID.setText(insightDisplayModel.getInsightID());
        holder.mMomentID.setText(insightDisplayModel.getMomentID());
        holder.mLastModified.setText(insightDisplayModel.getLastModified());
        holder.mRuleID.setText(insightDisplayModel.getRuleID());
        holder.mDeleteInsight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Insight> insightsToDelete = new ArrayList();
                insightsToDelete.add(mInsightList.get(position));
                DataServicesManager.getInstance().deleteInsights(insightsToDelete, dbRequestListener);
                mInsightList.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mInsightDisplayModelList.size();
    }

    public void setInsightList(final ArrayList<? extends Insight> insightList) {
        mInsightList = insightList;
        mInsightDisplayModelList.clear();
        for (Insight insight : mInsightList) {
            InsightDisplayModel insightDisplayModel = new InsightDisplayModel();
            insightDisplayModel.setInsightID(insight.getGUId());
            insightDisplayModel.setMomentID(insight.getMomentId());
            insightDisplayModel.setLastModified(insight.getLastModified());
            insightDisplayModel.setRuleID(insight.getRuleId());
            mInsightDisplayModelList.add(insightDisplayModel);
        }
    }

    public class InsightHolder extends RecyclerView.ViewHolder {
        TextView mInsightID;
        TextView mMomentID;
        TextView mLastModified;
        TextView mRuleID;
        Button mDeleteInsight;

        public InsightHolder(final View view) {
            super(view);
            mInsightID = (TextView) view.findViewById(R.id.insight_id);
            mMomentID = (TextView) view.findViewById(R.id.moment_id);
            mLastModified = (TextView) view.findViewById(R.id.last_modified);
            mRuleID = (TextView) view.findViewById(R.id.rule_id);
            mDeleteInsight = (Button) view.findViewById(R.id.btn_delete_insight);
        }
    }
}
