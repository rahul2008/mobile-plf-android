/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.cocointerface;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.productselection.listeners.ActionbarUpdateListener;

public interface UICoCoInterface {
    void loadPlugIn(Context context);

    void runCoCo(Context context);

    void unloadCoCo();

    void setActionbar(ActionbarUpdateListener actionBarClickListener);
    void setFragActivity(FragmentActivity fa);
}
