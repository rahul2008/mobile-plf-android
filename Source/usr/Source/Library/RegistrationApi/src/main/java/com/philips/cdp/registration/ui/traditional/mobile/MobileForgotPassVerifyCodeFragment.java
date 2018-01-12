
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
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.customviews.*;
import com.philips.cdp.registration.ui.traditional.*;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.uid.view.widget.*;

import org.greenrobot.eventbus.*;

import java.util.*;

import javax.inject.*;

import butterknife.*;

import static com.janrain.android.Jump.*;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.*;

public class MobileForgotPassVerifyCodeFragment extends RegistrationBaseFragment implements
        MobileForgotPassVerifyCodeContract, OnUpdateListener{

    @Inject
    NetworkUtility networkUtility;

    @BindView(R2.id.btn_reg_Verify)
    ProgressBarButton verifyButton;

    @BindView(R2.id.btn_reg_resend_code)
    Button smsNotReceived;

    @BindView(R2.id.reg_error_msg)
    XRegError errorMessage;

    @BindView(R2.id.reg_verify_mobile_desc1)
    Label verifyPasswordDesc1;

    @BindView(R2.id.usr_forgotpassword_inputId_ValidationEditText)
    ValidationEditText verificationCodeValidationEditText;

    @BindView(R2.id.usr_verification_root_layout)
    LinearLayout usrVerificationRootLayout;

    private Context context;

    private MobileForgotPassVerifyCodePresenter mobileVerifyCodePresenter;

    private Handler handler;

    boolean isVerified;

    private String verificationSmsCodeURL;

    private String mobileNumber;

    private String responseToken;

    private String redirectUri;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final String mobileNumberKey = "mobileNumber";
        final String responseTokenKey = "token";
        final String redirectUriKey = "redirectUri";
        final String verificationSmsCodeURLKey = "verificationSmsCodeURL";

        RegistrationConfiguration.getInstance().getComponent().inject(this);


        Bundle bundle = getArguments();
        if(bundle!=null) {
            mobileNumber = bundle.getString(mobileNumberKey);
            responseToken = bundle.getString(responseTokenKey);
            redirectUri = bundle.getString(redirectUriKey);
            verificationSmsCodeURL = bundle.getString(verificationSmsCodeURLKey);
        }

        mobileVerifyCodePresenter = new MobileForgotPassVerifyCodePresenter(this);
        View view = inflater.inflate(R.layout.reg_mobile_forgotpassword_verify_fragment, container, false);
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
        String normalText = getString(R.string.reg_DLS_VerifySMS_Description_Text);
        SpannableString str = new SpannableString(String.format(normalText, mobileNumber));
        str.setSpan(new StyleSpan(Typeface.BOLD), normalText.length()-2, str.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        verifyPasswordDesc1.setText(str);
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
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEvent(UpdateToken event){
        responseToken = event.getToken();
    }

    @Subscribe
    public void onEvent(UpdateMobile event){
        mobileNumber = event.getMobileNumber();
        setDescription();
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
       // applyParams(config, usrVerificationRootLayout, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_SigIn_TitleTxt;
    }

    private void updateUiStatus() {
        if (verificationCodeValidationEditText.getText().length()
                >= RegConstants.VERIFY_CODE_MINIMUM_LENGTH) {
            enableVerifyButton();
        } else {
            disableVerifyButton();
        }
    }

    public void handleUI() {
        updateUiStatus();
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
        createSMSPasswordResetIntent();
    }

    public Intent createSMSPasswordResetIntent() {

        RLog.d("MobileVerifyCodeFragment ", "response" + verificationCodeValidationEditText.getText()
                + " " + redirectUri + " " + responseToken);
        constructRedirectUri();
        final String redirectUriKey = "redirectUri";
        ResetPasswordWebView resetPasswordWebView = new ResetPasswordWebView();
        Bundle bundle = new Bundle();
        bundle.putString(redirectUriKey, redirectUri);
        resetPasswordWebView.setArguments(bundle);
        getRegistrationFragment().addFragment(resetPasswordWebView);
        return null;
    }

    private void constructRedirectUri() {
        redirectUri = redirectUri + "?code=" + verificationCodeValidationEditText.getText()
                + "&token=" + responseToken;
    }

    @OnClick(R2.id.btn_reg_resend_code)
    public void resendButtonClicked() {
        final String mobileNumberKey = "mobileNumber";
        final String tokenKey = "token";
        final String redirectUriKey = "redirectUri";
        final String verificationSmsCodeURLKey = "verificationSmsCodeURL";
        disableVerifyButton();
        verifyButton.hideProgressIndicator();
        errorMessage.hideError();
        addFragment(mobileNumberKey, tokenKey, redirectUriKey, verificationSmsCodeURLKey);
    }

    private void addFragment(String mobileNumberKey, String tokenKey, String redirectUriKey,
                             String verificationSmsCodeURLKey) {
        MobileForgotPassVerifyResendCodeFragment mobileForgotPasswordVerifyCodeFragment
                = new MobileForgotPassVerifyResendCodeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(mobileNumberKey, mobileNumber);
        bundle.putString(tokenKey, responseToken);
        bundle.putString(redirectUriKey, getRedirectUri());
        bundle.putString(verificationSmsCodeURLKey, verificationSmsCodeURL);
        mobileForgotPasswordVerifyCodeFragment.setArguments(bundle);
        getRegistrationFragment().addFragment(mobileForgotPasswordVerifyCodeFragment);
    }

    public Intent getServiceIntent() {
        return new Intent(context, HttpClientService.class);
    }

    public HttpClientServiceReceiver getClientServiceRecevier() {
        return new HttpClientServiceReceiver(handler);
    }

    public ComponentName startService(Intent intent) {
        return context.startService(intent);
    }

    public void enableVerifyButton() {
        if ((verificationCodeValidationEditText.getText().length()
                >= RegConstants.VERIFY_CODE_MINIMUM_LENGTH)
                && networkUtility.isNetworkAvailable()) {
            verifyButton.setEnabled(true);
        }
    }

    public void disableVerifyButton() {
        verifyButton.setEnabled(false);
    }

    @Override
    public void netWorkStateOnlineUiHandle() {
        if (verificationCodeValidationEditText.getText().length()
                >= RegConstants.VERIFY_CODE_MINIMUM_LENGTH) {
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


    public void hideProgressSpinner() {
        verifyButton.hideProgressIndicator();
        smsNotReceived.setEnabled(true);
        verificationCodeValidationEditText.setEnabled(true);
        enableVerifyButton();
    }

//
//    public void storePreference(String emailOrMobileNumber) {
//        RegPreferenceUtility.storePreference(
//                getRegistrationFragment().getContext(), emailOrMobileNumber, true);
//    }


}
