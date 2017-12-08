/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.port.time;

import com.philips.cdp2.commlib.core.port.PortProperties;

public class TimePortProperties implements PortProperties {

//
//    /di/v1/products/0/time
//    {
//        “datetime”      : "2016-09-01T11:29:14", string, ISO8601 based representation of current local time
//        “dst”           : “+00:00”, string, Current DST shift of server in format “+01:00” compared to time zone offset
//        “dstchangeover” : "2016-10-30T03:00:00", string, ISO8601 based representation of the local time at which the next DST changeover occurs
//        “dstoffset”     : "-01:00", string, Change to apply to the current time at the moment of the DST changeover
//        “timezone”      : “+01:00”, string, Time zone of server in format “+01:00” relative to UTC
//        “calday”        : 4, number, Calendar day of Local Time, 1 = Monday, …, 7 = Sunday
//    }

    public String datetime;

    public String dst;

    public String dstchangeover;

    public String dstoffset;

    public String timezone;

    public Integer calday;
}
