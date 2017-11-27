
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional.mobile;

import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.view.*;
import android.widget.*;
import android.widget.Button;

import com.jakewharton.rxbinding2.widget.*;
import com.philips.cdp.registration.*;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.app.tagging.*;
import com.philips.cdp.registration.handlers.*;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.customviews.*;
import com.philips.cdp.registration.ui.traditional.*;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.uid.view.widget.*;

import org.greenrobot.eventbus.*;

import java.util.*;

import javax.inject.*;

import butterknife.*;

import static com.philips.cdp.registration.app.tagging.AppTagingConstants.*;

public class MobileVerifyCodeFragment extends RegistrationBaseFragment implements
        MobileVerifyCodeContract, RefreshUserHandler, OnUpdateListener{

    @Inject
    NetworkUtility networkUtility;

    @BindView(R2.id.btn_reg_Verify)
    ProgressBarButton verifyButton;

    @BindView(R2.id.btn_reg_resend_code)
    Button smsNotReceived;

    @BindView(R2.id.reg_error_msg)
    XRegError errorMessage;

    @BindView(R2.id.reg_verify_mobile_desc1)
    Label reg_verify_mobile_desc1;

    @BindView(R2.id.usr_forgotpassword_inputId_ValidationEditText)
    ValidationEditText verificationCodeValidationEditText;

    @BindView(R2.id.usr_activation_root_layout)
    LinearLayout usrAccountRootLayout;

    private Context context;

    private User user;

    private MobileVerifyCodePresenter mobileVerifyCodePresenter;

    private Handler handler;

    boolean isVerified;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        user = new User(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        URInterface.getComponent().inject(this);

        mobileVerifyCodePresenter = new MobileVerifyCodePresenter(this);

        View view = inflater.inflate(R.layout.reg_mobile_activatiom_fragment, container, false);
        trackActionStatus(REGISTRATION_ACTIVATION_SMS,"","");
        ButterKnife.bind(this, view);
        handleOrientation(view);
        getRegistrationFragment().startCountDownTimer();
        setDescription();
        handler = new Handler();
        handleVerificationCode();
        return view;
    }

    private void setDescription() {
        String userId =  user.getMobile();
        String normalText = getString(R.string.reg_DLS_VerifySMS_Description_Text);
        SpannableString str = new SpannableString(String.format(normalText, userId));
        str.setSpan(new StyleSpan(Typeface.BOLD), normalText.length()-2, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        reg_verify_mobile_desc1.setText(str);
    }

    private void handleVerificationCode() {
        RxTextView.textChangeEvents(verificationCodeValidationEditText)
                .subscribe(aBoolean -> {
                    if (verificationCodeValidationEditText.getText().length() == 6)
                        enableVerifyButton();
                    else
                        disableVerifyButton();
                });
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
        RegistrationHelper.getInstance().unRegisterNetworkListener(getRegistrationFragment());
        mobileVerifyCodePresenter.cleanUp();
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, usrAccountRootLayout, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_DLS_URCreateAccount_NavTitle;
    }

    private void updateUiStatus() {
        if (verificationCodeValidationEditText.length() >= RegConstants.VERIFY_CODE_MINIMUM_LENGTH) {
            enableVerifyButton();
        } else {
            disableVerifyButton();
        }
    }

    public void handleUI() {
        updateUiStatus();
    }

    @Override
    public void onRefreshUserSuccess() {
        RLog.d(RLog.EVENT_LISTENERS, "MobileActivationFragment : onRefreshUserSuccess");
        storePreference(user.getMobile());
        setDescription();
        hideProgressSpinner();
        if(isVerified)
            getRegistrationFragment().addFragment(new AddSecureEmailFragment());
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

    @OnClick(R2.id.btn_reg_Verify)
    public void verifyClicked() {
        verifyButton.showProgressIndicator();
        smsNotReceived.setEnabled(false);
        verificationCodeValidationEditText.setEnabled(false);
        getRegistrationFragment().hideKeyBoard();
        mobileVerifyCodePresenter.verifyMobileNumber(user.getJanrainUUID(),
                verificationCodeValidationEditText.getText().toString());
    }


    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        user.refreshUser(this);
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEvent(UpdateMobile event){
        user.refreshUser(this);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        user.refreshUser(this);
        super.onViewStateRestored(savedInstanceState);
    }

    @OnClick(R2.id.btn_reg_resend_code)
    public void resendButtonClicked() {
        disableVerifyButton();
        verifyButton.hideProgressIndicator();
        getRegistrationFragment().addFragment( new MobileVerifyResendCodeFragment());
        errorMessage.hideError();

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
    public void enableVerifyButton() {
        if ((verificationCodeValidationEditText.length() >= RegConstants.VERIFY_CODE_MINIMUM_LENGTH) &&
                networkUtility.isNetworkAvailable()) {
            verifyButton.setEnabled(true);
        }
    }

    @Override
    public void disableVerifyButton() {
        verifyButton.setEnabled(false);
    }

    @Override
    public void netWorkStateOnlineUiHandle() {
        if (verificationCodeValidationEditText.length() >= RegConstants.VERIFY_CODE_MINIMUM_LENGTH) {
            verifyButton.setEnabled(true);
        }
        errorMessage.hideError();
        smsNotReceived.setEnabled(true);
    }

    @Override
    public void netWorkStateOfflineUiHandle() {
        hideProgressSpinner();
        errorMessage.setError(context.getResources().getString(R.string.reg_NoNetworkConnection));
        smsNotReceived.setEnabled(false);
        disableVerifyButton();
    }

    @Override
    public void showSmsSendFailedError() {
        errorMessage.setError(getString(R.string.reg_URX_SMS_InternalServerError));
        hideProgressSpinner();
    }

    @Override
    public void refreshUserOnSmsVerificationSuccess() {
        trackActionStatus(SEND_DATA, SPECIAL_EVENTS, SUCCESS_USER_REGISTRATION);
        isVerified = true;
        user.refreshUser(this);
    }

    @Override
    public void smsVerificationResponseError() {
        errorMessage.setError(getString(R.string.reg_Mobile_Verification_Invalid_Code));
        hideProgressSpinner();

    }

    @Override
    public void hideProgressSpinner() {
        verifyButton.hideProgressIndicator();
        smsNotReceived.setEnabled(true);
        verificationCodeValidationEditText.setEnabled(true);
        enableVerifyButton();
    }

    @Override
    public void setOtpInvalidErrorMessage() {
        trackActionStatus(SEND_DATA, USER_ERROR, ACTIVATION_NOT_VERIFIED);
        errorMessage.setError(getString(R.string.reg_Mobile_Verification_Invalid_Code));
        hideProgressSpinner();
    }

    @Override
    public void setOtpErrorMessageFromJson(String errorDescription) {
        trackActionStatus(SEND_DATA, USER_ERROR, ACTIVATION_NOT_VERIFIED);
        errorMessage.setError(errorDescription);
        hideProgressSpinner();
    }

    @Override
    public void storePreference(String emailOrMobileNumber) {
        RegPreferenceUtility.storePreference(getRegistrationFragment().getContext(), RegConstants.TERMS_N_CONDITIONS_ACCEPTED, emailOrMobileNumber);
    }

    @Override
    public void showOtpInvalidError() {
        errorMessage.setError(getString(R.string.reg_Mobile_Verification_Invalid_Code));
        hideProgressSpinner();
    }
}
