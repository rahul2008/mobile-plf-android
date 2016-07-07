/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.ui.controllers.ParentalConsentFragmentController;
import com.philips.cdp.registration.coppa.utils.RegCoppaUtility;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;

public class ParentalConsentFragment extends RegistrationCoppaBaseFragment implements OnClickListener, NetworStateListener {

    private Button mBtnConsentConfirm;
    private Button mBtnConsentChange;
    private LinearLayout mLlRootContainer;
    private TextView mTVRegConfirm;
    private Context mContext;
    private ParentalConsentFragmentController mParentalConsentFragmentController;
    private ClickableSpan privacyLinkClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            RegistrationHelper.getInstance().getUserRegistrationListener().notifyOnPrivacyPolicyClickEventOccurred(getActivity());
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onCreate");
        super.onCreate(savedInstanceState);
        mParentalConsentFragmentController = new ParentalConsentFragmentController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserParentalAccessFragment : onCreateView");

        View view = inflater.inflate(R.layout.reg_fragment_parental_consent, null);
        RegistrationHelper.getInstance().registerNetworkStateListener(this);

        mContext = getParentFragment().getActivity().getApplicationContext();
        initUi(view);
        handleOrientation(view);
        mBtnConsentConfirm.setOnClickListener(this);
        mBtnConsentChange.setOnClickListener(this);
        handleOrientation(view);
        return view;
    }

    private ProgressDialog mProgressDialog;

    public void showRefreshProgress() {

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity(), R.style.reg_Custom_loaderTheme);
            mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } else {
            mProgressDialog.show();
        }
    }

    public void hideRefreshProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onDetach");
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserParentalAccessFragment : onConfigurationChanged");
        setCustomParams(config);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, mLlRootContainer, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    private void initUi(View view) {
        consumeTouch(view);
        mBtnConsentConfirm = (Button) view.findViewById(R.id.btn_parental_consent_reconfirm_confirm);
        mBtnConsentChange = (Button) view.findViewById(R.id.btn_parental_consent_reconfirm_cancel);
        mLlRootContainer = (LinearLayout) view.findViewById(R.id.ll_reg_create_account_validation_fields);
        mTVRegConfirm = (TextView) view.findViewById(R.id.tv_reg_confirm);
        mTVRegConfirm.setText(getReConfirmText());
        if(getActivity()!=null){
            RegCoppaUtility.linkifyTermAndPolicy(mTVRegConfirm, getActivity(), privacyLinkClick);
        }

    }

    private String getReConfirmText() {
        return mContext.getString(R.string.reg_Coppa_US_Parental_Access_Reconfirm_Txt) + String.format(mContext.getString(R.string.reg_Coppa_Give_Approval_PrivacyNotes_txt), mContext.getString(R.string.reg_PrivacyNoticeText));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_parental_consent_reconfirm_confirm) {
            mParentalConsentFragmentController.addagreeConfirmation();
        }
        if (id == R.id.btn_parental_consent_reconfirm_cancel) {
            mParentalConsentFragmentController.disAgreeConfirmation();
        }
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_Coppa_Age_Confirmation_Screen_Title_txt;
    }

    @Override
    public void onNetWorkStateReceived(final boolean isOnline) {

    }
}
