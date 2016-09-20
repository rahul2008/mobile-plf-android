/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.appframework.introscreen.WelcomeActivity;
import com.philips.platform.modularui.statecontroller.UIState;

public class WelcomeState extends UIState {
    /**
     * constructor
     * @param stateID
     */
    public WelcomeState(@UIStateDef int stateID) {
        super(stateID);
    }

    /**
     * to navigate
     * @param context requires context
     */
    @Override
    public void navigate(Context context) {
        context.startActivity(new Intent(context, WelcomeActivity.class));
    }

    /**
     * handles back pressed
     * @param context requires context
     */
    @Override
    public void back(final Context context) {
    }

    @Override
    public void init(Context context) {

    }
}
