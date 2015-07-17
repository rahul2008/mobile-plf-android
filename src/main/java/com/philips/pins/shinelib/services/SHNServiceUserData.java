package com.philips.pins.shinelib.services;

import android.util.Log;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNCommandResultReporter;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNObjectResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.SHNStringResultListener;
import com.philips.pins.shinelib.datatypes.SHNSportTypeForThresholds;
import com.philips.pins.shinelib.datatypes.SHNUserConfiguration;
import com.philips.pins.shinelib.framework.BleUUIDCreator;
import com.philips.pins.shinelib.framework.SHNFactory;
import com.philips.pins.shinelib.utility.SHNBluetoothDataConverter;
import com.philips.pins.shinelib.utility.ScalarConverters;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNServiceUserData implements SHNService.SHNServiceListener {

    private static final String TAG = SHNServiceUserData.class.getSimpleName();
    private static final boolean LOGGING = false;

    public static final UUID SERVICE_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x181C));

    // Mandatory Characteristics
    public static final UUID DATABASE_CHANGE_INCREMENT_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A99)); // Review: Please rename to indicate Database Change Increment
    public static final UUID USER_INDEX_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A9A));
    public static final UUID USER_CONTROL_POINT_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A9F));

    // Optional Characteristics
    public static final UUID FIRST_NAME_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A8A));
    public static final UUID LAST_NAME_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A90));
    public static final UUID EMAIL_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A87));
    public static final UUID AGE_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A80));

    public static final UUID DATE_OF_BIRTH_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A85));
    public static final UUID GENDER_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A8C));
    public static final UUID WEIGHT_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A98));
    public static final UUID HEIGHT_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A8E));

    public static final UUID VO2MAX_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A96));
    public static final UUID HEART_RATE_MAX_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A8D));
    public static final UUID RESTING_HEART_RATE_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A92));
    public static final UUID MAXIMUM_RECOMMENDED_HEART_RATE_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A91));
    public static final UUID ANAEROBIC_THRESHOLD_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A7F));
    public static final UUID AEROBIC_THRESHOLD_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A83));

    public static final UUID SPORT_TYPE_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A93));
    public static final UUID DATE_OF_THRESHOLD_ASSESSMENT_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A86));
    public static final UUID WAIST_CIRCUMFERENCE_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A97));
    public static final UUID HIP_CIRCUMFERENCE_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A8F));

    public static final UUID FAT_BURN_HEART_RATE_LOWER_LIMIT_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A88));
    public static final UUID FAT_BURN_HEART_RATE_UPPER_LIMIT_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A89));

    public static final UUID AEROBIC_HEART_RATE_LOWER_LIMIT_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A7E));
    public static final UUID AEROBIC_HEART_RATE_UPPER_LIMIT_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A84));
    public static final UUID ANAEROBIC_HEART_RATE_LOWER_LIMIT_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A81));
    public static final UUID ANAEROBIC_HEART_RATE_UPPER_LIMIT_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A82));

    public static final UUID FIVE_ZONE_HEART_LIMITS_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A8B));
    public static final UUID THREE_ZONE_HEART_LIMITS_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A94));
    public static final UUID TWO_ZONE_HEART_LIMITS_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A95));
    public static final UUID LANGUAGE_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2AA2));

    // ControlPoint commands
    private static final byte OP_CODE_REGISTER_NEW_USER = (byte) 0x01;
    private static final byte OP_CODE_CONSENT = (byte) 0x02;
    private static final byte OP_CODE_DELETE_USER_DATA = (byte) 0x03;
    private static final byte OP_CODE_RESPONSE = (byte) 0x20;

    // ControlPoint response codes
    private static final byte RESPONSE_CODE_SUCCESS = (byte) 0x01;
    private static final byte RESPONSE_CODE_OP_CODE_NOT_SUPPORTED = (byte) 0x02;
    private static final byte RESPONSE_CODE_INVALID_PARAMETER = (byte) 0x03;
    private static final byte RESPONSE_CODE_OPERATION_FAILED = (byte) 0x04;
    private static final byte RESPONSE_CODE_USER_NOT_AUTHORIZED = (byte) 0x05;

    public static final int UNSUCCESSFUL_OPERATION_VALUE = -1;

    private SHNService shnService;

    // Characteristics resolutions
    private final static float WEIGHT_RESOLUTION = 0.005f;
    private final static float HEIGHT_RESOLUTION = 0.01f;
    private final static float WAIST_CIRCUMFERENCE_RESOLUTION = 0.01f;
    private final static float HIP_CIRCUMFERENCE_RESOLUTION = 0.01f;

    private LinkedList<SHNUserDataCommand> commandQueue;
    private boolean executing;

    private SHNServiceUserDataListener shnServiceUserDataListener;

    private SHNCharacteristic.SHNCharacteristicChangedListener shnCharacteristicChangedListener = new SHNCharacteristic.SHNCharacteristicChangedListener() {
        @Override
        public void onCharacteristicChanged(SHNCharacteristic shnCharacteristic, byte[] data) {
            if(shnCharacteristic.getUuid() == USER_CONTROL_POINT_CHARACTERISTIC_UUID) {
                if (executing) {
                    ByteBuffer byteBuffer = ByteBuffer.wrap(data);
                    byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                    processResponseData(byteBuffer);
                } else {
                    Log.w(TAG, "Notification is received with no request");
                }
            }
        }
    };

    public interface SHNServiceUserDataListener {
        void onServiceStateChanged(SHNServiceUserData shnServiceUserData, SHNService.State state);
    }

    public SHNServiceUserData(SHNFactory shnFactory) {
        shnService = shnFactory.createNewSHNService(SERVICE_UUID, getRequiredCharacteristics(), getOptionalCharacteristics());
        shnService.registerSHNServiceListener(this);
        commandQueue = new LinkedList<>();
    }

    private Set<UUID> getOptionalCharacteristics() {
        Set<UUID> optionalCharacteristics = new HashSet<>();
        optionalCharacteristics.add(FIRST_NAME_CHARACTERISTIC_UUID);
        optionalCharacteristics.add(LAST_NAME_CHARACTERISTIC_UUID);
        optionalCharacteristics.add(AGE_CHARACTERISTIC_UUID);

        optionalCharacteristics.add(DATE_OF_BIRTH_CHARACTERISTIC_UUID);
        optionalCharacteristics.add(GENDER_CHARACTERISTIC_UUID);
        optionalCharacteristics.add(HEIGHT_CHARACTERISTIC_UUID);
        optionalCharacteristics.add(WEIGHT_CHARACTERISTIC_UUID);

        optionalCharacteristics.add(VO2MAX_CHARACTERISTIC_UUID);
        optionalCharacteristics.add(MAXIMUM_RECOMMENDED_HEART_RATE_CHARACTERISTIC_UUID);
        optionalCharacteristics.add(HEART_RATE_MAX_CHARACTERISTIC_UUID);
        optionalCharacteristics.add(RESTING_HEART_RATE_CHARACTERISTIC_UUID);
        optionalCharacteristics.add(ANAEROBIC_THRESHOLD_CHARACTERISTIC_UUID);
        optionalCharacteristics.add(AEROBIC_THRESHOLD_CHARACTERISTIC_UUID);

        optionalCharacteristics.add(SPORT_TYPE_CHARACTERISTIC_UUID);
        optionalCharacteristics.add(DATE_OF_THRESHOLD_ASSESSMENT_CHARACTERISTIC_UUID);
        optionalCharacteristics.add(WAIST_CIRCUMFERENCE_CHARACTERISTIC_UUID);
        optionalCharacteristics.add(HIP_CIRCUMFERENCE_CHARACTERISTIC_UUID);

        optionalCharacteristics.add(FAT_BURN_HEART_RATE_LOWER_LIMIT_CHARACTERISTIC_UUID);
        optionalCharacteristics.add(FAT_BURN_HEART_RATE_UPPER_LIMIT_CHARACTERISTIC_UUID);

        optionalCharacteristics.add(AEROBIC_HEART_RATE_LOWER_LIMIT_CHARACTERISTIC_UUID);
        optionalCharacteristics.add(AEROBIC_HEART_RATE_UPPER_LIMIT_CHARACTERISTIC_UUID);
        optionalCharacteristics.add(ANAEROBIC_HEART_RATE_LOWER_LIMIT_CHARACTERISTIC_UUID);
        optionalCharacteristics.add(ANAEROBIC_HEART_RATE_UPPER_LIMIT_CHARACTERISTIC_UUID);

        optionalCharacteristics.add(FIVE_ZONE_HEART_LIMITS_CHARACTERISTIC_UUID);
        optionalCharacteristics.add(THREE_ZONE_HEART_LIMITS_CHARACTERISTIC_UUID);
        optionalCharacteristics.add(TWO_ZONE_HEART_LIMITS_CHARACTERISTIC_UUID);

        optionalCharacteristics.add(LANGUAGE_CHARACTERISTIC_UUID);

        return optionalCharacteristics;
    }

    private Set<UUID> getRequiredCharacteristics() {
        Set<UUID> requiredCharacteristicUUIDs = new HashSet<>();
        requiredCharacteristicUUIDs.add(DATABASE_CHANGE_INCREMENT_CHARACTERISTIC_UUID);
        requiredCharacteristicUUIDs.add(USER_INDEX_CHARACTERISTIC_UUID);
        requiredCharacteristicUUIDs.add(USER_CONTROL_POINT_CHARACTERISTIC_UUID);
        return requiredCharacteristicUUIDs;
    }

    public SHNService getShnService() {
        return shnService;
    }

    public void setShnServiceUserDataListener(SHNServiceUserDataListener shnServiceUserDataListener) {
        this.shnServiceUserDataListener = shnServiceUserDataListener;
    }

    //implements SHNService.SHNServiceListener
    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        if (shnService.getState() != SHNService.State.Available && state == SHNService.State.Available) {
            SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(USER_CONTROL_POINT_CHARACTERISTIC_UUID);
            shnCharacteristic.setIndication(true, null);
            shnCharacteristic.setShnCharacteristicChangedListener(shnCharacteristicChangedListener);
            shnService.transitionToReady();
        }
        if(shnServiceUserDataListener!=null) {
            shnServiceUserDataListener.onServiceStateChanged(this, state);
        }
    }

    private void getStringCharacteristic(UUID uuid, final SHNStringResultListener listener) {
        if (LOGGING) Log.i(TAG, "getStringCharacteristic");
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(uuid);
        SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (LOGGING) Log.i(TAG, "getStringCharacteristic reportResult");
                String value = null;
                if (shnResult == SHNResult.SHNOk) {
                    value = new String(data, StandardCharsets.UTF_8);
                }
                if (listener != null) {
                    listener.onActionCompleted(value, shnResult);
                }
            }
        };
        shnCharacteristic.read(resultReporter);
    }

    private void getUByteCharacteristic(UUID uuid, final SHNIntegerResultListener listener) {
        if (LOGGING) Log.i(TAG, "getUByteCharacteristic");
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(uuid);
        SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (LOGGING) Log.i(TAG, "getUByteCharacteristic reportResult");
                int value = UNSUCCESSFUL_OPERATION_VALUE;
                if (shnResult == SHNResult.SHNOk) {
                    ByteBuffer byteBuffer = ByteBuffer.wrap(data);
                    byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                    value = ScalarConverters.ubyteToInt(byteBuffer.get());
                }
                if (listener != null) {
                    listener.onActionCompleted(value, shnResult);
                }
            }
        };
        shnCharacteristic.read(resultReporter);
    }

    private void getUShortCharacteristic(UUID uuid, final SHNObjectResultListener listener, final float resolution) {
        if (LOGGING) Log.i(TAG, "getUByteCharacteristic");
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(uuid);
        SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (LOGGING) Log.i(TAG, "getUByteCharacteristic reportResult");
                int value = UNSUCCESSFUL_OPERATION_VALUE;
                if (shnResult == SHNResult.SHNOk) {
                    ByteBuffer byteBuffer = ByteBuffer.wrap(data);
                    byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                    value = ScalarConverters.ushortToInt(byteBuffer.getShort());
                }

                if (listener != null) {
                    listener.onActionCompleted((float) (value) * resolution, shnResult);
                }
            }
        };
        shnCharacteristic.read(resultReporter);
    }

    public void getUserIndex(final SHNIntegerResultListener listener) {
        getUByteCharacteristic(USER_INDEX_CHARACTERISTIC_UUID, listener);
    }

    private void getDateCharacteristic(UUID uuid, final SHNObjectResultListener listener) {
        if (LOGGING) Log.i(TAG, "getDate");
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(uuid);

        SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (LOGGING) Log.i(TAG, "getDate reportResult");
                Date date = null;
                if (shnResult == SHNResult.SHNOk) {
                    ByteBuffer byteBuffer = ByteBuffer.wrap(data);
                    byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                    date = SHNBluetoothDataConverter.getDate(byteBuffer);
                }
                if (listener != null) {
                    listener.onActionCompleted(date, shnResult);
                }
            }
        };
        shnCharacteristic.read(resultReporter);
    }

    public void getFirstName(SHNStringResultListener listener) {
        getStringCharacteristic(FIRST_NAME_CHARACTERISTIC_UUID, listener);
    }

    public void getLastName(SHNStringResultListener listener) {
        getStringCharacteristic(LAST_NAME_CHARACTERISTIC_UUID, listener);
    }

    public void getEmail(SHNStringResultListener listener) {
        getStringCharacteristic(EMAIL_CHARACTERISTIC_UUID, listener);
    }

    public void getAge(SHNIntegerResultListener listener) {
        getUByteCharacteristic(AGE_CHARACTERISTIC_UUID, listener);
    }

    public void getDateOfBirth(SHNObjectResultListener listener) {
        getDateCharacteristic(DATE_OF_BIRTH_CHARACTERISTIC_UUID, listener);
    }

    public void getSex(final SHNObjectResultListener listener) {
        SHNIntegerResultListener innerResultListener = new SHNIntegerResultListener() {

            @Override
            public void onActionCompleted(int value, SHNResult result) {
                SHNUserConfiguration.Sex sex = SHNUserConfiguration.Sex.Unspecified;
                if (value < 2) {
                    if (value == 0) {
                        sex = SHNUserConfiguration.Sex.Male;
                    } else {
                        sex = SHNUserConfiguration.Sex.Female;
                    }
                }
                if (listener != null) {
                    listener.onActionCompleted(sex, result);
                }
            }
        };
        getUByteCharacteristic(GENDER_CHARACTERISTIC_UUID, innerResultListener);
    }

    public void getWeightInKg(SHNObjectResultListener listener) {
        getUShortCharacteristic(WEIGHT_CHARACTERISTIC_UUID, listener, WEIGHT_RESOLUTION);
    }

    public void getHeightInMeters(SHNObjectResultListener listener) {
        getUShortCharacteristic(HEIGHT_CHARACTERISTIC_UUID, listener, HEIGHT_RESOLUTION);
    }

    public void getVO2Max(SHNIntegerResultListener listener) {
        getUByteCharacteristic(VO2MAX_CHARACTERISTIC_UUID, listener);
    }

    public void getHeartRateMax(SHNIntegerResultListener listener) {
        getUByteCharacteristic(HEART_RATE_MAX_CHARACTERISTIC_UUID, listener);
    }

    public void getRestingHeartRate(SHNIntegerResultListener listener) {
        getUByteCharacteristic(RESTING_HEART_RATE_CHARACTERISTIC_UUID, listener);
    }

    public void getMaximumRecommendedHeartRate(SHNIntegerResultListener listener) {
        getUByteCharacteristic(MAXIMUM_RECOMMENDED_HEART_RATE_CHARACTERISTIC_UUID, listener);
    }

    public void getAnaerobicThreshold(SHNIntegerResultListener listener) {
        getUByteCharacteristic(ANAEROBIC_THRESHOLD_CHARACTERISTIC_UUID, listener);
    }

    public void getAerobicThreshold(SHNIntegerResultListener listener) {
        getUByteCharacteristic(AEROBIC_THRESHOLD_CHARACTERISTIC_UUID, listener);
    }

    public void getSportType(final SHNObjectResultListener listener) {
        SHNIntegerResultListener innerResultListener = new SHNIntegerResultListener() {
            @Override
            public void onActionCompleted(int value, SHNResult result) {
                SHNSportTypeForThresholds thresholds = SHNSportTypeForThresholds.Unspecified;
                ;
                switch (value) {
                    case 1:
                        thresholds = SHNSportTypeForThresholds.Running;
                        break;
                    case 2:
                        thresholds = SHNSportTypeForThresholds.Cycling;
                        break;
                    case 3:
                        thresholds = SHNSportTypeForThresholds.Rowing;
                        break;
                    case 4:
                        thresholds = SHNSportTypeForThresholds.CrossTraining;
                        break;
                    case 5:
                        thresholds = SHNSportTypeForThresholds.Climbing;
                        break;
                    case 6:
                        thresholds = SHNSportTypeForThresholds.Skiing;
                        break;
                    case 7:
                        thresholds = SHNSportTypeForThresholds.Skating;
                        break;
                    case 8:
                        thresholds = SHNSportTypeForThresholds.ArmExercising;
                        break;
                    case 9:
                        thresholds = SHNSportTypeForThresholds.LowerBodyExercising;
                        break;
                    case 10:
                        thresholds = SHNSportTypeForThresholds.UpperBodyExercising;
                        break;
                    case 11:
                        thresholds = SHNSportTypeForThresholds.WholeBodyExercising;
                        break;
                }
                if (listener != null) {
                    listener.onActionCompleted(thresholds, result);
                }
            }
        };
        getUByteCharacteristic(SPORT_TYPE_CHARACTERISTIC_UUID, innerResultListener);
    }

    public void getDateOfThresholdAssessment(SHNObjectResultListener listener) {
        getDateCharacteristic(DATE_OF_THRESHOLD_ASSESSMENT_CHARACTERISTIC_UUID, listener);
    }

    public void getWaistCircumference(SHNObjectResultListener listener) {
        getUShortCharacteristic(WAIST_CIRCUMFERENCE_CHARACTERISTIC_UUID, listener, WAIST_CIRCUMFERENCE_RESOLUTION);
    }

    public void getHipCircumference(SHNObjectResultListener listener) {
        getUShortCharacteristic(HIP_CIRCUMFERENCE_CHARACTERISTIC_UUID, listener, HIP_CIRCUMFERENCE_RESOLUTION);
    }

    public void getFatBurnHeartRateLowerLimit(SHNIntegerResultListener listener) {
        getUByteCharacteristic(FAT_BURN_HEART_RATE_LOWER_LIMIT_CHARACTERISTIC_UUID, listener);
    }

    public void getFatBurnHeartRateUpperLimit(SHNIntegerResultListener listener) {
        getUByteCharacteristic(FAT_BURN_HEART_RATE_UPPER_LIMIT_CHARACTERISTIC_UUID, listener);
    }

    public void getAerobicHeartRateLowerLimit(SHNIntegerResultListener listener) {
        getUByteCharacteristic(AEROBIC_HEART_RATE_LOWER_LIMIT_CHARACTERISTIC_UUID, listener);
    }

    public void getAerobicHeartRateUpperLimit(SHNIntegerResultListener listener) {
        getUByteCharacteristic(AEROBIC_HEART_RATE_UPPER_LIMIT_CHARACTERISTIC_UUID, listener);
    }

    public void getAnaerobicHeartRateLowerLimit(SHNIntegerResultListener listener) {
        getUByteCharacteristic(ANAEROBIC_HEART_RATE_LOWER_LIMIT_CHARACTERISTIC_UUID, listener);
    }

    public void getAnaerobicHeartRateUpperLimit(SHNIntegerResultListener listener) {
        getUByteCharacteristic(ANAEROBIC_HEART_RATE_UPPER_LIMIT_CHARACTERISTIC_UUID, listener);
    }

    private void getZoneHeartRateLimits(UUID uuid, SHNCommandResultReporter resultReporter) {
        if (LOGGING) Log.i(TAG, "getZoneHeartRateLimits");
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(uuid);
        shnCharacteristic.read(resultReporter);
    }

    public void getFiveZoneHeartRateLimits(final SHNObjectResultListener listener) {
        getZoneHeartRateLimits(FIVE_ZONE_HEART_LIMITS_CHARACTERISTIC_UUID, new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (LOGGING) Log.i(TAG, "getFiveZoneHeartRateLimits reportResult");
                FiveZoneHeartRateLimits limits = null;
                if (shnResult == SHNResult.SHNOk) {
                    ByteBuffer byteBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
                    limits = new FiveZoneHeartRateLimits(byteBuffer);
                }
                if (listener != null) {
                    listener.onActionCompleted(limits, shnResult);
                }
            }
        });
    }

    public void getThreeZoneHeartRateLimits(final SHNObjectResultListener listener) {
        getZoneHeartRateLimits(THREE_ZONE_HEART_LIMITS_CHARACTERISTIC_UUID, new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (LOGGING) Log.i(TAG, "getThreeZoneHeartRateLimits reportResult");
                ThreeZoneHeartRateLimits limits = null;
                if (shnResult == SHNResult.SHNOk) {
                    ByteBuffer byteBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
                    limits = new ThreeZoneHeartRateLimits(byteBuffer);
                }
                if (listener != null) {
                    listener.onActionCompleted(limits, shnResult);
                }
            }
        });
    }

    public void getTwoZoneHeartRateLimits(final SHNObjectResultListener listener) {
        getZoneHeartRateLimits(TWO_ZONE_HEART_LIMITS_CHARACTERISTIC_UUID, new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (LOGGING) Log.i(TAG, "getThreeZoneHeartRateLimits reportResult");
                TwoZoneHeartRateLimits limits = null;
                if (shnResult == SHNResult.SHNOk) {
                    ByteBuffer byteBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
                    limits = new TwoZoneHeartRateLimits(byteBuffer);
                }
                if (listener != null) {
                    listener.onActionCompleted(limits, shnResult);
                }
            }
        });
    }

    public void getLanguage(SHNStringResultListener listener) {
        getStringCharacteristic(LANGUAGE_CHARACTERISTIC_UUID, listener);
    }

    // Setters for the characteristics
    public void setFirstName(String name, SHNResultListener listener) {
        setStringCharacteristic(FIRST_NAME_CHARACTERISTIC_UUID, name, listener);
    }

    public void setLastName(String name, SHNResultListener listener) {
        setStringCharacteristic(LAST_NAME_CHARACTERISTIC_UUID, name, listener);
    }

    public void setEmail(String email, SHNResultListener listener) {
        setStringCharacteristic(EMAIL_CHARACTERISTIC_UUID, email, listener);
    }

    public void setAge(int age, SHNResultListener listener) {
        writeScalarValueToCharacteristic(AGE_CHARACTERISTIC_UUID, 1, age, listener);
    }

    public void setDateOfBirth(Date dateOfBirth, SHNResultListener listener) {
        byte[] rawData = SHNBluetoothDataConverter.convertDateToByteArray(dateOfBirth);

        writeToCharacteristic(DATE_OF_BIRTH_CHARACTERISTIC_UUID, rawData, listener);
    }

    public void setSex(SHNUserConfiguration.Sex sex, SHNResultListener listener) {
        int value = 2;
        switch (sex) {
            case Male:
                value = 0;
                break;
            case Female:
                value = 1;
                break;
        }
        writeScalarValueToCharacteristic(GENDER_CHARACTERISTIC_UUID, 1, value, listener);
    }

    public void setWeightInKg(float kilograms, SHNResultListener listener) {
        writeScalarValueToCharacteristicWithResolution(WEIGHT_CHARACTERISTIC_UUID, 2, kilograms, listener, WEIGHT_RESOLUTION);
    }

    public void setHeightInMeters(float meters, SHNResultListener listener) {
        writeScalarValueToCharacteristicWithResolution(HEIGHT_CHARACTERISTIC_UUID, 2, meters, listener, HEIGHT_RESOLUTION);
    }

    public void setVO2Max(int mlPerKgPerMinute, SHNResultListener listener) {
        writeScalarValueToCharacteristic(VO2MAX_CHARACTERISTIC_UUID, 1, mlPerKgPerMinute, listener);
    }

    public void setHeartRateMax(int beatsPerMinute, SHNResultListener listener) {
        writeScalarValueToCharacteristic(HEART_RATE_MAX_CHARACTERISTIC_UUID, 1, beatsPerMinute, listener);
    }

    public void setRestingHeartRate(int beatsPerMinute, SHNResultListener listener) {
        writeScalarValueToCharacteristic(RESTING_HEART_RATE_CHARACTERISTIC_UUID, 1, beatsPerMinute, listener);
    }

    public void setMaximumRecommendedHeartRate(int beatsPerMinute, SHNResultListener listener) {
        writeScalarValueToCharacteristic(MAXIMUM_RECOMMENDED_HEART_RATE_CHARACTERISTIC_UUID, 1, beatsPerMinute, listener);
    }

    public void setAnaerobicThreshold(int beatsPerMinute, SHNResultListener listener) {
        writeScalarValueToCharacteristic(ANAEROBIC_THRESHOLD_CHARACTERISTIC_UUID, 1, beatsPerMinute, listener);
    }

    public void setAerobicThreshold(int beatsPerMinute, SHNResultListener listener) {
        writeScalarValueToCharacteristic(AEROBIC_THRESHOLD_CHARACTERISTIC_UUID, 1, beatsPerMinute, listener);
    }

    public void setDateOfThresholdAssessment(Date dateOfAssessment, SHNResultListener listener) {
        byte[] rawData = SHNBluetoothDataConverter.convertDateToByteArray(dateOfAssessment);

        writeToCharacteristic(DATE_OF_THRESHOLD_ASSESSMENT_CHARACTERISTIC_UUID, rawData, listener);
    }

    public void setWaistCircumference(float meters, SHNResultListener listener) {
        writeScalarValueToCharacteristicWithResolution(WAIST_CIRCUMFERENCE_CHARACTERISTIC_UUID, 2, meters, listener, WAIST_CIRCUMFERENCE_RESOLUTION);
    }

    public void setHipCircumference(float meters, SHNResultListener listener) {
        writeScalarValueToCharacteristicWithResolution(HIP_CIRCUMFERENCE_CHARACTERISTIC_UUID, 2, meters, listener, HIP_CIRCUMFERENCE_RESOLUTION);
    }

    public void setFatBurnHeartRateLowerLimit(int beatsPerMinute, SHNResultListener listener) {
        writeScalarValueToCharacteristic(FAT_BURN_HEART_RATE_LOWER_LIMIT_CHARACTERISTIC_UUID, 1, beatsPerMinute, listener);
    }

    public void setFatBurnHeartRateUpperLimit(int beatsPerMinute, SHNResultListener listener) {
        writeScalarValueToCharacteristic(FAT_BURN_HEART_RATE_UPPER_LIMIT_CHARACTERISTIC_UUID, 1, beatsPerMinute, listener);
    }

    public void setAerobicHeartRateLowerLimit(int beatsPerMinute, SHNResultListener listener) {
        writeScalarValueToCharacteristic(AEROBIC_HEART_RATE_LOWER_LIMIT_CHARACTERISTIC_UUID, 1, beatsPerMinute, listener);
    }

    public void setAerobicHeartRateUpperLimit(int beatsPerMinute, SHNResultListener listener) {
        writeScalarValueToCharacteristic(AEROBIC_HEART_RATE_UPPER_LIMIT_CHARACTERISTIC_UUID, 1, beatsPerMinute, listener);
    }

    public void setAnaerobicHeartRateLowerLimit(int beatsPerMinute, SHNResultListener listener) {
        writeScalarValueToCharacteristic(ANAEROBIC_HEART_RATE_LOWER_LIMIT_CHARACTERISTIC_UUID, 1, beatsPerMinute, listener);
    }

    public void setAnaerobicHeartRateUpperLimit(int beatsPerMinute, SHNResultListener listener) {
        writeScalarValueToCharacteristic(ANAEROBIC_HEART_RATE_UPPER_LIMIT_CHARACTERISTIC_UUID, 1, beatsPerMinute, listener);
    }

    public void setFiveZoneHeartRateLimits(FiveZoneHeartRateLimits limits, SHNResultListener listener) {
        byte[] rawData = limits.pack();
        writeToCharacteristic(FIVE_ZONE_HEART_LIMITS_CHARACTERISTIC_UUID, rawData, listener);
    }

    public void setThreeZoneHeartRateLimits(ThreeZoneHeartRateLimits limits, SHNResultListener listener) {
        byte[] rawData = limits.pack();
        writeToCharacteristic(THREE_ZONE_HEART_LIMITS_CHARACTERISTIC_UUID, rawData, listener);
    }

    public void setTwoZoneHeartRateLimits(TwoZoneHeartRateLimits limits, SHNResultListener listener) {
        byte[] rawData = limits.pack();
        writeToCharacteristic(TWO_ZONE_HEART_LIMITS_CHARACTERISTIC_UUID, rawData, listener);
    }

    public void setLanguage(String language, SHNResultListener listener) {
        setStringCharacteristic(LANGUAGE_CHARACTERISTIC_UUID, language, listener);
    }

    public void getDatabaseIncrement(final SHNIntegerResultListener listener) {
        if (LOGGING) Log.i(TAG, "getDatabaseIncrement");
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(DATABASE_CHANGE_INCREMENT_CHARACTERISTIC_UUID);

        SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (LOGGING) Log.i(TAG, "getDatabaseIncrement reportResult");
                int value = UNSUCCESSFUL_OPERATION_VALUE;
                if (shnResult == SHNResult.SHNOk) {
                    ByteBuffer byteBuffer = ByteBuffer.wrap(data);
                    byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                    value = byteBuffer.getInt();
                }
                if (listener != null) {
                    listener.onActionCompleted(value, shnResult);
                }
            }
        };
        shnCharacteristic.read(resultReporter);
    }

    public void setDatabaseIncrement(int increment, final SHNResultListener listener) {
        writeScalarValueToCharacteristic(DATABASE_CHANGE_INCREMENT_CHARACTERISTIC_UUID, 4, increment, listener);
    }

    private void setStringCharacteristic(UUID uuid, String name, final SHNResultListener listener) {
        if (LOGGING) Log.i(TAG, "setStringCharacteristic");
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(uuid);
        byte[] value = name.getBytes(StandardCharsets.UTF_8);
        SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (LOGGING) Log.i(TAG, "setStringCharacteristic reportResult");

                if (listener != null) {
                    listener.onActionCompleted(shnResult);
                }
            }
        };
        shnCharacteristic.write(value, resultReporter);
    }

    private void writeScalarValueToCharacteristicWithResolution(UUID uuid, int sizeByte, float value, final SHNResultListener listener, float resolution) {
        int rawValue = Math.round(value / resolution);
        writeScalarValueToCharacteristic(uuid, sizeByte, rawValue, listener);
    }

    private void writeScalarValueToCharacteristic(UUID uuid, int sizeByte, int value, final SHNResultListener listener) {
        if (LOGGING) Log.i(TAG, "writeScalarValueToCharacteristic");
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(uuid);

        ByteBuffer buffer = ByteBuffer.allocate(sizeByte);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        switch (sizeByte) {
            case 1:
                if (value < 0 || value > 255) {
                    throw new IllegalArgumentException("Value is outside of range 0-255");
                }
                buffer.put((byte) value);
                break;
            case 2:
                if (value < 0 || value > 65535) {
                    throw new IllegalArgumentException("Value is outside of range 0-65535");
                }
                buffer.putShort((short) value);
                break;
            case 4:
                buffer.putInt(value);
                break;
        }
        byte[] byteValue = buffer.array();

        SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (LOGGING) Log.i(TAG, "writeScalarValueToCharacteristic reportResult");
                if (listener != null) {
                    listener.onActionCompleted(shnResult);
                }
            }
        };

        shnCharacteristic.write(byteValue, resultReporter);
    }

    public void registerNewUser(int consentCode, final SHNIntegerResultListener listener) {
        SHNIntegerResultListener wrappedIntegerListener = new SHNIntegerResultListener() {
            @Override
            public void onActionCompleted(int value, SHNResult result) {
                listener.onActionCompleted(value, result);
                removeCurrentCommandAndStartNext();
            }
        };

        SHNUserDataCommand command = new SHNUserDataCommand.Builder().setCommand(SHNUserDataCommand.Command.REGISTER).setConsentCode(consentCode).setShnIntegerResultListener(wrappedIntegerListener).build();
        commandQueue.add(command);
        executeCommandIfAllowed();
    }

    public void consentExistingUser(int userId, int consentCode, final SHNResultListener listener) {
        SHNResultListener wrappedListener = getWrappedSHNResultListener(listener);

        SHNUserDataCommand command = new SHNUserDataCommand.Builder().setCommand(SHNUserDataCommand.Command.CONSENT).setUserId(userId).setConsentCode(consentCode).setShnResultListener(wrappedListener).build();
        commandQueue.add(command);
        executeCommandIfAllowed();
    }

    public void deleteUser(final SHNResultListener listener) {
        SHNResultListener wrappedListener = getWrappedSHNResultListener(listener);

        SHNUserDataCommand command = new SHNUserDataCommand.Builder().setCommand(SHNUserDataCommand.Command.DELETE).setShnResultListener(wrappedListener).build();
        commandQueue.add(command);
        executeCommandIfAllowed();
    }

    private SHNResultListener getWrappedSHNResultListener(final SHNResultListener listener) {
        return new SHNResultListener() {
            @Override
            public void onActionCompleted(final SHNResult result) {
                listener.onActionCompleted(result);
                removeCurrentCommandAndStartNext();
            }
        };
    }

    private void executeCommandIfAllowed() {
        if (!executing && commandQueue.size() > 0) {
            executing = true;
            final SHNUserDataCommand command = commandQueue.getFirst();
            switch (command.getType()) {
                case REGISTER:
                    registerNewUserImpl(command.consentCode, command.shnIntegerResultListener);
                    break;
                case CONSENT:
                    consentExistingUserImpl(command.userId, command.consentCode, command.shnResultListener);
                    break;
                case DELETE:
                    deleteUserImpl(command.shnResultListener);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }
    }

    private void removeCurrentCommandAndStartNext() {
        commandQueue.remove();
        executing = false;
        executeCommandIfAllowed();
    }

    private void registerNewUserImpl(int consentCode, final SHNIntegerResultListener listener) {
        if (consentCode > 9999 || consentCode < 0) {
            listener.onActionCompleted(UNSUCCESSFUL_OPERATION_VALUE, SHNResult.SHNInvalidParameterError);
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(3);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            buffer.put(OP_CODE_REGISTER_NEW_USER);
            buffer.putShort((short) consentCode);
            byte[] value = buffer.array();

            writeToControlPointCharacteristic(value, listener);
        }
    }

    private void consentExistingUserImpl(int userId, int consentCode, final SHNResultListener listener) {
        if (consentCode > 9999 || consentCode < 0 || userId < 0 || userId > 254) {
            listener.onActionCompleted(SHNResult.SHNInvalidParameterError);
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            buffer.put(OP_CODE_CONSENT);
            buffer.put((byte) userId);
            buffer.putShort((short) consentCode);
            byte[] value = buffer.array();

            writeToControlPointCharacteristic(value, listener);
        }
    }

    private void deleteUserImpl(SHNResultListener listener) {
        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.put(OP_CODE_DELETE_USER_DATA);
        byte[] value = buffer.array();

        writeToControlPointCharacteristic(value, listener);
    }

    private void writeToControlPointCharacteristic(byte[] value, final SHNIntegerResultListener listener) {
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(USER_CONTROL_POINT_CHARACTERISTIC_UUID);
        SHNCommandResultReporter shnCommandResultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (shnResult != SHNResult.SHNOk) {
                    listener.onActionCompleted(UNSUCCESSFUL_OPERATION_VALUE, shnResult);
                }
            }
        };

        shnCharacteristic.write(value, shnCommandResultReporter);
    }

    private void writeToControlPointCharacteristic(byte[] value, final SHNResultListener listener) {
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(USER_CONTROL_POINT_CHARACTERISTIC_UUID);
        SHNCommandResultReporter shnCommandResultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (shnResult != SHNResult.SHNOk) {
                    listener.onActionCompleted(shnResult);
                }
            }
        };

        shnCharacteristic.write(value, shnCommandResultReporter);
    }

    private void writeToCharacteristic(UUID uuid, byte[] value, final SHNResultListener listener) {
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(uuid);
        SHNCommandResultReporter shnCommandResultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                listener.onActionCompleted(shnResult);
            }
        };

        shnCharacteristic.write(value, shnCommandResultReporter);
    }

    private void processResponseData(ByteBuffer byteBuffer) {
        SHNUserDataCommand command = commandQueue.getFirst();
        try {
            if (byteBuffer.get() == OP_CODE_RESPONSE) {
                matchWithCommand(command, byteBuffer);
            } else {
                command.notifyListeners(SHNResult.SHNInvalidResponseError);
            }
        } catch (BufferUnderflowException ex) {
            command.notifyListeners(SHNResult.SHNResponseIncompleteError);
        }
    }

    private void matchWithCommand(SHNUserDataCommand command, ByteBuffer byteBuffer) {
        if (command.getType() == SHNUserDataCommand.Command.REGISTER && byteBuffer.get() == OP_CODE_REGISTER_NEW_USER) {
            extractValueAndNotifyListener(byteBuffer, command);
        } else if (command.getType() == SHNUserDataCommand.Command.CONSENT && byteBuffer.get() == OP_CODE_CONSENT) {
            extractResultAndNotifyListener(byteBuffer, command);
        } else if (command.getType() == SHNUserDataCommand.Command.DELETE && byteBuffer.get() == OP_CODE_DELETE_USER_DATA) {
            extractResultAndNotifyListener(byteBuffer, command);
        } else {
            command.notifyListeners(SHNResult.SHNInvalidResponseError);
        }
    }

    private void extractValueAndNotifyListener(ByteBuffer byteBuffer, SHNUserDataCommand command) {
        SHNResult result = getSHNResult(byteBuffer.get());
        int userId = UNSUCCESSFUL_OPERATION_VALUE;
        if (result == SHNResult.SHNOk) {
            userId = ScalarConverters.ubyteToInt(byteBuffer.get());
        }
        command.notifyListeners(userId, result);
    }

    private void extractResultAndNotifyListener(ByteBuffer byteBuffer, SHNUserDataCommand command) {
        SHNResult result = getSHNResult(byteBuffer.get());
        command.notifyListeners(result);
    }

    private SHNResult getSHNResult(byte byteResult) {
        switch (byteResult) {
            case RESPONSE_CODE_SUCCESS:
                return SHNResult.SHNOk;
            case RESPONSE_CODE_OP_CODE_NOT_SUPPORTED:
                return SHNResult.SHNUnsupportedOperation;
            case RESPONSE_CODE_INVALID_PARAMETER:
                return SHNResult.SHNInvalidParameterError;
            case RESPONSE_CODE_OPERATION_FAILED:
                return SHNResult.SHNOperationFailed;
            case RESPONSE_CODE_USER_NOT_AUTHORIZED:
                return SHNResult.SHNUserNotAuthorized;
            default:
                return SHNResult.SHNInvalidResponseError;
        }
    }

    protected static class SHNUserDataCommand {

        private enum Command {REGISTER, CONSENT, DELETE}

        private Command command;
        private int consentCode;
        private int userId = 255;

        private SHNIntegerResultListener shnIntegerResultListener;
        private SHNResultListener shnResultListener;

        private SHNUserDataCommand() {

        }

        private void notifyListeners(int value, SHNResult result) {
            if (shnIntegerResultListener != null)
                shnIntegerResultListener.onActionCompleted(value, result);
        }

        private void notifyListeners(SHNResult result) {
            if (shnIntegerResultListener != null)
                shnIntegerResultListener.onActionCompleted(-1, result);
            if (shnResultListener != null)
                shnResultListener.onActionCompleted(result);
        }

        private Command getType() {
            return command;
        }

        private int getConsentCode() {
            return consentCode;
        }

        public static class Builder {

            private SHNUserDataCommand shnUserDataCommand;

            private boolean hasCommand;
            private boolean hasListener;

            public Builder() {
                shnUserDataCommand = new SHNUserDataCommand();
            }

            public Builder setCommand(Command command) {
                shnUserDataCommand.command = command;
                hasCommand = true;
                return this;
            }

            public Builder setUserId(int userId) {
                shnUserDataCommand.userId = userId;
                return this;
            }

            public Builder setConsentCode(int consentCode) {
                shnUserDataCommand.consentCode = consentCode;
                return this;
            }

            public Builder setShnIntegerResultListener(SHNIntegerResultListener shnIntegerResultListener) {
                shnUserDataCommand.shnIntegerResultListener = shnIntegerResultListener;
                hasListener = true;
                return this;
            }

            public Builder setShnResultListener(SHNResultListener shnResultListener) {
                shnUserDataCommand.shnResultListener = shnResultListener;
                hasListener = true;
                return this;
            }

            public SHNUserDataCommand build() {
                if (hasCommand && hasListener) {
                    return shnUserDataCommand;
                } else throw new IllegalArgumentException();
            }
        }
    }

    public static class FiveZoneHeartRateLimits {

        private final int veryLightToLight;
        private final int lightToModerate;
        private final int moderateToHard;
        private final int hardToMaximum;

        public FiveZoneHeartRateLimits(ByteBuffer byteBuffer) {
            veryLightToLight = ScalarConverters.ubyteToInt(byteBuffer.get());
            lightToModerate = ScalarConverters.ubyteToInt(byteBuffer.get());
            moderateToHard = ScalarConverters.ubyteToInt(byteBuffer.get());
            hardToMaximum = ScalarConverters.ubyteToInt(byteBuffer.get());
        }

        public FiveZoneHeartRateLimits(int veryLightToLight, int lightToModerate, int moderateToHard, int hardToMaximum) {
            this.veryLightToLight = veryLightToLight;
            this.lightToModerate = lightToModerate;
            this.moderateToHard = moderateToHard;
            this.hardToMaximum = hardToMaximum;
        }

        public int getVeryLightToLightLimit() {
            return veryLightToLight;
        }

        public int getLightToModerateLimit() {
            return lightToModerate;
        }

        public int getModerateToHardLimit() {
            return moderateToHard;
        }

        public int getHardToMaximumLimit() {
            return hardToMaximum;
        }

        protected byte[] pack() {
            ByteBuffer byteBuffer = ByteBuffer.allocate(4);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            byteBuffer.put((byte) veryLightToLight);
            byteBuffer.put((byte) lightToModerate);
            byteBuffer.put((byte) moderateToHard);
            byteBuffer.put((byte) hardToMaximum);
            return byteBuffer.array();
        }
    }

    public static class ThreeZoneHeartRateLimits {

        private final int lightToModerate;
        private final int moderateToHard;

        public ThreeZoneHeartRateLimits(ByteBuffer byteBuffer) {
            lightToModerate = ScalarConverters.ubyteToInt(byteBuffer.get());
            moderateToHard = ScalarConverters.ubyteToInt(byteBuffer.get());
        }

        public ThreeZoneHeartRateLimits(int lightToModerate, int moderateToHard) {
            this.lightToModerate = lightToModerate;
            this.moderateToHard = moderateToHard;
        }

        public int getLightToModerateLimit() {
            return lightToModerate;
        }

        public int getModerateToHardLimit() {
            return moderateToHard;
        }

        protected byte[] pack() {
            ByteBuffer byteBuffer = ByteBuffer.allocate(2);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            byteBuffer.put((byte) lightToModerate);
            byteBuffer.put((byte) moderateToHard);
            return byteBuffer.array();
        }
    }

    public static class TwoZoneHeartRateLimits {

        private final int fatBurnFitnessLimit;

        public TwoZoneHeartRateLimits(ByteBuffer byteBuffer) {
            fatBurnFitnessLimit = ScalarConverters.ubyteToInt(byteBuffer.get());
        }

        public TwoZoneHeartRateLimits(int fatBurnFitnessLimit) {
            this.fatBurnFitnessLimit = fatBurnFitnessLimit;
        }

        public int getFatBurnFitnessLimit() {
            return fatBurnFitnessLimit;
        }

        protected byte[] pack() {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            byteBuffer.put((byte) fatBurnFitnessLimit);
            return byteBuffer.array();
        }
    }
}

