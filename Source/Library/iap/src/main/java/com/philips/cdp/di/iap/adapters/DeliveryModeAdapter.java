/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.response.addresses.DeliveryCost;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;
import com.philips.cdp.di.iap.screens.OnSetDeliveryModeListener;

import java.util.List;

public class DeliveryModeAdapter extends RecyclerView.Adapter<DeliveryModeAdapter.DeliverySelectionHolder>{

    private List<DeliveryModes> mModes;
    private int mSelectedIndex;
  //  private View.OnClickListener mConfirmBtnClick;
    private OnSetDeliveryModeListener mListener;
    private boolean isRadioBtnSelectedFirst;

    public DeliveryModeAdapter(final List<DeliveryModes> modes,
                               OnSetDeliveryModeListener listener) {
        mModes = modes;
        mSelectedIndex = 0;
       // mConfirmBtnClick = confirmBtnClick;
        mListener = listener;
    }

    @Override
    public DeliverySelectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.iap_delivery_mode_spinner_item, null);
        return new DeliverySelectionHolder(view);
    }

    @Override
    public void onBindViewHolder(final DeliverySelectionHolder holder, int position) {

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
        setToggleStatus(holder.deliveryRadioBtnToggle, position, holder.deliveryConfirmBtn);
        bindToggleButton(holder, holder.deliveryRadioBtnToggle);

        holder.deliveryConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(holder.getAdapterPosition());
            }
        });
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

    private void setToggleStatus(final RadioButton toggle, final int position, Button deliveryConfirmBtn) {
        if(isRadioBtnSelectedFirst){
            if (mSelectedIndex == position) {
                toggle.setChecked(true);
                deliveryConfirmBtn.setVisibility(View.VISIBLE);
            } else {
                toggle.setChecked(false);
                deliveryConfirmBtn.setVisibility(View.GONE);
            }
        }else{
            isRadioBtnSelectedFirst = false;
        }
    }

    @Override
    public int getItemCount() {
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
            deliveryModeName = (TextView) view.findViewById(R.id.iap_title_ups_parcel);
            deliveryModeDescription = (TextView) view.findViewById(R.id.iap_available_time);
            deliveryModePrice = (TextView) view.findViewById(R.id.iap_delivery_parcel_amount);
            deliveryConfirmBtn = (Button) view.findViewById(R.id.iap_delivery_confirm_btn);
            deliveryRadioBtnToggle = (RadioButton) view.findViewById(R.id.iap_ups_parcel_radio_btn);
            view.setOnClickListener(this);
          //  deliveryConfirmBtn.setOnClickListener(mConfirmBtnClick);
        }

        @Override
        public void onClick(View v) {
            isRadioBtnSelectedFirst = true;
            mSelectedIndex = getAdapterPosition();
            setToggleStatus(deliveryRadioBtnToggle, getAdapterPosition(),deliveryConfirmBtn);
            notifyDataSetChanged();
        }
    }
}