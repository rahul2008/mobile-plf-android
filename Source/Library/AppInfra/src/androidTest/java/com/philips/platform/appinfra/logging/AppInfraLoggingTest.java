package com.philips.platform.appinfra.logging;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AppInfraLoggingTest extends AppInfraInstrumentation {

    private Context context;
    private AppInfraLogging appInfraLogging;
    private AppInfra appInfra;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().build(context);
        appInfraLogging = new AppInfraLogging(appInfra);
    }

    public void testCreateInstanceForComponent() {
        context = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().build(context);
        appInfraLogging = new AppInfraLogging(appInfra);
        assertNotNull(appInfraLogging.createInstanceForComponent("test","1"));
    }

    public void testCreateLogger() {
        final Logger logger = mock(Logger.class);
        appInfraLogging = new AppInfraLogging(appInfra){
            @Override
            protected Logger getJavaLogger() {
                return logger;
            }
        };
        appInfraLogging.createLogger("component_id");
        verify(logger).log(Level.INFO, "Logger created");
    }

    /*public void testLog() {
        appInfraLogging = new AppInfraLogging(appInfra);
        appInfraLogging.log(LoggingInterface.LogLevel.DEBUG,"some_event","event_message");
    }*/
}






