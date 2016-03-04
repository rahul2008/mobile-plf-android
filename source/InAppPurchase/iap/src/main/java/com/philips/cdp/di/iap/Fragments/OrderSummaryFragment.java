package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.adapters.OrderProductAdapter;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.model.ModelConstants;
import com.philips.cdp.di.iap.payment.PaymentController;
import com.philips.cdp.di.iap.response.payment.MakePaymentData;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.response.placeorder.PlaceOrder;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrderSummaryFragment extends BaseAnimationSupportFragment implements View.OnClickListener,
        PaymentController.MakePaymentListener {
    private RecyclerView mOrderListView;
    private OrderProductAdapter mAdapter;
    private ArrayList<ShoppingCartData> mShoppingCartDataList;
    private AddressFields mBillingAddress;
    private PaymentMethod mPaymentMethod;
    private Button mBtnPayNow, mBtnCancel;
    private PaymentController mPaymentController;

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.iap_order_summary);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mOrderListView.setLayoutManager(layoutManager);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.iap_order_summary_fragment, container, false);
        IAPLog.d(IAPLog.ORDER_SUMMARY_FRAGMENT, "OrderSummaryFragment ");
        mPaymentController = new PaymentController(getContext(), this);

        mBtnPayNow = (Button) rootView.findViewById(R.id.btn_paynow);
        mBtnCancel = (Button) rootView.findViewById(R.id.btn_cancel);

        mBtnPayNow.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);

      Bundle bundle = getArguments();
        if (bundle.containsKey(IAPConstant.BILLING_ADDRESS_FIELDS)) {
            mBillingAddress = (AddressFields) bundle.getSerializable(IAPConstant.BILLING_ADDRESS_FIELDS);
        }
        if (bundle.containsKey(IAPConstant.SELECTED_PAYMENT)) {
            mPaymentMethod = (PaymentMethod) bundle.getSerializable(IAPConstant.SELECTED_PAYMENT);
        }

        setShoppingCartAdaptor(rootView);
        return rootView;
    }

    private void setShoppingCartAdaptor(final View rootView) {
        mOrderListView = (RecyclerView) rootView.findViewById(R.id.order_summary);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mOrderListView.setLayoutManager(layoutManager);
        mShoppingCartDataList = new ArrayList<ShoppingCartData>();
        mShoppingCartDataList = CartModelContainer.getInstance().getShoppingCartData();
        IAPLog.d(IAPLog.ORDER_SUMMARY_FRAGMENT, "Shopping Cart list = " + mShoppingCartDataList);
        mAdapter = new OrderProductAdapter(getContext(), mShoppingCartDataList, mBillingAddress, mPaymentMethod);
        mOrderListView.setAdapter(mAdapter);
    }

    public static OrderSummaryFragment createInstance(Bundle args, AnimationType animType) {
        OrderSummaryFragment fragment = new OrderSummaryFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.btn_paynow) {
            if (!Utility.isProgressDialogShowing()) {
                if (Utility.isInternetConnected(getContext())) {
                    Utility.showProgressDialog(getContext(), getString(R.string.iap_update_address));
                    mPaymentController.placeOrder();
                } else {
                    NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok),
                            getString(R.string.iap_network_error), getString(R.string.iap_check_connection));
                }
            }
        } else if (v == mBtnCancel) {
            addFragment(ShoppingCartFragment.createInstance(new Bundle(), AnimationType.NONE), null);
        }
    }

    @Override
    public void onMakePayment(final Message msg) {
        Utility.dismissProgressDialog();
        if (msg.obj instanceof MakePaymentData) {
            MakePaymentData mMakePaymentData = (MakePaymentData) msg.obj;
            Bundle bundle = new Bundle();
            bundle.putString(ModelConstants.WEBPAY_URL, mMakePaymentData.getWorldpayUrl());
            addFragment(WebPaymentFragment.createInstance(bundle, AnimationType.NONE), null);
        }else if (msg.obj instanceof VolleyError){
            NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok),
                    getString(R.string.iap_network_error), getString(R.string.iap_check_connection));
        }
    }

    @Override
    public void onPlaceOrder(final Message msg) {
        if (msg.obj instanceof PlaceOrder) {
            PlaceOrder order = (PlaceOrder) msg.obj;
            String orderID = order.getCode();
            mPaymentController.makPayment(orderID);
        }else if (msg.obj instanceof VolleyError){
            Utility.dismissProgressDialog();
            NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok),
                    getString(R.string.iap_network_error), getString(R.string.iap_check_connection));
        }
    }
}
