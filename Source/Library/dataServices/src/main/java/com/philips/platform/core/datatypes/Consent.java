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
    String SMART_EAR_THERMOMETER_DEVICE = "philipsEarThermometer";
    String SMART_BABY_MONITOR = "smartBabyMonitor";

    String getCreatorId();

    void setDateTime(@NonNull DateTime dateTime);

    Collection<? extends ConsentDetail> getConsentDetails();

    void addConsentDetails(ConsentDetail consentDetail);

    boolean isSynchronized();

    void setBackEndSynchronized(boolean backEndSynchronized);
}
