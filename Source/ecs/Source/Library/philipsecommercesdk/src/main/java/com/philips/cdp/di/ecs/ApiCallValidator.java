package com.philips.cdp.di.ecs;

import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.util.ECSConfig;

import java.util.List;

public class ApiCallValidator {

    ECSError getConfigAPIValidateError(){

        if(isLocaleNull()){
            return new ECSError("Locale is not present",100);
        }

        if(isBaseURLNull()){
            return new ECSError("Base URL is not present",101);
        }
        return null;
    }

    private boolean isAppInfraNull(){
        return ECSConfig.INSTANCE.getAppInfra() == null;
    }

    private boolean isLocaleNull(){
       return ECSConfig.INSTANCE.getLocale() == null;
    }

    private boolean isBaseURLNull(){
        return ECSConfig.INSTANCE.getBaseURL() == null;
    }

    private boolean isCategoryNull(){
        return ECSConfig.INSTANCE.getRootCategory() == null;
    }

    private boolean isSiteIDNull(){
        return ECSConfig.INSTANCE.getSiteId() == null;
    }

    private boolean isINValidString(String inputString){
        return inputString==null && inputString.isEmpty();
    }

    private boolean isINValidList(List list){
        return list==null && list.isEmpty();
    }
}
