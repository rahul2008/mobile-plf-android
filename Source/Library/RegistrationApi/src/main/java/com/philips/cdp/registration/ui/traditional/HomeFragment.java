/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.content.res.Configuration;
import android.os.*;
import android.support.graphics.drawable.*;
import android.support.v4.content.*;
import android.text.*;
import android.text.method.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.Button;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.*;
import com.philips.cdp.registration.app.tagging.*;
import com.philips.cdp.registration.configuration.*;
import com.philips.cdp.registration.dao.*;
import com.philips.cdp.registration.listener.*;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.customviews.*;
import com.philips.cdp.registration.ui.traditional.mobile.*;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.uid.view.widget.*;

import org.json.*;

import java.util.*;

import butterknife.*;

import static com.philips.cdp.registration.ui.utils.RegConstants.*;


public class HomeFragment extends RegistrationBaseFragment implements HomeContract {


    private static final int AUTHENTICATION_FAILED = -30;

    @BindView(R2.id.usr_startScreen_createAccount_Button)
    Button mBtnCreateAccount;

    @BindView(R2.id.usr_startScreen_Login_Button)
    Button mBtnMyPhilips;

    @BindView(R2.id.usr_startScreen_title_label)
    TextView mTvWelcome;

    @BindView(R2.id.usr_startScreen_valueAdd_label)
    TextView mTvWelcomeDesc;

    @BindView(R2.id.usr_startScreen_Social_Container)
    LinearLayout mLlSocialProviderBtnContainer;

    @BindView(R2.id.reg_error_msg)
    XRegError mRegError;

    Context mContext;

    @BindView(R2.id.sv_root_layout)
    ScrollView mSvRootLayout;

    @BindView(R2.id.ll_root_layout)
    LinearLayout spinnerLayout;

    @BindView(R2.id.usr_startScreen_baseLayout_LinearLayout)
    LinearLayout usr_startScreen_baseLayout_LinearLayout;

    @BindView(R2.id.usr_StartScreen_privacyNotice_country_LinearLayout)
    RelativeLayout usr_StartScreen_privacyNotice_country_LinearLayout;

    @BindView(R2.id.usr_StartScreen_country_label)
    TextView mCountryDisplay;

    @BindView(R2.id.usr_StartScreen_privacyNotice_label)
    TextView privacyPolicy;

    @BindView(R2.id.usr_StartScreen_privacyNotice_country2_LinearLayout)
    LinearLayout usr_StartScreen_privacyNotice_country_LinearLayout2;

    @BindView(R2.id.usr_StartScreen_country2_label)
    TextView mCountryDisplay2;

    @BindView(R2.id.usr_StartScreen_privacyNotice2_label)
    TextView privacyPolicy2;

    @BindView(R2.id.usr_StartScreen_Skip_Button)
    Button continueWithouAccount;

    @BindView(R2.id.usr_startScreen_orLoginWith_Label)
    Label usr_startScreen_orLoginWith_Label;

    private HomePresenter homePresenter;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homePresenter = new HomePresenter(this);
        URInterface.getComponent().inject(this);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onCreateView");
        mContext = getRegistrationFragment().getParentActivity().getApplicationContext();
        if (RegistrationConfiguration.getInstance().getPrioritisedFunction().equals(RegistrationFunction.Registration)) {
            view = inflater.inflate(R.layout.reg_fragment_home_create_top, container, false);
        } else {
            view = inflater.inflate(R.layout.reg_fragment_home_login_top, container, false);
        }
        ButterKnife.bind(this, view);
        initUI(view);
        handleOrientation(view);
        homePresenter.registerWeChatApp();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onResume");
        makeProviderButtonsClickable();
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onDestroy");
        if (homePresenter != null) {
            homePresenter.cleanUp();
        }
        super.onDestroy();
    }

    private Button getProviderBtn(final String providerName, int providerLogoDrawableId) {
        final com.philips.platform.uid.view.widget.Button socialButton =
                (com.philips.platform.uid.view.widget.Button)
                        getActivity().getLayoutInflater().inflate(R.layout.social_button, null);
        FontLoader.getInstance().setTypeface(socialButton, RegConstants.PUIICON_TTF);
        socialButton.setImageDrawable(VectorDrawableCompat.create(getResources(),
                providerLogoDrawableId, getContext().getTheme()));
        socialButton.setEnabled(true);
        socialButton.setId(providerLogoDrawableId);
        if (homePresenter.isNetworkAvailable()
                && UserRegistrationInitializer.getInstance().isJanrainIntialized()) {
            socialButton.setEnabled(true);
        } else {
            socialButton.setEnabled(false);
        }
        socialButton.setOnClickListener(v -> {
            trackPage(AppTaggingPages.CREATE_ACCOUNT);
            RLog.d(RLog.ONCLICK, "HomeFragment : socialButton providerName" + providerName);
            RLog.d(RLog.ONCLICK, "HomeFragment : socialButton getId" + socialButton.getId());


            if (mRegError.isShown()) mRegError.hideError();
            if (homePresenter.isNetworkAvailable()) {
                homePresenter.setFlowDeligate(HomePresenter.FLOWDELIGATE.SOCIALPROVIDER);
                homePresenter.setProvider(providerName);
                if (UserRegistrationInitializer.getInstance().isJanrainIntialized()) {
                    if (providerName.equalsIgnoreCase(SOCIAL_PROVIDER_WECHAT)) {
                        socialButton.setClickable(false);
                    }
                    callSocialProvider(providerName);
                } else {
                    showProgressDialog();
                    RegistrationHelper.getInstance().initializeUserRegistration(mContext);
                }
            } else {
                enableControls(false);
                updateErrorMessage(mContext.getResources().getString(R.string.reg_NoNetworkConnection));
            }
        });
        return socialButton;
    }

    private void updateErrorMessage(String errorMessage) {
        mRegError.setError(errorMessage);
        scrollViewAutomatically(mRegError, mSvRootLayout);
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onConfigurationChanged");
        setCustomParams(config);
    }

    private void initUI(View view) {
        consumeTouch(view);
        continueWithouAccount.setVisibility(View.GONE);
        setContentConfig();
        updateCountryText(RegistrationHelper.getInstance().getLocale(mContext).getDisplayCountry());
        linkifyPrivacyPolicy(privacyPolicy, privacyClickListener);
        linkifyPrivacyPolicy(privacyPolicy2, privacyClickListener);

        homePresenter.updateHomeControls();
        homePresenter.initServiceDiscovery();
        homePresenter.configureCountrySelection();
    }


    private void setContentConfig() {
        if (null != getRegistrationFragment().getContentConfiguration()) {
            if (null != getRegistrationFragment().getContentConfiguration().getValueForRegistrationTitle()) {
                mTvWelcome.setText(getRegistrationFragment().getContentConfiguration().getValueForRegistrationTitle());
            }
            if (null != getRegistrationFragment().getContentConfiguration().getValueForRegistrationDescription()) {
                mTvWelcomeDesc.setText(getRegistrationFragment().getContentConfiguration().getValueForRegistrationDescription());
            }
            if (getRegistrationFragment().getContentConfiguration().getEnableContinueWithouAccount()) {
                continueWithouAccount.setVisibility(View.VISIBLE);
            }
        }
    }


    private void handleCountrySelection() {
        if (homePresenter.isNetworkAvailable()) {
            ArrayList<Country> rawMasterList = homePresenter.getAllCountries();
            CountrySelectionListener countryChangeListener = new CountrySelectionListener() {
                @Override
                public void onSelectCountry(String name, String code) {
                    RLog.i(RLog.ONCLICK, "HomeFragment :Country Name: " + name + " - Code: ");
                    homePresenter.changeCountry(name, code.trim().toUpperCase());
                    getRegistrationFragment().onBackPressed();
                }
            };
            CountrySelectionFragment picker = new CountrySelectionFragment(countryChangeListener, rawMasterList, homePresenter.getRecentSelectedCountry());
            getRegistrationFragment().addFragment(picker);
        } else {
            homePresenter.updateHomeControls();
        }
    }


    @Override
    public void updateAppLocale(String localeString, String countryName) {
        RLog.d(RLog.SERVICE_DISCOVERY, "STRING S : " + localeString);
        String localeArr[] = localeString.toString().split("_");
        RegistrationHelper.getInstance().initializeUserRegistration(mContext);
        RegistrationHelper.getInstance().setLocale(localeArr[0].trim(), localeArr[1].trim());
        RLog.d(RLog.SERVICE_DISCOVERY, "Change Country code :" + RegistrationHelper.getInstance().getCountryCode());
        handleSocialProviders(RegistrationHelper.getInstance().getCountryCode());
        updateCountryText(countryName);
        homePresenter.addToRecent(RegistrationHelper.getInstance().getCountryCode());
    }

    @Override
    public void localeServiceDiscoveryFailed() {
        hideProgressDialog();
        updateErrorMessage(mContext.getString(R.string.reg_Generic_Network_Error));
    }

    @Override
    public void countryChangeStarted() {
        showProgressDialog();
    }

    private void launchSignInFragment() {
        trackPage(AppTaggingPages.SIGN_IN_ACCOUNT);
        if (UserRegistrationInitializer.getInstance().isJanrainIntialized()) {
            getRegistrationFragment().addFragment(new SignInAccountFragment());
            return;
        }
        if (homePresenter.isNetworkAvailable()) {
            showProgressDialog();
            homePresenter.setFlowDeligate(HomePresenter.FLOWDELIGATE.LOGIN);
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
        }
    }

    private void launchCreateAccountFragment() {
        trackPage(AppTaggingPages.CREATE_ACCOUNT);
        if (UserRegistrationInitializer.getInstance().isJanrainIntialized()) {
            showCreateAccountFragment();
            return;
        }
        if (homePresenter.isNetworkAvailable()) {
            showProgressDialog();
            homePresenter.setFlowDeligate(HomePresenter.FLOWDELIGATE.CREATE);
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
        }
    }

    private void showCreateAccountFragment() {
        getRegistrationFragment().addFragment(new CreateAccountFragment());
    }

//    private void makeProgressVisible() {
//        mSvRootLayout.setVisibility(View.INVISIBLE);
//        usr_StartScreen_privacyNotice_country_LinearLayout.setVisibility(View.INVISIBLE);
//        spinnerLayout.setVisibility(View.VISIBLE);
//    }
//
//    private void makeProgressInvisible() {
//        mSvRootLayout.setVisibility(View.VISIBLE);
//        usr_StartScreen_privacyNotice_country_LinearLayout.setVisibility(View.VISIBLE);
//        spinnerLayout.setVisibility(View.INVISIBLE);
//    }

    private void callSocialProvider(String providerName) {
        RLog.d("HomeFragment", "callSocialProvider method provider name :" + providerName);
        if (homePresenter.isNetworkAvailable()) {
            trackMultipleActionsLogin(providerName);
            homePresenter.trackSocialProviderPage();
            if (!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
                if (providerName.equalsIgnoreCase(SOCIAL_PROVIDER_WECHAT)) {
                    if (homePresenter.isWeChatAuthenticate()) {
                        homePresenter.startWeChatAuthentication();
                    } else {
                        hideProgressDialog();
                    }
                    return;
                } else {
                    showProgressDialog();
                    homePresenter.setProvider(providerName);
                    homePresenter.startSocialLogin();
                }
                return;
            }
            showProgressDialog();
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
        }
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        RLog.i(RLog.CALLBACK, "HomeFragment : onLoginSuccess lenth"+width);
        applyParams(config, usr_startScreen_baseLayout_LinearLayout, width);
        applyParams(config, usr_StartScreen_privacyNotice_country_LinearLayout, width);
        applyParams(config, usr_StartScreen_privacyNotice_country_LinearLayout2, width);
        handlePrivacyPolicyAndCountryView(width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_DLS_StratScreen_Nav_Title_Txt;
    }

    private ClickableSpan countryClickListener = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            if (mRegError.isShown()) mRegError.hideError();
            handleCountrySelection();
        }
    };

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

    private void handlePrivacyPolicy() {
        if( RegistrationConfiguration.getInstance().getUserRegistrationUIEventListener()!=null){

            RegistrationConfiguration.getInstance().getUserRegistrationUIEventListener().
                    onPrivacyPolicyClick(getRegistrationFragment().getParentActivity());
        }else {
            RegUtility.showErrorMessage(getRegistrationFragment().getParentActivity());
        }

    }

    private void handleLoginSuccess() {
        trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                AppTagingConstants.SUCCESS_LOGIN);
        RLog.i(RLog.CALLBACK, "HomeFragment : onLoginSuccess");
        hideProgressDialog();
        enableControls(true);
        homePresenter.navigateToScreen();
    }

    private void handleLoginFailedWithError(UserRegistrationFailureInfo
                                                    userRegistrationFailureInfo) {
        RLog.i(RLog.CALLBACK, "HomeFragment : onLoginFailedWithError : code :" +
                userRegistrationFailureInfo.getErrorCode());
        trackPage(AppTaggingPages.HOME);
        hideProgressDialog();
        enableControls(true);
        if (userRegistrationFailureInfo.getErrorCode() == AUTHENTICATION_FAILED) {
            updateErrorMessage(mContext.getString(R.string.reg_JanRain_Server_Connection_Failed));
        } else {
            updateErrorMessage(mContext.getString(R.string.reg_Generic_Network_Error));
        }
    }


    private void launchAlmostDoneFragment(JSONObject prefilledRecord, String socialRegistrationToken) {
        trackPage(AppTaggingPages.ALMOST_DONE);
        getRegistrationFragment().addAlmostDoneFragment(prefilledRecord, homePresenter.getProvider(),
                socialRegistrationToken);
    }


    private void handleLoginFailedWithMergeFlowError(String existingProvider, String mergeToken, String conflictingIdentityProvider, String emailId) {
        hideProgressDialog();
        enableControls(true);
        if (homePresenter.isMergePossible(existingProvider)) {
            launchMergeAccountFragment(mergeToken, conflictingIdentityProvider, emailId);
        } else {
            homePresenter.setProvider(existingProvider);
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


    private void handleContinueSocialProviderLoginFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.i(RLog.CALLBACK, "HomeFragment : onContinueSocialProviderLoginFailure");
        homePresenter.trackSocialProviderPage();
        hideProgressDialog();
        enableControls(true);
    }


    private ProgressDialog mProgressDialog;


    private void showProgressDialog() {
        if (!(getActivity().isFinishing())) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(getActivity(), R.style.reg_Custom_loaderTheme);
                mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
                mProgressDialog.setCancelable(false);
            }
            mProgressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }


    private void enableControls(boolean clickableState) {
        if (clickableState) {
            mRegError.hideError();
        }
        handleBtnClickableStates(clickableState);
    }


    private void handleBtnClickableStates(boolean state) {
        mBtnCreateAccount.setEnabled(state);
        enableSocialProviders(state);
        mBtnMyPhilips.setEnabled(state);
    }

    private void enableSocialProviders(boolean enableState) {
        for (int i = 0; i < mLlSocialProviderBtnContainer.getChildCount(); i++) {
            mLlSocialProviderBtnContainer.getChildAt(i).setEnabled(enableState);
            mLlSocialProviderBtnContainer.getChildAt(i).setClickable(enableState);
        }
    }


    @Override
    public void enableControlsOnNetworkConnectionArraival() {
        mRegError.hideError();
        mBtnCreateAccount.setEnabled(true);
        enableSocialProviders(true);
        mBtnMyPhilips.setEnabled(true);
        mCountryDisplay.setEnabled(true);
        mCountryDisplay2.setEnabled(true);
    }

    @Override
    public void disableControlsOnNetworkConnectionGone() {
        hideProgressDialog();
        mBtnCreateAccount.setEnabled(false);
        enableSocialProviders(false);
        mBtnMyPhilips.setEnabled(false);
        mCountryDisplay.setEnabled(false);
        mCountryDisplay2.setEnabled(false);
        updateErrorMessage(mContext.getResources().getString(R.string.reg_NoNetworkConnection));
    }


    @OnClick(R2.id.usr_startScreen_createAccount_Button)
    void createAccountButtonClick() {
        if (mRegError.isShown()) mRegError.hideError();
        trackMultipleActionsRegistration();
        launchCreateAccountFragment();
    }


    @OnClick(R2.id.usr_startScreen_Login_Button)
    void myPhilipsButtonClick() {
        if (mRegError.isShown()) mRegError.hideError();
        trackMultipleActionsLogin(AppTagingConstants.MY_PHILIPS);
        launchSignInFragment();
    }

    @OnClick(R2.id.usr_StartScreen_Skip_Button)
    void skipButtonClick() {
        if (mRegError.isShown()) mRegError.hideError();

        if (RegistrationConfiguration.getInstance().getUserRegistrationUIEventListener() != null) {
            RegistrationConfiguration.getInstance().getUserRegistrationUIEventListener().
                    onUserRegistrationComplete(getRegistrationFragment().getParentActivity());
        }else {
            RegUtility.showErrorMessage(getRegistrationFragment().getParentActivity());
        }
    }


    private void linkifyPrivacyPolicy(TextView pTvPrivacyPolicy, ClickableSpan span) {
        String privacy = pTvPrivacyPolicy.getText().toString();
        SpannableString spannableString = new SpannableString(privacy);

        spannableString.setSpan(span, 0, privacy.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        removeUnderlineFromLink(spannableString);

        pTvPrivacyPolicy.setText(spannableString);
        pTvPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());
        pTvPrivacyPolicy.setLinkTextColor(ContextCompat.getColor(getContext(),
                R.color.reg_hyperlink_highlight_color));
        pTvPrivacyPolicy.setHighlightColor(ContextCompat.getColor(getContext(),
                android.R.color.transparent));
    }


    private void updateCountryText(String text) {
        mCountryDisplay.setText(String.format("%s %s", getString(R.string.reg_Country)+":", text));
        mCountryDisplay2.setText(String.format("%s %s", getString(R.string.reg_Country)+":", text));

        linkifyPrivacyPolicy(mCountryDisplay, countryClickListener);
        linkifyPrivacyPolicy(mCountryDisplay2, countryClickListener);

    }

    @Override
    public void hideCountrySelctionLabel() {

        mCountryDisplay.setVisibility(View.INVISIBLE);
        mCountryDisplay2.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showCountrySelctionLabel() {
        mCountryDisplay.setVisibility(View.VISIBLE);
        mCountryDisplay2.setVisibility(View.VISIBLE);
    }


    @Override
    public void updateHomeCountry(String selectedCountryCode) {
        updateCountryText(new Locale("", selectedCountryCode).getDisplayCountry());
        handleSocialProviders(selectedCountryCode);
    }


    private void handleSocialProviders(final String countryCode) {
        RLog.d("HomeFragment : ", "handleSocialProviders method country code : " + countryCode);
        mLlSocialProviderBtnContainer.post(new Runnable() {

            @Override
            public void run() {
                mLlSocialProviderBtnContainer.removeAllViews();
                mLlSocialProviderBtnContainer.invalidate();
                List<String> providers = homePresenter.getProviders(countryCode);
                if (null != providers) {
                    for (int i = 0; i < providers.size(); i++) {
                        inflateEachProviderBtn(providers.get(i));
                    }
                    RLog.d("HomeFragment", "social providers : " + providers);
                }
                UIOverProvidersSize(providers);
                homePresenter.updateHomeControls();
            }

            private void UIOverProvidersSize(List<String> providers) {
                if (providers != null && providers.size() > 0) {
                    if (!RegistrationConfiguration.getInstance().getPrioritisedFunction().equals(RegistrationFunction.Registration)) {
                        Label usr_startScreen_Or_Label = (Label) view.findViewById(R.id.usr_startScreen_Or_Label);
                        usr_startScreen_Or_Label.setVisibility(View.VISIBLE);
                    }
                    usr_startScreen_orLoginWith_Label.setVisibility(View.VISIBLE);
                } else {
                    if (!RegistrationConfiguration.getInstance().getPrioritisedFunction().equals(RegistrationFunction.Registration)) {
                        Label usr_startScreen_Or_Label = (Label) view.findViewById(R.id.usr_startScreen_Or_Label);
                        usr_startScreen_Or_Label.setVisibility(View.INVISIBLE);
                    }
                    usr_startScreen_orLoginWith_Label.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    private void inflateEachProviderBtn(String provider) {

        try {
//            String providerName = "reg_" + provider;
//            String providerDrawable = "uid_social_media_" + provider + "_icon,";

            int drawableId = 0;
            if (provider.equals(SOCIAL_PROVIDER_FACEBOOK)) {
                drawableId = R.drawable.uid_social_media_facebook_icon;
            } else if (provider.equals(SOCIAL_PROVIDER_GOOGLEPLUS)) {
                drawableId = R.drawable.uid_social_media_googleplus_icon;
            } else if (provider.equals(SOCIAL_PROVIDER_WECHAT)) {
                drawableId = R.drawable.uid_social_media_wechat_icon;
            }
//            int resourceId = getRegistrationFragment().getParentActivity().getResources().getIdentifier(providerName, "string",
//                    getRegistrationFragment().getParentActivity().getPackageName());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMarginStart(16);
            params.setMarginEnd(16);

            mLlSocialProviderBtnContainer.addView(getProviderBtn(provider, drawableId), params);
            mLlSocialProviderBtnContainer.invalidate();
        } catch (Exception e) {
            RLog.e("HomeFragment", "Inflate Buttons exception :" + e.getMessage());
        }
    }

    private ClickableSpan privacyClickListener = new ClickableSpan() {

        @Override
        public void onClick(View widget) {
            if (mRegError.isShown()) mRegError.hideError();
            handlePrivacyPolicy();
        }
    };


    @Override
    public Activity getActivityContext() {
        return getRegistrationFragment().getParentActivity();
    }

    @Override
    public void registerWechatReceiver() {
        LocalBroadcastManager.getInstance(getActivityContext()).registerReceiver(homePresenter.getMessageReceiver(),
                new IntentFilter(RegConstants.WE_CHAT_AUTH));
    }

    @Override
    public void unRegisterWechatReceiver() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(homePresenter.getMessageReceiver());
    }

    @Override
    public void wechatAppNotInstalled() {
        makeProviderButtonsClickable();
        final String formatedString = String.format(mContext.getText(R.string.reg_App_NotInstalled_AlertMessage).toString(),
                mContext.getText(R.string.reg_wechat));
        Toast.makeText(mContext, formatedString
                , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void wechatAppNotSupported() {
        makeProviderButtonsClickable();
        Toast.makeText(mContext, mContext.getText(R.string.reg_Provider_Not_Supported)
                , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void wechatAuthenticationSuccessParsingError() {
        hideProgressDialog();
    }

    @Override
    public void wechatAuthenticationFailError() {
        trackPage(AppTaggingPages.HOME);
        hideProgressDialog();
        enableControls(true);
        updateErrorMessage(mContext.
                getString(R.string.reg_JanRain_Server_Connection_Failed));
    }

    @Override
    public void createSocialAccount(JSONObject prefilledRecord, String socialRegistrationToken) {
        hideProgressDialog();
        enableControls(true);
        launchAlmostDoneFragment(prefilledRecord, socialRegistrationToken);
    }

    @Override
    public void mergeSocialAccount(String existingProvider, String mergeToken, String conflictingIdentityProvider, String emailId) {
        handleLoginFailedWithMergeFlowError(existingProvider, mergeToken, conflictingIdentityProvider, emailId);
    }

    @Override
    public void loginFailed(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        handleLoginFailedWithError(userRegistrationFailureInfo);
    }

    @Override
    public void loginSuccess() {
        handleLoginSuccess();
    }

    @Override
    public void SocialLoginFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        handleContinueSocialProviderLoginFailure(userRegistrationFailureInfo);
    }

    @Override
    public void completeSocialLogin() {
        homePresenter.completeRegistation();
        hideProgressDialog();
        enableControls(true);
    }

    @Override
    public void initSuccess() {
        hideProgressDialog();
    }

    @Override
    public void initFailed() {
        hideProgressDialog();
    }

    @Override
    public void navigateToCreateAccount() {
        showCreateAccountFragment();
    }

    @Override
    public void navigateToLogin() {
        getRegistrationFragment().addFragment(new SignInAccountFragment());
    }

    @Override
    public void startWeChatAuthentication() {
        hideProgressDialog();
        hideProgressDialog();
        homePresenter.startWeChatAuthentication();
    }

    @Override
    public void switchToControlView() {
        hideProgressDialog();
    }

    @Override
    public void socialProviderLogin() {
        showProgressDialog();
        homePresenter.startSocialLogin();
    }

    @Override
    public void wechatAutheticationCanceled() {
        hideProgressDialog();
    }

    @Override
    public void startWeChatLogin() {
        showProgressDialog();
        homePresenter.handleWeChatCode();
    }

    @Override
    public void naviagteToAccountActivationScreen() {
        getRegistrationFragment().launchAccountActivationFragmentForLogin();
    }

    @Override
    public void naviagteToMobileAccountActivationScreen() {
        getRegistrationFragment().addFragment(new MobileVerifyCodeFragment());
        trackPage(AppTaggingPages.MOBILE_VERIFY_CODE);
    }

    @Override
    public void navigateToAcceptTermsScreen() {
        trackPage(AppTaggingPages.ALMOST_DONE);
        getRegistrationFragment().addAlmostDoneFragmentforTermsAcceptance();
    }

    @Override
    public void registrationCompleted() {
        getRegistrationFragment().userRegistrationComplete();
    }

    private void makeProviderButtonsClickable() {
        ViewGroup providerButtonGroup = mLlSocialProviderBtnContainer;
        for (int i = 0; i < providerButtonGroup.getChildCount(); i++) {
            View childView = providerButtonGroup.getChildAt(i);
            if (childView instanceof XProviderButton) {
                childView.setClickable(true);
            }
        }
    }
    private int viewLength (TextView text, String newText) {
        float textWidth = text.getPaint().measureText(newText);
        return Math.round(textWidth);
    }
   private void handlePrivacyPolicyAndCountryView(int width) {
        int length = viewLength(mCountryDisplay, mCountryDisplay.getText().toString()) +
                viewLength(privacyPolicy, privacyPolicy.getText().toString());
        if ((width *.9) > length) {
            usr_StartScreen_privacyNotice_country_LinearLayout2.setVisibility(View.GONE);
            usr_StartScreen_privacyNotice_country_LinearLayout.setVisibility(View.VISIBLE);

        } else {
            usr_StartScreen_privacyNotice_country_LinearLayout.setVisibility(View.GONE);
            usr_StartScreen_privacyNotice_country_LinearLayout2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void genericError() {
        hideProgressDialog();
        trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                AppTagingConstants.TECHNICAL_ERROR);
        RLog.i(RLog.CALLBACK, "HomeFragment error");
        enableControls(true);
        updateErrorMessage(mContext.getString(R.string.reg_Generic_Network_Error));
    }
}
