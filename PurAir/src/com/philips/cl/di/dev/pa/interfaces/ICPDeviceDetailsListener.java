package com.philips.cl.di.dev.pa.interfaces;

import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;

/**
 * 
 * @author 310124914
 *
 */
public interface ICPDeviceDetailsListener {
	public void onReceivedDeviceDetails(AirPurifierEventDto airPurifierDetails) ;
}
