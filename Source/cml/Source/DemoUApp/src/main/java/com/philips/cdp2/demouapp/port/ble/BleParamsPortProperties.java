/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.port.ble;

import com.philips.cdp2.commlib.core.port.PortProperties;

public class BleParamsPortProperties implements PortProperties {

    //    Example JSON response:
    //
    //    {
    //        "slowadmin": 1000,
    //        "slowadmax": 1000,
    //        "slowadtimeout": 0,
    //        "slowadenabled": true,
    //        "fastadmin": 20,
    //        "fastadmax": 30,
    //        "fastadtimeout": 30,
    //        "connmin": 7,
    //        "connmax": 1000,
    //        "connslavelatency": 0,
    //        "connsupertimeout": 10000
    //    }

    public Integer slowadmin;

    public Integer slowadmax;

    public Integer slowadtimeout;

    public Boolean slowadenabled;

    public Integer fastadmin;

    public Integer fastadmax;

    public Integer fastadtimeout;

    public Integer connmin;

    public Integer connmax;

    public Integer connslavelatency;

    public Integer connsupertimeout;
}
