/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.controller;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.OrderDetailRequest;
import com.philips.cdp.di.iap.model.OrderHistoryRequest;
import com.philips.cdp.di.iap.prx.PRXDataBuilder;
import com.philips.cdp.di.iap.response.orders.Entries;
import com.philips.cdp.di.iap.response.orders.OrderDetail;
import com.philips.cdp.di.iap.response.orders.ProductData;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.prxclient.datamodels.summary.Data;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderController implements AbstractModel.DataLoadListener {

    private Context mContext;
    private OrderListener mOrderListener;
    private HybrisDelegate mDelegate;
    private StoreSpec mStore;

    public interface OrderListener {
        void onGetOrderList(Message msg);
        void onGetOrderDetail(Message msg);
        void updateUiOnProductList();
    }

    public OrderController(Context context, OrderListener listener) {
        mContext = context;
        mOrderListener = listener;
    }

    public void getOrderList(int pageNo) {
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.CURRENT_PAGE, String.valueOf(pageNo));
        OrderHistoryRequest model = new OrderHistoryRequest(getStore(), query, this);
        model.setContext(mContext);
        getHybrisDelegate().sendRequest(RequestCode.GET_ORDERS, model, model);
    }

    public void getOrderDetails(String orderNumber) {
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.ORDER_NUMBER, orderNumber);
        OrderDetailRequest request = new OrderDetailRequest(getStore(), query, this);
        request.setContext(mContext);
        getHybrisDelegate().sendRequest(RequestCode.GET_ORDER_DETAIL, request, request);
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
        }
    }

    public void makePrxCall(final List<OrderDetail> details, AbstractModel.DataLoadListener listener) {
        ArrayList<String> ctnsToBeRequestedForPRX = new ArrayList<>();
        ArrayList<String> productsToBeShown = new ArrayList<>();
        String ctn;
        CartModelContainer cartModelContainer = CartModelContainer.getInstance();

        for(OrderDetail detail : details)
        {
            List<Entries> entries = detail.getDeliveryOrderGroups().get(0).getEntries();
            for (Entries entry : entries) {
                ctn = entry.getProduct().getCode();
                productsToBeShown.add(ctn);
                if (!cartModelContainer.isPRXDataPresent(ctn)) {
                    ctnsToBeRequestedForPRX.add(ctn);
                }
            }
        }

        if (ctnsToBeRequestedForPRX.size() > 0) {
            PRXDataBuilder builder = new PRXDataBuilder(mContext, ctnsToBeRequestedForPRX, listener);
            builder.preparePRXDataRequest();
        } else {
            HashMap<String, SummaryModel> prxModel = new HashMap<>();
            for (String ctnPresent : productsToBeShown) {
                prxModel.put(ctnPresent, cartModelContainer.getProductData(ctnPresent));
            }
            mOrderListener.updateUiOnProductList();
        }
    }

    public ArrayList<ProductData> getProductData(List<OrderDetail> orderDetail) {

        HashMap<String, SummaryModel> list = CartModelContainer.getInstance().getPRXDataObjects();
        ArrayList<ProductData> products = new ArrayList<>();
        String ctn;
        for(OrderDetail detail : orderDetail)
        {
            List<Entries> entries = detail.getDeliveryOrderGroups().get(0).getEntries();
            for (Entries entry : entries) {
                ctn = entry.getProduct().getCode();
                ProductData productItem = new ProductData(entry);
                Data data;
                if (list.containsKey(ctn)) {
                    data = list.get(ctn).getData();
                } else {
                    continue;
                }
                productItem.setImageURL(data.getImageURL());
                productItem.setProductTitle(data.getProductTitle());
                productItem.setQuantity(entry.getQuantity());
                productItem.setFormatedPrice(String.valueOf(entry.getTotalPrice().getFormattedValue()));
                productItem.setCtnNumber(entry.getProduct().getCode());
                productItem.setOrderCode(detail.getCode());
                products.add(productItem);
            }
        }

        return products;
    }

    HybrisDelegate getHybrisDelegate() {
        if (mDelegate == null) {
            mDelegate = HybrisDelegate.getInstance(mContext);
        }
        return mDelegate;
    }

    StoreSpec getStore() {
        if (mStore == null) {
            mStore = getHybrisDelegate().getStore();
        }
        return mStore;
    }
}
