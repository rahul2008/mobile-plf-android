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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.adapters.OrderDetailAdapter;
import com.philips.cdp.di.iap.controller.OrderController;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.response.orders.ContactsResponse;
import com.philips.cdp.di.iap.response.orders.OrderDetail;
import com.philips.cdp.di.iap.response.orders.ProductData;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;


public class OrderDetailsFragment extends InAppBaseFragment implements OrderController.OrderListener, View.OnClickListener, AbstractModel.DataLoadListener {

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
    private TextView mPaymentCardType;
    private OrderDetail mOrderDetail;
    private OrderDetailAdapter mAdapter;
    private LinearLayout mPaymentModeLayout;
    private OrderController mController;
    private View mPaymentDivider;
    private TextView mShippingStatus;
    private LinearLayout mProductListView;

    private String mPhoneContact;
    private String mOpeningHoursWeekdays;
    private String mOpeningHoursSaturday;


    @Override
    public void onResume() {
        super.onResume();
        setTitleAndBackButtonVisibility(R.string.iap_order_details, true);
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

        Button mBuyNow = (Button) view.findViewById(R.id.btn_paynow);
        mBuyNow.setOnClickListener(this);

        Button mCancelOrder = (Button) view.findViewById(R.id.btn_cancel);
        mCancelOrder.setOnClickListener(this);

        LinearLayout mTrackOrderLayout = (LinearLayout) view.findViewById(R.id.track_order_layout);
        mTrackOrderLayout.setOnClickListener(this);

        mProductListView = (LinearLayout) view.findViewById(R.id.product_detail);
        mShippingStatus = (TextView) view.findViewById(R.id.shipping_status);

        mAdapter = new OrderDetailAdapter(mContext, mProducts);
        mPaymentDivider = view.findViewById(R.id.payment_divider);

        Bundle bundle = getArguments();
        if (null != bundle) {
            if (bundle.containsKey(IAPConstant.ORDER_STATUS) && !(IAPConstant.ORDER_COMPLETED.equalsIgnoreCase(bundle.getString(IAPConstant.ORDER_STATUS))))
                mTrackOrderLayout.setVisibility(View.GONE);
            if (bundle.containsKey(IAPConstant.ORDER_DETAIL)) {
                mOrderDetail = bundle.getParcelable(IAPConstant.ORDER_DETAIL);
                updateUIwithDetails(mOrderDetail);
            }
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public static OrderDetailsFragment createInstance
            (Bundle args, InAppBaseFragment.AnimationType animType) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
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
        ArrayList<OrderDetail> detailList = new ArrayList<>();
        detailList.add(mOrderDetail);
        if (mController == null)
            mController = new OrderController(mContext, this);
        ArrayList<ProductData> productList = mController.getProductData(detailList);
        mProducts.clear();

        if (productList.size() > 0) {
            mController.getPhoneContact(productList.get(0).getSubCategory());
        }

        for (ProductData product : productList) {
            View productInfo = View.inflate(mContext, R.layout.iap_order_details_item, null);
            mProductListView.addView(productInfo);
            ((TextView) productInfo.findViewById(R.id.tv_productName)).setText(product.getProductTitle());
            ((TextView) productInfo.findViewById(R.id.tv_quantity)).setText(String.valueOf(product.getQuantity()));
            ((TextView) productInfo.findViewById(R.id.tv_total_price)).setText(product.getFormatedPrice());
            ((TextView) productInfo.findViewById(R.id.ctn_no)).setText(product.getCtnNumber());
            getNetworkImage(((NetworkImageView) productInfo.findViewById(R.id.iv_product_image)), product.getImageURL());
        }
        //     mProducts.add(product);
        mAdapter.notifyDataSetChanged();
        int totalQuantity = 0;
        for (ProductData data : productList) {
            totalQuantity += data.getQuantity();
        }
        mTvQuantity.setText(" (" + totalQuantity + " items)");
    }

    @Override
    public void onGetPhoneContact(Message msg) {
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
        if (msg.obj instanceof ContactsResponse) {
            ContactsResponse contactsResponse = (ContactsResponse) msg.obj;
            mPhoneContact = contactsResponse.getData().getPhone().get(0).getPhoneNumber();
            mOpeningHoursWeekdays = contactsResponse.getData().getPhone().get(0).getOpeningHoursWeekdays();
            mOpeningHoursSaturday = contactsResponse.getData().getPhone().get(0).getOpeningHoursSaturday();
        }
    }

    private void getNetworkImage(final NetworkImageView networkImage, final String imageURL) {
        ImageLoader mImageLoader;
        // Instantiate the RequestQueue.
        mImageLoader = NetworkImageLoader.getInstance(mContext)
                .getImageLoader();

        mImageLoader.get(imageURL, ImageLoader.getImageListener(networkImage,
                R.drawable.no_icon, android.R.drawable
                        .ic_dialog_alert));
        networkImage.setImageUrl(imageURL, mImageLoader);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_paynow)
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
                    bundle.putString(IAPConstant.ADD_DELIVERY_ADDRESS, Utility.formatAddress(mOrderDetail.getDeliveryAddress().getFormattedAddress()));
                }

                if (mOrderDetail.getOrdertrackUrl() != null) {
                    bundle.putString(IAPConstant.ORDER_TRACK_URL, mOrderDetail.getOrdertrackUrl());
                }
                addFragment(TrackOrderFragment.createInstance(bundle, AnimationType.NONE), TrackOrderFragment.TAG);
            }
        } else if (v.getId() == R.id.btn_cancel) {
            Bundle bundle = new Bundle();
            if (mOrderDetail != null) {
                if (mPhoneContact == null && mOpeningHoursSaturday == null && mOpeningHoursWeekdays == null) {
                    NetworkUtility.getInstance().showErrorDialog(mContext, getFragmentManager(),
                            mContext.getString(R.string.iap_ok), mContext.getString(R.string.iap_server_error), mContext.getString(R.string.iap_something_went_wrong));
                } else {
                    bundle.putString(IAPConstant.CUSTOMER_CARE_NUMBER, mPhoneContact);
                    bundle.putString(IAPConstant.CUSTOMER_CARE_WEEKDAYS_TIMING, mOpeningHoursWeekdays);
                    bundle.putString(IAPConstant.CUSTOMER_CARE_SATURDAY_TIMING, mOpeningHoursSaturday);
                    bundle.putString(IAPConstant.IAP_ORDER_ID, mOrderDetail.getCode());
                    addFragment(CancelOrderFragment.createInstance(bundle, AnimationType.NONE), CancelOrderFragment.TAG);
                }
            }
        }

    }

    public void updateUIwithDetails(OrderDetail detail) {
        mTime.setText(Utility.getFormattedDate(detail.getCreated()));
        String orderStatus = detail.getStatusDisplay();
        mOrderState.setText(orderStatus.substring(0, 1).toUpperCase() + orderStatus.substring(1));
        mOrderNumber.setText(detail.getCode());
        mTvQuantity.setText(" (" + mOrderDetail.getDeliveryItemsQuantity() + " item)");
        if (detail.getDeliveryOrderGroups() != null) {
            if (mController == null)
                mController = new OrderController(mContext, this);

            ArrayList<OrderDetail> detailList = new ArrayList<>();
            detailList.add(detail);
            mController.requestPrxData(detailList, this);
        }
        if (detail.getTotalPriceWithTax() != null) {
            mTvtotalPrice.setText(detail.getTotalPriceWithTax().getFormattedValue());
        }

        if (detail.getDeliveryAddress() != null) {
            mDeliveryName.setText(detail.getDeliveryAddress().getFirstName() + " " + detail.getDeliveryAddress().getLastName());
            mDeliveryAddress.setText(Utility.formatAddress(detail.getDeliveryAddress().getFormattedAddress()));
        }
        if (detail.getPaymentInfo() != null) {
            if (detail.getPaymentInfo().getBillingAddress() != null) {
                mBillingName.setText(detail.getPaymentInfo().getBillingAddress().getFirstName() + " " + detail.getPaymentInfo().getBillingAddress().getLastName());
                mBillingAddress.setText(Utility.formatAddress(detail.getPaymentInfo().getBillingAddress().getFormattedAddress()));
            }
            if (detail.getPaymentInfo().getCardType() != null) {
                mPaymentCardType.setText(detail.getPaymentInfo().getCardType().getCode() + " " + detail.getPaymentInfo().getCardNumber());
            } else {
                mPaymentModeLayout.setVisibility(View.GONE);
                mPaymentDivider.setVisibility(View.GONE);
            }


        }

        if (detail.getStatusDisplay() != null && detail.getStatusDisplay().equalsIgnoreCase(IAPConstant.ORDER_COMPLETED)) {
            if (detail.getConsignments() != null && detail.getConsignments().size() > 0) {
                mShippingStatus.setText(getString(R.string.iap_order_completed_text, detail.getConsignments().get(0).getTrackingID()));
            } else {
                mShippingStatus.setText(getString(R.string.iap_order_completed_text_without_track_id));
            }
        }
    }


    @Override
    public void onModelDataLoadFinished(Message msg) {
        if (processResponseFromPrx(msg)) return;
        if (Utility.isProgressDialogShowing()) {
            Utility.dismissProgressDialog();
        }

    }

    @Override
    public void onModelDataError(Message msg) {
        if (Utility.isProgressDialogShowing()) {
            Utility.dismissProgressDialog();
        }
    }


    @SuppressWarnings({"rawtype", "unchecked"})
    private boolean processResponseFromPrx(final Message msg) {
        if (msg.obj instanceof HashMap) {
            final HashMap obj = (HashMap) msg.obj;
            if (!obj.isEmpty()) {
                updateUiOnProductList();
            } else {
                Utility.dismissProgressDialog();
                return true;
            }
        }
        return false;
    }
}
