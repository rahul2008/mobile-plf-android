package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.adapters.OrderProductAdapter;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.model.ModelConstants;
import com.philips.cdp.di.iap.controller.PaymentController;
import com.philips.cdp.di.iap.response.error.ServerError;
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
    private AddressFields mBillingAddress;
    private PaymentMethod mPaymentMethod;
    private Button mBtnPayNow, mBtnCancel;
    private PaymentController mPaymentController;
    private String orderID;

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.iap_order_summary);
        if (isOrderPlaced()) {
            setBackButtonVisibility(View.INVISIBLE);
        }
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

        mOrderListView = (RecyclerView) rootView.findViewById(R.id.order_summary);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mOrderListView.setLayoutManager(layoutManager);
        if (isOrderPlaced()) {
            ArrayList<ShoppingCartData>  shoppingCartDataArrayList = CartModelContainer.getInstance().getShoppingCartData();
            mAdapter = new OrderProductAdapter(getContext(), shoppingCartDataArrayList, mBillingAddress, mPaymentMethod);
        } else {
            mAdapter = new OrderProductAdapter(getContext(), new ArrayList<ShoppingCartData>(), mBillingAddress, mPaymentMethod);
            updateCartOnResume();
        }
        mOrderListView.setAdapter(mAdapter);
        return rootView;
    }

    private void updateCartOnResume() {
        ShoppingCartPresenter presenter = new ShoppingCartPresenter(getContext(), mAdapter, getFragmentManager());
        if (Utility.isInternetConnected(getContext())) {
            if (!Utility.isProgressDialogShowing()) {
                Utility.showProgressDialog(getContext(), getString(R.string.iap_get_cart_details));
                updateCartDetails(presenter);
            }
        } else {
            NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok), getString(R.string.iap_network_error), getString(R.string.iap_check_connection));
        }
    }

    private void updateCartDetails(ShoppingCartPresenter presenter) {
        presenter.getCurrentCartDetails();
    }

    public static OrderSummaryFragment createInstance(Bundle args, AnimationType animType) {
        OrderSummaryFragment fragment = new OrderSummaryFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isOrderPlaced()) {
            finishActivity();
        }
    }

    private boolean isOrderPlaced() {
        return CartModelContainer.getInstance().isOrderPlaced();
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.btn_paynow) {
            if (!Utility.isProgressDialogShowing()) {
                if (Utility.isInternetConnected(getContext())) {
                    Utility.showProgressDialog(getContext(), getString(R.string.iap_please_wait));
                    if (!isOrderPlaced()) {
                        mPaymentController.placeOrder();
                    } else {
                        mPaymentController.makPayment(orderID);
                    }
                } else {
                    NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok),
                            getString(R.string.iap_network_error), getString(R.string.iap_check_connection));
                }
            }
        } else if (v.getId() == R.id.btn_cancel) {
            if (isOrderPlaced()) {
                finishActivity();
            } else {
                addFragment(ShoppingCartFragment.createInstance(new Bundle(), AnimationType.NONE), null);
            }
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
        } else if (msg.obj instanceof ServerError) {
            NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok),
                    getString(R.string.iap_network_error), getString(R.string.iap_check_connection));
        }
    }

    @Override
    public void onPlaceOrder(final Message msg) {
        if (msg.obj instanceof PlaceOrder) {
            PlaceOrder order = (PlaceOrder) msg.obj;
            orderID = order.getCode();
            CartModelContainer.getInstance().setOrderPlaced(true);
            mPaymentController.makPayment(orderID);
        } else if (msg.obj instanceof ServerError) {
            Utility.dismissProgressDialog();
            NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok),
                    getString(R.string.iap_network_error), getString(R.string.iap_check_connection));
        }
    }
}
