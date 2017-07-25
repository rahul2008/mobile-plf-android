
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
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.R2;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.tagging.AppTaggingPages;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.traditional.mobile.MobileVerifyCodeFragment;
import com.philips.cdp.registration.ui.utils.EmailValidator;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.NameValidator;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.PasswordValidator;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegPreferenceUtility;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.cdp.registration.ui.utils.ThreadUtils;
import com.philips.cdp.registration.ui.utils.UIFlow;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.platform.uid.view.widget.CheckBox;
import com.philips.platform.uid.view.widget.InputValidationLayout;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.ProgressBarButton;
import com.philips.platform.uid.view.widget.ValidationEditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateAccountFragment extends RegistrationBaseFragment implements CreateAccountContract {

    @Inject
    NetworkUtility networkUtility;

    @BindView(R2.id.reg_create_password_validation)
    LinearLayout passwordLayout;

    @BindView(R2.id.checkbox_receive)
    CheckBox mCbMarketingOpt;

    @BindView(R2.id.checkbox_accept)
    CheckBox mCbAcceptTerms;

    @BindView(R2.id.tv_reg_first_to_know)
    TextView mTvFirstToKnow;

    @BindView(R2.id.rl_reg_first_name)
    ValidationEditText mEtFirstName;

    @BindView(R2.id.rl_reg_last_name)
    ValidationEditText mEtLastName;

    @BindView(R2.id.rl_reg_first_name_input_field)
    InputValidationLayout mEtFirstNameInputValidation;

    @BindView(R2.id.rl_reg_last_name_input_field)
    InputValidationLayout mEtLastNameInputValidation;

    @BindView(R2.id.rl_reg_password_field)
    EditText mEtPassword;

    @BindView(R2.id.rl_reg_password_field_input_field)
    InputValidationLayout rl_reg_password_field_input_field;

    @BindView(R2.id.reg_error_msg)
    XRegError mRegError;

    @BindView(R2.id.cb_reg_accept_terms_error)
    XRegError mRegAccptTermsError;

    @BindView(R2.id.sv_root_layout)
    ScrollView mSvRootLayout;

    @BindView(R2.id.textbox_input_field)
    InputValidationLayout textbox_input_field;

    @BindView(R2.id.reg_field_email)
    ValidationEditText reg_field_email;

    @BindView(R2.id.progressBar)
    ProgressBar progressBar;

    @BindView(R2.id.passwordStrength)
    Label passwordStrength;

    @BindView(R2.id.password_description)
    Label password_description;

    @BindView(R2.id.btn_reg_switch_login)
    Button switchToLogin;

    @BindView(R2.id.buttonsProgressIndicatorExtraWideIndeterminate)
    ProgressBarButton progressBarButton;

    @BindView(R2.id.ll_reg_create_account_container)
    LinearLayout createAccountBaseLayout;

    private User mUser;

    private Context mContext;

    private long mTrackCreateAccountTime;

    private CreateAccountPresenter createAccountPresenter;

    private String mEmail;

    boolean isValidEmail;

    boolean isValidPassword;

    boolean isValidFirstname;

    boolean isValidLastame;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onCreate");
        super.onCreate(savedInstanceState);
    }

    public interface PasswordStrength {
        int getStrength(int strength);
    }

    public interface ValidEmail {
        int isValid(boolean valid);
        int isEmpty(boolean emptyField);
    }

    PasswordValidator passwordValidator = new PasswordValidator(new PasswordStrength() {
        @Override
        public int getStrength(int strength) {
            passwordLayout.setVisibility(View.VISIBLE);
            return passwordValidation(strength);
        }
    });


    public EmailValidator emailValidator = new EmailValidator(new ValidEmail() {
        @Override
        public int isValid(boolean valid) {
            isValidEmail = true;
            enableCreateButton();
            return 0;
        }

        @Override
        public int isEmpty(boolean emptyField) {
            if (emptyField) {
                textbox_input_field.setErrorMessage(R.string.reg_EmptyField_ErrorMsg);
            } else if (emptyField) {
                textbox_input_field.setErrorMessage(R.string.reg_InvalidEmailAdddress_ErrorMsg);
            }
            isValidEmail = false;
            disableCreateButton();
            return 0;
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        URInterface.getComponent().inject(this);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onCreateView");
        RLog.d(RLog.EVENT_LISTENERS,
                "CreateAccountFragment register: NetworStateListener,JANRAIN_INIT_SUCCESS");
        mContext = getRegistrationFragment().getActivity().getApplicationContext();

        createAccountPresenter = new CreateAccountPresenter(this);
        createAccountPresenter.registerListener();

        View view = inflater.inflate(R.layout.reg_fragment_create_account, container, false);
        ButterKnife.bind(this, view);
        textbox_input_field.setValidator(emailValidator);
        rl_reg_password_field_input_field.setValidator(passwordValidator);
        rl_reg_password_field_input_field.setErrorMessage(R.string.reg_EmptyField_ErrorMsg);
        initUI(view);
        handleABTestingFlow();
        handleUiState();
        mUser = new User(mContext);
        handleOrientation(view);
        mTrackCreateAccountTime = System.currentTimeMillis();
        return view;
    }

    private int passwordValidation(int strength) {
        RLog.d(RLog.EVENT_LISTENERS,
                "CreateAccountFragment register: NetworStateListener,strength " + strength);
        if (strength > 2) {
            passwordUiUpdate("Strong", 100, true, R.color.uid_green_level_30,
                    R.drawable.reg_password_strength_strong, 0, true);
            return 0;
        } else if (strength == 2) {
            passwordUiUpdate("Medium", 66, true, R.color.uid_pink_level_30,
                    R.drawable.reg_password_strength_medium, 0, false);
        } else if (strength == 1) {
            passwordUiUpdate("Weak", 33, false, R.color.uid_signal_red_level_15,
                    R.drawable.reg_password_strength_weak, R.string.reg_InValid_PwdErrorMsg, false);
        } else {
            passwordUiUpdate("Weak", 5, false, R.color.uid_signal_red_level_15,
                    R.drawable.reg_password_strength_weak, R.string.reg_InValid_PwdErrorMsg, false);
        }
        return 0;
    }

    private void passwordUiUpdate(String weak, int progress, boolean enabled, int color, int drawable, int invalidPasswordErrorId, boolean isPasswordValid) {
        passwordStrength.setText(weak);
        passwordStrength.setTextColor(ContextCompat.getColor(getContext(), color));
        progressBar.setProgress(progress);
        password_description.setBackgroundColor(ContextCompat.getColor(getContext(), color));
        progressBar.setProgressDrawable(getResources().getDrawable(drawable, null));
        isValidPassword = isPasswordValid;

        if (invalidPasswordErrorId != 0) {
            rl_reg_password_field_input_field.setErrorMessage(getResources().getString(invalidPasswordErrorId));
        }

        if (isValidPassword) {
            password_description.setVisibility(View.GONE);
            enableCreateButton();
        } else {
            password_description.setVisibility(View.VISIBLE);
            disableCreateButton();
        }
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onDestroy");
        createAccountPresenter.unRegisterListener();

        RLog.d(RLog.EVENT_LISTENERS,
                "CreateAccountFragment unregister: NetworStateListener,JANRAIN_INIT_SUCCESS");
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onConfigurationChanged");
        super.onConfigurationChanged(config);
        setCustomParams(config);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, createAccountBaseLayout, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    private void initUI(View view) {
        consumeTouch(view);
        RegUtility.linkifyTermsandCondition(mCbAcceptTerms, getRegistrationFragment().getParentActivity(), mTermsAndConditionClick);
        RegUtility.linkifyPhilipsNews(mCbMarketingOpt, getRegistrationFragment().getParentActivity(), mPhilipsNewsClick);
        String firstToKnow = "<b>" + mContext.getResources().getString(R.string.reg_Opt_In_Be_The_First) + "</b> ";
        mTvFirstToKnow.setText(Html.fromHtml(firstToKnow));
        ((RegistrationFragment) getParentFragment()).showKeyBoard();
        usernameUihandle();
        progressBarButton.setEnabled(false);
        mCbAcceptTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    mRegAccptTermsError.setError(mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
                } else {
                    mRegAccptTermsError.hideError();
                }
            }
        });
    }

    private void handleABTestingFlow() {
        final UIFlow abTestingUIFlow = RegUtility.getUiFlow();
        switch (abTestingUIFlow) {

            case FLOW_A:
                RLog.d(RLog.AB_TESTING, "UI Flow Type A");
                mCbMarketingOpt.setVisibility(View.VISIBLE);
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                        AppTagingConstants.REGISTRATION_CONTROL);
                break;
            case FLOW_B:

                RLog.d(RLog.AB_TESTING, "UI Flow Type B");
                mCbMarketingOpt.setVisibility(View.GONE);
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                        AppTagingConstants.REGISTRATION_SPLIT_SIGN_UP);
                break;
            case FLOW_C:
                RLog.d(RLog.AB_TESTING, "UI Flow Type C");
                mCbMarketingOpt.setVisibility(View.VISIBLE);
                mTvFirstToKnow.setVisibility(View.VISIBLE);
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                        AppTagingConstants.REGISTRATION_SOCIAL_PROOF);
                break;

            default:
                break;
        }
    }

    private void registerUserInfo() {
        mRegAccptTermsError.setVisibility(View.GONE);
        mEtFirstName.clearFocus();
        mEtLastName.clearFocus();
        reg_field_email.clearFocus();
        mEtPassword.clearFocus();
        showSpinner();
        if (FieldsValidator.isValidEmail(reg_field_email.getText().toString())) {
            mEmail = reg_field_email.getText().toString();
        } else {
            mEmail = FieldsValidator.getMobileNumber(reg_field_email.getText().toString());
        }
        createAccountPresenter.registerUserInfo(mUser, mEtFirstName.getText().toString(), mEtLastName.getText().toString(), mEmail
                , mEtPassword.getText().toString(), true, mCbMarketingOpt.isChecked());
    }


    private ClickableSpan mTermsAndConditionClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            getRegistrationFragment().getUserRegistrationUIEventListener().
                    onTermsAndConditionClick(getRegistrationFragment().getParentActivity());
        }
    };

    private ClickableSpan mPhilipsNewsClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            getRegistrationFragment().addPhilipsNewsFragment();
            trackPage(AppTaggingPages.PHILIPS_ANNOUNCEMENT);
        }
    };

    @Override
    public void trackCheckMarketing() {
        trackRemarketing();
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            if (mCbAcceptTerms.isChecked()) {
                trackActionForAcceptTermsOption(AppTagingConstants.ACCEPT_TERMS_OPTION_IN);
            } else {
                trackActionForAcceptTermsOption(AppTagingConstants.ACCEPT_TERMS_OPTION_OUT);
            }
        }
    }


    private void trackRemarketing() {
        final UIFlow abTestingUIFlow = RegUtility.getUiFlow();
        if (!abTestingUIFlow.equals(UIFlow.FLOW_B)) {
            if (mCbMarketingOpt.isChecked()) {
                trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_IN);
            } else {
                trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_OUT);
            }
        }
    }

    private void showSpinner() {
        progressBarButton.showProgressIndicator();
        disableCreateButton();
    }

    @Override
    public void hideSpinner() {
        ThreadUtils.postInMainThread(mContext, () -> {
            progressBarButton.hideProgressIndicator();
            enableCreateButton();
        });
    }

    @Override
    public void storeEMail() {
        RegPreferenceUtility.storePreference(mContext, mEmail, true);
    }

    @Override
    public void emailAlreadyUsed() {
        ThreadUtils.postInMainThread(mContext, () -> {
            textbox_input_field.showError();
        });
    }

    @Override
    public void registrtionFail() {
        ThreadUtils.postInMainThread(mContext, () -> {
            progressBarButton.hideProgressIndicator();
            disableCreateButton();
        });
    }

    @Override
    public void handleUiState() {
        if (networkUtility.isNetworkAvailable()) {
            mRegError.hideError();

        } else {
            mRegError.setError(mContext.getResources().getString(R.string.reg_NoNetworkConnection));
            scrollViewAutomatically(mRegError, mSvRootLayout);
        }
    }

    @Override
    public void launchMarketingAccountFragment() {
        addFragment(new MarketingAccountFragment());
        trackPage(AppTaggingPages.MARKETING_OPT_IN);
    }

    @Override
    public void launchMobileVerifyCodeFragment() {
        addFragment(new MobileVerifyCodeFragment());
        trackPage(AppTaggingPages.MOBILE_VERIFY_CODE);
    }

    @Override
    public void launchAccountActivateFragment() {
        addFragment(new AccountActivationFragment());
        trackPage(AppTaggingPages.ACCOUNT_ACTIVATION);
    }

    private void addFragment(Fragment fragment) {
        getRegistrationFragment().addFragment(fragment);
    }

    @Override
    public void launchWelcomeFragment() {
        getRegistrationFragment().replaceWelcomeFragmentOnLogin(new WelcomeFragment());
        trackPage(AppTaggingPages.WELCOME);
    }


    @Override
    public int getTitleResourceId() {
        return R.string.reg_RegCreateAccount_NavTitle;
    }

    @Override
    public void updateUiStatus() {
        if (networkUtility.isNetworkAvailable()) {
            enableCreateButton();
            mRegError.hideError();
        } else {
            disableCreateButton();
        }
        if (textbox_input_field.isShowingError()) {
            textbox_input_field.hideError();
        }
    }

    @Override
    public void emailError(int errorDesc) {
        textbox_input_field.setErrorMessage(mContext.getResources().getString(errorDesc));
    }

    @Override
    public void emailError(String errorDesc) {
        textbox_input_field.setErrorMessage(errorDesc);
    }

    @Override
    public String getEmail() {
        return mUser.getEmail();
    }

    @Override
    public long getTrackCreateAccountTime() {
        return mTrackCreateAccountTime;
    }

    @Override
    public void setTrackCreateAccountTime(long trackCreateAccountTime) {
        mTrackCreateAccountTime = trackCreateAccountTime;
    }

    @Override
    public void tractCreateActionStatus(String state, String key, String value) {
        trackActionStatus(state, key, value);
    }

    @Override
    public void scrollViewAutomaticallyToEmail() {
        ThreadUtils.postInMainThread(mContext, () -> scrollViewAutomatically(reg_field_email, mSvRootLayout));
    }

    @Override
    public void scrollViewAutomaticallyToError() {

        ThreadUtils.postInMainThread(mContext, () -> scrollViewAutomatically(mRegError, mSvRootLayout));
    }

    @OnClick(R2.id.btn_reg_switch_login)
    public void setSwitchToLogin() {
        getRegistrationFragment().addFragment(new SignInAccountFragment());
    }

    void enableCreateButton() {
        if (isValidPassword && isValidEmail && isValidFirstname && isValidLastame) {
            progressBarButton.setEnabled(true);
        }
    }

    void disableCreateButton() {
        progressBarButton.setEnabled(false);
    }


    private void usernameUihandle() {
        mEtFirstNameInputValidation.setValidator(new NameValidator());
        mEtLastNameInputValidation.setValidator(new NameValidator());
        mEtFirstNameInputValidation.setErrorMessage((R.string.reg_EmptyField_ErrorMsg));
        mEtLastNameInputValidation.setErrorMessage((R.string.reg_EmptyField_ErrorMsg));
        mEtFirstName.requestFocus();
        mEtFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    isValidFirstname = true;
                    enableCreateButton();
                } else {
                    isValidFirstname = false;
                    disableCreateButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEtLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    isValidLastame = true;
                    enableCreateButton();
                } else {
                    isValidLastame = false;
                    disableCreateButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R2.id.buttonsProgressIndicatorExtraWideIndeterminate)
    public void progressBar(){
        RLog.d(RLog.EVENT_LISTENERS,
                "CreateAccountFragment register: progresBarButton");
        RLog.d(RLog.ONCLICK, "CreateAccountFragment : Register Account");
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            if (mCbAcceptTerms.isChecked()) {
                registerUserInfo();
            } else {
                mRegAccptTermsError.setError(mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
            }
        } else {
            registerUserInfo();
        }
    }
}