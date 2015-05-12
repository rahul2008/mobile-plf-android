package com.philips.cl.di.dicomm.port;

import com.philips.cdp.dicommclient.request.Error;

public interface DIPortListener {

	public DIRegistration onPortUpdate(DICommPort<?> port);

    public DIRegistration onPortError(DICommPort<?> port, Error error, String errorData);

}
