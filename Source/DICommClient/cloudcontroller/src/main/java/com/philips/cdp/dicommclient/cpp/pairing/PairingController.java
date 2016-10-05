package com.philips.cdp.dicommclient.cpp.pairing;

import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.cpp.ICPCallbackHandler;
import com.philips.cdp.dicommclient.cpp.ICPEventListener;
import com.philips.cdp.dicommclient.util.LogConstants;
import com.philips.icpinterface.PairingService;
import com.philips.icpinterface.data.Commands;
import com.philips.icpinterface.data.Errors;
import com.philips.icpinterface.data.PairingEntitiyReference;
import com.philips.icpinterface.data.PairingInfo;
import com.philips.icpinterface.data.PairingRelationship;

public class PairingController implements IPairingController {

    /**
     * PAIRING CONSTANTS
     */
    public static final int PAIRING_RELATIONSHIPDURATION_SEC = 1000000000;  // 8 hours
    public static final int PAIRING_REQUESTTTL_MIN = 5; // ingored by cpp, because purifier already defined it

    private final CppController mCloudController;

    public PairingController(@NonNull CppController cloudController) {
        mCloudController = cloudController;
    }

    @Override
    public void addRelationship(String relationshipType, PairingHandlerRelationship pairingHandlerRelationship, @NonNull ICPEventListener icpEventListener) {
        addRelationship(relationshipType, null, pairingHandlerRelationship, icpEventListener);
    }

    @Override
    public void addRelationship(String relationshipType, String secretKey, PairingHandlerRelationship pairingHandlerRelationship, @NonNull ICPEventListener icpEventListener) {
        if (!mCloudController.isSignOn()) {
            return;
        }

        int status;
        PairingService pairingService = createPairingService(icpEventListener);
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
    public void removeRelationship(PairingEntitiyReference trustor, PairingEntitiyReference trustee, String relationType, @NonNull ICPEventListener icpEventListener) {
        if (!mCloudController.isSignOn()){
            return;
        }

        int status;
        PairingService removeRelationship = createPairingService(icpEventListener);

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
