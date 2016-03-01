package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.payment.PaymentMethodFields;
import com.philips.cdp.di.iap.payment.PaymentMethodsAdapter;
import com.philips.cdp.di.iap.session.NetworkConstants;

import java.util.List;

public class PaymentSelectionFragment extends BaseAnimationSupportFragment implements View.OnClickListener{

    private Context mContext;
    private RecyclerView mPaymentMethodsRecyclerView;
    private Button mBtnCancel;
    private PaymentMethodsAdapter mPaymentMethodsAdapter;
    private List<PaymentMethodFields> mPaymentMethodFields;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.iap_payment_method, container, false);
        mPaymentMethodsRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_payment_method);
        mBtnCancel = (Button)view.findViewById(R.id.btn_cancel);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mPaymentMethodsRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.iap_payment);
    }

    public static PaymentSelectionFragment createInstance(final Bundle args, final AnimationType animType) {
        PaymentSelectionFragment fragment = new PaymentSelectionFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnCancel){
            addFragment(ShoppingCartFragment.createInstance(new Bundle(), AnimationType.NONE), null);
        }
    }

    private PaymentMethodFields selectedPaymentDetails() {
        return mPaymentMethodFields.get(mPaymentMethodsAdapter.getSelectedPosition());
    }

    private PaymentMethodFields setPaymentMethodFields(PaymentMethodFields paymentMethodFields) {
        return paymentMethodFields;
    }
}
