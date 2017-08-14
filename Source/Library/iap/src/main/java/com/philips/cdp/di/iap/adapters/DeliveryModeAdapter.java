/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.response.addresses.DeliveryCost;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;

import java.util.List;

public class DeliveryModeAdapter extends RecyclerView.Adapter<DeliveryModeAdapter.DeliverySelectionHolder>{

    private Context mContext;
    private List<DeliveryModes> mModes;
    private int mSelectedIndex;

    public DeliveryModeAdapter(final Context context, int txtViewResourceId, final List<DeliveryModes> modes) {
        System.out.println("Kkkkkk 0 : "+modes.size());
        mContext = context;
        mModes = modes;
        mSelectedIndex = 0;
    }

    @Override
    public DeliverySelectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("Kkkkkk 1");
        View view = View.inflate(parent.getContext(), R.layout.iap_delivery_mode_spinner_item, null);
        return new DeliverySelectionHolder(view);
    }

    @Override
    public void onBindViewHolder(DeliverySelectionHolder holder, int position) {

        System.out.println("Kkkkkk 3");
        DeliveryModes modes = mModes.get(position);
        if (modes.getName() != null && !modes.getName().equals(""))
            holder.deliveryModeName.setText(modes.getName());
        holder.deliveryModeDescription.setText(modes.getDescription());

        //TODO :Cost is not in server response so value setting to 0.0.Report to Hybris.
        DeliveryCost deliveryCost = modes.getDeliveryCost();
        if (deliveryCost != null) {
            String cost = deliveryCost.getFormattedValue();
            holder.deliveryModePrice.setText(cost);
        } else {
            holder.deliveryModePrice.setText("0.0");
        }
        System.out.println("Kkkkkk 4");
        setToggleStatus(holder.deliveryRadioBtnToggle, position);
        bindToggleButton(holder, holder.deliveryRadioBtnToggle);

    }

    private void bindToggleButton(final DeliverySelectionHolder holder, final RadioButton toggle) {
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mSelectedIndex = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
    }

    private void setToggleStatus(final RadioButton toggle, final int position) {
        if (mSelectedIndex == position) {
            toggle.setChecked(true);
        } else {
            toggle.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        System.out.println("Kkkkkk getItemCount : " +mModes.size());
        return mModes.size();
    }

    public class DeliverySelectionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView deliveryModeName;
        private TextView deliveryModeDescription;
        private TextView deliveryModePrice;
        private Button deliveryConfirmBtn;
        private RadioButton deliveryRadioBtnToggle;


        public DeliverySelectionHolder(View view) {
            super(view);
            System.out.println("Kkkkkk 2");
            deliveryModeName = (TextView) view.findViewById(R.id.iap_title_ups_parcel);
            deliveryModeDescription = (TextView) view.findViewById(R.id.iap_available_time);
            deliveryModePrice = (TextView) view.findViewById(R.id.iap_delivery_parcel_amount);
            deliveryConfirmBtn = (Button) view.findViewById(R.id.iap_delivery_confirm_btn);
            deliveryRadioBtnToggle = (RadioButton) view.findViewById(R.id.iap_ups_parcel_radio_btn);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mSelectedIndex = getAdapterPosition();
            setToggleStatus(deliveryRadioBtnToggle, getAdapterPosition());
            notifyDataSetChanged();
        }
    }
}