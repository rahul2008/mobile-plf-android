
package com.philips.cdp.registration.ui.traditional;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.philips.cdp.registration.events.SocialProvider;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.customviews.XProviderButton;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegPreferenceUtility;

import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends RegistrationBaseFragment implements OnClickListener,
        NetworStateListener, SocialProviderLoginHandler, EventListener {

    private Button mBtnCreateAccount;

    private XProviderButton mBtnMyPhilips;

    private TextView mTvWelcome;

    private TextView mTvWelcomeDesc;

    private TextView mTvTermsAndConditionDesc;

    private TextView mTvWelcomeNeedAccount;

    private LinearLayout mLlCreateBtnContainer;

    private LinearLayout mLlLoginBtnContainer;

    private LinearLayout mLlSocialProviderBtnContainer;

    private XRegError mRegError;

    private User mUser;

    private String mProvider;

    private ProgressBar mPbJanrainInit;

    private Context mContext;

    private ScrollView mSvRootLayout;

    @Override
    public void onAttach(Activity activity) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onCreateView");
        mContext = getRegistrationFragment().getParentActivity().getApplicationContext();
        EventHelper.getInstance()
                .registerEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);
        EventHelper.getInstance()
                .registerEventNotification(RegConstants.JANRAIN_INIT_FAILURE, this);
        EventHelper.getInstance().registerEventNotification(RegConstants.PARSING_COMPLETED, this);
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        RLog.i(RLog.EVENT_LISTENERS,
                "HomeFragment register: NetworStateListener,JANRAIN_INIT_SUCCESS,JANRAIN_INIT_FAILURE,PARSING_COMPLETED");
        View view;
        if (RegistrationConfiguration.getInstance().getPrioritisedFunction().equals(RegistrationFunction.Registration)) {
            view = inflater.inflate(R.layout.fragment_home_create_top, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_home_login_top, container, false);
        }

        mSvRootLayout = (ScrollView) view.findViewById(R.id.sv_root_layout);
        initUI(view);
        handleOrientation(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onDestroy");
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
        EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_SUCCESS,
                this);
        EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_FAILURE,
                this);
        EventHelper.getInstance().unregisterEventNotification(RegConstants.PARSING_COMPLETED, this);
        RLog.i(RLog.EVENT_LISTENERS,
                "HomeFragment unregister: NetworStateListener,JANRAIN_INIT_SUCCESS,JANRAIN_INIT_FAILURE,PARSING_COMPLETED");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onDetach");
    }

    private void handleSocialProviders(final String countryCode) {
        RLog.d("HomeFragment : ", "handleSocialProviders method country code : " + countryCode);
        if (null != RegistrationConfiguration.getInstance().getSignInProviders()) {
            mLlSocialProviderBtnContainer.post(new Runnable() {

                @Override
                public void run() {
                    mLlSocialProviderBtnContainer.removeAllViews();
                    ArrayList<String> providers = new ArrayList<String>();
                    providers = RegistrationConfiguration.getInstance().getSignInProviders()
                            .getProvidersForCountry(countryCode);
                    if (null != providers) {
                        for (int i = 0; i < providers.size(); i++) {
                            inflateEachProviderBtn(providers.get(i));
                        }
                        RLog.d("HomeFragment", "social providers : " + providers);
                    }
                    handleUiState();
                }
            });
        }
    }

    private void inflateEachProviderBtn(String provider) {
        try {
            String providerName = provider;
            String providerDrawable = "reg_" + provider + "_ic";

            int resourceId = getRegistrationFragment().getParentActivity().getResources().getIdentifier(providerName, "string",
                    getRegistrationFragment().getParentActivity().getPackageName());

            int drawableId = getRegistrationFragment().getParentActivity().getResources().getIdentifier(providerDrawable, "string",
                    getRegistrationFragment().getParentActivity().getPackageName());

            mLlSocialProviderBtnContainer.addView(getProviderBtn(provider, resourceId, drawableId));

        } catch (Exception e) {
            RLog.e("HomeFragment", "Inflate Buttons exception :" + e.getMessage());
        }
    }

    private XProviderButton getProviderBtn(final String providerName, int providerNameStringId,
                                           int providerLogoDrawableId) {
        final XProviderButton providerBtn = new XProviderButton(mContext);
        providerBtn.setProviderName(providerNameStringId);
        providerBtn.setProviderLogoID(providerLogoDrawableId);
        providerBtn.setTag(providerName);

        providerBtn.setEnabled(true);
        if (NetworkUtility.isNetworkAvailable(mContext)
                && UserRegistrationInitializer.getInstance().isJanrainIntialized()) {
            providerBtn.setEnabled(true);
        } else {
            providerBtn.setEnabled(false);
        }
        providerBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                RLog.d(RLog.ONCLICK, "HomeFragment : " + providerName);
                if (NetworkUtility.isNetworkAvailable(mContext)) {
                    callSocialProvider(providerName);
                    providerBtn.showProgressBar();
                }else{
                    scrollViewAutomatically(mRegError,mSvRootLayout);
                    enableControls(false);
                    mRegError.setError(mContext.getResources().getString(R.string.NoNetworkConnection));
                }
            }
        });
        return providerBtn;
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onConfigurationChanged");
        setCustomParams(config);
    }

    private void initUI(View view) {
        consumeTouch(view);
        mTvWelcome = (TextView) view.findViewById(R.id.tv_reg_welcome);
        mTvTermsAndConditionDesc = (TextView) view.findViewById(R.id.tv_reg_legal_notice);
        int minAgeLimit = RegistrationConfiguration.getInstance().getFlow().
                getMinAgeLimitByCountry(RegistrationHelper.getInstance().getCountryCode());
        String termsAndCondition = getString(R.string.AgeLimitText);
        termsAndCondition = String.format(termsAndCondition, minAgeLimit);

        mTvTermsAndConditionDesc.setText(termsAndCondition);
        if (minAgeLimit > 0) {
            mTvTermsAndConditionDesc.setVisibility(View.VISIBLE);
        } else {
            mTvTermsAndConditionDesc.setVisibility(View.GONE);
        }

        mTvWelcomeDesc = (TextView) view.findViewById(R.id.tv_reg_terms_and_condition);
        mTvWelcomeNeedAccount = (TextView) view.findViewById(R.id.tv_reg_create_account);
        mLlCreateBtnContainer = (LinearLayout) view
                .findViewById(R.id.ll_reg_create_account_container);
        mLlLoginBtnContainer = (LinearLayout) view.findViewById(R.id.rl_reg_singin_options);
        mBtnCreateAccount = (Button) view.findViewById(R.id.btn_reg_create_account);
        mBtnCreateAccount.setOnClickListener(this);
        mBtnMyPhilips = (XProviderButton) view.findViewById(R.id.btn_reg_my_philips);
        mBtnMyPhilips.setOnClickListener(this);
        TextView mTvContent = (TextView) view.findViewById(R.id.tv_reg_create_account);
        if (mTvContent.getText().toString().trim().length() > 0) {
            mTvContent.setVisibility(View.VISIBLE);
        } else {
            mTvContent.setVisibility(View.GONE);
        }
        mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);

        mPbJanrainInit = (ProgressBar) view.findViewById(R.id.pb_reg_janrain_init);
        mPbJanrainInit.setClickable(false);
        mPbJanrainInit.setEnabled(false);
        mLlSocialProviderBtnContainer = (LinearLayout) view
                .findViewById(R.id.ll_reg_social_provider_container);
        handleSocialProviders(RegistrationHelper.getInstance().getCountryCode());



        mUser = new User(mContext);
        linkifyTermAndPolicy(mTvWelcomeDesc);

       // handleJanrainInitPb();
       // enableControls(NetworkUtility.isNetworkAvailable(mContext));
        handleUiState();
    }

    private void handleJanrainInitPb() {
        if (NetworkUtility.isNetworkAvailable(mContext)
                && UserRegistrationInitializer.getInstance().isJanrainIntialized()) {
            mPbJanrainInit.setVisibility(View.GONE);
        } else if (NetworkUtility.isNetworkAvailable(mContext)
                && !UserRegistrationInitializer.getInstance().isJanrainIntialized()) {
            mPbJanrainInit.setVisibility(View.VISIBLE);
        } else {
            mPbJanrainInit.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        /**
         * Library does not include resource constants after ADT 14 Link
         * :http://tools.android.com/tips/non-constant-fields
         */
        if (v.getId() == R.id.btn_reg_create_account) {
            RLog.d(RLog.ONCLICK, "HomeFragment : Create Account");
            trackMultipleActionsRegistration();
            launchCreateAccountFragment();

        } else if (v.getId() == R.id.btn_reg_my_philips) {
            RLog.d(RLog.ONCLICK, "HomeFragment : My Philips");
            trackMultipleActionsLogin(AppTagingConstants.MY_PHILIPS);
            launchSignInFragment();
        }
    }

    private void launchSignInFragment() {
        trackPage(AppTaggingPages.SIGN_IN_ACCOUNT);
        getRegistrationFragment().addFragment(new SignInAccountFragment());
    }

    private void launchCreateAccountFragment() {
        trackPage(AppTaggingPages.CREATE_ACCOUNT);
        getRegistrationFragment().addFragment(new CreateAccountFragment());
    }

    private void makeProgressVisible(){
        if(getView() != null) {
            getView().findViewById(R.id.sv_root_layout).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.ll_root_layout).setVisibility(View.VISIBLE);
        }
    }

    private void callSocialProvider(String providerName) {
        RLog.d("HomeFragment", "callSocialProvider method provider name :" + providerName);
        makeProgressVisible();
        mProvider = providerName;
        if (null == mUser)
            return;
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            trackMultipleActionsLogin(providerName);
            trackSocialProviderPage();
            mUser.loginUserUsingSocialProvider(getActivity(), providerName, this, null);
        }
    }

    @Override
    public void setViewParams(Configuration config, int width) {
            applyParams(config, mTvWelcome, width);
            applyParams(config, mTvWelcomeDesc, width);
            applyParams(config, mLlCreateBtnContainer, width);
            applyParams(config, mLlLoginBtnContainer, width);
            applyParams(config, mTvTermsAndConditionDesc, width);
            applyParams(config, mTvWelcomeNeedAccount, width);
    }

    @Override
    protected void handleOrientation(View view) {
            handleOrientationOnView(view);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.SigIn_TitleTxt;
    }

    private Handler mHandler = new Handler();

    @Override
    public void onEventReceived(String event) {
        RLog.i(RLog.EVENT_LISTENERS, "HomeFragment :onEventReceived isHomeFragment :onEventReceived is : " + event);
        if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
           // handleJanrainInitPb();
        } else if (RegConstants.JANRAIN_INIT_FAILURE.equals(event)) {
           // enableControls(false);
           // handleJanrainInitPb();
        } else if (RegConstants.PARSING_COMPLETED.equals(event)) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    enableControls(true);
                   // handleSocialProvider();
                }
            });

        }
    }

    private void handleSocialProvider() {
        RegistrationConfiguration.getInstance().getSignInProviders();
        handleSocialProviders(RegistrationHelper.getInstance().getCountryCode());
    }

    private void handleUiState() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
                mRegError.hideError();
                enableControls(true);
        } else {
            mRegError.setError(mContext.getResources().getString(R.string.NoNetworkConnection));
            enableControls(false);
            trackActionLoginError(AppTagingConstants.NETWORK_ERROR_CODE);
            scrollViewAutomatically(mRegError, mSvRootLayout);
        }
    }

    private void enableControls(boolean clickableState) {
        if(clickableState){
            mRegError.hideError();
        }
        handleBtnClickableStates(clickableState);
    }

    private void handleBtnClickableStates(boolean state) {
        mBtnCreateAccount.setEnabled(state);
        enableSocialProviders(state);
        mBtnMyPhilips.setEnabled(state);
        if (state) {
            mBtnMyPhilips.setProviderTextColor(R.color.reg_btn_text_enable_color);
            return;
        }
        mBtnMyPhilips.setProviderTextColor(R.color.reg_btn_text_disabled_color);
    }

    private void enableSocialProviders(boolean enableState) {
        for (int i = 0; i < mLlSocialProviderBtnContainer.getChildCount(); i++) {
            mLlSocialProviderBtnContainer.getChildAt(i).setEnabled(enableState);
        }
    }

    private void linkifyTermAndPolicy(TextView pTvPrivacyPolicy) {

        String privacyPolicyText = getString(R.string.LegalNoticeForPrivacy);
        privacyPolicyText = String.format(privacyPolicyText, getString(R.string.PrivacyPolicyText));
        mTvWelcomeDesc.setText(privacyPolicyText);

        String privacy = mContext.getResources().getString(R.string.PrivacyPolicyText);
        SpannableString spanableString = new SpannableString(privacyPolicyText);

        int privacyStartIndex = privacyPolicyText.toLowerCase().indexOf(
                privacy.toLowerCase());

        spanableString.setSpan(new ClickableSpan() {

                                   @Override
                                   public void onClick(View widget) {
                                       handlePrivacyPolicy();
                                   }

                               }, privacyStartIndex, privacyStartIndex + privacy.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        removeUnderlineFromLink(spanableString);

        pTvPrivacyPolicy.setText(spanableString);
        pTvPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());
        pTvPrivacyPolicy.setLinkTextColor(getResources().getColor(
                R.color.reg_hyperlink_highlight_color));
        pTvPrivacyPolicy.setHighlightColor(getResources().getColor(android.R.color.transparent));
    }

    private void removeUnderlineFromLink(SpannableString spanableString) {
        for (ClickableSpan u : spanableString.getSpans(0, spanableString.length(),
                ClickableSpan.class)) {
            spanableString.setSpan(new UnderlineSpan() {

                public void updateDrawState(TextPaint tp) {
                    tp.setUnderlineText(false);
                }
            }, spanableString.getSpanStart(u), spanableString.getSpanEnd(u), 0);
        }

        for (URLSpan u : spanableString.getSpans(0, spanableString.length(), URLSpan.class)) {
            spanableString.setSpan(new UnderlineSpan() {

                public void updateDrawState(TextPaint tp) {
                    tp.setUnderlineText(false);
                }
            }, spanableString.getSpanStart(u), spanableString.getSpanEnd(u), 0);
        }
    }

//    private void setAlphaForView(View view, float alpha) {
//        AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
//        animation.setDuration(0);
//        animation.setFillAfter(true);
//        view.startAnimation(animation);
//    }

    private void handlePrivacyPolicy() {
        RegistrationHelper.getInstance().getUserRegistrationListener()
                .notifyOnPrivacyPolicyClickEventOccurred(getRegistrationFragment().getParentActivity());
    }

    private void handleTermsCondition() {
        RegistrationHelper.getInstance().getUserRegistrationListener()
                .notifyOnTermsAndConditionClickEventOccurred(getRegistrationFragment().getParentActivity());
    }

    @Override
    public void onLoginSuccess() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleLoginSuccess();
            }
        });

    }

    private void handleLoginSuccess() {
        trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                AppTagingConstants.SUCCESS_LOGIN);

        //trackSocialProviderPage();
        RLog.i(RLog.CALLBACK, "HomeFragment : onLoginSuccess");
        hideProviderProgress();
        enableControls(true);
        User user = new User(mContext);
        if (user.getEmailVerificationStatus()) {
            launchWelcomeFragment();
        } else {
            launchAccountActivationFragment();
        }
    }

    private void launchAccountActivationFragment() {
       getRegistrationFragment().launchAccountActivationFragmentForLogin();
    }


    private void launchWelcomeFragment() {
        String emailId = mUser.getEmail();
        if (emailId != null && RegistrationConfiguration.getInstance().getFlow().isTermsAndConditionsAcceptanceRequired() && !RegPreferenceUtility.getStoredState(mContext, emailId)) {
            launchAlmostDoneForTermsAcceptanceFragment();
            return;
        }

        trackPage(AppTaggingPages.WELCOME);
        getRegistrationFragment().addWelcomeFragmentOnVerification();
    }


    private void launchAlmostDoneForTermsAcceptanceFragment() {
        trackPage(AppTaggingPages.ALMOST_DONE);
        getRegistrationFragment().addAlmostDoneFragmentforTermsAcceptance();
    }

    private void hideProviderProgress() {

        if(getView() == null){
            return;
        }

        getView().findViewById(R.id.sv_root_layout).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.ll_root_layout).setVisibility(View.INVISIBLE);

        if (null != getView().findViewWithTag(mProvider)) {
            XProviderButton providerButton = (XProviderButton) getView().findViewWithTag(mProvider);
            providerButton.hideProgressBar();
        }
    }

    private void showProviderProgress() {
        if (null != getView().findViewWithTag(mProvider)) {
            XProviderButton providerButton = (XProviderButton) getView().findViewWithTag(mProvider);
            providerButton.showProgressBar();
        }
    }

    @Override
    public void onLoginFailedWithError(final UserRegistrationFailureInfo userRegistrationFailureInfo) {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleLoginFailedWithError(userRegistrationFailureInfo);
            }
        });


    }

    private void handleLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.i(RLog.CALLBACK, "HomeFragment : onLoginFailedWithError : code :" + userRegistrationFailureInfo.getErrorCode());
        trackPage(AppTaggingPages.HOME);
        hideProviderProgress();
        enableControls(true);
        if (null != userRegistrationFailureInfo) {
            trackActionLoginError(userRegistrationFailureInfo.getErrorCode());
        }
    }

    @Override
    public void onLoginFailedWithTwoStepError(final JSONObject prefilledRecord,
                                              final String socialRegistrationToken) {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                RLog.i(RLog.CALLBACK, "HomeFragment : onLoginFailedWithTwoStepError");
                hideProviderProgress();
                enableControls(true);
                RLog.i("HomeFragment", "Login failed with two step error" + "JSON OBJECT :"+ prefilledRecord);
                launchAlmostDoneFragment(prefilledRecord, socialRegistrationToken);
            }
        });
    }

    private void launchAlmostDoneFragment(JSONObject prefilledRecord, String socialRegistrationToken) {
        trackPage(AppTaggingPages.ALMOST_DONE);
        getRegistrationFragment().addAlmostDoneFragment(prefilledRecord, mProvider,
                socialRegistrationToken);
    }

    @Override
    public void onLoginFailedWithMergeFlowError(final String mergeToken, final String existingProvider,
                                                final String conflictingIdentityProvider, String conflictingIdpNameLocalized,
                                                String existingIdpNameLocalized, final String emailId) {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleLoginFailedWithMergeFlowError(existingProvider, mergeToken, conflictingIdentityProvider, emailId);
            }
        });
    }

    private void handleLoginFailedWithMergeFlowError(String existingProvider, String mergeToken, String conflictingIdentityProvider, String emailId) {
        hideProviderProgress();
        enableControls(true);
        if (mUser.handleMergeFlowError(existingProvider)) {
            launchMergeAccountFragment(mergeToken, conflictingIdentityProvider, emailId);
        } else {
            mProvider = existingProvider;
            Bundle bundle = new Bundle();
            bundle.putString(RegConstants.SOCIAL_PROVIDER, conflictingIdentityProvider);
            bundle.putString(RegConstants.CONFLICTING_SOCIAL_PROVIDER, existingProvider);
            bundle.putString(RegConstants.SOCIAL_MERGE_TOKEN, mergeToken);
            bundle.putString(RegConstants.SOCIAL_MERGE_EMAIL, emailId);
            launchSocialToSocialMergeAccountFragment(bundle);
        }
    }

    private void launchMergeAccountFragment(String mergeToken, String existingProvider, String emailId) {
        trackPage(AppTaggingPages.MERGE_ACCOUNT);
        getRegistrationFragment().addMergeAccountFragment(mergeToken, existingProvider, emailId);
    }

    private void launchSocialToSocialMergeAccountFragment(Bundle bundle) {
        trackPage(AppTaggingPages.MERGE_SOCIAL_ACCOUNT);
        getRegistrationFragment().addMergeSocialAccountFragment(bundle);
    }

    @Override
    public void onContinueSocialProviderLoginSuccess() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                RLog.i(RLog.CALLBACK, "HomeFragment : onContinueSocialProviderLoginSuccess");
                hideProviderProgress();
                enableControls(true);
                launchWelcomeFragment();
            }
        });
    }

    @Override
    public void onContinueSocialProviderLoginFailure(
            final UserRegistrationFailureInfo userRegistrationFailureInfo) {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleContinueSocialProviderLoginFailure(userRegistrationFailureInfo);
            }
        });
    }

    private void handleContinueSocialProviderLoginFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.i(RLog.CALLBACK, "HomeFragment : onContinueSocialProviderLoginFailure");
        trackSocialProviderPage();
        hideProviderProgress();
        enableControls(true);
        if (null != userRegistrationFailureInfo) {
            trackActionLoginError(userRegistrationFailureInfo.getErrorCode());
        }
    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        RLog.i(RLog.NETWORK_STATE, "HomeFragment :onNetWorkStateReceived state :" + isOnline);
        if(!isOnline){
            hideProviderProgress();
        }
        handleUiState();
    }



    private void trackSocialProviderPage() {
        if (mProvider == null) {
            return;
        }
        if (mProvider.equalsIgnoreCase(SocialProvider.FACEBOOK)) {
            trackPage(AppTaggingPages.FACEBOOK);
        } else if (mProvider.equalsIgnoreCase(SocialProvider.GOOGLE_PLUS)) {
            trackPage(AppTaggingPages.GOOGLE_PLUS);
        } else if (mProvider.equalsIgnoreCase(SocialProvider.TWITTER)) {
            trackPage(AppTaggingPages.TWITTER);
        }
    }
}
