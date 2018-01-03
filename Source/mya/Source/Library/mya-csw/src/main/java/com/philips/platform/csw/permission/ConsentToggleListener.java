/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.permission;

import com.philips.platform.mya.chi.ConsentHandlerInterface;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;

public interface ConsentToggleListener {
    void onToggledConsent(ConsentDefinition definition, ConsentHandlerInterface handler, boolean on);
}
