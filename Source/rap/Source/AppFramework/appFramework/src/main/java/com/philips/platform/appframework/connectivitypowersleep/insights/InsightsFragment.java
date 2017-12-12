/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivitypowersleep.insights;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseFragment;
import com.philips.platform.baseapp.base.FragmentView;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Label;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InsightsFragment extends AbstractAppFrameworkBaseFragment implements InsightsContract.View, DBChangeListener, InsightsAdapter.InsightItemClickListener ,FragmentView{
    public static final String TAG = InsightsFragment.class.getSimpleName();

    private List<Insight> mInsightList = new ArrayList();
    private DataServicesManager dataServicesManager;


    private InsightsAdapter adapter;

    private InsightsContract.Action insightPresenter;

    @BindView(R.id.insights_error)
    Label errorLabel;

    @BindView(R.id.insights_recycler_view)
    RecyclerView recyclerViewInsights;

    private ProgressDialog progressDialog;

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
        ButterKnife.bind(this, view);
        recyclerViewInsights.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        startAppTagging(TAG);

        dataServicesManager = DataServicesManager.getInstance();

        return view;
    }

    protected InsightsContract.Action getInsightsPresenter() {
        return new InsightsPresenter(this, getActivity().getApplicationContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        dataServicesManager.registerDBChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        dataServicesManager.unRegisterDBChangeListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        insightPresenter= getInsightsPresenter();

        adapter = new InsightsAdapter(getActivity(), mInsightList);
        adapter.setInsightItemClickListener(this);
        recyclerViewInsights.setAdapter(adapter);

        insightPresenter.loadInsights(dataServicesManager);
    }

    @Override
    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.RA_loading_articles));
    }

    @Override
    public void hideProgressDialog() {
        if(progressDialog!=null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showToast(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onInsightLoadSuccess(final List<Insight> insightList) {
        RALog.d(TAG, "onInsightLoadSuccess : size - " + insightList.size());

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mInsightList.clear();
                    mInsightList.addAll(insightList);
                    adapter.notifyDataSetChanged();

                    setErrorViewVisibility(adapter.getItemCount() == 0);
                }
            });
        }
    }

    @Override
    public void onInsightLoadError(String errorMessage) {
        RALog.d(TAG, "onInsightLoadError : " + errorMessage);
        setErrorViewVisibility(adapter.getItemCount() == 0);
    }

    private void setErrorViewVisibility(boolean isVisible) {
        errorLabel.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }


    @Override
    public void dBChangeSuccess(SyncType syncType) {
        RALog.d(TAG, "dBChangeSuccess : syncType - "+ syncType);
        if(syncType == SyncType.INSIGHT) {
            insightPresenter.loadInsights(dataServicesManager);
        }
    }

    @Override
    public void dBChangeFailed(Exception e) {
        RALog.d(TAG, "dBChangeFailed : exception - "+ e.getMessage());
    }

    @Override
    public void onInsightsItemClicked(String momentId) {
        RALog.d(TAG, "Moment id : " + momentId);
        insightPresenter.showArticle(momentId);
    }


    @Override
    public FragmentActivity getFragmentActivity() {
        return getActivity();
    }

    @Override
    public ActionBarListener getActionBarListener() {
        return null;
    }

    @Override
    public int getContainerId() {
        return R.id.frame_container;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.removeInsightItemClickListener();
    }
}
