/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappdemo.screens.base;

import com.philips.platform.appframework.flowmanager.base.UIStateData;
import com.philips.platform.flowmanager.UappStates;
import com.philips.platform.flowmanager.utility.UappConstants;

/**
 * This class aims to handle events inside the states and also events when a particular state is loaded
 */
abstract public class UappBasePresenter {
    /*Event ID */
    protected static final int MENU_OPTION_HOME = 0;
    protected final int MENU_OPTION_ABOUT = 1;

    /* event to state map */
    protected final String HOME_ABOUT = "about";
    protected final String HOME_FRAGMENT = "home_fragment";

    private UappUIView uappUiView;

    public UappBasePresenter(final UappUIView uappUiView) {
        this.uappUiView = uappUiView;
    }

    /**
     * The onclick of objects in a particular state can be defined here
     * @param componentID The Id of any button or widget or any other component
     *
     */
    public abstract void onEvent(int componentID);

    /**
     * For seeting the current state , so that flow manager is updated with current state
     * @param stateID requires AppFlowState ID
     */
    public void setState(String stateID){

    }

    protected UIStateData setStateData(final String componentID) {
        switch (componentID) {
            case UappStates.HOME_FRAGMENT:
                UIStateData homeStateData = new UIStateData();
                homeStateData.setFragmentLaunchType(UappConstants.ADD_HOME_FRAGMENT);
                return homeStateData;
            case UappStates.ABOUT:
                UIStateData aboutStateData = new UIStateData();
                aboutStateData.setFragmentLaunchType(UappConstants.ADD_FROM_HAMBURGER);
                return aboutStateData;
            default:
                homeStateData = new UIStateData();
                homeStateData.setFragmentLaunchType(UappConstants.ADD_HOME_FRAGMENT);
                return homeStateData;
        }
    }
}
