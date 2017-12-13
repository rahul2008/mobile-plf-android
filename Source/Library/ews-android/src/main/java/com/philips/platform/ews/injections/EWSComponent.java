/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.ews.injections;

import com.philips.platform.ews.base.BaseTroubleShootingViewModel;
import com.philips.platform.ews.confirmwifi.ConfirmWifiNetworkViewModel;
import com.philips.platform.ews.connectionsuccessful.ConnectionSuccessfulViewModel;
import com.philips.platform.ews.homewificonnection.ConnectingDeviceWithWifiViewModel;
import com.philips.platform.ews.hotspotconnection.ConnectingWithDeviceViewModel;
import com.philips.platform.ews.settingdeviceinfo.ConnectWithPasswordViewModel;
import com.philips.platform.ews.setupsteps.FirstSetupStepsViewModel;
import com.philips.platform.ews.setupsteps.SecondSetupStepsViewModel;
import com.philips.platform.ews.startconnectwithdevice.StartConnectWithDeviceViewModel;
import com.philips.platform.ews.troubleshooting.connecttowrongphone.ConnectToWrongPhoneTroubleshootingViewModel;
import com.philips.platform.ews.troubleshooting.resetconnection.ResetConnectionTroubleshootingViewModel;
import com.philips.platform.ews.troubleshooting.resetdevice.ResetDeviceTroubleshootingViewModel;
import com.philips.platform.ews.troubleshooting.setupaccesspointmode.SetupAccessPointModeTroubleshootingViewModel;
import com.philips.platform.ews.troubleshooting.wificonnectionfailure.WIFIConnectionUnsuccessfulViewModel;
import com.philips.platform.ews.troubleshooting.wificonnectionfailure.WrongWifiNetworkViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {EWSModule.class, EWSConfigurationModule.class, EWSDependencyProviderModule.class})
public interface EWSComponent {
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