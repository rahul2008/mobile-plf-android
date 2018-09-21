package com.philips.platform.baseapp.screens.Optin;

import android.content.Context;

import com.philips.cdp.registration.ui.utils.RegistrationContentConfiguration;
import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static com.philips.platform.baseapp.screens.Optin.MarketingOptin.AB_TEST_OPTIN_IMAGE_KEY;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class MarketingOptinTest {

    private MarketingOptin marketingOptin;

    @Mock
    private AppInfraInterface appInfraInterfaceMock;
    @Mock
    private ABTestClientInterface abTestClientInterfaceMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(appInfraInterfaceMock.getAbTesting()).thenReturn(abTestClientInterfaceMock);
        Context context = RuntimeEnvironment.application;
        marketingOptin = new MarketingOptin() {
            @Override
            protected AppInfraInterface getAppInfra() {
                return appInfraInterfaceMock;
            }
        };
        marketingOptin.init(context);
    }

    public void testSettingAbTestValues() {
        when(abTestClientInterfaceMock.getTestValue(AB_TEST_OPTIN_IMAGE_KEY, "default_value", ABTestClientInterface.UPDATETYPE.APP_UPDATE)).thenReturn("Sonicare");
        RegistrationContentConfiguration registrationContentConfiguration = marketingOptin.getRegistrationContentConfiguration();
        assertEquals(registrationContentConfiguration.getEnableMarketImage(),R.drawable.abtesting_sonicare);
        assertEquals(registrationContentConfiguration.getOptInTitleText(),"Here\\'s what You Have To Look Forward To:");
        assertEquals(registrationContentConfiguration.getOptInQuessionaryText(),"Custom Reward Coupons, Holiday Surprises, VIP Shopping Days");
    }
}