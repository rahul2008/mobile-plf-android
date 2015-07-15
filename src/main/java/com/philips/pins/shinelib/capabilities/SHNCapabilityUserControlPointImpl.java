package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.datatypes.SHNUserConfiguration;
import com.philips.pins.shinelib.services.SHNServiceUserData;
import com.philips.pins.shinelib.utility.ShinePreferenceWrapper;

import java.util.Date;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNCapabilityUserControlPointImpl implements SHNCapabilityUserControlPoint {

    private final SHNServiceUserData shnServiceUserData;
    private final SHNUserConfiguration shnUserConfiguration;
    private final ShinePreferenceWrapper shinePreferenceWrapper;

    private SHNCapabilityUserControlPointListener shnCapabilityUserControlPointListener;

    private final static int INVALID_VALUE = -1;

    private SHNServiceUserData.SHNServiceUserDataListener shnServiceUserDataListener = new SHNServiceUserData.SHNServiceUserDataListener() {

        @Override
        public void onServiceStateChanged(SHNServiceUserData shnServiceUserData, SHNService.State state) {
            if (state == SHNService.State.Available) {
                autoConsentUser();
            }
        }
    };

    public SHNCapabilityUserControlPointImpl(SHNServiceUserData shnServiceUserData, SHNUserConfiguration shnUserConfiguration, ShinePreferenceWrapper wrapper) {
        this.shnServiceUserData = shnServiceUserData;
        shnServiceUserData.setShnServiceUserDataListener(shnServiceUserDataListener);
        this.shnUserConfiguration = shnUserConfiguration;
        this.shinePreferenceWrapper = wrapper;
    }

    @Override
    public void setSHNCapabilityUserControlPointListener(SHNCapabilityUserControlPointListener shnCapabilityUserControlPointListener) {
        this.shnCapabilityUserControlPointListener = shnCapabilityUserControlPointListener;
    }

    @Override
    public int getCurrentUserIndex() {
        return shinePreferenceWrapper.readUserIndex();
    }

    @Override
    public int getCurrentConsentCode() {
        return shinePreferenceWrapper.readUserConsentCode();
    }

    @Override
    public void registerNewUser(int consentCode, SHNIntegerResultListener shnIntegerResultListener) {
        shnServiceUserData.registerNewUser(consentCode, shnIntegerResultListener);
    }

    @Override
    public void setCurrentUser(int userIndex, int consentCode, SHNResultListener shnResultListener) {
        shinePreferenceWrapper.storeUserIndex(userIndex);
        shinePreferenceWrapper.storeUserConsentCode(consentCode);

        shnServiceUserData.consentExistingUser(userIndex, consentCode, shnResultListener);

        shnServiceUserData.getDatabaseIncrement(new SHNIntegerResultListener() {
            @Override
            public void onActionCompleted(int increment, SHNResult result) {
                if (result == SHNResult.SHNOk) {
                    int localIncrement = shinePreferenceWrapper.readDataBaseIncrement();
                    if (localIncrement != increment) {
                        if (increment == 0) {
                            pushUserConfiguration(null);
                        } else {
                            shnCapabilityUserControlPointListener.onMismatchedDatabaseIncrement(shinePreferenceWrapper.readUserIndex(), localIncrement, increment);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void deleteCurrentUser(SHNResultListener shnResultListener) {
        shnServiceUserData.deleteUser(shnResultListener);
    }

    @Override
    public void pushUserConfiguration(SHNResultListener shnResultListener) {
        new Pusher(shnUserConfiguration, shnServiceUserData, shinePreferenceWrapper.readDataBaseIncrement()).start(shnResultListener);
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

    private static class Pusher {

        private SHNResultListener shnResultListener = new SHNResultListener() {
            @Override
            public void onActionCompleted(SHNResult result) {
                //TODO: find out what to do if result not ok
                executeNext(result);
            }
        };

        Queue<Command> commandQueue;
        private SHNResultListener shnEndResultListener;

        private Pusher(SHNUserConfiguration userConfiguration, SHNServiceUserData shnServiceUserData, int dataBaseIncrement) {
            commandQueue = new LinkedBlockingQueue<>();
            commandQueue.add(new AgeCommand(userConfiguration.getAge(), shnServiceUserData));
            commandQueue.add(new RestingHeartRateCommand(userConfiguration.getRestingHeartRate(), shnServiceUserData));
            commandQueue.add(new MaxHeartRateCommand(userConfiguration.getMaxHeartRate(), shnServiceUserData));
            commandQueue.add(new HeightCommand(userConfiguration.getHeightInCm(), shnServiceUserData));

            commandQueue.add(new GenderCommand(userConfiguration.getSex(), shnServiceUserData));
            commandQueue.add(new WeightCommand(userConfiguration.getWeightInKg(), shnServiceUserData));
            commandQueue.add(new DateCommand(userConfiguration.getDateOfBirth(), shnServiceUserData));
            commandQueue.add(new DataBaseIncrementCommand(dataBaseIncrement, shnServiceUserData)); // at the end the increment is set to local
        }

        private void executeNext(SHNResult result) {
            Command next = commandQueue.poll();
            if (next != null) {
                next.run();
            }else{
                shnEndResultListener.onActionCompleted(result);
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
                    shnResultListener.onActionCompleted(null);
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
                shnServiceUserData.setAge(value, shnResultListener);
            }
        }

        private class RestingHeartRateCommand extends IntegerCommand {

            private RestingHeartRateCommand(Integer beats, SHNServiceUserData shnServiceUserData) {
                super(beats, shnServiceUserData);
            }

            @Override
            void setValue(int age, SHNResultListener shnResultListener) {
                shnServiceUserData.setRestingHeartRate(value, shnResultListener);
            }
        }

        private class MaxHeartRateCommand extends IntegerCommand {

            private MaxHeartRateCommand(Integer beats, SHNServiceUserData shnServiceUserData) {
                super(beats, shnServiceUserData);
            }

            @Override
            void setValue(int age, SHNResultListener shnResultListener) {
                shnServiceUserData.setHeartRateMax(value, shnResultListener);
            }
        }

        private class HeightCommand extends IntegerCommand {

            private HeightCommand(Integer heightInCm, SHNServiceUserData shnServiceUserData) {
                super(heightInCm, shnServiceUserData);
            }

            @Override
            void setValue(int age, SHNResultListener shnResultListener) {
                float heightInMeters = value / 100f;
                shnServiceUserData.setHeightInMeters(heightInMeters, shnResultListener);
            }
        }

        private class DataBaseIncrementCommand extends IntegerCommand {

            private DataBaseIncrementCommand(Integer age, SHNServiceUserData shnServiceUserData) {
                super(age, shnServiceUserData);
            }

            @Override
            void setValue(int age, SHNResultListener shnResultListener) {
                shnServiceUserData.setDatabaseIncrement(value, shnResultListener);
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
                if (sex != null) {
                    shnServiceUserData.setSex(sex, shnResultListener);
                } else {
                    shnResultListener.onActionCompleted(null);
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
                if (weight != null) {
                    shnServiceUserData.setWeightInKg(weight.floatValue(), shnResultListener);
                } else {
                    shnResultListener.onActionCompleted(null);
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
                if (date != null) {
                    shnServiceUserData.setDateOfBirth(date, shnResultListener);
                } else {
                    shnResultListener.onActionCompleted(null);
                }
            }
        }
    }
}
