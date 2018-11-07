package com.philips.platform.catk;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

interface DateTimeProvider {
    DateTime now(DateTimeZone dateTimeZone);
}
