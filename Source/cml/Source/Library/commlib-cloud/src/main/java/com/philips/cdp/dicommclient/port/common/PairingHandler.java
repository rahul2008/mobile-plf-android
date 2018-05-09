/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.cloudcontroller.api.CloudController;
import com.philips.cdp.cloudcontroller.api.pairing.PairingController;
import com.philips.cdp.cloudcontroller.api.pairing.PairingEntity;
import com.philips.cdp.cloudcontroller.api.pairing.PairingRelation;
import com.philips.cdp.cloudcontroller.api.pairing.PermissionListener;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.appliance.Appliance;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static com.philips.cdp.dicommclient.networknode.NetworkNode.PairingState.NOT_PAIRED;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.PairingState.PAIRED;

public class PairingHandler<T extends Appliance> {

    public static final String PAIRING_REFERENCEPROVIDER = "cpp";
    public static final String PAIRING_USER_REFERENCEPROVIDER = "cphuser";
    public static final String PAIRING_DI_COMM_RELATIONSHIP = "di-comm";
    public static final String PAIRING_NOTIFY_RELATIONSHIP = "notify";
    public static final String PAIRING_DATA_ACCESS_RELATIONSHIP = "dataaccess";
    public static final String USER_ENTITY_TYPE = "user";

    public static final String RELATION_STATUS_COMPLETED = "completed";
    public static final String RELATION_STATUS_FAILED = "failed";

    public static final int MAX_RETRY = 3;

    private CloudController cloudController;

    private String currentRelationshipType;
    private PairingListener<T> pairingListener;
    private String secretKey;

    private PermissionListener permissionListener;
    private static HashMap<String, Integer> attemptsCount = new HashMap<String, Integer>();
    private PairingRelation pairingRelation;

    private enum ENTITY {
        PURIFIER, APP, DATA_ACCESS
    }

    private ENTITY entityState;
    private T mAppliance;

    @VisibleForTesting
    PairingController.PairingCallback mPairingCallback = new PairingController.PairingCallback() {
        @Override
        public void onRelationshipAdd(String relationStatus) {
            handleRelationshipAdd(relationStatus);
        }

        @Override
        public void onRelationshipRemove() {
            handleRelationshipRemove();
        }

        @Override
        public void onPermissionsAdd() {
            handlePermissionsAdd();
        }

        @Override
        public void onPermissionsRemove() {
            handlePermissionsRemove();
        }

        @Override
        public void onPermissionsGet(Collection<String> permissions) {
            handlePermissionsGet(permissions);
        }

        @Override
        public void onPairingError(int eventType, int status) {
            handlePairingError();
        }
    };

    /**
     * Constructor for PairingHandler.
     *
     * @param appliance T
     */
    public PairingHandler(T appliance, @NonNull final CloudController cloudController) {
        this(appliance, null, cloudController);
    }

    /**
     * Constructor for PairingHandler.
     *
     * @param appliance       T
     * @param pairingListener PairingListener
     */
    public PairingHandler(T appliance, PairingListener<T> pairingListener, @NonNull final CloudController cloudController) {
        if (appliance == null) return;
        this.mAppliance = appliance;
        this.pairingListener = pairingListener;
        this.cloudController = cloudController;

        this.cloudController.getPairingController().setPairingCallback(mPairingCallback);
    }

    /**
     * Constructor for PairingHandler.
     *
     * @param pairingListener PairingListener
     * @param appliance       T
     */
    public PairingHandler(PairingListener<T> pairingListener, T appliance, @NonNull final CloudController cloudController) {
        this(appliance, pairingListener, cloudController);
    }

    /**
     * Method addPermission- adds permission to a existing relationship
     *
     * @param relationType String
     * @param permissions  String[]
     */
    public void addPermission(String relationType, String[] permissions) {
        cloudController.getPairingController().addPermission(relationType, permissions, getDICommApplianceEntity(), mPairingCallback);
    }

    /**
     * Method getPermission-get permissions of a existing relationship
     *
     * @param relationType String
     * @param permissions  String[]
     */
    public void getPermission(String relationType, String[] permissions) {
        cloudController.getPairingController().getPermission(relationType, permissions, getDICommApplianceEntity(), permissionListener, mPairingCallback);
    }

    /**
     * Method removePermission-remove permission from a existing relationship
     *
     * @param relationType String
     * @param permission   String[]
     */
    public void removePermission(String relationType, String[] permission) {
        cloudController.getPairingController().removePermission(relationType, permission, getDICommApplianceEntity(), mPairingCallback);
    }

    private void handleRelationshipAdd(String relationStatus) {
        DICommLog.i(DICommLog.PAIRING, "AddRelation call-SUCCESS");

        if (RELATION_STATUS_COMPLETED.equalsIgnoreCase(relationStatus)) {
            if (PAIRING_DI_COMM_RELATIONSHIP.equalsIgnoreCase(currentRelationshipType)) {
                if (isPerformingUserPairing()) {
                    notifyListenerSuccess();
                } else {
                    DICommLog.i(DICommLog.PAIRING, "Pairing relationship added successfully - Requesting Notification relationship");

                    currentRelationshipType = PAIRING_NOTIFY_RELATIONSHIP;
                    PairingRelation notifyPairingRelation = new PairingRelation(null, new PairingEntity(PAIRING_REFERENCEPROVIDER, mAppliance.getNetworkNode().getCppId(), mAppliance.getDeviceType(), null), PAIRING_NOTIFY_RELATIONSHIP);
                    cloudController.getPairingController().addRelationship(notifyPairingRelation, mPairingCallback);
                }
            } else {
                DICommLog.i(DICommLog.PAIRING, "Notification relationship added successfully - Pairing completed");
                DICommLog.i(DICommLog.PAIRING, "Pairing status set to true");

                mAppliance.getNetworkNode().setPairedState(PAIRED);
                mAppliance.getNetworkNode().setLastPairedTime(new Date().getTime());

                notifyListenerSuccess();
            }
        } else {
            DICommLog.e(DICommLog.PAIRING, "Pairing status is PENDING, pairing attempt:" + getPairingAttempts(mAppliance.getNetworkNode().getCppId()) + " Appliance name:" + mAppliance.getNetworkNode().getName());
            notifyListenerFailed();
        }
    }

    private boolean isPerformingUserPairing() {
        return pairingRelation.getTrustorEntity() != null && pairingRelation.getTrustorEntity().type.equals(USER_ENTITY_TYPE);
    }

    private void handleRelationshipRemove() {
        DICommLog.i(DICommLog.PAIRING, "RemoveRelation call-SUCCESS");

        if (PAIRING_DI_COMM_RELATIONSHIP.equalsIgnoreCase(currentRelationshipType)) {
            switch (entityState) {
                case PURIFIER:
                    DICommLog.i(DICommLog.PAIRING, "Outgoing DI-COMM relationship (one removed) - Need to remove the other");

                    entityState = ENTITY.APP;
                    PairingRelation relationshipToRemove = new PairingRelation(getDICommApplianceEntity(), getAppEntity(), currentRelationshipType);
                    cloudController.getPairingController().removeRelationship(relationshipToRemove, mPairingCallback);

                    break;
                case APP:
                    DICommLog.i(DICommLog.PAIRING, "DI-COMM Relationship removed successfully");

                    entityState = ENTITY.DATA_ACCESS;
                    currentRelationshipType = PAIRING_NOTIFY_RELATIONSHIP;

                    relationshipToRemove = new PairingRelation(getAppEntity(), getDICommApplianceEntity(), currentRelationshipType);
                    cloudController.getPairingController().removeRelationship(relationshipToRemove, mPairingCallback);

                    break;
                case DATA_ACCESS:
                default:
                    DICommLog.w(DICommLog.PAIRING, String.format("Unhandled entity state [%s] for relationship type [%s]", entityState, currentRelationshipType));
                    break;
            }
        } else if (PAIRING_NOTIFY_RELATIONSHIP.equalsIgnoreCase(currentRelationshipType)) {
            switch (entityState) {
                case DATA_ACCESS:
                    DICommLog.i(DICommLog.PAIRING, "Notify Relationship removed successfully");

                    entityState = ENTITY.PURIFIER;
                    currentRelationshipType = PAIRING_DATA_ACCESS_RELATIONSHIP;

                    PairingRelation relationshipToRemove = new PairingRelation(getDICommApplianceEntity(), null, currentRelationshipType);
                    cloudController.getPairingController().removeRelationship(relationshipToRemove, mPairingCallback);

                    break;
                case PURIFIER:
                case APP:
                default:
                    DICommLog.w(DICommLog.PAIRING, String.format("Unhandled entity state [%s] for relationship type [%s]", entityState, currentRelationshipType));
                    break;
            }
        }

        // This will indicate all relations have been removed
        else if (PAIRING_DATA_ACCESS_RELATIONSHIP.equalsIgnoreCase(currentRelationshipType)) {
            switch (entityState) {
                case PURIFIER:
                    DICommLog.i(DICommLog.PAIRING, "DATA_ACCESS Relationship removed successfully - Pairing removed successfully");

                    notifyListenerSuccess();
                    break;
                case APP:
                case DATA_ACCESS:
                default:
                    DICommLog.w(DICommLog.PAIRING, String.format("Unhandled entity state [%s] for relationship type [%s]", entityState, currentRelationshipType));
                    break;
            }
        }
    }

    private void handlePermissionsAdd() {
        permissionListener.onPermissionAdded();
    }

    private void handlePermissionsRemove() {
        permissionListener.onPermissionRemoved();
    }

    private void handlePermissionsGet(Collection<String> permissions) {
        boolean hasPermissionPush = permissions.contains(PairingController.PERMISSION_PUSH);
        permissionListener.onPermissionReturned(hasPermissionPush);
    }

    private void handlePairingError() {
        if (permissionListener == null) {
            DICommLog.e(DICommLog.PAIRING, "Pairing call-FAILED (get or add), pairing attempt:" +
                    getPairingAttempts(mAppliance.getNetworkNode().getCppId()) + " Appliance name:" + mAppliance.getNetworkNode().getName());
            notifyListenerFailed();
        } else {
            DICommLog.e(DICommLog.PAIRING, "get permission call failed");
            permissionListener.onCallFailed();
        }
    }

    public void setPermissionListener(PermissionListener permissionListener) {
        this.permissionListener = permissionListener;
    }

    /**
     * Method startPairing- starts pairing procedure
     */
    public void startPairing() {
        if (mAppliance == null) return;
        DICommLog.i(DICommLog.PAIRING, "Started pairing with appliance = " + mAppliance.getNetworkNode().getName() + " attempt: " + getPairingAttempts(mAppliance.getNetworkNode().getCppId()));

        currentRelationshipType = PAIRING_DI_COMM_RELATIONSHIP;
        pairingRelation = new PairingRelation(
                getAppEntity(),
                getDICommApplianceEntity(),
                PAIRING_DI_COMM_RELATIONSHIP
        );

        startPairingPortTask(pairingRelation);
    }

    /**
     * Method startUserPairing- starts pairing procedure based on a user relation
     *
     * @param userId      the user id
     * @param accessToken the access token
     * @since UNRELEASED
     */
    public void startUserPairing(String userId, String accessToken) {
        if (mAppliance == null) return;
        DICommLog.i(DICommLog.PAIRING, "Started user pairing with appliance = " + mAppliance.getNetworkNode().getName() + " attempt: " + getPairingAttempts(mAppliance.getNetworkNode().getCppId()));

        currentRelationshipType = PAIRING_DI_COMM_RELATIONSHIP;
        pairingRelation = new PairingRelation(
                new PairingEntity(PAIRING_USER_REFERENCEPROVIDER, userId, USER_ENTITY_TYPE, accessToken),
                getDICommApplianceEntity(),
                PAIRING_DI_COMM_RELATIONSHIP
        );

        startPairingPortTask(pairingRelation);
    }

    /**
     * Method startPairingPortTask.
     *
     * @param pairingRelation PairingRelation
     */
    private void startPairingPortTask(final PairingRelation pairingRelation) {
        if (mAppliance == null) return;
        secretKey = generateRandomSecretKey();

        PairingPort pairingPort = mAppliance.getPairingPort();
        pairingPort.addPortListener(new DICommPortListener<PairingPort>() {

            @Override
            public void onPortUpdate(PairingPort port) {
                DICommLog.i(DICommLog.PAIRING, "PairingPort call-SUCCESS");

                cloudController.getPairingController().addRelationship(pairingRelation, mPairingCallback, secretKey);
                port.removePortListener(this);
            }

            @Override
            public void onPortError(PairingPort port, Error error, String errorData) {
                DICommLog.e(DICommLog.PAIRING, "PairingPort call-FAILED: " + errorData);
                DICommLog.e(DICommLog.PAIRING, "PairingPort call-FAILED: " + error.getErrorMessage());

                notifyListenerFailed();
                port.removePortListener(this);
            }
        });

        final String clientType = pairingRelation.getTrustorEntity() == null ? null : pairingRelation.getTrustorEntity().type;
        final String clientId = pairingRelation.getTrustorEntity() == null ? null : pairingRelation.getTrustorEntity().id;

        pairingPort.pair(clientType, clientId, secretKey);
    }

    public void initializeRelationshipRemoval() {
        currentRelationshipType = PAIRING_DI_COMM_RELATIONSHIP;
        entityState = ENTITY.PURIFIER;

        PairingRelation relationship = new PairingRelation(null, getDICommApplianceEntity(), currentRelationshipType);
        cloudController.getPairingController().removeRelationship(relationship, mPairingCallback);
    }

    private PairingEntity getDICommApplianceEntity() {
        PairingEntity pairingTrustee = new PairingEntity(PAIRING_REFERENCEPROVIDER, mAppliance.getNetworkNode().getCppId(), mAppliance.getDeviceType(), null);

        DICommLog.i(DICommLog.PAIRING, "Appliance entityRefId" + pairingTrustee.id);
        DICommLog.i(DICommLog.PAIRING, "Appliance entityRefType" + pairingTrustee.type);

        return pairingTrustee;
    }

    private PairingEntity getAppEntity() {
        PairingEntity pairingTrustor = new PairingEntity(PAIRING_REFERENCEPROVIDER, cloudController.getAppCppId(), cloudController.getAppType(), null);

        DICommLog.i(DICommLog.PAIRING, "app entityRefId" + pairingTrustor.id);
        DICommLog.i(DICommLog.PAIRING, "app entityRefType" + pairingTrustor.type);

        return pairingTrustor;
    }

    /**
     * generates random key
     *
     * @return random secret key
     */
    public String generateRandomSecretKey() {
        byte[] randombytes = new byte[8];
        new SecureRandom().nextBytes(randombytes);
        String randomString = "";
        for (byte randombyte : randombytes) {
            randomString += String.format("%02x", randombyte);
        }
        return randomString;
    }

    public static int getPairingAttempts(String eui64) {
        int attempts = 0;
        if (attemptsCount.containsKey(eui64)) {
            attempts = attemptsCount.get(eui64);
        }
        return attempts;
    }

    public static void clear() {
        if (attemptsCount != null)
            attemptsCount.clear();
    }

    public void setPairingAttempts(String eui64) {
        int attempts = 0;
        if (attemptsCount.containsKey(eui64)) {
            attempts = attemptsCount.get(eui64);
        }
        attemptsCount.put(eui64, attempts + 1);
    }

    public void resetPairingAttempts(String eui64) {
        attemptsCount.put(eui64, 0);
    }

    private void notifyListenerSuccess() {
        if (pairingListener == null) return;
        pairingListener.onPairingSuccess(mAppliance);
    }

    private void notifyListenerFailed() {
        if (mAppliance == null) return;
        if (getPairingAttempts(mAppliance.getNetworkNode().getCppId()) < MAX_RETRY) {
            setPairingAttempts(mAppliance.getNetworkNode().getCppId());
            startPairingPortTask(pairingRelation);
        } else {
            mAppliance.getNetworkNode().setPairedState(NOT_PAIRED);
            if (pairingListener != null) {
                pairingListener.onPairingFailed(mAppliance);
            }
        }
    }

    public static long getDiffInDays(long pairedOn) {
        Date currentDate = new Date();
        long currentTimeInMillis = currentDate.getTime();

        // Difference between current and previous timestamp
        long diff = currentTimeInMillis - pairedOn;

        return diff / TimeUnit.DAYS.toMillis(1);
    }

    public static boolean pairApplianceIfNecessary(final @NonNull NetworkNode networkNode) {
        DICommLog.i(DICommLog.PAIRING, "pairApplianceIfNecessary: " + networkNode.getPairedState());

        // First time pairing or on EWS
        if (networkNode.getPairedState() == NOT_PAIRED) {
            return true;
        }
        // Everyday check for pairing
        long lastPairingCheckTime = networkNode.getLastPairedTime();
        long diffInDays = PairingHandler.getDiffInDays(lastPairingCheckTime);

        return networkNode.getPairedState() == PAIRED && diffInDays != 0;
    }
}
