/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.provider.ComponentProvider;

public class CswConsentAccessToolKitManipulator {

    public static void setCatkComponent(CatkComponent catkComponent) {
        ConsentAccessToolKit.getInstance().setCatkComponent(catkComponent);
    }

    public static void setComponentProvider(ComponentProvider catkComponent) {
        ConsentAccessToolKit.getInstance().setComponentProvider(catkComponent);
    }

    public static void setInstance(ConsentAccessToolKit instance) {
        ConsentAccessToolKit.setInstance(instance);
    }

}
