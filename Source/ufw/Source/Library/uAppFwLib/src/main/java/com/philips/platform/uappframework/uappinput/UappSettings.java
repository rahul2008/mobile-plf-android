/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappframework.uappinput;

/**
This class is used for Setting & getting application Context
 */

import android.content.Context;

import java.io.Serializable;

public class UappSettings implements Serializable {

    private static final long serialVersionUID = -8187303062569754675L;
    protected  Context mContext;

    /**
     * Constructor for Upappsettings
     * @param applicationContext :For passing application Context
     */

   public UappSettings(Context applicationContext){
       this.mContext=applicationContext;
   }

    /**
     * For retrieving application co
     * @return application context
     */

    public Context getContext(){
        return mContext;
    }
}
