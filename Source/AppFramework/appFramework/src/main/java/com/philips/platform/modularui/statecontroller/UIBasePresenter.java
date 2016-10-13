/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.statecontroller;

/**
 * This class aims to handle events inside the states and also events when a particular state is loaded
 */
abstract public class UIBasePresenter {

    private UIView uiView;

    public UIBasePresenter(final UIView uiView) {
        this.uiView = uiView;
    }

    /**
     * The onclick of objects in a particular state can be defined here
     * @param componentID The Id of any button or widget or any other component
     *
     */
    public abstract void onClick(int componentID);

    /**
     * This method needs to be implemented to do oeprations when te particular state loads
     */
    public abstract void onLoad();

    /**
     * For seeting the current state , so that flow manager is updated with current state
     * @param stateID requires State ID
     */
    public void setState(int stateID){

    }
}
