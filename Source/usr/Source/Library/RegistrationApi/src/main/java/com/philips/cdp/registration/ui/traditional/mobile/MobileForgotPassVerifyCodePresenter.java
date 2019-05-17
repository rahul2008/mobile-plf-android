package com.philips.cdp.registration.ui.traditional.mobile;

import android.support.annotation.VisibleForTesting;

import com.philips.cdp.registration.app.infra.ServiceDiscoveryWrapper;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.events.NetworkStateListener;
import com.philips.cdp.registration.settings.RegistrationHelper;

import javax.inject.Inject;

public class MobileForgotPassVerifyCodePresenter implements NetworkStateListener {

    @Inject
    ServiceDiscoveryWrapper serviceDiscoveryWrapper;

    private final MobileForgotPassVerifyCodeContract mobileVerifyCodeContract;

    public MobileForgotPassVerifyCodePresenter(MobileForgotPassVerifyCodeContract
                                                       mobileVerifyCodeContract) {
        RegistrationConfiguration.getInstance().getComponent().inject(this);
        this.mobileVerifyCodeContract = mobileVerifyCodeContract;
        RegistrationHelper.getInstance().registerNetworkStateListener(this);

    }


    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        if (isOnline) {
            mobileVerifyCodeContract.netWorkStateOnlineUiHandle();
        } else {
            mobileVerifyCodeContract.netWorkStateOfflineUiHandle();
        }
    }

    @VisibleForTesting
    @Deprecated
    public void mockInjections(ServiceDiscoveryWrapper wrapper) {
        serviceDiscoveryWrapper = wrapper;
    }

}
