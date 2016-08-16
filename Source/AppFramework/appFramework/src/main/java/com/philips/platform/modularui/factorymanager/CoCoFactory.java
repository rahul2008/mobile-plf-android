/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.factorymanager;


import com.philips.platform.modularui.cocointerface.UICoCoConsumerCareImpl;
import com.philips.platform.modularui.cocointerface.UICoCoInterface;
import com.philips.platform.modularui.util.UIConstants;

public class CoCoFactory {
    private static CoCoFactory instance = new CoCoFactory();

    private CoCoFactory() {

    }

    public static CoCoFactory getInstance() {
        if (null == instance) {
            instance = new CoCoFactory();
        }
        return instance;
    }

    public UICoCoInterface getCoCo(@UIConstants.UICoCoConstants int coCo) {

        switch (coCo) {
            case UIConstants.UI_COCO_CONSUMER_CARE:
                return UICoCoConsumerCareImpl.getInstance();
            case UIConstants.UI_COCO_IN_APP_PURCHASE:
//                return UICoCoInAppPurchase.getInstance();
            default:
                return null;
        }
    }
}