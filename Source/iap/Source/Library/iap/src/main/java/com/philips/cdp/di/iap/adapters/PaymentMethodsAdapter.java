package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.platform.uid.view.widget.RadioButton;

import java.util.List;

public class PaymentMethodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private Context mContext;
    private List<PaymentMethod> mPaymentMethodList;
    private int mSelectedIndex;

    public PaymentMethodsAdapter(final Context context, final List<PaymentMethod> paymentMethods) {
        mContext = context;
        mPaymentMethodList = paymentMethods;
        mSelectedIndex = 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {

            case TYPE_ITEM:
                view = View.inflate(parent.getContext(), R.layout.iap_payment_method_item, null);
                return new PaymentMethodsHolder(view);

            case TYPE_FOOTER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iap_payment_method_footer, parent, false);
                return new PaymentMethodsFooterHolder(view);

            default:
                return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof PaymentMethodsHolder) {

            PaymentMethodsHolder paymentMethodsHolder = (PaymentMethodsHolder) holder;
            PaymentMethod paymentMethod = mPaymentMethodList.get(position);

            paymentMethodsHolder.cardName.setText(paymentMethod.getCardType().getCode() + " " + paymentMethod.getCardNumber());
            paymentMethodsHolder.cardHoldername.setText(paymentMethod.getAccountHolderName());
            paymentMethodsHolder.cardValidity.setText((mContext.getResources().getString(R.string.iap_valid_until)) + " " +
                    paymentMethod.getExpiryMonth() + "/" + paymentMethod.getExpiryYear());

            //Update payment options buttons
            updatePaymentOptionBtnVisibility(paymentMethodsHolder.paymentOptions, position);

            //bind radio button state
            setPaymentRadioBtnState(paymentMethodsHolder.paymentRadioBtn, position);
            bindPaymentRadioBtn(paymentMethodsHolder, paymentMethodsHolder.paymentRadioBtn);

            //bind use payment button
            bindUsePaymentBtn(paymentMethodsHolder.usePayment);

        }
    }

    private void bindUsePaymentBtn(final Button newPayment) {
        newPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                EventHelper.getInstance().notifyEventOccurred(IAPConstant.USE_PAYMENT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPaymentMethodList.size() + 1; // +1 for footer
    }

    public int getSelectedPosition() {
        return mSelectedIndex;
    }

    private void setPaymentRadioBtnState(final RadioButton toggle, final int position) {
        if (mSelectedIndex == position) {
            toggle.setChecked(true);
        } else {
            toggle.setChecked(false);
        }
    }

    private void bindPaymentRadioBtn(final PaymentMethodsHolder holder, final RadioButton paymentRadioBtn) {
        paymentRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mSelectedIndex = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
    }

    private void updatePaymentOptionBtnVisibility(final ViewGroup paymentOptions, final int position) {
        if (mSelectedIndex == position) {
            paymentOptions.setVisibility(View.VISIBLE);
        } else {
            paymentOptions.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mPaymentMethodList.size()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public class PaymentMethodsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RadioButton paymentRadioBtn;
        TextView cardName;
        TextView cardHoldername;
        TextView cardValidity;
        Button usePayment;
        ViewGroup paymentOptions;

        public PaymentMethodsHolder(final View view) {
            super(view);
            paymentRadioBtn = view.findViewById(R.id.radio_btn_payment);
            cardName = view.findViewById(R.id.tv_card_name);
            cardHoldername = view.findViewById(R.id.tv_card_holder_name);
            cardValidity = view.findViewById(R.id.tv_card_validity);
            usePayment = view.findViewById(R.id.btn_use_payment_method);
            paymentOptions = view.findViewById(R.id.ll_payment_options);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mSelectedIndex = getAdapterPosition();
            setPaymentRadioBtnState(paymentRadioBtn, getAdapterPosition());
            notifyDataSetChanged();
        }
    }

    private class PaymentMethodsFooterHolder extends RecyclerView.ViewHolder {
        public PaymentMethodsFooterHolder(View view) {
            super(view);

            TextView tvSelectHeader = view.findViewById(R.id.tv_select_header);
            tvSelectHeader.setText(mContext.getString(R.string.iap_add_payment_method));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventHelper.getInstance().notifyEventOccurred(IAPConstant.ADD_NEW_PAYMENT);
                }
            });
        }
    }
}
