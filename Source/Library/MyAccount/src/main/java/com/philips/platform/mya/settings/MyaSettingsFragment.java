/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.csw.ConsentBundleConfig;
import com.philips.platform.csw.CswLaunchInput;
import com.philips.platform.mya.R;
import com.philips.platform.mya.base.mvp.MyaBaseFragment;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

import java.util.LinkedHashMap;


public class MyaSettingsFragment extends MyaBaseFragment implements View.OnClickListener,MyaSettingsContract.View {


    private AppInfraInterface appInfra;
    private ConsentBundleConfig config;
    public static final String ALERT_DIALOG_TAG = "ALERT_DIALOG_TAG";
    private MyaSettingsContract.Presenter presenter;
    private RecyclerView recyclerView;

    public static MyaSettingsFragment newInstance(ConsentBundleConfig config){
        final MyaSettingsFragment fragment = new MyaSettingsFragment();
        fragment.setArguments(config.toBundle());
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mya_settings_fragment, container, false);
        UIDHelper.injectCalligraphyFonts();
        initViews(view);
        recyclerView.setNestedScrollingEnabled(false);
        presenter = new MyaSettingsPresenter(this);
        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.mya_settings_recycler_view);
        Button logOutButton = view.findViewById(R.id.mya_settings_logout_btn);
        Label philipsWebsite = view.findViewById(R.id.philips_website);
       // this.config = new ConsentBundleConfig(getArguments());
        this.appInfra = MyaInterface.getMyaDependencyComponent().getAppInfra();
       /* TextView countryTextView = (TextView) view.findViewById(R.id.settings_country_value);
        Button logOutButton = (Button) view.findViewById(R.id.mya_settings_logout_btn);
        RelativeLayout consentLayout = (RelativeLayout) view.findViewById(R.id.consent_layout);
        RelativeLayout countryLayout = (RelativeLayout) view.findViewById(R.id.country_layout);
        countryTextView.setText(appInfra.getServiceDiscovery().getHomeCountry());
        Label philipsWebsite = (Label) view.findViewById(R.id.philips_website);
        consentLayout.setOnClickListener(this);
        countryLayout.setOnClickListener(this);
>>>>>>> c83af7c2288cf2234c1f7597198f585cf0dcc6b1*/
        philipsWebsite.setOnClickListener(this);
        logOutButton.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.getSettingItems(getContext(), MyaInterface.getMyaDependencyComponent().getAppInfra());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            config = new ConsentBundleConfig(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putAll(config.toBundle());
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
    public void onClick(View view) {
        int viewType = view.getId();
        if (viewType == R.id.mya_settings_logout_btn) {
            showDialog(getString(R.string.MYA_logout_title), getString(R.string.MYA_logout_message));
        } else if (viewType == R.id.philips_website) {

       /* int viewId = view.getId();
        if (viewId== R.id.consent_layout) {
            CswInterface cswInterface = new CswInterface();
            CswDependencies cswDependencies = new CswDependencies(appInfra);
            cswDependencies.setApplicationName(config.getApplicationName());
            cswDependencies.setPropositionName(config.getPropositionName());
            UappSettings uappSettings = new UappSettings(getContext());
            cswInterface.init(cswDependencies, uappSettings);
            cswInterface.launch(MyaInterface.getMyaUiComponent().getFragmentLauncher(), buildLaunchInput(true));
        } else if (viewId == R.id.philips_website) {
>>>>>>> c83af7c2288cf2234c1f7597198f585cf0dcc6b1*/
            String url = "http://www.Philips.com";
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }

    private CswLaunchInput buildLaunchInput(boolean addToBackStack) {
        CswLaunchInput cswLaunchInput = new CswLaunchInput(config, getContext());
        cswLaunchInput.addToBackStack(addToBackStack);
        return cswLaunchInput;
    }

    @Override
    public void showDialog(String title, String message) {
        View view = View.inflate(getContext(), R.layout.mya_dialog_layout, null);
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getContext())
                .setDialogType(DialogConstants.TYPE_DIALOG)
                .setDialogView(view)
                .setMessage(message)
                .setDimLayer(DialogConstants.DIM_SUBTLE)
                .setCancelable(false)
                .setTitle(title);
        TextView textView = view.findViewById(R.id.message_label);
        Button logout = view.findViewById(R.id.mya_dialog_logout_btn);
        Button cancel = view.findViewById(R.id.mya_dialog_cancel_btn);
        textView.setText(message);
        final AlertDialogFragment alertDialogFragment = builder.create();
        alertDialogFragment.show(getFragmentManager(), ALERT_DIALOG_TAG);

        logout.setOnClickListener(handleOnClickLogOut(alertDialogFragment));
        cancel.setOnClickListener(handleOnClickCancel(alertDialogFragment));
    }

    private View.OnClickListener handleOnClickCancel(final AlertDialogFragment alertDialogFragment) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogFragment.dismiss();
            }
        };
    }

    private View.OnClickListener handleOnClickLogOut(final AlertDialogFragment alertDialogFragment) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogFragment.dismiss();
                exitMyAccounts();
                MyaInterface.getMyaUiComponent().getMyaListener().onLogOut();
            }
        };
    }

    @Override
    public void showSettingsItems(LinkedHashMap<String, SettingsModel> dataModelLinkedHashMap) {
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
    public void showFragment(Fragment fragment) {
        super.showFragment(fragment);
    }

    private View.OnClickListener getOnClickListener(final LinkedHashMap<String, SettingsModel> profileList) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int viewType = recyclerView.indexOfChild(view);
                String key = (String) profileList.keySet().toArray()[viewType];
                SettingsModel value = profileList.get(key);
                boolean onClickMyaItem = MyaInterface.getMyaUiComponent().getMyaListener().onClickMyaItem(key);
                if (!onClickMyaItem)
                    presenter.onClickRecyclerItem(getContext(), key, value);
            }
        };
    }
}
