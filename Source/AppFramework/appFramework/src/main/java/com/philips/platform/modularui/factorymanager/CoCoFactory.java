package com.philips.platform.modularui.factorymanager;


import com.philips.platform.modularui.cocointerface.UICoCoConsumerCareImpl;
import com.philips.platform.modularui.cocointerface.UICoCoInterface;
import com.philips.platform.modularui.cocointerface.UICoCoProdRegImpl;
import com.philips.platform.modularui.cocointerface.UICoCoUserRegImpl;
import com.philips.platform.modularui.util.UIConstants;

/**
 * Created by 310213373 on 7/7/2016.
 */
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
            case UIConstants.UI_COCO_PRODUCT_REGISTRATION:
                return new UICoCoProdRegImpl();
            case UIConstants.UI_COCO_USER_REGISTRATION:
                return UICoCoUserRegImpl.getInstance();
            case UIConstants.UI_COCO_CONSUMER_CARE:
                return UICoCoConsumerCareImpl.getInstance();
            default:
                return null;
        }
    }
}