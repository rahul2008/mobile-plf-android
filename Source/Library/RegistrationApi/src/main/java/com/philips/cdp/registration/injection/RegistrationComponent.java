package com.philips.cdp.registration.injection;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.*;
import com.philips.cdp.registration.controller.*;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.social.*;
import com.philips.cdp.registration.ui.traditional.*;
import com.philips.cdp.registration.ui.traditional.mobile.*;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.appinfra.timesync.TimeInterface;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class, AppInfraModule.class, ConfigurationModule.class, RegistrationModule.class})
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

    void inject(MobileVerifyResendCodeFragment mobileVerifyResendCodeFragment);
    void inject(MobileVerifyResendCodePresenter mobileVerifyResendCodePresenter);



    void inject(AddSecureEmailPresenter addSecureEmailPresenter);

    void inject(AlmostDonePresenter almostDonePresenter);

    void inject(CreateAccountPresenter createAccountPresenter);

    void inject(RegisterTraditional registerTraditional);
}
