
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional.mobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.apptagging.AppTagingConstants;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.traditional.RegistrationBaseFragment;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;

public class MobileWelcomeFragment extends RegistrationBaseFragment {

    private TextView mTvWelcome;

    private TextView mTvSignInEmail;

    private LinearLayout mLlContinueBtnContainer;

    private Context mContext;

    private TextView mTvEmailDetails;

    private Button mBtnSignOut;

    private Button mBtnContinue;

    private XRegError mRegError;

    private ProgressDialog mProgressDialog;

    private String mUserDetails;

    private WelcomeFragmentController mWelcomeController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " MobileWelcomeFragment : onCreate");
        super.onCreate(savedInstanceState);
        mWelcomeController = new WelcomeFragmentController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserWelcomeFragment : onCreateView");
        View view = inflater.inflate(R.layout.reg_mobile_fragment_welcome, null);
        RegistrationHelper.getInstance().registerNetworkStateListener(mWelcomeController);

        mContext = getRegistrationFragment().getParentActivity().getApplicationContext();
        init(view);
        handleOrientation(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " MobileWelcomeFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " MobileWelcomeFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " MobileWelcomeFragment : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " MobileWelcomeFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " MobileWelcomeFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " MobileWelcomeFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " MobileWelcomeFragment : onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " MobileWelcomeFragment : onDetach");
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileWelcomeFragment : onConfigurationChanged");
        setCustomParams(config);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, mTvWelcome, width);
        applyParams(config, mTvEmailDetails, width);
        applyParams(config, mLlContinueBtnContainer, width);
        applyParams(config, mRegError, width);
        applyParams(config, mTvSignInEmail, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    private void init(View view) {
        consumeTouch(view);
        mTvWelcome = (TextView) view.findViewById(R.id.tv_reg_welcome);
        mLlContinueBtnContainer = (LinearLayout) view.findViewById(R.id.rl_reg_continue_id);
        mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
        mTvEmailDetails = (TextView) view.findViewById(R.id.tv_reg_email_details_container);
        mTvSignInEmail = (TextView) view.findViewById(R.id.tv_reg_sign_in_using);
        mBtnSignOut = (Button) view.findViewById(R.id.btn_reg_sign_out);
        mBtnSignOut.setOnClickListener(mWelcomeController);
        mBtnContinue = (Button) view.findViewById(R.id.btn_reg_continue);
        mBtnContinue.setOnClickListener(mWelcomeController);

        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(getActivity(), R.style.reg_Custom_loaderTheme);
        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        mProgressDialog.setCancelable(false);

        mTvWelcome.setText(getString(R.string.SignInSuccess_Welcome_lbltxt) + " " + /*mUser.getGivenName()*/"Kiran");
        mUserDetails = getString(R.string.InitialSignedIn_china_SigninNumberText);
        mUserDetails = String.format(mUserDetails, "1339 9999 9999");
        mTvSignInEmail.setText(mUserDetails);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.SigIn_TitleTxt;
    }

    public void getLogout() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                getRegistrationFragment().replaceWithHomeFragment();
            }
        });
    }
    public void networkUiState() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            if (UserRegistrationInitializer.getInstance().isJanrainIntialized()) {
                mRegError.hideError();
            } else {
                mRegError.hideError();
            }
            mBtnSignOut.setEnabled(true);
            mBtnContinue.setEnabled(true);
        } else {
            mRegError.setError(mContext.getResources().getString(R.string.NoNetworkConnection));
            trackActionLoginError(AppTagingConstants.NETWORK_ERROR_CODE);
            mBtnSignOut.setEnabled(false);
            mBtnContinue.setEnabled(false);
        }
    }
}
