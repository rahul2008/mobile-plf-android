/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.controller.PaymentController;
import com.philips.cdp.di.iap.core.ControllerFactory;
import com.philips.cdp.di.iap.core.ShoppingCartAPI;
import com.philips.cdp.di.iap.response.payment.MakePaymentData;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.response.placeorder.PlaceOrder;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;

public class OrderSummaryFragment extends BaseAnimationSupportFragment implements
        View.OnClickListener, PaymentController.MakePaymentListener, AddressController.AddressListener {
    private OrderProductAdapter mAdapter;
    private PaymentMethod mPaymentMethod;
    private Button mBtnPayNow;
    private Button mBtnCancel;
    private PaymentController mPaymentController;
    private Context mContext;
    public static final String TAG = OrderSummaryFragment.class.getName();
    Bundle bundle;

    @Override
    public void onResume() {
        super.onResume();
        IAPAnalytics.trackPage(IAPAnalyticsConstant.ORDER_SUMMARY_PAGE_NAME);
        setTitleAndBackButtonVisibility(R.string.iap_order_summary, true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.iap_order_summary_fragment, container, false);
        mPaymentController = new PaymentController(getContext(), this);

        mBtnPayNow = (Button) rootView.findViewById(R.id.btn_paynow);
        mBtnCancel = (Button) rootView.findViewById(R.id.btn_cancel);

        mBtnPayNow.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);

        bundle = getArguments();
        if (bundle.containsKey(IAPConstant.SELECTED_PAYMENT)) {
            mPaymentMethod = (PaymentMethod) bundle.getSerializable(IAPConstant.SELECTED_PAYMENT);
        }

        RecyclerView mOrderListView = (RecyclerView) rootView.findViewById(R.id.order_summary);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mOrderListView.setLayoutManager(layoutManager);
        AddressFields mBillingAddress = CartModelContainer.getInstance().getBillingAddress();
        mAdapter = new OrderProductAdapter(getContext(), this, new ArrayList<ShoppingCartData>(), mBillingAddress, mPaymentMethod);
        updateCartOnResume();
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
        if (!Utility.isProgressDialogShowing())
            Utility.showProgressDialog(getContext(), getString(R.string.iap_please_wait));
        updateCartDetails(presenter);
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
    public boolean handleBackEvent() {
        Fragment fragment = getFragmentManager().findFragmentByTag(BuyDirectFragment.TAG);
        if (fragment != null) {
            if (getFragmentManager().findFragmentByTag(BillingAddressFragment.TAG) != null ||
                    getFragmentManager().findFragmentByTag(PaymentSelectionFragment.TAG) != null) {
                return false;
            }
            moveToVerticalAppByClearingStack();
            return false;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(final View v) {
        if (!isNetworkConnected()) return;
        if (v == mBtnPayNow) {
            if (mPaymentMethod != null)
                showCvvDialog(getFragmentManager());
            else {
                placeOrderElseMakePayment(null);
            }
        } else if (v == mBtnCancel) {
            doOnCancelOrder();
        }
    }

    private void placeOrderElseMakePayment(String pSecurityCode) {
        IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA, IAPAnalyticsConstant.DELIVERY_METHOD,
                IAPAnalyticsConstant.DELIVERY_UPS_PARCEL);
        if (!Utility.isProgressDialogShowing()) {
            Utility.showProgressDialog(getContext(), getString(R.string.iap_please_wait));
            mPaymentController.placeOrder(pSecurityCode);
        }
    }

    private void doOnCancelOrder() {
        Fragment fragment = getFragmentManager().findFragmentByTag(BuyDirectFragment.TAG);
        if (fragment != null) {
            moveToVerticalAppByClearingStack();
        } else {
            showFragment(ShoppingCartFragment.TAG);
        }
    }

    private boolean paymentMethodAvailable() {
        return mPaymentMethod != null;
    }

    @Override
    public void onMakePayment(final Message msg) {
        Utility.dismissProgressDialog();
        if (msg.obj instanceof MakePaymentData) {

            //Track new billing address added action
            IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA, IAPAnalyticsConstant.SPECIAL_EVENTS,
                    IAPAnalyticsConstant.NEW_BILLING_ADDRESS_ADDED);

            MakePaymentData mMakePaymentData = (MakePaymentData) msg.obj;
            Bundle bundle = new Bundle();
            bundle.putString(ModelConstants.WEBPAY_URL, mMakePaymentData.getWorldpayUrl());
            addFragment(WebPaymentFragment.createInstance(bundle, AnimationType.NONE), null);
        } else if (msg.obj instanceof IAPNetworkError) {
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), getContext());
        } else {
            NetworkUtility.getInstance().showErrorDialog(mContext, getFragmentManager(), mContext.getString(R.string.iap_ok),
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
            String orderID = order.getCode();
            updateCount(0);
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
                NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), getContext());
            }
        }
    }

    private void checkForOutOfStock(final IAPNetworkError iapNetworkError, Message msg) {
        com.philips.cdp.di.iap.response.error.Error error = iapNetworkError.getServerError().getErrors().get(0);
        String type = error.getType();
        if (type.equalsIgnoreCase(IAPConstant.INSUFFICIENT_STOCK_LEVEL_ERROR)) {
            String subject = error.getMessage();
            NetworkUtility.getInstance().showErrorDialog(mContext, getFragmentManager(), getString(R.string.iap_ok),
                    getString(R.string.iap_out_of_stock), subject);
        } else {
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), getContext());
        }
    }

    @Override
    public void onGetRegions(Message msg) {

    }

    @Override
    public void onGetUser(Message msg) {

    }

    @Override
    public void onCreateAddress(Message msg) {

    }

    @Override
    public void onGetAddress(Message msg) {

    }

    @Override
    public void onSetDeliveryAddress(Message msg) {

    }

    @Override
    public void onGetDeliveryModes(Message msg) {

    }

    @Override
    public void onSetDeliveryMode(Message msg) {
        if (msg.obj.equals(IAPConstant.IAP_SUCCESS)) {
            updateCartOnResume();
        } else {
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);
            Utility.dismissProgressDialog();
        }
    }

    private void showCvvDialog(FragmentManager pFragmentManager) {
        if (mPaymentMethod != null)
            bundle.putSerializable(IAPConstant.SELECTED_PAYMENT, mPaymentMethod);
        CvvCvcDialogFragment cvvCvcDialogFragment = new CvvCvcDialogFragment();
        cvvCvcDialogFragment.setTargetFragment(this, CvvCvcDialogFragment.REQUEST_CODE);
        cvvCvcDialogFragment.setArguments(bundle);
        cvvCvcDialogFragment.show(pFragmentManager, "EditErrorDialog");
        cvvCvcDialogFragment.setShowsDialog(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CvvCvcDialogFragment.REQUEST_CODE) {
            String securityCode = data.getStringExtra(
                    IAPConstant.CVV_KEY_BUNDLE);
            IAPLog.d(IAPLog.LOG, "CVV =" + securityCode);
            placeOrderElseMakePayment(securityCode);
        }
    }
}
