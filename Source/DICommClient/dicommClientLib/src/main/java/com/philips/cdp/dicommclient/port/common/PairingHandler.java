/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.cpp.ICPEventListener;
import com.philips.cdp.dicommclient.cpp.pairing.AppPairingHandlerRelationship;
import com.philips.cdp.dicommclient.cpp.pairing.IPairingController;
import com.philips.cdp.dicommclient.cpp.pairing.PairingHandlerRelationship;
import com.philips.cdp.dicommclient.cpp.pairing.PermissionListener;
import com.philips.cdp.dicommclient.cpp.pairing.UserPairingHandlerRelationship;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.icpinterface.ICPClient;
import com.philips.icpinterface.PairingService;
import com.philips.icpinterface.data.Commands;
import com.philips.icpinterface.data.Errors;
import com.philips.icpinterface.data.PairingEntitiyReference;

import java.util.Date;
import java.util.HashMap;

public class PairingHandler<T extends DICommAppliance> {

    public static final String PAIRING_REFERENCEPROVIDER = "cpp";
    public static final String PAIRING_USER_REFERENCEPROVIDER = "cphuser";
    public static final String PAIRING_DI_COMM_RELATIONSHIP = "di-comm";
    public static final String PAIRING_NOTIFY_RELATIONSHIP = "notify";
    public static final String PAIRING_DATA_ACCESS_RELATIONSHIP = "dataaccess";
    private static final String RELATION_STATUS_COMPLETED = "completed";

    public static final int MAX_RETRY = 3;

    private CppController mCloudController;

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
    ICPEventListener mIcpEventListener = new ICPEventListener() {
        /**
         * Method onICPCallbackEventOccurred.
         *
         * @param eventType int
         * @param status    int
         * @param icpClient ICPClient
         * @see com.philips.cdp.dicommclient.cpp.ICPEventListener#onICPCallbackEventOccurred(int, int, ICPClient)
         */
        @Override
        public void onICPCallbackEventOccurred(int eventType, int status, ICPClient icpClient) {
            DICommLog.d(DICommLog.PAIRING, String.format("onICPCallbackEventOccurred eventType %s status %s", eventType, status));

            // Check event status
            if (status != Errors.SUCCESS) {
                if (permissionListener == null) {
                    DICommLog.e(DICommLog.PAIRING, "Pairing call-FAILED (get or add), pairing attempt:" +
                            getPairingAttempts(mAppliance.getNetworkNode().getCppId()) + " Appliance name:" + mAppliance.getNetworkNode().getName());
                    notifyListenerFailed();
                } else {
                    DICommLog.e(DICommLog.PAIRING, "get permission call failed");
                    permissionListener.onCallFailed();
                }
                return;
            }

            PairingService pairingService = (PairingService) icpClient;

            switch (eventType) {
                case Commands.PAIRING_ADD_RELATIONSHIP:
                    handleAddRelationship(pairingService);
                    break;
                case Commands.PAIRING_REMOVE_RELATIONSHIP:
                    handleRemoveRelationship();
                    break;
                case Commands.PAIRING_ADD_PERMISSIONS:
                    handleAddPermissions();
                    break;
                case Commands.PAIRING_GET_PERMISSIONS:
                    handleGetPermissions(pairingService);
                    break;
                case Commands.PAIRING_REMOVE_PERMISSIONS:
                    handleRemovePermissions();
                    break;
                default:
                    DICommLog.w(DICommLog.PAIRING, "Unhandled event type: " + eventType);
                    break;
            }
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
        mCloudController = DICommClientWrapper.getCloudController();
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
        mCloudController.getPairingController().addPermission(relationType, permissions, getDICommApplianceEntity(), mIcpEventListener);
    }

    /**
     * Method getPermission-get permissions of a existing relationship
     *
     * @param relationType String
     * @param permissions  String[]
     */
    public void getPermission(String relationType, String[] permissions) {
        mCloudController.getPairingController().getPermission(relationType, permissions, getDICommApplianceEntity(), permissionListener, mIcpEventListener);
    }

    /**
     * Method removePermission-remove permission from a existing relationship
     *
     * @param relationType String
     * @param permission   String[]
     */
    public void removePermission(String relationType, String[] permission) {
        mCloudController.getPairingController().removePermission(relationType, permission, getDICommApplianceEntity(), mIcpEventListener);
    }

    private void handleAddRelationship(PairingService pairingService) {
        DICommLog.i(DICommLog.PAIRING, "AddRelation call-SUCCESS");

        String relationStatus = pairingService.getAddRelationStatus();

        if (RELATION_STATUS_COMPLETED.equalsIgnoreCase(relationStatus)) {
            if (PAIRING_DI_COMM_RELATIONSHIP.equalsIgnoreCase(currentRelationshipType)) {
                if (pairingHandlerRelationship instanceof UserPairingHandlerRelationship) {
                    notifyListenerSuccess();
                } else {
                    DICommLog.i(DICommLog.PAIRING, "Pairing relationship added successfully - Requesting Notification relationship");

                    currentRelationshipType = PAIRING_NOTIFY_RELATIONSHIP;
                    AppPairingHandlerRelationship appPairingHandlerRelationship = new AppPairingHandlerRelationship(mCloudController.getAppCppId(), PAIRING_REFERENCEPROVIDER, mAppliance.getNetworkNode().getCppId(), mAppliance.getDeviceType());
                    mCloudController.getPairingController().addRelationship(PAIRING_NOTIFY_RELATIONSHIP, appPairingHandlerRelationship, mIcpEventListener);
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

    private void handleRemoveRelationship() {
        DICommLog.i(DICommLog.PAIRING, "RemoveRelation call-SUCCESS");

        if (PAIRING_DI_COMM_RELATIONSHIP.equalsIgnoreCase(currentRelationshipType)) {
            switch (entityState) {
                case PURIFIER:
                    DICommLog.i(DICommLog.PAIRING, "Outgoing DI-COMM relationship (one removed) - Need to remove the other");
                    entityState = ENTITY.APP;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mCloudController.getPairingController().removeRelationship(getDICommApplianceEntity(), getAppEntity(), currentRelationshipType, mIcpEventListener);
                        }
                    }).start();
                    break;
                case APP:
                    DICommLog.i(DICommLog.PAIRING, "DI-COMM Relationship removed successfully");
                    entityState = ENTITY.DATA_ACCESS;
                    currentRelationshipType = PAIRING_NOTIFY_RELATIONSHIP;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mCloudController.getPairingController().removeRelationship(getAppEntity(), getDICommApplianceEntity(), currentRelationshipType, mIcpEventListener);
                        }
                    }).start();
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

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mCloudController.getPairingController().removeRelationship(getDICommApplianceEntity(), null, currentRelationshipType, mIcpEventListener);
                        }
                    }).start();
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

    private void handleAddPermissions() {
        permissionListener.onPermissionAdded();
    }

    private void handleRemovePermissions() {
        permissionListener.onPermissionRemoved();
    }

    private void handleGetPermissions(PairingService pairingService) {
        boolean hasPermissionPush = false;

        for (int i = 0; i < pairingService.getNumberOfPermissionsReturned(); i++) {
            hasPermissionPush = IPairingController.PERMISSION_PUSH.equals(pairingService.getPermissionAtIndex(i));
            if (hasPermissionPush) break;
        }
        permissionListener.onPermissionReturned(hasPermissionPush);
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
        String appEui64 = mCloudController.getAppCppId();
        pairingHandlerRelationship = new AppPairingHandlerRelationship(appEui64, PAIRING_REFERENCEPROVIDER, mAppliance.getNetworkNode().getCppId(), mAppliance.getDeviceType());

        startPairingPortTask(currentRelationshipType, pairingHandlerRelationship);
    }

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
                        mCloudController.getPairingController().addRelationship(currentRelationshipType, secretKey, pairingHandlerRelationship, mIcpEventListener);
                    } else {
                        mCloudController.getPairingController().addRelationship(currentRelationshipType, pairingHandlerRelationship, mIcpEventListener);
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
            pairingPort.triggerPairing(mCloudController.getAppType(), pairingHandlerRelationship.getCppId(), secretKey);
        } else {
            currentRelationshipType = relationshipType;
            mCloudController.getPairingController().addRelationship(relationshipType, pairingHandlerRelationship, mIcpEventListener);
        }
    }

    public void initializeRelationshipRemoval() {
        currentRelationshipType = PAIRING_DI_COMM_RELATIONSHIP;
        entityState = ENTITY.PURIFIER;

        mCloudController.getPairingController().removeRelationship(null, getDICommApplianceEntity(), currentRelationshipType, mIcpEventListener);
    }

    /**
     * add Trustee data
     *
     * @return PairingEntitiyReference
     */
    private PairingEntitiyReference getDICommApplianceEntity() {
        PairingEntitiyReference pairingTrustee = new PairingEntitiyReference();
        pairingTrustee.entityRefId = mAppliance.getNetworkNode().getCppId();
        pairingTrustee.entityRefProvider = PAIRING_REFERENCEPROVIDER;
        pairingTrustee.entityRefType = mAppliance.getDeviceType();
        pairingTrustee.entityRefCredentials = null;

        DICommLog.i(DICommLog.PAIRING, "Appliance entityRefId"
                + pairingTrustee.entityRefId);
        DICommLog.i(DICommLog.PAIRING, "Appliance entityRefType"
                + pairingTrustee.entityRefType);

        return pairingTrustee;
    }

    /**
     * add Trustee data
     *
     * @return PairingEntitiyReference
     */
    private PairingEntitiyReference getAppEntity() {
        PairingEntitiyReference pairingTrustor = new PairingEntitiyReference();
        pairingTrustor.entityRefId = mCloudController.getAppCppId();
        pairingTrustor.entityRefProvider = PAIRING_REFERENCEPROVIDER;
        pairingTrustor.entityRefType = mCloudController.getAppType();
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
