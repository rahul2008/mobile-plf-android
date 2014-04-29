package com.philips.cl.di.dev.pa.purifier;

import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;

public interface SensorEventListener {
	public void sensorDataReceived(AirPortInfo airPurifierEventDto) ;
}
