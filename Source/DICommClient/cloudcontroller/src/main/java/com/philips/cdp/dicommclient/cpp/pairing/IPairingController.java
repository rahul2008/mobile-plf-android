package com.philips.cdp.dicommclient.cpp.pairing;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.cpp.ICPEventListener;
import com.philips.icpinterface.PairingService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface IPairingController {
    List<String> PAIRING_PERMISSIONS = Collections.unmodifiableList(Arrays.asList("Response", "Change"));
    List<String> PAIRING_PUSH_PERMISSIONS = Collections.unmodifiableList(Arrays.asList("Push"));

    void startPairing();

    void startUserPairing(String userId, String accessToken, String relationType);

    void initializeRelationshipRemoval();

    void addPermission(String relationType, String[] permission);

    void getPermission(String relationType, String[] permission);

    void removePermission(String relationType, String[] permission);

    void addRelationship(String relationshipType, PairingHandlerRelationship pairingHandlerRelationship, @NonNull ICPEventListener icpEventListener);
    void addRelationship(String relationshipType, String secretKey, PairingHandlerRelationship pairingHandlerRelationship, @NonNull ICPEventListener icpEventListener);

    PairingService createPairingService(@NonNull ICPEventListener icpEventListener);
}
