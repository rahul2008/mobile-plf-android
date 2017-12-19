/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappframework.uappinput;

/**
This class is used for Setting & getting application Context
 @since 1.0.0
 */

import android.content.Context;

import java.io.Serializable;

public class UappSettings implements Serializable {

    private static final long serialVersionUID = -8187303062569754675L;
    protected  Context mContext;

    /**
     * Constructor for Uappsettings
     * @param applicationContext For passing application Context
     * @since 1.0.0
     */

   public UappSettings(Context applicationContext){
       this.mContext=applicationContext;
   }

    /**
     * For retrieving application context
     * @return application context
     * @since 1.0.0
     */

    public Context getContext(){
        return mContext;
    }
}
