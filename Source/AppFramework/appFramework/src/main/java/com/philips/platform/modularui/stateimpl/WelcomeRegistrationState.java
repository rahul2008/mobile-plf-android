/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.introscreen.WelcomeActivity;
import com.philips.platform.modularui.statecontroller.UIState;

public class WelcomeRegistrationState extends UIState {
    /**
     * constructor
     * @param stateID
     */
    public WelcomeRegistrationState(@UIStateDef int stateID) {
        super(stateID);
    }

    /**
     * to navigate
     * @param context requires context
     */
    @Override
    protected void navigate(Context context) {
        context.startActivity(new Intent(context, WelcomeActivity.class));
    }

    /**
     * to handle back events
     * @param context requires context
     */
    @Override
    public void back(final Context context) {
        ((AppFrameworkBaseActivity)context).popBackTillHomeFragment();
    }
}
