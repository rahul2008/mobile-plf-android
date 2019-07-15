package com.philips.cdp.di.ecs.prx.serviceDiscovery;

public class AssetServiceDiscoveryRequest extends ServiceDiscoveryRequest {

    private static final String PRXAssetAssetServiceID = "prxclient.assets";

    public AssetServiceDiscoveryRequest(String ctn) {
        super(ctn, PRXAssetAssetServiceID);
    }
}
