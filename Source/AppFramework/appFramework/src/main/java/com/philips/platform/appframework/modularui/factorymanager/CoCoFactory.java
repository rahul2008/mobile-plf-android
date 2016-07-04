package com.philips.platform.appframework.modularui.factorymanager;

import com.philips.platform.appframework.modularui.statecontroller.UICoCoInterface;
import com.philips.platform.appframework.modularui.statecontroller.UICoCoProdRegImpl;
import com.philips.platform.appframework.modularui.statecontroller.UICoCoUserRegImpl;
import com.philips.platform.appframework.modularui.util.UIConstants;

/**
 * Created by 310240027 on 6/22/2016.
 */
public class CoCoFactory {

    public UICoCoInterface getCoCo(@UIConstants.UICoCoConstants int coCo) {

        switch (coCo) {
            case UIConstants.UI_COCO_PRODUCT_REGISTRATION:
                return new UICoCoProdRegImpl();
            case UIConstants.UI_COCO_USER_REGISTRATION:
                return new UICoCoUserRegImpl();
            default:
                return null;
        }
    }
}
