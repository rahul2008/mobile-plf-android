/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.janrain.android.Jump;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.R2;
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
import com.philips.cdp.registration.events.NetworkStateListener;
import com.philips.cdp.registration.handlers.ForgotPasswordHandler;
import com.philips.cdp.registration.handlers.ResendVerificationEmailHandler;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.restclient.URRequest;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.RegistrationSettingsURL;
import com.philips.cdp.registration.ui.customviews.OnUpdateListener;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.traditional.mobile.MobileForgotPassVerifyCodeFragment;
import com.philips.cdp.registration.ui.traditional.mobile.MobileVerifyCodeFragment;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.LoginFailureNotification;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegAlertDialog;
import com.philips.cdp.registration.ui.utils.RegChinaUtil;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegPreferenceUtility;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.InputValidationLayout;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.ProgressBarButton;
import com.philips.platform.uid.view.widget.ValidationEditText;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class SignInAccountFragment extends RegistrationBaseFragment implements OnClickListener,
        TraditionalLoginHandler, ForgotPasswordHandler, OnUpdateListener,
        EventListener, ResendVerificationEmailHandler,
        NetworkStateListener {

    private static final String TAG = SignInAccountFragment.class.getSimpleName();
    private static final String ALERT_DIALOG_TAG = "ALERT_DIALOG_TAG";

    @Inject
    NetworkUtility networkUtility;

    @Inject
    ServiceDiscoveryInterface serviceDiscoveryInterface;

    public static final String USER_REQUEST_PW_RESET_SMS_CODE = "/api/v1/user/requestPasswordResetSmsCode";

    public static final String USER_REQUEST_RESET_PW_REDIRECT_URI_SMS = "/c-w/user-registration/apps/reset-password.html";


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

    @BindView(R2.id.usr_loginScreen_email_label)
    Label usr_loginScreen_email_label;

    @BindView(R2.id.usr_loginScreen_progress_indicator)
    LinearLayout progressBar;

    private User mUser;

    private XRegError mRegError;

    private Context mContext;

    private static final int SOCIAL_SIGIN_IN_ONLY_CODE = 540;

    private static final int UN_EXPECTED_ERROR = 500;

    private static final int BAD_RESPONSE_CODE = 7004;

    private static final int INPUTS_INVALID_CODE = 390;

    private ScrollView mSvRootLayout;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private RegistrationSettingsURL registrationSettingsURL;

    private int mode;

    private AlertDialogFragment alertDialogFragment;

    private String resetPasswordSmsRedirectUri;

    private String verificationSmsCodeURL;

    private String mEmailOrMobile;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RegistrationConfiguration.getInstance().getComponent().inject(this);

        View view = inflater.inflate(R.layout.reg_fragment_sign_in_account, null);

        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        EventHelper.getInstance()
                .registerEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);

        ButterKnife.bind(this, view);
        mBtnSignInAccount.setEnabled(false);
        mSvRootLayout = view.findViewById(R.id.sv_root_layout);
        initUI(view);
        handleOrientation(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        compositeDisposable.add(observeLoginButton());
    }

    @Override
    public void onDestroyView() {
        getActivity().getWindow().setSoftInputMode(mode);
        super.onDestroyView();
        compositeDisposable.dispose();
    }

    @Override
    public void onDestroy() {
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
        EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_SUCCESS,
                this);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        setCustomParams(config);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        //Nop
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.usr_loginScreen_login_button) {
            hideValidations();
            disableAll();
            mBtnSignInAccount.showProgressIndicator();
            signIn();
        }
        if (alertDialogFragment != null) {
            alertDialogFragment.dismiss();
        } else {
            final AlertDialogFragment alertDialog = (AlertDialogFragment) getFragmentManager().findFragmentByTag(ALERT_DIALOG_TAG);
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
        }
    }

    private void hideValidations() {
        mRegError.hideError();
    }

    private void launchResetPasswordFragment() {
        getRegistrationFragment().addResetPasswordFragment();
        trackPage(AppTaggingPages.FORGOT_PASSWORD);
    }

    private void launchAccountActivationFragment() {
        getRegistrationFragment().launchAccountActivationFragmentForLogin();
    }

    private void initUI(View view) {
        consumeTouch(view);
        mBtnSignInAccount.setOnClickListener(this);

        mEtEmail.setValidator(email -> emailOrMobileValidator(email.toString()));
        mEtEmail.setFocusable(true);
        ((RegistrationFragment) getParentFragment()).showKeyBoard();
        mEtEmail.requestFocus();

        mEtPassword.setOnClickListener(this);
        mEtPassword.setValidator(password -> password.length() > 0);
        mEtPassword.setErrorMessage(getString(R.string.reg_EmptyField_ErrorMsg));
        linkifyPrivacyPolicy(resetPasswordLabel, forgotPasswordClickListener);
        mRegError = view.findViewById(R.id.usr_loginScreen_error_view);
        handleUiState();

        if (RegistrationHelper.getInstance().isMobileFlow()) {
            usr_loginScreen_email_label.setText(R.string.reg_DLS_Email_Phone_Label_Text);
        }
        mUser = new User(mContext);
        registrationSettingsURL = new RegistrationSettingsURL();
    }

    private boolean emailOrMobileValidator(String emailOrMobile) {
        if (emailOrMobile.isEmpty()) {
            mEtEmail.setErrorMessage(R.string.reg_EmptyField_ErrorMsg);
            return false;
        }

        if (RegistrationHelper.getInstance().isMobileFlow()) {
            if ((!FieldsValidator.isValidMobileNumber(emailOrMobile) || !FieldsValidator.isValidEmail(emailOrMobile))) {
                mEtEmail.setErrorMessage(R.string.reg_InvalidEmail_PhoneNumber_ErrorMsg);
                return FieldsValidator.isValidMobileNumber(emailOrMobile) || FieldsValidator.isValidEmail(emailOrMobile);
            }
        } else {
            mEtEmail.setErrorMessage(R.string.reg_InvalidEmailAdddress_ErrorMsg);
            return FieldsValidator.isValidEmail(emailOrMobile);
        }
        return false;
    }

    private ClickableSpan forgotPasswordClickListener = new ClickableSpan() {

        @Override
        public void onClick(View widget) {
            if (loginValidationEditText.getText().toString().trim().length() <= 0) {
                launchResetPasswordFragment();
            } else if (registrationSettingsURL.isMobileFlow() && (FieldsValidator.isValidMobileNumber(loginValidationEditText.getText().toString()))) {
                showForgotPasswordSpinner();
                handleResend();
            } else if (FieldsValidator.isValidEmail(loginValidationEditText.getText().toString())) {
                resetPassword();
            } else {
                mEtEmail.showError();
            }
        }

    };


    private void linkifyPrivacyPolicy(TextView pTvPrivacyPolicy, ClickableSpan span) {
        String privacy = pTvPrivacyPolicy.getText().toString();
        SpannableString spannableString = new SpannableString(privacy);

        spannableString.setSpan(span, 0, privacy.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        removeUnderlineFromLink(spannableString);

        pTvPrivacyPolicy.setText(spannableString);
        pTvPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());
        pTvPrivacyPolicy.setLinkTextColor(ContextCompat.getColor(getContext(),
                R.color.reg_hyperlink_highlight_color));
        pTvPrivacyPolicy.setHighlightColor(ContextCompat.getColor(getContext(),
                android.R.color.transparent));
    }

    private void removeUnderlineFromLink(SpannableString spanableString) {
        for (ClickableSpan u : spanableString.getSpans(0, spanableString.length(),
                ClickableSpan.class)) {
            spanableString.setSpan(new UnderlineSpan() {

                public void updateDrawState(TextPaint tp) {
                    tp.setUnderlineText(false);
                }
            }, spanableString.getSpanStart(u), spanableString.getSpanEnd(u), 0);
        }

        for (URLSpan u : spanableString.getSpans(0, spanableString.length(), URLSpan.class)) {
            spanableString.setSpan(new UnderlineSpan() {

                public void updateDrawState(TextPaint tp) {
                    tp.setUnderlineText(false);
                }
            }, spanableString.getSpanStart(u), spanableString.getSpanEnd(u), 0);
        }
    }

    private Disposable observeLoginButton() {
        return Observable.combineLatest(getLoginIdObservable(), getPasswordObservable(),
                (loginValid, passwordValid) -> loginValid && passwordValid)
                .subscribe(enabled -> mBtnSignInAccount.setEnabled(enabled && networkUtility.isNetworkAvailable()));
    }

    private Observable<Boolean> getLoginIdObservable() {
        return RxTextView.textChanges(loginValidationEditText)
                .map(email -> emailOrMobileValidator(email.toString()));
    }

    private Observable<Boolean> getPasswordObservable() {
        return RxTextView.textChanges(passwordValidationEditText)
                .map(password -> password.length() > 0);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_DLS_SigIn_TitleTxt;
    }

    private void signIn() {
        ((RegistrationFragment) getParentFragment()).hideKeyBoard();
        mRegError.hideError();
        mEtEmail.clearFocus();
        mEtPassword.clearFocus();
        if (mUser != null) {
            showSignInSpinner();

            if (FieldsValidator.isValidEmail(loginValidationEditText.getText().toString())) {
                mEmailOrMobile = loginValidationEditText.getText().toString();
            } else {
                mEmailOrMobile = FieldsValidator.getMobileNumber(loginValidationEditText.getText().toString());
            }

            mUser.loginUsingTraditional(mEmailOrMobile, passwordValidationEditText.getText().toString(), this);
        }
    }

    private void handleUiState() {
        if (networkUtility.isNetworkAvailable()) {
            mRegError.hideError();
            enableAll();
        } else {
            mRegError.setError(getString(R.string.reg_NoNetworkConnection));
            scrollViewAutomatically(mRegError, mSvRootLayout);
        }
    }

    @Override
    public void onLoginSuccess() {
        handleLoginSuccess();
    }

    private void completeRegistration() {
        getRegistrationFragment().userRegistrationComplete();
    }

    @Override
    public void onLoginFailedWithError(final UserRegistrationFailureInfo userRegistrationFailureInfo) {
        EventBus.getDefault().post(new LoginFailureNotification());
        handleLogInFailed(userRegistrationFailureInfo);
    }

    private void handleLogInFailed(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.d(RLog.CALLBACK, "SignInAccountFragment : onLoginFailedWithError");
        hideSignInSpinner();
        mBtnSignInAccount.hideProgressIndicator();
        enableAll();
        if (userRegistrationFailureInfo.getErrorCode() == -1 || userRegistrationFailureInfo.getErrorCode() == BAD_RESPONSE_CODE
                || userRegistrationFailureInfo.getErrorCode() == UN_EXPECTED_ERROR || userRegistrationFailureInfo.getErrorCode() == INPUTS_INVALID_CODE) {
            mRegError.setError(mContext.getResources().getString(R.string.reg_JanRain_Server_Connection_Failed));
        } else {
            if (userRegistrationFailureInfo.getErrorCode() >= RegConstants.HSDP_LOWER_ERROR_BOUND) {
                scrollViewAutomatically(mRegError, mSvRootLayout);
                mRegError.setError(mContext.getResources().getString(R.string.reg_Generic_Network_Error));
                scrollViewAutomatically(mRegError, mSvRootLayout);
            } else {
                scrollViewAutomatically(mRegError, mSvRootLayout);
                if (userRegistrationFailureInfo.getErrorCode() == RegConstants.INVALID_CREDENTIALS_ERROR_CODE) {
                    mRegError.setError(mContext.getResources().getString(R.string.reg_JanRain_Invalid_Credentials));
                    trackInvalidCredentials();
                } else {
                    if (null != userRegistrationFailureInfo.getErrorDescription()) {
                        mRegError.setError(userRegistrationFailureInfo.getErrorDescription());
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
        RLog.d(RLog.CALLBACK, "SignInAccountFragment : onSendForgotPasswordSuccess");
        trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.STATUS_NOTIFICATION,
                AppTagingConstants.RESET_PASSWORD_SUCCESS);
        hideForgotPasswordSpinner();
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getContext())
                .setDialogType(DialogConstants.TYPE_DIALOG)
                .setDialogLayout(R.layout.forgot_password_dialog)
                .setPositiveButton(mContext.getResources().getString(R.string.reg_DLS_Forgot_Password_Alert_Button_Title), this)
                .setTitle(mContext.getResources().getString(R.string.reg_ForgotPwdEmailResendMsg_Title))
                .setCancelable(false);
        alertDialogFragment = builder.create();
        alertDialogFragment.show(getFragmentManager(), ALERT_DIALOG_TAG);
    }

    @Override
    public void onSendForgotPasswordFailedWithError(final
                                                    UserRegistrationFailureInfo userRegistrationFailureInfo) {
        handleSendForgetPasswordFailure(userRegistrationFailureInfo);
    }

    private void handleSendForgetPasswordFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.d(RLog.CALLBACK, "SignInAccountFragment : onSendForgotPasswordFailedWithError ERROR CODE :" + userRegistrationFailureInfo.getErrorCode());
        hideForgotPasswordSpinner();

        if (userRegistrationFailureInfo.getErrorCode() == SOCIAL_SIGIN_IN_ONLY_CODE) {
            mEtEmail.setErrorMessage(getString(R.string.reg_TraditionalSignIn_ForgotPwdSocialError_lbltxt));
            trackActionStatus(AppTagingConstants.SEND_DATA,
                    AppTagingConstants.USER_ERROR, AppTagingConstants.ALREADY_SIGN_IN_SOCIAL);
            userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.REG_TRADITIONAL_SIGN_IN_FORGOT_PWD_SOCIAL_ERROR);
            AppTaggingErrors.trackActionForgotPasswordFailure(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
            return;
        } else {
            if (userRegistrationFailureInfo.getErrorCode() == -1) {
                mRegError.setError(mContext.getResources().getString(R.string.reg_JanRain_Server_Connection_Failed));
                userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.REG_JAN_RAIN_SERVER_CONNECTION_FAILED);
            }
        }

        if (null != userRegistrationFailureInfo.getErrorDescription()) {
            mEtEmail.setErrorMessage(userRegistrationFailureInfo.getErrorDescription());
            mEtEmail.showError();
            AppTaggingErrors.trackActionForgotPasswordFailure(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
            return;
        }
        AppTaggingErrors.trackActionForgotPasswordFailure(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
        enableAll();
    }

    private void showSignInSpinner() {
        mEtEmail.setClickable(false);
        mEtPassword.setClickable(false);
    }

    private void hideSignInSpinner() {
        mEtEmail.setClickable(true);
        mEtPassword.setClickable(true);
    }

    private void showForgotPasswordSpinner() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideForgotPasswordSpinner() {
        progressBar.setVisibility(View.GONE);
    }

    private void resetPassword() {
        if (networkUtility.isNetworkAvailable()) {
            if (mUser == null) {
                return;
            }
            showForgotPasswordSpinner();
            mEtEmail.clearFocus();
            mEtPassword.clearFocus();
            mBtnSignInAccount.setEnabled(false);
            if (FieldsValidator.isValidEmail(loginValidationEditText.getText().toString())) {
                mUser.forgotPassword(loginValidationEditText.getText().toString(), this);
            } else {
                serviceDiscovery();
            }
        }
    }

    @Override
    public void onUpdate() {
        handleUiState();
    }

    @Override
    public void onEventReceived(String event) {
        RLog.d(RLog.EVENT_LISTENERS, "SignInAccountFragment :onCounterEventReceived is : " + event);
        if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
            handleUiState();
        }
    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        RLog.d(RLog.NETWORK_STATE, "SignInAccountFragment : onNetWorkStateReceived state :" + isOnline);
        handleUiState();
        handleUiState();
        mBtnSignInAccount.setEnabled(isOnline);
    }


    @Override
    public void onResume() {
        mode = getActivity().getWindow().getAttributes().softInputMode;
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onResume();
    }


    @Override
    public void onResendVerificationEmailSuccess() {
        handleResendVerificationEmailSuccess();
    }

    private void handleResendVerificationEmailSuccess() {
        trackMultipleActionResendEmailStatus();
        RegAlertDialog.showResetPasswordDialog(mContext.getResources().getString(R.string.reg_Verification_email_Title),
                mContext.getResources().getString(R.string.reg_Verification_email_Message), getRegistrationFragment().getParentActivity(), mContinueVerifyBtnClick);
    }

    private void trackMultipleActionResendEmailStatus() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.SUCCESS_RESEND_EMAIL_VERIFICATION);
        map.put(AppTagingConstants.STATUS_NOTIFICATION, AppTagingConstants.RESEND_VERIFICATION_MAIL_LINK_SENT);
        trackMultipleActionsMap(AppTagingConstants.SEND_DATA, map);
    }

    private void trackInvalidCredentials() {
        AppTagging.trackAction(AppTagingConstants.SEND_DATA,
                AppTagingConstants.USER_ERROR,
                AppTagingConstants.INVALID_CREDENTIALS);
    }

    @Override
    public void onResendVerificationEmailFailedWithError(final UserRegistrationFailureInfo userRegistrationFailureInfo) {
        handleResendVerificationEmailFailed(userRegistrationFailureInfo);

    }

    private void handleResendVerificationEmailFailed(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.d(RLog.CALLBACK,
                "SignInAccountFragment : onResendVerificationEmailFailedWithError");
        AppTaggingErrors.trackActionResendNetworkFailure(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
        mRegError.setError(userRegistrationFailureInfo.getErrorDescription() + "\n"
                + userRegistrationFailureInfo.getErrorDescription());
    }

    private void handleLoginSuccess() {
        hideSignInSpinner();
        mRegError.hideError();
        mBtnSignInAccount.hideProgressIndicator();
        enableAll();

        boolean isEmailAvailable = mUser.getEmail() != null && FieldsValidator.isValidEmail(mUser.getEmail());
        boolean isMobileNoAvailable = mUser.getMobile() != null && FieldsValidator.isValidMobileNumber(mUser.getMobile());
        RLog.d(RLog.CALLBACK, "SignInAccountFragment : family name" + mUser.getFamilyName());

        if (isEmailAvailable && isMobileNoAvailable && !mUser.isEmailVerified()) {
            launchAccountActivationFragment();
            return;
        }

        if ((mUser.isEmailVerified() || mUser.isMobileVerified()) || !RegistrationConfiguration.getInstance().isEmailVerificationRequired()) {
            if (RegPreferenceUtility.getPreferenceValue(mContext, RegConstants.TERMS_N_CONDITIONS_ACCEPTED, mEmailOrMobile) && mUser.getReceiveMarketingEmail()) {
                completeRegistration();
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                        AppTagingConstants.SUCCESS_LOGIN);
            } else {
                if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired() || !mUser.getReceiveMarketingEmail()) {
                    launchAlmostDoneScreenForTermsAcceptance();
                } else {
                    trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                            AppTagingConstants.SUCCESS_LOGIN);
                    completeRegistration();
                }
            }
        } else {
            if (FieldsValidator.isValidEmail(loginValidationEditText.getText().toString())) {
                AccountActivationFragment fragment = new AccountActivationFragment();
                getRegistrationFragment().addFragment(fragment);

            } else if (FieldsValidator.isValidMobileNumber(loginValidationEditText.getText().toString())) {
                MobileVerifyCodeFragment fragment = new MobileVerifyCodeFragment();
                getRegistrationFragment().addFragment(fragment);
            } else {
                RLog.d(TAG, "SignInAccountFragment : invalid value");
                mRegError.setError(mContext.getResources().getString(R.string.reg_Generic_Network_Error));
                scrollViewAutomatically(mRegError, mSvRootLayout);
            }
        }
    }

    private OnClickListener mContinueVerifyBtnClick = view -> {
        RegAlertDialog.dismissDialog();
        updateActivationUIState();
    };

    private void updateActivationUIState() {
        launchAccountActivationFragment();
    }

    private void launchAlmostDoneScreenForTermsAcceptance() {
        getRegistrationFragment().addAlmostDoneFragmentforTermsAcceptance();
        trackPage(AppTaggingPages.ALMOST_DONE);
    }

    private void handleResend() {
        mBtnSignInAccount.setEnabled(false);
        if (registrationSettingsURL.isMobileFlow()) {
            serviceDiscovery();
        }
    }

    private String getClientId() {
        ClientIDConfiguration clientIDConfiguration = new ClientIDConfiguration();
        return clientIDConfiguration.getResetPasswordClientId(RegConstants.HTTPS_CONST + Jump.getCaptureDomain());
    }

    private void createResendSMSIntent(String url) {
        RLog.d(TAG, "MOBILE NUMBER *** : " + loginValidationEditText.getText().toString());
        RLog.d(TAG, " envir :" + RegistrationConfiguration.getInstance().getRegistrationEnvironment());

        String bodyContent = "provider=JANRAIN-CN&phonenumber=" + FieldsValidator.getMobileNumber(loginValidationEditText.getText().toString()) +
                "&locale=zh_CN&clientId=" + getClientId() + "&code_type=short&" +
                "redirectUri=" + getRedirectUri();
        RLog.d(TAG, " envir :" + getClientId() + getRedirectUri());
        URRequest urRequest = new URRequest(url, bodyContent, null, this::handleResendSMSRespone, error -> {
            hideForgotPasswordSpinner();
            mEtEmail.setErrorMessage(mContext.getResources().getString(R.string.reg_Invalid_PhoneNumber_ErrorMsg));
            mEtEmail.showError();
        });
        urRequest.makeRequest(true);
    }

    private void serviceDiscovery() {
        RLog.d(TAG, " Country :" + RegistrationHelper.getInstance().getCountryCode());

        serviceDiscoveryInterface.getServiceUrlWithCountryPreference("userreg.urx.verificationsmscode", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                hideForgotPasswordSpinner();
                RLog.d(TAG, " onError  : userreg.urx.verificationsmscode : " + errorvalues);
                verificationSmsCodeURL = null;
                mEtEmail.setErrorMessage(getResources().getString(R.string.reg_Generic_Network_Error));
            }

            @Override
            public void onSuccess(URL url) {
                RLog.d(TAG, " onSuccess  : userreg.urx.verificationsmscode:" + url.toString());
                String uriSubString = getBaseString(url.toString());
                verificationSmsCodeURL = uriSubString + USER_REQUEST_PW_RESET_SMS_CODE;
                resetPasswordSmsRedirectUri = uriSubString + USER_REQUEST_RESET_PW_REDIRECT_URI_SMS;
                createResendSMSIntent(verificationSmsCodeURL);
            }
        });
    }

    private void enableAll() {
        if (networkUtility.isNetworkAvailable()) {
            mBtnSignInAccount.setEnabled(true);
        } else {
            mRegError.setError(getString(R.string.reg_NoNetworkConnection));
        }
        loginValidationEditText.setEnabled(true);
        passwordValidationEditText.setEnabled(true);
        resetPasswordLabel.setEnabled(true);
    }

    private void disableAll() {
        loginValidationEditText.setEnabled(false);
        passwordValidationEditText.setEnabled(false);
        resetPasswordLabel.setEnabled(false);
    }

    @NonNull
    private String getBaseString(String respnseUrl) {
        URL url = null;

        try {
            url = new URL(respnseUrl);
        } catch (MalformedURLException e) {
            RLog.d(TAG, "Exception = " + e.getMessage());
        }
        return (url.getProtocol() + "://" + url.getHost());
    }

    private void handleResendSMSRespone(String response) {
        hideForgotPasswordSpinner();
        final String mobileNumberKey = "mobileNumber";
        final String tokenKey = "token";
        final String redirectUriKey = "redirectUri";
        final String verificationSmsCodeURLKey = "verificationSmsCodeURL";

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("errorCode").equals("0")) {
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
                RLog.d(TAG, " isAccountActivate is " + token + " -- " + response);
                MobileForgotPassVerifyCodeFragment mobileForgotPasswordVerifyCodeFragment = new MobileForgotPassVerifyCodeFragment();
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
                mEtEmail.showError();
                RLog.d(TAG, " SMS Resend failure = " + response);
                return;
            }

        } catch (Exception e) {
            RLog.d(TAG, " Exception  = " + e.getMessage());
        }
    }

    private void handleResendVerificationSMSSuccess() {
        trackActionStatus(AppTagingConstants.SEND_DATA,
                AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.SUCCESS_RESEND_EMAIL_VERIFICATION);
    }

    private String getRedirectUri() {
        return resetPasswordSmsRedirectUri;
    }
}