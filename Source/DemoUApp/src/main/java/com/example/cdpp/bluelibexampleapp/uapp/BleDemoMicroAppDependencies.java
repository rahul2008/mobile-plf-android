/*
 * Copyright © 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.example.cdpp.bluelibexampleapp.uapp;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.platform.uappframework.uappinput.UappDependencies;

public class BleDemoMicroAppDependencies extends UappDependencies {

    protected SHNCentral shnCentral;

    public BleDemoMicroAppDependencies(SHNCentral shnCentral) {
        super(null);
        this.shnCentral=shnCentral;
    }

    public BleDemoMicroAppDependencies() {
        super(null);

    }

    public SHNCentral getShnCentral(){
     return shnCentral;
    }
}
