package com.philips.cdp.appframework.settingscreen;

import android.text.Spanned;

/**
 * SettingScreenItem will describe state of the list itmes. On that basis we can enable disable items
 * from the list view.
 *
 * @author: ritesh.jha@philips.com
 * @since: June 22, 2016
 */
public class SettingScreenItem {
    Spanned title = null;
    SettingScreenItemType type = null;
    boolean userRegistrationRequired = false;
}
