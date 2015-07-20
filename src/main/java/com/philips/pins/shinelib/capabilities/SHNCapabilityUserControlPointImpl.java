package com.philips.pins.shinelib.capabilities;

import android.content.SharedPreferences;

import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.datatypes.SHNUserConfiguration;
import com.philips.pins.shinelib.services.SHNServiceUserData;
import com.philips.pins.shinelib.utility.SHNDevicePreferenceWrapper;

import java.util.Date;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNCapabilityUserControlPointImpl implements SHNCapabilityUserControlPoint {

    private final SHNServiceUserData shnServiceUserData;
    private final SHNDevicePreferenceWrapper shnDevicePreferenceWrapper;
    private final SHNUserConfiguration shnUserConfiguration;

    private SHNCapabilityUserControlPointListener shnCapabilityUserControlPointListener;

    private final static int INVALID_VALUE = -1;

    private static final String UC_DATABASE_INCREMENT = "USER_CONFIGURATION_DATABASE_INCREMENT";

    private static final String UDS_USER_ID = "UDS_USER_ID";
    private static final String UDS_CONSENT_CODE = "UDS_CONSENT_CODE";
    private static final String UDS_DATABASE_INCREMENT = "UDS_DATABASE_INCREMENT";

    private SHNServiceUserData.SHNServiceUserDataListener shnServiceUserDataListener = new SHNServiceUserData.SHNServiceUserDataListener() {

        @Override
        public void onServiceStateChanged(SHNServiceUserData shnServiceUserData, SHNService.State state) {
            if (state == SHNService.State.Available) {
                autoConsentUser();
            }
        }
    };

    public SHNCapabilityUserControlPointImpl(SHNServiceUserData shnServiceUserData, SHNUserConfiguration shnUserConfiguration, SHNDevicePreferenceWrapper wrapper) {
        this.shnServiceUserData = shnServiceUserData;
        shnServiceUserData.setShnServiceUserDataListener(shnServiceUserDataListener);
        this.shnUserConfiguration = shnUserConfiguration;
        this.shnDevicePreferenceWrapper = wrapper;
    }

    @Override
    public void setSHNCapabilityUserControlPointListener(SHNCapabilityUserControlPointListener shnCapabilityUserControlPointListener) {
        this.shnCapabilityUserControlPointListener = shnCapabilityUserControlPointListener;
    }

    @Override
    public int getCurrentUserIndex() {
        return shnDevicePreferenceWrapper.getInt(UDS_USER_ID);
    }

    @Override
    public int getCurrentConsentCode() {
        return shnDevicePreferenceWrapper.getInt(UDS_CONSENT_CODE);
    }

    @Override
    public void registerNewUser(int consentCode, SHNIntegerResultListener shnIntegerResultListener) {
        shnServiceUserData.registerNewUser(consentCode, shnIntegerResultListener);
    }

    @Override
    public void setCurrentUser(int userIndex, int consentCode, final SHNResultListener shnResultListener) {
        storeUserData(userIndex, consentCode);

        SHNResultListener innerShnResultListener = new SHNResultListener() {
            @Override
            public void onActionCompleted(SHNResult result) {
                shnResultListener.onActionCompleted(result);
                if (result == SHNResult.SHNOk) {
                    checkIncrementMismatch();
                }
            }
        };

        shnServiceUserData.consentExistingUser(userIndex, consentCode, innerShnResultListener);
    }

    @Override
    public void deleteCurrentUser(SHNResultListener shnResultListener) {
        shnServiceUserData.deleteUser(shnResultListener);
    }

    @Override
    public void pushUserConfiguration(SHNResultListener shnResultListener) {
        new Pusher(shnUserConfiguration, shnServiceUserData, shnDevicePreferenceWrapper).start(shnResultListener);
    }

    private void autoConsentUser() {
        final int userIndex = getCurrentUserIndex();
        final int consentCode = getCurrentConsentCode();

        if (userIndex != INVALID_VALUE && consentCode != INVALID_VALUE) {
            shnServiceUserData.consentExistingUser(userIndex, consentCode, new SHNResultListener() {
                @Override
                public void onActionCompleted(SHNResult result) {
                    if (result != SHNResult.SHNOk) {
                        shnCapabilityUserControlPointListener.onAutoConsentFailed(userIndex, consentCode, result);
                    }
                }
            });
        }
    }

    private void storeUserData(int userIndex, int consentCode) {
        SharedPreferences.Editor editor = shnDevicePreferenceWrapper.edit();
        editor.putInt(UDS_USER_ID, userIndex);
        editor.putInt(UDS_CONSENT_CODE, consentCode);
    }

    private int getStoredDataBaseIncrement() {
        return shnDevicePreferenceWrapper.getInt(UDS_DATABASE_INCREMENT);
    }

    private int getStoredUserConfigurationIncrement() {
        return shnDevicePreferenceWrapper.getInt(UC_DATABASE_INCREMENT);
    }

    private void checkIncrementMismatch() {
        if (shnUserConfiguration.getIncrementIndex() != getStoredUserConfigurationIncrement()) {
            notifyListener();
        } else {
            shnServiceUserData.getDatabaseIncrement(new SHNIntegerResultListener() {
                @Override
                public void onActionCompleted(int increment, SHNResult result) {
                    if (result == SHNResult.SHNOk) {
                        if (getStoredDataBaseIncrement() != increment) {
                            notifyListener();
                        } else {
                            checkAgeMismatch();
                        }
                    }
                }
            });
        }
    }

    private void checkAgeMismatch() {
        if (shnServiceUserData.hasAgeCharacteristic()) {
            shnServiceUserData.getAge(new SHNIntegerResultListener() {
                @Override
                public void onActionCompleted(int age, SHNResult result) {
                    if (result == SHNResult.SHNOk) {
                        if (shnUserConfiguration.getAge() != age) {
                            notifyListener();
                        }
                    }
                }
            });
        }
    }

    private void notifyListener() {
        if (shnCapabilityUserControlPointListener != null) {
            shnCapabilityUserControlPointListener.onMismatchedDatabaseIncrement(getCurrentUserIndex());
        }
    }

    private static class Pusher {

        private SHNResultListener shnResultListener = new SHNResultListener() {
            @Override
            public void onActionCompleted(SHNResult result) {
                if (result == SHNResult.SHNOk) {
                    executeNext(result);
                } else {
                    if (shnEndResultListener != null) {
                        shnEndResultListener.onActionCompleted(result);
                    }
                }
            }
        };

        Queue<Command> commandQueue;
        private SHNResultListener shnEndResultListener;
        private SHNUserConfiguration userConfiguration;
        private SHNDevicePreferenceWrapper shnDevicePreferenceWrapper;

        private Pusher(SHNUserConfiguration userConfiguration, SHNServiceUserData shnServiceUserData, SHNDevicePreferenceWrapper shnDevicePreferenceWrapper) {
            this.userConfiguration = userConfiguration;
            this.shnDevicePreferenceWrapper = shnDevicePreferenceWrapper;
            commandQueue = new LinkedBlockingQueue<>();
            commandQueue.add(new AgeCommand(userConfiguration.getAge(), shnServiceUserData));
            commandQueue.add(new RestingHeartRateCommand(userConfiguration.getRestingHeartRate(), shnServiceUserData));
            commandQueue.add(new MaxHeartRateCommand(userConfiguration.getMaxHeartRate(), shnServiceUserData));
            commandQueue.add(new HeightCommand(userConfiguration.getHeightInCm(), shnServiceUserData));

            commandQueue.add(new GenderCommand(userConfiguration.getSex(), shnServiceUserData));
            commandQueue.add(new WeightCommand(userConfiguration.getWeightInKg(), shnServiceUserData));
            commandQueue.add(new DateCommand(userConfiguration.getDateOfBirth(), shnServiceUserData));
            commandQueue.add(new DataBaseIncrementCommand(shnServiceUserData));
        }

        private void executeNext(SHNResult result) {
            Command next = commandQueue.poll();
            if (next != null) {
                next.run();
            } else {
                if (shnEndResultListener != null) {
                    shnEndResultListener.onActionCompleted(result);
                }
            }
        }

        public void start(SHNResultListener shnEndResultListener) {
            this.shnEndResultListener = shnEndResultListener;
            executeNext(null);
        }

        private interface Command extends Runnable {
        }

        private abstract class IntegerCommand implements Command {

            Integer value;
            protected SHNServiceUserData shnServiceUserData;

            private IntegerCommand(Integer value, SHNServiceUserData shnServiceUserData) {
                this.value = value;
                this.shnServiceUserData = shnServiceUserData;
            }

            @Override
            public void run() {
                if (value != null) {
                    setValue(value, shnResultListener);
                } else {
                    shnResultListener.onActionCompleted(SHNResult.SHNOk);
                }
            }

            abstract void setValue(int value, SHNResultListener shnResultListener);
        }

        private class AgeCommand extends IntegerCommand {

            private AgeCommand(Integer age, SHNServiceUserData shnServiceUserData) {
                super(age, shnServiceUserData);
            }

            @Override
            void setValue(int age, SHNResultListener shnResultListener) {
                if (shnServiceUserData.hasAgeCharacteristic()) {
                    shnServiceUserData.setAge(value, shnResultListener);
                } else {
                    shnResultListener.onActionCompleted(SHNResult.SHNOk);
                }
            }
        }

        private class RestingHeartRateCommand extends IntegerCommand {

            private RestingHeartRateCommand(Integer beats, SHNServiceUserData shnServiceUserData) {
                super(beats, shnServiceUserData);
            }

            @Override
            void setValue(int age, SHNResultListener shnResultListener) {
                if (shnServiceUserData.hasRestingHeartRateCharacteristic()) {
                    shnServiceUserData.setRestingHeartRate(value, shnResultListener);
                } else {
                    shnResultListener.onActionCompleted(SHNResult.SHNOk);
                }
            }
        }

        private class MaxHeartRateCommand extends IntegerCommand {

            private MaxHeartRateCommand(Integer beats, SHNServiceUserData shnServiceUserData) {
                super(beats, shnServiceUserData);
            }

            @Override
            void setValue(int age, SHNResultListener shnResultListener) {
                if (shnServiceUserData.hasHeartRateMaxCharacteristic()) {
                    shnServiceUserData.setHeartRateMax(value, shnResultListener);
                } else {
                    shnResultListener.onActionCompleted(SHNResult.SHNOk);
                }
            }
        }

        private class HeightCommand extends IntegerCommand {

            private HeightCommand(Integer heightInCm, SHNServiceUserData shnServiceUserData) {
                super(heightInCm, shnServiceUserData);
            }

            @Override
            void setValue(int age, SHNResultListener shnResultListener) {
                if (shnServiceUserData.hasHeightCharacteristic()) {
                    float heightInMeters = value / 100f;
                    shnServiceUserData.setHeightInMeters(heightInMeters, shnResultListener);
                } else {
                    shnResultListener.onActionCompleted(SHNResult.SHNOk);
                }
            }
        }

        private class DataBaseIncrementCommand implements Command {

            private int newIndex;
            private SHNServiceUserData shnServiceUserData;

            private DataBaseIncrementCommand(SHNServiceUserData shnServiceUserData) {
                this.shnServiceUserData = shnServiceUserData;
            }

            @Override
            public void run() {
                incrementDatabaseIncrement(new SHNResultListener() {
                    @Override
                    public void onActionCompleted(SHNResult result) {
                        shnResultListener.onActionCompleted(result);
                        if (result == SHNResult.SHNOk) {
                            SharedPreferences.Editor editor = shnDevicePreferenceWrapper.edit();
                            editor.putInt(UDS_DATABASE_INCREMENT, newIndex);
                            editor.putInt(UC_DATABASE_INCREMENT, userConfiguration.getIncrementIndex());
                        }
                    }
                });
            }

            private void incrementDatabaseIncrement(final SHNResultListener listener) {
                shnServiceUserData.getDatabaseIncrement(new SHNIntegerResultListener() {
                    @Override
                    public void onActionCompleted(int value, SHNResult result) {
                        if (result == SHNResult.SHNOk) {
                            newIndex = value + 1;
                            shnServiceUserData.setDatabaseIncrement(newIndex, listener);
                        } else {
                            listener.onActionCompleted(result);
                        }
                    }
                });
            }
        }

        private class GenderCommand implements Command {

            private SHNUserConfiguration.Sex sex;
            private SHNServiceUserData shnServiceUserData;

            private GenderCommand(SHNUserConfiguration.Sex sex, SHNServiceUserData shnServiceUserData) {
                this.sex = sex;
                this.shnServiceUserData = shnServiceUserData;
            }

            @Override
            public void run() {
                if (sex != null && shnServiceUserData.hasGenderCharacteristic()) {
                    shnServiceUserData.setSex(sex, shnResultListener);
                } else {
                    shnResultListener.onActionCompleted(SHNResult.SHNOk);
                }
            }
        }

        private class WeightCommand implements Command {

            private Double weight;
            private SHNServiceUserData shnServiceUserData;

            private WeightCommand(Double weight, SHNServiceUserData shnServiceUserData) {
                this.weight = weight;
                this.shnServiceUserData = shnServiceUserData;
            }

            @Override
            public void run() {
                if (weight != null && shnServiceUserData.hasWeightCharacteristic()) {
                    shnServiceUserData.setWeightInKg(weight.floatValue(), shnResultListener);
                } else {
                    shnResultListener.onActionCompleted(SHNResult.SHNOk);
                }
            }
        }

        private class DateCommand implements Command {

            private Date date;
            private SHNServiceUserData shnServiceUserData;

            private DateCommand(Date date, SHNServiceUserData shnServiceUserData) {
                this.date = date;
                this.shnServiceUserData = shnServiceUserData;
            }

            @Override
            public void run() {
                if (date != null && shnServiceUserData.hasDateOfBirthCharacteristic()) {
                    shnServiceUserData.setDateOfBirth(date, shnResultListener);
                } else {
                    shnResultListener.onActionCompleted(SHNResult.SHNOk);
                }
            }
        }
    }
}
