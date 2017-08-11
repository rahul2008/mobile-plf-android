/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional;

import android.content.*;
import android.content.res.Configuration;
import android.os.*;
import android.support.annotation.*;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.ProgressBar;

import com.jakewharton.rxbinding2.widget.*;
import com.janrain.android.Jump;
import com.philips.cdp.registration.*;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.app.tagging.*;
import com.philips.cdp.registration.configuration.*;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.*;
import com.philips.cdp.registration.events.EventListener;
import com.philips.cdp.registration.handlers.*;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.customviews.*;
import com.philips.cdp.registration.ui.traditional.mobile.MobileForgotPasswordVerifyCodeFragment;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.uid.view.widget.*;
import com.squareup.okhttp.RequestBody;

import org.json.*;

import java.net.*;
import java.util.*;

import javax.inject.Inject;

import butterknife.*;
import io.reactivex.Observable;
import io.reactivex.disposables.*;


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

    @BindView(R2.id.usr_loginScreen_login_button)
    ProgressBarButton mBtnSignInAccount;

    @BindView(R2.id.usr_loginScreen_login_inputLayout)
    InputValidationLayout mEtEmail;

    @BindView(R2.id.usr_loginScreen_login_textField)
    ValidationEditText loginValidationEditText;

    @BindView(R2.id.usr_loginScreen_password_inputLayout)
    InputValidationLayout mEtPassword;

    @BindView(R2.id.usr_loginScreen_password_textField)
    ValidationEditText passwordValidationEditText;

    @BindView(R2.id.usr_loginScreen_forgotPassword_button)
    Label resetPasswordLabel;

    private User mUser;

    private ProgressBar mPbResendSpinner;

    private XRegError mRegError;

    private Context mContext;

    private static final int SOCIAL_SIGIN_IN_ONLY_CODE = 540;

    private static final int UN_EXPECTED_ERROR = 500;

    private static final int BAD_RESPONSE_CODE = 7004;

    private static final int INPUTS_INVALID_CODE = 390;

    private ScrollView mSvRootLayout;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private RegistrationSettingsURL registrationSettingsURL;

    String resetPasswordSmsRedirectUri;

    String verificationSmsCodeURL;

    private String mEmail;

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
        ButterKnife.bind(this, view);
        mBtnSignInAccount.setEnabled(false);
        RLog.i(RLog.EVENT_LISTENERS,
                "SignInAccountFragment register: NetworStateListener,JANRAIN_INIT_SUCCESS");
        mSvRootLayout = (ScrollView) view.findViewById(R.id.sv_root_layout);
        initUI(view);
        handleOrientation(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onViewCreated");
        compositeDisposable.add(observeLoginButton());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "SignInAccountFragment : onDestroyView");
        compositeDisposable.dispose();
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
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.usr_loginScreen_login_button) {
            RLog.d(RLog.ONCLICK, "SignInAccountFragment : SignIn");
            hideValidations();
            mBtnSignInAccount.showProgressIndicator();
            signIn();
        } else if (id == R.id.btn_reg_forgot_password) {
            RLog.d(RLog.ONCLICK, "SignInAccountFragment : Forgot Password");
            hideValidations();
            loginValidationEditText.clearFocus();
            passwordValidationEditText.clearFocus();
            if (loginValidationEditText.getText().length() == 0) {
                launchResetPasswordFragment();
            } else {
                RLog.d(RLog.ONCLICK, "SignInAccountFragment : I am in Other Country");
                resetPassword();
            }
        } else if (id == R.id.btn_reg_resend) {
            RLog.d(RLog.ONCLICK, "SignInAccountFragment : Resend");
            loginValidationEditText.clearFocus();
            passwordValidationEditText.clearFocus();
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
        mBtnSignInAccount.setOnClickListener(this);
        mLlCreateAccountFields = (LinearLayout) view
                .findViewById(R.id.ll_reg_create_account_fields);
        mRlSignInBtnContainer = (RelativeLayout) view.findViewById(R.id.rl_reg_welcome_container);

        mEtEmail.setOnClickListener(this);
        mEtEmail.setValidator(email -> FieldsValidator.isValidEmail(email.toString()));
        mEtEmail.setErrorMessage("Please enter a valid email address.");
        mEtEmail.setFocusable(true);
        ((RegistrationFragment) getParentFragment()).showKeyBoard();
        mEtEmail.requestFocus();

        mEtPassword.setOnClickListener(this);
        mEtPassword.setValidator(password -> password.length() > 0);
        mEtPassword.setErrorMessage("Please enter a valid password");
        underlineResetPassword();
        mRegError = (XRegError) view.findViewById(R.id.usr_loginScreen_error_view);
        handleUiState();

        mUser = new User(mContext);
        mPbResendSpinner = (ProgressBar) view.findViewById(R.id.pb_reg_resend_spinner);
        registrationSettingsURL = new RegistrationSettingsURL();
    }

    private void underlineResetPassword() {
        SpannableString content = new SpannableString(resetPasswordLabel.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        resetPasswordLabel.setText(content);
        resetPasswordLabel.setOnClickListener(view -> launchResetPasswordFragment());
    }

    private Disposable observeLoginButton() {
        return Observable.combineLatest(getLoginIdObservable(), getPasswordObservable(),
                (loginValid, passwordValid) -> loginValid && passwordValid)
                .subscribe(enabled -> mBtnSignInAccount.setEnabled(enabled));
    }

    private Observable<Boolean> getLoginIdObservable() {
        return RxTextView.textChanges(loginValidationEditText)
                .map(email -> FieldsValidator.isValidEmail(email.toString()));
    }

    private Observable<Boolean> getPasswordObservable() {
        return RxTextView.textChanges(passwordValidationEditText)
                .map(password -> password.length() >= 8);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_SigIn_TitleTxt;
    }

    private void signIn() {
        ((RegistrationFragment) getParentFragment()).hideKeyBoard();
        mRegError.hideError();
        mEtEmail.clearFocus();
        loginValidationEditText.setEnabled(false);
        mEtPassword.clearFocus();
        passwordValidationEditText.setEnabled(false);
        resetPasswordLabel.setClickable(false);
        if (mUser != null) {
            showSignInSpinner();
        }
        if (FieldsValidator.isValidEmail(loginValidationEditText.getText().toString())) {
            mEmail = loginValidationEditText.getText().toString();
        }
        else {
            mEmail = FieldsValidator.getMobileNumber(loginValidationEditText.getText().toString());
        }

        mUser.loginUsingTraditional(mEmail, passwordValidationEditText.getText().toString(), this);
    }

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
        hideSignInSpinner();
        mBtnSignInAccount.hideProgressIndicator();
        loginValidationEditText.setEnabled(true);
        passwordValidationEditText.setEnabled(true);
        resetPasswordLabel.setClickable(true);

        if (userRegistrationFailureInfo.getErrorCode() == -1 || userRegistrationFailureInfo.getErrorCode() == BAD_RESPONSE_CODE
                || userRegistrationFailureInfo.getErrorCode() == UN_EXPECTED_ERROR || userRegistrationFailureInfo.getErrorCode() == INPUTS_INVALID_CODE) {
            mRegError.setError(mContext.getResources().getString(R.string.reg_JanRain_Server_Connection_Failed));
        } else {
            if (userRegistrationFailureInfo.getErrorCode() >= RegConstants.HSDP_LOWER_ERROR_BOUND) {
                //HSDP related error description
                scrollViewAutomatically(mRegError, mSvRootLayout);
                mRegError.setError(mContext.getResources().getString(R.string.reg_Generic_Network_Error));
                scrollViewAutomatically(mRegError, mSvRootLayout);
            } else {
                //Need to show password errors only
                scrollViewAutomatically(mRegError, mSvRootLayout);
                if (userRegistrationFailureInfo.getErrorCode() == RegConstants.INVALID_CREDENTIALS_ERROR_CODE) {
                    mRegError.setError(mContext.getResources().getString(R.string.reg_JanRain_Invalid_Credentials));
                } else {
                    if (null != userRegistrationFailureInfo.getPasswordErrorMessage()) {
                        mRegError.setError(userRegistrationFailureInfo.getPasswordErrorMessage());
                    } else {
                        mRegError.setError(userRegistrationFailureInfo.getErrorDescription());
                    }
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
                mContext.getResources().getString(R.string.reg_ForgotPwdEmailResendMsg), getRegistrationFragment().getParentActivity(), view -> RegAlertDialog.dismissDialog());
        hideForgotPasswordSpinner();
        mRegError.hideError();
    }

    @Override
    public void onSendForgotPasswordFailedWithError(final
                                                    UserRegistrationFailureInfo userRegistrationFailureInfo) {
        handleSendForgetPasswordFailure(userRegistrationFailureInfo);
    }

    private void handleSendForgetPasswordFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.i(RLog.CALLBACK, "SignInAccountFragment : onSendForgotPasswordFailedWithError ERROR CODE :" + userRegistrationFailureInfo.getErrorCode());
        hideForgotPasswordSpinner();

        if (userRegistrationFailureInfo.getErrorCode() == SOCIAL_SIGIN_IN_ONLY_CODE) {
            mEtEmail.setErrorMessage(getString(R.string.reg_TraditionalSignIn_ForgotPwdSocialError_lbltxt));
            trackActionStatus(AppTagingConstants.SEND_DATA,
                    AppTagingConstants.USER_ERROR, AppTagingConstants.ALREADY_SIGN_IN_SOCIAL);
            AppTaggingErrors.trackActionForgotPasswordFailure(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
            return;
        } else {
            if (userRegistrationFailureInfo.getErrorCode() == -1) {
                mRegError.setError(mContext.getResources().getString(R.string.reg_JanRain_Server_Connection_Failed));
            }
        }

        if (null != userRegistrationFailureInfo.getSocialOnlyError()) {
//            mEtEmail.showErrPopUp();
            mEtEmail.setErrorMessage(userRegistrationFailureInfo.getSocialOnlyError());
//            mEtEmail.showInvalidAlert();
            AppTaggingErrors.trackActionForgotPasswordFailure(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
            return;
        }

        if (null != userRegistrationFailureInfo.getEmailErrorMessage()) {
            mEtEmail.setErrorMessage(userRegistrationFailureInfo.getEmailErrorMessage());
//            mEtEmail.showInvalidAlert();
//            mEtEmail.showErrPopUp();
        }
        AppTaggingErrors.trackActionForgotPasswordFailure(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
    }

    private void showSignInSpinner() {
        mBtnSignInAccount.setEnabled(false);
        mEtEmail.setClickable(false);
        mEtPassword.setClickable(false);
//        mEtPassword.showPasswordEtFocusDisable();
//        mEtPassword.disableMaskPassoword();
    }

    private void hideSignInSpinner() {
//        mPbSignInSpinner.setVisibility(View.INVISIBLE);
        mBtnSignInAccount.setEnabled(true);
        mEtEmail.setClickable(true);
        mEtPassword.setClickable(true);
//        mEtPassword.showEtPasswordFocusEnable();
//        mEtPassword.enableMaskPassword();
    }

    private void showForgotPasswordSpinner() {
//        mPbForgotPasswdSpinner.setVisibility(View.VISIBLE);
//        mBtnForgot.setEnabled(false);
    }

    private void hideForgotPasswordSpinner() {
//        mPbForgotPasswdSpinner.setVisibility(View.INVISIBLE);
//        mBtnForgot.setEnabled(true);
    }

    private void resetPassword() {
        boolean validatorResult;
        if (FieldsValidator.isValidEmail(loginValidationEditText.getText().toString())) {
            validatorResult = true;
        } else {
            validatorResult = FieldsValidator.isValidMobileNumber(loginValidationEditText.getText().toString());
        }
        if (!validatorResult) {
//            mEtEmail.showInvalidAlert();
        } else {
            if (networkUtility.isNetworkAvailable()) {
                if (mUser != null) {
                    showForgotPasswordSpinner();
                    mEtEmail.clearFocus();
                    mEtPassword.clearFocus();
                    mBtnSignInAccount.setEnabled(false);
                    mUser.forgotPassword(loginValidationEditText.getText().toString(), this);
                    if (FieldsValidator.isValidEmail(loginValidationEditText.getText().toString())) {
                        mUser.forgotPassword(loginValidationEditText.getText().toString(), this);
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
        if (FieldsValidator.isValidEmail(loginValidationEditText.getText().toString()) && networkUtility.isNetworkAvailable()) {
            mBtnSignInAccount.setEnabled(true);
            mRegError.hideError();
        } else if (FieldsValidator.isValidEmail(loginValidationEditText.getText().toString()) && networkUtility.isNetworkAvailable()) {
            mBtnSignInAccount.setEnabled(false);
        } else {
            if (loginValidationEditText.getText().length() != 0) {
                mBtnSignInAccount.setEnabled(false);
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
        mRegError.setError(userRegistrationFailureInfo.getErrorDescription() + "\n"
                + userRegistrationFailureInfo.getEmailErrorMessage());
    }

    private void updateResendUIState() {
        mBtnSignInAccount.setEnabled(true);
        hideResendSpinner();
    }

    private void handleLoginSuccess() {
        hideSignInSpinner();
        mRegError.hideError();
        mBtnSignInAccount.hideProgressIndicator();
        mBtnSignInAccount.setEnabled(true);
        loginValidationEditText.setEnabled(true);
        passwordValidationEditText.setEnabled(true);
        resetPasswordLabel.setClickable(true);

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
            if (FieldsValidator.isValidEmail(loginValidationEditText.getText().toString())) {
                mRegError.setError(mContext.getResources().getString(R.string.reg_Janrain_Error_Need_Email_Verification));
                mRegError.setVisibility(View.VISIBLE);
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.USER_ERROR, AppTagingConstants.EMAIL_NOT_VERIFIED);
            } else {
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.USER_ERROR, AppTagingConstants.MOBILE_NUMBER_NOT_VERIFIED);
                mRegError.setError(mContext.getResources().getString(R.string.reg_Janrain_Error_Need_Mobile_Verification));
                mRegError.setVisibility(View.VISIBLE);
            }
        }

    }

    private OnClickListener mContinueVerifyBtnClick = view -> {
        RegAlertDialog.dismissDialog();
        updateActivationUIState();
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
        mBtnSignInAccount.setEnabled(false);
        if (registrationSettingsURL.isChinaFlow()) {
            serviceDiscovery();
        } else {
            mUser.resendVerificationMail(loginValidationEditText.getText().toString(), this);
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

    private void serviceDiscovery() {
        RLog.d(RLog.SERVICE_DISCOVERY, " Country :" + RegistrationHelper.getInstance().getCountryCode());

        serviceDiscoveryInterface.getServiceUrlWithCountryPreference("userreg.urx.verificationsmscode", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {

                RLog.d(RLog.SERVICE_DISCOVERY, " onError  : userreg.urx.verificationsmscode : " + errorvalues);
                verificationSmsCodeURL = null;
                mEtEmail.setErrorMessage(getResources().getString(R.string.reg_Generic_Network_Error));
                updateResendUIState();
            }

            @Override
            public void onSuccess(URL url) {
                RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.urx.verificationsmscode:" + url.toString());

                String uriSubString = getBaseString(url.toString());
                verificationSmsCodeURL = uriSubString + USER_REQUEST_PASSWORD_RESET_SMS_CODE;
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
                bundle.putString(mobileNumberKey, loginValidationEditText.getText().toString());
                bundle.putString(tokenKey, token);
                bundle.putString(redirectUriKey, getRedirectUri());
                bundle.putString(verificationSmsCodeURLKey, verificationSmsCodeURL);
                mobileForgotPasswordVerifyCodeFragment.setArguments(bundle);
                getRegistrationFragment().addFragment(mobileForgotPasswordVerifyCodeFragment);
            } else {
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.TECHNICAL_ERROR, AppTagingConstants.MOBILE_RESEND_SMS_VERFICATION_FAILURE);
                String errorMsg = RegChinaUtil.getErrorMsgDescription(jsonObject.getString("errorCode"), mContext);
                mEtEmail.setErrorMessage(errorMsg);
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

        RLog.d(RLog.EVENT_LISTENERS, "MOBILE NUMBER *** : " + loginValidationEditText.getText().toString());
        RLog.d("Configration : ", " envir :" + RegistrationConfiguration.getInstance().getRegistrationEnvironment());

        Intent httpServiceIntent = new Intent(mContext, HttpClientService.class);
        HttpClientServiceReceiver receiver = new HttpClientServiceReceiver(new Handler());
        receiver.setListener(this);

        String bodyContent = "provider=JANRAIN-CN&phonenumber=" + FieldsValidator.getMobileNumber(loginValidationEditText.getText().toString()) +
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
            mEtEmail.setErrorMessage(mContext.getResources().getString(R.string.reg_Invalid_PhoneNumber_ErrorMsg));
            updateResendUIState();
        } else {
            handleResendSMSRespone(response);
        }
    }
}
