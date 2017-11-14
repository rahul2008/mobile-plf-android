/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.launcher;

import android.content.Context;

import com.philips.platform.mya.interfaces.MyaListener;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;


public class MyaLaunchInput extends UappLaunchInput {

    private Context context;
    private MyaListener myaListener;

    public MyaLaunchInput(Context context, MyaListener myaListener) {
        this.context = context;
        this.myaListener = myaListener;
    }

    public Context getContext() {
        return context;
    }

    public MyaListener getMyaListener() {
        return myaListener;
    }

}
