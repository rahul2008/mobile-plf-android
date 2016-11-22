package com.philips.platform.appframework.introscreen.welcomefragment;

import com.philips.platform.modularui.statecontroller.FragmentView;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface WelcomeFragmentView extends FragmentView {

    void showActionBar();

    void hideActionBar();

    void finishActivityAffinity();
}
