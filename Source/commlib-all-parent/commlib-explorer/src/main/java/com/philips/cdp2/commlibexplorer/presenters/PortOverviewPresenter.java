/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlibexplorer.presenters;

import android.view.ViewGroup;

import com.philips.cdp2.commlibexplorer.appliance.SupportedPort;

import static android.support.v4.content.ContextCompat.getColor;

public class PortOverviewPresenter extends TwoLinePresenter<SupportedPort> {
    private int redColor;
    private int blackColor;


    @Override
    public TwoLinePresenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        redColor = getColor(parent.getContext(), android.R.color.holo_red_dark);
        blackColor = getColor(parent.getContext(), android.R.color.black);

        return super.onCreateViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(TwoLinePresenter.ViewHolder viewHolder, SupportedPort item) {
        String subTitle = "Loading...";
        int textColor = blackColor;
        if (!item.getErrorText().isEmpty()) {
            subTitle = item.getErrorText();
            textColor = redColor;
        } else if (!item.getStatusText().isEmpty()) {
            subTitle = item.getStatusText();
            textColor = blackColor;
        }

        viewHolder.titleView.setText(item.getPortName());

        viewHolder.subtitleView.setTextColor(textColor);
        viewHolder.subtitleView.setText(subTitle);
    }
}
