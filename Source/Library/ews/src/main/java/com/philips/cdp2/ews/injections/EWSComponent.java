/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.injections;

import com.philips.cdp2.ews.homewificonnection.ConnectingDeviceWithWifiFragment;
import com.philips.cdp2.ews.homewificonnection.ConnectingDeviceWithWifiViewModel;
import com.philips.cdp2.ews.hotspotconnection.ConnectingWithDeviceViewModel;
import com.philips.cdp2.ews.microapp.EWSInterface;
import com.philips.cdp2.ews.settingdeviceinfo.ConnectWithPasswordViewModel;
import com.philips.cdp2.ews.troubleshooting.connecttowrongphone.ConnectToWrongPhoneTroubleshootingViewModel;
import com.philips.cdp2.ews.troubleshooting.resetconnection.ResetConnectionTroubleshootingViewModel;
import com.philips.cdp2.ews.troubleshooting.resetdevice.ResetDeviceTroubleshootingViewModel;
import com.philips.cdp2.ews.troubleshooting.setupaccesspointmode.SetupAccessPointModeTroubleshootingViewModel;
import com.philips.cdp2.ews.troubleshooting.wificonnectionfailure.WIFIConnectionUnsuccessfulViewModel;
import com.philips.cdp2.ews.troubleshooting.wificonnectionfailure.WrongWifiNetworkViewModel;
import com.philips.cdp2.ews.view.EWSActivity;
import com.philips.cdp2.ews.view.dialog.TroubleshootDeviceAPModeFragment;
import com.philips.cdp2.ews.viewmodel.BaseTroubleShootingViewModel;
import com.philips.cdp2.ews.viewmodel.ConfirmWifiNetworkViewModel;
import com.philips.cdp2.ews.viewmodel.ConnectionSuccessfulViewModel;
import com.philips.cdp2.ews.viewmodel.FirstSetupStepsViewModel;
import com.philips.cdp2.ews.viewmodel.SecondSetupStepsViewModel;
import com.philips.cdp2.ews.viewmodel.StartConnectWithDeviceViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {EWSModule.class, EWSConfigurationModule.class})
public interface EWSComponent {

    void inject(EWSInterface ewsInterface);

    void inject(EWSActivity ewsActivity);

    void inject(TroubleshootDeviceAPModeFragment troubleshootDeviceAPModeFragment);

    void inject(ConnectingDeviceWithWifiFragment connectingDeviceWithWifiFragment);

    ResetConnectionTroubleshootingViewModel resetConnectionTroubleshootingViewModel();

    ResetDeviceTroubleshootingViewModel resetDeviceTroubleshootingViewModel();

    ConnectToWrongPhoneTroubleshootingViewModel connectToWrongPhoneTroubleshootingViewModel();

    SetupAccessPointModeTroubleshootingViewModel setupAccessPointModeTroubleshootingViewModel();

    BaseTroubleShootingViewModel baseTroubleShootingViewModel();

    ConnectingWithDeviceViewModel connectingWithDeviceViewModel();

    ConnectingDeviceWithWifiViewModel connectingDeviceWithWifiViewModel();

    WIFIConnectionUnsuccessfulViewModel wIFIConnectionUnsuccessfulViewModel();

    WrongWifiNetworkViewModel wrongWifiNetworkViewModel();

    StartConnectWithDeviceViewModel ewsGettingStartedViewModel();

    ConfirmWifiNetworkViewModel confirmWifiNetworkViewModel();

    FirstSetupStepsViewModel firstSetupStepsViewModel();

    SecondSetupStepsViewModel secondSetupStepsViewModel();

    ConnectWithPasswordViewModel connectWithPasswordViewModel();

    ConnectionSuccessfulViewModel connectionSuccessfulViewModel();
}