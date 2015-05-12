package com.philips.cl.di.dicomm.port;

import com.philips.cdp.dicommclient.request.Error;

public interface DICommPortListener {

	public ListenerRegistration onPortUpdate(DICommPort<?> port);

    public ListenerRegistration onPortError(DICommPort<?> port, Error error, String errorData);

}
