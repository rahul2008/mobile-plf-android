/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.flowmanager.base;

import android.content.Context;

import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * This class is the base class for all the state objects. Any new state that is created should extend from this class and implement the abstract methods.
 * @since 1.1.0
 */
abstract public class BaseState {

    private String stateID;
    private UIStateListener uiStateListener;
    private UIStateData uiStateData;

    /**
     * AppFlowState constructor
     *
     * @param stateID pass the state Id
     * @since 1.1.0
     *
     */
    public BaseState(String stateID) {
        this.stateID = stateID;
    }

    /**
     * Getter for state Id. These API is used to get state ID from the state Objects.
     *
     * @return stateID
     * @since 1.1.0
     */
    public String getStateID() {
        return stateID;
    }

    /**
     * Setter for state ID.
     *
     * @param stateID requirs the state ID
     * @since 1.1.0
     */

    public void setStateID(String stateID) {
        this.stateID = stateID;
    }

    /**
     * For navigating from one state to other.
     *
     * @param uiLauncher requires the UiLauncher object
     * @since 1.1.0
     */
    public abstract void navigate(UiLauncher uiLauncher);

    /**
     * For initialising the component. This API is used to initialize any state. For example, initialize any Micro app or common component that needs to be used as a state.
     * @param context the context of application
     * @since 1.1.0
     */

    public abstract void init(Context context);

    /**
     * To get the presenter object.
     *
     * @return UIStateListener is returned
     * @since 1.1.0
     */
    public UIStateListener getPresenter() {
        return uiStateListener;
    }

    /**
     * To set the presenter.
     *
     * @param uiBasePresenter UI state listener
     * @since 1.1.0
     */

    public void setStateListener(UIStateListener uiBasePresenter) {
        this.uiStateListener = uiBasePresenter;
    }

    /**
     * Get the UI state data.
     * @return returns the UI state data
     */
    public UIStateData getUiStateData() {
        return uiStateData;
    }

    /**
     * Set the UI state data.
     * @param uiStateData UI state data
     */
    public void setUiStateData(UIStateData uiStateData) {
        this.uiStateData = uiStateData;
    }

    /**
     * This API is used to pass data to a state. This will be used for example to pass the CTN to consumer care Micro app.
     * @since 1.1.0
     */
    public abstract void updateDataModel();

    // TODO: Deepthi - need to document on providing StateId to validate duplicate object
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseState) {
            BaseState baseState = (BaseState) obj;
            return baseState.stateID.equals(stateID);
        }
        return super.equals(obj);
    }

    /*public static class UIStateData {

        private int fragmentAddState;

        public int getFragmentLaunchState() {
            return fragmentAddState;
        }

        public void setFragmentLaunchType(int fragmentAddState) {
            this.fragmentAddState = fragmentAddState;
        }

    }*/


}
