/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.uappclasses;


import com.philips.platform.uappframework.uappinput.UappLaunchInput;

public class THSMicroAppLaunchInput extends UappLaunchInput {


    private String welcomeMessage;


    public THSMicroAppLaunchInput(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }
}
