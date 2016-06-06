/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
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

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.adapters.OrderHistoryAdapter;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.controller.OrderController;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.response.orders.Orders;
import com.philips.cdp.di.iap.response.orders.OrdersData;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class PurchaseHistoryFragment extends BaseAnimationSupportFragment implements OrderController.OrderListener, EventListener {

    public static final String TAG = PurchaseHistoryFragment.class.getName();
    private OrderHistoryAdapter mAdapter;
    private Context mContext;
    private RecyclerView mOrderHistoryView;
    private List<Orders> mOrders = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.iap_order_history);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.iap_order_history_fragment, container, false);

        EventHelper.getInstance().registerEventNotification(String.valueOf(IAPConstant.PURCHASE_HISTORY_DETAIL), this);
        mOrderHistoryView = (RecyclerView) rootView.findViewById(R.id.order_history);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mOrderHistoryView.setLayoutManager(layoutManager);

        mAdapter = new OrderHistoryAdapter(getContext(), mOrders);
        mOrderHistoryView.setAdapter(mAdapter);
        if(mOrders.size() == 0)
            updateHistoryListOnResume();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public boolean onBackPressed() {
        Fragment fragment = getActivity().getSupportFragmentManager().
                findFragmentByTag(ProductCatalogFragment.TAG);
        if (fragment != null) {
            getFragmentManager().popBackStack();
        } else {
            finishActivity();
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.PURCHASE_HISTORY_DETAIL), this);
    }

    private void updateHistoryListOnResume() {
        OrderController controller = new OrderController(getContext(), this);
        if (!Utility.isProgressDialogShowing()) {
            Utility.showProgressDialog(getContext(), getString(R.string.iap_please_wait));
            controller.getOrderList();
        }
    }

    @Override
    public void onGetOrderList(Message msg) {
        Utility.dismissProgressDialog();
        if (msg.obj instanceof IAPNetworkError) {
            getIAPActivity().getNetworkUtility().showErrorMessage(msg, getFragmentManager(), getContext());
        } else {
            if (msg.what == RequestCode.GET_ORDERS) {
                if (msg.obj instanceof OrdersData) {
                    OrdersData mOrderData = (OrdersData) msg.obj;
                    mOrders = mOrderData.getOrders();
                    mAdapter = new OrderHistoryAdapter(getContext(), mOrders);
                    mOrderHistoryView.setAdapter(mAdapter);
                }
            }
        }

    }

    @Override
    public void onGetOrderDetail(Message msg) {

    }

    private void startOrderDetailFragment()
    {
        int pos = mAdapter.getSelectedPosition();
        Orders order = mOrders.get(pos);
        Bundle bundle = new Bundle();
        if (order != null) {
            bundle.putString(IAPConstant.PURCHASE_ID, order.getCode());
            addFragment(OrderDetailsFragment.createInstance(bundle, AnimationType.NONE), null);
        }
    }

    public static PurchaseHistoryFragment createInstance
            (Bundle args, BaseAnimationSupportFragment.AnimationType animType) {
        PurchaseHistoryFragment fragment = new PurchaseHistoryFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onEventReceived(String event) {
        if (event.equalsIgnoreCase(String.valueOf(IAPConstant.PURCHASE_HISTORY_DETAIL))) {
            startOrderDetailFragment();
        }
    }
}
