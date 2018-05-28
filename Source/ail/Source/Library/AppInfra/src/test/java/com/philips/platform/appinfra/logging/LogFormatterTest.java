package com.philips.platform.appinfra.logging;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.timesync.TimeInterface;

import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Yogesh on 5/28/18.
 */
public class LogFormatterTest extends TestCase {


    private LogFormatter logFormatter;
    @Mock
    private AppInfra appInfra;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        logFormatter = new LogFormatter(appInfra);
    }

    public void testLogFormat() {
        TimeInterface timeInterface = mock(TimeInterface.class);
        when(appInfra.getTime()).thenReturn(timeInterface);
        Date value = new Date();
        when(timeInterface.getUTCTime()).thenReturn(value);
        LogRecord logRecord = mock(LogRecord.class);
        when(logRecord.getLevel()).thenReturn(Level.FINE);

        Object[] params = new Object[5];
        params[0] = "eventId";
        params[1] = "eventName";
        params[2] = "eventVersion";
        when(logRecord.getParameters()).thenReturn(params);
        String format = logFormatter.format(logRecord);
        assertNotNull(format);
        assertTrue(format.contains("eventId"));
        assertTrue(format.contains("eventName"));
        assertTrue(format.contains("eventVersion"));
        assertNotNull(logFormatter.getHead(new ConsoleHandler()));
        assertNotNull(logFormatter.getTail(new CloudLogHandler(appInfra)));
    }
}