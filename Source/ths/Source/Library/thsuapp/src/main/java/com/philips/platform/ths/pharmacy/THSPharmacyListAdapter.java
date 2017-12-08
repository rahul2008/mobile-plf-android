/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.pharmacy;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.philips.platform.ths.R;

import java.util.List;

public class THSPharmacyListAdapter extends RecyclerView.Adapter<THSPharmacyListAdapter.THSPharmacyListViewHolder> {

    private List<Pharmacy> pharmacyList;
    private THSOnPharmacyListItemClickListener onPharmacyListItemClickListener;

    public THSPharmacyListAdapter(List<Pharmacy> pharmacyList){
        this.pharmacyList = pharmacyList;
    }


    public void setOnPharmacyItemClickListener(THSOnPharmacyListItemClickListener onPharmacyListItemClickListener) {
        this.onPharmacyListItemClickListener = onPharmacyListItemClickListener;
    }


    public class THSPharmacyListViewHolder extends RecyclerView.ViewHolder {
        public TextView pharmacyName, addressLineOne, addressLineTwo, pharmacyState, pharmacyZipCode;
        public RelativeLayout relativeLayout;

        public THSPharmacyListViewHolder(View view) {
            super(view);

            pharmacyName = (TextView) view.findViewById(R.id.pharmacy_name);
            addressLineOne = (TextView) view.findViewById(R.id.pharmacy_address_line_one);
            addressLineTwo = (TextView) view.findViewById(R.id.pharmacy_address_line_two);
            pharmacyState = (TextView) view.findViewById(R.id.pharmacy_state);
            pharmacyZipCode = (TextView) view.findViewById(R.id.pharmacy_zip_code);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.pharmacy_list_layout);

        }
    }

    @Override
    public THSPharmacyListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ths_pharmacy_list, parent, false);

        return new THSPharmacyListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(THSPharmacyListViewHolder holder, int position) {
        final Pharmacy pharmacy = pharmacyList.get(position);
        holder.pharmacyName.setText(pharmacy.getName());
        holder.addressLineOne.setText(pharmacy.getAddress().getAddress1());
        holder.addressLineTwo.setText(pharmacy.getAddress().getAddress2());
        holder.pharmacyState.setText(pharmacy.getAddress().getState().getName());
        holder.pharmacyZipCode.setText(pharmacy.getAddress().getZipCode());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPharmacyListItemClickListener.onItemClick(pharmacy);
            }
        };

        holder.relativeLayout.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        if(null != pharmacyList){
            return pharmacyList.size();
        }
        else {
            return 0;
        }

    }
}
