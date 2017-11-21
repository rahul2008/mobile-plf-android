package com.philips.platform.csw.permission;

import com.philips.platform.catk.model.ConsentDefinition;

public interface ConsentToggleListener {
    void onToggledConsent(ConsentDefinition definition, boolean on);
}
