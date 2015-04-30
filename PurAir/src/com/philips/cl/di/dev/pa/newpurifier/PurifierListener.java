package com.philips.cl.di.dev.pa.newpurifier;

import com.philips.cl.di.dicomm.communication.Error;

public interface PurifierListener {

	void notifyAirPurifierEventListeners();

	void notifyFirmwareEventListeners();

	void notifyAirPurifierEventListenersErrorOccurred(
			Error purifierEventError);

}