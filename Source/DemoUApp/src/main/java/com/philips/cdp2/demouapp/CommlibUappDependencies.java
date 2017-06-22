/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.platform.uappframework.uappinput.UappDependencies;

public abstract class CommlibUappDependencies extends UappDependencies {

    public CommlibUappDependencies() {
        super(null);
    }

    public abstract CommCentral getCommCentral();
}
