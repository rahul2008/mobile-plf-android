/*
 * Copyright 2016 © Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller.pairing;

import android.support.annotation.NonNull;

import com.philips.cdp.cloudcontroller.ICPEventListener;
import com.philips.icpinterface.PairingService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface PairingController {

    interface PairingCallback {
        void onRelationshipAdd(String relationStatus);

        void onRelationshipRemove();

        void onPermissionsAdd();

        void onPermissionsRemove();

        void onPermissionsGet(Collection<String> permissions);

        void onPairingError(int eventType, int status);
    }

    String PERMISSION_RESPONSE = "Response";
    String PERMISSION_CHANGE = "Change";
    String PERMISSION_PUSH = "Push";

    List<String> PAIRING_PERMISSIONS = Collections.unmodifiableList(Arrays.asList(PERMISSION_RESPONSE, PERMISSION_CHANGE));
    List<String> PAIRING_PUSH_PERMISSIONS = Collections.unmodifiableList(Arrays.asList(PERMISSION_PUSH));

    PairingService createPairingService(@NonNull ICPEventListener icpEventListener);

    void setPairingCallback(@NonNull PairingCallback pairingCallback);

    void addRelationship(String relationshipType, PairingHandlerRelationship pairingHandlerRelationship, @NonNull PairingCallback callback);

    void addRelationship(String relationshipType, String secretKey, PairingHandlerRelationship pairingHandlerRelationship, @NonNull PairingCallback callback);

    void removeRelationship(PairingEntityReference trustor, PairingEntityReference trustee, String relationType, @NonNull PairingCallback callback);

    void addPermission(String relationType, String[] permission, PairingEntityReference trustee, @NonNull PairingCallback callback);

    void removePermission(String relationType, String[] permission, PairingEntityReference trustee, @NonNull PairingCallback callback);

    void getPermission(String relationType, String[] permission, PairingEntityReference trustee, PermissionListener permissionListener, @NonNull PairingCallback callback);
}
