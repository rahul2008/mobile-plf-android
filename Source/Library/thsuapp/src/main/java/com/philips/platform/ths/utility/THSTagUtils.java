package com.philips.platform.ths.utility;
/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

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

}
