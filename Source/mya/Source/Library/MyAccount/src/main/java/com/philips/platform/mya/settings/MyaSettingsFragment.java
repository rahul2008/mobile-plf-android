/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.settings;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.mya.MyaHelper;
import com.philips.platform.mya.R;
import com.philips.platform.mya.base.mvp.MyaBaseFragment;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

import java.util.Map;


public class MyaSettingsFragment extends MyaBaseFragment implements View.OnClickListener,MyaSettingsContract.View {


    public static final String ALERT_DIALOG_TAG = "ALERT_DIALOG_TAG";
    private MyaSettingsContract.Presenter presenter;
    private RecyclerView recyclerView;
    private String SETTINGS_BUNDLE = "settings_bundle";
    private boolean isDialogOpen = false;
    private String DIALOG_TITLE = "dialog_title", DIALOG_MESSAGE = "dialog_message", DIALOG_OPEN = "dialog_open";
    private String dialogTitle,dialogMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mya_settings_fragment, container, false);
        UIDHelper.injectCalligraphyFonts();
        setRetainInstance(true);
        initViews(view);
        recyclerView.setNestedScrollingEnabled(false);
        presenter = new MyaSettingsPresenter(this);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        dismissDialog();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.mya_settings_recycler_view);
        Button logOutButton = view.findViewById(R.id.mya_settings_logout_btn);
        Label philipsWebsite = view.findViewById(R.id.philips_website);
        philipsWebsite.setOnClickListener(this);
        logOutButton.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState == null) {
            presenter.getSettingItems(MyaHelper.getInstance().getAppInfra(), new AppConfigurationInterface.AppConfigurationError());
        } else {
            presenter.getSettingItems(MyaHelper.getInstance().getAppInfra(), new AppConfigurationInterface.AppConfigurationError());
            if (savedInstanceState.getBoolean(DIALOG_OPEN)) {
                dismissDialog();
                showDialog(savedInstanceState.getString(DIALOG_TITLE), savedInstanceState.getString(DIALOG_MESSAGE));
            } else {
                dismissDialog();
            }
        }

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle(SETTINGS_BUNDLE, getArguments());
        outState.putString(DIALOG_MESSAGE, dialogMessage);
        outState.putString(DIALOG_TITLE, dialogTitle);
        outState.putBoolean(DIALOG_OPEN, isDialogOpen);
        super.onSaveInstanceState(outState);
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
    public void onClick(View view) {
        int viewType = view.getId();
        if (viewType == R.id.mya_settings_logout_btn) {
            showDialog(getString(R.string.MYA_logout_title), getString(R.string.MYA_logout_message));
        } else if (viewType == R.id.philips_website) {
            String url = "https://www.Philips.com";
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }


    @Override
    public void showDialog(String title, String message) {
        this.dialogTitle = title;
        this.dialogMessage = message;
        this.isDialogOpen = true;
        LayoutInflater inflater = LayoutInflater.from(getContext()).cloneInContext(UIDHelper.getPopupThemedContext(getContext()));
        View view = inflater.inflate(R.layout.mya_dialog_layout, null);
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getContext())
                .setDialogType(DialogConstants.TYPE_DIALOG)
                .setDialogView(view)
                .setDimLayer(DialogConstants.DIM_SUBTLE)
                .setCancelable(false)
                .setDividers(false);

        Label textView = view.findViewById(R.id.message_label);
        Label title_label = view.findViewById(R.id.title_label);
        Button logout = view.findViewById(R.id.mya_dialog_logout_btn);
        Button cancel = view.findViewById(R.id.mya_dialog_cancel_btn);
        textView.setText(message);
        title_label.setText(title);
        AlertDialogFragment alertDialogFragment = builder.create();
        alertDialogFragment.show(getFragmentManager(), ALERT_DIALOG_TAG);

        logout.setOnClickListener(handleOnClickLogOut(alertDialogFragment));
        cancel.setOnClickListener(handleOnClickCancel(alertDialogFragment));
    }

    private View.OnClickListener handleOnClickCancel(final AlertDialogFragment alertDialogFragment) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDialogOpen = false;
                dismissDialog();
            }
        };
    }

    private void dismissDialog() {
        final FragmentActivity activity = getActivity();
        if (activity != null && !activity.isDestroyed()) {
            Fragment prev = getFragmentManager().findFragmentByTag(ALERT_DIALOG_TAG);
            if (prev != null && prev instanceof AlertDialogFragment) {
                AlertDialogFragment alertDialogFragment = (AlertDialogFragment) prev;
                alertDialogFragment.dismissAllowingStateLoss();
            }
        }
    }

    private View.OnClickListener handleOnClickLogOut(final AlertDialogFragment alertDialogFragment) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean onLogOut = MyaHelper.getInstance().getMyaListener().onLogOut();
                if(!onLogOut) {
                    presenter.logOut(getArguments());
                }
            }
        };
    }

    @Override
    public void showSettingsItems(Map<String, SettingsModel> dataModelLinkedHashMap) {
        MyaSettingsAdapter myaSettingsAdaptor = new MyaSettingsAdapter(dataModelLinkedHashMap);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        RecyclerViewSeparatorItemDecoration contentThemedRightSeparatorItemDecoration = new RecyclerViewSeparatorItemDecoration(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(contentThemedRightSeparatorItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myaSettingsAdaptor);
        myaSettingsAdaptor.setOnClickListener(getOnClickListener(dataModelLinkedHashMap));
    }

    @Override
    public void handleLogOut() {
        dismissDialog();
        exitMyAccounts();
    }

    private View.OnClickListener getOnClickListener(final Map<String, SettingsModel> profileList) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int viewType = recyclerView.indexOfChild(view);
                String key = (String) profileList.keySet().toArray()[viewType];
                SettingsModel value = profileList.get(key);
                boolean handled = presenter.handleOnClickSettingsItem(key, getFragmentLauncher());
                if (!handled) {
                    boolean onClickMyaItem = MyaHelper.getInstance().getMyaListener().onClickMyaItem(key);
                    if (!onClickMyaItem)
                        presenter.onClickRecyclerItem(key, value);
                }

            }
        };
    }
}
