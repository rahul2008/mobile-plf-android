package com.philips.pins.shinelib.datatypes;

/**
 * Created by 310188215 on 09/07/15.
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
    private boolean hasUserID;
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

    public boolean hasUserID() {
        return hasUserID;
    }

    public boolean hasPulseRateInBeatsPerMinute() {
        return hasPulseRateInBeatsPerMinute;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.BloodPressure;
    }

    public static class Builder {
        private SHNDataBloodPressure shnDataBloodPressure;
        
        public Builder() {
            shnDataBloodPressure = new SHNDataBloodPressure();
        }
        
        public Builder setBloodPressureSystolicInMmHg(float bloodPressureSystolicInMmHg) {
            shnDataBloodPressure.bloodPressureSystolicInMmHg = bloodPressureSystolicInMmHg;
            return this;
        }
    
        public Builder setBloodPressureDiastolicInMmHg(float bloodPressureDiastolicInMmHg) {
            shnDataBloodPressure.bloodPressureDiastolicInMmHg = bloodPressureDiastolicInMmHg;
            return this;
        }
    
        public Builder setBloodPressureMeanArterialInMmHg(float bloodPressureMeanArterialInMmHg) {
            shnDataBloodPressure.bloodPressureMeanArterialInMmHg = bloodPressureMeanArterialInMmHg;
            return this;
        }
    
        public Builder setUserId(short userId) {
            shnDataBloodPressure.userId = userId;
            shnDataBloodPressure.hasUserID = true;
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
    
        public SHNDataBloodPressure createSHNDataBloodPressure() {
            return shnDataBloodPressure;
        }
    }
}
