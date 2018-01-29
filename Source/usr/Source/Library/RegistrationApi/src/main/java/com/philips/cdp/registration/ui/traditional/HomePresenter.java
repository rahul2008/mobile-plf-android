package com.philips.cdp.registration.ui.traditional;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.infra.ServiceDiscoveryWrapper;
import com.philips.cdp.registration.app.tagging.AppTaggingPages;
import com.philips.cdp.registration.configuration.AppConfiguration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.Country;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.events.EventListener;
import com.philips.cdp.registration.events.NetworkStateListener;
import com.philips.cdp.registration.events.SocialProvider;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.listener.WeChatAuthenticationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.LoginFailureNotification;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegPreferenceUtility;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.cdp.registration.ui.utils.ThreadUtils;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.wechat.WeChatAuthenticator;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.philips.cdp.registration.app.tagging.AppTagging.trackPage;

public class HomePresenter implements NetworkStateListener, SocialProviderLoginHandler, EventListener {


    @Inject
    NetworkUtility networkUtility;

    @Inject
    AppConfiguration appConfiguration;

    @Inject
    ServiceDiscoveryInterface serviceDiscoveryInterface;

    @Inject
    ServiceDiscoveryWrapper serviceDiscoveryWrapper;


    @Inject
    User user;

    private HomeContract homeContract;


    public HomePresenter(HomeContract homeContract) {
        RegistrationConfiguration.getInstance().getComponent().inject(this);
        this.homeContract = homeContract;
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        EventHelper.getInstance()
                .registerEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);
        EventHelper.getInstance()
                .registerEventNotification(RegConstants.JANRAIN_INIT_FAILURE, this);
        EventHelper.getInstance()
                .registerEventNotification(RegConstants.WECHAT_AUTH, this);
    }

    public void cleanUp() {
        homeContract.unRegisterWechatReceiver();
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
        EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_SUCCESS,
                this);
        EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_FAILURE,
                this);
        EventHelper.getInstance().unregisterEventNotification(RegConstants.WECHAT_AUTH,
                this);
    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        if (isOnline) {
            homeContract.enableControlsOnNetworkConnectionArraival();
        } else {
            homeContract.disableControlsOnNetworkConnectionGone();
        }
    }


    public void updateHomeControls() {
        if (networkUtility.isNetworkAvailable()) {
            homeContract.enableControlsOnNetworkConnectionArraival();
        } else {
            homeContract.disableControlsOnNetworkConnectionGone();
        }
    }

    public void configureCountrySelection() {
        String mShowCountrySelection = appConfiguration.getShowCountrySelection();
        RLog.d(RLog.SERVICE_DISCOVERY, " Country Show Country Selection :" + mShowCountrySelection);
        if (mShowCountrySelection != null) {
            if (mShowCountrySelection.equalsIgnoreCase("false")) {
                homeContract.hideCountrySelctionLabel();
            } else {
                homeContract.showCountrySelctionLabel();
            }
        }
    }


    private ArrayList<Country> recentSelectedCountry = new ArrayList<>();

    public void addToRecent(String countryCode) {
        Country country = new Country(countryCode, new Locale("", countryCode).getDisplayCountry());
        recentSelectedCountry.add(0, country);
    }

    public ArrayList<Country> getRecentSelectedCountry() {
        return recentSelectedCountry;
    }


    public void initServiceDiscovery() {
        serviceDiscoveryInterface.getHomeCountry(new ServiceDiscoveryInterface.OnGetHomeCountryListener() {
            @Override
            public void onSuccess(String s, SOURCE source) {
                RLog.d(RLog.SERVICE_DISCOVERY, " Country Sucess :" + s);
                String selectedCountryCode;
                if (RegUtility.supportedCountryList().contains(s.toUpperCase())) {
                    selectedCountryCode = s.toUpperCase();
                } else {
                    selectedCountryCode = RegUtility.getFallbackCountryCode();
                }
                addToRecent(selectedCountryCode);
                serviceDiscoveryInterface.setHomeCountry(selectedCountryCode);
                homeContract.updateHomeCountry(selectedCountryCode);

            }

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                RLog.d(RLog.SERVICE_DISCOVERY, " Country Error :" + s);
                String selectedCountryCode = RegUtility.getFallbackCountryCode();
                addToRecent(selectedCountryCode);
                serviceDiscoveryInterface.setHomeCountry(selectedCountryCode);
                homeContract.updateHomeCountry(selectedCountryCode);
            }
        });
    }

    public List<String> getProviders(String countryCode) {
        return RegistrationConfiguration.getInstance().getProvidersForCountry(countryCode);
    }

    public boolean isWeChatAppRegistered() {
        return isWeChatAppRegistered;
    }

    private boolean isWeChatAppRegistered;
    String mWeChatAppId;
    String mWeChatAppSecret;
    IWXAPI mWeChatApi;


    public void registerWeChatApp() {
        mWeChatAppId = appConfiguration.getWeChatAppId();
        mWeChatAppSecret = appConfiguration.getWeChatAppSecret();
        RLog.d("WECHAT", "weChatId " + mWeChatAppId + " WechatSecrete " + mWeChatAppSecret);

        if (mWeChatAppId != null && mWeChatAppSecret != null) {
            mWeChatApi = WXAPIFactory.createWXAPI(homeContract.getActivityContext(),
                    mWeChatAppId, false);
            mWeChatApi.registerApp(mWeChatAppSecret);
            isWeChatAppRegistered = mWeChatApi.registerApp(mWeChatAppId);
            homeContract.registerWechatReceiver();
        }
    }

    public boolean isWeChatAuthenticate() {
        if (!mWeChatApi.isWXAppInstalled()) {
            homeContract.wechatAppNotInstalled();
            return false;
        }
        if (!mWeChatApi.isWXAppSupportAPI()) {
            homeContract.wechatAppNotSupported();
            return false;
        }
        return isWeChatAppRegistered;
    }

    public void startWeChatAuthentication() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "123456";
        mWeChatApi.sendReq(req);
    }


    public void handleWeChatCode() {
        RLog.d("WECHAT", String.format("WeChat Code: ", mWeChatCode));
        WeChatAuthenticator weChatAuthenticator = new WeChatAuthenticator();
        weChatAuthenticator.getWeChatResponse(mWeChatAppId, mWeChatAppSecret, mWeChatCode,
                new WeChatAuthenticationListener() {
                    @Override
                    public void onSuccess(final JSONObject jsonObj) {
                        try {
                            final String token = jsonObj.getString("access_token");
                            final String openId = jsonObj.getString("openid");
                            RLog.d("WECHAT body", "token " + token + " openid " + openId);
                            user.loginUserUsingSocialNativeProvider(homeContract.getActivityContext(),
                                    "wechat", token, openId, HomePresenter.this, "");
                        } catch (JSONException e) {
                            homeContract.wechatAuthenticationSuccessParsingError();
                        }
                    }

                    @Override
                    public void onFail() {
                        homeContract.wechatAuthenticationFailError();
                    }
                });
    }


    @Override
    public void onLoginFailedWithTwoStepError(final JSONObject prefilledRecord,
                                              final String socialRegistrationToken) {
        RLog.d("HomeFragment", "Login failed with two step error" + "JSON OBJECT :"
                + prefilledRecord);
        EventBus.getDefault().post(new LoginFailureNotification());
        homeContract.createSocialAccount(prefilledRecord, socialRegistrationToken);
    }


    @Override
    public void onLoginFailedWithMergeFlowError(final String mergeToken, final String existingProvider,
                                                final String conflictingIdentityProvider, String conflictingIdpNameLocalized,
                                                String existingIdpNameLocalized, final String emailId) {

        EventBus.getDefault().post(new LoginFailureNotification());
        homeContract.mergeSocialAccount(existingProvider, mergeToken, conflictingIdentityProvider, emailId);


    }


    @Override
    public void onContinueSocialProviderLoginSuccess() {
        RLog.d(RLog.CALLBACK, "HomeFragment : onContinueSocialProviderLoginSuccess");

        homeContract.completeSocialLogin();

    }

    @Override
    public void onContinueSocialProviderLoginFailure(
            final UserRegistrationFailureInfo userRegistrationFailureInfo) {
        EventBus.getDefault().post(new LoginFailureNotification());
        homeContract.SocialLoginFailure(userRegistrationFailureInfo);

    }

    @Override
    public void onLoginSuccess() {
        homeContract.loginSuccess();
    }

    @Override
    public void onLoginFailedWithError(final UserRegistrationFailureInfo userRegistrationFailureInfo) {
        EventBus.getDefault().post(new LoginFailureNotification());
        homeContract.loginFailed(userRegistrationFailureInfo);
    }

    public ArrayList<Country> getAllCountries() {
        try {
            ArrayList<Country> allCountriesList = new ArrayList<Country>();
            String[] recourseList = RegUtility.supportedCountryList().toArray(new String[RegUtility.supportedCountryList().size()]);
            for (String aRecourseList : recourseList) {
                Country country = new Country(aRecourseList, new Locale("", aRecourseList).getDisplayCountry());
                allCountriesList.add(country);
            }
            return allCountriesList;

        } catch (Exception e) {
            return null;
        }

    }


    private HomePresenter.FLOWDELIGATE deligateFlow = HomePresenter.FLOWDELIGATE.DEFAULT;

    public void setFlowDeligate(FLOWDELIGATE deligateFlow) {
        this.deligateFlow = deligateFlow;
    }

    public void navigateToScreen() {

        if (isEmailAvailable() && isMobileNoAvailable() && !isEmailVerified()) {
            homeContract.naviagteToAccountActivationScreen();
            return;
        }
        if ((isEmailVerified() || isMobileVerified())) {
            completeRegistation();
        } else {
            if (isEmailAvailable()) {
                homeContract.naviagteToAccountActivationScreen();
            } else if (isMobileNoAvailable() && !isMobileVerified()){
                homeContract.naviagteToMobileAccountActivationScreen();
            } else {
                homeContract.genericError();
            }
        }

    }


    public void completeRegistation() {
        String emailorMobile;
        if (FieldsValidator.isValidEmail(user.getEmail())) {
            emailorMobile = user.getEmail();
        } else {
            emailorMobile = user.getMobile();
        }
        if (emailorMobile != null && RegistrationConfiguration.getInstance().
                isTermsAndConditionsAcceptanceRequired() &&
                !RegPreferenceUtility.getPreferenceValue(homeContract.getActivityContext(), RegConstants.TERMS_N_CONDITIONS_ACCEPTED, emailorMobile) || !user.getReceiveMarketingEmail()) {
            homeContract.navigateToAcceptTermsScreen();
            return;
        }
        homeContract.registrationCompleted();
    }



    public void changeCountry(String countryName, String countryCode) {
        setFlowDeligate(HomePresenter.FLOWDELIGATE.DEFAULT);
        if (networkUtility.isNetworkAvailable()) {
            serviceDiscoveryInterface.setHomeCountry(countryCode);
            RegistrationHelper.getInstance().setCountryCode(countryCode);
            RLog.d(RLog.SERVICE_DISCOVERY, " Country :" + countryCode.length());
            homeContract.countryChangeStarted();
            RLog.d(RLog.SERVICE_DISCOVERY, " Country :" + RegistrationHelper.getInstance().getCountryCode());
            getLocaleServiceDiscovery(countryName);
        }
    }

    public boolean isNetworkAvailable() {
        return networkUtility.isNetworkAvailable();
    }

    public enum FLOWDELIGATE {
        DEFAULT, CREATE, LOGIN, SOCIALPROVIDER;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public boolean isMergePossible(String provider) {
        return user.handleMergeFlowError(provider);
    }

    public String getProvider() {
        return provider;
    }

    private String provider;

    String mWeChatCode;

    @Override
    public void onEventReceived(String event) {
        RLog.d(RLog.EVENT_LISTENERS, "HomeFragment :onCounterEventReceived" +
                " isHomeFragment :onCounterEventReceived is : " + event);
        if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
            homeContract.initSuccess();
            if (deligateFlow == FLOWDELIGATE.LOGIN) {
                homeContract.navigateToLogin();
            }
            if (deligateFlow == FLOWDELIGATE.CREATE) {
                homeContract.navigateToCreateAccount();
            }
            if (deligateFlow == FLOWDELIGATE.SOCIALPROVIDER) {
                homeContract.handleBtnClickableStates(false);
                if (provider.equalsIgnoreCase("wechat")) {
                    if (isWeChatAuthenticate()) {
                        homeContract.startWeChatAuthentication();
                    } else {
                        homeContract.switchToControlView();
                    }
                } else {
                    homeContract.socialProviderLogin();
                }
            }
            deligateFlow = FLOWDELIGATE.DEFAULT;
        } else if (RegConstants.JANRAIN_INIT_FAILURE.equals(event)) {
            homeContract.initFailed();
            deligateFlow = FLOWDELIGATE.DEFAULT;
        } else if (RegConstants.WECHAT_AUTH.equals(event)) {
            if (mWeChatCode != null) {
                homeContract.startWeChatLogin();
            }
        }
    }

    public void startSocialLogin() {
        user.loginUserUsingSocialProvider(homeContract.getActivityContext(), provider, this, null);
    }

    public void trackSocialProviderPage() {
        if (provider == null) {
            return;
        }
        if (provider.equalsIgnoreCase(SocialProvider.FACEBOOK)) {
            trackPage(AppTaggingPages.FACEBOOK);
        } else if (provider.equalsIgnoreCase(SocialProvider.GOOGLE_PLUS)) {
            trackPage(AppTaggingPages.GOOGLE_PLUS);
        } else if (provider.equalsIgnoreCase(SocialProvider.TWITTER)) {
            trackPage(AppTaggingPages.TWITTER);
        } else if (provider.equalsIgnoreCase(SocialProvider.WECHAT)) {
            trackPage(AppTaggingPages.WECHAT);
        }
    }


    public BroadcastReceiver getMessageReceiver() {
        return messageReceiver;
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int error_code = intent.getIntExtra(RegConstants.WECHAT_ERR_CODE, 0);
            String weChatCode = intent.getStringExtra(RegConstants.WECHAT_CODE);
            RLog.d("WECHAT", "BroadcastReceiver Got message: " + error_code + " " + weChatCode);
            switch (error_code) {
                case BaseResp.ErrCode.ERR_OK:
                    mWeChatCode = weChatCode;
                    ThreadUtils.postInMainThread(context, () -> EventHelper.getInstance().notifyEventOccurred(RegConstants.WECHAT_AUTH));
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    homeContract.wechatAutheticationCanceled();
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    homeContract.wechatAutheticationCanceled();
                    break;
            }
        }
    };


    public boolean isEmailAvailable() {
        boolean isEmailAvailable = user.getEmail() != null && FieldsValidator.isValidEmail(user.getEmail());
        return isEmailAvailable;
   }


    public boolean isMobileNoAvailable() {
        boolean isMobileNoAvailable = user.getMobile() != null && FieldsValidator.isValidMobileNumber(user.getMobile());
        return isMobileNoAvailable;
    }

    public boolean isEmailVerified() {
        boolean isEmailVerified = user.isEmailVerified();
        return isEmailVerified;
    }

    public boolean isMobileVerified() {
        boolean isMobileVerified =  user.isMobileVerified();
        return isMobileVerified;
    }


    public void getLocaleServiceDiscovery(final String countryName) {
        serviceDiscoveryWrapper.getServiceLocaleWithLanguagePreferenceSingle("userreg.janrain.api")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<String>() {
                    @Override
                    public void onSuccess(String verificationUrl) {
                        if (!verificationUrl.isEmpty()) {
                            homeContract.updateAppLocale(verificationUrl, countryName);
                            return;
                        }
                        getLocaleServiceDiscoveryByCountry(countryName);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getLocaleServiceDiscoveryByCountry(countryName);
                    }
                });
    }

    private void getLocaleServiceDiscoveryByCountry(String countryName) {
        serviceDiscoveryWrapper.getServiceLocaleWithCountryPreferenceSingle("userreg.janrain.api")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<String>() {
                    @Override
                    public void onSuccess(String verificationUrl) {
                        homeContract.updateAppLocale(verificationUrl, countryName);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ThreadUtils.postInMainThread(homeContract.getActivityContext(), () -> EventHelper.getInstance().notifyEventOccurred(RegConstants.JANRAIN_INIT_SUCCESS));
                       homeContract.localeServiceDiscoveryFailed();
                    }
                });
    }
}
