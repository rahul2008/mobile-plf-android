package com.philips.platform.pim.integration;

import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import java.util.ArrayList;

public class PIMLaunchInput extends UappLaunchInput {
    private ArrayList<String> pimCustomClaims;
    private ArrayList<String> pimScopes;

    public ArrayList<String> getPimCustomClaims() {
        return pimCustomClaims;
    }

    public void setPimCustomClaims(ArrayList<String> pimCustomClaims) {
        this.pimCustomClaims = pimCustomClaims;
    }

    public ArrayList<String> getPimScopes() {
        return pimScopes;
    }

    public void setPimScopes(ArrayList<String> pimScopes) {
        this.pimScopes = pimScopes;
    }


}
