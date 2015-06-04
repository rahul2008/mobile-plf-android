package com.pins.philips.shinelib.services.healththermometer;

import com.pins.philips.shinelib.SHNTemperatureMeasurementIntervalResultListener;
import com.pins.philips.shinelib.SHNResultListener;
import com.pins.philips.shinelib.SHNService;
import com.pins.philips.shinelib.SHNTemperatureMeasurementResultListener;
import com.pins.philips.shinelib.framework.BleUUIDCreator;

import java.util.Set;
import java.util.UUID;

/**
 * Created by 310188215 on 04/06/15.
 */
public class SHNServiceHealthThermometer extends SHNService implements SHNService.SHNServiceListener {
    public final static String TAG = SHNServiceHealthThermometer.class.getSimpleName();

    public final static UUID SERVICE_HEALTH_THERMOMETER_UUID                = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x1809));
    public final static UUID CHARACTERISTIC_TEMPERATURE_MEASUREMENT_UUID    = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A1C));
    public final static UUID CHARACTERISTIC_TEMPERATURE_TYPE_UUID           = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A1D));
    public final static UUID CHARACTERISTIC_INTERMEDIATE_TEMPERATURE_UUID   = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A1E));
    public final static UUID CHARACTERISTIC_MEASUREMENT_INTERVAL_UUID       = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A21));

    public interface SHNServiceHealthThermometerListener {
        void onTemperatureMeasurementReceived(SHNServiceHealthThermometer shnServiceHealthThermometer, SHNTemperatureMeasurement shnTemperatureMeasurement);
        void onServiceStateChanged(SHNServiceHealthThermometer shnServiceHealthThermometer, State state);
        void onIntermediateTemperatureReceived(SHNServiceHealthThermometer shnServiceHealthThermometer, SHNTemperatureMeasurement shnTemperatureMeasurement);
        void onMeasurementIntervalChanged(SHNServiceHealthThermometer shnServiceHealthThermometer, SHNTemperatureMeasurementInterval shnTemperatureMeasurementInterval);
    }

    public SHNServiceHealthThermometer(UUID serviceUuid, Set<UUID> requiredCharacteristics, Set<UUID> optionalCharacteristics) {
        super(serviceUuid, requiredCharacteristics, optionalCharacteristics);
    }


    public void setReceiveTemperatureMeasurement(boolean enabled, SHNResultListener shnResultListener) {
        throw new UnsupportedOperationException();
    }

    public void setReceiveIntermediateTemperature(boolean enabled, SHNResultListener shnResultListener) {
        throw new UnsupportedOperationException();
    }

    public void setReceiveMeasurementIntervalChanges(boolean enabled, SHNResultListener shnResultListener) {
        throw new UnsupportedOperationException();
    }

    public void readTemperatureType(SHNTemperatureMeasurementResultListener shnTemperatureMeasurementResultListener) {
        throw new UnsupportedOperationException();
    }

    public void readMeasurementInterval(SHNTemperatureMeasurementIntervalResultListener shnTemperatureMeasurementIntervalResultListener) {
        throw new UnsupportedOperationException();
    }

    public void writeMeasurementInterval(SHNTemperatureMeasurementInterval shnTemperatureMeasurementInterval, SHNResultListener shnResultListener) {
        throw new UnsupportedOperationException();
    }

    // implements SHNService.SHNServiceListener
    @Override
    public void onServiceStateChanged(SHNService shnService, State state) {

    }
}
