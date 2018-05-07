package com.philips.cdp2.demouapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.philips.cdp2.commlib.core.appliance.Appliance;

public class ApplianceFragmentFactory {

    public static final String APPLIANCE_KEY = "APPLIANCE_KEY";

    @Nullable
    public static <T extends Fragment> T newInstance(Class<T> clazz, @NonNull Appliance appliance) {
        T applianceFragment = null;
        try {
            applianceFragment = clazz.newInstance();
            final Bundle bundle = new Bundle();
            final String cppId = appliance.getNetworkNode().getCppId();
            bundle.putString(APPLIANCE_KEY, cppId);
            applianceFragment.setArguments(bundle);
        } catch (InstantiationException | IllegalAccessException e) {
            //don't care
        }

        return applianceFragment;
    }
}
