/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.settings;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.mya.R;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Label;

import java.util.LinkedHashMap;

class MyaSettingsAdaptor extends RecyclerView.Adapter<MyaSettingsAdaptor.SettingsViewHolder> {

    private LinkedHashMap<String, SettingsModel> settingsList;
    private View.OnClickListener onClickListener;

    MyaSettingsAdaptor(LinkedHashMap<String, SettingsModel> settingsList) {
        this.settingsList = settingsList;
    }

    void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class SettingsViewHolder extends RecyclerView.ViewHolder {
        Label settingTitle, settingValue;

        SettingsViewHolder(View view) {
            super(view);
            settingTitle = view.findViewById(R.id.item_title);
            settingValue = view.findViewById(R.id.second_item);
        }
    }

    @Override
    public SettingsViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        int position = getItemViewType(viewType);
        String key = (String) settingsList.keySet().toArray()[position];
        View itemView;
        SettingsModel settingsModel = settingsList.get(key);
        if (settingsModel.getItemCount() == 2) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mya_double_item_layout, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mya_single_item_layout, parent, false);
        }
        UIDHelper.injectCalligraphyFonts();
        itemView.setOnClickListener(onClickListener);
        return new SettingsViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(SettingsViewHolder holder, int position) {
        String key = (String) settingsList.keySet().toArray()[position];
        SettingsModel settingsModel = settingsList.get(key);
        holder.settingTitle.setText((settingsModel!=null && settingsModel.getFirstItem() != null) ? settingsModel.getFirstItem() : key);
        if (holder.settingValue != null && settingsModel != null && settingsModel.getItemCount() == 2) {
            holder.settingValue.setText(settingsModel.getSecondItem());
        }
    }

    @Override
    public int getItemCount() {
        return settingsList.size();
    }
}