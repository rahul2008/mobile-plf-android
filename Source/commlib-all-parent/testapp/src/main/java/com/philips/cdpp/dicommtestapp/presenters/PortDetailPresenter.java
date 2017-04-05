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
import com.philips.cdpp.dicommtestapp.appliance.property.Property;

import nl.rwslinkman.presentable.Presenter;

public class PortDetailPresenter implements Presenter<Property, PortDetailPresenter.ViewHolder> {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        // Inflate your custom XML layout representing a list item in the RecyclerView
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_port_property, parent, false);

        ViewHolder vh = new ViewHolder(v);
        vh.titleView = (TextView) v.findViewById(R.id.item_port_property_title);
        vh.subtitleView = (TextView) v.findViewById(R.id.item_port_property_subtitle);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Property item)
    {
        String title = String.format("%s (%s)", item.getName(), item.getType().name().toLowerCase());
        viewHolder.titleView.setText(title);
        viewHolder.subtitleView.setText(item.getValueText());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleView;
        TextView subtitleView;

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
