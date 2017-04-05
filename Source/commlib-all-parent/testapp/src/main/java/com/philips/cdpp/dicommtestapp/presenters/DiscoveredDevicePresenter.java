/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdpp.dicommtestapp.presenters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cdpp.dicommtestapp.R;
import com.philips.cdpp.dicommtestapp.appliance.GenericAppliance;

import nl.rwslinkman.presentable.Presenter;

public class DiscoveredDevicePresenter implements Presenter<GenericAppliance, DiscoveredDevicePresenter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_discovered_device, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.titleView = (TextView) v.findViewById(R.id.item_discovered_title);
        viewHolder.subtitleView = (TextView) v.findViewById(R.id.item_discovered_subtitle);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, GenericAppliance item) {
        String title = item.getName() == null ? "[Unnnamed device]" : item.getDeviceName();
        String subtitle = item.getNetworkNode().getIpAddress() == null ? "[Address unknown]" : item.getModelNumber();

        viewHolder.titleView.setText(title);
        viewHolder.subtitleView.setText(subtitle);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView subtitleView;

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
