/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.controller.OrderController;
import com.philips.cdp.di.iap.response.orders.OrderDetail;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.shamanland.fonticon.FontIconTextView;


public class OrderDetailsFragment extends BaseAnimationSupportFragment implements OrderController.OrderListener, View.OnClickListener {

    public static final String TAG = OrderDetailsFragment.class.getName();
    private Context mContext;
    TextView mTvProductName;
    NetworkImageView mNetworkImage;
    TextView mTvQuantity;
    TextView mTvtotalPrice;
    TextView mTime;
    TextView mOrderNumber;
    TextView mOrderState;
    TextView mDeliveryName;
    TextView mDeliveryAddress;
    TextView mBillingName;
    TextView mBillingAddress;
    ScrollView mParentView;
    FontIconTextView mOrderDetailArrow;
    TextView mPaymentCardType;
    Button mBuyNow;
    Button mCancelOrder;


    String mOrderId;

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.iap_order_details);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.iap_order_details_fragment, container, false);

        mParentView = (ScrollView) view.findViewById(R.id.scrollView);
        mTvProductName = (TextView) view.findViewById(R.id.tv_productName);
        mNetworkImage = (NetworkImageView) view.findViewById(R.id.iv_product_image);
        mTvQuantity = (TextView) view.findViewById(R.id.tv_total_item);
        mTvtotalPrice = (TextView) view.findViewById(R.id.tv_total_price);
        mTime = (TextView) view.findViewById(R.id.tv_time);
        mOrderNumber = (TextView) view.findViewById(R.id.tv_order_number);
        mOrderState = (TextView) view.findViewById(R.id.tv_order_state);
        mDeliveryName = (TextView) view.findViewById(R.id.tv_shipping_first_name);
        mDeliveryAddress = (TextView) view.findViewById(R.id.tv_shipping_address);
        mBillingName = (TextView) view.findViewById(R.id.tv_billing_first_name);
        mBillingAddress = (TextView) view.findViewById(R.id.tv_billing_address);
        mOrderDetailArrow = (FontIconTextView) view.findViewById(R.id.arrow);
        mOrderDetailArrow.setVisibility(View.GONE);
        mPaymentCardType = (TextView) view.findViewById(R.id.tv_card_type);
        mBuyNow = (Button) view.findViewById(R.id.btn_paynow);
        mBuyNow.setOnClickListener(this);
        mCancelOrder = (Button) view.findViewById(R.id.btn_cancel);
        mCancelOrder.setOnClickListener(this);

        Bundle bundle = getArguments();
        if (null != bundle && bundle.containsKey(IAPConstant.PURCHASE_ID)) {
            mOrderId = bundle.getString(IAPConstant.PURCHASE_ID);
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
        OrderController controller = new OrderController(getContext(), this);
        if (!Utility.isProgressDialogShowing()) {
            mParentView.setVisibility(View.GONE);
            Utility.showProgressDialog(getContext(), getString(R.string.iap_please_wait));
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
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), getContext());
        } else {
            if (msg.what == RequestCode.GET_ORDER_DETAIL) {
                if (msg.obj instanceof OrderDetail) {
                    OrderDetail detail = (OrderDetail) msg.obj;
                    updateUIwithDetails(detail);
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_cancel || v.getId() == R.id.btn_paynow)
            Toast.makeText(getContext(), "Yet to implement", Toast.LENGTH_SHORT).show();

    }

    public void updateUIwithDetails(OrderDetail detail) {
        mTime.setText(Utility.getFormattedDate(detail.getCreated()));
        mOrderState.setText(detail.getStatusDisplay());
        mOrderNumber.setText(detail.getCode());
        mDeliveryName.setText(detail.getDeliveryAddress().getFirstName() + " " + detail.getDeliveryAddress().getLastName());
        mDeliveryAddress.setText(Utility.createAddress(detail.getDeliveryAddress()));
        mBillingName.setText(detail.getPaymentInfo().getBillingAddress().getFirstName() + " " + detail.getPaymentInfo().getBillingAddress().getLastName());
        mBillingAddress.setText(Utility.createAddress(detail.getPaymentInfo().getBillingAddress()));
        mPaymentCardType.setText(detail.getPaymentInfo().getCardType().getCode() + " " + detail.getPaymentInfo().getCardNumber());
    }
}
