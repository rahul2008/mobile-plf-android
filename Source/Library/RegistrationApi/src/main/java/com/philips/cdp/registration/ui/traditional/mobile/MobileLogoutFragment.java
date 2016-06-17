
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
import com.philips.cdp.registration.apptagging.AppTaggingPages;
import com.philips.cdp.registration.apptagging.AppTagingConstants;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.handlers.UpdateReceiveMarketingEmailHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.customviews.XCheckBox;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.traditional.RegistrationBaseFragment;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegUtility;



public class MobileLogoutFragment extends RegistrationBaseFragment implements View.OnClickListener,
        UpdateReceiveMarketingEmailHandler, NetworStateListener, LogoutHandler, XCheckBox.OnCheckedChangeListener {

    private TextView mTvMobileWelcome;

    private TextView mTvSignInMobile;

    private XCheckBox mCbMobileTerms;

    private LinearLayout mLlMobileContinueBtnContainer;

    private Context mContext;

    private Button mBtnMobileLogOut;

    private XRegError mRegMobileError;

    private ProgressBar mPbMobileWelcomeCheck;

    private ScrollView mSvMobileRootLayout;

    private TextView mMobileAccessAccountSettingsLink;

    private FrameLayout mFlMobileReceivePhilipsNewsContainer;

    private ProgressDialog mMobileProgressDialog;

    public static final int BAD_RESPONSE_ERROR_CODE = 7008;

    private User mUser;

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        View view = inflater.inflate(R.layout.reg_mobile_fragment_logout, null);
        mContext = getRegistrationFragment().getParentActivity().getApplicationContext();
        mUser = new User(mContext);
        mSvMobileRootLayout = (ScrollView) view.findViewById(R.id.sv_root_layout);
        init(view);
        handleUiState();
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
        hideLogoutSpinner();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " MobileLogoutFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " MobileLogoutFragment : onDestroy");
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
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
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserWelcomeFragment : onConfigurationChanged");
        setCustomParams(config);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, mTvMobileWelcome, width);
        applyParams(config, mLlMobileContinueBtnContainer, width);
        applyParams(config, mFlMobileReceivePhilipsNewsContainer, width);
        applyParams(config, mRegMobileError, width);
        applyParams(config, mTvSignInMobile, width);
        applyParams(config, mMobileAccessAccountSettingsLink, width);
    }


    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    private void init(final View view) {
        consumeTouch(view);
        mTvMobileWelcome = (TextView) view.findViewById(R.id.tv_reg_mobile_welcome);
        mLlMobileContinueBtnContainer = (LinearLayout) view.findViewById(R.id.rl_reg_continue_id);
        mCbMobileTerms = (XCheckBox) view.findViewById(R.id.cb_reg_mobile_receive_philips_news);
        mCbMobileTerms.setPadding(RegUtility.getCheckBoxPadding(mContext), mCbMobileTerms.getPaddingTop(), mCbMobileTerms.getPaddingRight(), mCbMobileTerms.getPaddingBottom());
        mCbMobileTerms.setVisibility(view.VISIBLE);
        mCbMobileTerms.setChecked(mUser.getReceiveMarketingEmail());
        mRegMobileError = (XRegError) view.findViewById(R.id.reg_error_msg);
        mPbMobileWelcomeCheck = (ProgressBar) view.findViewById(R.id.pb_reg_mobile_welcome_spinner);

        if (mMobileProgressDialog == null)
            mMobileProgressDialog = new ProgressDialog(getActivity(), R.style.reg_Custom_loaderTheme);
        mMobileProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        mMobileProgressDialog.setCancelable(false);

        mTvSignInMobile = (TextView) view.findViewById(R.id.tv_reg_mobile_sign_in_using);
        mBtnMobileLogOut = (Button) view.findViewById(R.id.btn_reg_mobile_sign_out);
        mBtnMobileLogOut.setOnClickListener(this);
        TextView receivePhilipsNewsView = (TextView) view.findViewById(R.id.tv_reg_mobile_philips_news);
        mMobileAccessAccountSettingsLink = (TextView) view.findViewById(R.id.tv_reg_mobile_more_account_Setting);

        mFlMobileReceivePhilipsNewsContainer = (FrameLayout) view.findViewById(R.id.fl_reg_receive_philips_news);
        RegUtility.linkifyMobilePhilipsNews(receivePhilipsNewsView, getRegistrationFragment().getParentActivity(), mPhilipsNewsLinkClick);
        RegUtility.linkifyAccountSettingPhilips(mMobileAccessAccountSettingsLink, getRegistrationFragment().getParentActivity(), mPhilipsSettingLinkClick);

        mTvMobileWelcome.setText(getString(R.string.Signin_Success_Hello_lbltxt) + " " + mUser.getGivenName());

        String email = getString(R.string.InitialSignedIn_china_SigninNumberText);
        email = String.format(email, "1339 9999 9999");
        mTvSignInMobile.setText(email);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_reg_mobile_sign_out) {
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
            mRegMobileError.hideError();
            showProgressBar();
            updateUser();
        } else {
            mCbMobileTerms.setOnCheckedChangeListener(null);
            mCbMobileTerms.setChecked(!mCbMobileTerms.isChecked());
            mCbMobileTerms.setOnCheckedChangeListener(this);
            mRegMobileError.setError(getString(R.string.NoNetworkConnection));
            scrollViewAutomatically(mRegMobileError, mSvMobileRootLayout);
            trackActionRegisterError(AppTagingConstants.NETWORK_ERROR_CODE);
        }
    }

    private void showProgressBar() {
        mPbMobileWelcomeCheck.setVisibility(View.VISIBLE);
        mCbMobileTerms.setEnabled(false);
        mBtnMobileLogOut.setEnabled(false);

    }

    private void updateUser() {
        mUser.updateReceiveMarketingEmail(this, mCbMobileTerms.isChecked());
    }

    @Override
    public void onUpdateReceiveMarketingEmailSuccess() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                hideProgressBar();
                if (mCbMobileTerms.isChecked()) {
                    trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_IN);
                } else {
                    trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_OUT);
                }
            }
        });
    }

    private void hideProgressBar() {
        mPbMobileWelcomeCheck.setVisibility(View.INVISIBLE);
        mCbMobileTerms.setEnabled(true);
        mBtnMobileLogOut.setEnabled(true);
    }

    @Override
    public void onUpdateReceiveMarketingEmailFailedWithError(final int error) {

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
            mRegMobileError.setError(mContext.getResources().getString(R.string.JanRain_Server_Connection_Failed));
            return;
        }
        mCbMobileTerms.setOnCheckedChangeListener(null);
        mCbMobileTerms.setChecked(!mCbMobileTerms.isChecked());
        mCbMobileTerms.setOnCheckedChangeListener(MobileLogoutFragment.this);
    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        handleUiState();
    }

    @Override
    public int getTitleResourceId() {
        return R.string.Account_Setting_Titletxt;
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
                if (mBtnMobileLogOut.getVisibility() == View.VISIBLE) {
                    mBtnMobileLogOut.setEnabled(true);
                    mBtnMobileLogOut.setClickable(true);
                }
                hideLogoutSpinner();
                mRegMobileError.setError(message);
            }
        });
    }

    private void showLogoutSpinner() {
        if (!(getActivity().isFinishing()) && (mMobileProgressDialog != null)) mMobileProgressDialog.show();
        mBtnMobileLogOut.setEnabled(false);
        mCbMobileTerms.setEnabled(false);
    }

    private void hideLogoutSpinner() {
        if (mMobileProgressDialog != null && mMobileProgressDialog.isShowing()) {
            mMobileProgressDialog.cancel();
        }
        mBtnMobileLogOut.setEnabled(true);
        mCbMobileTerms.setEnabled(true);
    }

    private void handleUiState() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            if (UserRegistrationInitializer.getInstance().isJanrainIntialized()) {
                mRegMobileError.hideError();
            } else {
                mRegMobileError.hideError();
            }
        } else {
            mRegMobileError.setError(mContext.getResources().getString(R.string.NoNetworkConnection));
            trackActionLoginError(AppTagingConstants.NETWORK_ERROR_CODE);
        }
    }

    private ClickableSpan mPhilipsSettingLinkClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            RLog.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : onTermsAndConditionClick");
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(RegConstants.PHILIPS_LOGIN_URL));
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
