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

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.ui.controllers.ParentalConsentFragmentController;
import com.philips.cdp.registration.coppa.utils.CoppaInterface;
import com.philips.cdp.registration.coppa.utils.RegCoppaUtility;
import com.philips.cdp.registration.events.NetworkStateListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegUtility;

public class ParentalConsentFragment extends RegistrationCoppaBaseFragment
        implements OnClickListener, NetworkStateListener {

    private NetworkUtility networkUtility;

    private Button mBtnConsentConfirm;
    private Button mBtnConsentChange;
    private LinearLayout mLlRootContainer;
    private TextView mTVRegConfirm;
    private Context mContext;
    private XRegError mRegError;
    private ParentalConsentFragmentController mParentalConsentFragmentController;
    private ClickableSpan privacyLinkClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            RegistrationConfiguration.getInstance().getUserRegistrationUIEventListener().
                    onPrivacyPolicyClick(getActivity());
        }
    };
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalConsentFragment : onCreate");
        super.onCreate(savedInstanceState);
        mParentalConsentFragmentController = new ParentalConsentFragmentController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserParentalAccessFragment : onCreateView");
        networkUtility = CoppaInterface.getComponent().getNetworkUtility();
        View view = inflater.inflate(R.layout.reg_fragment_parental_consent, null);
        RegistrationHelper.getInstance().registerNetworkStateListener(this);

        mContext = getParentFragment().getActivity().getApplicationContext();
        initUi(view);
        handleOrientation(view);
        return view;
    }

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
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalConsentFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalConsentFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalConsentFragment : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalConsentFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalConsentFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalConsentFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalConsentFragment : onDestroy");
        getRegistrationFragment();
        RegistrationCoppaFragment.hideRefreshProgress();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalConsentFragment : onDetach");
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
        applyParams(config, mRegError, width);
        applyParams(config, mBtnConsentConfirm, width);
        applyParams(config, mBtnConsentChange, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    private void initUi(View view) {
        consumeTouch(view);
        mBtnConsentConfirm = (Button)
                view.findViewById(R.id.btn_parental_consent_reconfirm_confirm);
        mBtnConsentConfirm.setOnClickListener(this);
        mBtnConsentChange = (Button) view.findViewById(R.id.btn_parental_consent_reconfirm_cancel);
        mBtnConsentChange.setOnClickListener(this);
        mLlRootContainer = (LinearLayout)
                view.findViewById(R.id.ll_reg_create_account_validation_fields);
        mTVRegConfirm = (TextView) view.findViewById(R.id.tv_reg_confirm);
        mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
        mTVRegConfirm.setText(getReConfirmText());
        if(getActivity()!=null){
            RegCoppaUtility.linkifyTermAndPolicy(mTVRegConfirm, getActivity(), privacyLinkClick);
        }
        handleUiState();

    }

    private String getReConfirmText() {
        return mContext.getString(R.string.reg_Coppa_US_Parental_Access_Reconfirm_Txt) +
                String.format(mContext.getString(R.string.reg_Coppa_Give_Approval_PrivacyNotes_txt), mContext.getString(R.string.reg_PrivacyNoticeText));
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
        RLog.i(RLog.NETWORK_STATE, "ParentalConsentFragment :onNetWorkStateReceived state :"
                + isOnline);

        handleUiState();
    }
    private void handleUiState() {
        handleOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (networkUtility.isNetworkAvailable()) {
                    if (mRegError != null)
                        mRegError.hideError();
                    if (mBtnConsentConfirm != null)
                        mBtnConsentConfirm.setEnabled(true);
                    if (mBtnConsentChange != null)
                        mBtnConsentChange.setEnabled(true);
                } else {
                    if (mRegError != null)
                        mRegError.
                                setError(mContext.getResources().getString(R.string.reg_NoNetworkConnection));
                    /*if (mRegError != null && mSvRootLayout != null)
                        scrollViewAutomatically(mRegError, mSvRootLayout);*/
                    if (mBtnConsentConfirm != null)
                        mBtnConsentConfirm.setEnabled(false);
                    if (mBtnConsentChange != null)
                        mBtnConsentChange.setEnabled(false);
                }
            }
        });
    }
}
