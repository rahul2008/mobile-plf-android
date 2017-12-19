package com.philips.platform.mya.usr;

import com.philips.platform.consenthandlerinterface.ConsentHandlerInterface;
import com.philips.platform.consenthandlerinterface.ConsentListCallback;
import com.philips.platform.consenthandlerinterface.CreateConsentCallback;
import com.philips.platform.consenthandlerinterface.datamodel.ConsentDefinition;

/**
 * Created by Entreco on 19/12/2017.
 */

public class MarketingConsentHandler implements ConsentHandlerInterface {

    @Override
    public void checkConsents(ConsentListCallback callback) {

    }

    @Override
    public void post(ConsentDefinition definition, boolean status, CreateConsentCallback callback) {

    }
}
