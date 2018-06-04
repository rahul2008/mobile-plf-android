package com.philips.platform.mya.csw.justintime.spy;

import com.philips.platform.mya.csw.justintime.JustInTimeWidgetHandler;

public class JustInTimeWidgetHandlerSpy implements JustInTimeWidgetHandler {
    public boolean consentGiven;
    public boolean consentRejected;

    @Override
    public void onConsentGiven() {
        this.consentGiven = true;
    }

    @Override
    public void onConsentRejected() {
        this.consentRejected = true;
    }
}
