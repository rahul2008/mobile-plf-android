/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.utility;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.when;

public class THSDateUtilsTest {
    THSDateUtils thsDateUtils;

    Date first;

    Date last;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Calendar calendarFirst = Calendar.getInstance();
        calendarFirst.set(2011,8,7);
        Calendar calendarSecond = Calendar.getInstance();
        calendarSecond.set(2012,8,6);

        first = calendarFirst.getTime();
        last = calendarSecond.getTime();
        thsDateUtils = new THSDateUtils();
    }

    @Test
    public void getDiffYears() throws Exception {
        final int diffYears = THSDateUtils.getDiffYears(first, last);
        assert diffYears == 0;
    }
}