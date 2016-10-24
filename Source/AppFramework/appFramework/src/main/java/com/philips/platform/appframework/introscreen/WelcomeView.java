package com.philips.platform.appframework.introscreen;

import com.philips.platform.modularui.statecontroller.FragmentView;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface WelcomeView extends FragmentView {

    void showActionBar();

    void hideActionBar();

    void finishActivityAffinity();
}
