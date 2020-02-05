/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.profile;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.mya.MyaHelper;
import com.philips.platform.mya.R;
import com.philips.platform.mya.base.MyaBaseFragment;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

import java.util.Map;

public class MyaProfileFragment extends MyaBaseFragment implements MyaProfileContract.View {

    private RecyclerView recyclerView;
    private MyaProfileContract.Presenter presenter;
    private TextView userNameTextView;
    private String PROFILE_BUNDLE = "profile_bundle";
    private DefaultItemAnimator defaultItemAnimator;
    private RecyclerViewSeparatorItemDecoration recyclerViewSeparatorItemDecoration;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mya_profile_fragment, container, false);
        UIDHelper.injectCalligraphyFonts();
        recyclerView = view.findViewById(R.id.profile_recycler_view);
        userNameTextView = view.findViewById(R.id.mya_user_name);
        presenter = new MyaProfilePresenter(this);
        setRetainInstance(true);
        init(new DefaultItemAnimator(),new RecyclerViewSeparatorItemDecoration(getFragmentActivity()),
                new LinearLayoutManager(getFragmentActivity()));
        return view;
    }

    void init(DefaultItemAnimator defaultItemAnimator, RecyclerViewSeparatorItemDecoration recyclerViewSeparatorItemDecoration, LinearLayoutManager linearLayoutManager) {
        this.defaultItemAnimator=defaultItemAnimator;
        this.recyclerViewSeparatorItemDecoration = recyclerViewSeparatorItemDecoration;
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle(PROFILE_BUNDLE, getArguments());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        Bundle arguments;
        if (savedInstanceState == null) {
            arguments = getArguments();
        } else {
            arguments = savedInstanceState.getBundle(PROFILE_BUNDLE);
        }
        presenter.getProfileItems(MyaHelper.getInstance().getAppInfra(), arguments);
    }


    @Override
    public int getActionbarTitleResId() {
        return R.string.MYA_My_account;
    }

    @Override
    public String getActionbarTitle(Context context) {
        return context.getString(R.string.MYA_My_account);
    }

    @Override
    public boolean getBackButtonState() {
        return false;
    }

    @Override
    public void showProfileItems(final Map<String, String> profileList) {
        MyaProfileAdaptor myaProfileAdaptor = new MyaProfileAdaptor(profileList);
        RecyclerView.LayoutManager mLayoutManager = getLinearLayoutManager();
        RecyclerViewSeparatorItemDecoration contentThemedRightSeparatorItemDecoration = getRecyclerViewSeparatorItemDecoration();
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(contentThemedRightSeparatorItemDecoration);
        recyclerView.setItemAnimator(getAnimator());
        recyclerView.setAdapter(myaProfileAdaptor);
        myaProfileAdaptor.setOnClickListener(onClickRecyclerViewItem(profileList));
    }

    protected DefaultItemAnimator getAnimator() {
        return defaultItemAnimator;
    }

    protected RecyclerViewSeparatorItemDecoration getRecyclerViewSeparatorItemDecoration() {
        return recyclerViewSeparatorItemDecoration;
    }

    protected LinearLayoutManager getLinearLayoutManager() {
        return linearLayoutManager;
    }

    private View.OnClickListener onClickRecyclerViewItem(final Map<String, String> profileList) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int viewType = recyclerView.getChildAdapterPosition(view);
                String key = (String) profileList.keySet().toArray()[viewType];
                String value = profileList.get(key);
                String profileItem = value != null ? value : key;
                boolean handled = presenter.handleOnClickProfileItem(profileItem, getArguments());
                if (!handled) {
                    boolean onClickMyaItem = MyaHelper.getInstance().getMyaListener().onProfileMenuItemSelected(getFragmentLauncher(), key);
                    handleAppProfileEvent(onClickMyaItem, profileItem);
                }
            }
        };
    }


    @Override
    public void setUserName(String userName) {
        userNameTextView.setText(userName);
    }

    @Override
    public void showPassedFragment(MyaBaseFragment fragment) {
        fragment.setFragmentLauncher(getFragmentLauncher());
        fragment.setActionbarUpdateListener(getActionbarUpdateListener());
        showFragment(fragment);
    }

    private void handleAppProfileEvent(boolean onClickMyaItem, String profileItem) {
        MyaHelper.getInstance().getMyaLogger().log(LoggingInterface.LogLevel.DEBUG, MyaHelper.MYA_TLA, "Is proposition handling the profile event:" + onClickMyaItem);
        // TODO:code to be added in future
    }
}
