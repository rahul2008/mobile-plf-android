package com.philips.cdp.prodreg.launcher;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.uappframework.uappinput.UappDependencies;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegDependencies extends UappDependencies {

    private AppInfra appInfra;

    public AppInfra getAppInfra() {
        return appInfra;
    }

    public void setAppInfra(final AppInfra appInfra) {
        this.appInfra = appInfra;
    }
}
