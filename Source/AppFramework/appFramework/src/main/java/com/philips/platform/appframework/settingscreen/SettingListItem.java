/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.settingscreen;

import android.text.Spanned;

/**
 * SettingListItem will describe state of the list itmes. On that basis we can enable disable items
 * from the list view.
 *
 * @author: ritesh.jha@philips.com
 * @since: June 22, 2016
 */
public class SettingListItem {
    Spanned title = null;
    SettingScreenItemType type = null;
    boolean userRegistrationRequired = false;
}
