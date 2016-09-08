package com.philips.platform.appinfra.logging;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 * Created by 310238114 on 8/9/2016.
 */
public class LoggingTest extends MockitoTestCase {
    LoggingInterface loggingInterface ;
    LoggingInterface mockIoggingInterface;

    private Context context;
    private AppInfra mAppInfra;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
      /*  AppInfraLogging testLogger = new AppInfraLogging(){
            // public void setAppInfra(AppInfra ai) { mAppInfra = ai; }
            @Override
            protected InputStream getLoggerPropertiesInputStream() throws IOException {
                InputStream inputStream=null;
                String loggerProperties = ".level=FINE\n" +
                        "java.util.logging.ConsoleHandler.level=FINE\n" +
                        "java.util.logging.ConsoleHandler.formatter=java.util.logging.SimpleFormatter\n" +
                        "java.util.logging.FileHandler.pattern = AppInfra%u.log\n" +
                        "java.util.logging.FileHandler.limit =2097152\n" +
                        "java.util.logging.FileHandler.count = 5\n" +
                        "java.util.logging.FileHandler.append =true\n" +
                        "java.util.logging.FileHandler.level = FINE";
                StringBuffer stringBuffer = new StringBuffer(loggerProperties);
                byte[] bytes = stringBuffer.toString().getBytes();
                inputStream = new ByteArrayInputStream(bytes);
                return inputStream;
            }
        };
        mAppInfra = new AppInfra.Builder().setLogging(testLogger).build(context);
        loggingInterface = testLogger;*/
        mAppInfra = new AppInfra.Builder().setLogging(null).build(context);
        loggingInterface =  new AppInfraLogging(mAppInfra){
            // public void setAppInfra(AppInfra ai) { mAppInfra = ai; }
            @Override
            protected InputStream getLoggerPropertiesInputStream() throws IOException {
                InputStream inputStream=null;
                String loggerProperties = ".level=FINE\n" +
                        "java.util.logging.ConsoleHandler.level=FINE\n" +
                        "java.util.logging.ConsoleHandler.formatter=java.util.logging.SimpleFormatter\n" +
                        "java.util.logging.FileHandler.pattern = AppInfra%u.log\n" +
                        "java.util.logging.FileHandler.limit =2097152\n" +
                        "java.util.logging.FileHandler.count = 5\n" +
                        "java.util.logging.FileHandler.append =true\n" +
                        "java.util.logging.FileHandler.level = FINE";
                StringBuffer stringBuffer = new StringBuffer(loggerProperties);
                byte[] bytes = stringBuffer.toString().getBytes();
                inputStream = new ByteArrayInputStream(bytes);
                return inputStream;
            }
        };

       // testLogger.setAppInfra(mAppInfra);
        //loggingInterface = mAppInfra.getLogging();

        assertNotNull(loggingInterface);
       /* loggingInterface.createInstanceForComponent("Component Name","Component version");
        loggingInterface.log(LoggingInterface.LogLevel.INFO,"Event","Message");*/

        mockIoggingInterface = mock(AppInfraLogging.class);
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(mockIoggingInterface).createInstanceForComponent("Component Name mock","Component version");

        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when( mockIoggingInterface).log(LoggingInterface.LogLevel.INFO,"Event","Message");
    }

    public void testLogwithFileAndConsoleEnables(){
       /* loggingInterface.enableConsoleLog(true);
       // loggingInterface.enableConsoleLog(true);
        loggingInterface.enableFileLog(true);
       // loggingInterface.enableFileLog(true);
        for (LoggingInterface.LogLevel logLevel : LoggingInterface.LogLevel.values()) {
            loggingInterface.log(logLevel, null,"message");
            loggingInterface.log(logLevel, "Event","Message");

        }

        for (LoggingInterface.LogLevel logLevel : LoggingInterface.LogLevel.values()) {
            loggingInterface.log(logLevel, null,"message");
            loggingInterface.log(logLevel, "Event","Message");

        }*/



        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(mockIoggingInterface).enableConsoleLog(true);
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(mockIoggingInterface).enableConsoleLog(true);
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(mockIoggingInterface).enableFileLog(true);
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(mockIoggingInterface).enableFileLog(true);
        for (LoggingInterface.LogLevel logLevel : LoggingInterface.LogLevel.values()) {
            doAnswer(new Answer<Object>() {
                public Object answer(InvocationOnMock invocation) {
                    Object[] args = invocation.getArguments();
                    return null;
                }
            }).when(mockIoggingInterface).log(logLevel, null,"message");
            doAnswer(new Answer<Object>() {
                public Object answer(InvocationOnMock invocation) {
                    Object[] args = invocation.getArguments();
                    return null;
                }
            }).when(mockIoggingInterface).log(logLevel, "Event","Message");

        }

        for (LoggingInterface.LogLevel logLevel : LoggingInterface.LogLevel.values()) {
            doAnswer(new Answer<Object>() {
                public Object answer(InvocationOnMock invocation) {
                    Object[] args = invocation.getArguments();
                    return null;
                }
            }).when(mockIoggingInterface).log(logLevel, null,"message");
            doAnswer(new Answer<Object>() {
                public Object answer(InvocationOnMock invocation) {
                    Object[] args = invocation.getArguments();
                    return null;
                }
            }).when(mockIoggingInterface).log(logLevel, "Event","Message");

        }
    }

    public void testLogwithFileAndConsoleDisabled(){
      /*  loggingInterface.enableConsoleLog(false);
        loggingInterface.enableConsoleLog(false);
        loggingInterface.enableFileLog(false);
        loggingInterface.enableFileLog(false);
        for (LoggingInterface.LogLevel logLevel : LoggingInterface.LogLevel.values()) {
            loggingInterface.log(logLevel, null,"message");
            loggingInterface.log(logLevel, "Event","Message");

        }
        loggingInterface.createInstanceForComponent("Component Name","Component version");
        for (LoggingInterface.LogLevel logLevel : LoggingInterface.LogLevel.values()) {
            loggingInterface.log(logLevel, null,"message");
            loggingInterface.log(logLevel, "Event","Message");

        }*/

        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when( mockIoggingInterface).enableConsoleLog(false);
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(mockIoggingInterface).enableConsoleLog(false);
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(mockIoggingInterface).enableFileLog(false);
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(mockIoggingInterface).enableFileLog(false);
        for (LoggingInterface.LogLevel logLevel : LoggingInterface.LogLevel.values()) {
            doAnswer(new Answer<Object>() {
                public Object answer(InvocationOnMock invocation) {
                    Object[] args = invocation.getArguments();
                    return null;
                }
            }).when(mockIoggingInterface).log(logLevel, null,"message");
            doAnswer(new Answer<Object>() {
                public Object answer(InvocationOnMock invocation) {
                    Object[] args = invocation.getArguments();
                    return null;
                }
            }).when(mockIoggingInterface).log(logLevel, "Event","Message");

        }

        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(mockIoggingInterface).createInstanceForComponent("Component Name mock","Component version");
        for (LoggingInterface.LogLevel logLevel : LoggingInterface.LogLevel.values()) {
            doAnswer(new Answer<Object>() {
                public Object answer(InvocationOnMock invocation) {
                    Object[] args = invocation.getArguments();
                    return null;
                }
            }).when(mockIoggingInterface).log(logLevel, null,"message");
            doAnswer(new Answer<Object>() {
                public Object answer(InvocationOnMock invocation) {
                    Object[] args = invocation.getArguments();
                    return null;
                }
            }).when(mockIoggingInterface).log(logLevel, "Event","Message");

        }
    }



}
