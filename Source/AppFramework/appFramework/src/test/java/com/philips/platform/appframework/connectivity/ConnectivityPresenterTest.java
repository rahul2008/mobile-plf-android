/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or disseminationpackage com.philips.platform.appframework.connectivity;
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.import android.content.Context;
*/
package com.philips.platform.appframework.connectivity;
import android.content.Context;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.registration.User;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;
import com.philips.platform.appframework.connectivity.appliance.DeviceMeasurementPort;
import com.philips.platform.appframework.connectivity.appliance.DeviceMeasurementPortProperties;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.core.utils.DataServicesConstants;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.MalformedURLException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for ConnectivityPresenter
 */

@RunWith(MockitoJUnitRunner.class)
public class ConnectivityPresenterTest {

    @Mock
    ConnectivityContract.View view;

    @Mock
    Context context;

    ConnectivityPresenter connectivityPresenter;

    @Mock
    User user;

    @Mock
    RefAppBleReferenceAppliance appliance;

    @Mock
    CommunicationStrategy communicationStrategy;

    @Captor
    ArgumentCaptor<DICommPortListener<DeviceMeasurementPort>> diCommPortListenerArgumentCaptor;

    @Captor
    ArgumentCaptor<ServiceDiscoveryInterface.OnGetServiceUrlListener> serviceUrlListenerArgumentCaptor;

    ServiceDiscoveryInterface.OnGetServiceUrlListener serviceUrlListener;

    DICommPortListener<DeviceMeasurementPort> diCommPortListener;

    @Mock
    DeviceMeasurementPort deviceMeasurementPort;

    @Mock
    DeviceMeasurementPortProperties deviceMeasurementPortProperties;

    @Mock
    AppInfraInterface appInfraInterface;

    private ConnectivityPresenterMock connectivityPresenterMock=null;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryInterface;



    @Before
    public void setUp(){
        connectivityPresenter=new ConnectivityPresenter(view,user,context);
        when(appliance.getDeviceMeasurementPort()).thenReturn(deviceMeasurementPort);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryInterface);
    }
    @Test(expected = IllegalArgumentException.class)
    public void setUpApplicance() throws Exception {
        connectivityPresenter.setUpApplicance(null);
    }

    @Test
    public void portUpdate_SuccessTest(){
        when(deviceMeasurementPort.getPortProperties()).thenReturn(deviceMeasurementPortProperties);
        connectivityPresenter.setUpApplicance(appliance);
        verify(deviceMeasurementPort).addPortListener(diCommPortListenerArgumentCaptor.capture());
        diCommPortListener=diCommPortListenerArgumentCaptor.getValue();
        diCommPortListener.onPortUpdate(deviceMeasurementPort);
        verify(view).updateDeviceMeasurementValue(any(String.class));
    }

    @Test
    public void portUpdate_ErrorTest(){
        when(deviceMeasurementPort.getPortProperties()).thenReturn(null);
        connectivityPresenter.setUpApplicance(appliance);
        verify(deviceMeasurementPort).addPortListener(diCommPortListenerArgumentCaptor.capture());
        diCommPortListener=diCommPortListenerArgumentCaptor.getValue();
        diCommPortListener.onPortUpdate(deviceMeasurementPort);
        verify(view).onDeviceMeasurementError(any(Error.class),any(String.class));
    }
    @Test
    public void onPortErrorTest(){
        when(deviceMeasurementPort.getPortProperties()).thenReturn(null);
        connectivityPresenter.setUpApplicance(appliance);
        verify(deviceMeasurementPort).addPortListener(diCommPortListenerArgumentCaptor.capture());
        diCommPortListener=diCommPortListenerArgumentCaptor.getValue();
        diCommPortListener.onPortError(deviceMeasurementPort,Error.NO_REQUEST_DATA,"");
        verify(view).onDeviceMeasurementError(any(Error.class),any(String.class));
    }

    @Test
    public void processMoment_Success_Test(){
        connectivityPresenterMock=new ConnectivityPresenterMock(view,user,context);
        connectivityPresenterMock.setCanSync(true);
        connectivityPresenterMock.processMoment("50");
        verify(view).onProcessMomentProgress(any(String.class));
    }

    @Test
    public void processMoment_Error_Test(){
        connectivityPresenterMock=new ConnectivityPresenterMock(view,user,context);
        connectivityPresenterMock.setCanSync(false);
        connectivityPresenterMock.processMoment("50");
        verify(view).onProcessMomentError(any(String.class));
    }

    @Test
    public void loadDataCoreUrl_Error_Test() throws MalformedURLException {
        connectivityPresenterMock=new ConnectivityPresenterMock(view,user,context);
        connectivityPresenterMock.loadDataCoreURLFromServiceDiscovery(context);
        verify(serviceDiscoveryInterface).getServiceUrlWithCountryPreference(eq(DataServicesConstants.BASE_URL_KEY),serviceUrlListenerArgumentCaptor.capture());
        serviceUrlListener=serviceUrlListenerArgumentCaptor.getValue();
        serviceUrlListener.onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR,"");
        verify(view).onProcessMomentError(any(String.class));

    }

    @Test
    public void getDummyUserMomentTest(){
        Assert.assertNotNull(connectivityPresenter.getDummyUserMoment("50"));
    }

    class ConnectivityPresenterMock extends ConnectivityPresenter{

        private boolean canSync;

        public ConnectivityPresenterMock(ConnectivityContract.View connectivityViewListener, User user, Context context) {
            super(connectivityViewListener, user, context);
        }

        public void setCanSync(boolean canSync){
            this.canSync=canSync;
        }

        @Override
        protected boolean canSync(User user) {
            return canSync;
        }

        public void postMoment(){
           //Avoided postMoment call
        }

        protected AppInfraInterface getAppInfra(){
            return appInfraInterface;
        }
    }

}