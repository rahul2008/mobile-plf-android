package com.philips.cdp.di.iap.payment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.uikit.customviews.UIKitRadioButton;

import java.util.List;

public class PaymentMethodsAdapter extends RecyclerView.Adapter<PaymentMethodsHolder> {

    private Context mContext;
    private List<PaymentMethod> mPaymentMethodList;
    private PaymentMethod mSelectedPaymentMethod;
    private int mSelectedIndex;
    private Drawable mOptionsDrawable;
    private int mOptionsClickPosition = -1;

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
        updatePaymentOptionBtnVisiblity(holder.paymentOptions, position);

        //bind radio button state
        setPaymentRadioBtnState(holder.paymentRadioBtn, position);
        bindPaymentRadioBtn(holder, holder.paymentRadioBtn);

        //bind use payment button
        bindUsePaymentBtn(holder.usePayment, position);

        //bind add new payment button
        bindAddNewPaymentBtn(holder.addNewPayment, position);
    }

    private void bindUsePaymentBtn(final Button newAddress, final int position) {
        newAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                EventHelper.getInstance().notifyEventOccurred(IAPConstant.USE_PAYMENT);
            }
        });
    }

    private void bindAddNewPaymentBtn(Button deliver, final int position) {
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
                setSelectedPaymentMethod(mSelectedIndex);
                notifyDataSetChanged();
            }
        });
    }

    private void setSelectedPaymentMethod(int position) {
        if (mPaymentMethodList.size() > 0 && position < mPaymentMethodList.size()) {
            mSelectedPaymentMethod = mPaymentMethodList.get(position);
        }
    }

    private void updatePaymentOptionBtnVisiblity(final ViewGroup paymentOptions, final int position) {
        if (mSelectedIndex == position) {
            paymentOptions.setVisibility(View.VISIBLE);
        } else {
            paymentOptions.setVisibility(View.GONE);
        }
    }
}
