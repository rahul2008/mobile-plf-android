
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional.mobile;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
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

import com.philips.cdp.registration.HttpClientService;
import com.philips.cdp.registration.HttpClientServiceReceiver;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.R2;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.events.CounterHelper;
import com.philips.cdp.registration.events.CounterListener;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.ui.customviews.OnUpdateListener;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.traditional.RegistrationBaseFragment;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.NotificationBarHandler;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegAlertDialog;
import com.philips.cdp.registration.ui.utils.RegChinaUtil;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.URInterface;
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
        MobileForgotPassVerifyResendCodeContract, RefreshUserHandler, OnUpdateListener, CounterListener {

    @BindView(R2.id.btn_reg_resend_update)
    ProgressBarButton resendSMSButton;

    @BindView(R2.id.btn_reg_code_received)
    Button smsReceivedButton;

    @BindView(R2.id.reg_error_msg)
    XRegError errorMessage;

    @BindView(R2.id.rl_reg_number_field)
    ValidationEditText phoneNumberEditText;

    @BindView(R2.id.usr_mobileverification_resendsmstimer_progress)
    ProgressBarWithLabel usr_mobileverification_resendsmstimer_progress;

    @BindView(R2.id.ll_reg_root_container)
    LinearLayout fragmentRootLayout;

    private Context context;

    private MobileForgotPassVerifyResendCodePresenter mobileVerifyResendCodePresenter;

    private Handler handler;

    private PopupWindow popupWindow;

    @Inject
    NetworkUtility networkUtility;

    private String verificationSmsCodeURL;

    private String mobileNumber;

    private String redirectUri;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        URInterface.getComponent().inject(this);

        mobileVerifyResendCodePresenter = new MobileForgotPassVerifyResendCodePresenter(this);

        final String mobileNumberKey = "mobileNumber";
        final String redirectUriKey = "redirectUri";
        final String verificationSmsCodeURLKey = "verificationSmsCodeURL";
        Bundle bundle = getArguments();

        if(bundle!=null) {
            mobileNumber = bundle.getString(mobileNumberKey);
            redirectUri = bundle.getString(redirectUriKey);
            verificationSmsCodeURL = bundle.getString(verificationSmsCodeURLKey);
            mobileVerifyResendCodePresenter.setRedirectUri(redirectUri);
        }
        View view = inflater.inflate(R.layout.reg_mobile_forgot_password_resend_fragment, container, false);

        trackActionStatus(REGISTRATION_ACTIVATION_SMS, "", "");
        ButterKnife.bind(this, view);
        handleOrientation(view);
        handler = new Handler();
        phoneNumberEditText.setText(mobileNumber);
        phoneNumberEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        disableResendButton();
        if (!getRegistrationFragment().getCounterState()) {
            enableResendButton();
        }
        phoneNumberChange();
        CounterHelper.getInstance()
                .registerCounterEventNotification(RegConstants.COUNTER_TICK, this);
        CounterHelper.getInstance()
                .registerCounterEventNotification(RegConstants.COUNTER_FINISH, this);
        return view;
    }

    private void phoneNumberChange() {
        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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

            }
        });
    }

    private ProgressDialog mProgressDialog;

    private void showProgressDialog() {
        if (!getActivity().isFinishing()) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(getActivity(), R.style.reg_Custom_loaderTheme);
                mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
                mProgressDialog.setCancelable(false);
            }
            mProgressDialog.show();        }
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
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
        mobileVerifyResendCodePresenter.cleanUp();
        CounterHelper.getInstance().unregisterCounterEventNotification(RegConstants.COUNTER_TICK, this);
        CounterHelper.getInstance().unregisterCounterEventNotification(RegConstants.COUNTER_FINISH, this);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, fragmentRootLayout, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_Resend_SMS_title;
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
//        RegAlertDialog.showResetPasswordDialog(context.getResources().getString(R.string.reg_Resend_SMS_title),
//                context.getResources().getString(R.string.reg_Resend_SMS_Success_Content),
//                getRegistrationFragment().getParentActivity(), mContinueVerifyBtnClick);
        viewOrHideNotificationBar();
        hideProgressDialog();
        getRegistrationFragment().startCountDownTimer();
        if(!mobileNumber.equals(phoneNumberEditText.getText().toString())){
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
        EventBus.getDefault().unregister(this);
        hidePopup();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void updateToken(String token) {
        EventBus.getDefault().post(new UpdateToken(token));

    }

    public void handleUI() {
        updateUiStatus();
    }

    @Override
    public void onRefreshUserSuccess() {
        RLog.d(RLog.EVENT_LISTENERS, "MobileActivationFragment : onRefreshUserSuccess");
    }

    @Override
    public void onRefreshUserFailed(int error) {
        hideProgressSpinner();
        RLog.d(RLog.EVENT_LISTENERS, "MobileActivationFragment : onRefreshUserFailed");
    }


    @Override
    public void onUpdate() {
        handleUI();
    }

    private View.OnClickListener mContinueVerifyBtnClick = view -> RegAlertDialog.dismissDialog();


    @Override
    public void trackVerifyActionStatus(String state, String key, String value) {
        trackActionStatus(state, key, value);
    }

    @Override
    public void showSMSSpecifedError(String id) {
        String errorMsg = RegChinaUtil.getErrorMsgDescription(id, context);
        showSmsResendTechincalError(errorMsg);
    }


    @OnClick(R2.id.btn_reg_resend_update)
    public void verifyClicked() {
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
        hidePopup();
        getRegistrationFragment().onBackPressed();
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
        if (networkUtility.isNetworkAvailable())
            resendSMSButton.setEnabled(true);
            hideProgressDialog();
    }

    public void updateResendTime(long timeLeft) {
        int timeRemaining = (int) (timeLeft / 1000);
        usr_mobileverification_resendsmstimer_progress.setSecondaryProgress(
                ((60 - timeRemaining) * 100) / 60);
        String timeRemainingAsString = Integer.toString(timeRemaining);
        usr_mobileverification_resendsmstimer_progress.setText(
                String.format(getResources().getString(R.string.reg_DLS_ResendSMS_Progress_View_Progress_Text), timeRemaining));
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
        errorMessage.setError(context.getResources().getString(R.string.reg_NoNetworkConnection));
        phoneNumberEditText.setEnabled(false);
        resendSMSButton.setEnabled(false);
        smsReceivedButton.setEnabled(false);
    }

    @Override
    public void showSmsSendFailedError() {
        errorMessage.setError(getResources().getString(R.string.reg_URX_SMS_InternalServerError));
        phoneNumberEditText.setText(mobileNumber);
        enableResendButton();
    }

    @Override
    public void enableResendButtonAndHideSpinner() {
        trackMultipleActionsOnMobileSuccess();
        handleResendVerificationEmailSuccess();
    }

    @Override
    public void showSmsResendTechincalError(String errorCodeString) {
        trackActionStatus(SEND_DATA, TECHNICAL_ERROR, MOBILE_RESEND_SMS_VERFICATION_FAILURE);
        errorMessage.setError(errorCodeString);
        enableResendButton();
    }

    @Override
    public void trackMultipleActionsOnMobileSuccess() {
        Map<String, String> map = new HashMap<>();
        map.put(SPECIAL_EVENTS, MOBILE_RESEND_EMAIL_VERFICATION);
        map.put(MOBILE_INAPPNATIFICATION, MOBILE_RESEND_SMS_VERFICATION);
        AppTagging.trackMultipleActions(SEND_DATA, map);
    }

    @Override
    public void onCounterEventReceived(String event, long timeLeft) {
        int progress = 100;
        if (event.equals(RegConstants.COUNTER_FINISH)) {
            usr_mobileverification_resendsmstimer_progress.setSecondaryProgress(progress);
            usr_mobileverification_resendsmstimer_progress.setText(getResources().getString(R.string.reg_DLS_ResendSMS_Progress_View_Title_Text));
            enableResendButton();
        } else {
            updateResendTime(timeLeft);
        }
    }

    public void viewOrHideNotificationBar() {
        if (popupWindow == null) {
            View view = getRegistrationFragment().getNotificationContentView(
                    context.getResources().getString(R.string.reg_Resend_SMS_Success_Content)
                    ,mobileNumber);
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setContentView(view);

        }
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            popupWindow.showAtLocation(getActivity().
                    findViewById(R.id.ll_reg_root_container), Gravity.TOP, 0, 0);
        }
    }
    void hidePopup() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

}
