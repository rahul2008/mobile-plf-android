
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional.mobile;

import android.content.*;
import android.content.res.Configuration;
import android.os.*;
import android.text.*;
import android.view.*;
import android.widget.*;

import com.philips.cdp.registration.*;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.ui.customviews.*;
import com.philips.cdp.registration.ui.traditional.RegistrationBaseFragment;
import com.philips.cdp.registration.ui.utils.*;

import java.util.*;

import butterknife.*;

import static android.view.View.GONE;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.*;

public class MobileVerifyCodeFragment extends RegistrationBaseFragment implements
        MobileVerifyCodeContract, RefreshUserHandler, OnUpdateListener{

    @BindView(R2.id.ll_reg_create_account_fields)
    LinearLayout phoneNumberEditTextContainer;

    @BindView(R2.id.rl_reg_singin_options)
    RelativeLayout verifyButtonContainer;

    @BindView(R2.id.btn_reg_Verify)
    Button verifyButton;

    @BindView(R2.id.btn_reg_resend_code)
    Button smsNotReceived;

    @BindView(R2.id.rl_reg_name_field)
    XEditText verificationCodeEditText;

    @BindView(R2.id.pb_reg_activate_spinner)
    ProgressBar spinnerProgress;

    @BindView(R2.id.reg_error_msg)
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
        ButterKnife.bind(this, view);
        handleOrientation(view);
        getRegistrationFragment().startCountDownTimer();

        verificationCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= RegConstants.VERIFY_CODE_MINIMUM_LENGTH) {
                    enableVerifyButton();
                } else {
                    disableVerifyButton();
                }
                errorMessage.hideError();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
        if (verificationCodeEditText.getText().length() >= RegConstants.VERIFY_CODE_MINIMUM_LENGTH) {
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
        hideProgressSpinner();
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
        spinnerProgress.setVisibility(View.VISIBLE);
        disableVerifyButton();
        mobileVerifyCodePresenter.verifyMobileNumber(user.getJanrainUUID(),
                verificationCodeEditText.getText().toString());
    }

    @OnClick(R2.id.btn_reg_resend_code)
    public void resendButtonClicked() {
        disableVerifyButton();
        spinnerProgress.setVisibility(GONE);
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
        errorMessage.setError(getString(R.string.reg_URX_SMS_InternalServerError));
    }

    @Override
    public void refreshUserOnSmsVerificationSuccess() {
        trackActionStatus(SEND_DATA, SPECIAL_EVENTS, SUCCESS_USER_REGISTRATION);
        user.refreshUser(this);
    }

    @Override
    public void smsVerificationResponseError() {
        errorMessage.setError(getString(R.string.reg_Mobile_Verification_Invalid_Code));
    }

    @Override
    public void hideProgressSpinner() {
        spinnerProgress.setVisibility(GONE);
        enableVerifyButton();

    }

    @Override
    public void setOtpInvalidErrorMessage() {
        trackActionStatus(SEND_DATA, USER_ERROR, ACTIVATION_NOT_VERIFIED);
        errorMessage.setError(getString(R.string.reg_Mobile_Verification_Invalid_Code));
    }

    @Override
    public void setOtpErrorMessageFromJson(String errorDescription) {
        trackActionStatus(SEND_DATA, USER_ERROR, ACTIVATION_NOT_VERIFIED);
        errorMessage.setError(errorDescription);
    }

    @Override
    public void storePreference(String emailOrMobileNumber) {
        RegPreferenceUtility.storePreference(getRegistrationFragment().getContext(), emailOrMobileNumber, true);
    }

    @Override
    public void showOtpInvalidError() {
        errorMessage.setError(getString(R.string.reg_Mobile_Verification_Invalid_Code));
    }
}
