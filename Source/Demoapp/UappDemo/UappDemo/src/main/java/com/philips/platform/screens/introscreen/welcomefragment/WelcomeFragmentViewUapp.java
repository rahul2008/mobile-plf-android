/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.screens.introscreen.welcomefragment;


import com.philips.platform.screens.base.FragmentViewUapp;

public interface WelcomeFragmentViewUapp extends FragmentViewUapp {

    void showActionBar();

    void hideActionBar();

    void finishActivity();
}
