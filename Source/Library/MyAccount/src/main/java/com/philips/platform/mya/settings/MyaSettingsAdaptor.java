/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.settings;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.mya.R;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Label;

import java.util.LinkedHashMap;

class MyaSettingsAdaptor extends RecyclerView.Adapter<MyaSettingsAdaptor.SettingsViewHolder> {

    private LinkedHashMap<String,String> settingsList;
    private View.OnClickListener onClickListener;
    private Context context;
    private String homeCountry;

    void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class SettingsViewHolder extends RecyclerView.ViewHolder {
         Label settingTitle,settingValue;

         SettingsViewHolder(View view) {
            super(view);
            settingTitle = (Label) view.findViewById(R.id.item_title);
            settingValue = (Label) view.findViewById(R.id.second_item);
        }
    }

     MyaSettingsAdaptor(LinkedHashMap<String,String> settingsList, Context context) {
        this.settingsList = settingsList;
         this.context = context;
    }

    @Override
    public SettingsViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        String key = (String) settingsList.keySet().toArray()[viewType];
        View itemView;
        if(key.equals(context.getString(R.string.MYA_Country))) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mya_double_item_layout, parent, false);
        } else{
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
        String title = settingsList.get(key);
        holder.settingTitle.setText(title!=null?title:key);
       /* if(position == 2) {
            holder.settingValue.setText(title!=null?title:key);
        }*/
    }

    @Override
    public int getItemCount() {
        return settingsList.size();
    }

    public void setHomeCountry(String homeCountry) {
        this.homeCountry = homeCountry;
    }
}