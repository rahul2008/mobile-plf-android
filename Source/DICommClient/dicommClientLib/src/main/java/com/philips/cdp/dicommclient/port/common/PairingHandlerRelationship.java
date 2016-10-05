package com.philips.cdp.dicommclient.port.common;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.icpinterface.data.PairingEntitiyReference;

abstract class PairingHandlerRelationship {
    protected String cppId;
    protected String provider;
    protected String type;
    protected String credentials;
    private final String applianceCppId;
    private final String applianceDeviceType;

    protected PairingHandlerRelationship(String cppId, String provider, String type, String credentials, String applianceCppId, String applicanceDeviceType) {
        this.cppId = cppId;
        this.provider = provider;
        this.type = type;
        this.credentials = credentials;
        this.applianceCppId = applianceCppId;
        this.applianceDeviceType = applicanceDeviceType;
    }

    public String getCppId() {
        return cppId;
    }

    public abstract PairingEntitiyReference getTrustorEntity();

    public PairingEntitiyReference getTrusteeEntity() {
        PairingEntitiyReference pairingTrustee = new PairingEntitiyReference();
        pairingTrustee.entityRefId = applianceCppId;
        pairingTrustee.entityRefProvider = provider;
        pairingTrustee.entityRefType = applianceDeviceType;
        pairingTrustee.entityRefCredentials = null;

        DICommLog.i(DICommLog.PAIRING, "Appliance entityRefId" + pairingTrustee.entityRefId);
        DICommLog.i(DICommLog.PAIRING, "Appliance entityRefType" + pairingTrustee.entityRefType);

        return pairingTrustee;
    }
}

class UserPairingHandlerRelationship extends PairingHandlerRelationship {

    public UserPairingHandlerRelationship(String cppId, String provider, String type, String credentials, String applianceCppId, String applicanceDeviceType) {
        super(cppId, provider, type, credentials, applianceCppId, applicanceDeviceType);
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

    public AppPairingHandlerRelationship(String cppId, String provider, String applianceCppId, String applicanceDeviceType) {
        super(cppId, provider, applicanceDeviceType, null, applianceCppId, applicanceDeviceType);
    }

    @Override
    public PairingEntitiyReference getTrustorEntity() {
        return null;
    }
}
