package cdp.philips.com.mydemoapp.insights;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;

import java.util.ArrayList;
import java.util.List;

import cdp.philips.com.mydemoapp.R;

public class InsightAdapter extends RecyclerView.Adapter<InsightAdapter.InsightHolder> {

    private List<InsightDisplayModel> mInsightDisplayModelList;

    public InsightAdapter(final List<InsightDisplayModel> insightDisplayModelList) {
        mInsightDisplayModelList = insightDisplayModelList;
    }

    @Override
    public InsightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.insight_row_item, null);
        return new InsightHolder(view);
    }

    @Override
    public void onBindViewHolder(InsightHolder holder, int position) {
        final InsightDisplayModel insightDisplayModel = mInsightDisplayModelList.get(position);
        holder.mLastModified.setText(insightDisplayModel.getLastModified());
        holder.mTimeStamp.setText(insightDisplayModel.getTimeStamp());
        holder.mRuleID.setText(insightDisplayModel.getRuleID());
        holder.mMomentType.setText(insightDisplayModel.getMomentType());
    }

    @Override
    public int getItemCount() {
        return mInsightDisplayModelList.size();
    }

    public void setInsightList(final ArrayList<? extends Insight> insightList) {
        for(Insight insight : insightList){
            InsightDisplayModel insightDisplayModel = new InsightDisplayModel();
            insightDisplayModel.setLastModified("lastModified " + insight.getLastModified());
            insightDisplayModel.setTimeStamp("timeStamp " + insight.getTimeStamp());
            insightDisplayModel.setRuleID("ruleID " + insight.getRuleId());
            insightDisplayModel.setMomentType("momentType " + insight.getType());
            mInsightDisplayModelList.add(insightDisplayModel);
        }
    }


    public class InsightHolder extends RecyclerView.ViewHolder {
        TextView mLastModified;
        TextView mTimeStamp;
        TextView mRuleID;
        TextView mMomentType;

        public InsightHolder(final View view) {
            super(view);
            mLastModified = (TextView) view.findViewById(R.id.last_modified);
            mTimeStamp = (TextView) view.findViewById(R.id.timestamp);
            mRuleID = (TextView) view.findViewById(R.id.rule_id);
            mMomentType = (TextView) view.findViewById(R.id.moment_type);
        }
    }
}
