package com.philips.cdp.registration.configuration;

/**
 * Created by 310202337 on 1/22/2016.
 */
public enum Configuration {

    STAGING("Staging"),
    EVALUATION("Evaluation");

    private String value;

    private Configuration(final String pValue){
        value = pValue;
    }

    public String getValue(){
        System.out.println("Enum value " +value);
        return  value;
    }
}
