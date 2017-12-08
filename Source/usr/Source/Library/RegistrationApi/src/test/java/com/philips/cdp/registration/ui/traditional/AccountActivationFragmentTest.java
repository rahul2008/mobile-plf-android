package com.philips.cdp.registration.ui.traditional;

import android.view.InflateException;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.URInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by philips on 11/21/17.
 */

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class AccountActivationFragmentTest {

    AccountActivationFragment accountActivationFragment;

    @Mock
    RegistrationHelper registrationHelperMock;

    @Mock
    private RegistrationComponent mockRegistrationComponent;

    AccountActivationPresenter accountActivationPresenter;

    @Mock
    AccountActivationContract accountActivationContractMock;
    @Before
    public void setUp() throws Exception {

        initMocks(this);
        URInterface.setComponent(mockRegistrationComponent);
        accountActivationFragment= new AccountActivationFragment();
        accountActivationPresenter=new AccountActivationPresenter(accountActivationContractMock, registrationHelperMock);
    }

    @Test(expected = InflateException.class)
    public void should_startFragment() throws Exception {
        SupportFragmentTestUtil.startFragment(accountActivationFragment);
    }

}