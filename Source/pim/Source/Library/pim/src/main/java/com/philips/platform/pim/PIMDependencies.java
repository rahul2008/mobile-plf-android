package com.philips.platform.pim;

import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;


public class PIMDependencies extends UappDependencies {
    /**
     * Constructor for UappDependencies.
     *
     * @param appInfra Requires appInfraInterface object
     *
     * @since TODO: App version
     */
    public PIMDependencies(@NonNull AppInfraInterface appInfra) {
        super(appInfra);
    }
}
