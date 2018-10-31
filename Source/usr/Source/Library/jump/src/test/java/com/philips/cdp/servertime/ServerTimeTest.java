/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.servertime;

import com.philips.ntputils.ServerTime;
import com.philips.ntputils.constants.ServerTimeConstants;
import com.philips.platform.appinfra.timesync.TimeInterface;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@RunWith(MockitoJUnitRunner.class)
public class ServerTimeTest extends TestCase {

    @Mock
    private TimeInterface timeInterface;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRefreshOffset() {
        ServerTime.getCurrentTime();
        ServerTime.refreshOffset();
        assertNull(ServerTime.getCurrentUTCTimeWithFormat("dd-mm-yyyy"));
    }


    @Test
    public void testRefreshOffsetCall() {
        final SimpleDateFormat sdf = new SimpleDateFormat(ServerTimeConstants.DATE_FORMAT, Locale.ROOT);
        Date date = new Date(0);
        sdf.setTimeZone(TimeZone.getTimeZone(ServerTimeConstants.UTC));
        String firstJan1970 = sdf.format(date);

        assertNotSame(firstJan1970, ServerTime.getCurrentUTCTimeWithFormat(ServerTimeConstants.DATE_FORMAT));
    }

}