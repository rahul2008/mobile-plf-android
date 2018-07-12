package com.philips.pins.shinelib.utility;

import com.philips.pins.shinelib.tagging.SHNTagger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@PrepareForTest({SHNTagger.class})
@RunWith(PowerMockRunner.class)
public class LoggingExceptionHandlerTest {

    private LoggingExceptionHandler handler;

    @Before
    public void setUp() {
        mockStatic(SHNTagger.class);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        handler = new LoggingExceptionHandler();
    }

    @Test
    public void whenUncaughtExceptionIsProcessedByLoggingExceptionHandler_thenTagIsSentWithProperData() {
        Throwable throwable = new Throwable();
        handler.uncaughtException(new Thread(new Runnable() {
            @Override
            public void run() {
            }
        }), throwable);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verifyStatic(SHNTagger.class, times(1));
        SHNTagger.sendTechnicalError(captor.capture());
        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        final String result = String.format(Locale.US, "Uncaught exception: %s\nStack trace: %s\n", throwable.toString(), sw.toString());
        assertEquals(result, captor.getValue());
    }

    private class ExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
        }
    }
}
