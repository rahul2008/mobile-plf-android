
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional.mobile;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.philips.cdp.registration.B;
import com.philips.cdp.registration.HttpClientService;
import com.philips.cdp.registration.HttpClientServiceReceiver;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.ui.customviews.OnUpdateListener;
import com.philips.cdp.registration.ui.customviews.XMobileHavingProblems;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.customviews.XVerifyNumber;
import com.philips.cdp.registration.ui.traditional.RegistrationBaseFragment;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegAlertDialog;
import com.philips.cdp.registration.ui.utils.RegChinaUtil;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterfork.Bind;
import butterfork.ButterFork;
import butterfork.OnClick;

import static android.view.View.GONE;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.ACTIVATION_NOT_VERIFIED;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.MOBILE_INAPPNATIFICATION;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.MOBILE_RESEND_EMAIL_VERFICATION;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.MOBILE_RESEND_SMS_VERFICATION;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.MOBILE_RESEND_SMS_VERFICATION_FAILURE;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.REGISTRATION_ACTIVATION_SMS;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.SEND_DATA;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.SPECIAL_EVENTS;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.SUCCESS_RESEND_EMAIL_VERIFICATION;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.SUCCESS_USER_REGISTRATION;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.TECHNICAL_ERROR;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.USER_ERROR;

public class MobileVerifyCodeFragment extends RegistrationBaseFragment implements MobileVerifyCodeContract, RefreshUserHandler, OnUpdateListener {

    @Inject
    ServiceDiscoveryInterface serviceDiscoveryInterface;

    @Bind(B.id.ll_reg_create_account_fields)
    LinearLayout phoneNumberEditTextContainer;

    @Bind(B.id.rl_reg_singin_options)
    RelativeLayout verifyButtonContainer;

    @Bind(B.id.btn_reg_Verify)
    Button verifyButton;

    @Bind(B.id.rl_reg_name_field)
    XVerifyNumber otpEditTextAndResendButton;

    @Bind(B.id.pb_reg_activate_spinner)
    ProgressBar spinnerProgress;

    @Bind(B.id.view_reg_verify_hint)
    XMobileHavingProblems havingProblems;

    @Bind(B.id.reg_error_msg)
    XRegError errorMessage;

    private Context context;

    private User user;

    private MobileVerifyCodePresenter mobileVerifyCodePresenter;

    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        URInterface.getComponent().inject(this);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onCreateView");
        trackActionStatus(REGISTRATION_ACTIVATION_SMS,"","");
        context = getRegistrationFragment().getActivity().getApplicationContext();
        mobileVerifyCodePresenter = new MobileVerifyCodePresenter(this);
        user = new User(context);
        View view = inflater.inflate(R.layout.reg_mobile_activatiom_fragment, container, false);
        ButterFork.bind(this, view);
        otpEditTextAndResendButton.setOnUpdateListener(this);
        mobileVerifyCodePresenter.startResendTimer();
        handleOrientation(view);
        handler = new Handler();
        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onConfigurationChanged");
        super.onConfigurationChanged(config);
        setCustomParams(config);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mobileVerifyCodePresenter.cleanUp();
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, phoneNumberEditTextContainer, width);
        applyParams(config, verifyButtonContainer, width);
        applyParams(config, havingProblems, width);
        applyParams(config, errorMessage, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_RegCreateAccount_NavTitle;
    }

    private void updateUiStatus() {
        if (otpEditTextAndResendButton.getNumber().length() >= RegConstants.VERIFY_CODE_MINIMUM_LENGTH) {
            enableVerifyButton();
        } else {
            disableVerifyButton();
        }
    }

    private void handleResendVerificationEmailSuccess() {
        trackActionStatus(SEND_DATA, SPECIAL_EVENTS, SUCCESS_RESEND_EMAIL_VERIFICATION);
        RegAlertDialog.showResetPasswordDialog(context.getResources().getString(R.string.reg_Resend_SMS_title),
                context.getResources().getString(R.string.reg_Resend_SMS_Success_Content), getRegistrationFragment().getParentActivity(), mContinueVerifyBtnClick);
    }

    public void handleUI() {
        handleOnUIThread(() -> updateUiStatus());
    }

    @Override
    public void onRefreshUserSuccess() {
        RLog.d(RLog.EVENT_LISTENERS, "MobileActivationFragment : onRefreshUserSuccess");
        hideSpinner();
        getRegistrationFragment().addFragment(new AddSecureEmailFragment());
    }

    @Override
    public void onRefreshUserFailed(int error) {
        hideSpinner();
        RLog.d(RLog.EVENT_LISTENERS, "MobileActivationFragment : onRefreshUserFailed");
    }

    private void hideSpinner() {
        spinnerProgress.setVisibility(GONE);
        enableVerifyButton();
    }

    @Override
    public void onUpdate() {
        handleUI();
    }

    private View.OnClickListener mContinueVerifyBtnClick = view -> RegAlertDialog.dismissDialog();
  
    private void trackMultipleActionsOnMobileSuccess() {
        Map<String, String> map = new HashMap<>();
        map.put(SPECIAL_EVENTS, MOBILE_RESEND_EMAIL_VERFICATION);
        map.put(MOBILE_INAPPNATIFICATION, MOBILE_RESEND_SMS_VERFICATION);
        AppTagging.trackMultipleActions(SEND_DATA, map);
    }

    @OnClick(B.id.btn_reg_Verify)
    public void verifyClicked() {
        spinnerProgress.setVisibility(View.VISIBLE);
        disableVerifyButton();
        otpEditTextAndResendButton.disableResendSpinner();
        mobileVerifyCodePresenter.verifyMobileNumber(user.getJanrainUUID(), otpEditTextAndResendButton.getNumber());
    }

    @OnClick(B.id.rl_reg_name_field)
    public void resendButtonClicked() {
        otpEditTextAndResendButton.showResendSpinnerAndDisableResendButton();
        disableVerifyButton();
        spinnerProgress.setVisibility(GONE);
        otpEditTextAndResendButton.showValidEmailAlert();
        mobileVerifyCodePresenter.resendOTPRequest(user.getMobile());
    }

    @Override
    public Intent getServiceIntent() {
        return new Intent(context, HttpClientService.class);
    }

    @Override
    public HttpClientServiceReceiver getClientServiceRecevier() {
        return new HttpClientServiceReceiver(handler);
    }

    @Override
    public ComponentName startService(Intent intent) {
        return context.startService(intent);
    }

    @Override
    public void enableResendButton() {
        otpEditTextAndResendButton.hideResendSpinnerAndEnableResendButton();
        otpEditTextAndResendButton.setCounterFinish();
    }

    @Override
    public void updateResendTimer(String timeRemaining) {
        otpEditTextAndResendButton.setCountertimer(timeRemaining);
    }

    @Override
    public void enableVerifyButton() {
        verifyButton.setEnabled(true);
    }

    @Override
    public void hideErrorMessage() {
        errorMessage.hideError();
    }

    @Override
    public void disableVerifyButton() {
        verifyButton.setEnabled(false);
    }

    @Override
    public void showNoNetworkErrorMessage() {
        errorMessage.setError(context.getResources().getString(R.string.reg_NoNetworkConnection));
    }

    @Override
    public void showSmsSendFailedError() {
        otpEditTextAndResendButton.hideResendSpinnerAndEnableResendButton();
        otpEditTextAndResendButton.showEmailIsInvalidAlert();
        otpEditTextAndResendButton.setErrDescription(getString(R.string.reg_URX_SMS_InternalServerError));
    }

    @Override
    public void refreshUserOnSmsVerificationSuccess() {
        trackActionStatus(SEND_DATA, SPECIAL_EVENTS, SUCCESS_USER_REGISTRATION);
        user.refreshUser(this);
    }

    @Override
    public void smsVerificationResponseError() {
        otpEditTextAndResendButton.setErrDescription(getString(R.string.reg_Mobile_Verification_Invalid_Code));
    }

    @Override
    public void hideProgressSpinner() {
        spinnerProgress.setVisibility(GONE);
    }

    @Override
    public void setOtpInvalidErrorMessage() {
        trackActionStatus(SEND_DATA, USER_ERROR, ACTIVATION_NOT_VERIFIED);
        otpEditTextAndResendButton.setErrDescription(getString(R.string.reg_Mobile_Verification_Invalid_Code));
    }

    @Override
    public void setOtpErrorMessageFromJson(String errorDescription) {
        trackActionStatus(SEND_DATA, USER_ERROR, ACTIVATION_NOT_VERIFIED);
        otpEditTextAndResendButton.setErrDescription(errorDescription);
    }

    @Override
    public void showOtpInvalidError() {
        otpEditTextAndResendButton.showEmailIsInvalidAlert();
    }

    @Override
    public void enableResendButtonAndHideSpinner() {
        otpEditTextAndResendButton.setEnabled(true);
        trackMultipleActionsOnMobileSuccess();
        otpEditTextAndResendButton.hideResendSpinnerAndEnableResendButton();
        handleResendVerificationEmailSuccess();
    }

    @Override
    public void showSmsResendTechincalError(String errorCodeString) {
        trackActionStatus(SEND_DATA, TECHNICAL_ERROR, MOBILE_RESEND_SMS_VERFICATION_FAILURE);
        String errorMsg = RegChinaUtil.getErrorMsgDescription(errorCodeString, context);
        otpEditTextAndResendButton.hideResendSpinnerAndEnableResendButton();
        otpEditTextAndResendButton.showEmailIsInvalidAlert();
        otpEditTextAndResendButton.setErrDescription(errorMsg);
    }
}
