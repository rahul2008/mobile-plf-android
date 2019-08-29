package com.philips.cdp.di.ecs;

import com.philips.cdp.di.ecs.util.ECSConfig;

public class StaticBlock {


    public static void initialize(){
        ECSConfig.INSTANCE.setBaseURL("acc.us.pil.shop.philips.com/");
        ECSConfig.INSTANCE.setSiteId("US_Tuscany");
        ECSConfig.INSTANCE.setPropositionID("Tuscany2016");
        ECSConfig.INSTANCE.setLocale("en_US");
    }

    public static  String getBaseURL(){
        return ECSConfig.INSTANCE.getBaseURL();
    }

    public static String getSiteID(){
        return ECSConfig.INSTANCE.getSiteId();
    }

    public static String getPropositionID(){
        return ECSConfig.INSTANCE.getPropositionID();
    }

    public static String getLocale(){
        return ECSConfig.INSTANCE.getLocale();
    }

}
