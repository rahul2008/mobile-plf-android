package com.philips.platform.appinfra.logging;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

import junit.framework.TestCase;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import static com.philips.platform.appinfra.logging.LoggingConfiguration.APP_INFRA_CLOUD_LOGGING_PRODUCT_KEY;
import static com.philips.platform.appinfra.logging.LoggingConfiguration.APP_INFRA_CLOUD_LOGGING_SECRET_KEY;
import static com.philips.platform.appinfra.logging.LoggingConfiguration.APP_INFRA_CLOUD_LOGGING_SHARED_KEY;
import static com.philips.platform.appinfra.logging.LoggingConfiguration.HSDP_GROUP;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoggingConfigurationTest extends TestCase {

    private LoggingConfiguration loggingConfiguration;
    private AppInfra mAppInfra;
    @Mock
    private AppConfigurationInterface.AppConfigurationError appConfigurationError;

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        Context context = mock(Context.class);
        mAppInfra = mock(AppInfra.class);
        when(mAppInfra.getAppInfraContext()).thenReturn(context);
        final HashMap hashMap = new HashMap();
        hashMap.put("logging.debugConfig", true);
        hashMap.put("componentLevelLogEnabled", true);
        hashMap.put("consoleLogEnabled", true);
        hashMap.put("cloudLogEnabled", true);
        hashMap.put("fileLogEnabled", true);
        hashMap.put("cloudBatchLimit", 5);
        hashMap.put("logLevel", "All");

        loggingConfiguration = new LoggingConfiguration(mAppInfra, "", "") {
            @Override
            HashMap<?, ?> getLoggingProperties() {
                return hashMap;
            }

            @NonNull
            @Override
            AppConfigurationInterface.AppConfigurationError getAppConfigurationError() {
                return appConfigurationError;
            }
        };
    }

    public void testIsComponentLevelLogEnabled() {
        assertTrue(loggingConfiguration.isComponentLevelLogEnabled());
    }

    public void testIsFileLogEnabled() {
        assertTrue(loggingConfiguration.isFileLogEnabled());
    }

    public void testIsCloudLogEnabled() {
        assertTrue(loggingConfiguration.isCloudLogEnabled());
    }

    public void testIsConsoleLogEnabled() {
        assertTrue(loggingConfiguration.isConsoleLogEnabled());
    }

    public void testGetLogLevel() {
        assertEquals(loggingConfiguration.getLogLevel(), "All");
    }

    public void testAssertingNonNull() {
        assertNotNull(loggingConfiguration.getAppInfra());
        assertNotNull(loggingConfiguration.getComponentVersion());
        assertNotNull(loggingConfiguration.getAppConfigurationError());
        assertNotNull(loggingConfiguration.getLoggingProperties());
    }

    public void testIsLoggingEnabled() {
        boolean loggingEnabled = loggingConfiguration.isLoggingEnabled();
        assertTrue(loggingEnabled);
        final HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("logLevel", "Off");
        loggingConfiguration = new LoggingConfiguration(mAppInfra, "", "") {
            @Override
            HashMap<?, ?> getLoggingProperties() {
                return hashMap;
            }
        };
        loggingEnabled = loggingConfiguration.isLoggingEnabled();
        assertFalse(loggingEnabled);
    }


    public void testValidateIfComponentLogCriteriaMet() {
        assertFalse(loggingConfiguration.checkIfComponentLogCriteriaMet("componentId"));
    }

    public void testGetBatchLimit() {
        assertEquals(loggingConfiguration.getBatchLimit(),5);
        assertFalse(loggingConfiguration.getBatchLimit() == 6);
    }

    public void testNotNullAssertions() {
        AppConfigurationInterface appConfigurationInterface = mock(AppConfigurationInterface.class);
        when(mAppInfra.getConfigInterface()).thenReturn(appConfigurationInterface);
        when(appConfigurationInterface.getPropertyForKey(APP_INFRA_CLOUD_LOGGING_PRODUCT_KEY, HSDP_GROUP, appConfigurationError)).thenReturn("productKey");
        when(appConfigurationInterface.getPropertyForKey(APP_INFRA_CLOUD_LOGGING_SECRET_KEY, HSDP_GROUP, appConfigurationError)).thenReturn("secretKey");
        when(appConfigurationInterface.getPropertyForKey(APP_INFRA_CLOUD_LOGGING_SHARED_KEY, HSDP_GROUP, appConfigurationError)).thenReturn("sharedKey");
        assertNotNull(loggingConfiguration.getCLProductKey());
        assertEquals(loggingConfiguration.getCLProductKey(),"productKey");
        assertNotNull(loggingConfiguration.getCLSecretKey());
        assertEquals(loggingConfiguration.getCLSecretKey(),"secretKey");
        assertNotNull(loggingConfiguration.getCLSharedKey());
        assertEquals(loggingConfiguration.getCLSharedKey(),"sharedKey");
    }
}