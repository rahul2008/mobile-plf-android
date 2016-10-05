package com.philips.cdp.dicommclient.cpp.pairing;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.cpp.ICPEventListener;
import com.philips.icpinterface.PairingService;
import com.philips.icpinterface.data.PairingEntitiyReference;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface IPairingController {
    String PERMISSION_RESPONSE = "Response";
    String PERMISSION_CHANGE = "Change";
    String PERMISSION_PUSH = "Push";

    List<String> PAIRING_PERMISSIONS = Collections.unmodifiableList(Arrays.asList(PERMISSION_RESPONSE, PERMISSION_CHANGE));
    List<String> PAIRING_PUSH_PERMISSIONS = Collections.unmodifiableList(Arrays.asList(PERMISSION_PUSH));

    void addRelationship(String relationshipType, PairingHandlerRelationship pairingHandlerRelationship, @NonNull ICPEventListener icpEventListener);

    void addRelationship(String relationshipType, String secretKey, PairingHandlerRelationship pairingHandlerRelationship, @NonNull ICPEventListener icpEventListener);

    void removeRelationship(PairingEntitiyReference trustor, PairingEntitiyReference trustee, String relationType, @NonNull ICPEventListener icpEventListener);

    void addPermission(String relationType, String[] permission, PairingEntitiyReference trustee, @NonNull ICPEventListener icpEventListener);

    void getPermission(String relationType, String[] permission, PairingEntitiyReference trustee, PermissionListener permissionListener, @NonNull ICPEventListener icpEventListener);

    void removePermission(String relationType, String[] permission, PairingEntitiyReference trustee, @NonNull ICPEventListener icpEventListener);

    PairingService createPairingService(@NonNull ICPEventListener icpEventListener);

}
