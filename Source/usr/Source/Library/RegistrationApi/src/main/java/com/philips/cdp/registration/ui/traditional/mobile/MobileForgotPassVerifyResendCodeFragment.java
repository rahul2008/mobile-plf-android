
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional.mobile;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.android.volley.VolleyError;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.R2;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.errors.ErrorCodes;
import com.philips.cdp.registration.errors.ErrorType;
import com.philips.cdp.registration.errors.URError;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.ui.customviews.OnUpdateListener;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.traditional.RegistrationBaseFragment;
import com.philips.cdp.registration.ui.utils.CountDownEvent;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.NotificationBarHandler;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.UpdateMobile;
import com.philips.cdp.registration.ui.utils.UpdateToken;
import com.philips.platform.uid.view.widget.ProgressBarButton;
import com.philips.platform.uid.view.widget.ProgressBarWithLabel;
import com.philips.platform.uid.view.widget.ValidationEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.philips.cdp.registration.app.tagging.AppTagingConstants.MOBILE_INAPPNATIFICATION;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.MOBILE_RESEND_EMAIL_VERFICATION;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.MOBILE_RESEND_SMS_VERFICATION;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.MOBILE_RESEND_SMS_VERFICATION_FAILURE;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.REGISTRATION_ACTIVATION_SMS;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.SEND_DATA;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.SPECIAL_EVENTS;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.SUCCESS_RESEND_EMAIL_VERIFICATION;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.TECHNICAL_ERROR;

public class MobileForgotPassVerifyResendCodeFragment extends RegistrationBaseFragment implements
        MobileForgotPassVerifyResendCodeContract, RefreshUserHandler, OnUpdateListener {

    private String TAG = "MobileForgotPassVerifyResendCodeFragment";

    @BindView(R2.id.btn_reg_resend_update)
    ProgressBarButton resendSMSButton;

    @BindView(R2.id.btn_reg_code_received)
    Button smsReceivedButton;

    @BindView(R2.id.reg_error_msg)
    XRegError errorMessage;

    @BindView(R2.id.rl_reg_number_field)
    ValidationEditText phoneNumberEditText;

    @BindView(R2.id.usr_mobileverification_resendsmstimer_progress)
    ProgressBarWithLabel usrMobileverificationResendsmstimerProgress;

    @BindView(R2.id.ll_reg_root_container)
    LinearLayout fragmentRootLayout;

    private Context context;

    private MobileForgotPassVerifyResendCodePresenter mobileVerifyResendCodePresenter;

    private PopupWindow popupWindow;

    @Inject
    NetworkUtility networkUtility;

    private String verificationSmsCodeURL;

    private String mobileNumber;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        RegistrationConfiguration.getInstance().getComponent().inject(this);
        RLog.i(TAG, "Screen name is " + TAG);

        registerInlineNotificationListener(this);
        mobileVerifyResendCodePresenter = new MobileForgotPassVerifyResendCodePresenter(this);

        final String mobileNumberKey = "mobileNumber";
        final String redirectUriKey = "redirectUri";
        final String verificationSmsCodeURLKey = "verificationSmsCodeURL";
        Bundle bundle = getArguments();

        if (bundle != null) {
            mobileNumber = bundle.getString(mobileNumberKey);
            String redirectUri = bundle.getString(redirectUriKey);
            verificationSmsCodeURL = bundle.getString(verificationSmsCodeURLKey);
            mobileVerifyResendCodePresenter.setRedirectUri(redirectUri);
        }
        View view = inflater.inflate(R.layout.reg_mobile_forgot_password_resend_fragment, container, false);

        trackActionStatus(REGISTRATION_ACTIVATION_SMS, "", "");
        ButterKnife.bind(this, view);
        handleOrientation(view);
        phoneNumberEditText.setText(mobileNumber);
        phoneNumberEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        disableResendButton();
        if (!getRegistrationFragment().getCounterState()) {
            enableResendButton();
        }
        phoneNumberChange();
        return view;
    }

    private void phoneNumberChange() {
        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do not do anything
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (FieldsValidator.isValidMobileNumber(s.toString())) {
                    enableResendButton();
                } else {
                    disableResendButton();
                }

                errorMessage.hideError();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do not do anything
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        RLog.d(TAG, " onConfigurationChanged");
        super.onConfigurationChanged(config);
        setCustomParams(config);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mobileVerifyResendCodePresenter.cleanUp();
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        // Do not do anything
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.USR_Resend_SMS_title;
    }

    private void updateUiStatus() {
        if (FieldsValidator.isValidMobileNumber(phoneNumberEditText.getText().toString())) {
            resendSMSButton.setEnabled(true);
        } else {
            resendSMSButton.setEnabled(false);
        }
        phoneNumberEditText.setEnabled(true);
        smsReceivedButton.setEnabled(true);
    }

    private void handleResendVerificationEmailSuccess() {
        trackActionStatus(SEND_DATA, SPECIAL_EVENTS, SUCCESS_RESEND_EMAIL_VERIFICATION);
        viewOrHideNotificationBar();
        hideProgressDialog();
        getRegistrationFragment().startCountDownTimer();
        if (!mobileNumber.equals(phoneNumberEditText.getText().toString())) {
            EventBus.getDefault().post(new UpdateMobile(phoneNumberEditText.getText().toString()));
        }
    }


    @Subscribe
    public void onEvent(NotificationBarHandler event) {
        viewOrHideNotificationBar();
    }

    @Override
    public void onPause() {
        super.onPause();
        hidePopup();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!EventBus.getDefault().isRegistered(this) ){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void updateToken(String token) {
        EventBus.getDefault().post(new UpdateToken(token));

    }

    @Override
    public void onSuccessResponse(String response) {
        mobileVerifyResendCodePresenter.handleResendSMSRespone(response);
        mobileVerifyResendCodePresenter.updateToken(response);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        showSmsSendFailedError(new URError(context).getLocalizedError(ErrorType.NETWOK, ErrorCodes.NETWORK_ERROR));
        enableResendButtonAndHideSpinner();
    }

    public void handleUI() {
        updateUiStatus();
    }

    @Override
    public void onRefreshUserSuccess() {
        RLog.d(TAG, " onRefreshUserSuccess");
    }

    @Override
    public void onRefreshUserFailed(int error) {
        hideProgressSpinner();
        RLog.d(TAG, " onRefreshUserFailed");
    }

    @Override
    public void onUpdate() {
        handleUI();
    }

    @Override
    public void trackVerifyActionStatus(String state, String key, String value) {
        trackActionStatus(state, key, value);
    }

    @Override
    public void showSMSSpecifedError(int errorCode) {
        trackActionStatus(SEND_DATA, TECHNICAL_ERROR, MOBILE_RESEND_SMS_VERFICATION_FAILURE);
        updateErrorNotification(new URError(context).getLocalizedError(ErrorType.URX, errorCode), errorCode);
        enableResendButton();
    }


    @OnClick(R2.id.btn_reg_resend_update)
    public void verifyClicked() {
        RLog.i(TAG, TAG + ".verify Clicked");
        showProgressDialog();
        getRegistrationFragment().hideKeyBoard();
        hidePopup();
        errorMessage.hideError();
        mobileVerifyResendCodePresenter.resendOTPRequest(
                verificationSmsCodeURL, phoneNumberEditText.getText().toString());
        disableResendButton();
    }

    @OnClick(R2.id.btn_reg_code_received)
    public void thanksBtnClicked() {
        RLog.i(TAG, TAG + ".thanksBtn Clicked");

        hidePopup();
        getRegistrationFragment().onBackPressed();
    }

    @Override
    public void enableResendButton() {
        if (networkUtility.isNetworkAvailable()) {
            resendSMSButton.setEnabled(true);
        }
        hideProgressDialog();
    }

    public void updateResendTime(long timeLeft) {
        int timeRemaining = (int) (timeLeft / 1000);
        usrMobileverificationResendsmstimerProgress.setSecondaryProgress(
                ((60 - timeRemaining) * 100) / 60);
        usrMobileverificationResendsmstimerProgress.setText(
                String.format(getResources().getString(R.string.USR_DLS_ResendSMS_Progress_View_Progress_Text), timeRemaining));
        disableResendButton();
    }

    @Override
    public void netWorkStateOnlineUiHandle() {
        errorMessage.hideError();
        updateUiStatus();
    }

    @Override
    public void hideProgressSpinner() {
        enableResendButton();
        hideProgressDialog();
    }

    public void disableResendButton() {
        resendSMSButton.setEnabled(false);
    }

    @Override
    public void netWorkStateOfflineUiHandle() {
        errorMessage.setError(new URError(context).getLocalizedError(ErrorType.NETWOK, ErrorCodes.NO_NETWORK));
        phoneNumberEditText.setEnabled(false);
        resendSMSButton.setEnabled(false);
        smsReceivedButton.setEnabled(false);
    }

    @Override
    public void showSmsSendFailedError(String localizedError) {
        errorMessage.setError(localizedError);
        phoneNumberEditText.setText(mobileNumber);
        enableResendButton();
    }

    @Override
    public void enableResendButtonAndHideSpinner() {
        trackMultipleActionsOnMobileSuccess();
        handleResendVerificationEmailSuccess();
    }

    @Override
    public void trackMultipleActionsOnMobileSuccess() {
        Map<String, String> map = new HashMap<>();
        map.put(SPECIAL_EVENTS, MOBILE_RESEND_EMAIL_VERFICATION);
        map.put(MOBILE_INAPPNATIFICATION, MOBILE_RESEND_SMS_VERFICATION);
        AppTagging.trackMultipleActions(SEND_DATA, map);
    }

    @Subscribe
    public void onCountDownEvent(CountDownEvent event) {
        int progress = 100;
        if (event.getEvent().equals(RegConstants.COUNTER_FINISH)) {
            usrMobileverificationResendsmstimerProgress.setSecondaryProgress(progress);
            usrMobileverificationResendsmstimerProgress.setText(getResources().getString(R.string.USR_DLS_ResendSMS_Progress_View_Title_Text));
            enableResendButton();
        } else {
            updateResendTime(event.getTimeleft());
        }
    }

    public void viewOrHideNotificationBar() {
        if (popupWindow == null) {
            View view = getRegistrationFragment().getNotificationContentView(
                    context.getResources().getString(R.string.USR_DLS_ResendSMS_NotificationBar_Title)
                    , mobileNumber);
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setContentView(view);

        }
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            if (this.isVisible() && popupWindow != null) {
                popupWindow.showAtLocation(getActivity().
                        findViewById(R.id.ll_reg_root_container), Gravity.TOP, 0, 0);
            }
        }
    }

    void hidePopup() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    @Override
    public void notificationInlineMsg(String msg) {
        errorMessage.setError(msg);
    }
}
