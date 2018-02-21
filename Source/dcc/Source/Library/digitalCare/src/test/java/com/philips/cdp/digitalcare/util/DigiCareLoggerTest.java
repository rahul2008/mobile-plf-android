package com.philips.cdp.digitalcare.util;

import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;

import com.philips.cdp.digitalcare.CcDependencies;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.locatephilips.fragments.ServiceLocatorFragment;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;

import com.philips.platform.appinfra.logging.AppInfraLogging;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.Robolectric;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by philips on 27/11/17.
 */


@RunWith(CustomRobolectricRunnerCC.class)
@PrepareForTest(DigitalCareConfigManager.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*", "org.apache.xerces", "javax.xml.*", "org.xml.sax.*", "org.w3c.dom.*",  "org.springframework.context.*", "org.apache.log4j.*"})

public class DigiCareLoggerTest {


    CcDependencies ccDependenciesMock;

    AppInfraInterface appInfraInterface;

    public LoggingInterface loggingInterface;

    public DigitalCareConfigManager digitalCareConfigManager;
    private Context mContext;

    @Before
    public void setUp() throws Exception {

        loggingInterface = Mockito.mock(LoggingInterface.class);
        appInfraInterface = Mockito.mock(AppInfraInterface.class);

        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(loggingInterface.createInstanceForComponent(anyString(), anyString())).thenReturn(loggingInterface);

        mContext = mock(Context.class);
        digitalCareConfigManager = DigitalCareConfigManager.getInstance();
    }

    @After
    public void tearDown(){
        digitalCareConfigManager.setContext(null);
        digitalCareConfigManager = null;
    }

    @Test
    public void testLoggerIssupported() {
        digitalCareConfigManager.initializeDigitalCareLibrary(mContext, appInfraInterface);
        boolean isTrue = DigiCareLogger.isLoggingSupported();
        Assert.assertTrue(isTrue);
      //  verify(loggingInterface).createInstanceForComponent(anyString(), anyString());

    }

    @Test
    public void testDebug() {
        digitalCareConfigManager.initializeDigitalCareLibrary(mContext, appInfraInterface);
        DigiCareLogger.d("tag","message");
        verify(loggingInterface).log(LoggingInterface.LogLevel.DEBUG, "tag","message");
    }

    @Test
    public void testShouldNotCallDebug() {
        digitalCareConfigManager.initializeDigitalCareLibrary(null, null);
        DigiCareLogger.d("tag","message");
        verify(loggingInterface, never()).log(LoggingInterface.LogLevel.DEBUG, "tag","message");
    }

    @Test
    public void testInfo() {
        digitalCareConfigManager.initializeDigitalCareLibrary(mContext, appInfraInterface);
        DigiCareLogger.i("tagInfo","infoMessage");
        verify(loggingInterface).log(LoggingInterface.LogLevel.INFO, "tagInfo","infoMessage");
    }

    @Test
    public void testShouldNotCallInfo() {
        digitalCareConfigManager.initializeDigitalCareLibrary(null, null);
        DigiCareLogger.i("tag","message");
        verify(loggingInterface, never()).log(LoggingInterface.LogLevel.INFO, "tag","message");
    }

    @Test
    public void testVerbose() {
        digitalCareConfigManager.initializeDigitalCareLibrary(mContext, appInfraInterface);
        DigiCareLogger.v("tag","message");
        verify(loggingInterface).log(LoggingInterface.LogLevel.VERBOSE, "tag","message");
    }

    @Test
    public void testShouldNotCallVerbose() {
        digitalCareConfigManager.initializeDigitalCareLibrary(null, null);
        DigiCareLogger.v("tag","message");
        verify(loggingInterface, never()).log(LoggingInterface.LogLevel.VERBOSE, "tag","message");
    }

    @Test
    public void testWarn() {
        digitalCareConfigManager.initializeDigitalCareLibrary(mContext, appInfraInterface);
        DigiCareLogger.w("tag","message");
        verify(loggingInterface).log(LoggingInterface.LogLevel.WARNING, "tag","message");
    }

    @Test
    public void testShouldNotCallWarn() {
        digitalCareConfigManager.initializeDigitalCareLibrary(null, null);
        DigiCareLogger.w("tag","message");
        verify(loggingInterface, never()).log(LoggingInterface.LogLevel.WARNING, "tag","message");
    }

    @Test
    public void testError() {
        digitalCareConfigManager.initializeDigitalCareLibrary(mContext, appInfraInterface);
        DigiCareLogger.e("tag","message");
        verify(loggingInterface).log(LoggingInterface.LogLevel.ERROR, "tag","message");
    }

    @Test
    public void testShouldNotCallError() {
        digitalCareConfigManager.initializeDigitalCareLibrary(null, null);
        DigiCareLogger.e("tag","message");
        verify(loggingInterface, never()).log(LoggingInterface.LogLevel.ERROR, "tag","message");
    }

    }
