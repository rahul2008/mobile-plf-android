package com.philips.cdp.registration.injection;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.BaseConfiguration;
import com.philips.cdp.registration.configuration.AppConfiguration;
import com.philips.cdp.registration.configuration.HSDPConfiguration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.controller.UpdateUserRecord;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.RegistrationSettingsURL;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.social.AlmostDoneFragment;
import com.philips.cdp.registration.ui.social.MergeAccountFragment;
import com.philips.cdp.registration.ui.social.MergeSocialToSocialAccountFragment;
import com.philips.cdp.registration.ui.traditional.AccountActivationFragment;
import com.philips.cdp.registration.ui.traditional.CreateAccountFragment;
import com.philips.cdp.registration.ui.traditional.ForgotPasswordFragment;
import com.philips.cdp.registration.ui.traditional.HomeFragment;
import com.philips.cdp.registration.ui.traditional.LogoutFragment;
import com.philips.cdp.registration.ui.traditional.MarketingAccountFragment;
import com.philips.cdp.registration.ui.traditional.RegistrationFragment;
import com.philips.cdp.registration.ui.traditional.SignInAccountFragment;
import com.philips.cdp.registration.ui.traditional.WelcomeFragment;
import com.philips.cdp.registration.ui.traditional.mobile.AddSecureEmailPresenter;
import com.philips.cdp.registration.ui.traditional.mobile.MobileForgotPasswordVerifyCodeFragment;
import com.philips.cdp.registration.ui.traditional.mobile.MobileVerifyCodeFragment;
import com.philips.cdp.registration.ui.traditional.mobile.MobileVerifyCodePresenter;
import com.philips.cdp.registration.ui.utils.NetworkStateReceiver;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.appinfra.timesync.TimeInterface;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class, AppInfraModule.class, ConfigurationModule.class, UserModule.class})
public interface RegistrationComponent {

    NetworkUtility getNetworkUtility();

    TimeInterface getTimeInterface();

    LoggingInterface getLoggingInterface();

    ServiceDiscoveryInterface getServiceDiscoveryInterface();

    ABTestClientInterface getAbTestClientInterface();

    AppTaggingInterface getAppTaggingInterface();

    void inject(User user);

    void inject(HsdpUser hsdpUser);

    void inject(RegistrationHelper registrationHelper);

    void inject(AlmostDoneFragment almostDoneFragment);

    void inject(RegistrationFragment registrationFragment);

    void inject(NetworkStateReceiver networkStateReceiver);

    void inject(MergeAccountFragment mergeAccountFragment);

    void inject(MergeSocialToSocialAccountFragment mergeSocialToSocialAccountFragment);

    void inject(AccountActivationFragment accountActivationFragment);

    void inject(CreateAccountFragment createAccountFragment);

    void inject(ForgotPasswordFragment forgotPasswordFragment);

    void inject(HomeFragment homeFragment);

    void inject(LogoutFragment logoutFragment);

    void inject(MarketingAccountFragment marketingAccountFragment);

    void inject(MobileForgotPasswordVerifyCodeFragment mobileForgotPasswordVerifyCodeFragment);

    void inject(MobileVerifyCodeFragment mobileVerifyCodeFragment);

    void inject(SignInAccountFragment signInAccountFragment);

    void inject(WelcomeFragment welcomeFragment);

    void inject(HSDPConfiguration hsdpConfiguration);

    void inject(RegistrationConfiguration registrationConfiguration);

    void inject(RegistrationSettingsURL registrationSettingsURL);

    void inject(AppConfiguration appConfiguration);

    void inject(BaseConfiguration baseConfiguration);

    void inject(UserRegistrationInitializer userRegistrationInitializer);

    void inject(UpdateUserRecord updateUserRecord);

    void inject(MobileVerifyCodePresenter mobileVerifyCodePresenter);

    void inject(AddSecureEmailPresenter addSecureEmailPresenter);
}
