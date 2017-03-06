package com.philips.cdp.registration.injection;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.settings.RegistrationHelper;
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
import com.philips.cdp.registration.ui.traditional.mobile.MobileForgotPasswordVerifyCodeFragment;
import com.philips.cdp.registration.ui.traditional.mobile.MobileVerifyCodeFragment;
import com.philips.cdp.registration.ui.utils.NetworkStateReceiver;
import com.philips.cdp.registration.ui.utils.NetworkUtility;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class, AppInfraModule.class})
public interface RegistrationComponent {

    NetworkUtility getNetworkUtility();

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
}
