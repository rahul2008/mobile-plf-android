package com.philips.platform.appinfra.consentmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.List;

/**
 * Provides callback whenever there is an attempt to change the status of the {@link ConsentDefinition}
 * To observer changes it must be registered through {@link ConsentManager#registerConsentDefinitions(List)}
 */
public interface ConsentStatusChangedListener {
    /**
     * Provides callback when value update for {@link ConsentDefinition} is requested.
     * @param consentDefinition {@link ConsentDefinition} which needs to be observed.
     * @param consentError {@link ConsentError} if any. Null value means it's a success otherwise an error occured during post.
     * @param requestedStatus The original request status and not the updated value after store operation.
     */
    void consentStatusChanged(@NonNull ConsentDefinition consentDefinition, @Nullable ConsentError consentError, boolean requestedStatus);
}