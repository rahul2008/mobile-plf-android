
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional;

import android.content.*;
import android.content.res.Configuration;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.content.*;
import android.text.*;
import android.text.style.*;
import android.view.*;
import android.widget.Button;
import android.widget.*;
import android.widget.ProgressBar;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.*;
import com.philips.cdp.registration.app.tagging.*;
import com.philips.cdp.registration.configuration.*;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.customviews.*;
import com.philips.cdp.registration.ui.traditional.mobile.*;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.uid.view.widget.CheckBox;
import com.philips.platform.uid.view.widget.*;

import javax.inject.*;

import butterknife.*;

public class CreateAccountFragment extends RegistrationBaseFragment implements CreateAccountContract {

    @Inject
    NetworkUtility networkUtility;

    @BindView(R2.id.usr_createScreen_passwordValidation_Layout)
    LinearLayout usr_createScreen_passwordValidation_Layout;

    @BindView(R2.id.usr_createscreen_marketingmails_checkbox)
    CheckBox usr_createscreen_marketingmails_checkbox;

    @BindView(R2.id.usr_createscreen_termsandconditions_checkbox)
    CheckBox usr_createscreen_termsandconditions_checkbox;

    @BindView(R2.id.usr_createScreen_firstName_textField)
    ValidationEditText usr_createScreen_firstName_textField;

    @BindView(R2.id.usr_createScreen_lastName_textField)
    ValidationEditText usr_createScreen_lastName_textField;

    @BindView(R2.id.usr_createScreen_firstName_inputValidation)
    InputValidationLayout usr_createScreen_firstName_inputValidation;

    @BindView(R2.id.usr_createScreen_lastName_inputValidation)
    InputValidationLayout usr_createScreen_lastName_inputValidation;

    @BindView(R2.id.usr_createScreen_password_textField)
    ValidationEditText usr_createScreen_password_textField;

    @BindView(R2.id.usr_createScreen_password_inputValidationField)
    InputValidationLayout usr_createScreen_password_inputValidationField;

    @BindView(R2.id.usr_createscreen_error_view)
    XRegError usr_createscreen_error_view;

    @BindView(R2.id.usr_createscreen_termsandconditionsalert_view)
    XRegError usr_createscreen_termsandconditionsalert_view;

    @BindView(R2.id.usr_createScreen_rootLayout_scrollView)
    ScrollView usr_createScreen_rootLayout_scrollView;

    @BindView(R2.id.usr_createscreen_emailormobile_inputValidationLayout)
    InputValidationLayout usr_createscreen_emailormobile_inputValidationLayout;

    @BindView(R2.id.usr_createscreen_emailormobile_textfield)
    ValidationEditText usr_createscreen_emailormobile_textfield;

    @BindView(R2.id.usr_createscreen_password_progressbar)
    ProgressBar usr_createscreen_password_progressbar;

    @BindView(R2.id.usr_createscreen_passwordstrength_label)
    Label usr_createscreen_passwordstrength_label;

    @BindView(R2.id.usr_createscreen_passwordhint_label)
    Label usr_createscreen_passwordhint_label;

    @BindView(R2.id.usr_createscreen_switchtologin_button)
    Button usr_createscreen_switchtologin_button;

    @BindView(R2.id.usr_createscreen_create_button)
    ProgressBarButton usr_createscreen_create_button;

    @BindView(R2.id.usr_createScreen_baseLayout_LinearLayout)
    LinearLayout usr_createScreen_baseLayout_LinearLayout;

    private User user;

    private Context context;

    private long trackCreateAccountTime;

    private CreateAccountPresenter createAccountPresenter;

    private String emailString;

    boolean isValidEmail;

    boolean isValidPassword;

    boolean isValidFirstname;

    boolean isValidLastame;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onCreate");
        super.onCreate(savedInstanceState);
    }


    PasswordValidator passwordValidator = new PasswordValidator(new ValidPassword() {
        @Override
        public int getStrength(int strength) {
            usr_createScreen_passwordValidation_Layout.setVisibility(View.VISIBLE);
            return passwordValidation(strength);
        }
    });


    public LoginIdValidator loginIdValidator = new LoginIdValidator(new ValidLoginId() {
        @Override
        public int isValid(boolean valid) {
            isValidEmail = valid;
            enableCreateButton();
            return 0;
        }

        @Override
        public int isEmpty(boolean emptyField) {
            if (emptyField) {
                usr_createscreen_emailormobile_inputValidationLayout.setErrorMessage(
                        R.string.reg_EmptyField_ErrorMsg);
            } else {
                if (RegistrationHelper.getInstance().isMobileFlow()) {
                    usr_createscreen_emailormobile_inputValidationLayout.setErrorMessage(
                            R.string.reg_InvalidEmail_PhoneNumber_ErrorMsg);
                } else {
                    usr_createscreen_emailormobile_inputValidationLayout.setErrorMessage(
                            R.string.reg_InvalidEmailAdddress_ErrorMsg);
                }
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
                "CreateAccountFragment register: NetworkStateListener,JANRAIN_INIT_SUCCESS");
        context = getRegistrationFragment().getActivity().getApplicationContext();

        createAccountPresenter = new CreateAccountPresenter(this);
        createAccountPresenter.registerListener();

        View view = inflater.inflate(R.layout.reg_fragment_create_account, container, false);
        ButterKnife.bind(this, view);
        usr_createscreen_emailormobile_inputValidationLayout.setValidator(new InputValidationLayout.Validator() {
            @Override
            public boolean validate(CharSequence charSequence) {
                return false;
            }
        });
        usr_createScreen_password_inputValidationField.setValidator(passwordValidator);
        usr_createScreen_password_inputValidationField.setErrorMessage(R.string.reg_EmptyField_ErrorMsg);
        initUI(view);
        handleABTestingFlow();
        handleUiState();
        user = new User(context);
        handleOrientation(view);
        trackCreateAccountTime = System.currentTimeMillis();
        return view;
    }

    private int passwordValidation(int strength) {

        int strengthStrong=2;
        int strengthMedium=1;
        int stringthMeterNone=5;
        int strengthMeterWeak=33;
        int strengthMeterStrong=100;
        int strengthMeterMedium=66;

        RLog.d(RLog.EVENT_LISTENERS,
                "CreateAccountFragment register: NetworkStateListener,strength " + strength);
        if (strength > strengthStrong) {
            passwordUiUpdate(RegConstants.PASSWORD_STRENGTH_STRONG, strengthMeterStrong, true, R.color.uid_green_level_30,
                    R.drawable.reg_password_strength_strong, 0, true);
            return 0;
        } else if (strength == strengthStrong) {
            passwordUiUpdate(RegConstants.PASSWORD_STRENGTH_MEDIUIM, strengthMeterMedium, true, R.color.uid_pink_level_30,
                    R.drawable.reg_password_strength_medium, 0, false);
        } else if (strength == strengthMedium) {
            passwordUiUpdate(RegConstants.PASSWORD_STRENGTH_WEAK, strengthMeterWeak, false, R.color.uid_signal_red_level_15,
                    R.drawable.reg_password_strength_weak, R.string.reg_InValid_PwdErrorMsg, false);
        } else {
            passwordUiUpdate(RegConstants.PASSWORD_STRENGTH_WEAK, stringthMeterNone, false, R.color.uid_signal_red_level_15,
                    R.drawable.reg_password_strength_weak, R.string.reg_InValid_PwdErrorMsg, false);
        }
        return 0;
    }

    private void passwordUiUpdate(String weak, int progress, boolean enabled, int color, int drawable, int invalidPasswordErrorId, boolean isPasswordValid) {
        usr_createscreen_passwordstrength_label.setText(weak);
        usr_createscreen_passwordstrength_label.setTextColor(ContextCompat.getColor(getContext(), color));
        usr_createscreen_password_progressbar.setProgress(progress);
        usr_createscreen_passwordhint_label.setBackgroundColor(ContextCompat.getColor(getContext(), color));
        usr_createscreen_password_progressbar.setProgressDrawable(getResources().getDrawable(drawable, null));
        isValidPassword = isPasswordValid;

        if (invalidPasswordErrorId != 0) {
            usr_createScreen_password_inputValidationField.setErrorMessage(getResources().getString(invalidPasswordErrorId));
        }

        if (isValidPassword) {
            usr_createscreen_passwordhint_label.setVisibility(View.GONE);
            enableCreateButton();
        } else {
            usr_createscreen_passwordhint_label.setVisibility(View.VISIBLE);
            disableCreateButton();
        }
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onDestroy");
        createAccountPresenter.unRegisterListener();

        RLog.d(RLog.EVENT_LISTENERS,
                "CreateAccountFragment unregister: NetworkStateListener,JANRAIN_INIT_SUCCESS");
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
        applyParams(config, usr_createScreen_baseLayout_LinearLayout, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    private void initUI(View view) {
        consumeTouch(view);
        RegUtility.linkifyTermsandCondition(usr_createscreen_termsandconditions_checkbox, getRegistrationFragment().getParentActivity(), mTermsAndConditionClick);
        RegUtility.linkifyPhilipsNews(usr_createscreen_marketingmails_checkbox, getRegistrationFragment().getParentActivity(), mPhilipsNewsClick);
        ((RegistrationFragment) getParentFragment()).showKeyBoard();
        usernameUihandle();
        usr_createscreen_create_button.setEnabled(false);
        usr_createscreen_termsandconditions_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    usr_createscreen_termsandconditionsalert_view.setError(context.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
                } else {
                    usr_createscreen_termsandconditionsalert_view.hideError();
                }
            }
        });
    }

    private void handleABTestingFlow() {
        final UIFlow abTestingUIFlow = RegUtility.getUiFlow();
        switch (abTestingUIFlow) {

            case FLOW_A:
                RLog.d(RLog.AB_TESTING, "UI Flow Type A");
                usr_createscreen_marketingmails_checkbox.setVisibility(View.VISIBLE);
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                        AppTagingConstants.REGISTRATION_CONTROL);
                break;
            case FLOW_B:

                RLog.d(RLog.AB_TESTING, "UI Flow Type B");
                usr_createscreen_marketingmails_checkbox.setVisibility(View.GONE);
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                        AppTagingConstants.REGISTRATION_SPLIT_SIGN_UP);
                break;
            case FLOW_C:
                RLog.d(RLog.AB_TESTING, "UI Flow Type C");
                usr_createscreen_marketingmails_checkbox.setVisibility(View.VISIBLE);
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                        AppTagingConstants.REGISTRATION_SOCIAL_PROOF);
                break;

            default:
                break;
        }
    }

    private void registerUserInfo() {
        showSpinner();
        usr_createscreen_termsandconditionsalert_view.setVisibility(View.GONE);
        usr_createScreen_firstName_textField.clearFocus();
        usr_createScreen_lastName_textField.clearFocus();
        usr_createscreen_emailormobile_textfield.clearFocus();
        usr_createScreen_password_textField.clearFocus();
        usr_createscreen_error_view.hideError();
        if (FieldsValidator.isValidEmail(usr_createscreen_emailormobile_textfield.getText().toString())) {
            emailString = usr_createscreen_emailormobile_textfield.getText().toString();
        } else {
            emailString = FieldsValidator.getMobileNumber(usr_createscreen_emailormobile_textfield.getText().toString());
        }
        createAccountPresenter.registerUserInfo(user, usr_createScreen_firstName_textField.getText().toString(), usr_createScreen_lastName_textField.getText().toString(), emailString
                , usr_createScreen_password_textField.getText().toString(), true, usr_createscreen_marketingmails_checkbox.isChecked());
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
            if (usr_createscreen_termsandconditions_checkbox.isChecked()) {
                trackActionForAcceptTermsOption(AppTagingConstants.ACCEPT_TERMS_OPTION_IN);
            } else {
                trackActionForAcceptTermsOption(AppTagingConstants.ACCEPT_TERMS_OPTION_OUT);
            }
        }
    }


    private void trackRemarketing() {
        final UIFlow abTestingUIFlow = RegUtility.getUiFlow();
        if (!abTestingUIFlow.equals(UIFlow.FLOW_B)) {
            if (usr_createscreen_marketingmails_checkbox.isChecked()) {
                trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_IN);
            } else {
                trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_OUT);
            }
        }
    }

    private void showSpinner() {
        usr_createscreen_create_button.showProgressIndicator();
        disableCreateButton();
        usr_createscreen_switchtologin_button.setEnabled(false);
    }

    @Override
    public void hideSpinner() {
        ThreadUtils.postInMainThread(context, () -> {
            usr_createscreen_create_button.hideProgressIndicator();
            usr_createscreen_switchtologin_button.setEnabled(true);

            enableCreateButton();
        });
    }

    @Override
    public void storeEMail() {
        RegPreferenceUtility.storePreference(context, emailString, true);
    }

    @Override
    public void emailAlreadyUsed() {
        ThreadUtils.postInMainThread(context, () -> {
            usr_createscreen_emailormobile_inputValidationLayout.showError();
        });
    }

    @Override
    public void registrtionFail() {
        ThreadUtils.postInMainThread(context, () -> {
            usr_createscreen_create_button.hideProgressIndicator();
            usr_createscreen_switchtologin_button.setEnabled(true);
            disableCreateButton();
        });
    }

    @Override
    public void handleUiState() {
        if (networkUtility.isNetworkAvailable()) {
            usr_createscreen_error_view.hideError();

        } else {
            usr_createscreen_error_view.setError(context.getResources().getString(R.string.reg_NoNetworkConnection));
            scrollViewAutomatically(usr_createscreen_error_view, usr_createScreen_rootLayout_scrollView);
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
            usr_createscreen_error_view.hideError();
        } else {
            disableCreateButton();
        }
        if (usr_createscreen_emailormobile_inputValidationLayout.isShowingError()) {
            usr_createscreen_emailormobile_inputValidationLayout.hideError();
        }
    }

    @Override
    public void emailError(int errorDesc) {
        usr_createscreen_emailormobile_inputValidationLayout.setErrorMessage(context.getResources().getString(errorDesc));
    }

    @Override
    public void emailError(String errorDesc) {
        usr_createscreen_emailormobile_inputValidationLayout.setErrorMessage(errorDesc);
    }

    @Override
    public void genericError(String errorDesc) {
        usr_createscreen_error_view.setError(errorDesc);
        scrollViewAutomatically(usr_createscreen_error_view, usr_createScreen_rootLayout_scrollView);
    }

    @Override
    public void genericError(int errorDescID) {
        usr_createscreen_error_view.setError(context.getString(errorDescID));
        scrollViewAutomatically(usr_createscreen_error_view, usr_createScreen_rootLayout_scrollView);
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public long getTrackCreateAccountTime() {
        return trackCreateAccountTime;
    }

    @Override
    public void setTrackCreateAccountTime(long trackCreateAccountTime) {
        this.trackCreateAccountTime = trackCreateAccountTime;
    }

    @Override
    public void tractCreateActionStatus(String state, String key, String value) {
        trackActionStatus(state, key, value);
    }

    @Override
    public void scrollViewAutomaticallyToEmail() {
        ThreadUtils.postInMainThread(context, () -> scrollViewAutomatically(usr_createscreen_emailormobile_textfield, usr_createScreen_rootLayout_scrollView));
    }

    @Override
    public void scrollViewAutomaticallyToError() {

        ThreadUtils.postInMainThread(context, () -> scrollViewAutomatically(usr_createscreen_error_view, usr_createScreen_rootLayout_scrollView));
    }

    @OnClick(R2.id.usr_createscreen_switchtologin_button)
    public void setSwitchToLogin() {
        getRegistrationFragment().addFragment(new SignInAccountFragment());
    }

    void enableCreateButton() {
        if (isValidPassword && isValidEmail && isValidFirstname && isValidLastame) {
            usr_createscreen_create_button.setEnabled(true);
        }
    }

    void disableCreateButton() {
        usr_createscreen_create_button.setEnabled(false);
    }


    private void usernameUihandle() {
        usr_createScreen_firstName_inputValidation.setValidator(firstName -> firstName.length() > 0);
        usr_createScreen_lastName_inputValidation.setValidator(lastName -> lastName.length() > 0);
        usr_createScreen_firstName_inputValidation.setErrorMessage((R.string.reg_EmptyField_ErrorMsg));
        usr_createScreen_lastName_inputValidation.setErrorMessage((R.string.reg_EmptyField_ErrorMsg));
        usr_createScreen_firstName_textField.requestFocus();
        usr_createScreen_firstName_textField.addTextChangedListener(new TextWatcher() {
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

        usr_createScreen_lastName_textField.addTextChangedListener(new TextWatcher() {
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

    @OnClick(R2.id.usr_createscreen_create_button)
    public void progressBar() {
        RLog.d(RLog.EVENT_LISTENERS,
                "CreateAccountFragment register: progresBarButton");
        RLog.d(RLog.ONCLICK, "CreateAccountFragment : Register Account");
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            if (usr_createscreen_termsandconditions_checkbox.isChecked()) {
                registerUserInfo();
            } else {
                usr_createscreen_termsandconditionsalert_view.setError(context.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
            }
        } else {
            registerUserInfo();
        }
    }
}