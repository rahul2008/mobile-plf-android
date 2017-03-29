package com.philips.platform.pushnotification;

/**
 * Created by philips on 29/03/17.
 */

public class PushNotificationManager {

    private static PushNotificationManager pushNotificationManager;

    private PushNotificationManager() {

    }

    public PushNotificationManager getInstance(){
        if(pushNotificationManager==null){
            pushNotificationManager=new PushNotificationManager();
        }
        return pushNotificationManager;
    }


}
