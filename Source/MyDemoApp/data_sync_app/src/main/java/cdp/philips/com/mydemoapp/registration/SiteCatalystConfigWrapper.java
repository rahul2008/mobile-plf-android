package cdp.philips.com.mydemoapp.registration;

import android.content.Context;

import com.adobe.mobile.Config;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SiteCatalystConfigWrapper {

    @Inject
    public SiteCatalystConfigWrapper() {
    }

    public void setContext(Context applicationContext) {
        Config.setContext(applicationContext);
    }
}
