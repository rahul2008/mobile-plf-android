package com.philips.cdp.di.iap.model;

/**
 * Created by philips on 2/7/19.
 */

public enum SalutationEnum {

    MR("Mr."),
    MS("Ms.");

    private String field ;


    SalutationEnum(String field){
        this.field = field;
    }

    public String getField(){
        return this.field;
    }

}
