/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.statecontroller;

public class UIStateData {

    private int fragmentAddState;

    public int getFragmentLaunchState() {
        return fragmentAddState;
    }

    public void setFragmentLaunchType(int fragmentAddState) {
        this.fragmentAddState = fragmentAddState;
    }

}
