package com.philips.cdp.dicommclient.cpp.pairing;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.cpp.ICPEventListener;
import com.philips.icpinterface.PairingService;
import com.philips.icpinterface.data.PairingEntitiyReference;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface IPairingController {
    List<String> PAIRING_PERMISSIONS = Collections.unmodifiableList(Arrays.asList("Response", "Change"));
    List<String> PAIRING_PUSH_PERMISSIONS = Collections.unmodifiableList(Arrays.asList("Push"));

    void addRelationship(String relationshipType, PairingHandlerRelationship pairingHandlerRelationship, @NonNull ICPEventListener icpEventListener);
    void addRelationship(String relationshipType, String secretKey, PairingHandlerRelationship pairingHandlerRelationship, @NonNull ICPEventListener icpEventListener);

    void removeRelationship(PairingEntitiyReference trustor, PairingEntitiyReference trustee, String relationType, @NonNull ICPEventListener icpEventListener);

    PairingService createPairingService(@NonNull ICPEventListener icpEventListener);
}
