package com.philips.cdp.registration.ui.traditional;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.UserLoginState;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.myaccount.UserDetailsFragment;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class RegistrationFragmentTest {


    private RegistrationFragment registrationFragment;
    @Mock
    private RegistrationComponent mockRegistrationComponent;
    @Mock
    private AppTaggingInterface mockAppTaggingInterface;
    @Mock
    private User mUser;
    @Spy
    private FragmentManager mFragmentManager;
    @Mock
    private FragmentTransaction fragmentTransaction;
    @Mock
    private URLaunchInput launchInput;


    @Before
    public void setUp() throws Exception {
        initMocks(this);
        registrationFragment = new RegistrationFragment();
        when(mockRegistrationComponent.getAppTaggingInterface()).thenReturn(mockAppTaggingInterface);
        when(mockAppTaggingInterface.createInstanceForComponent(anyString(), anyString())).thenReturn(mockAppTaggingInterface);

        RegistrationConfiguration.getInstance().setComponent(mockRegistrationComponent);
        AppTagging.init();
    }


    @Test
    public void shouldUserDetailsFragment_WhenHSDPConfigrationAvailable_LogginStateUSER_LOGGED_IN() {
        when(mFragmentManager.beginTransaction()).thenReturn(fragmentTransaction);
        when(mUser.getUserLoginState()).thenReturn(UserLoginState.USER_LOGGED_IN);
        Fragment fragment = registrationFragment.handleUseRLoginStateFragments(true, mFragmentManager, mUser);
        Assert.assertTrue(fragment instanceof UserDetailsFragment);
    }

    @Test
    public void shouldUserDetailsFragment_WhenHSDPConfigrationAvailable_LogginStateUSER_NOT_LOGGED_IN() {
        when(mFragmentManager.beginTransaction()).thenReturn(fragmentTransaction);
        when(mUser.getUserLoginState()).thenReturn(UserLoginState.USER_NOT_LOGGED_IN);
        Fragment fragment = registrationFragment.handleUseRLoginStateFragments(true, mFragmentManager, mUser);
        Assert.assertTrue(fragment instanceof HomeFragment);
    }

    @Test
    public void shouldHomeFragment_WhenHSDPConfigrationNotAvailable_LogginStateUSER_NOT_LOGGED_IN() {
        when(mFragmentManager.beginTransaction()).thenReturn(fragmentTransaction);
        when(mUser.getUserLoginState()).thenReturn(UserLoginState.USER_NOT_LOGGED_IN);
        Fragment fragment = registrationFragment.handleUseRLoginStateFragments(false, mFragmentManager, mUser);
        Assert.assertTrue(fragment instanceof HomeFragment);
    }

    @Test
    public void shouldMarketingAccountFragment_WhenHSDPConfigrationAvailable_LogginStateHSDP_PENDING() {
        when(mUser.getUserLoginState()).thenReturn(UserLoginState.PENDING_HSDP_LOGIN);
        Bundle bundle = new Bundle();
        bundle.putSerializable(RegConstants.REGISTRATION_LAUNCH_MODE, RegistrationLaunchMode.MARKETING_OPT);
        registrationFragment.setArguments(bundle);
        registrationFragment.onCreate(null);
        when(mFragmentManager.beginTransaction()).thenReturn(fragmentTransaction);
        Fragment fragment = registrationFragment.handleUseRLoginStateFragments(true, mFragmentManager,mUser);
        Assert.assertTrue(fragment instanceof MarketingAccountFragment);
    }

    @Test
    public void shouldHomeFragment_WhenHSDPConfigrationNotAvailable_LogginStateHSDP_PENDING() {
        when(mUser.getUserLoginState()).thenReturn(UserLoginState.PENDING_HSDP_LOGIN);
        Bundle bundle = new Bundle();
        bundle.putSerializable(RegConstants.REGISTRATION_LAUNCH_MODE, RegistrationLaunchMode.MARKETING_OPT);
        registrationFragment.setArguments(bundle);
        registrationFragment.onCreate(null);
        when(mFragmentManager.beginTransaction()).thenReturn(fragmentTransaction);
        Fragment fragment = registrationFragment.handleUseRLoginStateFragments(false, mFragmentManager,mUser);
        Assert.assertTrue(fragment instanceof HomeFragment);
    }
}