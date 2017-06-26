/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.logging;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.ConfigValues;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;

import org.json.JSONObject;
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

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LoggingTest extends AppInfraInstrumentation {
    LoggingInterface loggingInterface ;
    LoggingInterface loggingInterfaceMock;
    AppConfigurationInterface mConfigInterface = null;
    private Context context;
    private AppInfra mAppInfra;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);

        mAppInfra = new AppInfra.Builder().build(context);
        mConfigInterface = new AppConfigurationManager(mAppInfra) {
            @Override
            protected JSONObject getMasterConfigFromApp() {
                JSONObject result = null;
                try {
                    String testJson = ConfigValues.testJson();
                    result = new JSONObject(testJson);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

        };
        assertNotNull(mConfigInterface);
        mAppInfra= new AppInfra.Builder().setConfig(mConfigInterface).build(context);
        loggingInterface = mAppInfra.getLogging().createInstanceForComponent("ail","1.5");
        assertNotNull(mAppInfra);
        assertNotNull(loggingInterface);
        loggingInterface.log(LoggingInterface.LogLevel.INFO,"Event","Message");
        loggingInterfaceMock = mock(AppInfraLogging.class);
    }

    public void testCreateInstanceForComponent() {
        context = getInstrumentation().getContext();
        AppInfra appInfra = new AppInfra.Builder().build(context);
        AppInfraLogging appInfraLogging = new AppInfraLogging(appInfra);
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
        appInfraLogging.createLogger("component_id");
        verify(logger).setLevel(Level.FINE);
        verify(logger).log(Level.INFO, "Logger created");
    }

    public void testLog() {
        final Logger logger = mock(Logger.class);
        AppInfraLogging appInfraLogging = new AppInfraLogging(mAppInfra) {
            @Override
            protected Logger getJavaLogger() {
                return logger;
            }
        };
        appInfraLogging.createLogger("component_id");
        appInfraLogging.log(LoggingInterface.LogLevel.DEBUG, "some_event", "event_message");
        appInfraLogging.log(LoggingInterface.LogLevel.ERROR, "some_event", "event_message");
        appInfraLogging.log(LoggingInterface.LogLevel.INFO, "some_event", "event_message");
        appInfraLogging.log(LoggingInterface.LogLevel.VERBOSE, "some_event", "event_message");
        appInfraLogging.log(LoggingInterface.LogLevel.WARNING, "some_event", "event_message");
        verify(logger).log(Level.INFO, "Logger created");
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
        appInfraLogging.createLogger("component_id");
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
                Object[] args = invocation.getArguments();
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

    public void testLogwithFile() {

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
                        Object[] args = invocation.getArguments();
                        return null;
                    }
                }).when(loggingInterfaceMock).log(loglevel1, null, "message");
                doAnswer(new Answer<Object>() {
                    public Object answer(InvocationOnMock invocation) {
                        Object[] args = invocation.getArguments();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    public void testLogForDictionary(){
        try {
            Method method = loggingInterface.getClass().getDeclaredMethod("log", LoggingInterface.LogLevel.class,String.class,String.class,Map.class);
            method.setAccessible(true);
            String event= new String("Log Event");
            String msg= new String("Log message");
            Map<String,String> map = new HashMap<>();
            map.put("key1","val1");
            map.put("key2","val2");
            method.invoke(loggingInterface, LoggingInterface.LogLevel.DEBUG,event,msg,map);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testLogForDictionary_emptymap(){
        try {
            Method method = loggingInterface.getClass().getDeclaredMethod("log", LoggingInterface.LogLevel.class,String.class,String.class,Map.class);
            method.setAccessible(true);
            String event= new String("Log Event");
            String msg= new String("Log message");
            method.invoke(loggingInterface, LoggingInterface.LogLevel.DEBUG,event,msg,null);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testLogForDictionary_emptymsg(){
        try {
            Method method = loggingInterface.getClass().getDeclaredMethod("log", LoggingInterface.LogLevel.class,String.class,String.class,Map.class);
            method.setAccessible(true);
            String event= new String("Log Event");
            String msg= new String("Log message");
            Map<String,String> map = new HashMap<>();
            map.put("key1","val1");
            map.put("key2","val2");
            method.invoke(loggingInterface, LoggingInterface.LogLevel.DEBUG,event,null,map);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testLogForDictionary_emptymsgAndMap(){
        try {
            Method method = loggingInterface.getClass().getDeclaredMethod("log", LoggingInterface.LogLevel.class,String.class,String.class,Map.class);
            method.setAccessible(true);
            String event= new String("Log Event");
            String msg= new String("Log message");
            Map<String,String> map = new HashMap<>();
            map.put("key1","val1");
            map.put("key2","val2");
            method.invoke(loggingInterface, LoggingInterface.LogLevel.DEBUG,event,null,null);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public void testGetFileHandler() {
        try {
            AppInfraLogging appInfraLogging = new AppInfraLogging(mAppInfra);
            Method method = appInfraLogging.getClass().getDeclaredMethod("getFileHandler");
            method.setAccessible(true);
            method.invoke(appInfraLogging);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    public void testComponentLevelLogEnabled() {
        AppInfra appInfra = new AppInfra.Builder().build(context);
        AppConfigurationInterface configInterface = new AppConfigurationManager(appInfra) {
            @Override
            protected JSONObject getMasterConfigFromApp() {
                JSONObject result = null;
                try {
                    String testJson = ConfigValues.testJson();
                    result = new JSONObject(testJson);
                    JSONObject appInfraConfig = result.optJSONObject("APPINFRA");
                    JSONObject loggingConfig = appInfraConfig.optJSONObject("LOGGING.DEBUGCONFIG");
                    loggingConfig.put("componentLevelLogEnabled", true);
                    appInfraConfig.put("LOGGING.DEBUGCONFIG", loggingConfig);
                    result.put("APPINFRA", appInfraConfig);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }
        };
        appInfra = new AppInfra.Builder().setConfig(configInterface).build(context);
        LoggingInterface loggingInterface = appInfra.getLogging().createInstanceForComponent("DemoAppInfra", "1.5");
        for (LoggingInterface.LogLevel logLevel : LoggingInterface.LogLevel.values()) {
            loggingInterface.log(logLevel, null, "message");
            loggingInterface.log(logLevel, "Event", "Message");
        }

    }

    /*Testing fallback if logging key values are not present in 'appconfig.json' file
    * then properties from 'logging.properties' file should be picked
    * this test case will should be removed when  'logging.properties' file is removed under asset
     */
    @Deprecated
    public void testLogWithoutLoggingPropertiesInAppConfig() {

        AppInfra appInfra = new AppInfra.Builder().build(context);
        AppConfigurationInterface configInterface = new AppConfigurationManager(appInfra) {
            @Override
            protected JSONObject getMasterConfigFromApp() {
                JSONObject result = null;
                try {
                    String testJson = ConfigValues.testJson();
                    result = new JSONObject(testJson);
                    JSONObject appInfraConfig = result.optJSONObject("APPINFRA");
                    JSONObject loggingConfig = appInfraConfig.optJSONObject("LOGGING.DEBUGCONFIG");
                    appInfraConfig.put("LOGGING.DEBUGCONFIG", null); // removing logging key values
                    appInfraConfig.put("LOGGING.RELEASECONFIG", null); // removing logging key values
                    result.put("APPINFRA", appInfraConfig);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }
        };
        appInfra = new AppInfra.Builder().setConfig(configInterface).build(context);
        LoggingInterface loggingInterface = appInfra.getLogging().createInstanceForComponent("DemoAppInfra", "1.5");
        for (LoggingInterface.LogLevel logLevel : LoggingInterface.LogLevel.values()) {
            loggingInterface.log(logLevel, null, "message");
            loggingInterface.log(logLevel, "Event", "Message");
        }
    }



}
