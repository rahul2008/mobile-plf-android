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

import com.philips.plataform.mya.model.error.ConsentError;
import com.philips.plataform.mya.model.listener.ConsentResponseListener;
import com.philips.plataform.mya.model.ConsentAccessToolKit;
import com.philips.plataform.mya.model.response.ConsentModel;
import com.philips.plataform.mya.model.response.ConsentStatus;
import com.philips.platform.csw.CswBaseFragment;
import com.philips.platform.mya.consentwidgets.R;
import com.philips.platform.uid.view.widget.Switch;

import java.util.List;

import static com.janrain.android.engage.JREngage.getApplicationContext;

public class PermissionView extends CswBaseFragment implements
        PermissionInterface {

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
        ConsentAccessToolKit consentAccessToolKit = new ConsentAccessToolKit();
        consentAccessToolKit.getConsentDetails(getApplicationContext(),new ConsentResponseListener() {

            @Override
            public void onResponseSuccessConsent(List<ConsentModel> responseData) {
                hideProgressDialog();
                if (responseData.get(0).getStatus().equals(ConsentStatus.active)) {
                    mConsentSwitch.setChecked(true);
                } else {
                    mConsentSwitch.setChecked(false);
                }
                Log.d(" Consent : ", "Success msg  DateTime:" + responseData.get(0).getDateTime());
                Log.d(" Consent : ", "Success msg  status:" + responseData.get(0).getStatus());
                Log.d(" Consent : ", "Success msg  subject:" + responseData.get(0).getSubject());
                Log.d(" Consent : ", "Success msg  Lang:" + responseData.get(0).getLanguage());
            }

            @Override
            public void onResponseFailureConsent(ConsentError consentError) {
                hideProgressDialog();
                //Need to handle
                Log.d(" Consent : ", "failed");
            }
        });
    }

    @Override
    public void onDestroy() {
        hideProgressDialog();
        super.onDestroy();
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
                mProgressDialog = new ProgressDialog(getActivity(),R.style.reg_Custom_loaderTheme);
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
