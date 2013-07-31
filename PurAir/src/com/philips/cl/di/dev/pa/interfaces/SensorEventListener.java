package com.philips.cl.di.dev.pa.interfaces;

import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;

public interface SensorEventListener {
	public void sensorDataReceived(AirPurifierEventDto airPurifierEventDto) ;
}
