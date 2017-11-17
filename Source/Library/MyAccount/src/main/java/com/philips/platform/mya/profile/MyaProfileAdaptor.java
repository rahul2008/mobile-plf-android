/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.profile;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.mya.R;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Label;

import java.util.List;

class MyaProfileAdaptor extends RecyclerView.Adapter<MyaProfileAdaptor.ProfileViewHolder> {

    private List<String> profileList;
    private View.OnClickListener onClickListener;

    void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class ProfileViewHolder extends RecyclerView.ViewHolder {
         Label profileTitle;

         ProfileViewHolder(View view) {
            super(view);
            profileTitle = (Label) view.findViewById(R.id.profile_title);
        }
    }

     MyaProfileAdaptor(List<String> profileList) {
        this.profileList = profileList;
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mya_profile_adaptor, parent, false);
        UIDHelper.injectCalligraphyFonts();
        itemView.setOnClickListener(onClickListener);
        return new ProfileViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        String title = profileList.get(position);
        holder.profileTitle.setText(title);
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }


}