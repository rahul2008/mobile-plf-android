/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.statecontroller;

import android.content.Context;

abstract public class UIBasePresenter {
    public abstract void onClick(int componentID, Context context);
    public abstract void onLoad(Context context);
    public void setState(int stateID){

    }
}
