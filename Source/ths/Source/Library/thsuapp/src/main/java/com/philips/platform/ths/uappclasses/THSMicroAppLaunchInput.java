/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.uappclasses;


import com.philips.platform.uappframework.uappinput.UappLaunchInput;

@SuppressWarnings("serial")
public class THSMicroAppLaunchInput extends UappLaunchInput {


    public THSCompletionProtocol getThsCompletionProtocol() {
        return thsCompletionProtocol;
    }
    public boolean isAppointmentFlow() {
        return appointmentFlow;
    }

    private final THSCompletionProtocol thsCompletionProtocol;


    private  boolean  appointmentFlow = false;

    public THSMicroAppLaunchInput(String welcomeMessage, THSCompletionProtocol thsCompletionProtocol) {
        this.thsCompletionProtocol = thsCompletionProtocol;
    }

    public THSMicroAppLaunchInput(String welcomeMessage, THSCompletionProtocol thsCompletionProtocol, boolean appointmentFlow) {
        this.thsCompletionProtocol = thsCompletionProtocol;
        this.appointmentFlow = appointmentFlow;
    }
}
