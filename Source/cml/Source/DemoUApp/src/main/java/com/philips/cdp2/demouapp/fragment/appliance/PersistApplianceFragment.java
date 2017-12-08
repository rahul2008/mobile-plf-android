/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment.appliance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager;
import com.philips.cdp2.commlib.core.appliance.CurrentApplianceManager;
import com.philips.cdp2.demouapp.CommlibUapp;

import static com.philips.cdp2.commlib.demouapp.R.id.cml_buttonForget;
import static com.philips.cdp2.commlib.demouapp.R.id.cml_buttonPersist;
import static com.philips.cdp2.commlib.demouapp.R.layout.cml_fragment_persistence;
import static com.philips.cdp2.commlib.demouapp.R.string.cml_forget_failed;
import static com.philips.cdp2.commlib.demouapp.R.string.cml_forget_success;
import static com.philips.cdp2.commlib.demouapp.R.string.cml_persist_failed;
import static com.philips.cdp2.commlib.demouapp.R.string.cml_persist_success;
import static com.philips.cdp2.demouapp.util.UiUtils.showIndefiniteMessage;
import static com.philips.cdp2.demouapp.util.UiUtils.showMessage;

public class PersistApplianceFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(cml_fragment_persistence, container, false);

        final Appliance currentAppliance = CurrentApplianceManager.getInstance().getCurrentAppliance();
        final ApplianceManager manager = CommlibUapp.get().getDependencies().getCommCentral().getApplianceManager();
        final View activityRoot = getActivity().findViewById(android.R.id.content);

        if (currentAppliance == null) return null;

        rootView.findViewById(cml_buttonPersist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manager.storeAppliance(currentAppliance)) {
                    showMessage(getActivity(), activityRoot, getString(cml_persist_success));
                } else {
                    showIndefiniteMessage(getActivity(), activityRoot, getString(cml_persist_failed));
                }
            }
        });

        rootView.findViewById(cml_buttonForget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manager.forgetStoredAppliance(currentAppliance)) {
                    showMessage(getActivity(), activityRoot, getString(cml_forget_success));
                } else {
                    showIndefiniteMessage(getActivity(), activityRoot, getString(cml_forget_failed));
                }
            }
        });

        return rootView;
    }
}
