/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.apptagging.AppTaggingPages;
import com.philips.cdp.registration.apptagging.AppTagingConstants;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.customviews.XCheckBox;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegUtility;

public class LogoutFragment extends RegistrationBaseFragment implements OnClickListener,
        UpdateUserDetailsHandler, NetworStateListener, LogoutHandler,
        XCheckBox.OnCheckedChangeListener {

    private TextView mTvWelcome;

    private TextView mTvSignInEmail;

    private XCheckBox mCbTerms;

    private LinearLayout mLlContinueBtnContainer;

    private User mUser;

    private Context mContext;

    private TextView mTvEmailDetails;

    private Button mBtnLogOut;

    private XRegError mRegError;

    private ProgressBar mPbWelcomeCheck;

    private ScrollView mSvLayout;

    private TextView mAccessAccountSettingsLink;

    private FrameLayout mFlReceivePhilipsNewsContainer;

    public static final int BAD_RESPONSE_ERROR_CODE = 7008;

    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " WelcomeFragment : onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserWelcomeFragment : onCreateView");
        RegistrationHelper.getInstance().registerNetworkStateListener(this);

        View view = inflater.inflate(R.layout.reg_fragment_logout, null);
        mContext = getRegistrationFragment().getParentActivity().getApplicationContext();
        mUser = new User(mContext);
        mSvLayout = (ScrollView) view.findViewById(R.id.sv_root_layout);
        initUi(view);
        handleUiStates();
        handleOrientation(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " LogoutFragment : onStart");
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " LogoutFragment : onActivityCreated");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " LogoutFragment : onPause");
    }
    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " LogoutFragment : onStop");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " LogoutFragment : onResume");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideLogoutSpinner();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " Logout Fragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " Logout Fragment : onDestroy");
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " Logout Fragment : onDetach");
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserWelcomeFragment : onConfigurationChanged");
        setCustomParams(config);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, mTvWelcome, width);
        applyParams(config, mTvEmailDetails, width);
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

    private void initUi(View view) {
        consumeTouch(view);
        mTvWelcome = (TextView) view.findViewById(R.id.tv_reg_welcome);
        mLlContinueBtnContainer = (LinearLayout) view.findViewById(R.id.rl_reg_continue_id);
        mCbTerms = (XCheckBox) view.findViewById(R.id.cb_reg_receive_philips_news);
        mCbTerms.setPadding(RegUtility.getCheckBoxPadding(mContext), mCbTerms.getPaddingTop(),
                mCbTerms.getPaddingRight(), mCbTerms.getPaddingBottom());
        mCbTerms.setVisibility(View.VISIBLE);
        mCbTerms.setChecked(mUser.getReceiveMarketingEmail());
        mCbTerms.setOnCheckedChangeListener(this);
        mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
        mPbWelcomeCheck = (ProgressBar) view.findViewById(R.id.pb_reg_welcome_spinner);

        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(getActivity(), R.style.reg_Custom_loaderTheme);
        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        mProgressDialog.setCancelable(false);

        mTvEmailDetails = (TextView) view.findViewById(R.id.tv_reg_email_details_container);
        mTvSignInEmail = (TextView) view.findViewById(R.id.tv_reg_sign_in_using);
        mBtnLogOut = (Button) view.findViewById(R.id.btn_reg_sign_out);
        mBtnLogOut.setOnClickListener(this);
        TextView receivePhilipsNewsView = (TextView) view.findViewById(R.id.tv_reg_philips_news);
        mAccessAccountSettingsLink = (TextView) view.findViewById(R.id.tv_reg_more_account_Setting);

        mFlReceivePhilipsNewsContainer = (FrameLayout) view.
                findViewById(R.id.fl_reg_receive_philips_news);
        RegUtility.linkifyPhilipsNews(receivePhilipsNewsView,
                getRegistrationFragment().getParentActivity(), mPhilipsNewsLinkClick);
        RegUtility.linkifyAccountSettingPhilips(mAccessAccountSettingsLink,
                getRegistrationFragment().getParentActivity(), mPhilipsSettingLinkClick);

        mTvWelcome.setText(getString(R.string.reg_Signin_Success_Hello_lbltxt) + " " + mUser.getGivenName());

        String email = getString(R.string.reg_InitialSignedIn_SigninEmailText);
        email = String.format(email, mUser.getEmail());
        mTvSignInEmail.setText(email);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_reg_sign_out) {
            RLog.d(RLog.ONCLICK, "WelcomeFragment : Sign Out");
            showLogoutSpinner();
            handleLogout();
        }
    }

    private void handleLogout() {
        trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                AppTagingConstants.SIGN_OUT);
        mUser.logout(this);
    }

    private void handleUpdate() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            mRegError.hideError();
            showProgressBar();
            updateUser();
        } else {
            mCbTerms.setOnCheckedChangeListener(null);
            mCbTerms.setChecked(!mCbTerms.isChecked());
            mCbTerms.setOnCheckedChangeListener(this);
            mRegError.setError(getString(R.string.reg_NoNetworkConnection));
            scrollViewAutomatically(mRegError, mSvLayout);
            trackActionRegisterError(AppTagingConstants.NETWORK_ERROR_CODE);
        }
    }

    private void showProgressBar() {
        mPbWelcomeCheck.setVisibility(View.VISIBLE);
        mCbTerms.setEnabled(false);
        mBtnLogOut.setEnabled(false);

    }

    private void updateUser() {
        mUser.updateReceiveMarketingEmail(this, mCbTerms.isChecked());
    }

    @Override
    public void onUpdateSuccess() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                hideProgressBar();
                if (mCbTerms.isChecked()) {
                    trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_IN);
                } else {
                    trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_OUT);
                }
            }
        });
    }

    private void hideProgressBar() {
        mPbWelcomeCheck.setVisibility(View.INVISIBLE);
        mCbTerms.setEnabled(true);
        mBtnLogOut.setEnabled(true);
    }

    @Override
    public void onUpdateFailedWithError(final int error) {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleUpdateReceiveMarket(error);
            }
        });

    }

    private void handleUpdateReceiveMarket(int error) {
        hideProgressBar();
        if (error == Integer.parseInt(RegConstants.INVALID_REFRESH_TOKEN_CODE)) {
            if (getRegistrationFragment() != null) {
                getRegistrationFragment().replaceWithHomeFragment();
            }
            return;
        }
        if (error == -1 || error == BAD_RESPONSE_ERROR_CODE) {
            mRegError.setError(mContext.getResources().getString(R.string.reg_JanRain_Server_Connection_Failed));
            return;
        }
        mCbTerms.setOnCheckedChangeListener(null);
        mCbTerms.setChecked(!mCbTerms.isChecked());
        mCbTerms.setOnCheckedChangeListener(LogoutFragment.this);
    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        handleUiStates();
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_Account_Setting_Titletxt;
    }

    @Override
    public void onLogoutSuccess() {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                trackPage(AppTaggingPages.HOME);
                hideLogoutSpinner();
                getRegistrationFragment().replaceWithHomeFragment();
            }
        });

    }

    @Override
    public void onLogoutFailure(int responseCode, final String message) {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (mBtnLogOut.getVisibility() == View.VISIBLE) {
                    mBtnLogOut.setEnabled(true);
                    mBtnLogOut.setClickable(true);
                }
                hideLogoutSpinner();
                mRegError.setError(message);
            }
        });
    }

    private void showLogoutSpinner() {
        if (!(getActivity().isFinishing()) && (mProgressDialog != null)) mProgressDialog.show();
        mBtnLogOut.setEnabled(false);
        mCbTerms.setEnabled(false);
    }

    private void hideLogoutSpinner() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
        mBtnLogOut.setEnabled(true);
        mCbTerms.setEnabled(true);
    }

    private void handleUiStates() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            if (UserRegistrationInitializer.getInstance().isJanrainIntialized()) {
                mRegError.hideError();
            } else {
                mRegError.hideError();
            }
        } else {
            mRegError.setError(mContext.getResources().getString(R.string.reg_NoNetworkConnection));
            trackActionLoginError(AppTagingConstants.NETWORK_ERROR_CODE);
        }
    }

    private ClickableSpan mPhilipsSettingLinkClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            RLog.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : onTermsAndConditionClick");
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(RegConstants.PHILIPS_LOGIN_URL));
            startActivity(browserIntent);
        }
    };

    private ClickableSpan mPhilipsNewsLinkClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            getRegistrationFragment().addPhilipsNewsFragment();
            trackPage(AppTaggingPages.PHILIPS_ANNOUNCEMENT);
        }
    };

    @Override
    public void onCheckedChanged(View view, boolean checked) {
        handleUpdate();
    }
}
