/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.permission;

import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

public interface ConsentToggleListener {
    boolean onToggledConsent(int position, ConsentDefinition definition, boolean on);
}
