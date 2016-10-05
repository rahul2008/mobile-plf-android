package com.philips.platform.appframework.introscreen;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface WelcomeView {

    void showActionBar();

    void hideActionBar();

    void loadWelcomeFragment();

    void finishActivityAffinity();
}
