package com.philips.cdp.dicommclient.port;

import com.philips.cdp.dicommclient.request.Error;

public interface DICommPortListener {

	public void onPortUpdate(DICommPort<?> port);

    public void onPortError(DICommPort<?> port, Error error, String errorData);

}
