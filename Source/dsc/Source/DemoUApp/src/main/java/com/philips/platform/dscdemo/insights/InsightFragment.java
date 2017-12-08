/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo.insights;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.DSBaseFragment;
import com.philips.platform.dscdemo.R;

import java.util.ArrayList;
import java.util.List;

public class InsightFragment extends DSBaseFragment
        implements DBRequestListener<Insight>, DBFetchRequestListner<Insight>, DBChangeListener {

    private ArrayList<? extends Insight> mInsightList = new ArrayList<>();

    private InsightAdapter mInsightAdapter;
    private DataServicesManager mDataServicesManager;

    private RecyclerView mInsightsRecyclerView;
    private TextView mNoInsights;

    @Override
    public int getActionbarTitleResId() {
        return R.string.insights_title;
    }

    @Override
    public String getActionbarTitle() {
        return getString(R.string.insights_title);
    }

    @Override
    public boolean getBackButtonState() {
        return true;
    }

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
    public void dBChangeFailed(final Exception e) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "Exception :" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
        refreshOnFailure(exception);
    }

    @Override
    public void onSuccess(List<? extends Insight> insights) {
        updateUI(insights);
    }

    @Override
    public void onFailure(Exception exception) {
        refreshOnFailure(exception);
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

    private void refreshOnFailure(final Exception exception) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
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


