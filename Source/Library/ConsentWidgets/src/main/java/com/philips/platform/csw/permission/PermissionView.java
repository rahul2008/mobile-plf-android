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

import com.philips.platform.catk.CatkConstants;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.csw.ConsentDefinition;
import com.philips.platform.csw.CswBaseFragment;
import com.philips.platform.mya.consentwidgets.R;
import com.philips.platform.mya.consentwidgets.R2;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PermissionView extends CswBaseFragment implements
        PermissionInterface {

    private ProgressDialog mProgressDialog;

    private String applicationName;

    private String propositionName;

    private Unbinder unbinder;

    private PermissionsAdapter permissionAdapter;

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
            applicationName = getArguments().getString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME);
            propositionName = getArguments().getString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME);
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
            applicationName = state.getString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME);
            propositionName = state.getString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        if (state != null) {
            state.putString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME, applicationName);
            state.putString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME, propositionName);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<ConsentView> consentDefinitions = createConsentDefinitions();

        CreateConsentInteractor createConsentInteractor = new CreateConsentInteractor();
        GetConsentInteractor getConsentInteractor = new GetConsentInteractor(ConsentAccessToolKit.getInstance(), consentDefinitions);


        PermissionPresenter permissionPresenter = new PermissionPresenter(this, getConsentInteractor);
        permissionPresenter.getConsentStatus();

        permissionAdapter = new PermissionsAdapter(consentDefinitions, createConsentInteractor);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(permissionAdapter);
    }

    @NonNull
    private List<ConsentView> createConsentDefinitions() {
        // TODO: Should come from ConsentAccessToolkit
        final List<ConsentView> temporaryList = new ArrayList<>();
        temporaryList.add(new ConsentView(new ConsentDefinition("text1", "help", "moment", 0, Locale.getDefault())));
        temporaryList.add(new ConsentView(new ConsentDefinition("asdflasdkjf asdlfj asdf asd3", "help", "moment", 0, Locale.getDefault())));
        return temporaryList;
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
    public void onConsentRetrieved(@NonNull ConsentView consent) {
        if(permissionAdapter != null) {
            permissionAdapter.onConsentRetrieved(consent);
        }
    }

    public void setArguments(String applicationName, String propositionName) {
        Bundle b = new Bundle();
        b.putString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME, applicationName);
        b.putString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME, propositionName);
        this.setArguments(b);
    }
}
