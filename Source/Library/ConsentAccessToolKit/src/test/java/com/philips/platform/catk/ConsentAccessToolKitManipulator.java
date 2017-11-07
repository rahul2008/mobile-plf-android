package com.philips.platform.catk;


import com.philips.platform.catk.injection.CatkComponent;

public class ConsentAccessToolKitManipulator {

    public static void setCatkComponent(CatkComponent catkComponent) {
        ConsentAccessToolKit.getInstance().setCatkComponent(catkComponent);
    }

}
