/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */
package com.philips.cdp.registration.settings;


import android.content.Context;

import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;

public class JanrainInitializer implements JumpFlowDownloadStatusListener {

    public interface JanrainInitializeListener {
        void onJanrainInitializeSuccess();

        void onJanrainInitializeFailed();

        boolean isJanrainInitializeRequired();
    }

    private JanrainInitializeListener mJanrainInitializeListener;

    @Override
    public void onFlowDownloadSuccess() {
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }

    @Override
    public void onFlowDownloadFailure() {
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }

    public void initializeJanrain(final Context context,
                                  final JanrainInitializeListener
                                          janrainInitializeListener) {
        mJanrainInitializeListener = janrainInitializeListener;
        if (!UserRegistrationInitializer.getInstance().isJumpInitializated()
                && !UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            UserRegistrationInitializer.getInstance().
                    registerJumpFlowDownloadListener(JanrainInitializer.this);
            RegistrationHelper.getInstance().initializeUserRegistration(context);
            return;
        }
        if (!UserRegistrationInitializer.getInstance().isJumpInitializated()
                && UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(
                    JanrainInitializer.this);
        }
    }


    public boolean isJanrainInitializeRequired() {
        return !(UserRegistrationInitializer.getInstance().isJumpInitializated());
    }
}
