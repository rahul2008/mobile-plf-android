/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappframework.listener;

import android.support.annotation.IdRes;

/**
 * Micro app framework defines the below interface which needs to be implemented by base app to inject as a parameter of launcher API.
 */
public interface ActionBarListener {
    /**
     * For setting the title of action bar and to set back key Enabled/Disabled
     */
    void updateActionBar(@IdRes int resId, boolean enableBackKey);
}
