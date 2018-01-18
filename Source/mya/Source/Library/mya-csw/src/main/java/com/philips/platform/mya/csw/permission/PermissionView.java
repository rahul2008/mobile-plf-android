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
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.mya.chi.ConsentConfiguration;
import com.philips.platform.mya.chi.ConsentError;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.csw.CswBaseFragment;
import com.philips.platform.mya.csw.CswInterface;
import com.philips.platform.mya.csw.description.DescriptionView;
import com.philips.platform.mya.csw.dialogs.DialogView;
import com.philips.platform.mya.csw.permission.adapter.PermissionAdapter;
import com.philips.platform.mya.csw.utils.CswLogger;
import com.philips.platform.mya.csw.R;
import com.philips.platform.mya.csw.R2;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PermissionView extends CswBaseFragment implements PermissionInterface, HelpClickListener {

    private static final String TAG = "PermissionView";

    private ProgressDialog mProgressDialog;

    private Unbinder unbinder;

    @BindView(R2.id.consentList)
    RecyclerView recyclerView;

    private RecyclerViewSeparatorItemDecoration separatorItemDecoration;
    private List<ConsentConfiguration> configs;
    private PermissionAdapter adapter;

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

//        List<ConsentView> consentItems = createConsentsList();
        PermissionPresenter permissionPresenter = new PermissionPresenter(this, configs, adapter);
        permissionPresenter.getConsentStatus();
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
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new PermissionAdapter(createConsentsList(), this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        separatorItemDecoration = new RecyclerViewSeparatorItemDecoration(getContext());
        recyclerView.addItemDecoration(separatorItemDecoration);
        recyclerView.setAdapter(adapter);
    }

//    private PermissionPresenter injectPresenter() {
//        return DaggerPermissionComponent.builder().permissionModule(new PermissionModule(this, this)).build().presenter();
//    }

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
    public void showErrorDialog(ConsentError error) {
        CswLogger.e(TAG, error.getError());
        OkOnErrorListener okListener = new OkOnErrorListener();
        final AlertDialogFragment alertDialogFragment = new AlertDialogFragment.Builder(getContext())
                .setTitle(R.string.csw_problem_occurred_error_title)
                .setMessage(getString(R.string.csw_problem_occurred_error_message, error.getErrorCode()))
                .setPositiveButton(R.string.csw_ok, okListener)
                .create();
        okListener.setDialog(alertDialogFragment);
        alertDialogFragment.show(getFragmentManager(), TAG);
    }

    @Override
    public void showOfflineErrorDialog() {
        new DialogView().showDialog(getCswFragment().getActivity());
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

    private List<ConsentView> createConsentsList() {
        final List<ConsentView> consentViewList = new ArrayList<>();
        for (ConsentConfiguration configuration : configs) {
            for (final ConsentDefinition definition : configuration.getConsentDefinitionList()) {
                consentViewList.add(new ConsentView(definition, configuration.getHandlerInterface()));
            }
        }
        return consentViewList;
    }
}
