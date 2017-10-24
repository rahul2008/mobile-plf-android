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

import com.philips.cdp.registration.User;
import com.philips.platform.csw.CswBaseFragment;
import com.philips.platform.mya.catk.ConsentAccessToolKit;
import com.philips.platform.mya.catk.listener.ConsentResponseListener;
import com.philips.platform.mya.catk.model.GetConsentsModel;
import com.philips.platform.mya.catk.response.ConsentStatus;
import com.philips.platform.mya.consentwidgets.R;
import com.philips.platform.uid.view.widget.Switch;

import java.util.List;

import static com.janrain.android.engage.JREngage.getApplicationContext;

public class PermissionView extends CswBaseFragment implements
        PermissionInterface {

    public static final String CONSENT_TYPE_MOMENT_SYNC = "momentsync";

    public static final String applicationName = "OneBackend";
    public static final String propositionName = "OneBackendProp";
    public static final String CONSENT_TYPE_MOMENT = "moment";
    public static final int version = 0;

    private PermissionPresenter permissionPresenter;
    private Switch mConsentSwitch;
    private ProgressDialog mProgressDialog;

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
        initUI(view);
        getConsentStatus();
        return view;
    }

    private void getConsentStatus() {
        showProgressDialog();
        ConsentAccessToolKit cat = new ConsentAccessToolKit(getApplicationContext(), applicationName, propositionName);
        User user = new User(getApplicationContext());
        cat.getStatusForConsentType(CONSENT_TYPE_MOMENT, version, new ConsentResponseListener() {
            @Override
            public void onResponseSuccessConsent(List<GetConsentsModel> responseData) {
                if (responseData != null && !responseData.isEmpty()) {
                    GetConsentsModel consentModel = responseData.get(0);

                    hideProgressDialog();
                    if (consentModel.getStatus().equals(ConsentStatus.active)) {
                        mConsentSwitch.setChecked(true);
                    } else {
                        mConsentSwitch.setChecked(false);
                    }
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
            }

            @Override
            public int onResponseFailureConsent(int consentError) {
                hideProgressDialog();
                Log.d(" Consent : ", "fail  :" + consentError);
                return consentError;
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        permissionPresenter = new PermissionPresenter(this, getContext());
    }

    private void initUI(View view) {
        mConsentSwitch = (Switch) view.findViewById(R.id.toggleicon);
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
}
