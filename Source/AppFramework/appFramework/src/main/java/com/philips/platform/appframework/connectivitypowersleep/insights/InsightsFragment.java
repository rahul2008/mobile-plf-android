/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivitypowersleep.insights;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseFragment;

import java.util.ArrayList;

public class InsightsFragment extends AbstractAppFrameworkBaseFragment {
    public static final String TAG = InsightsFragment.class.getSimpleName();
    private RecyclerView recyclerViewInsights;
    private ArrayList<String> insightsTitleItemList = new ArrayList<String>();
    private ArrayList<String> insightsDescItemList = new ArrayList<String>();


    private InsightsAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        ((AbstractAppFrameworkBaseActivity) getActivity()).updateActionBarIcon(true);
    }

    @Override
    public String getActionbarTitle() {
        return getString(R.string.RA_DLS_ps_tips_title);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_insights, container, false);
        recyclerViewInsights = (RecyclerView) view.findViewById(R.id.insights_recycler_view);
        recyclerViewInsights.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        startAppTagging(TAG);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeTipsList();
        adapter = new InsightsAdapter(getActivity(), insightsTitleItemList, insightsDescItemList);
        recyclerViewInsights.setAdapter(adapter);
    }

    public void initializeTipsList() {

        insightsTitleItemList.add(getString(R.string.sleep_tip_title_1));
        insightsTitleItemList.add(getString(R.string.sleep_tip_title_2));
        insightsTitleItemList.add(getString(R.string.sleep_tip_title_3));

        insightsDescItemList.add(getString(R.string.sleep_tip_desc_1));
        insightsDescItemList.add(getString(R.string.sleep_tip_desc_2));
        insightsDescItemList.add(getString(R.string.sleep_tip_desc_3));


    }
}
