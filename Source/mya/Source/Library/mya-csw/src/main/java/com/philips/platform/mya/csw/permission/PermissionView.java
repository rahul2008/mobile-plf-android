/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.permission;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.pif.chi.ConsentConfiguration;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.csw.CswBaseFragment;
import com.philips.platform.mya.csw.CswInterface;
import com.philips.platform.mya.csw.description.DescriptionView;
import com.philips.platform.mya.csw.description.PrivacyNoticeFragment;
import com.philips.platform.mya.csw.dialogs.DialogView;
import com.philips.platform.mya.csw.permission.adapter.PermissionAdapter;
import com.philips.platform.mya.csw.utils.CswLogger;
import com.philips.platform.mya.csw.R;
import com.philips.platform.mya.csw.R2;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PermissionView extends CswBaseFragment implements PermissionInterface, HelpClickListener, View.OnClickListener, PrivacyNoticeClickListener {


    private static final String TAG = "PermissionView";
    private static final String PRIVACY_NOTICE_TAG = "PrivacyNoticeTag";
    private ProgressDialog mProgressDialog;

    private Unbinder unbinder;

    @BindView(R2.id.consentList)
    RecyclerView recyclerView;

    private RecyclerViewSeparatorItemDecoration separatorItemDecoration;
    private List<ConsentConfiguration> configs;
    private PermissionAdapter adapter;

    @Override
    protected void setViewParams(Configuration config, int width) {
        // Update recycle view rows
        // applyParams(config, recyclerView, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.csw_privacy_settings;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.csw_permission_view, container, false);
        unbinder = ButterKnife.bind(this, view);

        configs = CswInterface.getCswComponent().getConsentConfigurations();
        if(configs == null) {
            configs = new ArrayList<>();
        }

        handleOrientation(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getRestClient().isInternetReachable()) {
            PermissionPresenter presenter = getPermissionPresenter();
            presenter.getConsentStatus();
        } else{
            showErrorDialog(true, getString(R.string.csw_offline_title), getString(R.string.csw_offline_message));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbindView();
    }

    private void unbindView() {
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new PermissionAdapter(createConsentsList(), this);
        adapter.setPrivacyNoticeClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        separatorItemDecoration = new RecyclerViewSeparatorItemDecoration(getContext());
        recyclerView.addItemDecoration(separatorItemDecoration);
        recyclerView.setAdapter(adapter);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPrivacyNoticeClicked(String url) {
        PrivacyNoticeFragment privacyNoticeFragment = new PrivacyNoticeFragment();
        privacyNoticeFragment.setUrl(url);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.csw_frame_layout_view_container, privacyNoticeFragment, PRIVACY_NOTICE_TAG);
        fragmentTransaction.addToBackStack(PRIVACY_NOTICE_TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }


    @Override
    public void showProgressDialog() {
        if (!(getActivity().isFinishing())) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(getActivity(), R.style.reg_Custom_loaderTheme);
                mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
                mProgressDialog.setCancelable(false);
            }
            mProgressDialog.show();
        }
    }

    @Override
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void showErrorDialog(boolean goBack, String title, String message) {
        CswLogger.e(TAG, message);
        DialogView dialogView = getDialogView(goBack);
        dialogView.showDialog(getCswFragment().getActivity(), title, message);
    }

    @Override
    public void onHelpClicked(String helpText) {
        DescriptionView.show(getFragmentManager(), helpText);
    }

    @Override
    public void onClick(View view) {
        getParentFragment().getFragmentManager().popBackStack();
    }

    private List<ConsentView> createConsentsList() {
        final List<ConsentView> consentViewList = new ArrayList<>();
        for (ConsentConfiguration configuration : configs) {
            for (final ConsentDefinition definition : configuration.getConsentDefinitionList()) {
                consentViewList.add(new ConsentView(definition, configuration.getHandlerInterface()));
            }
        }
        return consentViewList;
    }

    @VisibleForTesting
    protected RestInterface getRestClient() {
        return CswInterface.get().getDependencies().getAppInfra().getRestClient();
    }

    @VisibleForTesting
    protected PermissionPresenter getPermissionPresenter() {
        PermissionPresenter permissionPresenter = new PermissionPresenter(this, configs, adapter);
        permissionPresenter.mContext = getContext();
        return permissionPresenter;
    }

    @NonNull
    private DialogView getDialogView(boolean goBack) {
        DialogView dialogView = new DialogView();
        if(goBack) {
            dialogView = new DialogView(this);
        }
        return dialogView;
    }
}
