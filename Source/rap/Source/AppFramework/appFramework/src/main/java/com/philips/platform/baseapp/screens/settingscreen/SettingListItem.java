/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.settingscreen;

import android.text.Spanned;

/**
 * Used for setting individual items in Settings list
 */

public class SettingListItem {
    public Spanned title = null;
    public SettingListItemType type = null;
    public boolean userRegistrationRequired = false;
}
