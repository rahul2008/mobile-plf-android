/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappframework.uappinput;

/*
This class is used for Setting application Context
 */
import android.content.Context;

public class UappSettings {
     protected  Context mContext;

   public UappSettings(Context applicationContext){
       this.mContext=applicationContext;
   }
    public Context getContext(){
        return mContext;
    }
}
