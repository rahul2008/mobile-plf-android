package com.philips.cdp.registration.ui.traditional.mobile;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cdp.registration.B;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.ui.customviews.LoginIdEditText;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.traditional.AccountActivationFragment;
import com.philips.cdp.registration.ui.traditional.RegistrationBaseFragment;
import com.philips.cdp.registration.ui.traditional.WelcomeFragment;
import com.philips.cdp.registration.ui.utils.RLog;

import butterfork.Bind;
import butterfork.ButterFork;
import butterfork.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.philips.cdp.registration.app.tagging.AppTagingConstants.REGISTRATION_ACTIVATION_SMS;


public class AddSecureEmailFragment extends RegistrationBaseFragment implements AddSecureEmailContract {

    @Bind(B.id.btn_reg_secure_data_email)
    Button addRecoveryEmailButton;

    @Bind(B.id.btn_reg_secure_data_email_later)
    Button maybeLaterButton;

    @Bind(B.id.rl_reg_securedata_email_field)
    LoginIdEditText recoveryEmail;

    @Bind(B.id.reg_error_msg)
    XRegError recoveryErrorTextView;

    private AddSecureEmailPresenter addSecureEmailPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onCreateView");
        trackActionStatus(REGISTRATION_ACTIVATION_SMS,"","");
        addSecureEmailPresenter = new AddSecureEmailPresenter(this);
        View view = inflater.inflate(R.layout.reg_fragment_secure_email, container, false);
        ButterFork.bind(this, view);
        setUpRecoveryEmail();
        return view;
    }

    private void setUpRecoveryEmail() {
        recoveryEmail.setInputType(InputType.TYPE_CLASS_TEXT);
        recoveryEmail.setHint(getString(R.string.reg_recover_email_enter_your_email));
    }

    @Override
    protected void setViewParams(Configuration config, int width) {

    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_RegCreateAccount_NavTitle;
    }


    @OnClick(B.id.btn_reg_secure_data_email)
    public void addEmailButtonClicked(){
        recoveryErrorTextView.setVisibility(GONE);
        addSecureEmailPresenter.addEmailClicked(recoveryEmail.getEmailId());
    }

    @OnClick(B.id.btn_reg_secure_data_email_later)
    public void maybeLaterButtonClicked() {
        addSecureEmailPresenter.maybeLaterClicked();
    }

    @Override
    public void showWelcomeScreen() {
        getRegistrationFragment().addFragment(new WelcomeFragment());
    }

    @Override
    public void showInvalidEmailError() {
        recoveryEmail.setErrDescription(getString(R.string.reg_InvalidEmailAdddress_ErrorMsg));
        recoveryEmail.showInvalidAlert();
    }

    @Override
    public void onAddRecoveryEmailSuccess() {
        getRegistrationFragment().addFragment(new AccountActivationFragment());
    }

    @Override
    public void onAddRecoveryEmailFailure(String error) {
        recoveryErrorTextView.setError(error);
        recoveryErrorTextView.setVisibility(VISIBLE);
    }
}
