/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlibexplorer.presenters;

import com.philips.cdp2.commlibexplorer.appliance.GenericAppliance;

public class DiscoveredDevicePresenter extends TwoLinePresenter<GenericAppliance> {

    @Override
    public void onBindViewHolder(TwoLinePresenter.ViewHolder viewHolder, GenericAppliance item) {
        String title = item.getName() == null ? "[Unnnamed device]" : item.getDeviceName();
        String subtitle = item.getNetworkNode().getIpAddress() == null ? "[Address unknown]" : item.getModelId();

        viewHolder.titleView.setText(title);
        viewHolder.subtitleView.setText(subtitle);
    }
}
