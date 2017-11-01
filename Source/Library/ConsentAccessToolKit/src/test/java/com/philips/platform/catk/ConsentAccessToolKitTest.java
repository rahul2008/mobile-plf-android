package com.philips.platform.catk;

import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.injection.UserLocale;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.listener.RequestListener;
import com.philips.platform.catk.network.NetworkAbstractModel;
import com.philips.platform.catk.network.NetworkHelper;
import com.philips.platform.catk.util.CustomRobolectricRunnerCATK;
import com.philips.platform.mya.consentaccesstoolkit.BuildConfig;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Maqsood on 10/27/17.
 */

@RunWith(CustomRobolectricRunnerCATK.class)
@PrepareForTest({NetworkHelper.class,CatkInterface.class})
@Config(constants = BuildConfig.class, sdk = 25)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class ConsentAccessToolKitTest {

    private String appName = "OneBackend";
    private String propName = "OneBackendProp";

    @Mock
    RestInterface mockRestInterface;

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    @Mock
    private ConsentResponseListener listnerMock;

    @Mock
    private NetworkHelper mockNetworkHelper;

    private ConsentAccessToolKit consentAccessToolKit;

    @Mock
    private CatkComponent mockCatkComponent;

    @Mock
    private CatkInterface mockCatkInterface;

    @Mock
    CreateConsentListener mockCreateConsentListener;

    @Mock
    User user;

    @Mock
    UserLocale mockUserLocale;

    @Mock
    ConsentAccessToolKit mockConsentAccessToolKit;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(NetworkHelper.class);
        PowerMockito.mockStatic(CatkInterface.class);
        when(NetworkHelper.getInstance()).thenReturn(mockNetworkHelper);
        mockCatkInterface.setCatkComponent(mockCatkComponent);
        when(mockCatkInterface.getCatkComponent()).thenReturn(mockCatkComponent);
        when(mockCatkInterface.getCatkComponent().getUser()).thenReturn(user);
        CatkInterface.getCatkComponent().inject(mockConsentAccessToolKit);
        when(mockCatkInterface.getCatkComponent().getUserLocale()).thenReturn(mockUserLocale);
        consentAccessToolKit = new ConsentAccessToolKit(appName, propName);
    }

    @After
    public void tearDown() throws Exception {
        mockNetworkHelper = null;
        listnerMock = null;
    }

    @Test
    public void shouldCallNetworkHelperSendRequestMethodWhenGetConsentDetailsMethodISCalled() throws Exception {
        consentAccessToolKit.getConsentDetails(listnerMock);
        verify(mockNetworkHelper).sendRequest(anyInt(), any(NetworkAbstractModel.class), any(RequestListener.class));
    }

   /* @Test
    public void shouldCallNetworkHelperSendRequestMethodWhenCreateConsentDetailsMethodISCalled() throws Exception {
        consentAccessToolKit.createConsent(ConsentStatus.active,mockCreateConsentListener);
        verify(mockNetworkHelper).sendRequest(anyInt(), any(NetworkAbstractModel.class), any(RequestListener.class));
    }*/

    @Test
    public void shouldCallNetworkHelperSendRequestMethodWhengetStatusForConsentType() throws Exception {
        consentAccessToolKit.getStatusForConsentType("active",1,listnerMock);
        Assert.assertNotNull(consentAccessToolKit.buildPolicyRule("consentType",1, "IN",propName, appName));
    }
}
