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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.csw.ConsentBundleConfig;
import com.philips.platform.csw.CswBaseFragment;
import com.philips.platform.csw.description.DescriptionView;
import com.philips.platform.csw.utils.CswLogger;
import com.philips.platform.mya.consentwidgets.R;
import com.philips.platform.mya.consentwidgets.R2;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PermissionView extends CswBaseFragment implements PermissionInterface, HelpClickListener {

    private static final String TAG = "PermissionView";

    private ProgressDialog mProgressDialog;

    private Unbinder unbinder;

    private ConsentBundleConfig config;

    @BindView(R2.id.consentList)
    RecyclerView recyclerView;

    private RecyclerViewSeparatorItemDecoration separatorItemDecoration;

    @Override
    protected void setViewParams(Configuration config, int width) {
        //Update recycle view rows
        //  applyParams(config, recyclerView, width);
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
        separatorItemDecoration = new RecyclerViewSeparatorItemDecoration(getContext());
        recyclerView.addItemDecoration(separatorItemDecoration);
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
        CswLogger.e(TAG, error.getMessage());
        OkOnErrorListener okListener = new OkOnErrorListener();
        final AlertDialogFragment alertDialogFragment = new AlertDialogFragment.Builder(getContext())
                .setTitle(R.string.csw_problem_occurred_error_title)
                .setMessage(getString(R.string.csw_problem_occurred_error_message, error.getCatkErrorCode()))
                .setPositiveButton(com.philips.cdp.registration.R.string.reg_DLS_Button_Title_Ok, okListener)
                .create();
        okListener.setDialog(alertDialogFragment);
        alertDialogFragment.show(getFragmentManager(), TAG);
    }

    @Override
    public void onHelpClicked(String helpText) {
        DescriptionView.show(getFragmentManager(), helpText);
    }

    private static class OkOnErrorListener implements View.OnClickListener {

        @Nullable
        DialogFragment dialog;

        @Override
        public void onClick(View view) {
            if (dialog != null) {
                dialog.dismiss();
            }
        }

        void setDialog(@NonNull DialogFragment dialog) {
            this.dialog = dialog;
        }
    }
}
