/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.modularui.statecontroller.UIState;

public class HomeActivityState extends UIState {
    /**
     * constructor
     * @param stateID
     */
    public HomeActivityState(@UIStateDef int stateID) {
        super(stateID);
    }

    /**
     * Navigate to HomeActivity
     * @param context requires context
     */
    @Override
    public void navigate(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
    }

    /**
     * to handle back
     * @param context requires context
     */
    @Override
    public void back(final Context context) {
    }
}
