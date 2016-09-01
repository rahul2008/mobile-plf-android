package com.philips.cdp.dicommclient.port.common;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.icpinterface.data.PairingEntitiyReference;

abstract class PairingHandlerRelationship {
    protected String cppId;
    protected String provider;
    protected String type;
    protected String credentials;
    protected DICommAppliance appliance;

    public PairingHandlerRelationship(String cppId, String provider, String type, String credentials, DICommAppliance appliance) {
        this.cppId = cppId;
        this.provider = provider;
        this.type = type;
        this.credentials = credentials;
        this.appliance = appliance;
    }

    public String getCppId() {
        return cppId;
    }

    public abstract PairingEntitiyReference getTrustorEntity();

    public PairingEntitiyReference getTrusteeEntity() {
        PairingEntitiyReference pairingTrustee = new PairingEntitiyReference();
        pairingTrustee.entityRefId = appliance.getNetworkNode().getCppId();
        pairingTrustee.entityRefProvider = provider;
        pairingTrustee.entityRefType = appliance.getDeviceType();
        pairingTrustee.entityRefCredentials = null;

        DICommLog.i(DICommLog.PAIRING, "Appliance entityRefId"
                + pairingTrustee.entityRefId);
        DICommLog.i(DICommLog.PAIRING, "Appliance entityRefType"
                + pairingTrustee.entityRefType);

        return pairingTrustee;
    }
}

class UserPairingHandlerRelationship extends PairingHandlerRelationship {

    public UserPairingHandlerRelationship(String cppId, String provider, String type, String credentials, DICommAppliance appliance) {
        super(cppId, provider, type, credentials, appliance);
    }

    @Override
    public PairingEntitiyReference getTrustorEntity() {
        PairingEntitiyReference pairingTrustor = new PairingEntitiyReference();
        pairingTrustor.entityRefId = cppId;
        pairingTrustor.entityRefProvider = provider;
        pairingTrustor.entityRefType = type;
        pairingTrustor.entityRefCredentials = credentials;

        DICommLog.i(DICommLog.PAIRING, "User entityRefId" + pairingTrustor.entityRefId);
        DICommLog.i(DICommLog.PAIRING, "User entityRefType" + pairingTrustor.entityRefType);

        return pairingTrustor;
    }
}

class AppPairingHandlerRelationship extends PairingHandlerRelationship {

    public AppPairingHandlerRelationship(String cppId, String provider, DICommAppliance appliance) {
        super(cppId, provider, appliance.getDeviceType(), null, appliance);
    }

    @Override
    public PairingEntitiyReference getTrustorEntity() {
        return null;
    }
}