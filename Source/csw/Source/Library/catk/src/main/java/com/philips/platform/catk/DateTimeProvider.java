package com.philips.platform.catk;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public interface DateTimeProvider {
    DateTime now(DateTimeZone dateTimeZone);
}
