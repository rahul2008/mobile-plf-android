package com.philips.platform.core.datatypes;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Collection;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface Consent extends BaseAppData, DateData, Serializable {

    String DEFAULT_DOCUMENT_VERSION = "draft";
    String DEFAULT_DEVICE_IDENTIFICATION_NUMBER = "manual";
    //TODO: Spoorti: Please remove unused variable
    String SMART_EAR_THERMOMETER_DEVICE = "philipsEarThermometer";
    //TODO: Spoorti: Why is it used? Can we remove it?

    String SMART_BABY_MONITOR = "smartBabyMonitor";

    String getCreatorId();

    //TODO: Spoorti: Use this API for storing DateTime
    void setDateTime(@NonNull DateTime dateTime);

    Collection<? extends ConsentDetail> getConsentDetails();

    void addConsentDetails(ConsentDetail consentDetail);

    //TODO: Spoorti: Use this API for checking isSynced or not
    boolean isSynchronized();

    void setBackEndSynchronized(boolean backEndSynchronized);
}
