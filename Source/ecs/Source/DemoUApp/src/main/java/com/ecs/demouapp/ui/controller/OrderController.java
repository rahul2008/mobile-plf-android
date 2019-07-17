/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.controller;

import android.content.Context;
import android.os.Message;
import com.ecs.demouapp.ui.container.CartModelContainer;
import com.ecs.demouapp.ui.model.AbstractModel;
import com.ecs.demouapp.ui.model.ContactCallRequest;
import com.ecs.demouapp.ui.model.OrderDetailRequest;
import com.ecs.demouapp.ui.model.OrderHistoryRequest;
import com.ecs.demouapp.ui.response.orders.Consignment;
import com.ecs.demouapp.ui.response.orders.ConsignmentEntries;
import com.ecs.demouapp.ui.response.orders.Entries;
import com.ecs.demouapp.ui.response.orders.OrderDetail;
import com.ecs.demouapp.ui.response.orders.ProductData;
import com.ecs.demouapp.ui.session.HybrisDelegate;
import com.ecs.demouapp.ui.session.RequestCode;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.ModelConstants;
import com.philips.cdp.prxclient.datamodels.summary.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderController implements AbstractModel.DataLoadListener {

    private Context mContext;
    private OrderListener mOrderListener;
    private HybrisDelegate mDelegate;
    private StoreListener mStore;

    public interface OrderListener {
        void onGetOrderList(Message msg);

        void onGetOrderDetail(Message msg);

        void updateUiOnProductList();

        void onGetPhoneContact(Message msg);
    }

    public OrderController(Context context, OrderListener listener) {
        mContext = context;
        mOrderListener = listener;
    }

    public void getOrderList(int pageNo) {
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.CURRENT_PAGE, String.valueOf(pageNo));
        OrderHistoryRequest model = new OrderHistoryRequest(getStore(), query, this);
        getHybrisDelegate().sendRequest(RequestCode.GET_ORDERS, model, model);
    }

    public void getOrderDetails(String orderNumber) {
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.ORDER_NUMBER, orderNumber);
        OrderDetailRequest request = new OrderDetailRequest(getStore(), query, this);
        getHybrisDelegate().sendRequest(RequestCode.GET_ORDER_DETAIL, request, request);
    }

    public void getPhoneContact(String subCategory){
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.CATEGORY, subCategory);

        ContactCallRequest request = new ContactCallRequest(getStore(), query, this);
        getHybrisDelegate().sendRequest(RequestCode.GET_PHONE_CONTACT, request, request);
    }

    @Override
    public void onModelDataLoadFinished(Message msg) {
        sendListener(msg);
    }

    @Override
    public void onModelDataError(Message msg) {
        sendListener(msg);
    }

    private void sendListener(Message msg) {
        if (null == mOrderListener) return;

        int requestCode = msg.what;

        switch (requestCode) {
            case RequestCode.GET_ORDERS:
                mOrderListener.onGetOrderList(msg);
                break;
            case RequestCode.GET_ORDER_DETAIL:
                mOrderListener.onGetOrderDetail(msg);
                break;
            case RequestCode.GET_PHONE_CONTACT:
                mOrderListener.onGetPhoneContact(msg);
                break;
        }
    }

    public ArrayList<ProductData> getProductData(List<OrderDetail> orderDetail) {

        ArrayList<ProductData> products = new ArrayList<>();
        String ctn;
        for(OrderDetail detail : orderDetail) {
            if (detail.getDeliveryOrderGroups() != null) {
                List<Entries> entries = detail.getDeliveryOrderGroups().get(0).getEntries();
                for (Entries entry : entries) {
                    ctn = entry.getProduct().getCode();
                    ProductData productItem = new ProductData(entry);
                    Data data;
                    if (CartModelContainer.getInstance().isPRXSummaryPresent(ctn)) {
                        data = CartModelContainer.getInstance().getProductSummary(ctn);
                    } else {
                        continue;
                    }
                    setProductData(products, detail, entry, productItem, data);
                }
            }
        }

        return products;
    }

    public void setProductData(ArrayList<ProductData> products, OrderDetail detail, Entries entry, ProductData productItem, Data data) {
        productItem.setImageURL(data.getImageURL());
        productItem.setProductTitle(data.getProductTitle());
        productItem.setQuantity(entry.getQuantity());
        productItem.setFormatedPrice(String.valueOf(entry.getTotalPrice().getFormattedValue()));
        productItem.setCtnNumber(entry.getProduct().getCode());
        productItem.setOrderCode(detail.getCode());
        productItem.setSubCategory(data.getSubcategory());
        productItem.setMarketingTextHeader(data.getMarketingTextHeader());
        ConsignmentEntries entries = getEntriesFromConsignMent(detail, entry.getProduct().getCode());
        productItem.setTrackOrderUrl(getOrderTrackUrl(entries));
        products.add(productItem);
    }

    public ConsignmentEntries getEntriesFromConsignMent(OrderDetail detail, String ctn) {
        if (detail.getConsignments() == null) return null;
        for (Consignment consignment : detail.getConsignments()) {
            for (ConsignmentEntries entries : consignment.getEntries()) {
                String consignmentCtn = entries.getOrderEntry().getProduct().getCode();
                if (ctn.trim().equalsIgnoreCase(consignmentCtn.trim())) {
                    return entries;
                }
            }
        }
        return null;
    }


    public String getOrderTrackUrl(ConsignmentEntries entries) {
        if (entries == null) return null;
        if (isArrayNullOrEmpty(entries.getTrackAndTraceIDs()) || isArrayNullOrEmpty(entries.getTrackAndTraceUrls())) {
            return null;
        }
        String trackAndTraceID = entries.getTrackAndTraceIDs().get(0);
        String trackAndTraceUrl = entries.getTrackAndTraceUrls().get(0);
        return getTrackUrl(trackAndTraceID, trackAndTraceUrl);
    }

    private String getTrackUrl(String trackAndTraceID, String trackAndTraceUrl) {
        //sample URL
        //{300068874=http:\/\/www.fedex.com\/Tracking?action=track&cntry_code=us&tracknumber_list=300068874}
        String urlWithEndCurlyBrace = trackAndTraceUrl.replace("{" + trackAndTraceID + "=", "");
        return urlWithEndCurlyBrace.replace("}", "");
    }

    private boolean isArrayNullOrEmpty(List traceIdOrTraceURL) {
        return traceIdOrTraceURL == null || traceIdOrTraceURL.size() == 0;
    }

    public void setHybrisDelegate(HybrisDelegate delegate) {
        mDelegate = delegate;
    }

    HybrisDelegate getHybrisDelegate() {
        if (mDelegate == null) {
            mDelegate = HybrisDelegate.getInstance(mContext);
        }
        return mDelegate;
    }

    public void setStore(StoreListener store) {
        mStore = store;
    }

    StoreListener getStore() {
        if (mStore == null) {
            mStore = getHybrisDelegate().getStore();
        }
        return mStore;
    }
}
