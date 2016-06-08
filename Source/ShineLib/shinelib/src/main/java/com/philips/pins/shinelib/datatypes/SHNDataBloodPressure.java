/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

/**
 * Blood pressure measurement data.
 */
public class SHNDataBloodPressure extends SHNData {
    public enum SHNPulseRateStatus {
        WithinRange, ExceedsUpperLimit, LessThanLowerLimit, Unknown
    }

    private float bloodPressureSystolicInMmHg;
    private float bloodPressureDiastolicInMmHg;
    private float bloodPressureMeanArterialInMmHg;
    private short userId;
    private short pulseRateInBeatsPerMinute;
    private SHNPulseRateStatus shnPulseRateStatus;
    private boolean hasUserId;
    private boolean hasPulseRateInBeatsPerMinute;

    private SHNDataBloodPressure() {
    }

    public float getBloodPressureSystolicInMmHg() {
        return bloodPressureSystolicInMmHg;
    }

    public float getBloodPressureDiastolicInMmHg() {
        return bloodPressureDiastolicInMmHg;
    }

    public float getBloodPressureMeanArterialInMmHg() {
        return bloodPressureMeanArterialInMmHg;
    }

    public short getUserId() {
        return userId;
    }

    public short getPulseRateInBeatsPerMinute() {
        return pulseRateInBeatsPerMinute;
    }

    public SHNPulseRateStatus getShnPulseRateStatus() {
        return shnPulseRateStatus;
    }

    public boolean hasUserId() {
        return hasUserId;
    }

    public boolean hasPulseRateInBeatsPerMinute() {
        return hasPulseRateInBeatsPerMinute;
    }

    /**
     * @return {@link SHNDataType#BloodPressure}
     */
    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.BloodPressure;
    }

    /**
     * Builder for {@link SHNDataBloodPressure}.
     * <p/>
     * Chain calls to setters and finish with {@link Builder#build()} to get a {@code SHNDataBloodPressure} object.
     */
    public static class Builder {
        private SHNDataBloodPressure shnDataBloodPressure;

        private boolean hasBloodPressureSystolicInMmHg;
        private boolean hasBloodPressureDiastolicInMmHg;
        private boolean hasBloodPressureMeanArterialInMmHg;

        public Builder() {
            shnDataBloodPressure = new SHNDataBloodPressure();
        }

        public Builder setBloodPressureSystolicInMmHg(float bloodPressureSystolicInMmHg) {
            shnDataBloodPressure.bloodPressureSystolicInMmHg = bloodPressureSystolicInMmHg;
            hasBloodPressureSystolicInMmHg = true;
            return this;
        }

        public Builder setBloodPressureDiastolicInMmHg(float bloodPressureDiastolicInMmHg) {
            shnDataBloodPressure.bloodPressureDiastolicInMmHg = bloodPressureDiastolicInMmHg;
            hasBloodPressureDiastolicInMmHg = true;
            return this;
        }

        public Builder setBloodPressureMeanArterialInMmHg(float bloodPressureMeanArterialInMmHg) {
            shnDataBloodPressure.bloodPressureMeanArterialInMmHg = bloodPressureMeanArterialInMmHg;
            hasBloodPressureMeanArterialInMmHg = true;
            return this;
        }

        public Builder setUserId(short userId) {
            shnDataBloodPressure.userId = userId;
            shnDataBloodPressure.hasUserId = true;
            return this;
        }

        public Builder setPulseRateInBeatsPerMinute(short pulseRateInBeatsPerMinute) {
            shnDataBloodPressure.pulseRateInBeatsPerMinute = pulseRateInBeatsPerMinute;
            shnDataBloodPressure.hasPulseRateInBeatsPerMinute = true;
            return this;
        }

        public Builder setShnPulseRateStatus(SHNPulseRateStatus shnPulseRateStatus) {
            shnDataBloodPressure.shnPulseRateStatus = shnPulseRateStatus;
            return this;
        }

        public SHNDataBloodPressure build() {
            if (hasBloodPressureSystolicInMmHg && hasBloodPressureDiastolicInMmHg && hasBloodPressureMeanArterialInMmHg) {
                return shnDataBloodPressure;
            } else {
                throw new IllegalArgumentException("Incomplete data");
            }
        }
    }
}
