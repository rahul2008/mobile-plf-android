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
import com.philips.cdp.di.iap.controller.PaymentController;
import com.philips.cdp.di.iap.model.ModelConstants;
import com.philips.cdp.di.iap.response.payment.MakePaymentData;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.response.placeorder.PlaceOrder;
import com.philips.cdp.di.iap.session.IAPNetworkError;
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
    private final static String TAG = OrderSummaryFragment.class.getSimpleName();

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
        IAPLog.d(TAG, "OrderSummaryFragment ");
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
            if (!Utility.isProgressDialogShowing()) {
                Utility.showProgressDialog(getContext(), getString(R.string.iap_please_wait));
                updateCartDetails(presenter);
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
        if (isOrderPlaced()) {
            //finishActivity();
            addFragment(EmptyCartFragment.createInstance(new Bundle(), AnimationType.NONE), null);
        } else {
            super.onBackPressed();
        }
    }

    private boolean isOrderPlaced() {
        return CartModelContainer.getInstance().isOrderPlaced();
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.btn_paynow) {
            if (!Utility.isProgressDialogShowing()) {
                    Utility.showProgressDialog(getContext(), getString(R.string.iap_please_wait));
                    if (!isOrderPlaced() || paymentMethodAvailable()) {
                        mPaymentController.placeOrder();
                    } else {
                        mPaymentController.makPayment(orderID);
                    }
            }
        } else if (v.getId() == R.id.btn_cancel) {
            addFragment(ShoppingCartFragment.createInstance(new Bundle(), AnimationType.NONE), null);
        }
    }


    private boolean paymentMethodAvailable() {
        return mPaymentMethod != null;
    }

    @Override
    public void onMakePayment(final Message msg) {
        Utility.dismissProgressDialog();
        if (msg.obj instanceof MakePaymentData) {

            MakePaymentData mMakePaymentData = (MakePaymentData) msg.obj;
            Bundle bundle = new Bundle();
            bundle.putString(ModelConstants.WEBPAY_URL, mMakePaymentData.getWorldpayUrl());
            addFragment(WebPaymentFragment.createInstance(bundle, AnimationType.NONE), null);
        } else if (msg.obj instanceof IAPNetworkError) {
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), getContext());
        }
    }

    private void launchConfirmationScreen(PlaceOrder details) {
        Bundle bundle = new Bundle();
        bundle.putString(ModelConstants.ORDER_NUMBER, details.getCode());
        replaceFragment(PaymentConfirmationFragment.createInstance(bundle, AnimationType.NONE), null);
    }

    @Override
    public void onPlaceOrder(final Message msg) {
        if (msg.obj instanceof PlaceOrder) {
            PlaceOrder order = (PlaceOrder) msg.obj;
            orderID = order.getCode();
            CartModelContainer.getInstance().setOrderPlaced(true);
            if (paymentMethodAvailable()) {
                Utility.dismissProgressDialog();
                launchConfirmationScreen((PlaceOrder) msg.obj);
            } else {
                mPaymentController.makPayment(orderID);
            }
        } else if (msg.obj instanceof IAPNetworkError) {
            Utility.dismissProgressDialog();
            IAPNetworkError iapNetworkError = (IAPNetworkError) msg.obj;
            if (null != iapNetworkError.getServerError()) {
                checkForOutOfStock(iapNetworkError,msg);
            } else {
                NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), getContext());
            }
        }
    }

    private void checkForOutOfStock(final IAPNetworkError iapNetworkError, Message msg) {
        com.philips.cdp.di.iap.response.error.Error error = iapNetworkError.getServerError().getErrors().get(0);
        String type = error.getType();
        if (type.equalsIgnoreCase(IAPConstant.INSUFFICIENT_STOCK_LEVEL_ERROR)) {
            String subject = error.getMessage();
            NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok),
                    getString(R.string.iap_out_of_stock), subject);
        } else {
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), getContext());
        }
    }
}
