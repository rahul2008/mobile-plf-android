/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.statecontroller;

import android.content.Context;

/**
 * This class aims to handle events inside the states and also events when a particular state is loaded
 */
abstract public class UIBasePresenter {
    /**
     * The onclick of objects in a particular state can be defined here
     * @param componentID The Id of any button or widget or any other component
     * @param context the current context
     */
    public abstract void onClick(int componentID, Context context);

    /**
     * This method needs to be implemented to do oeprations when te particular state loads
     * @param context requires context
     */
    public abstract void onLoad(Context context);

    /**
     * For seeting the current state , so that flow manager is updated with current state
     * @param stateID requires State ID
     */
    public void setState(int stateID){

    }
}
