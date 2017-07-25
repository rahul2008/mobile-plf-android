package com.philips.platform.ths.appointment;

import com.philips.platform.ths.providerslist.THSProviderInfo;

import java.util.Date;

public interface THSAppointmentInterface {
    THSProviderInfo getTHSProviderInfo();
    Date getAppointmentDate();
}
