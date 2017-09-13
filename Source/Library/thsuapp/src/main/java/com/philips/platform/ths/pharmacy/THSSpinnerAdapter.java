/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.pharmacy;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.americanwell.sdk.entity.State;
import com.philips.platform.ths.R;
import com.philips.platform.uid.view.widget.Label;

import java.util.List;


public class THSSpinnerAdapter extends ArrayAdapter<Integer> {

    private Context context;
    private Label pharmacyState;
    private List<State> stateList;
    private LayoutInflater inflator;

    public THSSpinnerAdapter(@NonNull Context context, @LayoutRes int resource, List<State> stateList) {
        super(context, resource);
        this.context = context;
        this.stateList = stateList;
        inflator = (LayoutInflater.from(context));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position,convertView, parent);
    }

    @NonNull
    protected View getCustomView(int position,@Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = inflator.inflate(R.layout.ths_pharmacy_spinner_layout, null);
        pharmacyState = (Label) convertView.findViewById(R.id.pharmacy_state_list_item);
        pharmacyState.setText(stateList.get(position).getName());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position,convertView, parent);
    }

    @Override
    public int getCount() {
        return stateList.size();
    }
}
