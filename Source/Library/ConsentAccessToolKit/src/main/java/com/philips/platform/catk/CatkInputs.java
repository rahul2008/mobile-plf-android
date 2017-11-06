package com.philips.platform.catk;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

/**
 * This class is used to provide input parameters and customizations for Consent access tool kit.
 */

public class CatkInputs {

    private AppInfraInterface appInfra;


    private Context context;

    private String propositionName;

    private String applicationName;


    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
    
    public AppInfraInterface getAppInfra() {
        return appInfra;
    }


    public String getPropositionName() {
        return propositionName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setPropositionName(String propositionName) {
        this.propositionName = propositionName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setAppInfra(AppInfraInterface appInfra) {
        this.appInfra = appInfra;
    }
}
