/*
 * Copyright 2016 © Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller.pairing;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.cloudcontroller.ICPCallbackHandler;
import com.philips.cdp.cloudcontroller.ICPEventListener;
import com.philips.cdp.cloudcontroller.util.LogConstants;
import com.philips.icpinterface.ICPClient;
import com.philips.icpinterface.PairingService;
import com.philips.icpinterface.data.Commands;
import com.philips.icpinterface.data.Errors;
import com.philips.icpinterface.data.PairingEntitiyReference;
import com.philips.icpinterface.data.PairingInfo;
import com.philips.icpinterface.data.PairingRelationship;

import java.util.ArrayList;
import java.util.Collection;

public class DefaultPairingController implements PairingController, ICPEventListener {

    private static final int PAIRING_RELATIONSHIPDURATION_SEC = 1000000000;  // 8 hours
    private static final int PAIRING_REQUESTTTL_MIN = 5; // ingored by cpp, because purifier already defined it

    private final CloudController mCloudController;
    private PairingCallback mPairingCallback;

    public DefaultPairingController(@NonNull CloudController cloudController) {
        mCloudController = cloudController;
    }

    @Override
    public void setPairingCallback(@NonNull PairingCallback pairingCallback) {
        mPairingCallback = pairingCallback;
    }

    @Override
    public void onICPCallbackEventOccurred(int eventType, int status, ICPClient obj) {
        PairingService pairingService = (PairingService) obj;

        switch (eventType) {
            case Commands.PAIRING_ADD_RELATIONSHIP:
                mPairingCallback.onRelationshipAdd(pairingService.getAddRelationStatus());

                break;
            case Commands.PAIRING_REMOVE_RELATIONSHIP:
                mPairingCallback.onRelationshipRemove();

                break;
            case Commands.PAIRING_ADD_PERMISSIONS:
                mPairingCallback.onPermissionsAdd();

                break;
            case Commands.PAIRING_GET_PERMISSIONS:
                final Collection<String> permissions = new ArrayList<>();

                for (int i = 0; i < pairingService.getNumberOfPermissionsReturned(); i++) {
                    permissions.add(pairingService.getPermissionAtIndex(i));
                }
                mPairingCallback.onPermissionsGet(permissions);

                break;
            case Commands.PAIRING_REMOVE_PERMISSIONS:
                mPairingCallback.onPermissionsRemove();

                break;
            default:
                Log.e(LogConstants.PAIRING, "Unhandled event type: " + eventType);
                mPairingCallback.onPairingError(eventType, status);

                break;
        }
    }

    @Override
    public void addRelationship(String relationshipType, PairingHandlerRelationship pairingHandlerRelationship, @NonNull PairingCallback callback) {
        addRelationship(relationshipType, null, pairingHandlerRelationship, mPairingCallback);
    }

    @Override
    public void addRelationship(String relationshipType, String secretKey, PairingHandlerRelationship pairingHandlerRelationship, @NonNull PairingCallback callback) {
        if (!mCloudController.isSignOn()) {
            return;
        }

        int status;
        PairingService pairingService = createPairingService(this);
        PairingInfo pairingInfo = secretKey != null ? getPairingInfo(secretKey) : null;

        PairingEntitiyReference trustorInternalRepresentation = pairingHandlerRelationship.getTrustorEntity() == null ? null : convertPairingEntity(pairingHandlerRelationship.getTrustorEntity());
        PairingEntitiyReference trusteeInternalRepresentation = pairingHandlerRelationship.getTrusteeEntity() == null ? null : convertPairingEntity(pairingHandlerRelationship.getTrusteeEntity());

        pairingService.addRelationshipRequest(trustorInternalRepresentation, trusteeInternalRepresentation, null,
                getPairingRelationshipData(relationshipType, PAIRING_PERMISSIONS.toArray(new String[PAIRING_PERMISSIONS.size()])), pairingInfo);

        pairingService.setPairingServiceCommand(Commands.PAIRING_ADD_RELATIONSHIP);
        status = pairingService.executeCommand();

        if (Errors.REQUEST_PENDING != status) {
            Log.d(LogConstants.PAIRING, "Request Invalid/Failed Status: ");
        }
    }

    /**
     * Method removeRelationship-remove an existing relationship
     *
     * @param relationType String
     */
    @Override
    public void removeRelationship(@Nullable PairingEntityReference trustor, @Nullable PairingEntityReference trustee, String relationType, @NonNull PairingCallback callback) {
        if (!mCloudController.isSignOn()) {
            return;
        }

        int status;
        PairingService removeRelationship = createPairingService(this);

        PairingEntitiyReference trustorInternalRepresentation = trustor == null ? null : convertPairingEntity(trustor);
        PairingEntitiyReference trusteeInternalRepresentation = trustee == null ? null : convertPairingEntity(trustee);

        status = removeRelationship.removeRelationshipRequest(trustorInternalRepresentation, trusteeInternalRepresentation, relationType);
        if (Errors.SUCCESS == status) {
            removeRelationship.setPairingServiceCommand(Commands.PAIRING_REMOVE_RELATIONSHIP);
            status = removeRelationship.executeCommand();

            if (Errors.REQUEST_PENDING != status) {
                Log.d(LogConstants.PAIRING, "Request Invalid/Failed Status: " + status);
            }
        } else {
            Log.d(LogConstants.PAIRING, "Request Invalid/Failed Status: " + status);
        }
    }

    /**
     * Method addPermission- adds permission to a existing relationship
     *
     * @param relationType String
     * @param permission   String[]
     */
    @Override
    public void addPermission(String relationType, String[] permission, PairingEntityReference trustee, @NonNull PairingCallback callback) {
        if (!mCloudController.isSignOn()) {
            return;
        }
        int status;
        PairingService pairingService = createPairingService(this);

        status = pairingService.addPermissionsRequest(null, convertPairingEntity(trustee), relationType, permission);

        if (Errors.SUCCESS != status) {
            Log.d(LogConstants.PAIRING, "Request Invalid/Failed Status: " + status);
            return;
        }
        pairingService.setPairingServiceCommand(Commands.PAIRING_ADD_PERMISSIONS);
        status = pairingService.executeCommand();

        if (Errors.REQUEST_PENDING != status) {
            Log.d(LogConstants.PAIRING, "Request Invalid/Failed Status: " + status);
        }
    }

    /**
     * Method getPermission-get permissions of a existing relationship
     *
     * @param relationType String
     * @param permission   String[]
     */
    @Override
    public void getPermission(String relationType, String[] permission, PairingEntityReference trustee,
                              PermissionListener permissionListener, @NonNull PairingCallback callback) {
        if (!mCloudController.isSignOn()) {
            permissionListener.onCallFailed();
            return;
        }

        int iMaxPermissons = 5;
        int iPermIndex = 0;

        PairingService pairingService = createPairingService(this);
        int status = pairingService.getPermissionsRequest(null, convertPairingEntity(trustee), relationType, iMaxPermissons, iPermIndex);

        if (Errors.SUCCESS != status) {
            Log.d(LogConstants.PAIRING, "Request Invalid/Failed Status: " + status);
            permissionListener.onCallFailed();
            return;
        }
        pairingService.setPairingServiceCommand(Commands.PAIRING_GET_PERMISSIONS);
        status = pairingService.executeCommand();

        if (Errors.REQUEST_PENDING != status) {
            permissionListener.onCallFailed();
            Log.d(LogConstants.PAIRING, "Request Invalid/Failed Status: " + status);
        }
    }

    /**
     * Method removePermission-remove permission from a existing relationship
     *
     * @param relationType String
     * @param permission   String[]
     */
    @Override
    public void removePermission(String relationType, String[] permission, PairingEntityReference trustee, @NonNull PairingCallback callback) {
        if (!mCloudController.isSignOn()) {
            return;
        }
        PairingService pairingService = createPairingService(this);
        int status = pairingService.removePermissionsRequest(null, convertPairingEntity(trustee), relationType, permission);

        if (Errors.SUCCESS != status) {
            Log.d(LogConstants.PAIRING, "Request Invalid/Failed Status: " + status);
            return;
        }
        pairingService.setPairingServiceCommand(Commands.PAIRING_REMOVE_PERMISSIONS);
        status = pairingService.executeCommand();

        if (Errors.REQUEST_PENDING != status) {
            Log.d(LogConstants.PAIRING, "Request Invalid/Failed Status: " + status);
        }
    }

    private PairingService createPairingService(@NonNull ICPEventListener icpEventListener) {
        return new PairingService(new ICPCallbackHandler(icpEventListener));
    }

    /**
     * add pairing info
     *
     * @param secretKey
     * @return
     */
    private PairingInfo getPairingInfo(String secretKey) {
        PairingInfo pairingTypeInfo = new PairingInfo();
        pairingTypeInfo.pairingInfoIsMatchIPAddr = false;
        pairingTypeInfo.pairingInfoRequestTTL = PAIRING_REQUESTTTL_MIN;
        pairingTypeInfo.pairingInfoSecretKey = secretKey;

        return pairingTypeInfo;
    }

    /**
     * add pairingRelationshipData
     *
     * @param permission
     * @param relationshipType
     * @return
     */
    private PairingRelationship getPairingRelationshipData(
            String relationshipType, String[] permission) {
        PairingRelationship pairingRelationshipData = new PairingRelationship();
        pairingRelationshipData.pairingRelationshipIsAllowDelegation = false;
        pairingRelationshipData.pairingRelationshipMetadata = null;
        pairingRelationshipData.pairingRelationshipRelationType = relationshipType;
        pairingRelationshipData.pairingRelationshipTTL = PAIRING_RELATIONSHIPDURATION_SEC;
        pairingRelationshipData.pairingRelationshipPermissionArray = permission;

        return pairingRelationshipData;
    }

    public final PairingEntitiyReference convertPairingEntity(PairingEntityReference reference) {
        PairingEntitiyReference result = new PairingEntitiyReference();
        result.entityRefCredentials = reference.entityRefCredentials;
        result.entityRefId = reference.entityRefId;
        result.entityRefProvider = reference.entityRefProvider;
        result.entityRefType = reference.entityRefType;

        return result;
    }
}
