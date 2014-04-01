package com.philips.cl.di.dev.pa.purifier;

import com.philips.cl.di.dev.pa.datamodel.AirPurifierEventDto;

public interface SensorEventListener {
	public void sensorDataReceived(AirPurifierEventDto airPurifierEventDto) ;
}
