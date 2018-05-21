package com.philips.platform.pif.chi;

/**
 * Created by abhishek on 5/21/18.
 */

public interface ConsentChangeListener {
    void onConsentChanged(String consentType, boolean status);
}
