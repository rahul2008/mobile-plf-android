/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappframework.listener;

import androidx.annotation.StringRes;

import java.io.Serializable;


/**
 * Micro app framework defines the below interface which needs to be implemented by base app to inject as a parameter of launcher API.
 * @since 1.0.0
 */
public interface ActionBarListener extends Serializable {
    /**
     * For setting the title of action bar and to set back key Enabled/Disabled
     * @param resId The resource Id of the String to be displayed
     * @param enableBackKey To set back key enabled or disabled
     * @since 1.0.0
     */
    void updateActionBar(@StringRes int resId, boolean enableBackKey);

    /**
     * For setting the title of action bar and to set back key Enabled/Disabled
     * @param resString The String to be displayed
     * @param enableBackKey To set back key enabled or disabled
     * @since 1.0.0
     */
    void updateActionBar(String resString, boolean enableBackKey);
}
