/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.flowmanager.base;

import android.content.Context;

import com.philips.platform.baseapp.base.UIBasePresenter;
import com.philips.platform.baseapp.base.UIStateData;
import com.philips.platform.uappframework.launcher.UiLauncher;

abstract public class BaseState {

    private String stateID;
    private UIBasePresenter uiBasePresenter;
    private UIStateData uiStateData;

    /**
     * AppFlowState constructor
     *
     * @param stateID pass the state Id
     */
    public BaseState(String stateID) {
        this.stateID = stateID;
    }

    /**
     * getter for state Id
     *
     * @return stateID
     */
    public String getStateID() {
        return stateID;
    }

    /**
     * setter for state ID
     *
     * @param stateID requirs the state ID
     */

    public void setStateID(String stateID) {
        this.stateID = stateID;
    }

    /**
     * For navigating from one state to other
     *
     * @param uiLauncher requires the UiLauncher object
     */
    public abstract void navigate(UiLauncher uiLauncher);

    /**
     * For initialising the component
     *
     * @param context
     */

    public abstract void init(Context context);

    /**
     * to get the presenter object
     *
     * @return
     */
    public UIBasePresenter getPresenter() {
        return uiBasePresenter;
    }

    /**
     * to set the presenter
     *
     * @param uiBasePresenter
     */

    public void setStateListener(UIBasePresenter uiBasePresenter) {
        this.uiBasePresenter = uiBasePresenter;
    }

    public UIStateData getUiStateData() {
        return uiStateData;
    }

    public void setUiStateData(UIStateData uiStateData) {
        this.uiStateData = uiStateData;
    }

    public abstract void updateDataModel();

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseState) {
            BaseState baseState = (BaseState) obj;
            return baseState.stateID.equals(stateID);
        }
        return super.equals(obj);
    }
}
