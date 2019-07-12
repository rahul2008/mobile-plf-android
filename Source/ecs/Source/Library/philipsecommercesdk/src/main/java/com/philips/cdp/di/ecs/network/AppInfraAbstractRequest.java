package com.philips.cdp.di.ecs.network;

public abstract class AppInfraAbstractRequest implements APPInfraJSONRequest {

    public void executeRequest(){
        new NetworkController(this).executeRequest();
    }
}
