package com.philips.cdp.di.iap.Fragments;

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
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.adapters.ShoppingCartAdapter;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.core.ControllerFactory;
import com.philips.cdp.di.iap.core.ShoppingCartAPI;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.response.State.RegionsList;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.tagging.Tagging;

import java.util.ArrayList;

public class ShoppingCartFragment extends BaseAnimationSupportFragment
        implements View.OnClickListener, EventListener, AddressController.AddressListener,
        ShoppingCartAdapter.OutOfStockListener, ShoppingCartPresenter.LoadListener<ShoppingCartData> {

    public static final String TAG = ShoppingCartFragment.class.getName();
    private Button mCheckoutBtn;
    private Button mContinuesBtn;
    public ShoppingCartAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private AddressController mAddressController;
    private Context mContext;
    private ShoppingCartAPI mShoppingCartAPI;

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
        IAPLog.d(IAPLog.FRAGMENT_LIFECYCLE, "ShoppingCartFragment onCreateView");
        View rootView = inflater.inflate(R.layout.iap_shopping_cart_view, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.shopping_cart_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mCheckoutBtn = (Button) rootView.findViewById(R.id.checkout_btn);
        mCheckoutBtn.setOnClickListener(this);
        mContinuesBtn = (Button) rootView.findViewById(R.id.continues_btn);
        mContinuesBtn.setOnClickListener(this);

        mShoppingCartAPI = ControllerFactory.getInstance()
                .getShoppingCartPresenter(getContext(), this, getFragmentManager());

        mAddressController = new AddressController(getContext(), this);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        IAPAnalytics.trackPage(IAPAnalyticsConstant.SHOPPING_CART_PAGE_NAME);
        setTitle(R.string.iap_shopping_cart);
        updateCartOnResume();
    }

    private void updateCartOnResume() {

        if (!Utility.isProgressDialogShowing()) {
            Utility.showProgressDialog(getContext(), getString(R.string.iap_get_cart_details));
        }
        updateCartDetails(mShoppingCartAPI);
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
    }

    @Override
    public void onClick(final View v) {
        if (v == mCheckoutBtn) {
            if (!Utility.isProgressDialogShowing()) {
                Utility.showProgressDialog(mContext, mContext.getResources().getString(R.string.iap_please_wait));
                mAddressController.getRegions();
            }
            //Track checkout action
            Tagging.trackAction(IAPAnalyticsConstant.SEND_DATA,
                    IAPAnalyticsConstant.SPECIAL_EVENTS, IAPAnalyticsConstant.CHECKOUT_BUTTON_SELECTED);

            if (mAdapter.isFreeDelivery()) {
                //Action to track free delivery
                Tagging.trackAction(IAPAnalyticsConstant.SEND_DATA,
                        IAPAnalyticsConstant.SPECIAL_EVENTS, IAPAnalyticsConstant.FREE_DELIVERY);
            }
        }
        if (v == mContinuesBtn) {
            if (isNetworkConnected()) return;

            //Track continue shopping action
            Tagging.trackAction(IAPAnalyticsConstant.SEND_DATA, IAPAnalyticsConstant.SPECIAL_EVENTS,
                    IAPAnalyticsConstant.CONTINUE_SHOPPING_SELECTED);
            EventHelper.getInstance().notifyEventOccurred(IAPConstant.IAP_LAUNCH_PRODUCT_CATALOG);
        }
    }

    @Override
    public boolean onBackPressed() {
        Fragment fragment = getFragmentManager().findFragmentByTag(ProductCatalogFragment.TAG);
        if (fragment == null) {
            finishActivity();
        }
        return false;
    }

    @Override
    public void onEventReceived(final String event) {
        if (event.equalsIgnoreCase(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED)) {
            addFragment(EmptyCartFragment.createInstance(new Bundle(), AnimationType.NONE), null);
        }
        if (event.equalsIgnoreCase(String.valueOf(IAPConstant.BUTTON_STATE_CHANGED))) {
            mCheckoutBtn.setEnabled(!Boolean.getBoolean(event));
        }
        if (event.equalsIgnoreCase(String.valueOf(IAPConstant.PRODUCT_DETAIL_FRAGMENT))) {
            startProductDetailFragment();
        }
        if (event.equalsIgnoreCase(String.valueOf(IAPConstant.IAP_LAUNCH_PRODUCT_CATALOG))) {
            launchProductCatalog();
        }
    }

    private void startProductDetailFragment() {
        ShoppingCartData shoppingCartData = mAdapter.getTheProductDataForDisplayingInProductDetailPage();
        Bundle bundle = new Bundle();
        bundle.putString(IAPConstant.PRODUCT_TITLE, shoppingCartData.getProductTitle());
        bundle.putString(IAPConstant.PRODUCT_CTN, shoppingCartData.getCtnNumber());
        bundle.putString(IAPConstant.PRODUCT_PRICE, shoppingCartData.getFormatedPrice());
        bundle.putString(IAPConstant.PRODUCT_VALUE_PRICE, shoppingCartData.getValuePrice());
        bundle.putString(IAPConstant.PRODUCT_OVERVIEW, shoppingCartData.getMarketingTextHeader());
        addFragment(ProductDetailFragment.createInstance(bundle, AnimationType.NONE), ProductDetailFragment.TAG);
    }

    @Override
    public void onGetAddress(Message msg) {
        Utility.dismissProgressDialog();
        if (msg.obj instanceof IAPNetworkError) {
            NetworkUtility.getInstance().showErrorMessage(mErrorDialogListener, msg, getFragmentManager(), getContext());
        } else {
            if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {
                addFragment(
                        ShippingAddressFragment.createInstance(new Bundle(), AnimationType.NONE), ShippingAddressFragment.TAG);
            } else {
                addFragment(
                        AddressSelectionFragment.createInstance(new Bundle(), AnimationType.NONE), AddressSelectionFragment.TAG);
            }
        }
    }

    @Override
    public void onCreateAddress(Message msg) {
        //NOP
    }

    @Override
    public void onSetDeliveryAddress(final Message msg) {
        //NOP
    }

    @Override
    public void onSetDeliveryModes(final Message msg) {
        //NOP
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
        mAddressController.getShippingAddresses();
    }

    @Override
    public void onOutOfStock(boolean isOutOfStockReached) {
        if (isOutOfStockReached) {
            mCheckoutBtn.setEnabled(false);
        } else {
            mCheckoutBtn.setEnabled(true);
        }
    }

    @Override
    public void onLoadFinished(final ArrayList<ShoppingCartData> data) {
        if (getActivity() == null) return;

        onOutOfStock(false);
        mContinuesBtn.setVisibility(View.VISIBLE);
        mCheckoutBtn.setVisibility(View.VISIBLE);
        mAdapter = new ShoppingCartAdapter(getContext(), data, getFragmentManager(), this, mShoppingCartAPI);
        if (data.get(0) != null && data.get(0).getDeliveryItemsQuantity() > 0) {
            updateCount(data.get(0).getDeliveryItemsQuantity());
        }
        mRecyclerView.setAdapter(mAdapter);
    }
}
