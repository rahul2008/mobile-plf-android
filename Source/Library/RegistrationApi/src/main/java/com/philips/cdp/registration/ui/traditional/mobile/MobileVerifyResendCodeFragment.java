
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional.mobile;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.widget.Button;
import android.widget.*;

import com.philips.cdp.registration.*;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.app.tagging.*;
import com.philips.cdp.registration.events.*;
import com.philips.cdp.registration.handlers.*;
import com.philips.cdp.registration.ui.customviews.*;
import com.philips.cdp.registration.ui.traditional.*;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.uid.view.widget.*;

import org.greenrobot.eventbus.*;

import java.util.*;

import javax.inject.*;

import butterknife.*;

import static com.philips.cdp.registration.app.tagging.AppTagingConstants.*;

public class MobileVerifyResendCodeFragment extends RegistrationBaseFragment implements
        MobileVerifyResendCodeContract, RefreshUserHandler, OnUpdateListener, CounterListener{

   @BindView(R2.id.btn_reg_resend_update)
    ProgressBarButton resendSMSButton;

    @BindView(R2.id.btn_reg_code_received)
    Button smsReceivedButton;

    @BindView(R2.id.reg_error_msg)
    XRegError errorMessage;

    @BindView(R2.id.ll_reg_root_container)
    LinearLayout rootLayout;

    @BindView(R2.id.rl_reg_number_field)
    ValidationEditText phoneNumberEditText;

    @BindView(R2.id.usr_mobileverification_resendsmstimer_progress)
    ProgressBarWithLabel usr_mobileverification_resendsmstimer_progress;

    private Context context;

    private User user;

    private MobileVerifyResendCodePresenter mobileVerifyResendCodePresenter;

    private Handler handler;

    @Inject
    NetworkUtility networkUtility;

    private PopupWindow popupWindow;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        user = new User(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        URInterface.getComponent().inject(this);

        mobileVerifyResendCodePresenter = new MobileVerifyResendCodePresenter(this);
        View view = inflater.inflate(R.layout.reg_mobile_activation_resend_fragment, container,
                false);
        ButterKnife.bind(this, view);

        trackActionStatus(REGISTRATION_ACTIVATION_SMS, "", "");
        handleOrientation(view);
        handler = new Handler();
        phoneNumberEditText.setText(user.getMobile());
        phoneNumberEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        disableResendButton();
        if(!getRegistrationFragment().getCounterState()){
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
                if (!user.getMobile().equals(s.toString())) {

                    if (FieldsValidator.isValidMobileNumber(s.toString())) {
                        enableUpdateButton();
                    } else {
                        disableResendButton();
                    }
                } else {
                    enableResendButton();
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
        CounterHelper.getInstance().unregisterCounterEventNotification(RegConstants.COUNTER_TICK,
                this);
        CounterHelper.getInstance().unregisterCounterEventNotification(RegConstants.COUNTER_FINISH,
                this);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, rootLayout, width);
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

    private void handleResendVerificationSMSSuccess() {
        trackActionStatus(SEND_DATA, SPECIAL_EVENTS, SUCCESS_RESEND_SMS_VERIFICATION);
//        RegAlertDialog.showResetPasswordDialog(context.getResources().getString(
//          R.string.reg_Resend_SMS_title),
//        context.getResources().getString(R.string.reg_Resend_SMS_Success_Content),
//          getRegistrationFragment().getParentActivity(), mContinueVerifyBtnClick);
        viewOrHideNotificationBar();
        getRegistrationFragment().startCountDownTimer();
    }

    public void handleUI() {
       updateUiStatus();
    }

    @Override
    public void onRefreshUserSuccess() {
        RLog.d(RLog.EVENT_LISTENERS, "MobileActivationFragment : onRefreshUserSuccess");
        EventBus.getDefault().post(new UpdateMobile(user.getMobile()));
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

    private void trackMultipleActionsOnMobileSuccess() {
        Map<String, String> map = new HashMap<>();
        map.put(SPECIAL_EVENTS, MOBILE_RESEND_EMAIL_VERFICATION);
        map.put(MOBILE_INAPPNATIFICATION, MOBILE_RESEND_SMS_VERFICATION);
        AppTagging.trackMultipleActions(SEND_DATA, map);
    }

    @OnClick(R2.id.btn_reg_resend_update)
    public void verifyClicked() {
        showProgressDialog();
        getRegistrationFragment().hideKeyBoard();
        errorMessage.hideError();
        hidePopup();
        if (phoneNumberEditText.getText().toString().equals(user.getMobile())) {
            mobileVerifyResendCodePresenter.resendOTPRequest(user.getMobile());
            disableResendButton();

        } else {
            if (FieldsValidator.isValidMobileNumber(phoneNumberEditText.getText().toString())) {
                disableResendButton();
                mobileVerifyResendCodePresenter.updatePhoneNumber(
                        FieldsValidator.getMobileNumber(phoneNumberEditText.getText().toString()), context);
            } else {
                errorMessage.setError(getActivity().getResources().getString(
                        R.string.reg_InvalidPhoneNumber_ErrorMsg));
            }
        }
    }


   @OnClick(R2.id.btn_reg_code_received)
    public void thanksBtnClicked() {
        hidePopup();
       getRegistrationFragment().onBackPressed();
    }

    void hidePopup() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
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
        resendSMSButton.setText(getActivity().getResources().getString(
                R.string.reg_Resend_SMS_title));
        resendSMSButton.setProgressText(getActivity().getResources().getString(
                R.string.reg_Resend_SMS_title));
        if(networkUtility.isNetworkAvailable())
            resendSMSButton.setEnabled(true);
    }

    @Override
    public void enableUpdateButton() {
        resendSMSButton.setText(getActivity().getResources().getString(
                R.string.reg_Update_MobileNumber_Button_Text));
        resendSMSButton.setProgressText(getActivity().getResources().getString(
                R.string.reg_Update_MobileNumber_Button_Text));
        resendSMSButton.setEnabled(true);

    }

    public void updateResendTime(long timeLeft) {
        if (user.getMobile().equals(phoneNumberEditText.getText().toString())) {
                    int timeRemaining = (int)(timeLeft / 1000);
            usr_mobileverification_resendsmstimer_progress.setSecondaryProgress(
                    ((60 - timeRemaining)*100)/60);
            String timeRemainingAsString = Integer.toString(timeRemaining);
            usr_mobileverification_resendsmstimer_progress.setText(
                    String.format(getString(R.string.reg_DLS_ResendSMS_Progress_View_Progress_Text), timeRemaining));
            disableResendButton();
        }
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

    @Override
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
        phoneNumberEditText.setText(user.getMobile());
        enableResendButton();
    }

    @Override
    public void enableResendButtonAndHideSpinner() {
        trackMultipleActionsOnMobileSuccess();
        handleResendVerificationSMSSuccess();
    }

    @Override
    public void showSmsResendTechincalError(String errorCodeString) {
        trackActionStatus(SEND_DATA, TECHNICAL_ERROR, MOBILE_RESEND_SMS_VERFICATION_FAILURE);
        errorMessage.setError(errorCodeString);
        enableResendButton();
    }

    @Override
    public void showNumberChangeTechincalError(String errorCodeString) {
        trackActionStatus(SEND_DATA, TECHNICAL_ERROR, MOBILE_RESEND_SMS_VERFICATION_FAILURE);
        String errorMsg = RegChinaUtil.getErrorMsgDescription(errorCodeString, context);
        errorMessage.setError(errorMsg);
        enableUpdateButton();
    }

    @Override
    public void refreshUser() {
        user.refreshUser(this);
        getRegistrationFragment().stopCountDownTimer();
        enableResendButton();
    }


    @Override
    public void onCounterEventReceived(String event, long timeLeft) {
        int progress =100;
        if(event.equals(RegConstants.COUNTER_FINISH)){
            usr_mobileverification_resendsmstimer_progress.setSecondaryProgress(progress);
            usr_mobileverification_resendsmstimer_progress.setText(getResources().getString(R.string.reg_DLS_ResendSMS_Progress_View_Title_Text));
            enableResendButton();
        }else{
            updateResendTime(timeLeft);
        }
    }

    public void viewOrHideNotificationBar() {
        if (popupWindow == null) {
            View view = getRegistrationFragment().getNotificationContentView(
                    context.getResources().getString(R.string.reg_Resend_SMS_Success_Content),
                    user.getMobile().toString());
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


}
