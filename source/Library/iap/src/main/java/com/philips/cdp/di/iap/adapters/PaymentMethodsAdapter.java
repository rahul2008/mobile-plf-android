package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.uikit.customviews.UIKitRadioButton;

import java.util.List;

public class PaymentMethodsAdapter extends RecyclerView.Adapter<PaymentMethodsAdapter.PaymentMethodsHolder> {

    private Context mContext;
    private List<PaymentMethod> mPaymentMethodList;
    private int mSelectedIndex;

    public PaymentMethodsAdapter(final Context context, final List<PaymentMethod> paymentMethods) {
        mContext = context;
        mPaymentMethodList = paymentMethods;
        mSelectedIndex = 0;
    }

    @Override
    public PaymentMethodsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.iap_payment_method_item, null);
        return new PaymentMethodsHolder(view);
    }

    @Override
    public void onBindViewHolder(PaymentMethodsHolder holder, int position) {
        PaymentMethod paymentMethod = mPaymentMethodList.get(position);

        holder.cardName.setText(paymentMethod.getCardType().getCode() + " " +paymentMethod.getCardNumber());
        holder.cardHoldername.setText(paymentMethod.getAccountHolderName());
        holder.cardValidity.setText((mContext.getResources().getString(R.string.iap_valid_until)) + " " +
                paymentMethod.getExpiryMonth() + "/" + paymentMethod.getExpiryYear());

        //Update payment options buttons
        updatePaymentOptionBtnVisibility(holder.paymentOptions, position);

        //bind radio button state
        setPaymentRadioBtnState(holder.paymentRadioBtn, position);
        bindPaymentRadioBtn(holder, holder.paymentRadioBtn);

        //bind use payment button
        bindUsePaymentBtn(holder.usePayment);

        //bind add new payment button
        bindAddNewPaymentBtn(holder.addNewPayment);
    }

    private void bindUsePaymentBtn(final Button newPayment) {
        newPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                EventHelper.getInstance().notifyEventOccurred(IAPConstant.USE_PAYMENT);
            }
        });
    }

    private void bindAddNewPaymentBtn(Button deliver) {
        deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                EventHelper.getInstance().notifyEventOccurred(IAPConstant.ADD_NEW_PAYMENT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPaymentMethodList.size();
    }

    public int getSelectedPosition() {
        return mSelectedIndex;
    }

    private void setPaymentRadioBtnState(final UIKitRadioButton toggle, final int position) {
        if (mSelectedIndex == position) {
            toggle.setChecked(true);
        } else {
            toggle.setChecked(false);
        }
    }

    private void bindPaymentRadioBtn(final PaymentMethodsHolder holder, final UIKitRadioButton paymentRadioBtn) {
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


    public class PaymentMethodsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        UIKitRadioButton paymentRadioBtn;
        TextView cardName;
        TextView cardHoldername;
        TextView cardValidity;
        Button usePayment;
        Button addNewPayment;
        ViewGroup paymentOptions;

        public PaymentMethodsHolder(final View view) {
            super(view);
            paymentRadioBtn = (UIKitRadioButton) view.findViewById(R.id.radio_btn_payment);
            cardName = (TextView) view.findViewById(R.id.tv_card_name);
            cardHoldername = (TextView) view.findViewById(R.id.tv_card_holder_name);
            cardValidity = (TextView)view.findViewById(R.id.tv_card_validity);
            usePayment = (Button) view.findViewById(R.id.btn_use_payment_method);
            addNewPayment = (Button) view.findViewById(R.id.btn_add_new_payment);
            paymentOptions = (ViewGroup) view.findViewById(R.id.ll_payment_options);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mSelectedIndex = getAdapterPosition();
            setPaymentRadioBtnState(paymentRadioBtn, getAdapterPosition());
            notifyDataSetChanged();
        }
    }
}
