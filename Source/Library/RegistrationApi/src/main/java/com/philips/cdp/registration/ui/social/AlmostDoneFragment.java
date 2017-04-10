
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.social;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.philips.cdp.registration.B;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.app.tagging.AppTaggingPages;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.ui.customviews.LoginIdEditText;
import com.philips.cdp.registration.ui.customviews.OnUpdateListener;
import com.philips.cdp.registration.ui.customviews.XButton;
import com.philips.cdp.registration.ui.customviews.XCheckBox;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.traditional.AccountActivationFragment;
import com.philips.cdp.registration.ui.traditional.MarketingAccountFragment;
import com.philips.cdp.registration.ui.traditional.RegistrationBaseFragment;
import com.philips.cdp.registration.ui.traditional.mobile.MobileVerifyCodeFragment;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegPreferenceUtility;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.cdp.registration.ui.utils.UIFlow;
import com.philips.cdp.registration.ui.utils.URInterface;

import javax.inject.Inject;

import butterfork.Bind;
import butterfork.ButterFork;
import butterfork.OnClick;

public class AlmostDoneFragment extends RegistrationBaseFragment implements AlmostDoneContract,
        OnUpdateListener, XCheckBox.OnCheckedChangeListener {

    @Bind(B.id.tv_reg_sign_in_with)
    TextView signInWithTextView;

    @Bind(B.id.ll_reg_almost_done)
    LinearLayout almostDoneContainer;

    @Bind(B.id.fl_reg_receive_philips_news)
    FrameLayout periodicOffersCheck;

    @Bind(B.id.ll_reg_accept_terms)
    LinearLayout acceptTermsContainer;

    @Bind(B.id.cb_reg_accept_terms)
    XCheckBox acceptTermsCheck;

    @Bind(B.id.cb_reg_accept_terms_error)
    XRegError acceptTermserrorMessage;

    @Bind(B.id.rl_reg_btn_continue_container)
    RelativeLayout continueBtnContainer;

    @Bind(B.id.cb_reg_receive_philips_news)
    XCheckBox marketingOptCheck;

    @Bind(B.id.reg_error_msg)
    XRegError errorMessage;

    @Bind(B.id.rl_reg_email_field)
    LoginIdEditText loginIdEditText;

    @Bind(B.id.reg_btn_continue)
    XButton continueButton;

    @Bind(B.id.pb_reg_marketing_opt_spinner)
    ProgressBar marketingProgressBar;

    @Bind(B.id.sv_root_layout)
    ScrollView rootLayout;

    @Bind(B.id.tv_join_now)
    TextView joinNowView;

    @Bind(B.id.reg_view_accep_terms_line)
    View acceptTermsViewLine;

    @Bind(B.id.tv_reg_accept_terms)
    TextView acceptTermsView;

    @Bind(B.id.tv_reg_first_to_know)
    TextView firstToKnowView;

    @Bind(B.id.tv_reg_philips_news)
    TextView receivePhilipsNewsView;

    @Bind(B.id.reg_view_line)
    View fieldViewLine;

    @Bind(B.id.reg_recieve_email_line)
    View receivePhilipsNewsLineView;

    @Inject
    User mUser;

    private AlmostDonePresenter almostDonePresenter;

    private Context mContext;

    private Bundle mBundle;

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
        almostDonePresenter = new AlmostDonePresenter(this,mUser);
        View view = inflater.inflate(R.layout.reg_fragment_social_almost_done, container, false);
        ButterFork.bind(this, view);
        initUI(view);
        almostDonePresenter.parseRegistrationInfo(mBundle);
        almostDonePresenter.updateUIControls();
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
        applyParams(config, signInWithTextView, width);
        applyParams(config, almostDoneContainer, width);
        applyParams(config, periodicOffersCheck, width);
        applyParams(config, continueBtnContainer, width);
        applyParams(config, errorMessage, width);
        applyParams(config, acceptTermserrorMessage, width);
        applyParams(config, acceptTermsContainer, width);
        applyParams(config, firstToKnowView, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    private void initUI(View view) {
        consumeTouch(view);
        marketingOptCheck.setPadding(RegUtility.getCheckBoxPadding(mContext), marketingOptCheck.getPaddingTop(),
                marketingOptCheck.getPaddingRight(), marketingOptCheck.getPaddingBottom());
        RegUtility.linkifyTermsandCondition(acceptTermsView, getRegistrationFragment().getParentActivity(), mTermsAndConditionClick);
        updateReceiveMarketingViewStyle();
        initListener();
        handleUiAcceptTerms();
    }

    private void initListener() {
        marketingOptCheck.setOnCheckedChangeListener(this);
        loginIdEditText.setOnUpdateListener(this);
        marketingProgressBar.setClickable(false);
        marketingProgressBar.setEnabled(true);
    }

    private void updateReceiveMarketingViewStyle() {
        RegUtility.linkifyPhilipsNews(receivePhilipsNewsView, getRegistrationFragment().getParentActivity(), mPhilipsNewsClick);
        String sourceString = mContext.getResources().getString(R.string.reg_Opt_In_Join_Now) ;
        String updateJoinNowText =  " " + "<b>" + mContext.getResources().getString(R.string.reg_Opt_In_Over_Peers) + "</b> ";
        sourceString = String.format(sourceString, updateJoinNowText);
        joinNowView.setText(Html.fromHtml(sourceString));
    }

    @Override
    public void emailFieldHide() {
        loginIdEditText.setVisibility(View.GONE);
        continueButton.setEnabled(true);
    }

    @Override
    public void showEmailField() {
        fieldViewLine.setVisibility(View.VISIBLE);
        loginIdEditText.setVisibility(View.VISIBLE);
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
        joinNowView.setVisibility(View.GONE);
        almostDonePresenter.handleAcceptTermsAndReceiveMarketingOpt();
    }

    @Override
    public void hideAcceptTermsView() {
        acceptTermsViewLine.setVisibility(View.GONE);
        acceptTermsContainer.setVisibility(View.GONE);
    }

    @Override
    public void updateTermsAndConditionView() {
        hideAcceptTermsAndConditionContainer();
    }

    @Override
    public void showMarketingOptCheck() {
        periodicOffersCheck.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideMarketingOptCheck() {
        periodicOffersCheck.setVisibility(View.GONE);
    }

    private void hideAcceptTermsAndConditionContainer() {
        acceptTermsCheck.setChecked(true);
        fieldViewLine.setVisibility(View.GONE);
        joinNowView.setVisibility(View.GONE);
        acceptTermsContainer.setVisibility(View.GONE);
    }

    @Override
    public void updateReceiveMarketingView() {
        periodicOffersCheck.setVisibility(View.GONE);
        fieldViewLine.setVisibility(View.GONE);
        joinNowView.setVisibility(View.GONE);
        acceptTermsViewLine.setVisibility(View.GONE);
    }

    @Override
    public void updateABTestingUIFlow() {
        final UIFlow abTestingUIFlow = RegUtility.getUiFlow();

        switch (abTestingUIFlow){
            case FLOW_A:
                RLog.d(RLog.AB_TESTING, "UI Flow Type A");
                acceptTermsContainer.setVisibility(View.VISIBLE);
                joinNowView.setVisibility(View.GONE);
                break;

            case FLOW_B:
                RLog.d(RLog.AB_TESTING, "UI Flow Type B");
                acceptTermsContainer.setVisibility(View.VISIBLE);
                periodicOffersCheck.setVisibility(View.GONE);
                receivePhilipsNewsLineView.setVisibility(View.GONE);
                joinNowView.setVisibility(View.GONE);
                break;

            case FLOW_C:
                RLog.d(RLog.AB_TESTING, "UI Flow Type C");
                String firstToKnow = "<b>" + mContext.getResources().getString(R.string.reg_Opt_In_Be_The_First) + "</b> ";
                firstToKnowView.setText(Html.fromHtml(firstToKnow));
                firstToKnowView.setVisibility(View.VISIBLE);
                acceptTermsContainer.setVisibility(View.VISIBLE);
                joinNowView.setVisibility(View.VISIBLE);
                break;
            default:break;
        }
    }

    @Override
    public void validateEmailFieldUI() {
        if ((loginIdEditText.isShown() && loginIdEditText.isValidEmail()) ||
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
        marketingProgressBar.setVisibility(View.VISIBLE);
        continueButton.setEnabled(false);
    }

    @Override
    public void hideMarketingOptSpinner() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                marketingOptCheck.setEnabled(true);
                marketingProgressBar.setVisibility(View.INVISIBLE);
                continueButton.setEnabled(true);
            }
        });
    }

    @OnClick(B.id.reg_btn_continue)
    public void continueButtonClicked() {
        RLog.d(RLog.ONCLICK, "AlmostDoneFragment : Continue");
        loginIdEditText.clearFocus();
        if (loginIdEditText.isShown() && !loginIdEditText.isValidEmail()) return;
        if (mBundle == null) {
            almostDonePresenter.handleTraditionalTermsAndCondition();
            return;
        }
        almostDonePresenter.handleSocialTermsAndCondition();
    }

    @Override
    public String getMobileNumber() {
        return FieldsValidator.getMobileNumber(loginIdEditText.getEmailId().trim());
    }

    @Override
    public boolean isAcceptTermsContainerVisible() {
        return acceptTermsContainer.getVisibility() == View.VISIBLE;
    }

    @Override
    public boolean isAcceptTermsChecked(){
        return acceptTermsCheck.isChecked();
    }

    @Override
    public void showTermsAndConditionError(){
        acceptTermserrorMessage.setError(mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
    }

    @Override
    public void handleAcceptTermsTrue() {
        almostDonePresenter.storeEmailOrMobileInPreference();
        trackActionForAcceptTermsOption(AppTagingConstants.ACCEPT_TERMS_OPTION_IN);
        launchWelcomeFragment();
    }

    @Override
    public void storePreference(String emailOrMobileNumber){
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                RegPreferenceUtility.storePreference(mContext, emailOrMobileNumber, true);
            }
        });
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
    public void phoneNumberAlreadyInuseError(){
        loginIdEditText.setErrDescription(mContext.getResources().getString(R.string.reg_CreateAccount_Using_Phone_Alreadytxt));
    }

    @Override
    public void emailAlreadyInuseError(){
        loginIdEditText.setErrDescription(mContext.getResources().getString(R.string.reg_EmailAlreadyUsed_TxtFieldErrorAlertMsg));
    }

    @Override
    public void showLoginFailedError() {
        loginIdEditText.showInvalidAlert();
        loginIdEditText.showErrPopUp();
        scrollViewAutomatically(loginIdEditText, rootLayout);
    }

    @Override
    public void addMergeAccountFragment() {
    handleOnUIThread(new Runnable() {
        @Override
        public void run() {
            getRegistrationFragment().addFragment(new MergeAccountFragment());
            trackPage(AppTaggingPages.MERGE_ACCOUNT);
        }
     });
    }

    @Override
    public void handleContinueSocialProvider() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                RLog.i(RLog.CALLBACK, "AlmostDoneFragment : onContinueSocialProviderLoginSuccess");
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                        AppTagingConstants.SUCCESS_USER_CREATION);
                trackMultipleActions();
                handleABTestingFlow();
                hideMarketingOptSpinner();
            }
        });
    }

    private void handleABTestingFlow() {
        final UIFlow abTestingUIFlow = RegUtility.getUiFlow();
        switch (abTestingUIFlow){
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
            default:break;
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
    public void displayNameErrorMessage(UserRegistrationFailureInfo userRegistrationFailureInfo,String displayName){
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                loginIdEditText.setErrDescription(userRegistrationFailureInfo.getDisplayNameErrorMessage());
                loginIdEditText.showInvalidAlert();
                errorMessage.setError(userRegistrationFailureInfo.getErrorDescription() + ".\n'"
                        + displayName + "' "
                        + userRegistrationFailureInfo.getDisplayNameErrorMessage());
            }
        });
    }

    @Override
    public void emailErrorMessage(UserRegistrationFailureInfo userRegistrationFailureInfo){
     handleOnUIThread(new Runnable() {
        @Override
        public void run() {
            loginIdEditText.setErrDescription(userRegistrationFailureInfo.getEmailErrorMessage());
            loginIdEditText.showInvalidAlert();
            loginIdEditText.showErrPopUp();
        }
    });
    }

    @Override
    public void marketingOptCheckDisable() {
        marketingOptCheck.setOnCheckedChangeListener(null);
        marketingOptCheck.setChecked(!marketingOptCheck.isChecked());
        marketingOptCheck.setOnCheckedChangeListener(this);
        errorMessage.setError(getString(R.string.reg_NoNetworkConnection));
    }

    @Override
    public void handleUpdateUser() {
        errorMessage.hideError();
        showMarketingOptSpinner();
        almostDonePresenter.updateUser(marketingOptCheck.isChecked());
    }

    @Override
    public void onCheckedChanged(View view, boolean isChecked) {
        almostDonePresenter.handleUpdateMarketingOpt();
    }

    @Override
    public void failedToConnectToServer(){
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                errorMessage.setError(mContext.getResources().getString(R.string.reg_JanRain_Server_Connection_Failed));
            }
        });
    }

    @Override
    public void replaceWithHomeFragment() {
     handleOnUIThread(new Runnable() {
        @Override
        public void run() {
            if (getRegistrationFragment() != null) {
                getRegistrationFragment().replaceWithHomeFragment();
            }
        }
     });
    }

    @Override
    public void onUpdate() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                almostDonePresenter.updateUIControls();
            }
        });
    }

    @Override
    public void updateMarketingOptFailedError(){
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                marketingOptCheck.setOnCheckedChangeListener(null);
                marketingOptCheck.setChecked(!marketingOptCheck.isChecked());
                marketingOptCheck.setOnCheckedChangeListener(AlmostDoneFragment.this);
            }
        });
    }

    @Override
    public void hideErrorMessage(){
        acceptTermserrorMessage.setVisibility(View.GONE);
    }

    public boolean getPreferenceStoredState(String emailOrMobileNumber){
        return RegPreferenceUtility.getStoredState(mContext, emailOrMobileNumber);
    }

    @Override
    public boolean isMarketingOptChecked(){
        return marketingOptCheck.isChecked();
    }

    private void trackAbtesting() {
        final UIFlow abTestingFlow = RegUtility.getUiFlow();

        switch (abTestingFlow){
            case FLOW_A :
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
            default:break;
        }
    }
}
