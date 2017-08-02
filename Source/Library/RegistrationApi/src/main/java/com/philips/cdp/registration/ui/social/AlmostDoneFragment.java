
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.social;

import android.content.*;
import android.content.res.Configuration;
import android.os.*;
import android.text.style.*;
import android.view.*;
import android.widget.CheckBox;
import android.widget.*;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.*;
import com.philips.cdp.registration.app.tagging.*;
import com.philips.cdp.registration.configuration.*;
import com.philips.cdp.registration.dao.*;
import com.philips.cdp.registration.ui.customviews.*;
import com.philips.cdp.registration.ui.traditional.*;
import com.philips.cdp.registration.ui.traditional.mobile.*;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.uid.view.widget.*;

import javax.inject.*;

import butterknife.*;

public class AlmostDoneFragment extends RegistrationBaseFragment implements AlmostDoneContract,
        OnUpdateListener, XCheckBox.OnCheckedChangeListener {

    @BindView(R2.id.usr_almostDoneScreen_termsAndConditions_checkBox)
    CheckBox acceptTermsCheck;

    @BindView(R2.id.cb_reg_accept_terms_error)
    XRegError acceptTermserrorMessage;

    @BindView(R2.id.usr_almostDoneScreen_marketingMails_checkBox)
    CheckBox marketingOptCheck;

    @BindView(R2.id.reg_error_msg)
    XRegError errorMessage;

    @BindView(R2.id.rl_reg_email_field_inputValidationLayout)
    InputValidationLayout loginIdEditText;

    @BindView(R2.id.rl_reg_email_field)
    ValidationEditText rl_reg_email_field;

    @BindView(R2.id.rl_almost_email_label)
    Label emailTitle;

    @BindView(R2.id.usr_almostDoneScreen_continue_button)
    ProgressBarButton continueButton;

    @BindView(R2.id.sv_root_layout)
    ScrollView rootLayout;

    @BindView(R2.id.rl_reg_email_field_description)
    Label rl_reg_email_field_description;

    @Inject
    User mUser;

    private AlmostDonePresenter almostDonePresenter;

    private Context mContext;

    private Bundle mBundle;

    boolean isValidEmail;


    public EmailValidator emailValidator = new EmailValidator(new ValidEmail() {
        @Override
        public int isValid(boolean valid) {
            isValidEmail = valid;
            return 0;
        }

        @Override
        public int isEmpty(boolean emptyField) {
            if (emptyField) {
                loginIdEditText.setErrorMessage(R.string.reg_EmptyField_ErrorMsg);
            } else {
                loginIdEditText.setErrorMessage(R.string.reg_InvalidEmailAdddress_ErrorMsg);
            }
            isValidEmail = false;
            return 0;
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        URInterface.getComponent().inject(this);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onCreateView");
        mBundle = getArguments();
        if (null != mBundle) {
            trackAbtesting();
        }
        mContext = getRegistrationFragment().getActivity().getApplicationContext();
        almostDonePresenter = new AlmostDonePresenter(this, mUser);
        View view = inflater.inflate(R.layout.reg_fragment_social_almost_done, container, false);
        ButterKnife.bind(this, view);
        loginIdEditText.setValidator(emailValidator);
        initUI(view);
        almostDonePresenter.parseRegistrationInfo(mBundle);
        almostDonePresenter.updateUIControls();
        handleUiAcceptTerms();
        handleOrientation(view);
        return view;
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onDestroy");
        super.onDestroy();
        almostDonePresenter.cleanUp();
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onConfigurationChanged");
        setCustomParams(config);
    }

    @Override
    public void setViewParams(Configuration config, int width) {

        applyParams(config, marketingOptCheck, width);
        applyParams(config, errorMessage, width);
        applyParams(config, acceptTermserrorMessage, width);

    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    private void initUI(View view) {
        consumeTouch(view);

        acceptTermsCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    acceptTermserrorMessage.setError(mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
                } else {
                    acceptTermserrorMessage.hideError();
                }
            }
        });


        marketingOptCheck.setPadding(RegUtility.getCheckBoxPadding(mContext), marketingOptCheck.getPaddingTop(),
                marketingOptCheck.getPaddingRight(), marketingOptCheck.getPaddingBottom());
        RegUtility.linkifyTermsandCondition(acceptTermsCheck, getRegistrationFragment().getParentActivity(), mTermsAndConditionClick);
        updateReceiveMarketingViewStyle();

    }

    private void updateReceiveMarketingViewStyle() {
        RegUtility.linkifyPhilipsNews(marketingOptCheck, getRegistrationFragment().getParentActivity(), mPhilipsNewsClick);
    }

    @Override
    public void emailFieldHide() {
        rl_reg_email_field.setVisibility(View.GONE);
        emailTitle.setVisibility(View.GONE);
        continueButton.setEnabled(true);
        rl_reg_email_field_description.setText(mContext.getResources().getString(R.string.reg_almost_description2));
    }

    @Override
    public void showEmailField() {
        rl_reg_email_field.setVisibility(View.VISIBLE);
        emailTitle.setVisibility(View.VISIBLE);
        rl_reg_email_field_description.setVisibility(View.VISIBLE);
        rl_reg_email_field_description.setText(mContext.getResources().getString(R.string.reg_almost_description1));

    }

    private ClickableSpan mTermsAndConditionClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            getRegistrationFragment().getUserRegistrationUIEventListener()
                    .onTermsAndConditionClick(getRegistrationFragment().getParentActivity());
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
    public void handleUiAcceptTerms() {
        almostDonePresenter.handleAcceptTermsAndReceiveMarketingOpt();
    }

    @Override
    public void hideAcceptTermsView() {
        acceptTermsCheck.setVisibility(View.GONE);
    }

    @Override
    public void updateTermsAndConditionView() {
        hideAcceptTermsAndConditionContainer();
    }

    @Override
    public void showMarketingOptCheck() {
        marketingOptCheck.setVisibility(View.VISIBLE);
        rl_reg_email_field_description.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideMarketingOptCheck() {
        marketingOptCheck.setVisibility(View.GONE);
        if(rl_reg_email_field.getVisibility() != View.VISIBLE){
            rl_reg_email_field_description.setVisibility(View.GONE);
        }
    }

    private void hideAcceptTermsAndConditionContainer() {
        acceptTermsCheck.setChecked(true);
        acceptTermsCheck.setVisibility(View.GONE);
    }

    @Override
    public void updateReceiveMarketingView() {
        marketingOptCheck.setVisibility(View.GONE);
    }

    @Override
    public void updateABTestingUIFlow() {
        final UIFlow abTestingUIFlow = RegUtility.getUiFlow();

        switch (abTestingUIFlow) {
            case FLOW_A:
                RLog.d(RLog.AB_TESTING, "UI Flow Type A");
                acceptTermsCheck.setVisibility(View.VISIBLE);
                break;

            case FLOW_B:
                RLog.d(RLog.AB_TESTING, "UI Flow Type B");
                acceptTermsCheck.setVisibility(View.VISIBLE);
                marketingOptCheck.setVisibility(View.GONE);
                break;

            case FLOW_C:
                RLog.d(RLog.AB_TESTING, "UI Flow Type C");
                acceptTermsCheck.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void validateEmailFieldUI() {
        if ((loginIdEditText.isShown() && isValidEmail) ||
                (loginIdEditText.getVisibility() != View.VISIBLE)) {
            continueButton.setEnabled(true);
        }
        errorMessage.hideError();
    }

    @Override
    public void enableContinueBtn() {
        continueButton.setEnabled(true);
        errorMessage.hideError();
    }

    @Override
    public void handleOfflineMode() {
        errorMessage.setError(getString(R.string.reg_NoNetworkConnection));
        continueButton.setEnabled(false);
        scrollViewAutomatically(errorMessage, rootLayout);
    }

    @Override
    public void showMarketingOptSpinner() {
        marketingOptCheck.setEnabled(false);
        continueButton.setEnabled(false);
    }

    @Override
    public void hideMarketingOptSpinner() {
        marketingOptCheck.setEnabled(true);
        continueButton.setEnabled(true);
    }

    @OnClick(R2.id.usr_almostDoneScreen_continue_button)
    public void continueButtonClicked() {
        RLog.d(RLog.ONCLICK, "AlmostDoneFragment : Continue");
        loginIdEditText.clearFocus();
        if (rl_reg_email_field.getVisibility()== View.VISIBLE && !isValidEmail) {
            if (rl_reg_email_field.getText().length() == 0) {
                loginIdEditText.setErrorMessage(R.string.reg_EmptyField_ErrorMsg);
            } else {
                loginIdEditText.setErrorMessage(R.string.reg_InvalidEmailAdddress_ErrorMsg);
            }
            loginIdEditText.showError();
            return;
        }
        if (mBundle == null) {
            almostDonePresenter.handleTraditionalTermsAndCondition();
            return;
        }
        almostDonePresenter.handleSocialTermsAndCondition();
    }

    @Override
    public String getMobileNumber() {
        return FieldsValidator.getMobileNumber(rl_reg_email_field.getText().toString());
    }

    @Override
    public boolean isAcceptTermsContainerVisible() {
        return acceptTermsCheck.getVisibility() == View.VISIBLE;
    }

    @Override
    public boolean isAcceptTermsChecked() {
        return acceptTermsCheck.isChecked();
    }

    @Override
    public void showTermsAndConditionError() {
        acceptTermserrorMessage.setError(mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
    }

    @Override
    public void handleAcceptTermsTrue() {
        almostDonePresenter.storeEmailOrMobileInPreference();
        trackActionForAcceptTermsOption(AppTagingConstants.ACCEPT_TERMS_OPTION_IN);
        launchWelcomeFragment();
    }

    @Override
    public void storePreference(String emailOrMobileNumber) {
        RegPreferenceUtility.storePreference(mContext, emailOrMobileNumber, true);
    }

    private void trackMultipleActions() {
        trackABTestingUIFlow();
        trackTermsAndConditionAccepted();
    }

    private void trackTermsAndConditionAccepted() {
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            if (acceptTermsCheck.isChecked()) {
                trackActionForAcceptTermsOption(AppTagingConstants.ACCEPT_TERMS_OPTION_IN);
            } else {
                trackActionForAcceptTermsOption(AppTagingConstants.ACCEPT_TERMS_OPTION_OUT);
            }
        }
    }

    private void trackABTestingUIFlow() {
        final UIFlow abTestingUIFlow = RegUtility.getUiFlow();
        if (!abTestingUIFlow.equals(UIFlow.FLOW_B)) {
            trackMarketingOpt();
        }
    }

    @Override
    public void trackMarketingOpt() {
        if (marketingOptCheck.isChecked()) {
            trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_IN);
        } else {
            trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_OUT);
        }
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_SigIn_TitleTxt;
    }

    @Override
    public void phoneNumberAlreadyInuseError() {
        loginIdEditText.setErrorMessage(mContext.getResources().getString(R.string.reg_CreateAccount_Using_Phone_Alreadytxt));
    }

    @Override
    public void emailAlreadyInuseError() {
        loginIdEditText.setErrorMessage(mContext.getResources().getString(R.string.reg_EmailAlreadyUsed_TxtFieldErrorAlertMsg));
    }

    @Override
    public void showLoginFailedError() {
        loginIdEditText.showError();
        scrollViewAutomatically(loginIdEditText, rootLayout);
    }

    @Override
    public void addMergeAccountFragment() {
        getRegistrationFragment().addFragment(new MergeAccountFragment());
        trackPage(AppTaggingPages.MERGE_ACCOUNT);
    }

    @Override
    public void handleContinueSocialProvider() {
        RLog.i(RLog.CALLBACK, "AlmostDoneFragment : onContinueSocialProviderLoginSuccess");
        trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                AppTagingConstants.SUCCESS_USER_CREATION);
        trackMultipleActions();
        handleABTestingFlow();
        hideMarketingOptSpinner();
    }

    private void handleABTestingFlow() {
        final UIFlow abTestingUIFlow = RegUtility.getUiFlow();
        switch (abTestingUIFlow) {
            case FLOW_A:
                RLog.d(RLog.AB_TESTING, "UI Flow Type A");
                if (almostDonePresenter.isEmailVerificationStatus()) {
                    launchWelcomeFragment();
                } else {
                    launchAccountActivateFragment();
                }
                break;
            case FLOW_B:
                RLog.d(RLog.AB_TESTING, "UI Flow Type B");
                launchMarketingAccountFragment();
                break;
            case FLOW_C:
                RLog.d(RLog.AB_TESTING, "UI Flow Type C");
                if (almostDonePresenter.isEmailVerificationStatus()) {
                    launchWelcomeFragment();
                } else {
                    launchAccountActivateFragment();
                }
                break;
            default:
                break;
        }
    }

    public void clearUserData() {
        if (null != acceptTermsCheck && !acceptTermsCheck.isChecked() &&
                RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            almostDonePresenter.handleClearUserData();
        }
    }

    private void launchMarketingAccountFragment() {
        getRegistrationFragment().addFragment(new MarketingAccountFragment());
        trackPage(AppTaggingPages.MARKETING_OPT_IN);
    }

    private void launchAccountActivateFragment() {
        trackPage(AppTaggingPages.ACCOUNT_ACTIVATION);
        if (almostDonePresenter.isValidEmail()) {
            getRegistrationFragment().addFragment(new AccountActivationFragment());
        } else {
            getRegistrationFragment().addFragment(new MobileVerifyCodeFragment());
        }
    }

    public void launchWelcomeFragment() {
        getRegistrationFragment().addWelcomeFragmentOnVerification();
        trackPage(AppTaggingPages.WELCOME);
    }

    @Override
    public void displayNameErrorMessage(UserRegistrationFailureInfo userRegistrationFailureInfo, String displayName) {
        loginIdEditText.setErrorMessage(userRegistrationFailureInfo.getDisplayNameErrorMessage());
        errorMessage.setError(userRegistrationFailureInfo.getErrorDescription() + ".\n'"
                + displayName + "' "
                + userRegistrationFailureInfo.getDisplayNameErrorMessage());
    }

    @Override
    public void emailErrorMessage(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        loginIdEditText.setErrorMessage(userRegistrationFailureInfo.getEmailErrorMessage());
    }

    @Override
    public void marketingOptCheckDisable() {
        marketingOptCheck.setOnCheckedChangeListener(null);
        marketingOptCheck.setChecked(!marketingOptCheck.isChecked());
        errorMessage.setError(getString(R.string.reg_NoNetworkConnection));
    }

    @Override
    public void handleUpdateUser() {
        errorMessage.hideError();
        almostDonePresenter.updateUser(marketingOptCheck.isChecked());
    }

    @Override
    public void onCheckedChanged(View view, boolean isChecked) {
        almostDonePresenter.handleUpdateMarketingOpt();
    }

    @Override
    public void failedToConnectToServer() {
        errorMessage.setError(mContext.getResources().getString(R.string.reg_JanRain_Server_Connection_Failed));
    }

    @Override
    public void replaceWithHomeFragment() {
        if (getRegistrationFragment() != null) {
            getRegistrationFragment().replaceWithHomeFragment();
        }
    }

    @Override
    public void onUpdate() {
        almostDonePresenter.updateUIControls();
    }

    @Override
    public void updateMarketingOptFailedError() {
        marketingOptCheck.setOnCheckedChangeListener(null);
        marketingOptCheck.setChecked(!marketingOptCheck.isChecked());
    }

    @Override
    public void hideErrorMessage() {
        acceptTermserrorMessage.setVisibility(View.GONE);
    }

    public boolean getPreferenceStoredState(String emailOrMobileNumber) {
        return RegPreferenceUtility.getStoredState(mContext, emailOrMobileNumber);
    }

    @Override
    public boolean isMarketingOptChecked() {
        return marketingOptCheck.isChecked();
    }

    private void trackAbtesting() {
        final UIFlow abTestingFlow = RegUtility.getUiFlow();

        switch (abTestingFlow) {
            case FLOW_A:
                RLog.d(RLog.AB_TESTING, "UI Flow Type A");
                AppTagging.trackAction(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                        AppTagingConstants.REGISTRATION_CONTROL);
                break;

            case FLOW_B:
                RLog.d(RLog.AB_TESTING, "UI Flow Type B");
                AppTagging.trackAction(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                        AppTagingConstants.REGISTRATION_SPLIT_SIGN_UP);
                break;
            case FLOW_C:
                RLog.d(RLog.AB_TESTING, "UI Flow Type C");
                AppTagging.trackAction(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                        AppTagingConstants.REGISTRATION_SOCIAL_PROOF);
                break;
            default:
                break;
        }
    }
}