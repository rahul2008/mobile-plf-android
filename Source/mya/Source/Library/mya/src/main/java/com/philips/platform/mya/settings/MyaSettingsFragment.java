/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.mya.MyaHelper;
import com.philips.platform.mya.R;
import com.philips.platform.mya.base.MyaBaseFragment;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

import java.util.HashMap;
import java.util.Map;

import static com.philips.platform.mya.launcher.MyaInterface.appTaggingInterface;
import static com.philips.platform.mya.settings.MyaPhilipsLinkFragment.PHILIPS_LINK;


public class MyaSettingsFragment extends MyaBaseFragment implements View.OnClickListener,MyaSettingsContract.View {


    public static final String ALERT_DIALOG_TAG = "ALERT_DIALOG_TAG";
    private MyaSettingsContract.Presenter presenter;
    private RecyclerView recyclerView;
    private String SETTINGS_BUNDLE = "settings_bundle";
    private boolean isDialogOpen = false;
    private String DIALOG_TITLE = "dialog_title", DIALOG_MESSAGE = "dialog_message", DIALOG_OPEN = "dialog_open";
    private String dialogTitle,dialogMessage;
    private DefaultItemAnimator defaultItemAnimator;
    private RecyclerViewSeparatorItemDecoration recyclerViewSeparatorItemDecoration;
    private LinearLayoutManager linearLayoutManager;
    private AppConfigurationInterface.AppConfigurationError error;
    private Button logout;
    private Label philipsWebsite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mya_settings_fragment, container, false);
        UIDHelper.injectCalligraphyFonts();
        setRetainInstance(true);
        initViews(view);
        presenter = new MyaSettingsPresenter(this);
        init(new DefaultItemAnimator(),new RecyclerViewSeparatorItemDecoration(getContext()),
                new LinearLayoutManager(getContext()));
        error = new AppConfigurationInterface.AppConfigurationError();
        return view;
    }

    void init(DefaultItemAnimator defaultItemAnimator, RecyclerViewSeparatorItemDecoration recyclerViewSeparatorItemDecoration, LinearLayoutManager linearLayoutManager) {
        this.defaultItemAnimator=defaultItemAnimator;
        this.recyclerViewSeparatorItemDecoration = recyclerViewSeparatorItemDecoration;
        this.linearLayoutManager = linearLayoutManager;
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
        philipsWebsite = view.findViewById(R.id.philips_website);
        logOutButton.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(appTaggingInterface!=null) {
            appTaggingInterface.trackPageWithInfo("MYA_01_06_settings_page","MYA_01_06_settings_page","My Settings page");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean(DIALOG_OPEN)) {
                dismissDialog();
                showDialog(savedInstanceState.getString(DIALOG_TITLE), savedInstanceState.getString(DIALOG_MESSAGE));
            } else {
                dismissDialog();
            }
            presenter.getSettingItems(MyaHelper.getInstance().getAppInfra(), error);
        } else {
            presenter.getSettingItems(MyaHelper.getInstance().getAppInfra(), error);
        }


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
        }
    }

    private void showDialog(String title, String message) {
        this.dialogTitle = title;
        this.dialogMessage = message;
        this.isDialogOpen = true;
        LayoutInflater inflater = LayoutInflater.from(getContext()).cloneInContext(UIDHelper.getPopupThemedContext(getContext()));
        View dialogView = inflater.inflate(R.layout.mya_dialog_layout, null);
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getContext())
                .setDialogType(DialogConstants.TYPE_DIALOG)
                .setDialogView(dialogView)
                .setDimLayer(DialogConstants.DIM_SUBTLE)
                .setCancelable(false)
                .setDividers(false);

        Label textView = dialogView.findViewById(R.id.message_label);
        Label title_label = dialogView.findViewById(R.id.title_label);
        logout = dialogView.findViewById(R.id.mya_dialog_logout_btn);
        Button cancel = dialogView.findViewById(R.id.mya_dialog_cancel_btn);
        textView.setText(message);
        title_label.setText(title);
        AlertDialogFragment alertDialogFragment = builder.create();
        alertDialogFragment.show(getChildFragmentManager(), ALERT_DIALOG_TAG);

        logout.setOnClickListener(onClickLogOut(logout));
        cancel.setOnClickListener(handleOnClickCancel());
    }

    private View.OnClickListener handleOnClickCancel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDialogOpen = false;
                dismissDialog();
                if(appTaggingInterface != null) {
                    Map<String,String> map = new HashMap<>();
                    map.put("inAppNotification","Are you sure you want to log out?");
                    map.put("inAppNotificationResponse","Cancel");
                    appTaggingInterface.trackActionWithInfo("sendData",map);
                }
            }
        };
    }

    private void dismissDialog() {
        final FragmentActivity activity = getActivity();
        if (activity != null && !activity.isDestroyed()) {
            Fragment prev = getChildFragmentManager().findFragmentByTag(ALERT_DIALOG_TAG);
            if (prev != null && prev instanceof AlertDialogFragment) {
                AlertDialogFragment alertDialogFragment = (AlertDialogFragment) prev;
                alertDialogFragment.dismissAllowingStateLoss();
            }
        }
    }

    private View.OnClickListener onClickLogOut(final Button logout) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* boolean onLogOut = MyaHelper.getInstance().getMyaListener().onMenuItemSelected(view.getContext().getString(R.string.mya_log_out));
                if(!onLogOut) {
                    presenter.logOut(getArguments());
                }*/
                //TODO - need to invoke above commented code when introduced call back for log out success
                presenter.logOut(getArguments());
                dismissDialog();
                if(appTaggingInterface != null) {
                    Map<String,String> map = new HashMap<>();
                    map.put("inAppNotification","Are you sure you want to log out?");
                    map.put("inAppNotificationResponse","Log out");
                    appTaggingInterface.trackActionWithInfo("sendData",map);
                }
            }
        };
    }

    @Override
    public void showSettingsItems(Map<String, SettingsModel> dataModelLinkedHashMap) {
        MyaSettingsAdapter myaSettingsAdaptor = new MyaSettingsAdapter(getContext(),dataModelLinkedHashMap);
        RecyclerView.LayoutManager mLayoutManager = getLinearLayoutManager();
        RecyclerViewSeparatorItemDecoration contentThemedRightSeparatorItemDecoration = getRecyclerViewSeparatorItemDecoration();
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(contentThemedRightSeparatorItemDecoration);
        recyclerView.setItemAnimator(getAnimator());
        recyclerView.setAdapter(myaSettingsAdaptor);
        myaSettingsAdaptor.setOnClickListener(getOnClickListener(dataModelLinkedHashMap));
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

    @SuppressWarnings("deprecation")
    @Override
    public void setLinkUrl(final String url) {
        final SpannableString myString;
        if (!TextUtils.isEmpty(url)) {
            philipsWebsite.setClickable(true);
            myString = new SpannableString(url);
            ClickableSpan clickableSpan = getClickableSpan(url);
            myString.setSpan(clickableSpan, 0, url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            String philipsDefaultUrl = getString(R.string.MYA_philips_website);
            myString = new SpannableString(philipsDefaultUrl);
            ClickableSpan clickableSpan = getClickableSpan(philipsDefaultUrl);
            myString.setSpan(clickableSpan, 0, philipsDefaultUrl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        philipsWebsite.setText(myString);
        philipsWebsite.setMovementMethod(LinkMovementMethod.getInstance());
    }

    ClickableSpan getClickableSpan(final String url) {
        return new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                MyaPhilipsLinkFragment fragment = new MyaPhilipsLinkFragment();
                Bundle args = new Bundle();
                args.putString(PHILIPS_LINK, url);
                fragment.setArguments(args);
                AppIdentityInterface appIdentityInterface = MyaHelper.getInstance().getAppInfra().getAppIdentity();
                String appName = appIdentityInterface.getAppName();
                String url = getString(R.string.MYA_philips_website)+"?origin=15_global_en_"+appName+"-app_"+appName+"-app";
                if(appTaggingInterface != null) {
                    appTaggingInterface.trackActionWithInfo("sendData", "exitLinkName", url);
                }
                showFragment(fragment);
            }
        };
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
                    boolean onClickMyaItem = MyaHelper.getInstance().getMyaListener().onSettingsMenuItemSelected(key);
                    if (!onClickMyaItem)
                        presenter.onClickRecyclerItem(key, value);
                }

            }
        };
    }

    AppConfigurationInterface.AppConfigurationError getError() {
        return error;
    }
}
