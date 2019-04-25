package com.philips.platform.pim;

import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import java.util.ArrayList;
import java.util.HashMap;

public class PIMLaunchInput extends UappLaunchInput {
    private HashMap<String, String> pimCustomClaims;
    private ArrayList<String> pimScopes;

    public HashMap<String, String> getPimCustomClaims() {
        return pimCustomClaims;
    }

    public void setPimCustomClaims(HashMap<String, String> pimCustomClaims) {
        this.pimCustomClaims = pimCustomClaims;
    }

    public ArrayList<String> getPimScopes() {
        return pimScopes;
    }

    public void setPimScopes(ArrayList<String> pimScopes) {
        this.pimScopes = pimScopes;
    }


}
