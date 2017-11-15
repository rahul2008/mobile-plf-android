/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.mock;

import android.content.Context;

import com.philips.platform.mya.launcher.MyaLaunchInput;


public class LaunchInputMock extends MyaLaunchInput {

    public ContextMock context;

    public LaunchInputMock() {
        context = new ContextMock();
    }

    @Override
    public Context getContext() {
        return context;
    }
}
