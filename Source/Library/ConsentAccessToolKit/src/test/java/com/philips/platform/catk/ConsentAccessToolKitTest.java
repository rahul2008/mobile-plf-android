package com.philips.platform.catk;

import com.android.volley.Request;
import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.listener.RequestListener;
import com.philips.platform.catk.mock.CatkComponentMock;
import com.philips.platform.catk.network.NetworkAbstractModel;
import com.philips.platform.catk.network.NetworkHelper;
import com.philips.platform.catk.network.NetworkHelperManipulator;
import com.philips.platform.catk.response.ConsentStatus;
import com.philips.platform.catk.util.CustomRobolectricRunnerCATK;
import com.philips.platform.mya.consentaccesstoolkit.BuildConfig;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Maqsood on 10/27/17.
 */

@RunWith(CustomRobolectricRunnerCATK.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class ConsentAccessToolKitTest {

    private static final String COUNTRY_CODE = "IN";

    private String appName = "OneBackend";

    private String propName = "OneBackendProp";

    private CatkComponentMock catkComponent;

    private ConsentAccessToolKit consentAccessToolKit;

    @Mock
    RestInterface mockRestInterface;

    @Mock
    private ConsentResponseListener listnerMock;

    @Mock
    private NetworkHelper mockNetworkHelper;

    @Mock
    private CatkComponent mockCatkComponent;

    @Mock
    CreateConsentListener mockCreateConsentListener;

    @Mock
    User user;

    @Mock
    ConsentAccessToolKit mockConsentAccessToolKit;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        consentAccessToolKit = ConsentAccessToolKit.getInstance();
        catkComponent = new CatkComponentMock();
        catkComponent.getUser_return = user;
        when(user.getCountryCode()).thenReturn(COUNTRY_CODE);
        consentAccessToolKit.setCatkComponent(catkComponent);
        NetworkHelperManipulator.setInstance(mockNetworkHelper);
    }

    @After
    public void tearDown() throws Exception {
        mockNetworkHelper = null;
        listnerMock = null;
    }

    @Test
    public void shouldCallNetworkHelperSendRequestMethodWhenGetConsentDetailsMethodISCalled() throws Exception {
        consentAccessToolKit.getConsentDetails(listnerMock);
        verify(mockNetworkHelper).sendRequest(eq(Request.Method.GET), any(NetworkAbstractModel.class), any(RequestListener.class));
    }

    @Test
    public void shouldCallNetworkHelperSendRequestMethodWhenCreateConsentDetailsMethodISCalled() throws Exception {
        givenLocale("nl-NL");
        consentAccessToolKit.createConsent(ConsentStatus.active,mockCreateConsentListener);
        verify(mockNetworkHelper).sendRequest(eq(Request.Method.POST), any(NetworkAbstractModel.class), any(RequestListener.class));
    }

    @Test
    public void shouldCallNetworkHelperSendRequestMethodWhengetStatusForConsentType() throws Exception {
        consentAccessToolKit.getStatusForConsentType("active",1,listnerMock);
        Assert.assertNotNull(consentAccessToolKit.buildPolicyRule("consentType",1, COUNTRY_CODE, propName, appName));
    }

    private void givenLocale(String locale) {
        catkComponent.getServiceDiscoveryInterface_return.getServiceLocaleWithCountryPreference_return = locale;
    }

}
