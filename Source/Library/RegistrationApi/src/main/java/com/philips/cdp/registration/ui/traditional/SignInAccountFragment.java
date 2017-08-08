/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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

import com.janrain.android.Jump;
import com.philips.cdp.registration.HttpClientService;
import com.philips.cdp.registration.HttpClientServiceReceiver;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.app.tagging.AppTaggingErrors;
import com.philips.cdp.registration.app.tagging.AppTaggingPages;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.ClientIDConfiguration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.events.EventListener;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.handlers.ForgotPasswordHandler;
import com.philips.cdp.registration.handlers.ResendVerificationEmailHandler;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.RegistrationSettingsURL;
import com.philips.cdp.registration.ui.customviews.LoginIdEditText;
import com.philips.cdp.registration.ui.customviews.OnUpdateListener;
import com.philips.cdp.registration.ui.customviews.PasswordView;
import com.philips.cdp.registration.ui.customviews.XButton;
import com.philips.cdp.registration.ui.customviews.XHavingProblems;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.traditional.mobile.MobileForgotPasswordVerifyCodeFragment;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegAlertDialog;
import com.philips.cdp.registration.ui.utils.RegChinaUtil;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegPreferenceUtility;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


public class SignInAccountFragment extends RegistrationBaseFragment implements OnClickListener,
        TraditionalLoginHandler, ForgotPasswordHandler, OnUpdateListener,
        EventListener, ResendVerificationEmailHandler,
        NetworStateListener, HttpClientServiceReceiver.Listener {

    @Inject
    NetworkUtility networkUtility;

    @Inject
    ServiceDiscoveryInterface serviceDiscoveryInterface;

    public static final String USER_REQUEST_PASSWORD_RESET_SMS_CODE = "/api/v1/user/requestPasswordResetSmsCode";
    public static final String USER_REQUEST_RESET_PASSWORD_REDIRECT_URI_SMS = "/c-w/user-registration/apps/reset-password.html";

    private LinearLayout mLlCreateAccountFields;

    private RelativeLayout mRlSignInBtnContainer;

    private Button mBtnSignInAccount;

    private Button mBtnForgot;

    private XButton mBtnResend;

    private LoginIdEditText mEtEmail;

    private PasswordView mEtPassword;

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

    private static final int SOCIAL_SIGIN_IN_ONLY_CODE = 540;

    private static final int UN_EXPECTED_ERROR = 500;

    private static final int BAD_RESPONSE_CODE = 7004;

    private static final int INPUTS_INVALID_CODE = 390;

    private ScrollView mSvRootLayout;

    private boolean isSavedEmailError;

    private boolean isSavedRegError;

    private boolean isSavedPasswordErr;

    private boolean isSavedVerifyEmail;

    private RegistrationSettingsURL registrationSettingsURL;
    String resetPasswordSmsRedirectUri;



    /*public static final String DEV_VERFICATION_CODE = "http://10.128.30.23:8080/philips-api/api/v1/user/requestVerificationSmsCode";
    public static final String EVAL_VERFICATION_CODE = "https://acc.philips.com.cn/api/v1/user/requestVerificationSmsCode";
    public static final String PROD_VERFICATION_CODE = "https://www.philips.com.cn/api/v1/user/requestVerificationSmsCode";
    public static final String STAGE_VERFICATION_CODE = "https://acc.philips.com.cn/api/v1/user/requestVerificationSmsCode";
    public static final String TEST_VERFICATION_CODE = "https://tst.philips.com/api/v1/user/requestVerificationSmsCode";
*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        URInterface.getComponent().inject(this);
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

    private boolean isLoginBtn;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mBundle = outState;
        super.onSaveInstanceState(mBundle);
        if (mBundle != null) {
            if (mRegError != null) {
                if (mRegError.getVisibility() == View.VISIBLE) {
                    mBundle.putBoolean("isSavedRegError", true);
                    mBundle.putString("saveErrText", mRegError.getErrorMsg());
                }
            }
            if (mEtEmail != null) {
                if (mEtEmail.isEmailErrorVisible()) {
                    isSavedEmailError = true;
                    mBundle.putBoolean("isSaveEmailErrText", isSavedEmailError);
                    mBundle.putString("saveEmailErrText", mEtEmail.getSavedEmailErrDescription());
                }
            }
            if (mEtPassword != null) {
                if (mEtPassword.isPasswordErrorVisible()) {
                    isSavedPasswordErr = true;
                    mBundle.putBoolean("isSavedPasswordErr", isSavedPasswordErr);
                    mBundle.putString("savedPasswordErr", mEtPassword.getmSavedPasswordErrDescription());
                }
            }
            if (mBtnResend != null) {
                if (mBtnResend.getVisibility() == View.VISIBLE && mEtEmail.isEmailErrorVisible()) {
                    isSavedVerifyEmail = true;
                    mBundle.putBoolean("isSavedVerifyEmail", isSavedVerifyEmail);
                    mBundle.putString("savedVerifyEmail", mEtEmail.getSavedEmailErrDescription());
                }
            }
            mBundle.putBoolean("isLoginBton", isLoginBtn);
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.getString("saveEmailErrText") != null
                    && savedInstanceState.getBoolean("isSaveEmailErrText")
                    && !savedInstanceState.getBoolean("isLoginBton")) {
                mEtEmail.setErrDescription(savedInstanceState.getString("saveEmailErrText"));
                mEtEmail.showInvalidAlert();
                mEtEmail.showErrPopUp();

            } else if (savedInstanceState.getBoolean("isSavedRegError")) {
                mRegError.setError(savedInstanceState.getString("saveErrText"));
            }
            if (savedInstanceState.getString("savedPasswordErr") != null && savedInstanceState.getBoolean("isSavedPasswordErr")) {
                mEtPassword.setErrDescription(savedInstanceState.getString("savedPasswordErr"));
                mEtPassword.showInvalidPasswordAlert();
            }
            if (savedInstanceState.getString("savedVerifyEmail") != null && savedInstanceState.getBoolean("isSavedVerifyEmail")) {
                mEtEmail.setErrDescription(savedInstanceState.getString("savedVerifyEmail"));
                mEtEmail.showInvalidAlert();
                mEtEmail.showErrPopUp();
                mBtnResend.setVisibility(View.VISIBLE);
            }
        }
        mBundle = null;
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

    private Bundle mBundle;

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_reg_sign_in) {
            isLoginBtn = true;
            RLog.d(RLog.ONCLICK, "SignInAccountFragment : SignIn");
            hideValidations();
            signIn();
        } else if (id == R.id.btn_reg_forgot_password) {
            isLoginBtn = false;
            RLog.d(RLog.ONCLICK, "SignInAccountFragment : Forgot Password");
            hideValidations();
            mEtEmail.clearFocus();
            mEtPassword.clearFocus();
            if (mEtEmail.getEmailId().length() == 0) {

                launchResetPasswordFragment();
            } else {
                RLog.d(RLog.ONCLICK, "SignInAccountFragment : I am in Other Country");
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
        mRegError.hideError();
    }

    private void launchResetPasswordFragment() {
//        if (registrationSettingsURL.isChinaFlow()) {
//            getRegistrationFragment().addFragment(new ResetPasswordWebView());
//        } else {
        getRegistrationFragment().addResetPasswordFragment();

        //}
        trackPage(AppTaggingPages.FORGOT_PASSWORD);
    }

    private void lauchAccountActivationFragment() {
        getRegistrationFragment().launchAccountActivationFragmentForLogin();
    }

    private void initUI(View view) {
        consumeTouch(view);
        mBtnSignInAccount = (Button) view.findViewById(R.id.btn_reg_sign_in);
        mViewHavingProblem = (XHavingProblems) view.findViewById(R.id.view_having_problem);
        mBtnSignInAccount.setOnClickListener(this);
        mBtnForgot = (Button) view.findViewById(R.id.btn_reg_forgot_password);
        mBtnForgot.setOnClickListener(this);
        mBtnResend = (XButton) view.findViewById(R.id.btn_reg_resend);
        mBtnResend.setOnClickListener(this);
        mLlCreateAccountFields = (LinearLayout) view
                .findViewById(R.id.ll_reg_create_account_fields);
        mRlSignInBtnContainer = (RelativeLayout) view.findViewById(R.id.rl_reg_welcome_container);

        mEtEmail = (LoginIdEditText) view.findViewById(R.id.rl_reg_email_field);
        mEtEmail.checkingEmailorMobileSignIn();
        mEtEmail.setOnClickListener(this);
        mEtEmail.setOnUpdateListener(this);
        mEtEmail.setFocusable(true);
        ((RegistrationFragment) getParentFragment()).showKeyBoard();
        mEtEmail.requestFocus();

        mEtPassword = (PasswordView) view.findViewById(R.id.rl_reg_password_field);
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
        registrationSettingsURL = new RegistrationSettingsURL();
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
        if (FieldsValidator.isValidEmail(mEtEmail.getEmailId())) {
            mEmail = mEtEmail.getEmailId();
        } else {
            mEmail = FieldsValidator.getMobileNumber(mEtEmail.getEmailId());
        }

        mUser.loginUsingTraditional(mEmail, mEtPassword.getPassword()
                .toString(), this);
    }

    private String mEmail;

    private void handleUiState() {
        if (networkUtility.isNetworkAvailable()) {
            mRegError.hideError();
        } else {
            mRegError.setError(getString(R.string.reg_NoNetworkConnection));
            scrollViewAutomatically(mRegError, mSvRootLayout);
        }
    }

    @Override
    public void onLoginSuccess() {
        handleLoginSuccess();
    }

    private void launchWelcomeFragment() {
        getRegistrationFragment().addWelcomeFragmentOnVerification();
        trackPage(AppTaggingPages.WELCOME);
    }

    @Override
    public void onLoginFailedWithError(final UserRegistrationFailureInfo userRegistrationFailureInfo) {

        handleLogInFailed(userRegistrationFailureInfo);
    }

    private void handleLogInFailed(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.i(RLog.CALLBACK, "SignInAccountFragment : onLoginFailedWithError");
        mBtnForgot.setEnabled(true);
        mBtnResend.setEnabled(true);
        hideSignInSpinner();
        mBtnSignInAccount.setEnabled(false);
        if (userRegistrationFailureInfo.getErrorCode() == -1 || userRegistrationFailureInfo.getErrorCode() == BAD_RESPONSE_CODE
                || userRegistrationFailureInfo.getErrorCode() == UN_EXPECTED_ERROR || userRegistrationFailureInfo.getErrorCode() == INPUTS_INVALID_CODE) {
            mRegError.setError(mContext.getResources().getString(R.string.reg_JanRain_Server_Connection_Failed));
        } else {
            if (userRegistrationFailureInfo.getErrorCode() >= RegConstants.HSDP_LOWER_ERROR_BOUND) {
                scrollViewAutomatically(mRegError, mSvRootLayout);
                mRegError.setError(mContext.getResources().getString(R.string.reg_Generic_Network_Error));
                scrollViewAutomatically(mRegError, mSvRootLayout);
            } else {
                if (userRegistrationFailureInfo.getErrorDescription() != null) {
                    mRegError.setError(userRegistrationFailureInfo.getErrorDescription());
                    scrollViewAutomatically(mRegError, mSvRootLayout);
                }
            }
        }
    }

    @Override
    public void onSendForgotPasswordSuccess() {
        handleSendForgotSuccess();
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
        handleSendForgetPasswordFailure(userRegistrationFailureInfo);
    }

    private void handleSendForgetPasswordFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {
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
            AppTaggingErrors.trackActionForgotPasswordFailure(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
            mBtnForgot.setEnabled(false);
            return;
        } else {
            mLlattentionBox.setVisibility(View.GONE);
            if (userRegistrationFailureInfo.getErrorCode() == -1) {
                mRegError.setError(mContext.getResources().getString(R.string.reg_JanRain_Server_Connection_Failed));
            }
        }


        if (null != userRegistrationFailureInfo.getErrorDescription()) {
            mEtEmail.setErrDescription(userRegistrationFailureInfo.getErrorDescription());
            mEtEmail.showInvalidAlert();
            mEtEmail.showErrPopUp();
        }
        AppTaggingErrors.trackActionForgotPasswordFailure(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
    }

    private void showSignInSpinner() {
        mBtnSignInAccount.setEnabled(false);
        mPbSignInSpinner.setVisibility(View.VISIBLE);
        mEtEmail.setClickableTrue(false);
        mEtPassword.setClicableTrue(false);
        mEtPassword.showPasswordEtFocusDisable();
        mEtPassword.disableMaskPassoword();
    }

    private void hideSignInSpinner() {
        mPbSignInSpinner.setVisibility(View.INVISIBLE);
        mBtnSignInAccount.setEnabled(true);
        mEtEmail.setClickableTrue(true);
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
        boolean validatorResult;
        if (FieldsValidator.isValidEmail(mEtEmail.getEmailId())) {
            validatorResult = true;
        } else {
            validatorResult = FieldsValidator.isValidMobileNumber(mEtEmail.getEmailId());
        }
        if (!validatorResult) {
            mEtEmail.showInvalidAlert();
        } else {
            if (networkUtility.isNetworkAvailable()) {
                if (mUser != null) {
                    showForgotPasswordSpinner();
                    mEtEmail.clearFocus();
                    mEtPassword.clearFocus();
                    mBtnSignInAccount.setEnabled(false);
                    mBtnResend.setEnabled(false);
                    //mUser.forgotPassword(mEtEmail.getEmailId(), this);
                    if (FieldsValidator.isValidEmail(mEtEmail.getEmailId())) {
                        mUser.forgotPassword(mEtEmail.getEmailId(), this);
                    } else {
                        serviceDiscovery();
                    }
                }

            } else {
                mRegError.setError(getString(R.string.reg_NoNetworkConnection));
            }
        }
    }

    private void updateUiStatus() {
        if (mEtEmail.isValidEmail() && mEtPassword.isValidPassword()
                && networkUtility.isNetworkAvailable()) {
            mLlattentionBox.setVisibility(View.GONE);
            mBtnSignInAccount.setEnabled(true);
            mBtnForgot.setEnabled(true);
            mBtnResend.setEnabled(true);
            mRegError.hideError();
        } else if (mEtEmail.isValidEmail() && networkUtility.isNetworkAvailable()) {
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
    public void onUpdate() {
        updateUiStatus();
    }

    @Override
    public void onEventReceived(String event) {
        RLog.i(RLog.EVENT_LISTENERS, "SignInAccountFragment :onCounterEventReceived is : " + event);
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
        handleResendVerificationEmailSuccess();
    }

    private void handleResendVerificationEmailSuccess() {
        trackMultipleActionResendEmailStatus();
        RegAlertDialog.showResetPasswordDialog(mContext.getResources().getString(R.string.reg_Verification_email_Title),
                mContext.getResources().getString(R.string.reg_Verification_email_Message), getRegistrationFragment().getParentActivity(), mContinueVerifyBtnClick);
        updateResendUIState();
    }

    private void trackMultipleActionResendEmailStatus() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.SUCCESS_RESEND_EMAIL_VERIFICATION);
        map.put(AppTagingConstants.STATUS_NOTIFICATION, AppTagingConstants.RESEND_VERIFICATION_MAIL_LINK_SENT);
        trackMultipleActionsMap(AppTagingConstants.SEND_DATA, map);
    }

    @Override
    public void onResendVerificationEmailFailedWithError(final UserRegistrationFailureInfo userRegistrationFailureInfo) {
        handleResendVerificationEmailFailed(userRegistrationFailureInfo);

    }

    private void handleResendVerificationEmailFailed(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.i(RLog.CALLBACK,
                "SignInAccountFragment : onResendVerificationEmailFailedWithError");
        updateResendUIState();
        AppTaggingErrors.trackActionResendNetworkFailure(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
        mRegError.setError(userRegistrationFailureInfo.getErrorDescription());
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
        mBtnForgot.setEnabled(true);
        mBtnResend.setEnabled(true);
        mRegError.hideError();

        boolean isEmailAvailable = mUser.getEmail() != null && FieldsValidator.isValidEmail(mUser.getEmail());
        boolean isMobileNoAvailable = mUser.getMobile() != null && FieldsValidator.isValidMobileNumber(mUser.getMobile());
        if (isEmailAvailable && isMobileNoAvailable && !mUser.isEmailVerified()) {
            lauchAccountActivationFragment();
            return;
        }

        if ((mUser.isEmailVerified() || mUser.isMobileVerified()) || !RegistrationConfiguration.getInstance().isEmailVerificationRequired()) {
            if (RegPreferenceUtility.getStoredState(mContext, mEmail) && mUser.getReceiveMarketingEmail()) {
                launchWelcomeFragment();
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                        AppTagingConstants.SUCCESS_LOGIN);
            } else {
                if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired() || !mUser.getReceiveMarketingEmail()) {
                    launchAlmostDoneScreenForTermsAcceptance();
                } else {
                    trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                            AppTagingConstants.SUCCESS_LOGIN);
                    launchWelcomeFragment();
                }
            }
        } else {
            if (FieldsValidator.isValidEmail(mEtEmail.getEmailId().toString())) {
                mEtEmail.setErrDescription(mContext.getResources().getString(R.string.reg_Janrain_Error_Need_Email_Verification));
                mTvResendDetails.setText(mContext.getResources().getString(R.string.reg_VerifyEmail_ResendErrorMsg_lbltxt));
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.USER_ERROR, AppTagingConstants.EMAIL_NOT_VERIFIED);
            } else {
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.USER_ERROR, AppTagingConstants.MOBILE_NUMBER_NOT_VERIFIED);
                mEtEmail.setErrDescription(mContext.getResources().getString(R.string.reg_Janrain_Error_Need_Mobile_Verification));
                mTvResendDetails.setText(mContext.getResources().getString(R.string.reg_Mobile_TraditionalSignIn_Instruction_lbltxt));
            }
            mTvResendDetails.setVisibility(View.VISIBLE);
            mViewHavingProblem.setVisibility(View.GONE);
            mEtEmail.showInvalidAlert();
            mEtEmail.showErrPopUp();
            mBtnSignInAccount.setEnabled(false);
            mBtnResend.setVisibility(View.VISIBLE);
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
        if (registrationSettingsURL.isChinaFlow()) {
            serviceDiscovery();
        } else {
            mUser.resendVerificationMail(mEtEmail.getEmailId(), this);
        }
    }

    private Intent createResendSMSIntent() {
        RLog.d(RLog.EVENT_LISTENERS, "MOBILE NUMBER * ** : " + mUser.getMobile());
        String url = verificationSmsCodeURL + "?provider=" +
                "JANRAIN-CN&locale=zh_CN" + "&phonenumber=" + FieldsValidator.getMobileNumber(mUser.getMobile());

        Intent httpServiceIntent = new Intent(mContext, HttpClientService.class);

        HttpClientServiceReceiver receiver = new HttpClientServiceReceiver(new Handler());
        receiver.setListener(this);
        RequestBody emptyBody = RequestBody.create(null, new byte[0]);
        httpServiceIntent.putExtra("receiver", receiver);
        httpServiceIntent.putExtra("bodyContent", emptyBody.toString());
        httpServiceIntent.putExtra("url", url);
        return httpServiceIntent;
    }

    private void trackMultipleActionsOnMobileSuccess() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.MOBILE_RESEND_EMAIL_VERFICATION);
        map.put(AppTagingConstants.MOBILE_INAPPNATIFICATION, AppTagingConstants.MOBILE_RESEND_SMS_VERFICATION);
        AppTagging.trackMultipleActions(AppTagingConstants.SEND_DATA, map);
    }

    String verificationSmsCodeURL;

    private void serviceDiscovery() {
        RLog.d(RLog.SERVICE_DISCOVERY, " Country :" + RegistrationHelper.getInstance().getCountryCode());

        //Temp: will be updated once actual URX received for reset sms

        serviceDiscoveryInterface.getServiceUrlWithCountryPreference("userreg.urx.verificationsmscode", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {

                RLog.d(RLog.SERVICE_DISCOVERY, " onError  : userreg.urx.verificationsmscode : " + errorvalues);
                verificationSmsCodeURL = null;
                mEtEmail.setErrDescription(getResources().getString(R.string.reg_Generic_Network_Error));
                mEtEmail.showErrPopUp();
                mEtEmail.showInvalidAlert();
                updateResendUIState();
            }

            @Override
            public void onSuccess(URL url) {
                RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.urx.verificationsmscode:" + url.toString());

                String uriSubString = getBaseString(url.toString());
                //Verification URI
                verificationSmsCodeURL = uriSubString + USER_REQUEST_PASSWORD_RESET_SMS_CODE;
                //Redirect URI
                resetPasswordSmsRedirectUri = uriSubString + USER_REQUEST_RESET_PASSWORD_REDIRECT_URI_SMS;

                getRegistrationFragment().getActivity().startService(createResendSMSIntent(verificationSmsCodeURL));
            }
        });


    }

    @NonNull
    private String getBaseString(String respnseUrl) {
        URL url = null;

        try {
            url = new URL(respnseUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return (url.getProtocol() + "://" + url.getHost());
    }

    private void handleResendVerificationSMSSuccess() {
        trackActionStatus(AppTagingConstants.SEND_DATA,
                AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.SUCCESS_RESEND_EMAIL_VERIFICATION);
    }

    private void handleResendSMSRespone(String response) {

        final String mobileNumberKey = "mobileNumber";
        final String tokenKey = "token";
        final String redirectUriKey = "redirectUri";
        final String verificationSmsCodeURLKey = "verificationSmsCodeURL";
        updateResendUIState();

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("errorCode").toString().equals("0")) {
                handleResendVerificationSMSSuccess();
                JSONObject json = null;
                String payload = null;
                String token = null;
                try {
                    json = new JSONObject(response);
                    payload = json.getString("payload");
                    json = new JSONObject(payload);
                    token = json.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RLog.i("MobileVerifyCodeFragment ", " isAccountActivate is " + token + " -- " + response);
                MobileForgotPasswordVerifyCodeFragment mobileForgotPasswordVerifyCodeFragment = new MobileForgotPasswordVerifyCodeFragment();
                Bundle bundle = new Bundle();
                bundle.putString(mobileNumberKey, mEtEmail.getEmailId());
                bundle.putString(tokenKey, token);
                bundle.putString(redirectUriKey, getRedirectUri());
                bundle.putString(verificationSmsCodeURLKey, verificationSmsCodeURL);
                mobileForgotPasswordVerifyCodeFragment.setArguments(bundle);
                getRegistrationFragment().addFragment(mobileForgotPasswordVerifyCodeFragment);
            } else {
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.TECHNICAL_ERROR, AppTagingConstants.MOBILE_RESEND_SMS_VERFICATION_FAILURE);
                String errorMsg = RegChinaUtil.getErrorMsgDescription(jsonObject.getString("errorCode").toString(), mContext);
                mEtEmail.setErrDescription(errorMsg);
                mEtEmail.showErrPopUp();
                mEtEmail.showInvalidAlert();
                RLog.i("MobileVerifyCodeFragment ", " SMS Resend failure = " + response);
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Intent createResendSMSIntent(String url) {
        final String receiverKey = "receiver";
        final String bodyContentKey = "bodyContent";
        final String urlKey = "url";

        RLog.d(RLog.EVENT_LISTENERS, "MOBILE NUMBER *** : " + mEtEmail.getEmailId());
        RLog.d("Configration : ", " envir :" + RegistrationConfiguration.getInstance().getRegistrationEnvironment());

        Intent httpServiceIntent = new Intent(mContext, HttpClientService.class);
        HttpClientServiceReceiver receiver = new HttpClientServiceReceiver(new Handler());
        receiver.setListener(this);

        String bodyContent = "provider=JANRAIN-CN&phonenumber=" + FieldsValidator.getMobileNumber(mEtEmail.getEmailId()) +
                "&locale=zh_CN&clientId=" + getClientId() + "&code_type=short&" +
                "redirectUri=" + getRedirectUri();
        RLog.d("Configration : ", " envir :" + getClientId() + getRedirectUri());

        httpServiceIntent.putExtra(receiverKey, receiver);
        httpServiceIntent.putExtra(bodyContentKey, bodyContent);
        httpServiceIntent.putExtra(urlKey, url);
        return httpServiceIntent;
    }

    private String getClientId() {
        ClientIDConfiguration clientIDConfiguration = new ClientIDConfiguration();
        return clientIDConfiguration.getResetPasswordClientId(RegConstants.HTTPS_CONST + Jump.getCaptureDomain());
    }

    private String getRedirectUri() {
        return resetPasswordSmsRedirectUri;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        String response = resultData.getString("responseStr");
        RLog.i("MobileVerifyCodeFragment ", "onReceiveResult Response Val = " + response);
        hideForgotPasswordSpinner();
        if (response == null) {
            mEtEmail.showInvalidAlert();
            mEtEmail.setErrDescription(mContext.getResources().getString(R.string.reg_Invalid_PhoneNumber_ErrorMsg));
            mEtEmail.showErrPopUp();
            updateResendUIState();
        } else {
            handleResendSMSRespone(response);
        }
    }
}
