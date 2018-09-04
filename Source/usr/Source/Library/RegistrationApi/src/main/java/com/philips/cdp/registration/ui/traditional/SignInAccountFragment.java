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
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
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
import com.philips.cdp.registration.errors.ErrorCodes;
import com.philips.cdp.registration.errors.ErrorType;
import com.philips.cdp.registration.errors.URError;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.events.EventListener;
import com.philips.cdp.registration.events.NetworkStateListener;
import com.philips.cdp.registration.handlers.ForgotPasswordHandler;
import com.philips.cdp.registration.handlers.LoginHandler;
import com.philips.cdp.registration.handlers.ResendVerificationEmailHandler;
import com.philips.cdp.registration.restclient.URRequest;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.RegistrationSettingsURL;
import com.philips.cdp.registration.ui.customviews.OnUpdateListener;
import com.philips.cdp.registration.ui.customviews.URNotification;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.traditional.mobile.MobileForgotPassVerifyCodeFragment;
import com.philips.cdp.registration.ui.traditional.mobile.MobileVerifyCodeFragment;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.LoginFailureNotification;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegAlertDialog;
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
        LoginHandler, ForgotPasswordHandler, OnUpdateListener,
        EventListener, ResendVerificationEmailHandler,
        NetworkStateListener {

    private static final String TAG = "SignInAccountFragment";
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
        RLog.i(TAG, "Screen name is " + TAG);


        registerInlineNotificationListener(this);
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
    public void onStop() {
        super.onStop();
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
        EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_SUCCESS,
                this);
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
            RLog.i(TAG, TAG + ".login button clicked");
            hideValidations();
            uiEnableState(false);
            mBtnSignInAccount.showProgressIndicator();
            signIn();
        }

        if (alertDialogFragment != null) {
            //TODO: optimize the alert dialog
            RLog.d(TAG, "onClick :Dismissing Alert Dialog");
            uiEnableState(true);
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
        mRegError = view.findViewById(R.id.usr_loginScreen_error_view);
        linkifyPrivacyPolicy(resetPasswordLabel, forgotPasswordClickListener);
        handleUiState();

        if (RegistrationHelper.getInstance().isMobileFlow()) {
            usr_loginScreen_email_label.setText(R.string.USR_DLS_Email_Phone_Label_Text);
        }
        mUser = new User(mContext);
        registrationSettingsURL = new RegistrationSettingsURL();

        handlePasswordDoubleClick();

    }

    private void handlePasswordDoubleClick() {
        passwordValidationEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
            }
        });
    }

    private boolean emailOrMobileValidator(String emailOrMobile) {
        if (emailOrMobile.isEmpty() && !RegistrationHelper.getInstance().isMobileFlow()) {
            mEtEmail.setErrorMessage(R.string.USR_NameField_ErrorText);
        } else {
            mEtEmail.setErrorMessage(R.string.USR_InvalidEmailOrPhoneNumber_ErrorMsg);
        }

        if (RegistrationHelper.getInstance().isMobileFlow()) {
            if ((!FieldsValidator.isValidMobileNumber(emailOrMobile) || !FieldsValidator.isValidEmail(emailOrMobile))) {
                RLog.d(TAG, "Valid Mobile No./ Email"
                        + (FieldsValidator.isValidMobileNumber(emailOrMobile) || FieldsValidator.isValidEmail(emailOrMobile)));
                mEtEmail.setErrorMessage(R.string.USR_InvalidEmailOrPhoneNumber_ErrorMsg);
                return FieldsValidator.isValidMobileNumber(emailOrMobile) || FieldsValidator.isValidEmail(emailOrMobile);
            }
        } else {
            RLog.d(TAG, "Valid Email ID" + FieldsValidator.isValidEmail(emailOrMobile));
            mEtEmail.setErrorMessage(R.string.USR_InvalidOrMissingEmail_ErrorMsg);
            return FieldsValidator.isValidEmail(emailOrMobile);
        }
        return false;
    }

    private ClickableSpan forgotPasswordClickListener = new ClickableSpan() {

        @Override
        public void onClick(View widget) {
            mRegError.hideError();
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
        mRegError.hideError();
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
        return R.string.USR_DLS_SigIn_TitleTxt;
    }

    private void signIn() {
        ((RegistrationFragment) getParentFragment()).hideKeyBoard();
        mRegError.hideError();

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
            uiEnableState(true);
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
        hideSignInSpinner();
        mBtnSignInAccount.hideProgressIndicator();
        uiEnableState(true);
        RLog.e(TAG, "handleLogInFailed : Error Code :" + userRegistrationFailureInfo.getErrorCode());
        if (userRegistrationFailureInfo.getErrorCode() == RegConstants.INVALID_CREDENTIALS_ERROR_CODE) {
            updateErrorNotification(mContext.getApplicationContext().getString(R.string.USR_Janrain_Invalid_Credentials), userRegistrationFailureInfo.getErrorCode());
        } else {
            updateErrorNotification(userRegistrationFailureInfo.getErrorDescription(), userRegistrationFailureInfo.getErrorCode());
        }

        // trackInvalidCredentials();
    }

    @Override
    public void onSendForgotPasswordSuccess() {
        if (isVisible()) {
            handleSendForgotSuccess();
        }
    }

    private void handleSendForgotSuccess() {
        RLog.i(TAG, " handleSendForgotSuccess");
        trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.STATUS_NOTIFICATION,
                AppTagingConstants.RESET_PASSWORD_SUCCESS);
        hideForgotPasswordSpinner();
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getContext())
                .setDialogType(DialogConstants.TYPE_DIALOG)
                .setDialogLayout(R.layout.forgot_password_dialog)
                .setPositiveButton(mContext.getResources().getString(R.string.USR_DLS_Forgot_Password_Alert_Button_Title), new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialogFragment.dismiss();
                        RLog.d(TAG, "onClick :dismiss ");
                        uiEnableState(true);
                        if (networkUtility.isNetworkAvailable()) {
                            observeLoginButton();
                        }
                    }
                })
                .setTitle(mContext.getResources().getString(R.string.USR_DLS_Forgot_Password_Alert_Title))
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
        RLog.d(TAG, "onSendForgotPasswordFailedWithError ERROR CODE :" + userRegistrationFailureInfo.getErrorCode());
        hideForgotPasswordSpinner();

        if (userRegistrationFailureInfo.getErrorCode() == ErrorCodes.SOCIAL_SIGIN_IN_ONLY_CODE) {
            if (RegistrationHelper.getInstance().isMobileFlow())
                mEtEmail.setErrorMessage(getString(R.string.USR_DLS_Forgot_Password_Body_With_Phone_No));
            else
                mEtEmail.setErrorMessage(getString(R.string.USR_DLS_Forgot_Password_Body_Without_Phone_No));
            trackActionStatus(AppTagingConstants.SEND_DATA,
                    AppTagingConstants.USER_ERROR, AppTagingConstants.ALREADY_SIGN_IN_SOCIAL);
            userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.REG_TRADITIONAL_SIGN_IN_FORGOT_PWD_SOCIAL_ERROR);
            AppTaggingErrors.trackActionForgotPasswordFailure(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
            uiEnableState(true);
            return;
        }

        if (null != userRegistrationFailureInfo.getErrorDescription()) {
            updateErrorNotification(userRegistrationFailureInfo.getErrorDescription(), userRegistrationFailureInfo.getErrorCode());
            AppTaggingErrors.trackActionForgotPasswordFailure(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
            uiEnableState(true);
            return;
        }
        AppTaggingErrors.trackActionForgotPasswordFailure(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
        uiEnableState(true);
    }

    private void showSignInSpinner() {
        RLog.d(TAG, "showSignInSpinner");
        mEtEmail.setClickable(false);
        mEtPassword.setClickable(false);
    }

    private void hideSignInSpinner() {
        RLog.d(TAG, "hideSignInSpinner");
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
        RLog.d(TAG, "resetPassword");
        if (networkUtility.isNetworkAvailable()) {
            if (mUser == null) {
                return;
            }
            showForgotPasswordSpinner();
            uiEnableState(false);
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
        RLog.d(TAG, " onCounterEventReceived is : " + event);
        if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
            handleUiState();
        }
    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        RLog.d(TAG, " onNetWorkStateReceived state :" + isOnline);
        handleUiState();
        uiEnableState(isOnline);
        observeLoginButton();
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
        RLog.i(TAG, "handleResendVerificationEmailSuccess");
        trackMultipleActionResendEmailStatus();
        RegAlertDialog.showResetPasswordDialog(mContext.getResources().getString(R.string.USR_DLS_Resend_Email_NotificationBar_Title),
                mContext.getResources().getString(R.string.USR_DLS_Resend_Email_Body_Line1), getRegistrationFragment().getParentActivity(), mContinueVerifyBtnClick);
    }

    private void trackMultipleActionResendEmailStatus() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.SUCCESS_RESEND_EMAIL_VERIFICATION);
        map.put(AppTagingConstants.STATUS_NOTIFICATION, AppTagingConstants.RESEND_VERIFICATION_MAIL_LINK_SENT);
        trackMultipleActionsMap(AppTagingConstants.SEND_DATA, map);
    }

//    private void trackInvalidCredentials() {
//        AppTagging.trackAction(AppTagingConstants.SEND_DATA,
//                AppTagingConstants.USER_ERROR,
//                AppTagingConstants.INVALID_CREDENTIALS);
//    }

    @Override
    public void onResendVerificationEmailFailedWithError(final UserRegistrationFailureInfo userRegistrationFailureInfo) {
        handleResendVerificationEmailFailed(userRegistrationFailureInfo);

    }

    private void handleResendVerificationEmailFailed(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.e(TAG, "handleResendVerificationEmailFailed : Error Code =" + userRegistrationFailureInfo.getErrorCode());
        AppTaggingErrors.trackActionResendNetworkFailure(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
        updateErrorNotification(userRegistrationFailureInfo.getErrorDescription(), userRegistrationFailureInfo.getErrorCode());
    }

    private void handleLoginSuccess() {
        hideSignInSpinner();
        mRegError.hideError();
        mBtnSignInAccount.hideProgressIndicator();
        uiEnableState(true);

        boolean isEmailAvailable = mUser.getEmail() != null && FieldsValidator.isValidEmail(mUser.getEmail());
        boolean isMobileNoAvailable = mUser.getMobile() != null && FieldsValidator.isValidMobileNumber(mUser.getMobile());
        RLog.d(TAG, "handleLoginSuccess: family name" + mUser.getFamilyName());

        if (isEmailAvailable && isMobileNoAvailable && !mUser.isEmailVerified()) {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mContext);
            userRegistrationFailureInfo.setErrorCode(AppTagingConstants.EMAIL_NOT_VERIFIED_CODE);
            AppTaggingErrors.trackActionLoginError(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
            launchAccountActivationFragment();
            return;
        }

        if ((mUser.isEmailVerified() || mUser.isMobileVerified()) || !RegistrationConfiguration.getInstance().isEmailVerificationRequired()) {
            if (RegPreferenceUtility.getPreferenceValue(mContext, RegConstants.TERMS_N_CONDITIONS_ACCEPTED, mEmailOrMobile) && mUser.getReceiveMarketingEmail()) {
                RLog.d(TAG, "handleLoginSuccess : TERMS_N_CONDITIONS_ACCEPTED :getReceiveMarketingEmail : completeRegistration");
                completeRegistration();
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                        AppTagingConstants.SUCCESS_LOGIN);
                AppTagging.trackAction(AppTagingConstants.SEND_DATA, AppTagingConstants.KEY_COUNTRY_SELECTED,
                        RegistrationHelper.getInstance().getCountryCode());
            } else {
                if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired() || !mUser.getReceiveMarketingEmail()) {
                    launchAlmostDoneScreenForTermsAcceptance();
                } else {
                    trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                            AppTagingConstants.SUCCESS_LOGIN);
                    AppTagging.trackAction(AppTagingConstants.SEND_DATA, AppTagingConstants.KEY_COUNTRY_SELECTED,
                            RegistrationHelper.getInstance().getCountryCode());
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
                RLog.d(TAG, " invalid value");
//                updateErrorNotification(mContext.getResources().getString(R.string.reg_Generic_Network_Error));
//                mRegError.setError(mContext.getResources().getString(R.string.reg_Generic_Network_Error));
//                scrollViewAutomatically(mRegError, mSvRootLayout);

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
        uiEnableState(false);
        if (registrationSettingsURL.isMobileFlow()) {
            serviceDiscovery();
        }
    }

    private String getClientId() {
        ClientIDConfiguration clientIDConfiguration = new ClientIDConfiguration();
        return clientIDConfiguration.getResetPasswordClientId(RegConstants.HTTPS_CONST + Jump.getCaptureDomain());
    }

    private void createResendSMSIntent(String url) {
        RLog.d(TAG, "MOBILE NUMBER : " + loginValidationEditText.getText().toString());
        RLog.d(TAG, "RegistrationEnvironment :" + RegistrationConfiguration.getInstance().getRegistrationEnvironment());

        String bodyContent = "provider=JANRAIN-CN&phonenumber=" + FieldsValidator.getMobileNumber(loginValidationEditText.getText().toString()) +
                "&locale=zh_CN&clientId=" + getClientId() + "&code_type=short&" +
                "redirectUri=" + getRedirectUri();
        RLog.d(TAG, "createResendSMSIntent: envir :" + getClientId() + getRedirectUri());
        RLog.i(TAG, "createResendSMSIntent: url :" + url);
        RLog.d(TAG, "createResendSMSIntent: bodyContent :" + bodyContent);


        URRequest urRequest = new URRequest(url, bodyContent, null, this::handleResendSMSRespone, this::onErrorOfResendSMSIntent);
        urRequest.makeRequest(true);
    }

    private void onErrorOfResendSMSIntent(VolleyError error) {
        hideForgotPasswordSpinner();
        try {
            final String message = error.getMessage();
            if (message == null) {
                mEtEmail.setErrorMessage(new URError(mContext).getLocalizedError(ErrorType.URX, RegConstants.UNKNOWN_ERROR_ID));
                mEtEmail.showError();
                return;
            }
            JSONObject jsonObject = new JSONObject(message);
            final String errorCode = jsonObject.getString("errorCode");

            RLog.e(TAG, "onErrorOfResendSMSIntent : Error from Request " + error.getMessage());
            final Integer code = Integer.parseInt(errorCode);
            if (URNotification.INLINE_ERROR_CODE.contains(code)) {
                mEtEmail.setErrorMessage(new URError(mContext).getLocalizedError(ErrorType.URX, code));
                mEtEmail.showError();
            } else {
                updateErrorNotification(new URError(mContext).getLocalizedError(ErrorType.URX, code));
            }
        } catch (JSONException e) {
            RLog.e(TAG, "onErrorOfResendSMSIntent : Exception Occurred" + e.getMessage());
        }
    }

    private void serviceDiscovery() {
        RLog.d(TAG, " Country :" + RegistrationHelper.getInstance().getCountryCode());
        //TODO: optimize the class as the service discovery is not there every time
        serviceDiscoveryInterface.getServiceUrlWithCountryPreference("userreg.urx.verificationsmscode", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                hideForgotPasswordSpinner();
                RLog.e(TAG, " onError serviceDiscovery : userreg.urx.verificationsmscode : " + errorvalues);
                verificationSmsCodeURL = null;
                mEtEmail.setErrorMessage(new URError(mContext).getLocalizedError(ErrorType.NETWOK, ErrorCodes.NETWORK_ERROR));
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

    private void uiEnableState(boolean state) {
        if (networkUtility.isNetworkAvailable()) {
            mBtnSignInAccount.setEnabled(state);
            resetPasswordLabel.setEnabled(state);
        } else {
            mBtnSignInAccount.setEnabled(false);
            resetPasswordLabel.setEnabled(false);
        }
        loginValidationEditText.setEnabled(state);
        passwordValidationEditText.setEnabled(state);
    }

    @NonNull
    private String getBaseString(String respnseUrl) {
        URL url;

        try {
            url = new URL(respnseUrl);
            if (url != null) {
                return (url.getProtocol() + "://" + url.getHost());
            }
        } catch (MalformedURLException e) {
            RLog.d(TAG, "Exception = " + e.getMessage());
        }
        return "";
    }

    private void handleResendSMSRespone(String response) {
        hideForgotPasswordSpinner();
        final String mobileNumberKey = "mobileNumber";
        final String tokenKey = "token";
        final String redirectUriKey = "redirectUri";
        final String verificationSmsCodeURLKey = "verificationSmsCodeURL";
        mEtEmail.hideError();
        try {
            JSONObject jsonObject = new JSONObject(response);
            final String errorCode = jsonObject.getString("errorCode");
            if (errorCode.equals("0")) {
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
                mEtEmail.setErrorMessage(new URError(mContext).getLocalizedError(ErrorType.URX, Integer.parseInt(errorCode)));
                mEtEmail.showError();
                RLog.e(TAG, "handleResendSMSRespone :  SMS Resend failure with Error Response = " + response + " Error Code = " + errorCode);
            }

        } catch (Exception e) {
            RLog.e(TAG, "handleResendSMSRespone : Exception  = " + e.getMessage());
        }
    }

    private void handleResendVerificationSMSSuccess() {
        trackActionStatus(AppTagingConstants.SEND_DATA,
                AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.SUCCESS_RESEND_EMAIL_VERIFICATION);
    }

    private String getRedirectUri() {
        return resetPasswordSmsRedirectUri;
    }

    @Override
    public void notificationInlineMsg(String msg) {
        RLog.d(TAG, "notificationInlineMsg : " + msg);
        mRegError.setError(msg);
        uiEnableState(true);
        if (networkUtility.isNetworkAvailable()) {
            observeLoginButton();
        }
    }
}