
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
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.apptagging.AppTagingConstants;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.customviews.XCheckBox;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.traditional.RegistrationBaseFragment;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegUtility;

public class MobileLogoutFragment extends RegistrationBaseFragment {

    private TextView mTvWelcome;

    private TextView mTvSignInEmail;

    private XCheckBox mCbTerms;

    private LinearLayout mLlContinueBtnContainer;

    private User mUser;

    private Context mContext;

    private Button mBtnLogOut;

    private XRegError mRegError;

    private ProgressBar mPbWelcomeCheck;

    private ScrollView mSvRootLayout;

    private TextView mAccessAccountSettingsLink;

    private FrameLayout mFlReceivePhilipsNewsContainer;

    private ProgressDialog mProgressDialog;

    private LogoutFragmentController mlogoutController;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " MobileLogoutFragment : onCreate");
        super.onCreate(savedInstanceState);
        mlogoutController = new LogoutFragmentController(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileLogoutFragment : onCreateView");
        RegistrationHelper.getInstance().registerNetworkStateListener(mlogoutController);

        View view = inflater.inflate(R.layout.reg_mobile_fragment_logout, null);
        mContext = getRegistrationFragment().getParentActivity().getApplicationContext();
        mUser = new User(mContext);
        mSvRootLayout = (ScrollView) view.findViewById(R.id.sv_root_layout);
        init(view);
        handleOrientation(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " MobileLogoutFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " MobileLogoutFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " MobileLogoutFragment : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " MobileLogoutFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " MobileLogoutFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " MobileLogoutFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " MobileLogoutFragment : onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " MobileLogoutFragment : onDetach");
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileLogoutFragment : onConfigurationChanged");
        setCustomParams(config);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, mTvWelcome, width);
        applyParams(config, mLlContinueBtnContainer, width);
        applyParams(config, mFlReceivePhilipsNewsContainer, width);
        applyParams(config, mRegError, width);
        applyParams(config, mTvSignInEmail, width);
        applyParams(config, mAccessAccountSettingsLink, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    private void init(View view) {
        consumeTouch(view);
        mTvWelcome = (TextView) view.findViewById(R.id.tv_reg_welcome);
        mLlContinueBtnContainer = (LinearLayout) view.findViewById(R.id.rl_reg_continue_id);
        mCbTerms = (XCheckBox) view.findViewById(R.id.cb_reg_receive_philips_news);
        mCbTerms.setPadding(RegUtility.getCheckBoxPadding(mContext), mCbTerms.getPaddingTop(), mCbTerms.getPaddingRight(), mCbTerms.getPaddingBottom());
        mCbTerms.setVisibility(view.VISIBLE);
        mCbTerms.setChecked(mUser.getReceiveMarketingEmail());
        mCbTerms.setOnCheckedChangeListener(mlogoutController);
        mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
        mPbWelcomeCheck = (ProgressBar) view.findViewById(R.id.pb_reg_welcome_spinner);

        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(getActivity(), R.style.reg_Custom_loaderTheme);
        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        mProgressDialog.setCancelable(false);

        mTvSignInEmail = (TextView) view.findViewById(R.id.tv_reg_sign_in_using);
        mBtnLogOut = (Button) view.findViewById(R.id.btn_reg_sign_out);
        mBtnLogOut.setOnClickListener(mlogoutController);
         TextView receivePhilipsNewsView = (TextView) view.findViewById(R.id.tv_reg_philips_news);
        mAccessAccountSettingsLink = (TextView) view.findViewById(R.id.tv_reg_more_account_Setting);

        mFlReceivePhilipsNewsContainer = (FrameLayout) view.findViewById(R.id.fl_reg_receive_philips_news);
        RegUtility.linkifyMobilePhilipsNews(receivePhilipsNewsView, getRegistrationFragment().getParentActivity(), mPhilipsNewsLinkClick);
        RegUtility.linkifyAccountSettingPhilips(mAccessAccountSettingsLink, getRegistrationFragment().getParentActivity(), mPhilipsSettingLinkClick);

        mTvWelcome.setText(getString(R.string.Signin_Success_Hello_lbltxt) + " " +/* mUser.getGivenName()*/"Kiran");

        String email = getString(R.string.InitialSignedIn_SigninEmailText);
        email = String.format(email, /*mUser.getEmail()*/"Kiran");
        mTvSignInEmail.setText(email);
    }


    @Override
    public int getTitleResourceId() {
        return R.string.Account_Setting_Titletxt;
    }

    private ClickableSpan mPhilipsSettingLinkClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            RLog.d(RLog.EVENT_LISTENERS, "MobileLogoutFragment : onTermsAndConditionClick");
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(RegConstants.PHILIPS_LOGIN_URL));
            startActivity(browserIntent);
        }
    };

    private ClickableSpan mPhilipsNewsLinkClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            RLog.d(RLog.EVENT_LISTENERS, "MobileLogoutFragment : onTermsAndConditionClick");
        }
    };

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
            mBtnLogOut.setEnabled(true);
        } else {
            mRegError.setError(mContext.getResources().getString(R.string.NoNetworkConnection));
            trackActionLoginError(AppTagingConstants.NETWORK_ERROR_CODE);
            mBtnLogOut.setEnabled(false);
        }
    }
}
