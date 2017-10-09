/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.viewmodel;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.permission.PermissionHandler;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;
import javax.inject.Named;

public class EWSPressPlayAndFollowSetupViewModel extends ConnectPhoneToDeviceAPModeViewModel {


    @Inject
    public EWSPressPlayAndFollowSetupViewModel(@NonNull final Navigator navigator,
                                               @NonNull @Named("ews.event.bus") final EventBus eventBus,
                                               @NonNull final PermissionHandler permissionHandler,
                                               @NonNull final DialogFragment connectingDialog,
                                               @NonNull final DialogFragment unsuccesfulDialog,
                                               @NonNull final DialogFragment gpsSettingsDialog, @NonNull final Handler handler) {
        super(navigator, eventBus, permissionHandler, connectingDialog, unsuccesfulDialog, gpsSettingsDialog, handler);
    }

    public void onNextButtonClicked() {
        connectPhoneToDeviceHotspotWifi();
    }
}

