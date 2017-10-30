/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.injections;

import com.philips.cdp2.ews.homewificonnection.ConnectingDeviceWithWifiFragment;
import com.philips.cdp2.ews.homewificonnection.ConnectingDeviceWithWifiViewModel;
import com.philips.cdp2.ews.hotspotconnection.ConnectingPhoneToHotspotWifiViewModel;
import com.philips.cdp2.ews.settingdeviceinfo.ConnectWithPasswordFragment;
import com.philips.cdp2.ews.troubleshooting.connecttowrongphone.ConnectToWrongPhoneTroubleshootingViewModel;
import com.philips.cdp2.ews.troubleshooting.resetconnection.ResetConnectionTroubleshootingViewModel;
import com.philips.cdp2.ews.troubleshooting.resetdevice.ResetDeviceTroubleshootingViewModel;
import com.philips.cdp2.ews.troubleshooting.setupaccesspointmode.SetupAccessPointModeTroubleshootingViewModel;
import com.philips.cdp2.ews.troubleshooting.wificonnectionfailure.WIFIConnectionUnsuccessfulViewModel;
import com.philips.cdp2.ews.troubleshooting.wificonnectionfailure.WrongWifiNetworkViewModel;
import com.philips.cdp2.ews.view.BlinkingAccessPointFragment;
import com.philips.cdp2.ews.view.ChooseSetupStateFragment;
import com.philips.cdp2.ews.view.EWSActivity;
import com.philips.cdp2.ews.view.EWSDevicePowerOnFragment;
import com.philips.cdp2.ews.view.EWSGettingStartedFragment;
import com.philips.cdp2.ews.view.EWSHomeWifiDisplayFragment;
import com.philips.cdp2.ews.view.EWSPressPlayAndFollowSetupFragment;
import com.philips.cdp2.ews.view.EWSProductSupportFragment;
import com.philips.cdp2.ews.view.EWSResetDeviceFragment;
import com.philips.cdp2.ews.view.EWSWiFiPairedFragment;
import com.philips.cdp2.ews.view.TroubleshootCheckRouterSettingsFragment;
import com.philips.cdp2.ews.view.TroubleshootConnectionUnsuccessfulFragment;
import com.philips.cdp2.ews.view.TroubleshootIncorrectPasswordFragment;
import com.philips.cdp2.ews.view.TroubleshootWrongWiFiFragment;
import com.philips.cdp2.ews.view.dialog.TroubleshootDeviceAPModeFragment;
import com.philips.cdp2.ews.viewmodel.BaseTroubleShootingViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {EWSModule.class, EWSConfigurationModule.class})
public interface EWSComponent {

    void inject(EWSActivity ewsActivity);

    void inject(EWSGettingStartedFragment ewsGettingStartedFragment);

    void inject(EWSHomeWifiDisplayFragment ewsHomeWifiDisplayFragment);

    void inject(EWSDevicePowerOnFragment ewsDevicePowerOnFragment);

    void inject(EWSPressPlayAndFollowSetupFragment ewsPressPlayAndFollowSetupFragment);

    void inject(ConnectWithPasswordFragment connectWithPasswordFragment);

    void inject(EWSWiFiPairedFragment ewsWiFiPairedFragment);

    void inject(TroubleshootDeviceAPModeFragment troubleshootDeviceAPModeFragment);

    void inject(TroubleshootIncorrectPasswordFragment troubleshootIncorrectPasswordFragment);

    void inject(TroubleshootCheckRouterSettingsFragment troubleshootCheckRouterSettingsFragment);

    void inject(EWSProductSupportFragment ewsProductSupportFragment);

    void inject(TroubleshootWrongWiFiFragment troubleshootWrongWiFiFragment);

    void inject(TroubleshootConnectionUnsuccessfulFragment connectionUnsuccessfulFragment);

    void inject(EWSResetDeviceFragment ewsResetDeviceFragment);

    void inject(ChooseSetupStateFragment chooseSetupStateFragment);

    void inject(BlinkingAccessPointFragment blinkingAccessPointFragment);

    void inject(ConnectingDeviceWithWifiFragment connectingDeviceWithWifiFragment);

    ResetConnectionTroubleshootingViewModel resetConnectionTroubleshootingViewModel();

    ResetDeviceTroubleshootingViewModel resetDeviceTroubleshootingViewModel();

    ConnectToWrongPhoneTroubleshootingViewModel connectToWrongPhoneTroubleshootingViewModel();

    SetupAccessPointModeTroubleshootingViewModel setupAccessPointModeTroubleshootingViewModel();

    BaseTroubleShootingViewModel baseTroubleShootingViewModel();

    ConnectingPhoneToHotspotWifiViewModel connectingPhoneToHotspotWifiViewModel();

    ConnectingDeviceWithWifiViewModel connectingDeviceWithWifiViewModel();

    WIFIConnectionUnsuccessfulViewModel wIFIConnectionUnsuccessfulViewModel();

    WrongWifiNetworkViewModel wrongWifiNetworkViewModel();
}