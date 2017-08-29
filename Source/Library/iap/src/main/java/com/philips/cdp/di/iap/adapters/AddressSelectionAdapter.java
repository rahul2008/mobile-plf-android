/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.di.iap.view.EditDeletePopUP;
import com.philips.platform.uid.view.widget.RadioButton;

import java.util.List;

public class AddressSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Addresses> mAddresses;

    private EditDeletePopUP mPopUP;
    private int mSelectedIndex=1; //As Oth position is taken by header
    //private Drawable mOptionsDrawable;

    private int mOptionsClickPosition = -1;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;


    public AddressSelectionAdapter(final Context context, final List<Addresses> addresses) {
        mContext = context;
        addresses.add(0,null);
        mAddresses = addresses;
        mSelectedIndex = 1; //As Oth position is taken by header
        initOptionsDrawable();
    }

    private void initOptionsDrawable() {
        // mOptionsDrawable = Drawable.create(mContext, R.drawable.iap_options_icon_5x17);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {

            case TYPE_HEADER:

                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iap_address_selection_header, parent, false);
                return new AddressSelectionHeader(view);

            case TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iap_address_selection_item, parent, false);
                return new AddressSelectionHolder(view);

            case TYPE_FOOTER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iap_address_selection_footer, parent, false);
                return new AddressSelectionFooter(view);

        }
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mAddresses.size()+1;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder == null) return;

        if (holder instanceof AddressSelectionHolder) {
            Addresses address = mAddresses.get(position);
            AddressSelectionHolder addressSelectionHolder = (AddressSelectionHolder) holder;
            addressSelectionHolder.tvToggle.setText(address.getFirstName() + " " + address.getLastName());
            addressSelectionHolder.address.setText(Utility.formatAddress(address.getFormattedAddress() + "\n" + address.getCountry().getName()));
            // holder.options.setImageDrawable(mOptionsDrawable);

            //Update payment options buttons
            updatePaymentButtonsVisibility(addressSelectionHolder.paymentOptions,addressSelectionHolder.delete, position);

            //bind options: edit, delete menu
            // bindOptionsButton(holder.optionLayout, position);

            //bind toggle button
            setToggleStatus(addressSelectionHolder.toggle, position);
            bindToggleButton(addressSelectionHolder, addressSelectionHolder.toggle);

            //bind deliver to address
            bindDeliverToThisAddress(addressSelectionHolder.deliverToThisAddress);

            //bind add new address
            // bindAddNewAddress(holder.addNewAddress);
        }

    }

    private void bindAddNewAddress(final View newAddress) {
        newAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                EventHelper.getInstance().notifyEventOccurred(IAPConstant.ADD_NEW_ADDRESS);
            }
        });
    }

    private void bindDeliverToThisAddress(Button deliver) {
        deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                EventHelper.getInstance().notifyEventOccurred(IAPConstant.DELIVER_TO_THIS_ADDRESS);
            }
        });
    }

    public void onStop() {
        if (mPopUP != null && mPopUP.isShowing()) {
            mPopUP.dismiss();
        }
    }

    private void updatePaymentButtonsVisibility(final ViewGroup paymentOptions,final Button dleteButton, final int position) {
        if (mSelectedIndex == position) {
            paymentOptions.setVisibility(View.VISIBLE);
            if(this.getItemCount()== 2){
                dleteButton.setEnabled(false);
            }
        } else {
            paymentOptions.setVisibility(View.GONE);
        }
    }

    private void setToggleStatus(final RadioButton toggle, final int position) {
        if (mSelectedIndex == position) {
            toggle.setChecked(true);
        } else {
            toggle.setChecked(false);
        }
    }

    private void bindToggleButton(final AddressSelectionHolder holder, final RadioButton toggle) {
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
        mSelectedIndex = 0;
        data.add(0,null);
        mAddresses = data;
    }

    public class AddressSelectionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button deliverToThisAddress;
        // Button addNewAddress;

        //TextView name;
        TextView address;

        RadioButton toggle;
        TextView tvToggle;
        // ImageView options;

        ViewGroup paymentOptions;
        Button edit;
        Button delete;
        // ViewGroup optionLayout;

        public AddressSelectionHolder(final View view) {
            super(view);
            //name = (TextView) view.findViewById(R.id.tv_name);
            address = (TextView) view.findViewById(R.id.tv_address);
            toggle = (RadioButton) view.findViewById(R.id.rbtn_toggle);
            tvToggle=(TextView)view.findViewById(R.id.tv_rbtn_toggle);
            //options = (ImageView) view.findViewById(R.id.img_options);
            paymentOptions = (ViewGroup) view.findViewById(R.id.payment_options);
            deliverToThisAddress = (Button) view.findViewById(R.id.btn_deliver_to_this_address);
            edit=(Button)view.findViewById(R.id.btn_edit_address);
            delete=(Button)view.findViewById(R.id.btn_delete_address);
            // addNewAddress = (Button) view.findViewById(R.id.btn_add_new_address);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mSelectedIndex = getAdapterPosition();
            setToggleStatus(toggle, getAdapterPosition());
            notifyDataSetChanged();
        }
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private boolean isPositionFooter(int position) {
        return position == mAddresses.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    private class AddressSelectionHeader extends RecyclerView.ViewHolder {

        TextView mTvSelectAddressHeader;
        public AddressSelectionHeader(View view) {
            super(view);
            mTvSelectAddressHeader=(TextView)view.findViewById(R.id.tv_select_address);
            mTvSelectAddressHeader.setText(view.getContext().getString(R.string.iap_selection_select_address));
        }
    }

    private class AddressSelectionFooter extends RecyclerView.ViewHolder {
        public AddressSelectionFooter(View view) {
            super(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bindAddNewAddress(v);
                }
            });
        }
    }
}