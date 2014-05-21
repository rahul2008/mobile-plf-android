package com.philips.cl.di.dev.pa.purifier;

import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;

/**
 * The listener interface for receiving airPurifierEvent events.
 * The class that is interested in processing a airPurifierEvent
 * event implements this interface. 
 * When the airPurifierEvent event occurs, that object's 
 * appropriate method is invoked.
 *
 * @see AirPortInfo
 */
public interface AirPurifierEventListener {
	
	void onAirPurifierChanged();
	
	void onAirPurifierEventReceived() ;
	
	void onFirmwareEventReceived();
}
