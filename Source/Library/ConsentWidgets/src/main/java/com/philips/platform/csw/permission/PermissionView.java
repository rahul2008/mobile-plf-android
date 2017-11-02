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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.philips.platform.csw.CswBaseFragment;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.model.GetConsentsModel;
import com.philips.platform.catk.response.ConsentStatus;
import com.philips.platform.catk.CatkConstants;
import com.philips.platform.mya.consentwidgets.R;
import com.philips.platform.uid.view.widget.Switch;

import java.util.List;

public class PermissionView extends CswBaseFragment implements
        PermissionInterface,CompoundButton.OnCheckedChangeListener {

    public static final String CONSENT_TYPE_MOMENT_SYNC = "momentsync";
    public static final String CONSENT_TYPE_MOMENT = "moment";
    public static final int version = 0;

    private PermissionPresenter permissionPresenter;
    private Switch mConsentSwitch;
    private ProgressDialog mProgressDialog;

    private String applicationName;
    private String propositionName;

    @Override
    protected void setViewParams(Configuration config, int width) {

    }

    @Override
    protected void handleOrientation(View view) {

    }

    @Override
    public int getTitleResourceId() {
        return R.string.csw_permissions;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.csw_permission_view, container, false);
        if (getArguments() != null) {
            applicationName = getArguments().getString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME);
            propositionName = getArguments().getString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME);
        }
        mConsentSwitch = (Switch) view.findViewById(R.id.toggleicon);
        getConsentStatus();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mConsentSwitch.setOnCheckedChangeListener(null);
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

    private void getConsentStatus() {
        showProgressDialog();
        ConsentAccessToolKit cat = new ConsentAccessToolKit(applicationName, propositionName);
        cat.getStatusForConsentType(CONSENT_TYPE_MOMENT, version, new ConsentResponseListener() {

            @Override
            public void onResponseSuccessConsent(List<GetConsentsModel> responseData) {
                if (responseData != null && !responseData.isEmpty()) {
                    GetConsentsModel consentModel = responseData.get(0);
                    hideProgressDialog();
                    mConsentSwitch.setChecked(consentModel.getStatus().equals(ConsentStatus.active));
                    Log.d(" Consent : ", "getDateTime :" + consentModel.getDateTime());
                    Log.d(" Consent : ", "getLanguage :" + consentModel.getLanguage());
                    Log.d(" Consent : ", "status :" + consentModel.getStatus());
                    Log.d(" Consent : ", "policyRule :" + consentModel.getPolicyRule());
                    Log.d(" Consent : ", "Resource type :" + consentModel.getResourceType());
                    Log.d(" Consent : ", "subject  :" + consentModel.getSubject());
                } else {
                    hideProgressDialog();
                    mConsentSwitch.setChecked(false);
                    Log.d(" Consent : ", "no consent for type found on server");
                }
                mConsentSwitch.setOnCheckedChangeListener(PermissionView.this);
            }

            @Override
            public int onResponseFailureConsent(int consentError) {
                hideProgressDialog();
                Log.d(" Consent : ", "fail  :" + consentError);
                return consentError;
            }
        });
    }

    private void createConsentStatus(boolean isChecked) {
        showProgressDialog();
        ConsentStatus status = isChecked?ConsentStatus.active:ConsentStatus.rejected;
        ConsentAccessToolKit consentAccessToolKit = new ConsentAccessToolKit(applicationName, propositionName);
        consentAccessToolKit.createConsent(status,new CreateConsentListener() {

            @Override
            public void onSuccess(int code) {
                Log.d(" Create Consent: ", "Success : "+code);
                hideProgressDialog();
            }

            @Override
            public int onFailure(int errCode) {
                Log.d(" Create Consent: ", "Failed : "+errCode);
                hideProgressDialog();
                return errCode;
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        permissionPresenter = new PermissionPresenter(this, getContext());
    }

    private void showProgressDialog() {
        if (!(getActivity().isFinishing())) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(getActivity(), R.style.reg_Custom_loaderTheme);
                mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
                mProgressDialog.setCancelable(false);
            }
            mProgressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        showProgressDialog();
        createConsentStatus(isChecked);
    }

    public void setArguments(String applicationName, String propositionName) {
        Bundle b = new Bundle();
        b.putString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME, applicationName);
        b.putString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME, propositionName);
        this.setArguments(b);
    }
}
