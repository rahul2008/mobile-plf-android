package com.philips.cdp.di.iap.payment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;

import java.util.List;

public class PaymentMethodsAdapter extends RecyclerView.Adapter<PaymentMethodsHolder>{

    private Context mContext;
    private List<PaymentMethod> mPaymentMethods;
    private int mSelectedIndex;
    private Drawable mOptionsDrawable;
    private int mOptionsClickPosition = -1;

    public PaymentMethodsAdapter(final Context context, final List<PaymentMethod> paymentMethods) {
        mContext = context;
        mPaymentMethods = paymentMethods;
        mSelectedIndex = 0;
    }

    @Override
    public PaymentMethodsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.iap_payment_method_item, null);
        return new PaymentMethodsHolder(view);
    }

    @Override
    public void onBindViewHolder(PaymentMethodsHolder holder, int position) {
//        PaymentMethod paymentMethod = mPaymentMethods.get(position);
    }

    @Override
    public int getItemCount() {
        return mPaymentMethods.size();
    }
}
