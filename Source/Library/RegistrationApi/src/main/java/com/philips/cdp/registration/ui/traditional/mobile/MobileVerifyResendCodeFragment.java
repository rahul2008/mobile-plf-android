
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional.mobile;

import android.app.ProgressDialog;
import android.content.*;
import android.content.res.Configuration;
import android.os.*;
import android.text.*;
import android.view.*;
import android.widget.*;

import com.philips.cdp.registration.*;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.events.*;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.ui.customviews.*;
import com.philips.cdp.registration.ui.traditional.RegistrationBaseFragment;
import com.philips.cdp.registration.ui.utils.*;

import java.util.*;

import butterknife.*;

import static com.philips.cdp.registration.app.tagging.AppTagingConstants.*;

public class MobileVerifyResendCodeFragment extends RegistrationBaseFragment implements
        MobileVerifyResendCodeContract, RefreshUserHandler, OnUpdateListener, CounterListener{

    @BindView(R2.id.ll_reg_create_account_fields)
    LinearLayout phoneNumberEditTextContainer;

    @BindView(R2.id.rl_reg_singin_options)
    RelativeLayout verifyButtonContainer;

    @BindView(R2.id.btn_reg_resend_update)
    Button resendSMSButton;

    @BindView(R2.id.btn_reg_code_received)
    Button smsReceivedButton;

    @BindView(R2.id.reg_error_msg)
    XRegError errorMessage;

    @BindView(R2.id.rl_reg_number_field)
    XEditText phoneNumberEditText;

    private Context context;

    private User user;

    private MobileVerifyResendCodePresenter mobileVerifyResendCodePresenter;

    private Handler handler;

    private ProgressDialog mProgressDialog;

    private static final String UPDATE_PHONENUMBER = "Update PhoneNumber";

    private static final String RESEND_SMS = "Resend SMS";

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        URInterface.getComponent().inject(this);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onCreateView");
        trackActionStatus(REGISTRATION_ACTIVATION_SMS, "", "");
        context = getRegistrationFragment().getActivity().getApplicationContext();
        mobileVerifyResendCodePresenter = new MobileVerifyResendCodePresenter(this);
        user = new User(context);
        View view = inflater.inflate(R.layout.reg_mobile_activation_resend_fragment, container, false);
        ButterKnife.bind(this, view);
        handleOrientation(view);
        handler = new Handler();
        phoneNumberEditText.setText(user.getMobile());
        phoneNumberEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        if(!getRegistrationFragment().getCounterState()){
            resendSMSButton.setEnabled(true);
        }
        phoneNumberChange();
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(getActivity(), R.style.reg_Custom_loaderTheme);
        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        mProgressDialog.setCancelable(false);

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
                    resendSMSButton.setText(UPDATE_PHONENUMBER);
                    if (FieldsValidator.isValidMobileNumber(s.toString())) {
                        enableUpdateButton();
                    } else {
                        disableResendButton();
                    }
                } else {
                    resendSMSButton.setText(RESEND_SMS);
                    enableResendButton();
                }
                errorMessage.hideError();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void showProgressDialog() {
        if (!(getActivity().isFinishing()) && (mProgressDialog != null)) mProgressDialog.show();
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
        applyParams(config, phoneNumberEditTextContainer, width);
        applyParams(config, verifyButtonContainer, width);
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
        if (phoneNumberEditText.getText().length() >= RegConstants.VERIFY_CODE_MINIMUM_LENGTH) {
            enableResendButton();
        } else {
            disableResendButton();
        }
    }

    private void handleResendVerificationEmailSuccess() {
        trackActionStatus(SEND_DATA, SPECIAL_EVENTS, SUCCESS_RESEND_EMAIL_VERIFICATION);
        RegAlertDialog.showResetPasswordDialog(context.getResources().getString(R.string.reg_Resend_SMS_title),
                context.getResources().getString(R.string.reg_Resend_SMS_Success_Content), getRegistrationFragment().getParentActivity(), mContinueVerifyBtnClick);
        getRegistrationFragment().startCountDownTimer();
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

    private void trackMultipleActionsOnMobileSuccess() {
        Map<String, String> map = new HashMap<>();
        map.put(SPECIAL_EVENTS, MOBILE_RESEND_EMAIL_VERFICATION);
        map.put(MOBILE_INAPPNATIFICATION, MOBILE_RESEND_SMS_VERFICATION);
        AppTagging.trackMultipleActions(SEND_DATA, map);
    }

    @OnClick(R2.id.btn_reg_resend_update)
    public void verifyClicked() {
        showProgressDialog();
        errorMessage.hideError();
        if (phoneNumberEditText.getText().toString().equals(user.getMobile())) {
            mobileVerifyResendCodePresenter.resendOTPRequest(user.getMobile());
            disableResendButton();

        } else {
            if (FieldsValidator.isValidMobileNumber(phoneNumberEditText.getText().toString())) {
                disableResendButton();
                mobileVerifyResendCodePresenter.updatePhoneNumber(phoneNumberEditText.getText().toString(), context);
            } else {
                errorMessage.setError(getActivity().getResources().getString(R.string.reg_InvalidPhoneNumber_ErrorMsg));
            }
        }
    }


    @OnClick(R2.id.btn_reg_code_received)
    public void thanksBtnClicked() {
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
        resendSMSButton.setText(RESEND_SMS);
        resendSMSButton.setEnabled(true);
    }

    @Override
    public void enableUpdateButton() {
        resendSMSButton.setText(UPDATE_PHONENUMBER);
        resendSMSButton.setEnabled(true);

    }

    @Override
    public void updateResendTime(long timeLeft) {
        if (user.getMobile().equals(phoneNumberEditText.getText().toString())) {
                    int timeRemaining = (int)(timeLeft / 1000);
                        resendSMSButton.setText(RESEND_SMS + " (" + timeRemaining + "s)");
                        disableResendButton();
        }
    }

    @Override
    public void hideErrorMessage() {
        errorMessage.hideError();
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
    public void showNoNetworkErrorMessage() {
        errorMessage.setError(context.getResources().getString(R.string.reg_NoNetworkConnection));
        enableResendButton();
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
        handleResendVerificationEmailSuccess();
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
        errorMessage.setError(errorCodeString);
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
        if(event.equals(RegConstants.COUNTER_FINISH)){
            enableResendButton();
        }else{
            updateResendTime(timeLeft);
        }
    }
}
