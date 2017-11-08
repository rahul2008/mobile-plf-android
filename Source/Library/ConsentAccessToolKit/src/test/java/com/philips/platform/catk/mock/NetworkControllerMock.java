package com.philips.platform.catk.mock;

import com.philips.platform.catk.listener.RequestListener;
import com.philips.platform.catk.network.NetworkAbstractModel;
import com.philips.platform.catk.network.NetworkController;

public class NetworkControllerMock  extends NetworkController {
    public NetworkAbstractModel sendConsentRequest_model;

    @Override
    public void sendConsentRequest(final NetworkAbstractModel model) {
        this.sendConsentRequest_model = model;
    }

}
