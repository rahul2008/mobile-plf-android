/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.servicediscovery;

/**
 * Created by 310238655 on 6/1/2016.
 */
public interface AppIdentityInterface {

    public void configureAppIdentity(String configFilePath);
    public String loadJSONFromAsset();

}
