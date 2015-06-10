package com.philips.cdp.dicommclient.port;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.util.ListenerRegistration;

public interface DICommPortListener {

	public ListenerRegistration onPortUpdate(DICommPort<?> port);

    public ListenerRegistration onPortError(DICommPort<?> port, Error error, String errorData);

}
