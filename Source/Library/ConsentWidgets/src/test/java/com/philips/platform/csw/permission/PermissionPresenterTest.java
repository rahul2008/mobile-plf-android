package com.philips.platform.csw.permission;

import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentStatus;
import com.philips.platform.catk.network.NetworkHelper;
import com.philips.platform.csw.mock.CatkComponentMock;
import com.philips.platform.csw.mock.ServiceDiscoveryInterfaceMock;
import com.philips.platform.csw.utils.CswLogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PermissionPresenterTest {
    PermissionPresenter mPermissionPresenter;
    @Mock
    PermissionInterface mockPermissionInterface;
    @Mock
    Context mockContext;

    private CatkComponentMock catkComponent;

    private ConsentAccessToolKit consentAccessToolKit;
    @Mock
    private NetworkHelper mockNetworkHelper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mPermissionPresenter
                = new PermissionPresenter(mockPermissionInterface, mockContext);
        consentAccessToolKit = ConsentAccessToolKit.getInstance();

        catkComponent = new CatkComponentMock();
        User mockUser = mock(User.class);
        catkComponent.getUser_return = mockUser;
        when(mockUser.getCountryCode()).thenReturn("US");
        consentAccessToolKit.setCatkComponent(catkComponent);
        ServiceDiscoveryInterface.OnGetServiceLocaleListener onGetServiceLocaleListener = mock(ServiceDiscoveryInterface.OnGetServiceLocaleListener.class);

        catkComponent.getServiceDiscoveryInterface_return = mock(ServiceDiscoveryInterfaceMock.class);
        catkComponent.getServiceDiscoveryInterface_return.getServiceLocaleWithCountryPreference("en_US", onGetServiceLocaleListener);

        CswLogger.setLoogerInterface(catkComponent.getLoggingInterface());

    }

    @Test(expected = NoClassDefFoundError.class)
    public void testShowProgressDialog() throws Exception {
        mPermissionPresenter.getConsentStatus();
        Mockito.verify(mockPermissionInterface).showProgressDialog();
    }

    @Test(expected = UnsatisfiedLinkError.class)
    public void testShowProgressDialogOnCreate() throws Exception {
        mPermissionPresenter.createConsentStatus(true);
        Mockito.verify(mockPermissionInterface).showProgressDialog();
    }


    @Test
    public void testOnResponseSuccessConsent() throws Exception {
        mPermissionPresenter.onResponseFailureConsent(0);
        Mockito.verify(mockPermissionInterface).hideProgressDialog();
    }

    @Test
    public void testOnSuccess() throws Exception {
        mPermissionPresenter.onSuccess(0);
        Mockito.verify(mockPermissionInterface).hideProgressDialog();
    }

    @Test
    public void testOnFailuer() throws Exception {
        mPermissionPresenter.onFailure(0);
        Mockito.verify(mockPermissionInterface).hideProgressDialog();
    }

    @Test
    public void testonResponseSuccessConsent() throws Exception {
          Consent consent = new Consent(new Locale("nl","NL"), ConsentStatus.active, "moment", 1);

        List<Consent> responseData = new ArrayList<Consent>();
        responseData.add(consent);

        mPermissionPresenter.onResponseSuccessConsent(responseData);
        Mockito.verify(mockPermissionInterface).hideProgressDialog();
        Mockito.verify(mockPermissionInterface).updateSwitchStatus(true);
    }

    @Test
    public void testonResponseSuccessConsentWithNull() throws Exception {
        mPermissionPresenter.onResponseSuccessConsent(null);
        Mockito.verify(mockPermissionInterface).hideProgressDialog();
        Mockito.verify(mockPermissionInterface).updateSwitchStatus(false);
    }
}

