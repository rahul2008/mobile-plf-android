package com.philips.platform.ths.utility;
/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

import com.philips.platform.ths.intake.THSSymptomsFragment;

import java.util.Date;

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

}
