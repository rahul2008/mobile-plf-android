/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.logging;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.philips.platform.appinfra.ConfigValues.getMockResponse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoggingTest extends AppInfraInstrumentation {
    private LoggingInterface loggingInterface ;
    private LoggingInterface loggingInterfaceMock;
    private Context context;
    private AppInfra mAppInfra;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);

        mAppInfra = mock(AppInfra.class);
        when(mAppInfra.getAppInfraContext()).thenReturn(context);
        AppConfigurationInterface appConfigurationInterface = mock(AppConfigurationInterface.class);
        when(appConfigurationInterface.getPropertyForKey(anyString(),anyString(),any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(getMockResponse());
        when(mAppInfra.getConfigInterface()).thenReturn(appConfigurationInterface);
        loggingInterface = new AppInfraLogging(mAppInfra);
        loggingInterface.createInstanceForComponent("ail","1.5");
        when(mAppInfra.getLogging()).thenReturn(loggingInterface);
        loggingInterface.log(LoggingInterface.LogLevel.INFO,"Event","Message");
        loggingInterfaceMock = mock(AppInfraLogging.class);
    }

    public void testCreateInstanceForComponent() {
        context = getInstrumentation().getContext();
        AppInfraLogging appInfraLogging = new AppInfraLogging(mAppInfra);
        assertNotNull(appInfraLogging.createInstanceForComponent("test","1"));
    }

    public void testCreateLogger() {
        final Logger logger = mock(Logger.class);
        AppInfraLogging appInfraLogging;
        appInfraLogging = new AppInfraLogging(mAppInfra){
            @Override
            protected Logger getJavaLogger() {
                return logger;
            }
        };
        appInfraLogging.createLogger("component_id","component_version");
        verify(logger).log(Level.INFO, AppInfraLogEventID.AI_LOGGING + "Logger created");
    }

    public void testLog() {
        final Logger logger = mock(Logger.class);
        AppInfraLogging appInfraLogging = new AppInfraLogging(mAppInfra) {
            @Override
            protected Logger getJavaLogger() {
                return logger;
            }
        };
        appInfraLogging.createLogger("component_id","");
        appInfraLogging.log(LoggingInterface.LogLevel.DEBUG, "some_event", "event_message");
        appInfraLogging.log(LoggingInterface.LogLevel.ERROR, "some_event", "event_message");
        appInfraLogging.log(LoggingInterface.LogLevel.INFO, "some_event", "event_message");
        appInfraLogging.log(LoggingInterface.LogLevel.VERBOSE, "some_event", "event_message");
        appInfraLogging.log(LoggingInterface.LogLevel.WARNING, "some_event", "event_message");
        verify(logger).log(Level.CONFIG, "some_event", "event_message");
        verify(logger).log(Level.SEVERE, "some_event", "event_message");
        verify(logger).log(Level.INFO, "some_event", "event_message");
        verify(logger).log(Level.FINE, "some_event", "event_message");
        verify(logger).log(Level.WARNING, "some_event", "event_message");
    }

    public void testLogWithMap() {
        final Logger logger = mock(Logger.class);
        final Object[] values = new Object[2];
        AppInfraLogging appInfraLogging = new AppInfraLogging(mAppInfra) {
            @Override
            protected Logger getJavaLogger() {
                return logger;
            }

            @NonNull
            @Override
            Object[] getParamObjects() {
                return values;
            }
        };
        appInfraLogging.createLogger("component_id","");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("key1", "val1");
        map.put("key2", "val2");

        appInfraLogging.log(LoggingInterface.LogLevel.DEBUG, "some_event", "event_message", map);
        assertEquals(values[0], "event_message");
        assertEquals(values[1], map);
        appInfraLogging.log(LoggingInterface.LogLevel.ERROR, "some_event", "event_message", map);
        appInfraLogging.log(LoggingInterface.LogLevel.INFO, "some_event", "event_message", map);
        appInfraLogging.log(LoggingInterface.LogLevel.VERBOSE, "some_event", "event_message", map);
        appInfraLogging.log(LoggingInterface.LogLevel.WARNING, "some_event", "event_message", map);

        verify(logger).log(Level.CONFIG, "some_event", values);
        verify(logger).log(Level.SEVERE, "some_event", values);
        verify(logger).log(Level.INFO, "some_event", values);
        verify(logger).log(Level.FINE, "some_event", values);
        verify(logger).log(Level.WARNING, "some_event", values);
    }

    public void testLogInitialize(){
        assertNotNull(loggingInterface.createInstanceForComponent("Component Name","Component version"));
        loggingInterface.log(LoggingInterface.LogLevel.INFO,"Event","Message");

        assertNotNull(loggingInterfaceMock);
        loggingInterfaceMock.log(LoggingInterface.LogLevel.INFO,"Event","Message");
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                return null;
            }
        }).when(loggingInterfaceMock).createInstanceForComponent("Component Name mock","Component version");

        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                LoggingInterface.LogLevel logLevel= (LoggingInterface.LogLevel)args[0];
                assertNotNull(logLevel);
                return null;
            }
        }).when( loggingInterfaceMock).log(LoggingInterface.LogLevel.INFO,"Event","Message");
    }


    public void testLogWithConsole(){

        for (LoggingInterface.LogLevel logLevel : LoggingInterface.LogLevel.values()) {
            loggingInterface.log(logLevel, null,"message");
            loggingInterface.log(logLevel, "Event","Message");

        }

        for (LoggingInterface.LogLevel logLevel : LoggingInterface.LogLevel.values()) {
            loggingInterfaceMock.log(logLevel, null,"message");
            loggingInterfaceMock.log(logLevel, "Event","Message");

        }

        for (LoggingInterface.LogLevel loglevel : LoggingInterface.LogLevel.values()) {
            doAnswer(new Answer<Object>() {
                public Object answer(InvocationOnMock invocation) {
                    Object[] args = invocation.getArguments();
                    LoggingInterface.LogLevel logLevel= (LoggingInterface.LogLevel)args[0];
                    assertNotNull(logLevel);
                    return null;
                }
            }).when(loggingInterfaceMock).log(loglevel, null,"message");
            doAnswer(new Answer<Object>() {
                public Object answer(InvocationOnMock invocation) {
                    Object[] args = invocation.getArguments();
                    LoggingInterface.LogLevel logLevel= (LoggingInterface.LogLevel)args[0];
                    assertNotNull(logLevel);
                    return null;
                }
            }).when(loggingInterfaceMock).log(loglevel, "Event","Message");

        }

        for (LoggingInterface.LogLevel logLevel : LoggingInterface.LogLevel.values()) {
            loggingInterface.log(logLevel, null,"message");
            loggingInterface.log(logLevel, "Event","Message");

        }

        for (LoggingInterface.LogLevel logLevel : LoggingInterface.LogLevel.values()) {
            loggingInterfaceMock.log(logLevel, null,"message");
            loggingInterfaceMock.log(logLevel, "Event","Message");

        }

        for (LoggingInterface.LogLevel logLevel : LoggingInterface.LogLevel.values()) {
            doAnswer(new Answer<Object>() {
                public Object answer(InvocationOnMock invocation) {
                    Object[] args = invocation.getArguments();
                    LoggingInterface.LogLevel logLevel= (LoggingInterface.LogLevel)args[0];
                    assertNotNull(logLevel);
                    return null;
                }
            }).when(loggingInterfaceMock).log(logLevel, null,"message");
            doAnswer(new Answer<Object>() {
                public Object answer(InvocationOnMock invocation) {
                    Object[] args = invocation.getArguments();
                    LoggingInterface.LogLevel logLevel= (LoggingInterface.LogLevel)args[0];
                    assertNotNull(logLevel);
                    return null;
                }
            }).when(loggingInterfaceMock).log(logLevel, "Event","Message");

        }
    }

    public void testLogWithFile() {

        for (LoggingInterface.LogLevel logLevel : LoggingInterface.LogLevel.values()) {
            loggingInterface.log(logLevel, null,"message");
            loggingInterface.log(logLevel, "Event","Message");

        }

        for (LoggingInterface.LogLevel logLevel : LoggingInterface.LogLevel.values()) {
            loggingInterfaceMock.log(logLevel, null, "message");
            loggingInterfaceMock.log(logLevel, "Event", "Message");

        }
        loggingInterface.createInstanceForComponent("Component Name", "Component version");
        for (LoggingInterface.LogLevel logLevel : LoggingInterface.LogLevel.values()) {
            loggingInterfaceMock.log(logLevel, null, "message");
            loggingInterfaceMock.log(logLevel, "Event", "Message");

        }

        for (LoggingInterface.LogLevel loglevel : LoggingInterface.LogLevel.values()) {
            doAnswer(new Answer<Object>() {
                public Object answer(InvocationOnMock invocation) {
                    Object[] args = invocation.getArguments();
                    LoggingInterface.LogLevel logLevel = (LoggingInterface.LogLevel) args[0];
                    assertNotNull(logLevel);
                    return null;
                }
            }).when(loggingInterfaceMock).log(loglevel, null, "message");
            doAnswer(new Answer<Object>() {
                public Object answer(InvocationOnMock invocation) {
                    Object[] args = invocation.getArguments();
                    LoggingInterface.LogLevel logLevel = (LoggingInterface.LogLevel) args[0];
                    assertNotNull(logLevel);
                    return null;
                }
            }).when(loggingInterfaceMock).log(loglevel, "Event", "Message");

            for (LoggingInterface.LogLevel logLevel : LoggingInterface.LogLevel.values()) {
                loggingInterfaceMock.log(logLevel, null, "message");
                loggingInterfaceMock.log(logLevel, "Event", "Message");

            }
            loggingInterface.createInstanceForComponent("Component Name", "Component version");
            for (LoggingInterface.LogLevel logLevel : LoggingInterface.LogLevel.values()) {
                loggingInterfaceMock.log(logLevel, null, "message");
                loggingInterfaceMock.log(logLevel, "Event", "Message");

            }

            for (LoggingInterface.LogLevel loglevel1 : LoggingInterface.LogLevel.values()) {
                doAnswer(new Answer<Object>() {
                    public Object answer(InvocationOnMock invocation) {
                        return null;
                    }
                }).when(loggingInterfaceMock).log(loglevel1, null, "message");
                doAnswer(new Answer<Object>() {
                    public Object answer(InvocationOnMock invocation) {
                        return null;
                    }
                }).when(loggingInterfaceMock).log(loglevel1, "Event", "Message");

            }
        }

    }

    public void testEnableConsoleLog() {
        try {
            Method method = loggingInterface.getClass().getDeclaredMethod("enableConsoleLog", boolean.class);
            method.setAccessible(true);
            method.invoke(loggingInterface, true);
            method.invoke(loggingInterface, true);
            method.invoke(loggingInterface, false);
            method.invoke(loggingInterface, false);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(getClass() + "", " Illegal argument exception ");
        }

    }

    public void testEnableFileLog() {
        try {
            Method method = loggingInterface.getClass().getDeclaredMethod("enableFileLog", boolean.class);
            method.setAccessible(true);
            method.invoke(loggingInterface, true);
            method.invoke(loggingInterface, true);
            method.invoke(loggingInterface, false);
            method.invoke(loggingInterface, false);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(getClass() + "", " Invocation target / Illegal access exception ");
        }
    }

    public void testLogForDictionary(){
        try {
            Method method = loggingInterface.getClass().getDeclaredMethod("log", LoggingInterface.LogLevel.class,String.class,String.class,Map.class);
            method.setAccessible(true);
            String event= "Log Event";
            String msg= "Log message";
            Map<String,String> map = new HashMap<>();
            map.put("key1","val1");
            map.put("key2","val2");
            method.invoke(loggingInterface, LoggingInterface.LogLevel.DEBUG,event,msg,map);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(getClass() + "", " NoSuchMethod / Illegal access exception ");
        }
    }

    public void testLogForDictionary_emptymap(){
        try {
            Method method = loggingInterface.getClass().getDeclaredMethod("log", LoggingInterface.LogLevel.class,String.class,String.class,Map.class);
            method.setAccessible(true);
            String event= "Log Event";
            String msg= "Log message";
            method.invoke(loggingInterface, LoggingInterface.LogLevel.DEBUG,event,msg,null);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(getClass() + "", " Invocation target / Illegal access exception ");
        }
    }

    public void testLogForDictionary_emptymsg(){
        try {
            Method method = loggingInterface.getClass().getDeclaredMethod("log", LoggingInterface.LogLevel.class,String.class,String.class,Map.class);
            method.setAccessible(true);
            String event= "Log Event";
            Map<String,String> map = new HashMap<>();
            map.put("key1","val1");
            map.put("key2","val2");
            method.invoke(loggingInterface, LoggingInterface.LogLevel.DEBUG,event,null,map);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(getClass() + "", " NoSuchMethod / Illegal access exception ");
        }
    }

    public void testLogForDictionary_emptymsgAndMap(){
        try {
            Method method = loggingInterface.getClass().getDeclaredMethod("log", LoggingInterface.LogLevel.class,String.class,String.class,Map.class);
            method.setAccessible(true);
            String event= "Log Event";
            method.invoke(loggingInterface, LoggingInterface.LogLevel.DEBUG,event,null,null);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(getClass() + "", " NoSuchMethod / Illegal access exception ");
        }
    }
    public void testGetFileHandler() {
        try {
            AppInfraLogging appInfraLogging = new AppInfraLogging(mAppInfra);
            Method method = appInfraLogging.getClass().getDeclaredMethod("getFileHandler");
            method.setAccessible(true);
            method.invoke(appInfraLogging);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(getClass() + "", " NoSuchMethod / Illegal access exception ");
        }

    }

   public void testGetCurrentLogFileHandler() {
        try {
            AppInfraLogging appInfraLogging = new AppInfraLogging(mAppInfra);
            Method method = appInfraLogging.getClass().getDeclaredMethod("getCurrentLogFileHandler", Logger.class);
            method.setAccessible(true);
            Logger logger = Logger.getLogger("MyLogger");
            Method method2 = appInfraLogging.getClass().getDeclaredMethod("getFileHandler");
            method2.setAccessible(true);
            FileHandler fileHandler = (FileHandler) method2.invoke(appInfraLogging);
            logger.addHandler(fileHandler);
            method.invoke(appInfraLogging, logger);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(getClass() + "", " NoSuchMethod / Illegal access exception ");
        }
    }

    public void testGetCurrentConsoleFileHandler() {
        try {
            AppInfraLogging appInfraLogging = new AppInfraLogging(mAppInfra);
            Method method = appInfraLogging.getClass().getDeclaredMethod("getCurrentLogConsoleHandler", Logger.class);
            method.setAccessible(true);
            Logger logger = Logger.getLogger("MyLogger");
            ConsoleHandler consoleHandler = new ConsoleHandler();
            logger.addHandler(consoleHandler);
            method.invoke(appInfraLogging, logger);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(getClass() + "", " NoSuchMethod / Illegal access exception ");
        }
    }

}
