package com.philips.cdpp.dicommtestapp.background;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager;
import com.philips.cdp2.commlib.core.context.TransportContext;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdpp.dicommtestapp.DiCommTestApp;
import com.philips.cdpp.dicommtestapp.appliance.GenericAppliance;
import com.philips.cdpp.dicommtestapp.appliance.GenericApplianceFactory;
import com.philips.cdpp.dicommtestapp.strategy.CommStrategy;

import java.util.Timer;
import java.util.TimerTask;

public class BackgroundConnectionService extends Service
{
    private static final String TAG = "BackgroundConnectionSer";
    private CommCentral mCommCentral;
    private CommStrategy mCommStrategy;

    public class BackgroundBinder extends Binder {

        public BackgroundConnectionService getService(){
            return BackgroundConnectionService.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: someone bound");
        return mBinder;
    }

    private final IBinder mBinder = new BackgroundBinder();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: Someone unbound ");
        return super.onUnbind(intent);
    }

    public CommCentral createCommCentral(CommStrategy strategy)
    {
        if(mCommCentral != null) {
            // Remove existing one
            mCommCentral.stopDiscovery();
            mCommCentral = null;
        }

        // Create new factory
        this.mCommStrategy = strategy;
        DiCommTestApp app = getApp();
        GenericApplianceFactory factory = app.getApplianceFactory();
        factory.setTransportContext(mCommStrategy.getTransportContext());

        mCommCentral = new CommCentral(factory, mCommStrategy.getTransportContext());
        return mCommCentral;
    }

    public CommCentral getCommCentral() {
        return mCommCentral;
    }

    private DiCommTestApp getApp() {
        return (DiCommTestApp) getApplication();
    }

    public CommStrategy getCommStrategy() {
        return mCommStrategy;
    }

//                    if (result == SHNResult.SHNOk) {
//                public void onActionCompleted(int value, SHNResult result) {
//                @Override
//            battery.getBatteryLevel(new SHNIntegerResultListener() {
//        } else {
//            SHNLogger.d(TAG, "Battery capability is not available");
//            //
//        if(battery == null) {
//        SHNCapabilityBattery battery = (SHNCapabilityBattery) shnDevice.getCapabilityForType(SHNCapabilityType.BATTERY);
//    private void setupDeviceCapabilities(final SHNDevice shnDevice) {
//                        SHNLogger.d(TAG, "onActionCompleted: Battery level at " + value + "%");
//                    }
//                }
//            });
//        }
//
//        // Firmware capability
//        final SHNCapabilityFirmwareUpdate firmwareUpdate = (SHNCapabilityFirmwareUpdate) shnDevice.getCapabilityForType(SHNCapabilityType.FIRMWARE_UPDATE);
//        boolean hasFirmwareCap = firmwareUpdate != null;
//        if(!hasFirmwareCap) {
//            SHNLogger.w(TAG, "Firmware Update capability is not available");
//        } else {
//            //
//            SHNLogger.i(TAG, "FirmwareUpdate capability found.");
//            String firmwareState = firmwareUpdate.getState().name();
//            firmwareState = firmwareState.replace("SHNFirmwareUpdateState", "");
//            SHNLogger.d(TAG, "FirmwareUpdate state = " +firmwareState);
//        }
//
//        // Device Information capability
//        SHNCapabilityDeviceInformation di = (SHNCapabilityDeviceInformation) shnDevice.getCapabilityForType(SHNCapabilityType.DEVICE_INFORMATION);
//        boolean hasDeviceCap = di != null;
//        if (!hasDeviceCap) {
//            SHNLogger.w(TAG, "Device Information capability not available.");
//        } else {
//            //
//            di.readDeviceInformation(SHNCapabilityDeviceInformation.SHNDeviceInformationType.SoftwareRevision, new SHNCapabilityDeviceInformation.Listener() {
//                @Override
//                public void onDeviceInformation(@NonNull SHNCapabilityDeviceInformation.SHNDeviceInformationType deviceInformationType, @NonNull String value, @NonNull Date dateWhenAcquired) {
//                    String msg = String.format("onDeviceInformation: %s = %s", deviceInformationType.name(), value);
//                    SHNLogger.d(TAG, msg);
//                    shnDevice.disconnect();
//                }
//
//                @Override
//                public void onError(@NonNull SHNCapabilityDeviceInformation.SHNDeviceInformationType deviceInformationType, @NonNull SHNResult error) {
//                    SHNLogger.d(TAG, "onError: " + error.name());
//                }
//            });
//        }
//    }
}