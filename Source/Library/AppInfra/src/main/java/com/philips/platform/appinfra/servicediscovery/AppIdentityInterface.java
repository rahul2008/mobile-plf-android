package com.philips.platform.appinfra.servicediscovery;

/**
 * Created by 310238655 on 6/1/2016.
 */
public interface AppIdentityInterface {

    public void configureAppIdentity(String configFilePath);
    public String loadJSONFromAsset();

}
