
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
import com.philips.cdp.registration.app.tagging.AppTaggingPages;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
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
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegPreferenceUtility;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.cdp.registration.ui.utils.UIFlow;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import butterfork.Bind;
import butterfork.ButterFork;
import butterfork.OnClick;

import static com.philips.cdp.registration.ui.traditional.LogoutFragment.BAD_RESPONSE_ERROR_CODE;

public class AlmostDoneFragment extends RegistrationBaseFragment implements AlmostDoneContract,
        OnUpdateListener, SocialProviderLoginHandler,UpdateUserDetailsHandler, XCheckBox.OnCheckedChangeListener {

    private static final int LOGIN_FAILURE = -1;

    private final static int EMAIL_ADDRESS_ALREADY_USE_CODE = 390;

    @Inject
    NetworkUtility networkUtility;

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
    XRegError errorMessage;

    @Bind(B.id.rl_reg_btn_continue_container)
    RelativeLayout continueBtnContainer;

    @Bind(B.id.cb_reg_receive_philips_news)
    XCheckBox remarketingOptCheck;

    @Bind(B.id.reg_error_msg)
    XRegError mRegError;

    @Bind(B.id.rl_reg_email_field)
    LoginIdEditText mEtEmail;

    @Bind(B.id.reg_btn_continue)
    XButton mBtnContinue;

    @Bind(B.id.pb_reg_marketing_opt_spinner)
    ProgressBar mPbSpinner;

    @Bind(B.id.sv_root_layout)
    ScrollView mSvRootLayout;

    @Bind(B.id.tv_join_now)
    TextView mJoinNow;

    @Bind(B.id.reg_view_accep_terms_line)
    View mViewAcceptTermsLine;

    @Bind(B.id.tv_reg_accept_terms)
    TextView acceptTermsView;

    private User mUser;

    @Bind(B.id.tv_reg_first_to_know)
    TextView mTvFirstToKnow;

    @Bind(B.id.tv_reg_philips_news)
    TextView receivePhilipsNewsView;

    @Bind(B.id.reg_view_line)
    View fieldViewLine;

   /* @Bind(B.id.tv_join_now)
    TextView marketingJoinNowView;*/

    @Bind(B.id.reg_recieve_email_line)
    View receivePhilipsNewsLineView;

    private AlmostDonePresenter almostDonePresenter;

    private String mGivenName;

    private String mDisplayName;

    private String mFamilyName;

    private String mEmail;

    private String mProvider;

    private boolean isEmailExist;

    private String mRegistrationToken;

    private Context mContext;

    private Bundle mBundle;

    private boolean isSavedCBTermsChecked;

    private boolean isSavedCbAcceptTermsChecked;

    private boolean isTermsAndConditionVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onCreateView");
        almostDonePresenter = new AlmostDonePresenter(this);
        parseRegistrationInfo();
        RLog.i(RLog.EVENT_LISTENERS,
                "AlmostDoneFragment register: NetworStateListener,JANRAIN_INIT_SUCCESS");
        View view = inflater.inflate(R.layout.reg_fragment_social_almost_done, container, false);
        ButterFork.bind(this, view);
        initUI(view);
        updateUiStatus();
        handleOrientation(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onDestroy");
        RLog.i(RLog.EVENT_LISTENERS,
                "AlmostDoneFragment unregister: NetworStateListener,JANRAIN_INIT_SUCCESS");
        super.onDestroy();
        almostDonePresenter.cleanUp();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onDetach");
    }

    private Bundle mSavedBundle;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mSavedBundle = outState;
        super.onSaveInstanceState(mSavedBundle);
        if (acceptTermsCheck != null) {
            if (remarketingOptCheck.isChecked()) {
                isSavedCBTermsChecked = true;
                mSavedBundle.putBoolean("isSavedCBTermsChecked", isSavedCBTermsChecked);
                mSavedBundle.putString("savedCBTerms", mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
            }
        }
        if (acceptTermsCheck != null) {
            if (acceptTermsCheck.isChecked()) {
                isSavedCbAcceptTermsChecked = true;
                mSavedBundle.putBoolean("isSavedCbAcceptTermsChecked", isSavedCbAcceptTermsChecked);
                mSavedBundle.putString("savedCbAcceptTerms", mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
            }
        }
        if (errorMessage != null) {
            if (errorMessage.getVisibility() == View.VISIBLE) {
                isTermsAndConditionVisible = true;
                mSavedBundle.putBoolean("isTermsAndConditionVisible", isTermsAndConditionVisible);
                mSavedBundle.putString("saveTermsAndConditionErrText", mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
            }
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("isSavedCBTermsChecked")) {
                remarketingOptCheck.setChecked(true);
            }
            if (savedInstanceState.getBoolean("isSavedCbAcceptTermsChecked")) {
                acceptTermsCheck.setChecked(true);
            }
            if (savedInstanceState.getString("saveTermsAndConditionErrText") != null && savedInstanceState.getBoolean("isTermsAndConditionVisible")) {
                errorMessage.setError(savedInstanceState.getString("saveTermsAndConditionErrText"));
            }
        }
        mSavedBundle = null;
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
        applyParams(config, mRegError, width);
        applyParams(config, errorMessage, width);
        applyParams(config, acceptTermsContainer, width);
        applyParams(config, mTvFirstToKnow, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    private void parseRegistrationInfo() {
        mBundle = getArguments();
        if (null != mBundle) {
            try {
                if(mBundle.getString(RegConstants.SOCIAL_TWO_STEP_ERROR)!=null){
                     trackAbtesting();
                }

                if(mBundle.getString(RegConstants.SOCIAL_TWO_STEP_ERROR)!=null) {
                    JSONObject mPreRegJson = new JSONObject(mBundle.getString(RegConstants.SOCIAL_TWO_STEP_ERROR));
                    performSocialTwoStepError(mPreRegJson);
                }
                if (null == mGivenName) {
                    mGivenName = mDisplayName;
                }

            } catch (JSONException e) {
                RLog.e(RLog.EXCEPTION, "AlmostDoneFragment Exception : " + e.getMessage());
            }
        }
    }

    private void performSocialTwoStepError(JSONObject mPreRegJson) {
        try{
            if (null != mPreRegJson) {
                mProvider = mBundle.getString(RegConstants.SOCIAL_PROVIDER);
                mRegistrationToken = mBundle.getString(RegConstants.SOCIAL_REGISTRATION_TOKEN);

                if (!mPreRegJson.isNull(RegConstants.REGISTER_GIVEN_NAME)
                        && !RegConstants.SOCIAL_BLANK_CHARACTER.equals(mPreRegJson
                        .getString(RegConstants.REGISTER_GIVEN_NAME))) {
                    mGivenName = mPreRegJson.getString(RegConstants.REGISTER_GIVEN_NAME);
                }
                if (!mPreRegJson.isNull(RegConstants.REGISTER_DISPLAY_NAME)
                        && !RegConstants.SOCIAL_BLANK_CHARACTER.equals(mPreRegJson
                        .getString(RegConstants.REGISTER_DISPLAY_NAME))) {
                    mDisplayName = mPreRegJson.getString(RegConstants.REGISTER_DISPLAY_NAME);
                }
                if (!mPreRegJson.isNull(RegConstants.REGISTER_FAMILY_NAME)
                        && !RegConstants.SOCIAL_BLANK_CHARACTER.equals(mPreRegJson
                        .getString(RegConstants.REGISTER_FAMILY_NAME))) {
                    mFamilyName = mPreRegJson.getString(RegConstants.REGISTER_FAMILY_NAME);
                }
                if (!mPreRegJson.isNull(RegConstants.REGISTER_EMAIL)
                        && !RegConstants.SOCIAL_BLANK_CHARACTER.equals(mPreRegJson
                        .getString(RegConstants.REGISTER_EMAIL))) {
                    mEmail = mPreRegJson.getString(RegConstants.REGISTER_EMAIL);
                    isEmailExist = true;
                } else {
                    isEmailExist = false;
                }
            }
        }catch (JSONException e){
            RLog.e(RLog.EXCEPTION, "AlmostDoneFragment Exception : " + e.getMessage());
        }
    }

    private void trackAbtesting() {
        final UIFlow abTestingFlow = RegUtility.getUiFlow();

        switch (abTestingFlow){
            case FLOW_A :
                RLog.d(RLog.AB_TESTING, "UI Flow Type A");
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                        AppTagingConstants.REGISTRATION_CONTROL);
                break;

            case FLOW_B:
                RLog.d(RLog.AB_TESTING, "UI Flow Type B");
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                        AppTagingConstants.REGISTRATION_SPLIT_SIGN_UP);
                break;
            case FLOW_C:
                RLog.d(RLog.AB_TESTING, "UI Flow Type C");
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                        AppTagingConstants.REGISTRATION_SOCIAL_PROOF);
                break;
            default:break;
        }
    }

    private void initUI(View view) {
        consumeTouch(view);
        mContext = getRegistrationFragment().getActivity().getApplicationContext();
        //mBtnContinue.setOnClickListener(this);
        mUser = new User(mContext);
        remarketingOptCheck.setPadding(RegUtility.getCheckBoxPadding(mContext), remarketingOptCheck.getPaddingTop(), remarketingOptCheck.getPaddingRight(), remarketingOptCheck.getPaddingBottom());
        RegUtility.linkifyTermsandCondition(acceptTermsView, getRegistrationFragment().getParentActivity(), mTermsAndConditionClick);

        RegUtility.linkifyPhilipsNews(receivePhilipsNewsView, getRegistrationFragment().getParentActivity(), mPhilipsNewsClick);
        String sourceString = mContext.getResources().getString(R.string.reg_Opt_In_Join_Now) ;
        String updateJoinNowText =  " " + "<b>" + mContext.getResources().getString(R.string.reg_Opt_In_Over_Peers) + "</b> ";
        sourceString = String.format(sourceString, updateJoinNowText);
        mJoinNow.setText(Html.fromHtml(sourceString));

        remarketingOptCheck.setOnCheckedChangeListener(this);
        mEtEmail.setOnUpdateListener(this);
        //mEtEmail.setOnClickListener(this);

        mPbSpinner.setClickable(false);
        mPbSpinner.setEnabled(true);

        if (null != mProvider) {
            mProvider = Character.toUpperCase(mProvider.charAt(0)) + mProvider.substring(1);
        }

        if (isEmailExist) {
            mEtEmail.setVisibility(View.GONE);
            mBtnContinue.setEnabled(true);
        } else {
            if (mBundle == null) {
                mBtnContinue.setEnabled(true);
            } else {
                fieldViewLine.setVisibility(View.VISIBLE);
                mEtEmail.setVisibility(View.VISIBLE);
            }
        }
        handleUiAcceptTerms();
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
        mJoinNow.setVisibility(View.GONE);
        almostDonePresenter.handleAcceptTermsAndReceiveMarketingOpt();
    }

    @Override
    public boolean isReceiveMarketingEmail(){
        return mUser.getReceiveMarketingEmail();
    }

    public void hideAcceptTermsView() {
        mViewAcceptTermsLine.setVisibility(View.GONE);
        acceptTermsContainer.setVisibility(View.GONE);
    }

    @Override
    public void handleTermsAndCondition() {
        if (isEmailExist && RegPreferenceUtility.getStoredState(mContext, mEmail)) {
            mViewAcceptTermsLine.setVisibility(View.GONE);
            acceptTermsContainer.setVisibility(View.GONE);
        } else if(mBundle !=null && mBundle.getString(RegConstants.SOCIAL_TWO_STEP_ERROR)!=null){
            updateABTestingUIFlow();
        }
    }

    @Override
    public void updateTermsAndConditionView() {
        if(!mUser.getReceiveMarketingEmail()){
            periodicOffersCheck.setVisibility(View.VISIBLE);
        }else{
            periodicOffersCheck.setVisibility(View.GONE);
        }
        acceptTermsCheck.setChecked(true);
        fieldViewLine.setVisibility(View.GONE);
        mJoinNow.setVisibility(View.GONE);
        acceptTermsContainer.setVisibility(View.GONE);
    }

    @Override
    public void updateReceiveMarktingView() {
        periodicOffersCheck.setVisibility(View.GONE);
        fieldViewLine.setVisibility(View.GONE);
        mJoinNow.setVisibility(View.GONE);
        mViewAcceptTermsLine.setVisibility(View.GONE);
    }

    private void updateABTestingUIFlow() {
        final UIFlow abTestingUIFlow = RegUtility.getUiFlow();

        switch (abTestingUIFlow){
            case FLOW_A:
                RLog.d(RLog.AB_TESTING, "UI Flow Type A");
                acceptTermsContainer.setVisibility(View.VISIBLE);
                mJoinNow.setVisibility(View.GONE);
                break;

            case FLOW_B:
                RLog.d(RLog.AB_TESTING, "UI Flow Type B");
                acceptTermsContainer.setVisibility(View.VISIBLE);
                periodicOffersCheck.setVisibility(View.GONE);
                receivePhilipsNewsLineView.setVisibility(View.GONE);
                mJoinNow.setVisibility(View.GONE);
                break;

            case FLOW_C:
                RLog.d(RLog.AB_TESTING, "UI Flow Type C");
                String firstToKnow = "<b>" + mContext.getResources().getString(R.string.reg_Opt_In_Be_The_First) + "</b> ";
                mTvFirstToKnow.setText(Html.fromHtml(firstToKnow));
                mTvFirstToKnow.setVisibility(View.VISIBLE);
                acceptTermsContainer.setVisibility(View.VISIBLE);
                mJoinNow.setVisibility(View.VISIBLE);
                break;
            default:break;

        }

    }

    @Override
    public void updateUiStatus() {
        if (isEmailExist) {
            if (almostDonePresenter.isNetworkAvailable()) {
                mBtnContinue.setEnabled(true);
                mRegError.hideError();
            } else {
                mRegError.setError(getString(R.string.reg_NoNetworkConnection));
                mBtnContinue.setEnabled(false);
                scrollViewAutomatically(mRegError, mSvRootLayout);
            }
        } else {
            if (almostDonePresenter.isNetworkAvailable()) {

                if ((mEtEmail.isShown() && mEtEmail.isValidEmail()) ||
                        (mEtEmail.getVisibility() != View.VISIBLE)) {
                    mBtnContinue.setEnabled(true);
                }
                mRegError.hideError();
            } else {

                mRegError.setError(getString(R.string.reg_NoNetworkConnection));
                mBtnContinue.setEnabled(false);
                scrollViewAutomatically(mRegError, mSvRootLayout);
            }
        }
    }

    private void showSpinner() {
        remarketingOptCheck.setEnabled(false);
        mPbSpinner.setVisibility(View.VISIBLE);
        mBtnContinue.setEnabled(false);
    }

    private void hideSpinner() {
        remarketingOptCheck.setEnabled(true);
        mPbSpinner.setVisibility(View.INVISIBLE);
        mBtnContinue.setEnabled(true);
    }

    @OnClick(B.id.reg_btn_continue)
    public void continueButtonClicked() {
        RLog.d(RLog.ONCLICK, "AlmostDoneFragment : Continue");
        mEtEmail.clearFocus();
        if (mEtEmail.isShown() && !mEtEmail.isValidEmail()) return;
        if (mBundle == null) {
            if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
                if (acceptTermsCheck.isChecked()) {
                    storeEmailOrMobileInPreference();
                    trackActionForAcceptTermsOption(AppTagingConstants.ACCEPT_TERMS_OPTION_IN);
                    launchWelcomeFragment();
                } else {
                    errorMessage.setError(mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
                }
            } else {
                launchWelcomeFragment();
            }
            return;
        }
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired() && acceptTermsContainer.getVisibility() == View.VISIBLE) {
            if (acceptTermsCheck.isChecked()) {
                register();
            } else {
                errorMessage.setError(mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
            }
        } else {
            register();
        }
    }

    private void storeEmailOrMobileInPreference() {
        if (mEmail != null) {
            RegPreferenceUtility.storePreference(mContext, mEmail, true);
            return;
        }

        if(mUser.getMobile()!=null && !mUser.getMobile().equalsIgnoreCase("null")){
            RegPreferenceUtility.storePreference(mContext,mUser.getMobile(),true);
        }else if(mUser.getEmail()!=null && !mUser.getEmail().equalsIgnoreCase("null")){
            RegPreferenceUtility.storePreference(mContext,mUser.getEmail(),true);
        }
    }
    private void register() {
        if (almostDonePresenter.isNetworkAvailable()) {
            errorMessage.setVisibility(View.GONE);
            showSpinner();
            if (isEmailExist) {
                mUser.registerUserInfoForSocial(mGivenName, mDisplayName, mFamilyName, mEmail, true,
                        remarketingOptCheck.isChecked(), this, mRegistrationToken);
            } else {
                mEmail = FieldsValidator.getMobileNumber(mEtEmail.getEmailId().trim());
                mUser.registerUserInfoForSocial(mGivenName, mDisplayName, mFamilyName,
                        mEmail, true, remarketingOptCheck.isChecked(), this, mRegistrationToken);
            }
        }
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
            if (remarketingOptCheck.isChecked()) {
                trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_IN);
            } else {
                trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_OUT);
            }
        }
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_SigIn_TitleTxt;
    }

    @Override
    public void onLoginSuccess() {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                RLog.i(RLog.CALLBACK, "AlmostDoneFragment : onLoginSuccess");
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                        AppTagingConstants.SUCCESS_LOGIN);
                hideSpinner();
            }
        });
    }

    @Override
    public void onLoginFailedWithError(final UserRegistrationFailureInfo userRegistrationFailureInfo) {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleLoginFailed(userRegistrationFailureInfo);
            }
        });
    }

    private void handleLoginFailed(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.i(RLog.CALLBACK, "AlmostDoneFragment : onLoginFailedWithError");
        hideSpinner();
        if (userRegistrationFailureInfo.getErrorCode() == EMAIL_ADDRESS_ALREADY_USE_CODE) {
            if (RegistrationHelper.getInstance().isChinaFlow()){
                mEtEmail.setErrDescription(mContext.getResources().getString(R.string.reg_CreateAccount_Using_Phone_Alreadytxt));
            }else {
                mEtEmail.setErrDescription(mContext.getResources().getString(R.string.reg_EmailAlreadyUsed_TxtFieldErrorAlertMsg));
            }
            mEtEmail.showInvalidAlert();
            mEtEmail.showErrPopUp();
            scrollViewAutomatically(mEtEmail, mSvRootLayout);
        }
    }

    @Override
    public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord,
                                              String socialRegistrationToken) {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                RLog.i(RLog.CALLBACK, "AlmostDoneFragment : onLoginFailedWithTwoStepError");
                hideSpinner();
            }
        });
    }

    @Override
    public void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider,
                                                String conflictingIdentityProvider, String conflictingIdpNameLocalized,
                                                String existingIdpNameLocalized, String emailId) {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                RLog.i(RLog.CALLBACK, "AlmostDoneFragment : onLoginFailedWithMergeFlowError");
                hideSpinner();
                addMergeAccountFragment();
            }
        });
    }

    private void addMergeAccountFragment() {
        getRegistrationFragment().addFragment(new MergeAccountFragment());
        trackPage(AppTaggingPages.MERGE_ACCOUNT);
    }

    @Override
    public void onContinueSocialProviderLoginSuccess() {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleContinueSocialProvider();
            }
        });
    }

    private void handleContinueSocialProvider() {
        RegPreferenceUtility.storePreference(mContext, mEmail, true);
        RLog.i(RLog.CALLBACK, "AlmostDoneFragment : onContinueSocialProviderLoginSuccess");
        trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                AppTagingConstants.SUCCESS_USER_CREATION);
        trackMultipleActions();
        handleABTestingFlow();
        hideSpinner();
    }

    private void handleABTestingFlow() {
        final UIFlow abTestingUIFlow = RegUtility.getUiFlow();
        switch (abTestingUIFlow){
            case FLOW_A:
                RLog.d(RLog.AB_TESTING, "UI Flow Type A");
                if (mUser.getEmailVerificationStatus()) {
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
                if (mUser.getEmailVerificationStatus()) {
                    launchWelcomeFragment();
                } else {
                    launchAccountActivateFragment();
                }
                break;
            default:break;
        }
    }

    //called on click of back
    public void clearUserData() {
        if (null != acceptTermsCheck && !acceptTermsCheck.isChecked() &&
                RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            mUser.logout(null);
        }
    }

    private void launchMarketingAccountFragment() {
        getRegistrationFragment().addFragment(new MarketingAccountFragment());
        trackPage(AppTaggingPages.MARKETING_OPT_IN);
    }

    private void launchAccountActivateFragment() {
        trackPage(AppTaggingPages.ACCOUNT_ACTIVATION);
        if (FieldsValidator.isValidEmail(mEmail)) {
            getRegistrationFragment().addFragment(new AccountActivationFragment());
        } else {
            getRegistrationFragment().addFragment(new MobileVerifyCodeFragment());
        }
    }


    private void launchWelcomeFragment() {
        getRegistrationFragment().addWelcomeFragmentOnVerification();
        trackPage(AppTaggingPages.WELCOME);
    }

    @Override
    public void onContinueSocialProviderLoginFailure(final
                                                     UserRegistrationFailureInfo userRegistrationFailureInfo) {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleContinueSocialProviderFailed(userRegistrationFailureInfo);
            }
        });

    }

    private void handleContinueSocialProviderFailed(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.i(RLog.CALLBACK, "AlmostDoneFragment : onContinueSocialProviderLoginFailure");
        hideSpinner();
        if (null != userRegistrationFailureInfo.getDisplayNameErrorMessage()) {
            mEtEmail.setErrDescription(userRegistrationFailureInfo.getDisplayNameErrorMessage());
            mEtEmail.showInvalidAlert();
            mRegError.setError(userRegistrationFailureInfo.getErrorDescription() + ".\n'"
                    + mDisplayName + "' "
                    + userRegistrationFailureInfo.getDisplayNameErrorMessage());
            return;
        }
        if (null != userRegistrationFailureInfo.getEmailErrorMessage()) {
            mEtEmail.setErrDescription(userRegistrationFailureInfo.getEmailErrorMessage());
            mEtEmail.showInvalidAlert();
            mEtEmail.showErrPopUp();
        } else {
            if (userRegistrationFailureInfo.getErrorCode() == EMAIL_ADDRESS_ALREADY_USE_CODE) {
                if (RegistrationHelper.getInstance().isChinaFlow()){
                    mRegError.setError(mContext.getResources().getString(R.string.reg_CreateAccount_Using_Phone_Alreadytxt));
                }else {
                    mRegError.setError(mContext.getResources().getString(R.string.reg_EmailAlreadyUsed_TxtFieldErrorAlertMsg));
                }
            }
        }
    }

    private void handleUpdate() {
        if (almostDonePresenter.isNetworkAvailable()) {
            mRegError.hideError();
            showSpinner();
            updateUser();
        } else {
            remarketingOptCheck.setOnCheckedChangeListener(null);
            remarketingOptCheck.setChecked(!remarketingOptCheck.isChecked());
            remarketingOptCheck.setOnCheckedChangeListener(this);
            mRegError.setError(getString(R.string.reg_NoNetworkConnection));
        }
    }

    private void updateUser() {
        mUser.updateReceiveMarketingEmail(this, remarketingOptCheck.isChecked());
    }

    @Override
    public void onCheckedChanged(View view, boolean isChecked) {
        handleUpdate();
    }

    @Override
    public void onUpdateSuccess() {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                hideSpinner();
                if (remarketingOptCheck.isChecked()) {
                    trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_IN);
                } else {
                    trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_OUT);
                }
            }
        });

    }

    @Override
    public void onUpdateFailedWithError(final int error) {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleUpdateReceiveMarket(error);
            }
        });
    }

    private void handleUpdateReceiveMarket(int error) {
        hideSpinner();
        if (error == Integer.parseInt(RegConstants.INVALID_REFRESH_TOKEN_CODE)) {
            if (getRegistrationFragment() != null) {
                getRegistrationFragment().replaceWithHomeFragment();
            }
            return;
        }
        if (error == RegConstants.FAILURE_TO_CONNECT || error == BAD_RESPONSE_ERROR_CODE) {
            mRegError.setError(mContext.getResources().getString(R.string.reg_JanRain_Server_Connection_Failed));
            return;
        }
        remarketingOptCheck.setOnCheckedChangeListener(null);
        remarketingOptCheck.setChecked(!remarketingOptCheck.isChecked());
        remarketingOptCheck.setOnCheckedChangeListener(AlmostDoneFragment.this);
    }

    @Override
    public boolean isTermsAndConditionAccepted(){
        boolean isTermAccepted = false;
        String mobileNo = mUser.getMobile();
        String email  = mUser.getEmail();
        if(FieldsValidator.isValidMobileNumber(mobileNo)){
            isTermAccepted = RegPreferenceUtility.getStoredState(mContext, mobileNo);
        }else if(FieldsValidator.isValidEmail(email)){
            isTermAccepted = RegPreferenceUtility.getStoredState(mContext, email);
        }
        return isTermAccepted;
    }

    @Override
    public void onUpdate() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                updateUiStatus();
            }
        });
    }
}
