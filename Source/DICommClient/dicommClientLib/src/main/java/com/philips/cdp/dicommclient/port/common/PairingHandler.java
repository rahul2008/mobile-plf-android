/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import android.support.annotation.VisibleForTesting;

import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.cloudcontroller.pairing.AppPairingHandlerRelationship;
import com.philips.cdp.cloudcontroller.pairing.PairingController;
import com.philips.cdp.cloudcontroller.pairing.PairingEntityReference;
import com.philips.cdp.cloudcontroller.pairing.PairingHandlerRelationship;
import com.philips.cdp.cloudcontroller.pairing.PermissionListener;
import com.philips.cdp.cloudcontroller.pairing.UserPairingHandlerRelationship;
import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.util.DICommLog;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

public class PairingHandler<T extends DICommAppliance> {

    public static final String PAIRING_REFERENCEPROVIDER = "cpp";
    public static final String PAIRING_USER_REFERENCEPROVIDER = "cphuser";
    public static final String PAIRING_DI_COMM_RELATIONSHIP = "di-comm";
    public static final String PAIRING_NOTIFY_RELATIONSHIP = "notify";
    public static final String PAIRING_DATA_ACCESS_RELATIONSHIP = "dataaccess";

    public static final String RELATION_STATUS_COMPLETED = "completed";
    public static final String RELATION_STATUS_FAILED = "failed";

    public static final int MAX_RETRY = 3;

    private CloudController cloudController;

    private String currentRelationshipType;
    private PairingListener<T> pairingListener;
    private String secretKey;

    private PermissionListener permissionListener;
    private static HashMap<String, Integer> attemptsCount = new HashMap<String, Integer>();
    private PairingHandlerRelationship pairingHandlerRelationship;

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
    public PairingHandler(T appliance) {
        this(appliance, null);
    }

    /**
     * Constructor for PairingHandler.
     *
     * @param appliance       T
     * @param pairingListener PairingListener
     */
    public PairingHandler(T appliance, PairingListener<T> pairingListener) {
        if (appliance == null) return;
        this.mAppliance = appliance;
        this.pairingListener = pairingListener;
        cloudController = DICommClientWrapper.getCloudController();
        cloudController.getPairingController().setPairingCallback(mPairingCallback);
    }

    /**
     * Constructor for PairingHandler.
     *
     * @param iPairingListener PairingListener
     * @param appliance        T
     */
    public PairingHandler(PairingListener<T> iPairingListener, T appliance) {
        this(appliance, iPairingListener);
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
                if (pairingHandlerRelationship instanceof UserPairingHandlerRelationship) {
                    notifyListenerSuccess();
                } else {
                    DICommLog.i(DICommLog.PAIRING, "Pairing relationship added successfully - Requesting Notification relationship");

                    currentRelationshipType = PAIRING_NOTIFY_RELATIONSHIP;
                    AppPairingHandlerRelationship appPairingHandlerRelationship = new AppPairingHandlerRelationship(cloudController.getAppCppId(), PAIRING_REFERENCEPROVIDER, mAppliance.getNetworkNode().getCppId(), mAppliance.getDeviceType());
                    cloudController.getPairingController().addRelationship(PAIRING_NOTIFY_RELATIONSHIP, appPairingHandlerRelationship, mPairingCallback);
                }
            } else {
                DICommLog.i(DICommLog.PAIRING, "Notification relationship added successfully - Pairing completed");
                DICommLog.i(DICommLog.PAIRING, "Pairing status set to true");

                mAppliance.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
                mAppliance.getNetworkNode().setLastPairedTime(new Date().getTime());

                //TODO see user story COM-89
                DiscoveryManager<T> discoveryManager = (DiscoveryManager<T>) DiscoveryManager.getInstance();
                T appliance = discoveryManager.getApplianceByCppId(mAppliance.getNetworkNode().getCppId());
                appliance.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
                discoveryManager.updateApplianceInDatabase(appliance);

                notifyListenerSuccess();
            }
        } else {
            DICommLog.e(DICommLog.PAIRING, "Pairing status is PENDING, pairing attempt:" + getPairingAttempts(mAppliance.getNetworkNode().getCppId()) + " Appliance name:" + mAppliance.getNetworkNode().getName());
            notifyListenerFailed();
        }
    }

    private void handleRelationshipRemove() {
        DICommLog.i(DICommLog.PAIRING, "RemoveRelation call-SUCCESS");

        if (PAIRING_DI_COMM_RELATIONSHIP.equalsIgnoreCase(currentRelationshipType)) {
            switch (entityState) {
                case PURIFIER:
                    DICommLog.i(DICommLog.PAIRING, "Outgoing DI-COMM relationship (one removed) - Need to remove the other");

                    entityState = ENTITY.APP;
                    cloudController.getPairingController().removeRelationship(getDICommApplianceEntity(), getAppEntity(), currentRelationshipType, mPairingCallback);

                    break;
                case APP:
                    DICommLog.i(DICommLog.PAIRING, "DI-COMM Relationship removed successfully");

                    entityState = ENTITY.DATA_ACCESS;
                    currentRelationshipType = PAIRING_NOTIFY_RELATIONSHIP;

                    cloudController.getPairingController().removeRelationship(getAppEntity(), getDICommApplianceEntity(), currentRelationshipType, mPairingCallback);

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

                    cloudController.getPairingController().removeRelationship(getDICommApplianceEntity(), null, currentRelationshipType, mPairingCallback);

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
        String appEui64 = cloudController.getAppCppId();
        pairingHandlerRelationship = new AppPairingHandlerRelationship(appEui64, PAIRING_REFERENCEPROVIDER, mAppliance.getNetworkNode().getCppId(), mAppliance.getDeviceType());

        startPairingPortTask(currentRelationshipType, pairingHandlerRelationship);
    }

    /**
     * Method startUserPairing- starts pairing procedure based on a user relation
     *
     * @param userId
     * @param accessToken
     * @param relationType
     * @since UNRELEASED
     */
    public void startUserPairing(String userId, String accessToken, String relationType) {
        if (mAppliance == null) return;
        DICommLog.i(DICommLog.PAIRING, "Started user pairing with appliance = " + mAppliance.getNetworkNode().getName() + " attempt: " + getPairingAttempts(mAppliance.getNetworkNode().getCppId()));

        currentRelationshipType = PAIRING_DI_COMM_RELATIONSHIP;
        pairingHandlerRelationship = new UserPairingHandlerRelationship(userId, PAIRING_USER_REFERENCEPROVIDER, relationType, accessToken, mAppliance.getNetworkNode().getCppId(), mAppliance.getDeviceType());

        startPairingPortTask(currentRelationshipType, pairingHandlerRelationship);
    }

    /**
     * Method startPairingPortTask.
     *
     * @param relationshipType           String
     * @param pairingHandlerRelationship PairingHandlerRelationship
     */
    private void startPairingPortTask(final String relationshipType, final PairingHandlerRelationship pairingHandlerRelationship) {
        if (relationshipType.equals(PAIRING_DI_COMM_RELATIONSHIP)) {
            if (mAppliance == null) return;
            secretKey = generateRandomSecretKey();

            PairingPort pairingPort = mAppliance.getPairingPort();
            pairingPort.addPortListener(new DICommPortListener() {

                @Override
                public void onPortUpdate(DICommPort<?> port) {
                    DICommLog.i(DICommLog.PAIRING, "PairingPort call-SUCCESS");

                    if (PAIRING_DI_COMM_RELATIONSHIP.equals(currentRelationshipType)) {
                        cloudController.getPairingController().addRelationship(currentRelationshipType, secretKey, pairingHandlerRelationship, mPairingCallback);
                    } else {
                        cloudController.getPairingController().addRelationship(currentRelationshipType, pairingHandlerRelationship, mPairingCallback);
                    }
                    port.removePortListener(this);
                }

                @Override
                public void onPortError(DICommPort<?> port, Error error, String errorData) {
                    DICommLog.e(DICommLog.PAIRING, "PairingPort call-FAILED");

                    notifyListenerFailed();
                    port.removePortListener(this);
                }
            });
            pairingPort.triggerPairing(cloudController.getAppType(), pairingHandlerRelationship.getCppId(), secretKey);
        } else {
            currentRelationshipType = relationshipType;
            cloudController.getPairingController().addRelationship(relationshipType, pairingHandlerRelationship, mPairingCallback);
        }
    }

    public void initializeRelationshipRemoval() {
        currentRelationshipType = PAIRING_DI_COMM_RELATIONSHIP;
        entityState = ENTITY.PURIFIER;

        cloudController.getPairingController().removeRelationship(null, getDICommApplianceEntity(), currentRelationshipType, mPairingCallback);
    }

    /**
     * add Trustee data
     *
     * @return PairingController.PairingEntityReference
     */
    private PairingEntityReference getDICommApplianceEntity() {
        PairingEntityReference pairingTrustee = new PairingEntityReference();
        pairingTrustee.entityRefId = mAppliance.getNetworkNode().getCppId();
        pairingTrustee.entityRefProvider = PAIRING_REFERENCEPROVIDER;
        pairingTrustee.entityRefType = mAppliance.getDeviceType();
        pairingTrustee.entityRefCredentials = null;

        DICommLog.i(DICommLog.PAIRING, "Appliance entityRefId" + pairingTrustee.entityRefId);
        DICommLog.i(DICommLog.PAIRING, "Appliance entityRefType" + pairingTrustee.entityRefType);

        return pairingTrustee;
    }

    /**
     * add Trustee data
     *
     * @return PairingController.PairingEntityReference
     */
    private PairingEntityReference getAppEntity() {
        PairingEntityReference pairingTrustor = new PairingEntityReference();
        pairingTrustor.entityRefId = cloudController.getAppCppId();
        pairingTrustor.entityRefProvider = PAIRING_REFERENCEPROVIDER;
        pairingTrustor.entityRefType = cloudController.getAppType();
        pairingTrustor.entityRefCredentials = null;

        DICommLog.i(DICommLog.PAIRING, "app entityRefId" + pairingTrustor.entityRefId);
        DICommLog.i(DICommLog.PAIRING, "app entityRefType" + pairingTrustor.entityRefType);

        return pairingTrustor;
    }

    /**
     * generates random key
     *
     * @return random secret key
     */
    public String generateRandomSecretKey() {
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
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
            // If DI-COMM local (Pairing Port) request fails, then retry only the DI-COMM request
            if (pairingHandlerRelationship instanceof UserPairingHandlerRelationship) {
                startUserPairing(pairingHandlerRelationship.getCppId(), pairingHandlerRelationship.getCredentials(), pairingHandlerRelationship.getType());
            } else {
                startPairing();
            }
        } else {
            mAppliance.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.NOT_PAIRED);
            if (pairingListener == null) return;
            pairingListener.onPairingFailed(mAppliance);
        }
    }

    public static long getDiffInDays(long pairedOn) {
        Date currentDate = new Date();
        long currentTimeInMillis = currentDate.getTime();

        // Difference between current and previous timestamp
        long diff = currentTimeInMillis - pairedOn;
        long diffInDays = diff / (1000 * 60 * 60 * 24);

        return diffInDays;
    }

    public static boolean pairApplianceIfNecessary(NetworkNode networkNode) {
        if (networkNode == null || networkNode.getConnectionState() != ConnectionState.CONNECTED_LOCALLY) {
            return false;
        }
        DICommLog.i(DICommLog.PAIRING, "In PairToPurifier: " + networkNode.getPairedState());

        // First time pairing or on EWS
        if (networkNode.getPairedState() == NetworkNode.PAIRED_STATUS.NOT_PAIRED) {
            return true;
        }
        //Everyday check for pairing
        long lastPairingCheckTime = networkNode.getLastPairedTime();
        long diffInDays = PairingHandler.getDiffInDays(lastPairingCheckTime);

        if (networkNode.getPairedState() == NetworkNode.PAIRED_STATUS.PAIRED && diffInDays != 0) {
            return true;
        }
        return false;
    }
}
