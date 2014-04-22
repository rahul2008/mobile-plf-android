package com.philips.cl.di.dev.pa.purifier;

import com.philips.cl.di.dev.pa.datamodel.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.firmware.FirmwareEventDto;

/**
 * The listener interface for receiving airPurifierEvent events.
 * The class that is interested in processing a airPurifierEvent
 * event implements this interface. When
 * the airPurifierEvent event occurs, that object's appropriate
 * method is invoked.
 *
 * @see AirPurifierEventDto
 */
public interface AirPurifierEventListener {
	
	/**
	 * Sensor data received.
	 *
	 * @param airPurifierEvent the air purifier event
	 */
	public void airPurifierEventReceived(AirPurifierEventDto airPurifierEvent) ;
	
	public void firmwareEventReceived(FirmwareEventDto firmwareEventDto);
}
