/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlibexplorer.presenters;

import com.philips.cdp2.commlibexplorer.appliance.property.Property;

public class PortDetailPresenter extends TwoLinePresenter<Property> {

    @Override
    public void onBindViewHolder(TwoLinePresenter.ViewHolder viewHolder, Property item) {
        String title = String.format("%s (%s)", item.getName(), item.getType().name().toLowerCase());
        viewHolder.titleView.setText(title);
        viewHolder.subtitleView.setText(item.getValueText());
    }
}
