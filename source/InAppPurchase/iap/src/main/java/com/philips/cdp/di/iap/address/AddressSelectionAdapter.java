/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.address;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.R;

public class AddressSelectionAdapter extends RecyclerView.Adapter<AddressSelectionHolder> {
    @Override
    public AddressSelectionHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.iap_address_selection_item, null);
        return new AddressSelectionHolder(view);
    }

    @Override
    public void onBindViewHolder(final AddressSelectionHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
