/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.utility;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AmwellLogTest {


    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    AmwellLog amwellLog;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryInterfaceMock;


    @Before
    public void setUp() throws Exception {
        AmwellLog amwellLog = new AmwellLog();
        MockitoAnnotations.initMocks(this);
        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryInterfaceMock);
        when(serviceDiscoveryInterfaceMock.getHomeCountry()).thenReturn("sss");
        THSManager.getInstance().setAppInfra(appInfraInterface);
    }

    @Test
    public void enableLoggingTrue() throws Exception {
        AmwellLog.enableLogging(true);
        amwellLog.enableLogging(true);
        assert AmwellLog.isLoggingEnabled() == true;
    }

    @Test
    public void enableLoggingFalse() throws Exception {
        AmwellLog.enableLogging(false);
        assert AmwellLog.isLoggingEnabled() == false;
    }

    @Test
    public void d() throws Exception {
        AmwellLog.enableLogging(true);
        AmwellLog.d(AmwellLog.LOG,"");
        verify(loggingInterface).log(LoggingInterface.LogLevel.DEBUG,AmwellLog.LOG,"");
    }

    @Test
    public void e() throws Exception {
        AmwellLog.enableLogging(true);
        AmwellLog.e(AmwellLog.LOG,"");
        verify(loggingInterface).log(LoggingInterface.LogLevel.ERROR,AmwellLog.LOG,"");
    }

    @Test
    public void i() throws Exception {
        AmwellLog.enableLogging(true);
        AmwellLog.i(AmwellLog.LOG,"");
        verify(loggingInterface).log(LoggingInterface.LogLevel.INFO,AmwellLog.LOG,"");
    }

    @Test
    public void v() throws Exception {
        AmwellLog.enableLogging(true);
        AmwellLog.v(AmwellLog.LOG,"");
        verify(loggingInterface).log(LoggingInterface.LogLevel.VERBOSE,AmwellLog.LOG,"");
    }

    @Test
    public void isLoggingEnabled(){
        AmwellLog.enableLogging(false);
        Boolean isLogEnabled = AmwellLog.isLoggingEnabled();
        assert isLogEnabled == false;
    }
}