/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.debugtest;

import android.content.Context;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;

public class DebugTestFragmentPresenter extends UIBasePresenter {

    DebugTestFragmentPresenter(){
        setState(UIState.UI_DEBUG_FRAGMENT_STATE);
    }
    @Override
    public void onClick(int componentID, Context context) {

    }

    @Override
    public void onLoad(Context context) {

    }
}
