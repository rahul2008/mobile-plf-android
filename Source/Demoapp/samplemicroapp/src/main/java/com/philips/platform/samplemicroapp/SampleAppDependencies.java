package com.philips.platform.samplemicroapp;


import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;


public class SampleAppDependencies extends UappDependencies {


    private Context context;

    public SampleAppDependencies(final AppInfraInterface appInfra) {
        super(appInfra);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
