package com.philips.cl.di.dev.pa.cpp;

import com.philips.cl.di.dev.pa.datamodel.AirPurifierEventDto;

/**
 * 
 * @author 310124914
 *
 */
public interface ICPDeviceDetailsListener {
	public void onReceivedDeviceDetails(AirPurifierEventDto airPurifierDetails) ;
}
