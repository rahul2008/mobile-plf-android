package com.philips.platform.ths.utility;
/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

import com.philips.platform.ths.intake.THSSymptomsFragment;

import java.util.Date;
import java.util.HashMap;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static com.philips.platform.ths.utility.THSConstants.THS_IN_APP_NOTIFICATION;
import static com.philips.platform.ths.utility.THSConstants.THS_IN_APP_NOTIFICATION_RESPONSE;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;

public  class THSTagUtils {

    public static String addActions(String action,String NewAction){
        String updatedAction=action;
        if("".equals(action) ) {
            updatedAction += NewAction;
        } else if(!action.contains(NewAction)){
            updatedAction += "|"+NewAction;
        }
        return updatedAction;
    }

    public static long getCurrentTime(){
        Date date = new Date();
        return date.getTime();
    }

    public static String getVisitPrepareTime(long startTime){
        String timeInMinutedsAndSecond="";
        long time = (new Date() ).getTime() - startTime;
        if(time>0) {
            long second = (time / 1000) % 60;
            long minute = (time / (1000 * 60)) % 60;
            float minutesDecimalFloat= (float) minute + (second/60f) ;
            minutesDecimalFloat = Math.round(minutesDecimalFloat*10.0f)/10.0f;
            timeInMinutedsAndSecond = ""+minutesDecimalFloat;
        }
        return timeInMinutedsAndSecond;
    }

    public static String createErrorTag(String ErrorType, String ErrorMessage){
        StringBuilder value= new StringBuilder(THS_APPLICATION_ID);

        value.append(":");
        value.append(ErrorType);

        value.append(":");
        String serverURL= THSManager.getInstance().getServerURL()!=null?THSManager.getInstance().getServerURL():" ";
        value.append(serverURL);

        value.append(":");
        value.append(ErrorMessage);

        return value.toString();
    }

    public static void tagInAppNotification(String mesage, String buttonLabel){
        HashMap<String,String> map =  new HashMap<String,String>();
        map.put(THS_IN_APP_NOTIFICATION,mesage);
        map.put(THS_IN_APP_NOTIFICATION_RESPONSE,buttonLabel);
        THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, map);
        map=null;
    }

    public static void doTrackActionWithInfo(String action, String key , String value){
        HashMap<String,String> map =  new HashMap<String,String>();
        map.put(key,value);
        String country= THSManager.getInstance().getAppInfra().getServiceDiscovery().getHomeCountry();
        map.put("Country",country);
        THSManager.getInstance().getThsTagging().trackActionWithInfo(action, map);
    }
    public static void doTrackActionWithInfo(String action, HashMap<String , String> map){
        String country= THSManager.getInstance().getAppInfra().getServiceDiscovery().getHomeCountry();
        map.put("Country",country);
        THSManager.getInstance().getThsTagging().trackActionWithInfo(action, map);
    }

}
