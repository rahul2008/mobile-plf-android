/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.address;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.response.addresses.Addresses;

import java.util.List;
import java.util.Locale;

public class AddressSelectionAdapter extends RecyclerView.Adapter<AddressSelectionHolder> {
    private final static String NEW_LINE = "\n";
    private Context mContext;
    private List<Addresses> mAddresses;


    public AddressSelectionAdapter(final Context context, final List<Addresses> addresses) {
        mContext = context;
        mAddresses = addresses;
    }

    @Override
    public AddressSelectionHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.iap_address_selection_item, null);
        return new AddressSelectionHolder(view);
    }

    @Override
    public void onBindViewHolder(final AddressSelectionHolder holder, final int position) {
        Addresses address= mAddresses.get(position);
        holder.name.setText(address.getFirstName() +" " +address.getLastName());
        createAddress(address);
    }

    private String createAddress(final Addresses address) {
        StringBuilder sb = new StringBuilder();
        String line1 = address.getLine1();
        String line2 = address.getLine2();
        String country = getCountryName(address.getCountry().getIsocode());
        if (line1 != null) {
            sb.append(line1).append(NEW_LINE);
        }
        if (line2 != null) {
            sb.append(line2).append(NEW_LINE);
        }
        if (country != null) {
            sb.append(line2);
        }
        return sb.toString();
    }

    private String getCountryName(String isoCode) {
        return new Locale(isoCode).getDisplayCountry();
    }

    @Override
    public int getItemCount() {
        return mAddresses.size();
    }
}