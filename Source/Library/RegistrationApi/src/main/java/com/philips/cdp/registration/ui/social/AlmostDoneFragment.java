
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.apptagging.AppTaggingPages;
import com.philips.cdp.registration.apptagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.events.EventListener;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.customviews.XButton;
import com.philips.cdp.registration.ui.customviews.XCheckBox;
import com.philips.cdp.registration.ui.customviews.XEmail;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.customviews.onUpdateListener;
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

import java.util.HashMap;
import java.util.Map;

public class AlmostDoneFragment extends RegistrationBaseFragment implements EventListener,
        onUpdateListener, SocialProviderLoginHandler, NetworStateListener, OnClickListener, XCheckBox.OnCheckedChangeListener {

    private static final int LOGIN_FAILURE = -1;

    private final static int EMAIL_ADDRESS_ALREADY_USE_CODE = 390;

    private TextView mTvSignInWith;

    private LinearLayout mLlAlmostDoneContainer;

    private LinearLayout mLlPeriodicOffersCheck;

    private LinearLayout mLlAcceptTermsContainer;

    private XCheckBox mCbAcceptTerms;

    private XRegError mRegAccptTermsError;

    private RelativeLayout mRlContinueBtnContainer;

    private XCheckBox mCbRemarketingOpt;

    private XRegError mRegError;

    private XEmail mEtEmail;

    private XButton mBtnContinue;

    private ProgressBar mPbSpinner;

    private String mGivenName;

    private String mDisplayName;

    private String mFamilyName;

    private String mEmail;

    private String mProvider;

    private boolean isEmailExist;

    private String mRegistrationToken;

    private Context mContext;

    private ScrollView mSvRootLayout;

    private Bundle mBundle;

    private TextView mJoinNow;

    private boolean isForTermsAccepatance;

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
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        EventHelper.getInstance()
                .registerEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);
        parseRegistrationInfo();
        RLog.i(RLog.EVENT_LISTENERS,
                "AlmostDoneFragment register: NetworStateListener,JANRAIN_INIT_SUCCESS");
        View view = inflater.inflate(R.layout.reg_fragment_social_almost_done, container, false);
        mSvRootLayout = (ScrollView) view.findViewById(R.id.sv_root_layout);
        initUI(view);
        updateUiStatus(false);
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
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
        EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_SUCCESS,
                this);
        RLog.i(RLog.EVENT_LISTENERS,
                "AlmostDoneFragment unregister: NetworStateListener,JANRAIN_INIT_SUCCESS");
        super.onDestroy();
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
        if (mCbAcceptTerms != null) {
            if (mCbRemarketingOpt.isChecked()) {
                isSavedCBTermsChecked = true;
                mSavedBundle.putBoolean("isSavedCBTermsChecked", isSavedCBTermsChecked);
                mSavedBundle.putString("savedCBTerms", mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
            }
        }
        if (mCbAcceptTerms != null) {
            if (mCbAcceptTerms.isChecked()) {
                isSavedCbAcceptTermsChecked = true;
                mSavedBundle.putBoolean("isSavedCbAcceptTermsChecked", isSavedCbAcceptTermsChecked);
                mSavedBundle.putString("savedCbAcceptTerms", mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
            }
        }
        if (mRegAccptTermsError != null) {
            if (mRegAccptTermsError.getVisibility() == View.VISIBLE) {
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
                mCbRemarketingOpt.setChecked(true);
            }
            if (savedInstanceState.getBoolean("isSavedCbAcceptTermsChecked")) {
                mCbAcceptTerms.setChecked(true);
            }
            if (savedInstanceState.getString("saveTermsAndConditionErrText") != null && savedInstanceState.getBoolean("isTermsAndConditionVisible")) {
                mRegAccptTermsError.setError(savedInstanceState.getString("saveTermsAndConditionErrText"));
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
        applyParams(config, mTvSignInWith, width);
        applyParams(config, mLlAlmostDoneContainer, width);
        applyParams(config, mLlPeriodicOffersCheck, width);
        applyParams(config, mRlContinueBtnContainer, width);
        applyParams(config, mRegError, width);
        applyParams(config, mRegAccptTermsError, width);
        applyParams(config, mLlAcceptTermsContainer, width);
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

                isForTermsAccepatance = mBundle.getBoolean(RegConstants.IS_FOR_TERMS_ACCEPATNACE, false);

                JSONObject mPreRegJson = null;
                mPreRegJson = new JSONObject(mBundle.getString(RegConstants.SOCIAL_TWO_STEP_ERROR));

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

                if (null == mGivenName) {
                    mGivenName = mDisplayName;
                }

            } catch (JSONException e) {
                RLog.e(RLog.EXCEPTION, "AlmostDoneFragment Exception : " + e.getMessage());
            }
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

       /* if (abTestingFlow.equals(UIFlow.FLOW_A)) {
            RLog.d(RLog.AB_TESTING, "UI Flow Type A");
            trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                    AppTagingConstants.REGISTRATION_CONTROL);
        } else if (abTestingFlow.equals(UIFlow.FLOW_B)) {
            RLog.d(RLog.AB_TESTING, "UI Flow Type B");
            trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                    AppTagingConstants.REGISTRATION_SPLIT_SIGN_UP);
        } else if (abTestingFlow.equals(UIFlow.FLOW_C)) {
            RLog.d(RLog.AB_TESTING, "UI Flow Type C");
            trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                    AppTagingConstants.REGISTRATION_SOCIAL_PROOF);
        }*/
    }

    private void initUI(View view) {
        consumeTouch(view);
        mContext = getRegistrationFragment().getActivity().getApplicationContext();
        mBtnContinue = (XButton) view.findViewById(R.id.reg_btn_continue);
        mBtnContinue.setOnClickListener(this);
        mTvSignInWith = (TextView) view.findViewById(R.id.tv_reg_sign_in_with);
        mLlAlmostDoneContainer = (LinearLayout) view.findViewById(R.id.ll_reg_almost_done);

        mLlAcceptTermsContainer = (LinearLayout) view
                .findViewById(R.id.ll_reg_accept_terms);

        mLlPeriodicOffersCheck = (LinearLayout) view
                .findViewById(R.id.ll_reg_periodic_offers_check);

        mRlContinueBtnContainer = (RelativeLayout) view
                .findViewById(R.id.rl_reg_btn_continue_container);

        mCbRemarketingOpt = (XCheckBox) view.findViewById(R.id.cb_reg_receive_philips_news);
        mCbRemarketingOpt.setPadding(RegUtility.getCheckBoxPadding(mContext), mCbRemarketingOpt.getPaddingTop(), mCbRemarketingOpt.getPaddingRight(), mCbRemarketingOpt.getPaddingBottom());

        TextView acceptTermsView = (TextView) view.findViewById(R.id.tv_reg_accept_terms);
        mCbAcceptTerms = (XCheckBox) view.findViewById(R.id.cb_reg_accept_terms);
        RegUtility.linkifyTermsandCondition(acceptTermsView, getRegistrationFragment().getParentActivity(), mTermsAndConditionClick);

        TextView receivePhilipsNewsView = (TextView) view.findViewById(R.id.tv_reg_philips_news);
        RegUtility.linkifyPhilipsNews(receivePhilipsNewsView, getRegistrationFragment().getParentActivity(), mPhilipsNewsClick);
        mJoinNow = (TextView) view.findViewById(R.id.tv_join_now);

        String sourceString = mContext.getResources().getString(R.string.reg_Opt_In_Join_Now) ;
        String updateJoinNowText =  " " + "<b>" + mContext.getResources().getString(R.string.reg_Opt_In_Over_Peers) + "</b> ";
        sourceString = String.format(sourceString, updateJoinNowText);
        mJoinNow.setText(Html.fromHtml(sourceString));
        

        mCbAcceptTerms.setOnCheckedChangeListener(this);
        mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
        mEtEmail = (XEmail) view.findViewById(R.id.rl_reg_email_field);
        mEtEmail.setOnUpdateListener(this);
        mEtEmail.setOnClickListener(this);

        mPbSpinner = (ProgressBar) view.findViewById(R.id.pb_reg_social_almost_done_spinner);
        mPbSpinner.setClickable(false);
        mPbSpinner.setEnabled(true);

        mRegAccptTermsError = (XRegError) view.findViewById(R.id.cb_reg_accept_terms_error);

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
                View viewLine = view.findViewById(R.id.reg_view_line);
                viewLine.setVisibility(View.VISIBLE);
                mEtEmail.setVisibility(View.VISIBLE);
            }
        }
        handleUiAcceptTerms(view);
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

    private void handleUiAcceptTerms(View view) {
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            if (isEmailExist && RegPreferenceUtility.getStoredState(mContext, mEmail)) {
                View acceptTermsLine = view.findViewById(R.id.reg_view_accep_terms_line);
                acceptTermsLine.setVisibility(View.GONE);
                mLlAcceptTermsContainer.setVisibility(View.GONE);
                setAcceptanceUI(view);
            } else {
                setAcceptanceUI(view);
            }
        } else {
            View acceptTermsLine = view.findViewById(R.id.reg_view_accep_terms_line);
            acceptTermsLine.setVisibility(View.GONE);
            mLlAcceptTermsContainer.setVisibility(View.GONE);
        }

        if (!isForTermsAccepatance) {
            view.findViewById(R.id.ll_reg_periodic_offers_check).setVisibility(View.GONE);
            view.findViewById(R.id.reg_recieve_email_line).setVisibility(View.GONE);
            view.findViewById(R.id.tv_join_now).setVisibility(View.GONE);

        }
    }

    private void setAcceptanceUI(View view) {
        final UIFlow abStrings = RegUtility.getUiFlow();
        if (abStrings.equals(UIFlow.FLOW_A)) {
            RLog.d(RLog.AB_TESTING, "UI Flow Type A");
            mJoinNow.setVisibility(View.GONE);
        } else if (abStrings.equals(UIFlow.FLOW_B)) {
            RLog.d(RLog.AB_TESTING, "UI Flow Type B");
            mLlPeriodicOffersCheck.setVisibility(View.GONE);
            view.findViewById(R.id.reg_recieve_email_line).setVisibility(View.GONE);
            mJoinNow.setVisibility(View.GONE);
        } else if (abStrings.equals(UIFlow.FLOW_C)) {
            RLog.d(RLog.AB_TESTING, "UI Flow Type C");
            mJoinNow.setVisibility(View.VISIBLE);
        }
        mLlAcceptTermsContainer.setVisibility(View.VISIBLE);
    }

    private void updateUiStatus(Boolean isNetwork) {
        if (isEmailExist) {
            if (NetworkUtility.isNetworkAvailable(mContext)) {
                mBtnContinue.setEnabled(true);
                mRegError.hideError();
            } else {
                mRegError.setError(getString(R.string.reg_NoNetworkConnection));
                mBtnContinue.setEnabled(false);
                scrollViewAutomatically(mRegError, mSvRootLayout);
            }
        } else {
            if (NetworkUtility.isNetworkAvailable(mContext)) {

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
        mPbSpinner.setVisibility(View.VISIBLE);
        mBtnContinue.setEnabled(false);
    }

    private void hideSpinner() {
        mPbSpinner.setVisibility(View.INVISIBLE);
        mBtnContinue.setEnabled(true);
    }

    @Override
    public void onEventReceived(String event) {
        RLog.i(RLog.EVENT_LISTENERS, "AlmostDoneFragment :onEventReceived is : " + event);
        if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
            updateUiStatus(false);
        }
    }

    @Override
    public void onUpadte() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                updateUiStatus(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reg_btn_continue) {
            RLog.d(RLog.ONCLICK, "AlmostDoneFragment : Continue");
            mEtEmail.clearFocus();
            if (mEtEmail.isShown() && !mEtEmail.isValidEmail()) return;
            if (mBundle == null) {
                if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
                    if (mCbAcceptTerms.isChecked()) {
                        storeEmailOrMobileInPreference();
                        trackActionForAcceptTermsOption(AppTagingConstants.ACCEPT_TERMS_OPTION_IN);
                        launchWelcomeFragment();
                    } else {
                        mRegAccptTermsError.setError(mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
                    }
                } else {
                    launchWelcomeFragment();
                }
                return;
            }
            if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired() && mLlAcceptTermsContainer.getVisibility() == View.VISIBLE) {
                if (mCbAcceptTerms.isChecked()) {
                    register();
                } else {
                    mRegAccptTermsError.setError(mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
                }
            } else {
                register();
            }

        }
    }

    private void storeEmailOrMobileInPreference() {
        if (mEmail != null) {
            RegPreferenceUtility.storePreference(mContext, mEmail, true);
            return;
        }

        User user = new User(mContext);
        if(user.getMobile()!=null && !user.getMobile().equalsIgnoreCase("null")){
            RegPreferenceUtility.storePreference(mContext,user.getMobile(),true);
        }else if(user.getEmail()!=null && !user.getEmail().equalsIgnoreCase("null")){
            RegPreferenceUtility.storePreference(mContext,user.getEmail(),true);
        }
    }
    private void register() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            mRegAccptTermsError.setVisibility(View.GONE);
            User user = new User(mContext);
            showSpinner();
            if (isEmailExist) {
                user.registerUserInfoForSocial(mGivenName, mDisplayName, mFamilyName, mEmail, true,
                        mCbRemarketingOpt.isChecked(), this, mRegistrationToken);
            } else {
                mEmail = FieldsValidator.getMobileNumber(mEtEmail.getEmailId().trim());
                user.registerUserInfoForSocial(mGivenName, mDisplayName, mFamilyName,
                        mEmail, true, mCbRemarketingOpt.isChecked(), this, mRegistrationToken);
            }
        }
    }

    private void trackMultipleActions() {
        Map<String, Object> map = new HashMap<String, Object>();

        final UIFlow abStrings = RegUtility.getUiFlow();
        if (!abStrings.equals(UIFlow.FLOW_B)) {
            if (mCbRemarketingOpt.isChecked()) {
                trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_IN);
            } else {
                trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_OUT);
            }
        }
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            if (mCbAcceptTerms.isChecked()) {
                trackActionForAcceptTermsOption(AppTagingConstants.ACCEPT_TERMS_OPTION_IN);
            } else {
                trackActionForAcceptTermsOption(AppTagingConstants.ACCEPT_TERMS_OPTION_OUT);
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
        User user = new User(mContext);
        final UIFlow abStrings = RegUtility.getUiFlow();
        if (abStrings.equals(UIFlow.FLOW_A)) {
            RLog.d(RLog.AB_TESTING, "UI Flow Type A");
            if (user.getEmailVerificationStatus()) {
                launchWelcomeFragment();
            } else {
                launchAccountActivateFragment();
            }
        } else if (abStrings.equals(UIFlow.FLOW_B)) {
            RLog.d(RLog.AB_TESTING, "UI Flow Type B");
            launchMarketingAccountFragment();
        } else if (abStrings.equals(UIFlow.FLOW_C)) {
            RLog.d(RLog.AB_TESTING, "UI Flow Type C");
            if (user.getEmailVerificationStatus()) {
                launchWelcomeFragment();
            } else {
                launchAccountActivateFragment();
            }
        }
        hideSpinner();
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

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        RLog.i(RLog.NETWORK_STATE, "AlmostDone :onNetWorkStateReceived state :" + isOnline);
        //handleUiState();
        updateUiStatus(true);
    }

    //called on click of back
    public void clearUserData() {
        if (null != mCbAcceptTerms && !mCbAcceptTerms.isChecked() &&
                RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            User user = new User(mContext);
            user.logout(null);
        }
    }

    @Override
    public void onCheckedChanged(View view, boolean isChecked) {
        int id = mCbAcceptTerms.getId();
        if (id == R.id.cb_reg_accept_terms) {
            if (isChecked) {
                mRegAccptTermsError.setVisibility(View.GONE);
            } else {
                mRegAccptTermsError.setError(mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
            }
        }
    }
}
