package com.philips.cdp.di.ecs.prx.serviceDiscovery;

public class DisclaimerServiceDiscoveryRequest extends ServiceDiscoveryRequest {

    private static final String PRXDisclaimerDataServiceID = "prxclient.disclaimers";

    public DisclaimerServiceDiscoveryRequest(String ctn) {
        super(ctn, PRXDisclaimerDataServiceID);
    }
}
