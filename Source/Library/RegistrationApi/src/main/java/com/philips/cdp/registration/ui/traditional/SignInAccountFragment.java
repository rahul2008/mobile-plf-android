
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.apptagging.AppTaggingPages;
import com.philips.cdp.registration.apptagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.events.EventListener;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.handlers.ForgotPasswordHandler;
import com.philips.cdp.registration.handlers.ResendVerificationEmailHandler;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.customviews.XEmail;
import com.philips.cdp.registration.ui.customviews.XHavingProblems;
import com.philips.cdp.registration.ui.customviews.XPassword;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.customviews.onUpdateListener;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegAlertDialog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegPreferenceUtility;


public class SignInAccountFragment extends RegistrationBaseFragment implements OnClickListener,
        TraditionalLoginHandler, ForgotPasswordHandler, onUpdateListener, EventListener, ResendVerificationEmailHandler,
        NetworStateListener {

    private LinearLayout mLlCreateAccountFields;

    private RelativeLayout mRlSignInBtnContainer;

    private Button mBtnSignInAccount;

    private Button mBtnForgot;

    private Button mBtnResend;

    private XEmail mEtEmail;

    private XPassword mEtPassword;

    private User mUser;

    private ProgressBar mPbSignInSpinner;

    private ProgressBar mPbForgotPasswdSpinner;

    private ProgressBar mPbResendSpinner;

    private XRegError mRegError;

    private Context mContext;

    private LinearLayout mLlattentionBox;

    private View mViewAttentionBoxLine;

    private TextView mTvResendDetails;

    private XHavingProblems mViewHavingProblem;

    private final int SOCIAL_SIGIN_IN_ONLY_CODE = 540;

    private final int UN_EXPECTED_ERROR = 500;

    private final int BAD_RESPONSE_CODE = 7004;

    private ScrollView mSvRootLayout;

    @Override
    public void onAttach(Activity activity) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onCreateView");
        mContext = getRegistrationFragment().getParentActivity().getApplicationContext();
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        EventHelper.getInstance()
                .registerEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);
        View view = inflater.inflate(R.layout.reg_fragment_sign_in_account, null);
        RLog.i(RLog.EVENT_LISTENERS,
                "SignInAccountFragment register: NetworStateListener,JANRAIN_INIT_SUCCESS");
        mSvRootLayout = (ScrollView) view.findViewById(R.id.sv_root_layout);
        initUI(view);
        handleOrientation(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onDestroy");
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
        EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_SUCCESS,
                this);
        RLog.i(RLog.EVENT_LISTENERS,
                "SignInAccountFragment unregister: NetworStateListener,JANRAIN_INIT_SUCCESS");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onDetach");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }


    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onConfigurationChanged");
        setCustomParams(config);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, mLlCreateAccountFields, width);
        applyParams(config, mRlSignInBtnContainer, width);
        applyParams(config, mRegError, width);
        applyParams(config, mTvResendDetails, width);
        applyParams(config, mViewHavingProblem, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_reg_sign_in) {
            RLog.d(RLog.ONCLICK, "SignInAccountFragment : SignIn");
            hideValidations();
            signIn();
        } else if (id == R.id.btn_reg_forgot_password) {
            RLog.d(RLog.ONCLICK, "SignInAccountFragment : Forgot Password");
            hideValidations();
            mEtEmail.clearFocus();
            mEtPassword.clearFocus();
            if (mEtEmail.getEmailId().length() == 0) {
                launchResetPasswordFragment();
            } else {
                resetPassword();
            }
        } else if (id == R.id.btn_reg_resend) {
            RLog.d(RLog.ONCLICK, "SignInAccountFragment : Resend");
            mEtEmail.clearFocus();
            mEtPassword.clearFocus();
            RLog.d(RLog.ONCLICK, "AccountActivationFragment : Resend");
            handleResend();
        }
    }

    private void hideValidations() {
        //mEtEmail.hideErrPopUp();
        //mEtEmail.hideEmailInvalidAlert();
        mRegError.hideError();
    }

    private void launchResetPasswordFragment() {
        getRegistrationFragment().addResetPasswordFragment();
        trackPage(AppTaggingPages.FORGOT_PASSWORD);
    }

    private void lauchAccountActivationFragment() {
        getRegistrationFragment().launchAccountActivationFragmentForLogin();
        trackPage(AppTaggingPages.ACCOUNT_ACTIVATION);
    }

    private void initUI(View view) {
        consumeTouch(view);
        mBtnSignInAccount = (Button) view.findViewById(R.id.btn_reg_sign_in);
        mViewHavingProblem = (XHavingProblems) view.findViewById(R.id.view_having_problem);
        mBtnSignInAccount.setOnClickListener(this);
        mBtnForgot = (Button) view.findViewById(R.id.btn_reg_forgot_password);
        mBtnForgot.setOnClickListener(this);
        mBtnResend = (Button) view.findViewById(R.id.btn_reg_resend);
        mBtnResend.setOnClickListener(this);
        mLlCreateAccountFields = (LinearLayout) view
                .findViewById(R.id.ll_reg_create_account_fields);
        mRlSignInBtnContainer = (RelativeLayout) view.findViewById(R.id.rl_reg_welcome_container);

        mEtEmail = (XEmail) view.findViewById(R.id.rl_reg_email_field);
        mEtEmail.setOnClickListener(this);
        mEtEmail.setOnUpdateListener(this);
        mEtEmail.setFocusable(true);
        ((RegistrationFragment) getParentFragment()).showKeyBoard();
        mEtEmail.requestFocus();
        mEtPassword = (XPassword) view.findViewById(R.id.rl_reg_password_field);
        mEtPassword.setOnClickListener(this);
        mEtPassword.setOnUpdateListener(this);
        mEtPassword.isValidatePassword(false);
        mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
        mLlattentionBox = (LinearLayout) view.findViewById(R.id.ll_reg_attention_box);
        mViewAttentionBoxLine = view.findViewById(R.id.view_reg_attention_box_line);
        mTvResendDetails = (TextView) view.findViewById(R.id.tv_reg_resend_details);
        handleUiState();

        mUser = new User(mContext);
        mPbSignInSpinner = (ProgressBar) view.findViewById(R.id.pb_reg_sign_in_spinner);
        mPbForgotPasswdSpinner = (ProgressBar) view.findViewById(R.id.pb_reg_forgot_spinner);
        mPbResendSpinner = (ProgressBar) view.findViewById(R.id.pb_reg_resend_spinner);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_SigIn_TitleTxt;
    }

    private void signIn() {
        ((RegistrationFragment) getParentFragment()).hideKeyBoard();
        mEtEmail.clearFocus();
        mEtPassword.clearFocus();
        mBtnForgot.setEnabled(false);
        mBtnResend.setEnabled(false);
        if (mUser != null) {
            showSignInSpinner();
        }
        mEmail = mEtEmail.getEmailId().toString();
        mUser.loginUsingTraditional(mEmail, mEtPassword.getPassword()
                .toString(), this);
    }

    private String mEmail;

    private void handleUiState() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            mRegError.hideError();
        } else {
            trackActionLoginError(AppTagingConstants.NETWORK_ERROR_CODE);
            mRegError.setError(getString(R.string.reg_NoNetworkConnection));
            scrollViewAutomatically(mRegError, mSvRootLayout);
        }
    }

    @Override
    public void onLoginSuccess() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleLoginSuccess();
            }
        });
    }

    private void launchWelcomeFragment() {
        getRegistrationFragment().addWelcomeFragmentOnVerification();
        trackPage(AppTaggingPages.WELCOME);
    }

    @Override
    public void onLoginFailedWithError(final UserRegistrationFailureInfo userRegistrationFailureInfo) {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleLogInFailed(userRegistrationFailureInfo);
            }
        });
    }

    private void handleLogInFailed(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.i(RLog.CALLBACK, "SignInAccountFragment : onLoginFailedWithError");
        mBtnForgot.setEnabled(true);
        mBtnResend.setEnabled(true);
        hideSignInSpinner();
        mBtnSignInAccount.setEnabled(false);

        if(userRegistrationFailureInfo.getErrorCode() == -1 || userRegistrationFailureInfo.getErrorCode() == BAD_RESPONSE_CODE
                || userRegistrationFailureInfo.getErrorCode() == UN_EXPECTED_ERROR){
            mRegError.setError(mContext.getResources().getString(R.string.reg_JanRain_Server_Connection_Failed));
        }else {
            if (userRegistrationFailureInfo.getErrorCode() >= RegConstants.HSDP_LOWER_ERROR_BOUND) {
                //HSDP related error description
                scrollViewAutomatically(mRegError, mSvRootLayout);
                mRegError.setError(mContext.getResources().getString(R.string.reg_Generic_Network_Error));
                trackActionLoginError(userRegistrationFailureInfo.getErrorCode());
                scrollViewAutomatically(mRegError, mSvRootLayout);
            } else {
                //Need to show password errors only
                scrollViewAutomatically(mRegError, mSvRootLayout);
                mRegError.setError(userRegistrationFailureInfo.getPasswordErrorMessage());
                trackActionLoginError(userRegistrationFailureInfo.getErrorCode());
            }
        }
    }


    @Override
    public void onSendForgotPasswordSuccess() {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleSendForgotSuccess();
            }
        });

    }

    private void handleSendForgotSuccess() {
        RLog.i(RLog.CALLBACK, "SignInAccountFragment : onSendForgotPasswordSuccess");
        trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.STATUS_NOTIFICATION,
                AppTagingConstants.RESET_PASSWORD_SUCCESS);
        hideForgotPasswordSpinner();
        RegAlertDialog.showResetPasswordDialog(mContext.getResources().getString(R.string.reg_ForgotPwdEmailResendMsg_Title),
                mContext.getResources().getString(R.string.reg_ForgotPwdEmailResendMsg), getRegistrationFragment().getParentActivity(), mContinueBtnClick);
        hideForgotPasswordSpinner();
        mBtnResend.setEnabled(true);
        mRegError.hideError();
    }

    @Override
    public void onSendForgotPasswordFailedWithError(final
                                                    UserRegistrationFailureInfo userRegistrationFailureInfo) {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {

                handleSendForgetPasswordSuccess(userRegistrationFailureInfo);
            }
        });


    }

    private void handleSendForgetPasswordSuccess(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.i(RLog.CALLBACK, "SignInAccountFragment : onSendForgotPasswordFailedWithError ERROR CODE :" + userRegistrationFailureInfo.getErrorCode());
        mBtnResend.setEnabled(true);
        hideForgotPasswordSpinner();

        if (userRegistrationFailureInfo.getErrorCode() == SOCIAL_SIGIN_IN_ONLY_CODE) {
            mLlattentionBox.setVisibility(View.VISIBLE);
            mEtEmail.showInvalidAlert();
            mTvResendDetails.setVisibility(View.VISIBLE);
            mViewHavingProblem.setVisibility(View.GONE);
            mTvResendDetails.setText(getString(R.string.reg_TraditionalSignIn_ForgotPwdSocialExplanatory_lbltxt));
            mEtEmail.setErrDescription(getString(R.string.reg_TraditionalSignIn_ForgotPwdSocialError_lbltxt));
            mEtEmail.showErrPopUp();
            trackActionStatus(AppTagingConstants.SEND_DATA,
                    AppTagingConstants.USER_ERROR, AppTagingConstants.ALREADY_SIGN_IN_SOCIAL);
            trackActionForgotPasswordFailure(userRegistrationFailureInfo.getErrorCode());
            mBtnForgot.setEnabled(false);
            return;
        } else {
            mLlattentionBox.setVisibility(View.GONE);
            if(userRegistrationFailureInfo.getErrorCode() == -1) {
                mRegError.setError(mContext.getResources().getString(R.string.reg_JanRain_Server_Connection_Failed));
            }
        }

        if (null != userRegistrationFailureInfo.getSocialOnlyError()) {
            mEtEmail.showErrPopUp();
            mEtEmail.setErrDescription(userRegistrationFailureInfo.getSocialOnlyError());
            mEtEmail.showInvalidAlert();
            trackActionForgotPasswordFailure(userRegistrationFailureInfo.getErrorCode());
            return;
        }

        if (null != userRegistrationFailureInfo.getEmailErrorMessage()) {
            mEtEmail.setErrDescription(userRegistrationFailureInfo.getEmailErrorMessage());
            mEtEmail.showInvalidAlert();
            mEtEmail.showErrPopUp();
        }
        trackActionForgotPasswordFailure(userRegistrationFailureInfo.getErrorCode());
    }

    private void showSignInSpinner() {
        mBtnSignInAccount.setEnabled(false);
        mPbSignInSpinner.setVisibility(View.VISIBLE);
        mEtEmail.setClicableTrue(false);
        mEtPassword.setClicableTrue(false);
        mEtPassword.showPasswordEtFocusDisable();
        mEtPassword.disableMaskPassoword();
    }

    private void hideSignInSpinner() {
        mPbSignInSpinner.setVisibility(View.INVISIBLE);
        mBtnSignInAccount.setEnabled(true);
        mEtEmail.setClicableTrue(true);
        mEtPassword.setClicableTrue(true);
        mEtPassword.showEtPasswordFocusEnable();
        mEtPassword.enableMaskPassword();
    }

    private void showForgotPasswordSpinner() {
        mPbForgotPasswdSpinner.setVisibility(View.VISIBLE);
        mBtnForgot.setEnabled(false);
    }

    private void hideForgotPasswordSpinner() {
        mPbForgotPasswdSpinner.setVisibility(View.INVISIBLE);
        mBtnForgot.setEnabled(true);
    }

    private void resetPassword() {
        boolean validatorResult = FieldsValidator.isValidEmail(mEtEmail.getEmailId().toString());
        if (!validatorResult) {
            mEtEmail.showInvalidAlert();
        } else {
            if (NetworkUtility.isNetworkAvailable(mContext)) {
                if (mUser != null) {
                    showForgotPasswordSpinner();
                    mEtEmail.clearFocus();
                    mEtPassword.clearFocus();
                    mBtnSignInAccount.setEnabled(false);
                    mBtnResend.setEnabled(false);
                    mUser.forgotPassword(mEtEmail.getEmailId(), this);
                }

            } else {
                mRegError.setError(getString(R.string.reg_NoNetworkConnection));
            }
        }
    }

    private void updateUiStatus() {
        if (mEtEmail.isValidEmail() && mEtPassword.isValidPassword()
                && NetworkUtility.isNetworkAvailable(mContext)) {
            mLlattentionBox.setVisibility(View.GONE);
            mBtnSignInAccount.setEnabled(true);
            mBtnForgot.setEnabled(true);
            mBtnResend.setEnabled(true);
            mRegError.hideError();
        } else if (mEtEmail.isValidEmail() && NetworkUtility.isNetworkAvailable(mContext)) {
            mBtnForgot.setEnabled(true);
            mBtnSignInAccount.setEnabled(false);
            mBtnResend.setEnabled(false);
        } else {
            if (mEtEmail.getEmailId().length() == 0) {
                mBtnForgot.setEnabled(true);
            } else {
                mBtnForgot.setEnabled(false);
                mBtnSignInAccount.setEnabled(false);
                mBtnResend.setEnabled(false);
            }
        }
    }

    @Override
    public void onUpadte() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                updateUiStatus();
            }
        });
    }

    @Override
    public void onEventReceived(String event) {
        RLog.i(RLog.EVENT_LISTENERS, "SignInAccountFragment :onEventReceived is : " + event);
        if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
            updateUiStatus();
        }
    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        RLog.i(RLog.NETWORK_STATE, "SignInAccountFragment : onNetWorkStateReceived state :"
                + isOnline);
        handleUiState();
        updateUiStatus();
    }

    @Override
    public void onResendVerificationEmailSuccess() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleResendVerificationEmailSuccess();
            }
        });

    }

    private void handleResendVerificationEmailSuccess() {
        trackActionStatus(AppTagingConstants.SEND_DATA,
                AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.SUCCESS_RESEND_EMAIL_VERIFICATION);
        RegAlertDialog.showResetPasswordDialog(mContext.getResources().getString(R.string.reg_Verification_email_Title),
                mContext.getResources().getString(R.string.reg_Verification_email_Message), getRegistrationFragment().getParentActivity(), mContinueVerifyBtnClick);
        updateResendUIState();
    }

    @Override
    public void onResendVerificationEmailFailedWithError(final UserRegistrationFailureInfo userRegistrationFailureInfo) {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleResendVerificationEmailFailed(userRegistrationFailureInfo);
            }
        });


    }

    private void handleResendVerificationEmailFailed(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.i(RLog.CALLBACK,
                "SignInAccountFragment : onResendVerificationEmailFailedWithError");
        updateResendUIState();
        trackActionResendVerificationFailure(userRegistrationFailureInfo.getErrorCode());
        mRegError.setError(userRegistrationFailureInfo.getErrorDescription() + "\n"
                + userRegistrationFailureInfo.getEmailErrorMessage());
        mBtnResend.setEnabled(true);
    }

    private void updateResendUIState() {
        mBtnSignInAccount.setEnabled(true);
        mBtnResend.setEnabled(true);
        mBtnForgot.setEnabled(true);
        hideResendSpinner();
    }

    private void handleLoginSuccess() {
        hideSignInSpinner();
        trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                AppTagingConstants.SUCCESS_LOGIN);
        mBtnForgot.setEnabled(true);
        mBtnResend.setEnabled(true);
        mRegError.hideError();

        if (mUser.getEmailVerificationStatus() || !RegistrationConfiguration.getInstance().getFlow().isEmailVerificationRequired()) {
            if (RegPreferenceUtility.getStoredState(mContext, mEmail)) {
                launchWelcomeFragment();
            } else {
                if (RegistrationConfiguration.getInstance().getFlow().isTermsAndConditionsAcceptanceRequired()) {
                    launchAlmostDoneScreenForTermsAcceptance();
                } else {

                    launchWelcomeFragment();
                }
            }
        } else {
            mEtEmail.setErrDescription(mContext.getResources().getString(R.string.reg_Janrain_Error_Need_Email_Verification));
            mEtEmail.showInvalidAlert();
            mEtEmail.showErrPopUp();
            mBtnSignInAccount.setEnabled(false);
            mBtnResend.setVisibility(View.VISIBLE);
            mViewHavingProblem.setVisibility(View.VISIBLE);
            mTvResendDetails.setVisibility(View.GONE);
            mLlattentionBox.setVisibility(View.VISIBLE);
            mViewAttentionBoxLine.setVisibility(View.INVISIBLE);
        }

    }

    private OnClickListener mContinueBtnClick = new OnClickListener() {

        @Override
        public void onClick(View view) {
            RegAlertDialog.dismissDialog();
        }
    };


    private OnClickListener mContinueVerifyBtnClick = new OnClickListener() {

        @Override
        public void onClick(View view) {
            RegAlertDialog.dismissDialog();
            updateActivationUIState();
        }
    };

    private void hideResendSpinner() {
        mPbResendSpinner.setVisibility(View.GONE);
    }

    private void showResendSpinner() {
        mPbResendSpinner.setVisibility(View.VISIBLE);
    }

    private void updateActivationUIState() {
        lauchAccountActivationFragment();
    }

    private void launchAlmostDoneScreenForTermsAcceptance() {
        getRegistrationFragment().addAlmostDoneFragmentforTermsAcceptance();
        trackPage(AppTaggingPages.ALMOST_DONE);
    }

    private void handleResend() {
        showResendSpinner();
        mBtnResend.setEnabled(false);
        mBtnSignInAccount.setEnabled(false);
        mBtnForgot.setEnabled(false);
        mUser.resendVerificationMail(mEtEmail.getEmailId(), this);
    }
}
