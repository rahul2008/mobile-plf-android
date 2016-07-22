/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.activity.IAPActivity;
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
import com.philips.cdp.di.iap.utils.IAPLog;
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
    private OrderController mController;
    private int mTotalOrders = 0;
    private int mPageSize = 0;
    private int mPageNo = 0;
    private int mRemainingOrders = 0;
    private boolean mIsLoading = false;
    private int mOrderCount = 0;

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

        mAdapter = new OrderHistoryAdapter(mContext, mOrders, mProducts, mOrderDetails);
        mOrderHistoryView.setAdapter(mAdapter);
        mOrderHistoryView.addOnScrollListener(mRecyclerViewOnScrollListener);
        if (mOrders.isEmpty()) {
            updateHistoryListOnResume();
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public boolean onBackPressed() {
        if (getActivity() != null && getActivity() instanceof IAPActivity) {
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
        mController = new OrderController(mContext, this);
        if (!Utility.isProgressDialogShowing()) {
            Utility.showProgressDialog(mContext, getString(R.string.iap_please_wait));
            mController.getOrderList(mPageNo);
        }
    }

    @Override
    public void onGetOrderList(Message msg) {
        if (msg.obj instanceof IAPNetworkError) {
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);
        } else {
            if (msg.what == RequestCode.GET_ORDERS) {
                if (msg.obj instanceof OrdersData) {
                    OrdersData orderData = (OrdersData) msg.obj;
                    if (orderData.getOrders() == null || orderData.getOrders().size() == 0) {
                        if (Utility.isProgressDialogShowing()) {
                            Utility.dismissProgressDialog();
                        }
                        addFragment(EmptyPurchaseHistoryFragment.createInstance(new Bundle(),
                                BaseAnimationSupportFragment.AnimationType.NONE), EmptyPurchaseHistoryFragment.TAG);
                    } else {
                        for (Orders order : orderData.getOrders())
                            mOrders.add(order);
                        if (mTotalOrders == 0) {
                            mRemainingOrders = orderData.getPagination().getTotalResults();
                        }
                        mTotalOrders = orderData.getPagination().getTotalResults();
                        mPageSize = orderData.getPagination().getPageSize();
                        mPageNo = orderData.getPagination().getCurrentPage();
                        mIsLoading = false;
                        mOrderCount = mPageNo * mPageSize;
                        for (int i = mPageNo * mPageSize; i < mOrders.size(); i++) {
                            if (mController == null)
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
        mOrderCount++;
        if (msg.obj instanceof IAPNetworkError) {
  //          NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);
            IAPLog.d(TAG,((IAPNetworkError) msg.obj).getMessage());
        } else {
            if (msg.what == RequestCode.GET_ORDER_DETAIL) {
                if (msg.obj instanceof OrderDetail) {
                    OrderDetail orderDetail = (OrderDetail) msg.obj;
                    mOrderDetails.add(orderDetail);
                }
            }
        }
        if (mOrderCount == mOrders.size()) {
            updateProductDetails(mOrderDetails);
        }

    }

    @Override
    public void updateUiOnProductList() {
        if (mController == null)
            mController = new OrderController(mContext, this);
        ArrayList<ProductData> productList = mController.getProductData(mOrderDetails);
        mProducts.clear();
        for (ProductData product : productList)
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
            for(OrderDetail detail : mOrderDetails)
            {
                if(detail.getCode().equals(order.getCode())){
                    bundle.putParcelable(IAPConstant.ORDER_DETAIL, (Parcelable) detail);
                    break;
                }
            }
            if(bundle.getParcelable(IAPConstant.ORDER_DETAIL) != null)
                addFragment(OrderDetailsFragment.createInstance(bundle, AnimationType.NONE), OrderDetailsFragment.TAG);
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

    private void updateProductDetails(List<OrderDetail> orderDetails) {
        if (mController == null)
            mController = new OrderController(mContext, this);
        mController.requestPrxData(orderDetails, this);
    }

    //TODO
    @SuppressWarnings({"rawtype", "unchecked"})
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
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }

    @Override
    public void onModelDataError(Message msg) {
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }

    private RecyclerView.OnScrollListener
            mRecyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView,
                                         int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager mLayoutManager = (LinearLayoutManager) mOrderHistoryView
                    .getLayoutManager();

            int visibleItemCount = mLayoutManager.getChildCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

            if (!mIsLoading) {
                //if scrolled beyond page size and we have more items to load
                if ((visibleItemCount + firstVisibleItemPosition) >= mLayoutManager.getItemCount()
                        && firstVisibleItemPosition >= 0
                        && mRemainingOrders > mPageSize) {
                    mIsLoading = true;
                    IAPLog.d(TAG, "visibleItem " + visibleItemCount + ", firstvisibleItemPistion " + firstVisibleItemPosition + "itemCount " + mLayoutManager.getItemCount());
                    loadMoreItems();
                }
            }
        }
    };

    private void loadMoreItems() {
        if (!Utility.isProgressDialogShowing())
            Utility.showProgressDialog(mContext, getString(R.string.iap_please_wait));
        mRemainingOrders = mRemainingOrders - mPageSize;
        mController.getOrderList(++mPageNo);
    }
}
