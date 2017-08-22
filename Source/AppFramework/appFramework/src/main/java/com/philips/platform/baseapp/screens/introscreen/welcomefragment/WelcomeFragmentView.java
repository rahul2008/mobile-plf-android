/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.introscreen.welcomefragment;

import com.philips.platform.baseapp.base.FragmentView;

public interface WelcomeFragmentView extends FragmentView {

    void showActionBar();

    void hideActionBar();

    void finishActivityAffinity();

    void clearAdapter();
}
