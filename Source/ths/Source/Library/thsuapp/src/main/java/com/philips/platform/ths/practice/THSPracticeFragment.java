/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.practice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.practice.Practice;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

import static com.philips.platform.ths.utility.THSConstants.THS_PRACTICE_PAGE;


public class THSPracticeFragment extends THSBaseFragment implements THSPracticeListViewInterface{

    public static final String TAG = THSPracticeFragment.class.getSimpleName();

    private THSPracticePresenter mPresenter;
    private RecyclerView mPracticeRecyclerView;
    private THSPracticeRecyclerViewAdapter thsPracticeRecyclerViewAdapter;
    private ActionBarListener actionBarListener;
    private RelativeLayout mRealtiveLayoutPracticeContainer;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ths_practice, container, false);
        mPracticeRecyclerView = (RecyclerView)view.findViewById(R.id.ths_recycler_view_practice);
        mRealtiveLayoutPracticeContainer = (RelativeLayout)view.findViewById(R.id.activity_main);
        mPresenter = new THSPracticePresenter(this);
        mPracticeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(null!=(mPresenter)) {
            createCustomProgressBar(mRealtiveLayoutPracticeContainer,BIG);
            ( mPresenter).fetchPractices();
        }
        THSManager.getInstance().getThsTagging().trackPageWithInfo(THS_PRACTICE_PAGE,null,null);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        actionBarListener = getActionBarListener();
        if(null != actionBarListener){
            actionBarListener.updateActionBar(getString(R.string.ths_practice_screen_title),true);
        }
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return getActivity();
    }

    public void showPracticeList(THSPracticeList practices){
        hideProgressBar();
        thsPracticeRecyclerViewAdapter = new THSPracticeRecyclerViewAdapter(getActivity(), practices);
        mPracticeRecyclerView.setAdapter(thsPracticeRecyclerViewAdapter);
        thsPracticeRecyclerViewAdapter.setOnPracticeItemClickListener(new OnPracticeItemClickListener() {
            @Override
            public void onItemClick(Practice practice) {
               mPresenter.showProviderList(practice);
               }
        });
    }

    public void showErrorToast(){
        final String string = getString(R.string.ths_se_server_error_toast_message);
        showError(string,true, false);
    }
}
