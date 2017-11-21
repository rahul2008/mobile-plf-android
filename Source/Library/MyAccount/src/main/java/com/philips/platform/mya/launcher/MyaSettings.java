/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.launcher;

import android.content.Context;

import com.philips.platform.csw.ConsentDefinition;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.util.ArrayList;
import java.util.List;

public class MyaSettings extends UappSettings {

    private final List<ConsentDefinition> consentDefinitions;

    public MyaSettings(Context applicationContext) {
        this(applicationContext, new ArrayList<ConsentDefinition>());
    }

    public MyaSettings(Context applicationContext, List<ConsentDefinition> definitions){
        super(applicationContext);
        consentDefinitions = definitions;
    }

    public ArrayList<ConsentDefinition> getConsentDefinitions() {
        return new ArrayList<>(consentDefinitions);
    }
}
