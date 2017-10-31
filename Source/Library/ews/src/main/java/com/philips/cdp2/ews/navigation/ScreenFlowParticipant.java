/**
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.navigation;

import android.support.annotation.StringRes;

public interface ScreenFlowParticipant {

    int getHierarchyLevel();

    boolean onBackPressed();

    @StringRes
    int getToolbarTitle();

}
