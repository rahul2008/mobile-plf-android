/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.thsdemolaunch;

import com.philips.platform.appinfra.AppInfraInterface;

public class THSAppInfraInstance {
    static THSAppInfraInstance sTHSAppInfraInstance = new THSAppInfraInstance();

    public AppInfraInterface getAppInfraInterface() {
        return appInfraInterface;
    }

    public void setAppInfraInterface(AppInfraInterface appInfraInterface) {
        this.appInfraInterface = appInfraInterface;
    }

    AppInfraInterface appInfraInterface;

    private THSAppInfraInstance(){

    }

    public static THSAppInfraInstance getInstance(){
        return sTHSAppInfraInstance;
    }
}
