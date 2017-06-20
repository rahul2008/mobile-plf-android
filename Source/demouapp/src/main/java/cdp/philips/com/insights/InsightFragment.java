/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package cdp.philips.com.insights;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.ArrayList;
import java.util.List;

import cdp.philips.com.R;


public class InsightFragment extends Fragment implements DBRequestListener<Insight>, DBFetchRequestListner<Insight>, DBChangeListener {
    InsightAdapter mInsightAdapter;
    RecyclerView mInsightsRecyclerView;
    ArrayList<? extends Insight> mInsightList = new ArrayList();
    DataServicesManager mDataServicesManager;
    private TextView mNoInsights;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insight_layout, container, false);

        mDataServicesManager = DataServicesManager.getInstance();

        mInsightAdapter = new InsightAdapter(mInsightList, this);

        mNoInsights = (TextView) view.findViewById(R.id.tv_no_insights);
        mInsightsRecyclerView = (RecyclerView) view.findViewById(R.id.insight_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mInsightsRecyclerView.setLayoutManager(layoutManager);
        mInsightsRecyclerView.setAdapter(mInsightAdapter);

        mDataServicesManager.fetchInsights(this);

        mInsightAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                if (itemCount == 0) {
                    mNoInsights.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void dBChangeSuccess(SyncType type) {
        if (type == SyncType.INSIGHT) {
            DataServicesManager.getInstance().fetchInsights(this);
        }
    }

    @Override
    public void dBChangeFailed(Exception e) {

    }

    @Override
    public void onFetchSuccess(final List<? extends Insight> data) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUI(data);
            }
        });
    }

    @Override
    public void onFetchFailure(Exception exception) {

    }

    @Override
    public void onSuccess(List<? extends Insight> insights) {
        updateUI(insights);
    }

    @Override
    public void onFailure(Exception exception) {

    }

    public void updateUI(final List<? extends Insight> insights) {
        if (getActivity() != null && insights != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mInsightList = (ArrayList<? extends Insight>) insights;

                    if (mInsightList.size() > 0) {
                        mInsightsRecyclerView.setVisibility(View.VISIBLE);
                        mNoInsights.setVisibility(View.GONE);

                        mInsightAdapter.setInsightList(mInsightList);
                        mInsightAdapter.notifyDataSetChanged();
                    } else {
                        mInsightsRecyclerView.setVisibility(View.GONE);
                        mNoInsights.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        mDataServicesManager.registerDBChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        DataServicesManager.getInstance().unRegisterDBChangeListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}


