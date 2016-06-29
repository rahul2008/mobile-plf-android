package com.philips.platform.appinfra;

/**
 * Created by 310238655 on 6/29/2016.
 */
public class GlobalStore {

    private String pageName;
    private static GlobalStore mInstance= null;

    protected GlobalStore(){}

    public static synchronized GlobalStore getInstance(){
        if(null == mInstance){
            mInstance = new GlobalStore();
        }
        return mInstance;
    }


    public String getValue() {
        return pageName;
    }


    public void setValue(String pageName) {
        this.pageName = pageName;
    }
}
