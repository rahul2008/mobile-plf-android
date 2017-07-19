/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.activity.IAPActivity;
import com.philips.cdp.di.iap.adapters.ShoppingCartAdapter;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.cart.ShoppingCartAPI;
import com.philips.cdp.di.iap.cart.ShoppingCartData;
import com.philips.cdp.di.iap.cart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.controller.ControllerFactory;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.response.State.RegionsList;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;
import com.philips.cdp.di.iap.response.addresses.GetDeliveryModes;
import com.philips.cdp.di.iap.response.addresses.GetShippingAddressData;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.NetworkUtility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartFragment extends InAppBaseFragment
        implements View.OnClickListener, EventListener, AddressController.AddressListener,
        ShoppingCartAdapter.OutOfStockListener, ShoppingCartPresenter.ShoppingCartListener<ShoppingCartData>,
        DeliveryModeDialog.DialogListener {

    public static final String TAG = ShoppingCartFragment.class.getName();
    private Context mContext;

    private Button mCheckoutBtn;
    private Button mContinuesBtn;

    public ShoppingCartAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private AddressController mAddressController;
    private ShoppingCartAPI mShoppingCartAPI;
    private ArrayList<ShoppingCartData> mData = new ArrayList<>();
    private List<Addresses> mAddresses = new ArrayList<>();
    private DeliveryModes mSelectedDeliveryMode;

    public static ShoppingCartFragment createInstance(Bundle args, AnimationType animType) {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
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

        View rootView = inflater.inflate(R.layout.iap_shopping_cart_view, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.shopping_cart_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mCheckoutBtn = (Button) rootView.findViewById(R.id.checkout_btn);
        mCheckoutBtn.setOnClickListener(this);
        mContinuesBtn = (Button) rootView.findViewById(R.id.continues_btn);
        mContinuesBtn.setOnClickListener(this);
        mShoppingCartAPI = ControllerFactory.getInstance()
                .getShoppingCartPresenter(mContext, this);
        mAddressController = new AddressController(mContext, this);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        IAPAnalytics.trackPage(IAPAnalyticsConstant.SHOPPING_CART_PAGE_NAME);
        IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                IAPAnalyticsConstant.SPECIAL_EVENTS, IAPAnalyticsConstant.SHOPPING_CART_VIEW);
        setTitleAndBackButtonVisibility(R.string.iap_shopping_cart, true);
        if (isNetworkConnected()) {
            updateCartOnResume();
        }

//        mAdapter = new ShoppingCartAdapter(mContext, mData, this);
//        mRecyclerView.setAdapter(mAdapter);
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
    }

    @Override
    public void onClick(final View v) {
        if (v == mCheckoutBtn) {
            if (!isProgressDialogShowing()) {
                showProgressDialog(mContext,
                        mContext.getResources().getString(R.string.iap_please_wait));
                mAddressController.getRegions();
            }

            //Track checkout action
            IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                    IAPAnalyticsConstant.SPECIAL_EVENTS, IAPAnalyticsConstant.CHECKOUT_BUTTON_SELECTED);

            if (mAdapter.isFreeDelivery()) {
                //Action to track free delivery
                IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                        IAPAnalyticsConstant.SPECIAL_EVENTS, IAPAnalyticsConstant.FREE_DELIVERY);
            }
        }
        if (v == mContinuesBtn) {
            if (!isNetworkConnected()) return;

            //Track continue shopping action
            IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA, IAPAnalyticsConstant.SPECIAL_EVENTS,
                    IAPAnalyticsConstant.CONTINUE_SHOPPING_SELECTED);
            EventHelper.getInstance().notifyEventOccurred(IAPConstant.IAP_LAUNCH_PRODUCT_CATALOG);
        }
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
            mCheckoutBtn.setEnabled(!Boolean.getBoolean(event));
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
            DeliveryModeDialog mDeliveryModeDialog = new DeliveryModeDialog
                    (mContext, this);
            mDeliveryModeDialog.showDialog();
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
        if (mCheckoutBtn == null) return;
        if (isOutOfStockReached) {
            mCheckoutBtn.setEnabled(false);
        } else {
            mCheckoutBtn.setEnabled(true);
        }
    }

    @Override
    public void onLoadFinished(final ArrayList<ShoppingCartData> data) {
        dismissProgressDialog();
        if (getActivity() == null) return;
        mData = data;
        onOutOfStock(false);
        mAdapter = new ShoppingCartAdapter(mContext, mData, this);
        if (data.get(0) != null && data.get(0).getDeliveryItemsQuantity() > 0) {
            updateCount(data.get(0).getDeliveryItemsQuantity());
        }
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.tagProducts();
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
        dismissProgressDialog();
    }

    @Override
    public void onSetDeliveryMode(Message msg) {
        updateCartDetails(mShoppingCartAPI);
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
        mSelectedDeliveryMode = deliveryModes.get(position);

        if (!isProgressDialogShowing())
            showProgressDialog(mContext, mContext.getString(R.string.iap_please_wait));
        mAddressController.setDeliveryMode(deliveryModes.get(position).getCode());
    }
}
