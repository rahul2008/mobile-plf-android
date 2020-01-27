package com.philips.cdp.prodreg.launcher;

import androidx.annotation.NonNull;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;

/**
 * It is used to pass AppInfraInterface from proposition to Product Registration component
 * @since 1.0.0
 */
public class PRDependencies extends UappDependencies {

    private static final long serialVersionUID = -6635233525340545677L;

    private UserDataInterface userDataInterface;

    /**
     * Create PRDependencies instance from AppInfraInterface and UserDataInterface object
     * @param appInfra - to pass the instance of AppInfraInterface
     * @param userDataInterface - to pass thee instance of UserDataInterface
     * @since 1903
     */
    public PRDependencies(@NonNull AppInfraInterface appInfra, @NonNull UserDataInterface userDataInterface) {
        super(appInfra);
        this.userDataInterface = userDataInterface;
    }

    public UserDataInterface getUserDataInterface() {
        return userDataInterface;
    }
}
