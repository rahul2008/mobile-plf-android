/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.permission;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.csw.ConsentBundleConfig;
import com.philips.platform.csw.CswBaseFragment;
import com.philips.platform.mya.consentwidgets.R;
import com.philips.platform.mya.consentwidgets.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PermissionView extends CswBaseFragment implements PermissionInterface, HelpClickListener {

    private ProgressDialog mProgressDialog;

    private Unbinder unbinder;

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

        PermissionPresenter permissionPresenter = injectPresenter();
        permissionPresenter.getConsentStatus();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(permissionPresenter.getAdapter());
    }

    private PermissionPresenter injectPresenter() {
        return DaggerPermissionComponent.builder().permissionModule(new PermissionModule(this, this, config)).build().presenter();
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
    public void showErrorDialog(ConsentNetworkError error) {
        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onHelpClicked(String helpText) {
        Toast.makeText(getContext(), helpText, Toast.LENGTH_LONG).show();
    }
}
