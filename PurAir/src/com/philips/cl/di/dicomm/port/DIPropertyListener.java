package com.philips.cl.di.dicomm.port;

import com.philips.cl.di.dicomm.communication.Error;

public interface DIPropertyListener {

	public DIRegistration handlePropertyUpdateForPort(DICommPort<?> port);

    public DIRegistration handleErrorForPort(DICommPort<?> port, Error error, String errorData);

}
