package com.pins.philips.shinelib.services;

import android.util.Log;

import com.pins.philips.shinelib.SHNCharacteristic;
import com.pins.philips.shinelib.SHNCommandResultReporter;
import com.pins.philips.shinelib.SHNResult;
import com.pins.philips.shinelib.SHNService;
import com.pins.philips.shinelib.SHNStringResultListener;
import com.pins.philips.shinelib.capabilities.SHNCapabilityDeviceInformation;
import com.pins.philips.shinelib.framework.BleUUIDCreator;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by 310188215 on 31/03/15.
 */
public class SHNServiceDeviceInformation extends SHNService implements SHNService.SHNServiceListener {
    public static final String SERVICE_UUID = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x180A);
    public static final String SYSTEM_ID_CHARACTERISTIC_UUID          = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A23);
    public static final String MODEL_NUMBER_CHARACTERISTIC_UUID       = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A24);
    public static final String SERIAL_NUMBER_CHARACTERISTIC_UUID      = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A25);
    public static final String FIRMWARE_REVISION_CHARACTERISTIC_UUID  = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A26);
    public static final String HARDWARE_REVISION_CHARACTERISTIC_UUID  = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A27);
    public static final String SOFTWARE_REVISION_CHARACTERISTIC_UUID  = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A28);
    public static final String MANUFACTURER_NAME_CHARACTERISTIC_UUID  = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A29);

    private static final String TAG = SHNServiceDeviceInformation.class.getSimpleName();
    private static final boolean LOGGING = false;

    public SHNServiceDeviceInformation() {
        super(UUID.fromString(SERVICE_UUID), getRequiredCharacteristics(), getOptionalCharacteristics());
        registerSHNServiceListener(this);
    }

    private static Set<UUID> getRequiredCharacteristics() {
        return new HashSet<>();
    }

    private static Set<UUID> getOptionalCharacteristics() {
        Set<UUID> optionalCharacteristicUUIDs = new HashSet<>();
        optionalCharacteristicUUIDs.add(UUID.fromString(SHNServiceDeviceInformation.FIRMWARE_REVISION_CHARACTERISTIC_UUID));
        optionalCharacteristicUUIDs.add(UUID.fromString(SHNServiceDeviceInformation.HARDWARE_REVISION_CHARACTERISTIC_UUID));
        optionalCharacteristicUUIDs.add(UUID.fromString(SHNServiceDeviceInformation.MANUFACTURER_NAME_CHARACTERISTIC_UUID));
        optionalCharacteristicUUIDs.add(UUID.fromString(SHNServiceDeviceInformation.MODEL_NUMBER_CHARACTERISTIC_UUID));
        optionalCharacteristicUUIDs.add(UUID.fromString(SHNServiceDeviceInformation.SERIAL_NUMBER_CHARACTERISTIC_UUID));
        optionalCharacteristicUUIDs.add(UUID.fromString(SHNServiceDeviceInformation.SOFTWARE_REVISION_CHARACTERISTIC_UUID));
        optionalCharacteristicUUIDs.add(UUID.fromString(SHNServiceDeviceInformation.SYSTEM_ID_CHARACTERISTIC_UUID));
        return optionalCharacteristicUUIDs;
    }

    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        if (state == SHNService.State.Available) {
            shnService.transitionToReady();
        }
    }

    public void readDeviceInformation(final SHNCapabilityDeviceInformation.SHNDeviceInformationType shnDeviceInformationType, final SHNStringResultListener shnStringResultListener) {
        if (LOGGING) Log.i(TAG, "readDeviceInformation");
        final SHNCharacteristic shnCharacteristic = getSHNCharacteristic(getCharacteristicUUIDForDeviceInformationType(shnDeviceInformationType));
        SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (LOGGING) Log.i(TAG, "readDeviceInformation reportResult");
                String value = null;
                if (shnResult == SHNResult.SHNOk) {
                    value = new String(data, StandardCharsets.UTF_8);
                }
                if (shnStringResultListener != null) {
                    shnStringResultListener.onActionCompleted(value, shnResult);
                }
            }
        };

        shnCharacteristic.read(resultReporter);
    }

    private UUID getCharacteristicUUIDForDeviceInformationType(SHNCapabilityDeviceInformation.SHNDeviceInformationType shnDeviceInformationType) {
        String uuidString = null;
        switch(shnDeviceInformationType) {
            case FirmwareRevision:
                uuidString = SHNServiceDeviceInformation.FIRMWARE_REVISION_CHARACTERISTIC_UUID;
                break;
            case HardwareRevision:
                uuidString = SHNServiceDeviceInformation.HARDWARE_REVISION_CHARACTERISTIC_UUID;
                break;
            case ManufacturerName:
                uuidString = SHNServiceDeviceInformation.MANUFACTURER_NAME_CHARACTERISTIC_UUID;
                break;
            case ModelNumber:
                uuidString = SHNServiceDeviceInformation.MODEL_NUMBER_CHARACTERISTIC_UUID;
                break;
            case SerialNumber:
                uuidString = SHNServiceDeviceInformation.SERIAL_NUMBER_CHARACTERISTIC_UUID;
                break;
            case SoftwareRevision:
                uuidString = SHNServiceDeviceInformation.SOFTWARE_REVISION_CHARACTERISTIC_UUID;
                break;
            case SystemID:
                uuidString = SHNServiceDeviceInformation.SYSTEM_ID_CHARACTERISTIC_UUID;
                break;
        }
        if (uuidString != null) {
            return UUID.fromString(uuidString);
        }
        return null;
    }
}
