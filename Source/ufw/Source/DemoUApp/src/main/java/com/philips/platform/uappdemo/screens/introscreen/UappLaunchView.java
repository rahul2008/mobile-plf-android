/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappdemo.screens.introscreen;


import com.philips.platform.uappdemo.screens.base.UappFragmentView;

public interface UappLaunchView extends UappFragmentView {

    void showActionBar();

    void hideActionBar();

    void finishActivity();
}
