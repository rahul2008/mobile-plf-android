/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.view.EditDeletePopUP;
import com.philips.cdp.uikit.customviews.UIKitRadioButton;
import com.philips.cdp.uikit.drawable.VectorDrawable;

import java.util.List;
import java.util.Locale;

public class AddressSelectionAdapter extends RecyclerView.Adapter<AddressSelectionHolder> {
    private final static String NEW_LINE = "\n";

    private Context mContext;
    private List<Addresses> mAddresses;

    private EditDeletePopUP mPopUP;
    private Addresses mSelectedAddress;
    private int mSelectedIndex;
    private Drawable mOptionsDrawable;

    private int mOptionsClickPosition = -1;

    public AddressSelectionAdapter(final Context context, final List<Addresses> addresses) {
        mContext = context;
        mAddresses = addresses;
        mSelectedIndex = 0;
        initOptionsDrawable();
    }

    void initOptionsDrawable() {
        mOptionsDrawable = VectorDrawable.create(mContext, R.drawable.iap_options_icon_5x17);
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

        //Handle the last item delete scenario
        handleLastItemDeletion(position);

        //Update payment options buttons
        updatePaymentButtonsVisiblity(holder.paymentOptions, position);

        //bind options: edit, delete menu
        bindOptionsButton(holder.optionLayout, position);

        //bind toggle button
        setToggleStatus(holder.toggle, position);
        bindToggleButton(holder, holder.toggle);

        //bind deliver to address
        bindDeliverToAddress(holder.deliver, position);

        //bind add new address
        bindNewAddress(holder.newAddress, position);
    }

    private void handleLastItemDeletion(int position) {
        if (mSelectedIndex > mAddresses.size() - 1 && isLastItem(position)) {
            mSelectedIndex = mAddresses.size() - 1;
        }
    }

    private boolean isLastItem(int position) {
        return position == (mAddresses.size() - 1);
    }

    private void bindNewAddress(final Button newAddress, final int position) {
        newAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                EventHelper.getInstance().notifyEventOccurred(IAPConstant.SHIPPING_ADDRESS_FRAGMENT);
            }
        });
    }

    private void bindDeliverToAddress(Button deliver, final int position) {
        deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Addresses addr = mAddresses.get(position);
                EventHelper.getInstance().notifyEventOccurred(IAPConstant.ADD_DELIVERY_ADDRESS);
            }
        });
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
                notifyDataSetChanged();
            }
        });
    }

    public int getSelectedPosition() {
        return mSelectedIndex;
    }

    public int getOptionsClickPosition() {
        return mOptionsClickPosition;
    }

    String createAddress(final Addresses address) {
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

    private void bindOptionsButton(View view, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mOptionsClickPosition = position;
                boolean disableDelete = false;
                if (mAddresses.size() == 1) {
                    disableDelete = true;
                }
                mPopUP = new EditDeletePopUP(mContext, v, disableDelete);
                mPopUP.show();
            }
        });
    }

    public void setAddresses(final List<Addresses> data) {
        mAddresses = data;
    }
}