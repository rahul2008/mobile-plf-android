package com.philips.cdp.dicommclient.cpp.pairing;

import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.cpp.ICPCallbackHandler;
import com.philips.cdp.dicommclient.cpp.ICPEventListener;
import com.philips.cdp.dicommclient.util.LogConstants;
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

    public static final int PAIRING_RELATIONSHIPDURATION_SEC = 1000000000;  // 8 hours
    public static final int PAIRING_REQUESTTTL_MIN = 5; // ingored by cpp, because purifier already defined it

    private final CppController mCloudController;
    private PairingCallback mPairingCallback;

    public DefaultPairingController(@NonNull CppController cloudController) {
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
                Log.w(LogConstants.PAIRING, "Unhandled event type: " + eventType);

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

        pairingService.addRelationshipRequest(pairingHandlerRelationship.getTrustorEntity(), pairingHandlerRelationship.getTrusteeEntity(), null,
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
    public void removeRelationship(PairingEntitiyReference trustor, PairingEntitiyReference trustee, String relationType, @NonNull PairingCallback callback) {
        if (!mCloudController.isSignOn()) {
            return;
        }

        int status;
        PairingService removeRelationship = createPairingService(this);

        status = removeRelationship.removeRelationshipRequest(trustor, trustee, relationType);
        if (Errors.SUCCESS != status) {
            Log.d(LogConstants.PAIRING, "Request Invalid/Failed Status: " + status);
            return;
        }
        removeRelationship.setPairingServiceCommand(Commands.PAIRING_REMOVE_RELATIONSHIP);
        status = removeRelationship.executeCommand();

        if (Errors.REQUEST_PENDING != status) {
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
    public void addPermission(String relationType, String[] permission, PairingEntitiyReference trustee, @NonNull PairingCallback callback) {
        if (!mCloudController.isSignOn()) {
            return;
        }
        int status;
        PairingService pairingService = mCloudController.getPairingController().createPairingService(this);
        status = pairingService.addPermissionsRequest(null, trustee, relationType, permission);

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
    public void getPermission(String relationType, String[] permission, PairingEntitiyReference trustee,
                              PermissionListener permissionListener, @NonNull PairingCallback callback) {
        if (!mCloudController.isSignOn()) {
            permissionListener.onCallFailed();
            return;
        }

        int iMaxPermissons = 5;
        int iPermIndex = 0;

        PairingService pairingService = createPairingService(this);
        int status = pairingService.getPermissionsRequest(null, trustee, relationType, iMaxPermissons, iPermIndex);

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
    public void removePermission(String relationType, String[] permission, PairingEntitiyReference trustee, @NonNull PairingCallback callback) {
        if (!mCloudController.isSignOn()) {
            return;
        }
        PairingService pairingService = createPairingService(this);
        int status = pairingService.removePermissionsRequest(null, trustee, relationType, permission);

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

    @Override
    public PairingService createPairingService(@NonNull ICPEventListener icpEventListener) {
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
}
