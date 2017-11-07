package com.philips.platform.catk.mock;

import com.philips.platform.catk.listener.RequestListener;
import com.philips.platform.catk.network.NetworkAbstractModel;
import com.philips.platform.catk.network.NetworkController;

public class NetworkControllerMock  extends NetworkController {
    public NetworkAbstractModel sendConsentRequest_model;
    public RequestListener sendConsentRequest_requestListener;

    @Override
    public void sendConsentRequest(final NetworkAbstractModel model, final RequestListener requestListener) {
        this.sendConsentRequest_model = model;
        this.sendConsentRequest_requestListener = requestListener;
    }

}
