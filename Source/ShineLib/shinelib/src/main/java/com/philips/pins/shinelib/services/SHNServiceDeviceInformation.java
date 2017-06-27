/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2017.
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
import com.philips.pins.shinelib.datatypes.SHNCharacteristicInfo;
import com.philips.pins.shinelib.framework.BleUUIDCreator;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @publicPluginApi
 */
public class SHNServiceDeviceInformation extends SHNService {
    public static final String SERVICE_UUID = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x180A);
    public static final String SYSTEM_ID_CHARACTERISTIC_UUID = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A23);
    public static final String MODEL_NUMBER_CHARACTERISTIC_UUID = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A24);
    public static final String SERIAL_NUMBER_CHARACTERISTIC_UUID = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A25);
    public static final String FIRMWARE_REVISION_CHARACTERISTIC_UUID = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A26);
    public static final String HARDWARE_REVISION_CHARACTERISTIC_UUID = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A27);
    public static final String SOFTWARE_REVISION_CHARACTERISTIC_UUID = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A28);
    public static final String MANUFACTURER_NAME_CHARACTERISTIC_UUID = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A29);

    @NonNull
    private final Map<SHNCapabilityDeviceInformation.SHNDeviceInformationType, String> uuidMap = new HashMap<>();

    public SHNServiceDeviceInformation() {
        super(UUID.fromString(SERVICE_UUID), getRequiredCharacteristics(), getOptionalCharacteristics());

        uuidMap.put(SHNCapabilityDeviceInformation.SHNDeviceInformationType.FirmwareRevision, FIRMWARE_REVISION_CHARACTERISTIC_UUID);
        uuidMap.put(SHNCapabilityDeviceInformation.SHNDeviceInformationType.HardwareRevision, HARDWARE_REVISION_CHARACTERISTIC_UUID);
        uuidMap.put(SHNCapabilityDeviceInformation.SHNDeviceInformationType.ManufacturerName, MANUFACTURER_NAME_CHARACTERISTIC_UUID);
        uuidMap.put(SHNCapabilityDeviceInformation.SHNDeviceInformationType.ModelNumber, MODEL_NUMBER_CHARACTERISTIC_UUID);
        uuidMap.put(SHNCapabilityDeviceInformation.SHNDeviceInformationType.SerialNumber, SERIAL_NUMBER_CHARACTERISTIC_UUID);
        uuidMap.put(SHNCapabilityDeviceInformation.SHNDeviceInformationType.SoftwareRevision, SOFTWARE_REVISION_CHARACTERISTIC_UUID);
        uuidMap.put(SHNCapabilityDeviceInformation.SHNDeviceInformationType.SystemID, SYSTEM_ID_CHARACTERISTIC_UUID);

        registerSHNServiceListener(new SHNServiceListener() {
            @Override
            public void onServiceStateChanged(final SHNService shnService, final State state) {
                if (state == SHNService.State.Available) {
                    shnService.transitionToReady();
                }
            }
        });
    }

    @NonNull
    private static Set<SHNCharacteristicInfo> getRequiredCharacteristics() {
        return new HashSet<>();
    }

    @NonNull
    private static Set<SHNCharacteristicInfo> getOptionalCharacteristics() {
        Set<SHNCharacteristicInfo> set = new HashSet<>();
        set.add(new SHNCharacteristicInfo(UUID.fromString(FIRMWARE_REVISION_CHARACTERISTIC_UUID), false));
        set.add(new SHNCharacteristicInfo(UUID.fromString(HARDWARE_REVISION_CHARACTERISTIC_UUID), false));
        set.add(new SHNCharacteristicInfo(UUID.fromString(MANUFACTURER_NAME_CHARACTERISTIC_UUID), false));
        set.add(new SHNCharacteristicInfo(UUID.fromString(MODEL_NUMBER_CHARACTERISTIC_UUID), false));
        set.add(new SHNCharacteristicInfo(UUID.fromString(SERIAL_NUMBER_CHARACTERISTIC_UUID), false));
        set.add(new SHNCharacteristicInfo(UUID.fromString(SOFTWARE_REVISION_CHARACTERISTIC_UUID), false));
        set.add(new SHNCharacteristicInfo(UUID.fromString(SYSTEM_ID_CHARACTERISTIC_UUID), false));
        return set;
    }

    @Deprecated
    public void readDeviceInformation(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType shnDeviceInformationType, @NonNull final SHNStringResultListener shnStringResultListener) {
        readDeviceInformation(shnDeviceInformationType, new SHNCapabilityDeviceInformation.Listener() {
            @Override
            public void onDeviceInformation(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType shnDeviceInformationType, @NonNull final String value, @NonNull final Date dateWhenAcquired) {
                shnStringResultListener.onActionCompleted(value, SHNResult.SHNOk);
            }

            @Override
            public void onError(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType shnDeviceInformationType, @NonNull final SHNResult error) {
                shnStringResultListener.onActionCompleted(null, error);
            }
        });
    }

    public void readDeviceInformation(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType informationType, @NonNull final SHNCapabilityDeviceInformation.Listener resultListener) {
        UUID uuid = getCharacteristicUUIDForDeviceInformationType(informationType);
        final SHNCharacteristic shnCharacteristic = getSHNCharacteristic(uuid);
        if (shnCharacteristic == null) {
            resultListener.onError(informationType, SHNResult.SHNErrorUnsupportedOperation);
        } else {
            SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
                @Override
                public void reportResult(@NonNull SHNResult shnResult, byte[] data) {
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

        return uuidString != null ? UUID.fromString(uuidString) : null;
    }
}
