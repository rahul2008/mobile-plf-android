/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk;

import com.philips.platform.mya.catk.injection.CatkComponent;
import com.philips.platform.mya.catk.provider.ComponentProvider;

public class ConsentAccessToolKitManipulator {

    public static void setCatkComponent(CatkComponent catkComponent) {
        ConsentAccessToolKit.getInstance().setCatkComponent(catkComponent);
    }

    public static void setComponentProvider(ComponentProvider catkComponent) {
        ConsentAccessToolKit.getInstance().setComponentProvider(catkComponent);
    }

}
