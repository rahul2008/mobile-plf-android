/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
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
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.response.orders.OrderDetail;
import com.philips.cdp.di.iap.response.orders.Orders;
import com.philips.cdp.di.iap.response.orders.OrdersData;
import com.philips.cdp.di.iap.response.orders.ProductData;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PurchaseHistoryFragment extends BaseAnimationSupportFragment implements OrderController.OrderListener, EventListener, AbstractModel.DataLoadListener {

    public static final String TAG = PurchaseHistoryFragment.class.getName();
    private OrderHistoryAdapter mAdapter;
    private Context mContext;
    private RecyclerView mOrderHistoryView;
    private List<Orders> mOrders = new ArrayList<>();
    private  OrderController mController;
    ArrayList<OrderDetail> mOrderDetails = new ArrayList<>();
    ArrayList<ProductData> mProducts = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        IAPAnalytics.trackPage(IAPAnalyticsConstant.ORDER_HISTORY_PAGE_NAME);
        setTitle(R.string.iap_order_history);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.iap_order_history_fragment, container, false);

        EventHelper.getInstance().registerEventNotification(String.valueOf(IAPConstant.PURCHASE_HISTORY_DETAIL), this);

        mOrderHistoryView = (RecyclerView) rootView.findViewById(R.id.order_history);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mOrderHistoryView.setLayoutManager(layoutManager);

        mAdapter = new OrderHistoryAdapter(mContext, mOrders, mProducts);
        mOrderHistoryView.setAdapter(mAdapter);
        if (mOrders.size() == 0)
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
        finishActivity();
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.PURCHASE_HISTORY_DETAIL), this);
    }

    private void updateHistoryListOnResume() {
        mController = new OrderController(mContext, this);
        if (!Utility.isProgressDialogShowing()) {
                Utility.showProgressDialog(mContext, getString(R.string.iap_please_wait));
            mController.getOrderList();
        }
    }

    @Override
    public void onGetOrderList(Message msg) {
        if (msg.obj instanceof IAPNetworkError) {
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);
        } else {
            if (msg.what == RequestCode.GET_ORDERS) {
                if (msg.obj instanceof OrdersData) {
                    OrdersData mOrderData = (OrdersData) msg.obj;
                    if (mOrderData.getOrders() == null || mOrderData.getOrders().size() == 0) {
                        if (Utility.isProgressDialogShowing())
                            Utility.dismissProgressDialog();
                        removeFragment();
                        addFragment(EmptyPurchaseHistoryFragment.createInstance(new Bundle(),
                                BaseAnimationSupportFragment.AnimationType.NONE), EmptyPurchaseHistoryFragment.TAG);
                    } else {
                        for(Orders order : mOrderData.getOrders())
                            mOrders.add(order);
                        for(int i=0; i < mOrders.size(); i++)
                        {
                            if(mController == null)
                                mController = new OrderController(mContext, this);
                            mController.getOrderDetails(mOrders.get(i).getCode());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onGetOrderDetail(Message msg) {
        if (msg.obj instanceof IAPNetworkError) {
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);
        } else {
            if (msg.what == RequestCode.GET_ORDER_DETAIL) {
                if (msg.obj instanceof OrderDetail) {
                    OrderDetail orderDetail = (OrderDetail) msg.obj;
                    mOrderDetails.add(orderDetail);
                    if(mOrderDetails.size() == mOrders.size())
                    {
                        updateProductDetails(mOrderDetails);
                    }
                }
            }
        }

    }

    @Override
    public void updateUiOnProductList() {
        if(mController == null)
            mController = new OrderController(mContext, this);
        ArrayList<ProductData> productList=  mController.getProductData(mOrderDetails);
        for(ProductData product : productList)
                mProducts.add(product);
        mAdapter.notifyDataSetChanged();
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }

    private void startOrderDetailFragment() {
        if (isNetworkNotConnected()) return;
        int pos = mAdapter.getSelectedPosition();
        Orders order = mOrders.get(pos);
        Bundle bundle = new Bundle();
        if (order != null) {
            bundle.putString(IAPConstant.PURCHASE_ID, order.getCode());
            bundle.putString(IAPConstant.ORDER_STATUS, order.getStatusDisplay());
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
    private void updateProductDetails(List<OrderDetail> orderDetails)
    {
        if(mController == null)
            mController = new OrderController(mContext, this);
        mController.makePrxCall(orderDetails, this);
    }

    private boolean processResponseFromPRX(final Message msg) {
        if (msg.obj instanceof HashMap) {
            HashMap<String, SummaryModel> prxModel = (HashMap<String, SummaryModel>) msg.obj;
            if (prxModel == null || prxModel.size() == 0) {
                Utility.dismissProgressDialog();
                return true;
            }
            updateUiOnProductList();
        }
        return false;
    }

    @Override
    public void onModelDataLoadFinished(Message msg) {
        if (processResponseFromPRX(msg)) return;
     //   if (Utility.isProgressDialogShowing())
      //      Utility.dismissProgressDialog();
    }

    @Override
    public void onModelDataError(Message msg) {

    }
}
