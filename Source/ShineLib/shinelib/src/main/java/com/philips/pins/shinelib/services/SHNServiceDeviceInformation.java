/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.services;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNCommandResultReporter;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.SHNStringResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;
import com.philips.pins.shinelib.framework.BleUUIDCreator;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SHNServiceDeviceInformation extends SHNService implements SHNService.SHNServiceListener {
    public static final String SERVICE_UUID = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x180A);
    public static final String SYSTEM_ID_CHARACTERISTIC_UUID = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A23);
    public static final String MODEL_NUMBER_CHARACTERISTIC_UUID = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A24);
    public static final String SERIAL_NUMBER_CHARACTERISTIC_UUID = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A25);
    public static final String FIRMWARE_REVISION_CHARACTERISTIC_UUID = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A26);
    public static final String HARDWARE_REVISION_CHARACTERISTIC_UUID = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A27);
    public static final String SOFTWARE_REVISION_CHARACTERISTIC_UUID = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A28);
    public static final String MANUFACTURER_NAME_CHARACTERISTIC_UUID = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A29);

    private static final String TAG = SHNServiceDeviceInformation.class.getSimpleName();
    private static final boolean LOGGING = false;

    private final Map<SHNCapabilityDeviceInformation.SHNDeviceInformationType, String> uuidMap = new HashMap<>();

    public SHNServiceDeviceInformation() {
        super(UUID.fromString(SERVICE_UUID), getRequiredCharacteristics(), getOptionalCharacteristics());

        uuidMap.put(SHNCapabilityDeviceInformation.SHNDeviceInformationType.FirmwareRevision, SHNServiceDeviceInformation.FIRMWARE_REVISION_CHARACTERISTIC_UUID);
        uuidMap.put(SHNCapabilityDeviceInformation.SHNDeviceInformationType.HardwareRevision, SHNServiceDeviceInformation.HARDWARE_REVISION_CHARACTERISTIC_UUID);
        uuidMap.put(SHNCapabilityDeviceInformation.SHNDeviceInformationType.ManufacturerName, SHNServiceDeviceInformation.MANUFACTURER_NAME_CHARACTERISTIC_UUID);
        uuidMap.put(SHNCapabilityDeviceInformation.SHNDeviceInformationType.ModelNumber, SHNServiceDeviceInformation.MODEL_NUMBER_CHARACTERISTIC_UUID);
        uuidMap.put(SHNCapabilityDeviceInformation.SHNDeviceInformationType.SerialNumber, SHNServiceDeviceInformation.SERIAL_NUMBER_CHARACTERISTIC_UUID);
        uuidMap.put(SHNCapabilityDeviceInformation.SHNDeviceInformationType.SoftwareRevision, SHNServiceDeviceInformation.SOFTWARE_REVISION_CHARACTERISTIC_UUID);
        uuidMap.put(SHNCapabilityDeviceInformation.SHNDeviceInformationType.SystemID, SHNServiceDeviceInformation.SYSTEM_ID_CHARACTERISTIC_UUID);

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

    @Deprecated
    public void readDeviceInformation(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType shnDeviceInformationType, @NonNull final SHNStringResultListener shnStringResultListener) {
        readDeviceInformation(shnDeviceInformationType, new SHNCapabilityDeviceInformation.Listener() {
            @Override
            public void onDeviceInformation(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType shnDeviceInformationType, @NonNull final String value, @NonNull final Date lastCacheUpdate) {
                shnStringResultListener.onActionCompleted(value, SHNResult.SHNOk);
            }

            @Override
            public void onError(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType shnDeviceInformationType, @NonNull final SHNResult error) {
                shnStringResultListener.onActionCompleted(null, error);
            }
        });
    }

    public void readDeviceInformation(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType informationType, @NonNull final SHNCapabilityDeviceInformation.Listener resultListener) {
        final SHNCharacteristic shnCharacteristic = getSHNCharacteristic(getCharacteristicUUIDForDeviceInformationType(informationType));
        if (shnCharacteristic == null) {
            resultListener.onError(informationType, SHNResult.SHNErrorUnsupportedOperation);
        } else {
            SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
                @Override
                public void reportResult(SHNResult shnResult, byte[] data) {
                    if (shnResult == SHNResult.SHNOk) {
                        String value = new String(data, StandardCharsets.UTF_8);
                        resultListener.onDeviceInformation(informationType, value, new Date());
                    } else {
                        resultListener.onError(informationType, shnResult);
                    }
                }
            };
            shnCharacteristic.read(resultReporter);
        }
    }

    @Nullable
    private UUID getCharacteristicUUIDForDeviceInformationType(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType shnDeviceInformationType) {
        String uuidString = uuidMap.get(shnDeviceInformationType);

        if (uuidString != null) {
            return UUID.fromString(uuidString);
        }

        return null;
    }
}
