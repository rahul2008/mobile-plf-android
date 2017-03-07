package cdp.philips.com.mydemoapp.insights;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cdp.philips.com.mydemoapp.R;

public class InsightAdapter extends RecyclerView.Adapter<InsightAdapter.InsightHolder> {

    private List<Insight> mInsightList;

    public InsightAdapter(final List<Insight> insightList) {
        mInsightList = insightList;
    }

    @Override
    public InsightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.insight_row_item, null);
        return new InsightHolder(view);
    }

    @Override
    public void onBindViewHolder(InsightHolder holder, int position) {
        final Insight insight = mInsightList.get(position);
        holder.mLastModified.setText(insight.getLastModified());
        holder.mTimeStamp.setText(insight.getTimeStamp());
        holder.mRuleID.setText(insight.getRuleID());
        holder.mMomentType.setText(insight.getMomentType());
    }

    @Override
    public int getItemCount() {
        return mInsightList.size();
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
