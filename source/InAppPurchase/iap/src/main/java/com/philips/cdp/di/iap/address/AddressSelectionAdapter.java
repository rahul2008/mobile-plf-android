/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.address;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.view.EditDeletePopUP;
import com.philips.cdp.uikit.customviews.UIKitRadioButton;
import com.philips.cdp.uikit.drawable.VectorDrawable;

import java.util.List;
import java.util.Locale;

public class AddressSelectionAdapter extends RecyclerView.Adapter<AddressSelectionHolder> implements EventListener {
    private final static String NEW_LINE = "\n";
    private Context mContext;
    private List<Addresses> mAddresses;

    private EditDeletePopUP mPopUP;
    private Addresses mSelectedAddress;
    private int mSelectedIndex;
    private Drawable mOptionsDrawable;

    public AddressSelectionAdapter(final Context context, final List<Addresses> addresses) {
        mContext = context;
        mAddresses = addresses;
        mOptionsDrawable = VectorDrawable.create(context, R.drawable.iap_options_icon_5x17);
        mSelectedIndex = 0;
        setSelectedAddress(0);
        EventHelper.getInstance().registerEventNotification(EditDeletePopUP.EVENT_EDIT, this);
        EventHelper.getInstance().registerEventNotification(EditDeletePopUP.EVENT_EDIT, this);
    }

    @Override
    public AddressSelectionHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.iap_address_selection_item, null);
        return new AddressSelectionHolder(view);
    }

    @Override
    public int getItemCount() {
        return mAddresses.size();
    }

    @Override
    public void onBindViewHolder(final AddressSelectionHolder holder, final int position) {
        Addresses address = mAddresses.get(position);
        holder.name.setText(address.getFirstName() + " " + address.getLastName());
        holder.address.setText(createAddress(address));
        holder.options.setImageDrawable(mOptionsDrawable);

        //Update payment options buttons
        updatePaymentButtonsVisiblity(holder.paymentOptions, position);

        //bind options: edit, delete menu
        bindOptionsButton(holder.options);

        //bind toggle button
        setToggleStatus(holder.toggle, position);
        bindToggleButton(holder, holder.toggle);
    }

    @Override
    public void raiseEvent(final String event) {

    }

    @Override
    public void onEventReceived(final String event) {
        if (!TextUtils.isEmpty(event)) {
            if (EditDeletePopUP.EVENT_EDIT.equals(event)) {

            } else if (EditDeletePopUP.EVENT_DELETE.equals(event)) {

            }
        }
    }

    public void onStop() {
        if (mPopUP != null && mPopUP.isShowing()) {
            mPopUP.dismiss();
        }
    }

    private void updatePaymentButtonsVisiblity(final ViewGroup paymentOptions, final int position) {
        if (mSelectedIndex == position) {
            paymentOptions.setVisibility(View.VISIBLE);
        } else {
            paymentOptions.setVisibility(View.GONE);
        }
    }

    private void setToggleStatus(final UIKitRadioButton toggle, final int position) {
        if (mSelectedIndex == position) {
            toggle.setChecked(true);
        } else {
            toggle.setChecked(false);
        }
    }

    private void bindToggleButton(final AddressSelectionHolder holder, final UIKitRadioButton toggle) {
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mSelectedIndex = holder.getAdapterPosition();
                setSelectedAddress(mSelectedIndex);
            }
        });
    }

    private void setSelectedAddress(int position) {
        if (mAddresses.size() > 0 && position < mAddresses.size()) {
            mSelectedAddress = mAddresses.get(position);
        }
    }

    private String createAddress(final Addresses address) {
        StringBuilder sb = new StringBuilder();

        appendAddressWithNewLineIfNotNull(sb, address.getLine1());
        appendAddressWithNewLineIfNotNull(sb, address.getLine2());
        appendAddressWithNewLineIfNotNull(sb, address.getTown());
        appendAddressWithNewLineIfNotNull(sb, address.getPostalCode());

        //Don't add new line for last entry
        String country = getCountryName(address.getCountry().getIsocode());
        if (country != null) {
            sb.append(country);
        }
        return sb.toString();
    }

    private void appendAddressWithNewLineIfNotNull(StringBuilder sb, String code) {
        if (!TextUtils.isEmpty(code)) {
            sb.append(code).append(NEW_LINE);
        }
    }

    private String getCountryName(String isoCode) {
        return new Locale(Locale.getDefault().toString(), isoCode).getDisplayCountry();
    }

    private void bindOptionsButton(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mPopUP = new EditDeletePopUP(mContext, v);
                mPopUP.show();
            }
        });
    }
}