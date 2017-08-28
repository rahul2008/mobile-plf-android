/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.screens;

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
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.activity.IAPActivity;
import com.philips.cdp.di.iap.adapters.CheckOutHistoryAdapter;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.cart.ShoppingCartAPI;
import com.philips.cdp.di.iap.cart.ShoppingCartData;
import com.philips.cdp.di.iap.cart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.controller.ControllerFactory;
import com.philips.cdp.di.iap.controller.PaymentController;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.response.State.RegionsList;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;
import com.philips.cdp.di.iap.response.addresses.GetDeliveryModes;
import com.philips.cdp.di.iap.response.addresses.GetShippingAddressData;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderSummaryFragment extends InAppBaseFragment
        implements View.OnClickListener, PaymentController.MakePaymentListener,EventListener, AddressController.AddressListener,
        CheckOutHistoryAdapter.OutOfStockListener, ShoppingCartPresenter.ShoppingCartListener<ShoppingCartData>,
        DeliveryModeDialog.DialogListener, com.philips.cdp.di.iap.utils.AlertListener ,CheckOutHistoryAdapter.OrderSummaryUpdateListner {

    public static final String TAG = ShoppingCartFragment.class.getName();
    private Context mContext;

    private Button mPayNowBtn;
    private Button mCancelBtn;

    public CheckOutHistoryAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private AddressController mAddressController;
    private ShoppingCartAPI mShoppingCartAPI;
    private ArrayList<ShoppingCartData> mData = new ArrayList<>();
    private List<Addresses> mAddresses = new ArrayList<>();
    private DeliveryModes mSelectedDeliveryMode;
    private TextView mNumberOfProducts;
    private PaymentMethod mPaymentMethod;
    Bundle bundle;
    private PaymentController mPaymentController;

    public static OrderSummaryFragment createInstance(Bundle args, AnimationType animType) {
        OrderSummaryFragment fragment = new OrderSummaryFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        EventHelper.getInstance().registerEventNotification(String.valueOf(IAPConstant.BUTTON_STATE_CHANGED), this);
        EventHelper.getInstance().registerEventNotification(String.valueOf(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED), this);
        EventHelper.getInstance().registerEventNotification(String.valueOf(IAPConstant.PRODUCT_DETAIL_FRAGMENT), this);
        EventHelper.getInstance().registerEventNotification(String.valueOf(IAPConstant.IAP_LAUNCH_PRODUCT_CATALOG), this);
        EventHelper.getInstance().registerEventNotification(String.valueOf(IAPConstant.IAP_DELETE_PRODUCT), this);
        EventHelper.getInstance().registerEventNotification(String.valueOf(IAPConstant.IAP_UPDATE_PRODUCT_COUNT), this);
        EventHelper.getInstance().registerEventNotification(String.valueOf(IAPConstant.IAP_EDIT_DELIVERY_MODE), this);
        EventHelper.getInstance().registerEventNotification(String.valueOf(IAPConstant.IAP_DELETE_PRODUCT_CONFIRM), this);

        View rootView = inflater.inflate(R.layout.iap_order_summary_fragment, container, false);
        mPaymentController = new PaymentController(mContext, this);
        bundle = getArguments();
        if (bundle.containsKey(IAPConstant.SELECTED_PAYMENT)) {
            mPaymentMethod = (PaymentMethod) bundle.getSerializable(IAPConstant.SELECTED_PAYMENT);
        }

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.shopping_cart_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mPayNowBtn = (Button) rootView.findViewById(R.id.pay_now_btn);
        mPayNowBtn.setOnClickListener(this);
        mCancelBtn = (Button) rootView.findViewById(R.id.cancel_btn);
        mCancelBtn.setOnClickListener(this);
        mShoppingCartAPI = ControllerFactory.getInstance()
                .getShoppingCartPresenter(mContext, this);
        mAddressController = new AddressController(mContext, this);
        mNumberOfProducts = (TextView) rootView.findViewById(R.id.number_of_products);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        IAPAnalytics.trackPage(IAPAnalyticsConstant.SHOPPING_CART_PAGE_NAME);
        IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                IAPAnalyticsConstant.SPECIAL_EVENTS, IAPAnalyticsConstant.SHOPPING_CART_VIEW);
        setTitleAndBackButtonVisibility(R.string.iap_checkout, true);
        if (isNetworkConnected()) {
            updateCartOnResume();
        }
    }

    private void updateCartOnResume() {
        if (!isProgressDialogShowing()) {
            showProgressDialog(mContext, getString(R.string.iap_please_wait));
        }
        mAddressController.getDeliveryModes();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null)
            mAdapter.onStop();
        dismissProgressDialog();
        NetworkUtility.getInstance().dismissErrorDialog();
    }

    private void updateCartDetails(ShoppingCartAPI presenter) {
        presenter.getCurrentCartDetails();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.BUTTON_STATE_CHANGED), this);
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED), this);
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.PRODUCT_DETAIL_FRAGMENT), this);
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.IAP_LAUNCH_PRODUCT_CATALOG), this);
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.IAP_EDIT_DELIVERY_MODE), this);
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.IAP_DELETE_PRODUCT), this);
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.IAP_UPDATE_PRODUCT_COUNT), this);
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.IAP_DELETE_PRODUCT_CONFIRM), this);
    }

    @Override
    public void onClick(final View v) {

        if (!isNetworkConnected()) return;
        if (v == mPayNowBtn) {
            showCvvDialog(getFragmentManager());// need to remove
            if (mPaymentMethod != null)
                showCvvDialog(getFragmentManager());
            else {
                placeOrder(null);
            }
        } else if (v == mCancelBtn) {
            doOnCancelOrder();
        }
    }

    private void placeOrder(String pSecurityCode) {
        IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA, IAPAnalyticsConstant.DELIVERY_METHOD,
                IAPAnalyticsConstant.DELIVERY_UPS_PARCEL);
        if (!isProgressDialogShowing()) {
            showProgressDialog(mContext, getString(R.string.iap_please_wait));
            mPaymentController.placeOrder(pSecurityCode);
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
    public boolean handleBackEvent() {
        Fragment fragment = getFragmentManager().findFragmentByTag(ProductCatalogFragment.TAG);
        if (fragment == null && getActivity() != null && getActivity() instanceof IAPActivity) {
            finishActivity();
        }
        return false;
    }

    @Override
    public void onEventReceived(final String event) {
        dismissProgressDialog();
        if (event.equalsIgnoreCase(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED)) {
            addFragment(EmptyCartFragment.createInstance(new Bundle(), AnimationType.NONE), EmptyCartFragment.TAG);
        } else if (event.equalsIgnoreCase(String.valueOf(IAPConstant.BUTTON_STATE_CHANGED))) {
            mPayNowBtn.setEnabled(!Boolean.getBoolean(event));
        } else if (event.equalsIgnoreCase(String.valueOf(IAPConstant.PRODUCT_DETAIL_FRAGMENT))) {
            startProductDetailFragment();
        } else if (event.equalsIgnoreCase(String.valueOf(IAPConstant.IAP_LAUNCH_PRODUCT_CATALOG))) {
            showProductCatalogFragment(ShoppingCartFragment.TAG);
        } else if (event.equalsIgnoreCase(IAPConstant.IAP_DELETE_PRODUCT)) {
            if (!isProgressDialogShowing()) {
                showProgressDialog(mContext, mContext.getString(R.string.iap_please_wait));
                mShoppingCartAPI.deleteProduct(mData.get(mAdapter.getSelectedItemPosition()));
            }
        } else if (event.equalsIgnoreCase(IAPConstant.IAP_UPDATE_PRODUCT_COUNT)) {
            if (!isProgressDialogShowing()) {
                showProgressDialog(mContext, mContext.getString(R.string.iap_please_wait));
                mShoppingCartAPI.updateProductQuantity(mData.get(mAdapter.getSelectedItemPosition()),
                        mAdapter.getNewCount(), mAdapter.getQuantityStatusInfo());
            }
        } else if (event.equalsIgnoreCase(IAPConstant.IAP_EDIT_DELIVERY_MODE)) {

            addFragment(DeliveryMethodFragment.createInstance(new Bundle(), AnimationType.NONE),
                    AddressSelectionFragment.TAG);
        } else if (event.equalsIgnoreCase(IAPConstant.IAP_DELETE_PRODUCT_CONFIRM)) {
            Utility.showActionDialog(mContext, getString(R.string.iap_remove_product), getString(R.string.iap_cancel)
                    , null, getString(R.string.iap_product_remove_description), getFragmentManager(), this);
        }
    }

    private void startProductDetailFragment() {
        ShoppingCartData shoppingCartData = mAdapter.getTheProductDataForDisplayingInProductDetailPage();
        Bundle bundle = new Bundle();
        bundle.putString(IAPConstant.PRODUCT_TITLE, shoppingCartData.getProductTitle());
        bundle.putString(IAPConstant.PRODUCT_CTN, shoppingCartData.getCtnNumber());
        bundle.putString(IAPConstant.PRODUCT_PRICE, shoppingCartData.getFormattedPrice());
        bundle.putString(IAPConstant.PRODUCT_VALUE_PRICE, shoppingCartData.getValuePrice());
        bundle.putString(IAPConstant.PRODUCT_OVERVIEW, shoppingCartData.getMarketingTextHeader());
        bundle.putInt(IAPConstant.PRODUCT_QUANTITY, shoppingCartData.getQuantity());
        bundle.putInt(IAPConstant.PRODUCT_STOCK, shoppingCartData.getStockLevel());
        bundle.putSerializable(IAPConstant.SHOPPING_CART_CODE, shoppingCartData);
        addFragment(ProductDetailFragment.createInstance(bundle, AnimationType.NONE), ProductDetailFragment.TAG);
    }

    @Override
    public void onGetAddress(Message msg) {
        dismissProgressDialog();
        if (msg.obj instanceof IAPNetworkError) {
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);
        } else {
            Bundle bundle = new Bundle();
            if (mSelectedDeliveryMode != null)
                bundle.putParcelable(IAPConstant.SET_DELIVERY_MODE, mSelectedDeliveryMode);

            if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {
                addFragment(ShippingAddressFragment.createInstance(bundle, AnimationType.NONE),
                        ShippingAddressFragment.TAG);
            } else if (msg.obj instanceof GetShippingAddressData) {
                GetShippingAddressData shippingAddresses = (GetShippingAddressData) msg.obj;
                mAddresses = shippingAddresses.getAddresses();
                bundle.putSerializable(IAPConstant.ADDRESS_LIST, (Serializable) mAddresses);
                addFragment(AddressSelectionFragment.createInstance(bundle, AnimationType.NONE),
                        AddressSelectionFragment.TAG);
            }
        }
    }

    @Override
    public void onGetRegions(Message msg) {
        if (msg.obj instanceof IAPNetworkError) {
            CartModelContainer.getInstance().setRegionList(null);
        } else if (msg.obj instanceof RegionsList) {
            CartModelContainer.getInstance().setRegionList((RegionsList) msg.obj);
        } else {
            CartModelContainer.getInstance().setRegionList(null);
        }

        mAddressController.getAddresses();
    }

    @Override
    public void onOutOfStock(boolean isOutOfStockReached) {
        if (mPayNowBtn == null) return;
        if (isOutOfStockReached) {
            mPayNowBtn.setEnabled(false);
        } else {
            mPayNowBtn.setEnabled(true);
        }
    }

//    @Override
//    public void onLoadFinished(final ArrayList<ShoppingCartData> data) {
//        dismissProgressDialog();
//        if (getActivity() == null) return;
//        mData = data;
//        onOutOfStock(false);
//        mAdapter = new ShoppingCartAdapter(mContext, mData, this);
//        if (data.get(0) != null && data.get(0).getDeliveryItemsQuantity() > 0) {
//            updateCount(data.get(0).getDeliveryItemsQuantity());
//        }
//        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.tagProducts();
//
//        String numberOfProducts = mContext.getResources().getString(R.string.iap_number_of_products);
//        numberOfProducts = String.format(numberOfProducts, mData.size());
//        mNumberOfProducts.setText(numberOfProducts);
//        mNumberOfProducts.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void onLoadFinished(ShoppingCartData data) {
//        dismissProgressDialog();
//        IAPLog.d(IAPLog.LOG, data.getCtnNumber());
//
//    }

    @Override
    public void onLoadFinished(ArrayList<?> data) {
        if (data != null && data instanceof ArrayList)
            dismissProgressDialog();
        if (getActivity() == null) return;
        mData = (ArrayList<ShoppingCartData>) data;
        onOutOfStock(false);
        mAdapter = new CheckOutHistoryAdapter(mContext, mData, this);
        mAdapter.setOrderSummaryUpdateListner(this);
        if (mData.get(0) != null && mData.get(0).getDeliveryItemsQuantity() > 0) {
            updateCount(mData.get(0).getDeliveryItemsQuantity());
        }
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.tagProducts();
        String numberOfProducts = mContext.getResources().getString(R.string.iap_number_of_products);
        if (mData.size() == 1) {
            numberOfProducts = mContext.getResources().getString(R.string.iap_number_of_product);
        }
        numberOfProducts = String.format(numberOfProducts, mData.size());
        mNumberOfProducts.setText(numberOfProducts);
        mNumberOfProducts.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadError(Message msg) {
        dismissProgressDialog();
        if (!isNetworkConnected()) return;

        if (msg.obj instanceof IAPNetworkError) {
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);
        } else {
            NetworkUtility.getInstance().showErrorDialog(mContext, getFragmentManager(), mContext.getString(R.string.iap_ok),
                    mContext.getString(R.string.iap_server_error), mContext.getString(R.string.iap_something_went_wrong));
        }
    }

    @Override
    public void onGetUser(Message msg) {
        //NOP
    }

    @Override
    public void onSetDeliveryAddress(Message msg) {
        //NOP
    }

    @Override
    public void onGetDeliveryModes(Message msg) {
        if ((msg.obj instanceof IAPNetworkError)) {
            updateCartDetails(mShoppingCartAPI);
        } else if ((msg.obj instanceof GetDeliveryModes)) {
            GetDeliveryModes deliveryModes = (GetDeliveryModes) msg.obj;
            List<DeliveryModes> deliveryModeList = deliveryModes.getDeliveryModes();
            CartModelContainer.getInstance().setDeliveryModes(deliveryModeList);
            updateCartDetails(mShoppingCartAPI);
        }
    }

    @Override
    public void onSetDeliveryMode(Message msg) {
        if (msg.obj.equals(IAPConstant.IAP_SUCCESS)) {
            updateCartOnResume();
        } else {
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);
            dismissProgressDialog();
        }
    }

    @Override
    public void onCreateAddress(Message msg) {
        //NOP
    }

    @Override
    public void onRetailerError(IAPNetworkError errorMsg) {
        dismissProgressDialog();
    }

    @Override
    public void onItemClick(int position) {
        final List<DeliveryModes> deliveryModes = CartModelContainer.getInstance().getDeliveryModes();
        if (!isProgressDialogShowing())
            showProgressDialog(mContext, mContext.getString(R.string.iap_please_wait));
        mAddressController.setDeliveryMode(deliveryModes.get(position).getCode());

        /*final List<DeliveryModes> deliveryModes = CartModelContainer.getInstance().getDeliveryModes();
        mSelectedDeliveryMode = deliveryModes.get(position);

        if (!isProgressDialogShowing())
            showProgressDialog(mContext, mContext.getString(R.string.iap_please_wait));
        mAddressController.setDeliveryMode(deliveryModes.get(position).getCode());*/
    }

    @Override
    public void onPositiveBtnClick() {
        EventHelper.getInstance().notifyEventOccurred(IAPConstant.IAP_DELETE_PRODUCT);
    }

    @Override
    public void onNegativeBtnClick() {

    }

    @Override
    public void onMakePayment(Message msg) {
        dismissProgressDialog();
        if (msg.obj instanceof MakePaymentData) {

            //Track new billing address added action
            IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA, IAPAnalyticsConstant.SPECIAL_EVENTS,
                    IAPAnalyticsConstant.NEW_BILLING_ADDRESS_ADDED);

            MakePaymentData mMakePaymentData = (MakePaymentData) msg.obj;
            Bundle bundle = new Bundle();
            bundle.putString(ModelConstants.WEB_PAY_URL, mMakePaymentData.getWorldpayUrl());
            addFragment(WebPaymentFragment.createInstance(bundle, AnimationType.NONE), null);
        } else if (msg.obj instanceof IAPNetworkError) {
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);
        } else {
            NetworkUtility.getInstance().showErrorDialog(mContext, getFragmentManager(), mContext.getString(R.string.iap_ok),
                    mContext.getString(R.string.iap_server_error), mContext.getString(R.string.iap_something_went_wrong));
        }

    }

    @Override
    public void onPlaceOrder(final Message msg) {
        launchConfirmationScreen(new PlaceOrder());//need to remove
        /*if (msg.obj instanceof PlaceOrder) {
            PlaceOrder order = (PlaceOrder) msg.obj;
            String orderID = order.getCode();
            updateCount(0);
            CartModelContainer.getInstance().setOrderNumber(orderID);
            if (paymentMethodAvailable()) {
                dismissProgressDialog();
                launchConfirmationScreen((PlaceOrder) msg.obj);
            } else {
                mPaymentController.makPayment(orderID);
            }
        } else if (msg.obj instanceof IAPNetworkError) {
            dismissProgressDialog();
            IAPNetworkError iapNetworkError = (IAPNetworkError) msg.obj;
            if (null != iapNetworkError.getServerError()) {
                checkForOutOfStock(iapNetworkError, msg);
            } else {
                NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);
            }
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CvvCvcDialogFragment.REQUEST_CODE) {
            String securityCode = data.getStringExtra(
                    IAPConstant.CVV_KEY_BUNDLE);
            IAPLog.d(IAPLog.LOG, "CVV =" + securityCode);
            placeOrder(securityCode);
        }
    }


    private void launchConfirmationScreen(PlaceOrder details) {
        Bundle bundle = new Bundle();
        bundle.putString(ModelConstants.ORDER_NUMBER, details.getCode());
        bundle.putBoolean(ModelConstants.PAYMENT_SUCCESS_STATUS, Boolean.TRUE);
        addFragment(PaymentConfirmationFragment.createInstance(bundle, AnimationType.NONE), null);
    }

    private void checkForOutOfStock(final IAPNetworkError iapNetworkError, Message msg) {
        com.philips.cdp.di.iap.response.error.Error error = iapNetworkError.getServerError().getErrors().get(0);
        String type = error.getType();
        if (type.equalsIgnoreCase(IAPConstant.INSUFFICIENT_STOCK_LEVEL_ERROR)) {
            String subject = error.getMessage();
            NetworkUtility.getInstance().showErrorDialog(mContext, getFragmentManager(), getString(R.string.iap_ok),
                    getString(R.string.iap_out_of_stock), subject);
        } else {
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);
        }
    }

    @Override
    public void onGetCartUpdate() {
        dismissProgressDialog();
    }
}
