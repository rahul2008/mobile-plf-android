/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.services;

import android.content.SharedPreferences;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_SUFFIX = "date";

    private final Map<SHNCapabilityDeviceInformation.SHNDeviceInformationType, String> uuidMap = new HashMap<>();
    private SharedPreferences sharedPreferences;

    public SHNServiceDeviceInformation(@NonNull final SharedPreferences sharedPreferences) {
        super(UUID.fromString(SERVICE_UUID), getRequiredCharacteristics(), getOptionalCharacteristics());
        this.sharedPreferences = sharedPreferences;

        uuidMap.put(SHNCapabilityDeviceInformation.SHNDeviceInformationType.FirmwareRevision, FIRMWARE_REVISION_CHARACTERISTIC_UUID);
        uuidMap.put(SHNCapabilityDeviceInformation.SHNDeviceInformationType.HardwareRevision, HARDWARE_REVISION_CHARACTERISTIC_UUID);
        uuidMap.put(SHNCapabilityDeviceInformation.SHNDeviceInformationType.ManufacturerName, MANUFACTURER_NAME_CHARACTERISTIC_UUID);
        uuidMap.put(SHNCapabilityDeviceInformation.SHNDeviceInformationType.ModelNumber, MODEL_NUMBER_CHARACTERISTIC_UUID);
        uuidMap.put(SHNCapabilityDeviceInformation.SHNDeviceInformationType.SerialNumber, SERIAL_NUMBER_CHARACTERISTIC_UUID);
        uuidMap.put(SHNCapabilityDeviceInformation.SHNDeviceInformationType.SoftwareRevision, SOFTWARE_REVISION_CHARACTERISTIC_UUID);
        uuidMap.put(SHNCapabilityDeviceInformation.SHNDeviceInformationType.SystemID, SYSTEM_ID_CHARACTERISTIC_UUID);

        registerSHNServiceListener(this);
    }

    private static Set<UUID> getRequiredCharacteristics() {
        return new HashSet<>();
    }

    private static Set<UUID> getOptionalCharacteristics() {
        Set<UUID> set = new HashSet<>();
        set.add(UUID.fromString(FIRMWARE_REVISION_CHARACTERISTIC_UUID));
        set.add(UUID.fromString(HARDWARE_REVISION_CHARACTERISTIC_UUID));
        set.add(UUID.fromString(MANUFACTURER_NAME_CHARACTERISTIC_UUID));
        set.add(UUID.fromString(MODEL_NUMBER_CHARACTERISTIC_UUID));
        set.add(UUID.fromString(SERIAL_NUMBER_CHARACTERISTIC_UUID));
        set.add(UUID.fromString(SOFTWARE_REVISION_CHARACTERISTIC_UUID));
        set.add(UUID.fromString(SYSTEM_ID_CHARACTERISTIC_UUID));
        return set;
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
                public void reportResult(@NonNull SHNResult shnResult, byte[] data) {
                    cacheInformation(shnResult, data);
                    String value = getValueFromCache();
                    Date date = getDateFromCache();

                    if (value == null || date == null) {
                        resultListener.onError(informationType, shnResult);
                    } else {
                        resultListener.onDeviceInformation(informationType, value, date);
                    }
                }

                @Nullable
                private Date getDateFromCache() {
                    String dateString = sharedPreferences.getString(informationType.name() + DATE_SUFFIX, null);
                    Date date = null;
                    try {
                        date = getSimpleDateFormat().parse(dateString);
                    } catch (ParseException e) {
                    }
                    return date;
                }

                @Nullable
                private String getValueFromCache() {
                    return sharedPreferences.getString(informationType.name(), null);
                }

                private void cacheInformation(final @NonNull SHNResult shnResult, final byte[] data) {
                    if (shnResult == SHNResult.SHNOk) {
                        String value = new String(data, StandardCharsets.UTF_8);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString(informationType.name(), value);
                        edit.putString(informationType.name() + DATE_SUFFIX, getSimpleDateFormat().format(new Date()));
                        edit.commit();
                    }
                }
            };
            shnCharacteristic.read(resultReporter);
        }
    }

    @NonNull
    SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat(DATE_PATTERN);
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
