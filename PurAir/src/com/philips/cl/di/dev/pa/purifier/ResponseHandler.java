package com.philips.cl.di.dev.pa.purifier;

import com.philips.cl.di.dicomm.communication.Error;

public interface ResponseHandler {
	
	void onSuccess(String data);
	void onError(Error error);
}
