package com.philips.cdp.registration.ui.traditional;

import android.app.*;

import com.philips.cdp.registration.dao.*;

import org.json.*;

interface HomeContract {
  

    void enableControlsOnNetworkConnectionArraival();

    void disableControlsOnNetworkConnectionGone();

    void hideCountrySelctionLabel();

    void showCountrySelctionLabel();

    void updateHomeCountry(String selectedCountryCode);

    Activity getActivityContext();

    void registerWechatReceiver();

    void unRegisterWechatReceiver();

    void wechatAppNotInstalled();

    void wechatAppNotSupported();

    void wechatAuthenticationSuccessParsingError();

    void wechatAuthenticationFailError();

    void createSocialAccount(JSONObject prefilledRecord, String socialRegistrationToken);

    void mergeSocialAccount(String existingProvider, String mergeToken, String conflictingIdentityProvider, String emailId);

    void loginFailed(UserRegistrationFailureInfo userRegistrationFailureInfo);

    void loginSuccess();

    void SocialLoginFailure(UserRegistrationFailureInfo userRegistrationFailureInfo);

    void completeSocialLogin();

    void initSuccess();

    void initFailed();

    void navigateToCreateAccount();

    void navigateToLogin();

    void startWeChatAuthentication();

    void switchToControlView();

    void socialProviderLogin();

    void wechatAutheticationCanceled();

    void startWeChatLogin();

    void naviagteToAccountActivationScreen();

    void naviagteToMobileAccountActivationScreen();

    void navigateToAcceptTermsScreen();

    void registrationCompleted();

    void updateAppLocale(String verificationUrl, String countryName);

    void localeServiceDiscoveryFailed();

    void countryChangeStarted();

    void genericError();
}
