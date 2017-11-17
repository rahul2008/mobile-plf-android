/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.profile;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.mya.R;
import com.philips.platform.mya.details.MyaDetailsFragment;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.mya.util.mvp.MyaBaseFragment;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

import java.util.List;


public class MyaProfileFragment extends MyaBaseFragment implements MyaProfileContract.View {

    private RecyclerView recyclerView;
    private MyaProfileContract.Presenter presenter;
    private List<String> appConfigList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mya_profile_fragment, container, false);
        UIDHelper.injectCalligraphyFonts();
        recyclerView = (RecyclerView) view.findViewById(R.id.profile_recycler_view);
        presenter = new MyaProfilePresenter(this);
        appConfigList = presenter.getAppConfigProfileItems(getContext(),MyaInterface.getMyaDependencyComponent().getAppInfra().getConfigInterface());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onViewActive(this);
    }

    @Override
    public void onPause() {
        presenter.onViewInactive();
        super.onPause();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.getProfileItems(getContext(), MyaInterface.getMyaDependencyComponent().getAppInfra());
    }


    @Override
    public int getActionbarTitleResId() {
        return R.string.MYA_My_account;
    }

    @Override
    public String getActionbarTitle() {
        return getString(R.string.MYA_My_account);
    }

    @Override
    public boolean getBackButtonState() {
        return false;
    }

    @Override
    public void showProfileItems(final List<String> profileList) {

        MyaProfileAdaptor myaProfileAdaptor = new MyaProfileAdaptor(profileList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        RecyclerViewSeparatorItemDecoration contentThemedRightSeparatorItemDecoration = new RecyclerViewSeparatorItemDecoration(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(contentThemedRightSeparatorItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myaProfileAdaptor);

        myaProfileAdaptor.setOnClickListener(getOnClickListener(profileList));
    }

    private View.OnClickListener getOnClickListener(final List<String> profileList) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int viewType = recyclerView.indexOfChild(view);
                boolean onClickMyaItem = MyaInterface.getMyaUiComponent().getMyaListener().onClickMyaItem(appConfigList.get(viewType));
                if (appConfigList != null && appConfigList.size() != 0)
                    handleTransition(onClickMyaItem, profileList.get(viewType));
                else
                    handleTransition(onClickMyaItem, getContext().getString(R.string.MYA_My_details));
            }
        };
    }

    @Override
    public void setUserName(String userName) {

    }


    private void handleTransition(boolean onClickMyaItem, String profileItem) {
        if (!onClickMyaItem) {
            if (profileItem.equals(getContext().getString(R.string.MYA_My_details))) {
                MyaDetailsFragment myaDetailsFragment = new MyaDetailsFragment();
                myaDetailsFragment.showFragment(myaDetailsFragment, MyaInterface.getMyaUiComponent().getFragmentLauncher());
            }
        }
    }
}
