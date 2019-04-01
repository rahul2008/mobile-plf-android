package integration;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;

public class PIMDependencies extends UappDependencies {
    /**
     * Constructor for UappDependencies.
     *
     * @param appInfra Requires appInfraInterface object
     * @since 1.0.0
     */
    public PIMDependencies(AppInfraInterface appInfra) {
        super(appInfra);
    }
}
