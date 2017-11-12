package com.philips.platform.catk;

import com.philips.platform.catk.injection.CatkComponent;

public class CatkConsentAccessToolKitManipulator {
    public static void setCatkComponent(CatkComponent catkComponent) {
        ConsentAccessToolKitMock consentAccessToolKitMock = new ConsentAccessToolKitMock();
        ConsentAccessToolKit.setInstance(consentAccessToolKitMock);
        consentAccessToolKitMock.setCatkComponent(catkComponent);
    }
}
