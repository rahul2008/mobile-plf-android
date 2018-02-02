/*
 * Copyright (c) 2018 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.permission;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;

public class PermissionHelper {
    private static volatile PermissionHelper permissionHelper;
    private MyAccountUIEventListener myAccountUIEventListener;

    public static synchronized PermissionHelper getInstance() {
        if (permissionHelper == null) {
            synchronized (RegistrationConfiguration.class) {
                if (permissionHelper == null) {
                    permissionHelper = new PermissionHelper();
                }
            }
        }
        return permissionHelper;
    }

    public MyAccountUIEventListener getUserRegistrationUIEventListener() {
        return myAccountUIEventListener;
    }

    public void setUserRegistrationUIEventListener(MyAccountUIEventListener myAccountUIEventListener) {
        this.myAccountUIEventListener = myAccountUIEventListener;
    }
}
