/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.platform.csw.permission;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.csw.ConsentBundleConfig;
import com.philips.platform.csw.ConsentDefinition;
import com.philips.platform.csw.CswBaseFragment;
import com.philips.platform.mya.consentwidgets.R;
import com.philips.platform.mya.consentwidgets.R2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PermissionView extends CswBaseFragment implements
        PermissionInterface {

    private ProgressDialog mProgressDialog;

    private Unbinder unbinder;

    private PermissionAdapter permissionAdapter;

    private ConsentBundleConfig config;

    @BindView(R2.id.consentList)
    RecyclerView recyclerView;

    @Override
    protected void setViewParams(Configuration config, int width) {
        applyParams(config, recyclerView, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.csw_permissions;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.csw_permission_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getArguments() != null) {
            config = new ConsentBundleConfig(getArguments());
        }

        handleOrientation(view);
//        consumeTouch(view);
        return view;
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
    public void onViewStateRestored(Bundle state) {
        super.onViewStateRestored(state);
        if (state != null) {
            config = new ConsentBundleConfig(state);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        if (state != null) {
            state.putAll(config.toBundle());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<ConsentView> consentDefinitions = createConsentDefinitions();

        ConsentAccessToolKit instance = ConsentAccessToolKit.getInstance();
        CreateConsentInteractor createConsentInteractor = new CreateConsentInteractor(instance);
        GetConsentInteractor getConsentInteractor = new GetConsentInteractor(instance, consentDefinitions);

        PermissionPresenter permissionPresenter = new PermissionPresenter(this, getConsentInteractor);
        permissionPresenter.getConsentStatus();

        permissionAdapter = new PermissionAdapter(consentDefinitions, createConsentInteractor);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(permissionAdapter);
    }

    @NonNull
    private List<ConsentView> createConsentDefinitions() {
        final List<ConsentView> consentViewList = new ArrayList<>();
        for(final ConsentDefinition definition : config.getConsentDefinitions()){
            consentViewList.add(new ConsentView(definition));
        }
        return consentViewList;
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
    public void onConsentRetrieved(@NonNull List<ConsentView> consents) {
        if(permissionAdapter != null) {
            permissionAdapter.onConsentRetrieved(consents);
        }
    }

}
