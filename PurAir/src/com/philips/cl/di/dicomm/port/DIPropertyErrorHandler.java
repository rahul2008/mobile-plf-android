package com.philips.cl.di.dicomm.port;
import com.philips.cl.di.dicomm.communication.Error;

public abstract class DIPropertyErrorHandler {

	private boolean isEnabled;

	public DIPropertyErrorHandler() {
		isEnabled = true;
	}

	// Hide this method for subclasses
	void handleErrorForPortIfEnabled(DICommPort<?> port, Error error) {
		if (isEnabled) {
			handleErrorForPort(port, error);
		}
	}

	public abstract void handleErrorForPort(DICommPort<?> port, Error error);

}
