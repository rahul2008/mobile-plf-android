package com.philips.cdp.registration.ui.traditional.mobile;

import android.support.annotation.*;

import com.philips.cdp.registration.app.infra.*;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.events.*;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.utils.*;

import javax.inject.*;

import io.reactivex.disposables.*;

public class MobileForgotPassVerifyCodePresenter implements NetworkStateListener {

    @Inject
    ServiceDiscoveryWrapper serviceDiscoveryWrapper;

    private final MobileForgotPassVerifyCodeContract mobileVerifyCodeContract;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MobileForgotPassVerifyCodePresenter(MobileForgotPassVerifyCodeContract
                                                       mobileVerifyCodeContract) {
        RegistrationConfiguration.getInstance().getComponent().inject(this);
        this.mobileVerifyCodeContract = mobileVerifyCodeContract;
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
    }

    public void cleanUp() {
        compositeDisposable.clear();
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
