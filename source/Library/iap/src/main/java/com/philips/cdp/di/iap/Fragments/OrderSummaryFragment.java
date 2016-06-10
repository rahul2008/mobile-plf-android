package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
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
import com.philips.cdp.di.iap.adapters.OrderProductAdapter;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.PaymentController;
import com.philips.cdp.di.iap.core.ControllerFactory;
import com.philips.cdp.di.iap.core.ShoppingCartAPI;
import com.philips.cdp.di.iap.response.payment.MakePaymentData;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.response.placeorder.PlaceOrder;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.tagging.Tagging;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrderSummaryFragment extends BaseAnimationSupportFragment implements View.OnClickListener, TwoButtonDailogFragment.TwoButtonDialogListener,
        PaymentController.MakePaymentListener {
    private OrderProductAdapter mAdapter;
    private AddressFields mBillingAddress;
    private PaymentMethod mPaymentMethod;
    private Button mBtnPayNow;
    private Button mBtnCancel;
    private PaymentController mPaymentController;
    private String orderID;
    private Context mContext;
    public static final String TAG = OrderSummaryFragment.class.getName();
    private TwoButtonDailogFragment mDailogFragment;

    @Override
    public void onResume() {
        super.onResume();
        IAPAnalytics.trackPage(IAPAnalyticsConstant.ORDER_SUMMARY_PAGE_NAME);
        setTitle(R.string.iap_order_summary);
        if (isOrderPlaced()) {
            setBackButtonVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.iap_order_summary_fragment, container, false);
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

        RecyclerView mOrderListView = (RecyclerView) rootView.findViewById(R.id.order_summary);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mOrderListView.setLayoutManager(layoutManager);
        if (isOrderPlaced()) {
            ArrayList<ShoppingCartData> shoppingCartDataArrayList = CartModelContainer.getInstance().getShoppingCartData();
            mAdapter = new OrderProductAdapter(getContext(), shoppingCartDataArrayList, mBillingAddress, mPaymentMethod);
        } else {
            mAdapter = new OrderProductAdapter(getContext(), new ArrayList<ShoppingCartData>(), mBillingAddress, mPaymentMethod);
            updateCartOnResume();
        }
        mOrderListView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void updateCartOnResume() {
        ShoppingCartAPI presenter = ControllerFactory.getInstance()
                .getShoppingCartPresenter(getContext(), mAdapter, getFragmentManager());
        if (!Utility.isProgressDialogShowing()) {
            Utility.showProgressDialog(getContext(), getString(R.string.iap_please_wait));
            updateCartDetails(presenter);
        }
    }

    private void updateCartDetails(ShoppingCartAPI presenter) {
        presenter.getCurrentCartDetails();
    }

    public static OrderSummaryFragment createInstance(Bundle args, AnimationType animType) {
        OrderSummaryFragment fragment = new OrderSummaryFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public boolean onBackPressed() {
        if (isOrderPlaced()) {
            ShowDialogOnBackPressed();
            return true;
        } else return false;
    }

    private void setSetOrderPlaceFalse() {
        CartModelContainer.getInstance().setOrderPlaced(false);
    }

    private boolean isOrderPlaced() {
        return CartModelContainer.getInstance().isOrderPlaced();
    }

    @Override
    public void onClick(final View v) {
        if (isNetworkNotConnected()) return;
        if (v == mBtnPayNow) {
            placeOrderElseMakePayment();
        } else if (v == mBtnCancel) {
            moveToShoppingCart();
        }
    }

    private void placeOrderElseMakePayment() {
        Tagging.trackAction(IAPAnalyticsConstant.SEND_DATA, IAPAnalyticsConstant.DELIVERY_METHOD,
                IAPAnalyticsConstant.DELIVERY_UPS_PARCEL);
        if (!Utility.isProgressDialogShowing()) {
            Utility.showProgressDialog(getContext(), getString(R.string.iap_please_wait));
            if (!isOrderPlaced() || paymentMethodAvailable()) {
                mPaymentController.placeOrder();
            } else {
                mPaymentController.makPayment(orderID);
            }
        }
    }

    private void moveToShoppingCart() {
        setSetOrderPlaceFalse();
        moveToFragment(ShoppingCartFragment.TAG);
    }

    private boolean paymentMethodAvailable() {
        return mPaymentMethod != null;
    }

    @Override
    public void onMakePayment(final Message msg) {
        Utility.dismissProgressDialog();
        if (msg.obj instanceof MakePaymentData) {

            //Track new billing address added action
            Tagging.trackAction(IAPAnalyticsConstant.SEND_DATA, IAPAnalyticsConstant.SPECIAL_EVENTS,
                    IAPAnalyticsConstant.NEW_BILLING_ADDRESS_ADDED);

            MakePaymentData mMakePaymentData = (MakePaymentData) msg.obj;
            Bundle bundle = new Bundle();
            bundle.putString(ModelConstants.WEBPAY_URL, mMakePaymentData.getWorldpayUrl());
            addFragment(WebPaymentFragment.createInstance(bundle, AnimationType.NONE), null);
        } else if (msg.obj instanceof IAPNetworkError) {
            getIAPActivity().getNetworkUtility().showErrorMessage(msg, getFragmentManager(), getContext());
        } else {
            getIAPActivity().getNetworkUtility().showErrorDialog(mContext, getFragmentManager(), mContext.getString(R.string.iap_ok),
                    mContext.getString(R.string.iap_server_error), mContext.getString(R.string.iap_something_went_wrong));
        }
    }

    private void launchConfirmationScreen(PlaceOrder details) {
        Bundle bundle = new Bundle();
        bundle.putString(ModelConstants.ORDER_NUMBER, details.getCode());
        bundle.putBoolean(ModelConstants.PAYMENT_SUCCESS_STATUS, Boolean.TRUE);
        addFragment(PaymentConfirmationFragment.createInstance(bundle, AnimationType.NONE), null);
    }

    @Override
    public void onPlaceOrder(final Message msg) {
        if (msg.obj instanceof PlaceOrder) {
            PlaceOrder order = (PlaceOrder) msg.obj;
            orderID = order.getCode();
            updateCount(0);
            CartModelContainer.getInstance().setOrderPlaced(true);
            CartModelContainer.getInstance().setOrderNumber(orderID);

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
                checkForOutOfStock(iapNetworkError, msg);
            } else {
                getIAPActivity().getNetworkUtility().showErrorMessage(msg, getFragmentManager(), getContext());
            }
        }
    }

    private void checkForOutOfStock(final IAPNetworkError iapNetworkError, Message msg) {
        com.philips.cdp.di.iap.response.error.Error error = iapNetworkError.getServerError().getErrors().get(0);
        String type = error.getType();
        if (type.equalsIgnoreCase(IAPConstant.INSUFFICIENT_STOCK_LEVEL_ERROR)) {
            String subject = error.getMessage();
            getIAPActivity().getNetworkUtility().showErrorDialog(mContext, getFragmentManager(), getString(R.string.iap_ok),
                    getString(R.string.iap_out_of_stock), subject);
        } else {
            getIAPActivity().getNetworkUtility().showErrorMessage(msg, getFragmentManager(), getContext());
        }
    }

    private void ShowDialogOnBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putString(IAPConstant.MODEL_ALERT_CONFIRM_DESCRIPTION, getString(R.string.cancelPaymentMsg));
        if (mDailogFragment == null) {
            mDailogFragment = new TwoButtonDailogFragment();
            mDailogFragment.setArguments(bundle);
            mDailogFragment.setOnDialogClickListener(this);
            mDailogFragment.setShowsDialog(false);
        }
        try {
            mDailogFragment.show(getFragmentManager(), "TwoButtonDialog");
            mDailogFragment.setShowsDialog(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDialogOkClick() {
        //Track Payment cancelled action
        Tagging.trackAction(IAPAnalyticsConstant.SEND_DATA,
                IAPAnalyticsConstant.PAYMENT_STATUS, IAPAnalyticsConstant.CANCELLED);
        moveToShoppingCart();
    }

    @Override
    public void onDialogCancelClick() {
        //NOP
    }
}
