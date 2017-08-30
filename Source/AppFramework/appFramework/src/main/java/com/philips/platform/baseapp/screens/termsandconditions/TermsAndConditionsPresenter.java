/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.termsandconditions;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;

import java.net.URL;


/**
 * Created by philips on 25/07/17.
 */

public class TermsAndConditionsPresenter implements TermsAndConditionsContract.Action {

    private TermsAndConditionsContract.View viewListener;

    private Context context;

    protected static final String TERMS_AND_CONDITIONS = "app.termsandconditions";

    protected static final String PRIVACY = "app.privacynotice";

    public TermsAndConditionsPresenter(TermsAndConditionsContract.View viewListener, Context context) {
        this.viewListener = viewListener;
        this.context = context;
    }

    @Override
    public void loadTermsAndConditionsUrl(TermsAndPrivacyStateData.TermsAndPrivacyEnum state) {
        AppInfraInterface appInfra = ((AppFrameworkApplication) context.getApplicationContext()).getAppInfra();
        ServiceDiscoveryInterface serviceDiscoveryInterface = appInfra.getServiceDiscovery();
        String value;
        if (state== TermsAndPrivacyStateData.TermsAndPrivacyEnum.PRIVACY_CLICKED) {
            value = PRIVACY;
        } else {
            value = TERMS_AND_CONDITIONS;
        }

        serviceDiscoveryInterface.getServiceUrlWithCountryPreference(value, new
                ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES errorvalues, String errorText) {
                        viewListener.onUrlLoadError(errorText);
                    }

                    @Override
                    public void onSuccess(URL url) {
                        if (url == null) {
                            viewListener.onUrlLoadError("Empty Url from Service discovery");
                        } else {
                            viewListener.updateUiOnUrlLoaded(url.toString());

                        }
                    }
                });
    }
}
