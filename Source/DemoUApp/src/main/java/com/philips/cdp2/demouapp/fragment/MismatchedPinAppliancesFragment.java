/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.CurrentApplianceManager;
import com.philips.cdp2.commlib.demouapp.R;
import com.philips.cdp2.demouapp.CommlibUapp;
import com.philips.cdp2.demouapp.appliance.ApplianceAdapter;

import static com.philips.cdp2.commlib.lan.context.LanTransportContext.findAppliancesWithMismatchedPinIn;

public class MismatchedPinAppliancesFragment extends Fragment {

    private CommCentral commCentral;

    private ApplianceAdapter applianceAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.cml_fragment_mismatched_pin_appliances, container, false);

        commCentral = CommlibUapp.get().getDependencies().getCommCentral();
        applianceAdapter = new ApplianceAdapter(getContext());

        final ListView listViewAppliances = (ListView) rootview.findViewById(R.id.cml_listViewAppliances);
        listViewAppliances.setAdapter(applianceAdapter);
        listViewAppliances.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                final Appliance appliance = applianceAdapter.getItem(position);
                CurrentApplianceManager.getInstance().setCurrentAppliance(appliance);

                CommlibUapp.get().nextFragment(new ApplianceFragment());
            }
        });

        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();

        refresh();
    }

    public static MismatchedPinAppliancesFragment newInstance() {
        return new MismatchedPinAppliancesFragment();
    }

    public void refresh() {
        applianceAdapter.clear();
        applianceAdapter.addAll(findAppliancesWithMismatchedPinIn(commCentral.getApplianceManager().getAvailableAppliances()));
    }
}
