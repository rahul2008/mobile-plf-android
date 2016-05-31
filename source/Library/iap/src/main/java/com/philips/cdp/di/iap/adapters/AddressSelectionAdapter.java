/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.di.iap.view.EditDeletePopUP;
import com.philips.cdp.uikit.customviews.UIKitRadioButton;
import com.philips.cdp.uikit.drawable.VectorDrawable;

import java.util.List;

public class AddressSelectionAdapter extends RecyclerView.Adapter<AddressSelectionAdapter.AddressSelectionHolder> {
    private Context mContext;
    private List<Addresses> mAddresses;

    private EditDeletePopUP mPopUP;
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
        holder.address.setText(Utility.createAddress(address));
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
        bindDeliverToAddress(holder.deliver);

        //bind add new address
        bindNewAddress(holder.newAddress);
    }

    private void handleLastItemDeletion(int position) {
        if (mSelectedIndex > mAddresses.size() - 1 && isLastItem(position)) {
            mSelectedIndex = mAddresses.size() - 1;
        }
    }

    private boolean isLastItem(int position) {
        return position == (mAddresses.size() - 1);
    }

    private void bindNewAddress(final Button newAddress) {
        newAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                EventHelper.getInstance().notifyEventOccurred(IAPConstant.SHIPPING_ADDRESS_FRAGMENT);
            }
        });
    }

    private void bindDeliverToAddress(Button deliver) {
        deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
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

    public class AddressSelectionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView address;
        UIKitRadioButton toggle;
        ImageView options;
        Button deliver;
        Button newAddress;
        ViewGroup paymentOptions;
        ViewGroup optionLayout;
        RelativeLayout addressItem;

        public AddressSelectionHolder(final View view) {
            super(view);
            addressItem = (RelativeLayout) view.findViewById(R.id.address_item);
            name = (TextView) view.findViewById(R.id.tv_name);
            address = (TextView) view.findViewById(R.id.tv_address);
            toggle = (UIKitRadioButton) view.findViewById(R.id.rbtn_toggle);
            options = (ImageView) view.findViewById(R.id.img_options);
            paymentOptions = (ViewGroup) view.findViewById(R.id.payment_options);
            deliver = (Button) view.findViewById(R.id.btn_deliver);
            newAddress = (Button) view.findViewById(R.id.btn_new_address);
            optionLayout = (ViewGroup) view.findViewById(R.id.options_layout);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mSelectedIndex = getAdapterPosition();
            setToggleStatus(toggle, getAdapterPosition());
            notifyDataSetChanged();
        }
    }
}