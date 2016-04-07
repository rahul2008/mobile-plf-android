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
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartAdapter;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.tagging.Tagging;

import java.util.ArrayList;

public class ShoppingCartFragment extends BaseAnimationSupportFragment
        implements View.OnClickListener, EventListener, AddressController.AddressListener, ShoppingCartAdapter.OutOfStockListener {

    private Button mCheckoutBtn;
    private Button mContinuesBtn;
    public ShoppingCartAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private AddressController mAddressController;
    private Context mContext;

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
        IAPLog.d(IAPLog.FRAGMENT_LIFECYCLE, "ShoppingCartFragment onCreateView");
        View rootView = inflater.inflate(R.layout.iap_shopping_cart_view, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.shopping_cart_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);


        mCheckoutBtn = (Button) rootView.findViewById(R.id.checkout_btn);
        mCheckoutBtn.setOnClickListener(this);
        mContinuesBtn = (Button) rootView.findViewById(R.id.continues_btn);
        mContinuesBtn.setOnClickListener(this);

        mAddressController = new AddressController(getContext(), this);
        mAdapter = new ShoppingCartAdapter(getContext(), new ArrayList<ShoppingCartData>(), getFragmentManager(), this);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.iap_shopping_cart);
        updateCartOnResume();
    }

    private void updateCartOnResume() {
        ShoppingCartPresenter presenter = new ShoppingCartPresenter(getContext(), mAdapter, getFragmentManager());
        if (!Utility.isProgressDialogShowing()) {
            Utility.showProgressDialog(getContext(), getString(R.string.iap_get_cart_details));
            updateCartDetails(presenter);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.onStop();
        NetworkUtility.getInstance().dismissErrorDialog();
    }

    private void updateCartDetails(ShoppingCartPresenter presenter) {
        presenter.getCurrentCartDetails();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.BUTTON_STATE_CHANGED), this);
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED), this);
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.PRODUCT_DETAIL_FRAGMENT), this);
    }

    @Override
    public void onClick(final View v) {
        if (v == mCheckoutBtn) {
            if (!Utility.isProgressDialogShowing()) {
                Utility.showProgressDialog(mContext, mContext.getResources().getString(R.string.iap_please_wait));
                mAddressController.getShippingAddresses();
            }
            //Track checkout action
            Tagging.trackAction(IAPAnalyticsConstant.SEND_DATA,
                    IAPAnalyticsConstant.SPECIAL_EVENTS, IAPAnalyticsConstant.CHECKOUT_BUTTON_SELECTED);
        }
        if (v == mContinuesBtn) {
            //Track continue shopping action
            Tagging.trackAction(IAPAnalyticsConstant.SEND_DATA, IAPAnalyticsConstant.SPECIAL_EVENTS,
                    IAPAnalyticsConstant.CONTINUE_SHOPPING_SELECTED);

            finishActivity();
        }
    }

    @Override
    public void onBackPressed() {
        //Track back button press action
        Tagging.trackAction(IAPAnalyticsConstant.SEND_DATA,
                IAPAnalyticsConstant.SPECIAL_EVENTS, IAPAnalyticsConstant.BACK_BUTTON_PRESS);
        finishActivity();
    }

    @Override
    public void raiseEvent(final String event) {
        // NOP
    }

    @Override
    public void onEventReceived(final String event) {
        if (event.equalsIgnoreCase(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED)) {
            IAPAnalytics.trackPage(IAPAnalyticsConstant.EMPTY_SHOPPING_CART_PAGE_NAME);
            addFragment(EmptyCartFragment.createInstance(new Bundle(), AnimationType.NONE), null);
        }
        if (event.equalsIgnoreCase(String.valueOf(IAPConstant.BUTTON_STATE_CHANGED))) {
            mCheckoutBtn.setEnabled(!Boolean.getBoolean(event));
        }
        if (event.equalsIgnoreCase(String.valueOf(IAPConstant.PRODUCT_DETAIL_FRAGMENT))) {
            startProductDetailFragment();
        }
    }

    private void startProductDetailFragment() {
        IAPAnalytics.trackPage(IAPAnalyticsConstant.SHOPPING_CART_ITEM_DETAIL_PAGE_NAME);
        ShoppingCartData shoppingCartData = mAdapter.getTheProductDataForDisplayingInProductDetailPage();
        Bundle bundle = new Bundle();
        bundle.putString(IAPConstant.PRODUCT_TITLE, shoppingCartData.getProductTitle());
        bundle.putString(IAPConstant.PRODUCT_CTN, shoppingCartData.getCtnNumber());
        bundle.putString(IAPConstant.PRODUCT_PRICE, shoppingCartData.getFormatedPrice());
        bundle.putString(IAPConstant.PRODUCT_OVERVIEW, shoppingCartData.getMarketingTextHeader());
        addFragment(ProductDetailFragment.createInstance(bundle, AnimationType.NONE), null);
    }

    @Override
    public void onGetAddress(Message msg) {
        Utility.dismissProgressDialog();
        if (msg.obj instanceof IAPNetworkError) {
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), getContext());
        } else {
            if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {
                IAPAnalytics.trackPage(IAPAnalyticsConstant.SHIPPING_ADDRESS_PAGE_NAME);
                addFragment(
                        ShippingAddressFragment.createInstance(new Bundle(), AnimationType.NONE), null);
            } else {
                IAPAnalytics.trackPage(IAPAnalyticsConstant.SHIPPING_ADDRESS_SELECTION_PAGE_NAME);
                addFragment(
                        AddressSelectionFragment.createInstance(new Bundle(), AnimationType.NONE), null);
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
    public void onGetDeliveryAddress(final Message msg) {
        //NOP
    }

    @Override
    public void onSetDeliveryModes(final Message msg) {
        //NOP
    }

    @Override
    public void onGetDeliveryModes(final Message msg) {

    }

    @Override
    public void onOutOfStock(boolean isOutOfStockReached) {
        if (isOutOfStockReached) {
            mCheckoutBtn.setEnabled(false);
        } else {
            mCheckoutBtn.setEnabled(true);
        }
    }
}
