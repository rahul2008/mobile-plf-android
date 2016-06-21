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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.adapters.OrderDetailAdapter;
import com.philips.cdp.di.iap.controller.OrderController;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.response.orders.OrderDetail;
import com.philips.cdp.di.iap.response.orders.ProductData;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.shamanland.fonticon.FontIconTextView;

import java.util.ArrayList;
import java.util.HashMap;


public class OrderDetailsFragment extends BaseAnimationSupportFragment implements OrderController.OrderListener, View.OnClickListener, AbstractModel.DataLoadListener {

    public static final String TAG = OrderDetailsFragment.class.getName();
    private Context mContext;

    ArrayList<ProductData> mProducts = new ArrayList<>();
    private TextView mTvQuantity;
    private TextView mTvtotalPrice;
    private TextView mTime;
    private TextView mOrderNumber;
    private TextView mOrderState;
    private TextView mDeliveryName;
    private TextView mDeliveryAddress;
    private TextView mBillingName;
    private TextView mBillingAddress;
    private ScrollView mParentView;
    private FontIconTextView mOrderDetailArrow;
    private TextView mPaymentCardType;
    private Button mBuyNow;
    private Button mCancelOrder;
    private RelativeLayout mTrackOrderLayout;
    private OrderDetail mOrderDetail;
    private RecyclerView mProductListView;
    private OrderDetailAdapter mAdapter;
    private LinearLayout mPaymentModeLayout;
    private OrderController mController;
    private View mPaymentDivider;
    private TextView mShippingStatus;

    private String mOrderId;

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.iap_order_details);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.iap_order_details_fragment, container, false);

        mParentView = (ScrollView) view.findViewById(R.id.scrollView);
        mTvQuantity = (TextView) view.findViewById(R.id.tv_quantity);
        mTvtotalPrice = (TextView) view.findViewById(R.id.tv_total_price);
        mTime = (TextView) view.findViewById(R.id.tv_time);
        mOrderNumber = (TextView) view.findViewById(R.id.tv_order_number);
        mOrderState = (TextView) view.findViewById(R.id.tv_order_state);
        mDeliveryName = (TextView) view.findViewById(R.id.tv_shipping_first_name);
        mDeliveryAddress = (TextView) view.findViewById(R.id.tv_shipping_address);
        mBillingName = (TextView) view.findViewById(R.id.tv_billing_first_name);
        mBillingAddress = (TextView) view.findViewById(R.id.tv_billing_address);
        mPaymentModeLayout = (LinearLayout) view.findViewById(R.id.ll_payment_mode);
        mPaymentCardType = (TextView) view.findViewById(R.id.tv_card_type);
        mBuyNow = (Button) view.findViewById(R.id.btn_paynow);
        mBuyNow.setOnClickListener(this);
        mCancelOrder = (Button) view.findViewById(R.id.btn_cancel);
        mCancelOrder.setOnClickListener(this);
        mTrackOrderLayout = (RelativeLayout) view.findViewById(R.id.track_order_layout);
        mTrackOrderLayout.setOnClickListener(this);
        mProductListView = (RecyclerView) view.findViewById(R.id.product_detail);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mProductListView.setLayoutManager(layoutManager);
        mProductListView.setNestedScrollingEnabled(false);
        mShippingStatus = (TextView) view.findViewById(R.id.shipping_status);

        mAdapter = new OrderDetailAdapter(mContext, mProducts);
        mProductListView.setAdapter(mAdapter);
        mPaymentDivider = (View) view.findViewById(R.id.payment_divider);

        Bundle bundle = getArguments();
        if (null != bundle && bundle.containsKey(IAPConstant.PURCHASE_ID)) {
            mOrderId = bundle.getString(IAPConstant.PURCHASE_ID);
            if (!(bundle.getString(IAPConstant.ORDER_STATUS).equalsIgnoreCase(IAPConstant.ORDER_COMPLETED))) {
                mTrackOrderLayout.setVisibility(View.GONE);
            }
            updateOrderDetailOnResume(mOrderId);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    public static OrderDetailsFragment createInstance
            (Bundle args, BaseAnimationSupportFragment.AnimationType animType) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    private void updateOrderDetailOnResume(String purchaseId) {
        OrderController controller = new OrderController(mContext, this);
        if (!Utility.isProgressDialogShowing()) {
            mParentView.setVisibility(View.GONE);
            Utility.showProgressDialog(mContext, getString(R.string.iap_please_wait));
            controller.getOrderDetails(purchaseId);
        }
    }

    @Override
    public void onGetOrderList(Message msg) {

    }

    @Override
    public void onGetOrderDetail(Message msg) {
        Utility.dismissProgressDialog();
        mParentView.setVisibility(View.VISIBLE);
        if (msg.obj instanceof IAPNetworkError) {
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);
        } else {
            if (msg.what == RequestCode.GET_ORDER_DETAIL) {
                if (msg.obj instanceof OrderDetail) {
                    mOrderDetail = (OrderDetail) msg.obj;
                    updateUIwithDetails(mOrderDetail);
                }
            }
        }

    }

    @Override
    public void updateUiOnProductList() {
        ArrayList<OrderDetail> detailList = new ArrayList<OrderDetail>();
        detailList.add(mOrderDetail);
        if(mController == null)
            mController = new OrderController(mContext, this);
        ArrayList<ProductData> productList=  mController.getProductData(detailList);
        for(ProductData product : productList)
            mProducts.add(product);
        mAdapter.notifyDataSetChanged();
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_cancel || v.getId() == R.id.btn_paynow)
            Toast.makeText(mContext, "Yet to implement", Toast.LENGTH_SHORT).show();
        else if (v.getId() == R.id.track_order_layout) {
            Bundle bundle = new Bundle();
            if (mOrderDetail != null) {
                bundle.putString(IAPConstant.PURCHASE_ID, mOrderDetail.getCode());
                if (mOrderDetail.getConsignments() != null && mOrderDetail.getConsignments().size() > 0 && mOrderDetail.getConsignments().get(0).getTrackingID() != null) {
                    bundle.putString(IAPConstant.TRACKING_ID, mOrderDetail.getConsignments().get(0).getTrackingID());
                }
                if (mOrderDetail.getDeliveryAddress() != null) {
                    bundle.putString(IAPConstant.DELIVERY_NAME, mOrderDetail.getDeliveryAddress().getFirstName() + " " + mOrderDetail.getDeliveryAddress().getLastName());
                    bundle.putString(IAPConstant.ADD_DELIVERY_ADDRESS, Utility.createAddress(mOrderDetail.getDeliveryAddress()));
                }

                if(mOrderDetail.getOrdertrackUrl() != null){
                    bundle.putString(IAPConstant.ORDER_TRACK_URL, mOrderDetail.getOrdertrackUrl());
                }
                addFragment(TrackOrderFragment.createInstance(bundle, AnimationType.NONE), null);
            }
        }

    }

    public void updateUIwithDetails(OrderDetail detail) {
        mTime.setText(Utility.getFormattedDate(detail.getCreated()));
        mOrderState.setText(detail.getStatusDisplay());
        mOrderNumber.setText(detail.getCode());
        if(detail.getDeliveryOrderGroups() != null)
        {
            if(mController == null)
                mController = new OrderController(mContext, this);
        }
        ArrayList<OrderDetail> detailList = new ArrayList<OrderDetail>();
        detailList.add(detail);
        mController.makePrxCall(detailList, this);

        if(detail.getTotalPriceWithTax() != null)
            mTvtotalPrice.setText(detail.getTotalPriceWithTax().getFormattedValue());
        if(detail.getTotalItems() != 0)
            mTvQuantity.setText(" (" + detail.getTotalItems() + " items)");

        if (detail.getDeliveryAddress() != null) {
            mDeliveryName.setText(detail.getDeliveryAddress().getFirstName() + " " + detail.getDeliveryAddress().getLastName());
            mDeliveryAddress.setText(Utility.createAddress(detail.getDeliveryAddress()));
        }
        if (detail.getPaymentInfo() != null) {
            if (detail.getPaymentInfo().getBillingAddress() != null) {
                mBillingName.setText(detail.getPaymentInfo().getBillingAddress().getFirstName() + " " + detail.getPaymentInfo().getBillingAddress().getLastName());
                mBillingAddress.setText(Utility.createAddress(detail.getPaymentInfo().getBillingAddress()));
            }
            if (detail.getPaymentInfo().getCardType() != null)
                mPaymentCardType.setText(detail.getPaymentInfo().getCardType().getCode() + " " + detail.getPaymentInfo().getCardNumber());
            else {
                mPaymentModeLayout.setVisibility(View.GONE);
                mPaymentDivider.setVisibility(View.GONE);
            }


        }

        if(detail.getStatusDisplay() != null && detail.getStatusDisplay().equalsIgnoreCase(IAPConstant.ORDER_COMPLETED))
        {
            mShippingStatus.setText(getString(R.string.iap_order_completed_text, detail.getConsignments().get(0).getTrackingID()));
        }
    }


    @Override
    public void onModelDataLoadFinished(Message msg) {
        if (processResponseFromPRX(msg)) return;
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();

    }

    @Override
    public void onModelDataError(Message msg) {

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
}
