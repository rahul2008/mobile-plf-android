/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.ShoppingCart;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.activity.EmptyCartActivity;
import com.philips.cdp.di.iap.model.CartModel;
import com.philips.cdp.di.iap.response.cart.Entries;
import com.philips.cdp.di.iap.response.cart.GetCartData;
import com.philips.cdp.di.iap.response.cart.UpdateCartData;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.prxdatabuilder.ProductSummaryBuilder;
import com.philips.cdp.prxclient.prxdatamodels.summary.Data;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShoppingCartPresenter {
    private static final  String TAG = ShoppingCartPresenter.class.getName();
    Context mContext;
    ArrayList<ShoppingCartData> mProductData;
    private LoadListener mLoadListener;
    private Resources mResources;

    public interface LoadListener {
        void onLoadFinished(ArrayList<ShoppingCartData> data);
    }

    public ShoppingCartPresenter(Context context, LoadListener listener){
        mContext = context;
        mProductData = new ArrayList<ShoppingCartData>();
        mLoadListener = listener;
        mResources = mContext.getResources();
    }

    //TODO: fix with TAG
    private void addShippingCostRowToTheList() {
        ShoppingCartData summary = new ShoppingCartData();
        mProductData.add(summary);
        mProductData.add(summary);
        mProductData.add(summary);
    }

    public void getCurrentCartDetails(){
            HybrisDelegate.getInstance(mContext).sendRequest(RequestCode.GET_CART,
                    new RequestListener() {
                        @Override
                        public void onSuccess(Message msg) {
                            GetCartData data = (GetCartData) msg.obj;

                            if (data.getEntries() == null) {
                                Intent intent = new Intent(mContext, EmptyCartActivity.class);
                                mContext.startActivity(intent);
                                Utility.dismissProgressDialog();
                                return;
                            }

                            ShoppingCartData item = new ShoppingCartData();
                            item.setQuantity(data.getEntries().get(0).getQuantity());
                            item.setTotalPrice(data.getTotalPrice().getValue());
                            item.setCurrency(data.getTotalPrice().getCurrencyIso());
                            item.setTotalItems(data.getTotalItems());
                            item.setCartNumber(data.getCode());

                            List<Entries> list = data.getEntries();
                            for (int i = 0; i < list.size(); i++) {
                                getProductDetails(item, list.get(i));
                                item.setStockLevel(data.getEntries().get(i).getProduct().getStock()
                                        .getStockLevel());
                            }
                        }

                        @Override
                        public void onError(Message msg) {
                            Toast.makeText(mContext, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            Utility.dismissProgressDialog();
                        }
                    }, null);
    }

    public void getProductDetails(final ShoppingCartData summary, final Entries entry){
        //TODO: Should be coming from configuration xml
        if (Utility.isInternetConnected(mContext)) {
            final String code = entry.getProduct().getCode();
            String mCtn = code.replaceAll("_", "/");
            String mSectorCode = "B2C";
            String mLocale = "en_US";
            String mCatalogCode = "CONSUMER";
            String mRequestTag = null;

            PrxLogger.enablePrxLogger(true);
            ProductSummaryBuilder mProductAssetBuilder = new ProductSummaryBuilder(mCtn, mRequestTag);
            mProductAssetBuilder.setmSectorCode(mSectorCode);
            mProductAssetBuilder.setmLocale(mLocale);
            mProductAssetBuilder.setmCatalogCode(mCatalogCode);

            RequestManager mRequestManager = new RequestManager();
            mRequestManager.init(mContext);
            mRequestManager.executeRequest(mProductAssetBuilder, new ResponseListener() {
                @Override
                public void onResponseSuccess(ResponseData responseData) {

                    SummaryModel mAssetModel = (SummaryModel) responseData;

                    Data data = mAssetModel.getData();

                    summary.setImageUrl(data.getImageURL());
                    summary.setProductTitle(data.getProductTitle());
                    summary.setCtnNumber(code);
                    summary.setEntryNumber(entry.getEntryNumber());

                    addItem(summary);
                }

                @Override
                public void onResponseError(String error, int code) {
                    Log.d(TAG, "Negative Response Data : " + error + " with error code : " + code);
                    Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
                    Utility.dismissProgressDialog();
                }
            });

        } else {
            Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
        }
    }

    boolean checkDuplicateValues(ShoppingCartData item){
        String ctn = item.getCtnNumber();
        for(int i=0;i<mProductData.size();i++) {
            if (mProductData.get(i).getCtnNumber().equalsIgnoreCase(ctn)){
                return true;
            }
        }
        return false;
    }

    void addItem(ShoppingCartData summary){
        if(!checkDuplicateValues(summary)) {
            mProductData.add(summary);
            addShippingCostRowToTheList();
            refreshList(mProductData);
        }else{
            Utility.dismissProgressDialog();
        }
    }

    public void refreshList(ArrayList<ShoppingCartData> data){
        if(mLoadListener != null) {
            mLoadListener.onLoadFinished(data);
        }
    }


    public void deleteProduct(final ShoppingCartData summary) {
        Utility.showProgressDialog(mContext, "Getting Cart Details");
        Map<String,String> query = new HashMap<>();
        query.put(mResources.getString(R.string.iap_code), summary.getCartNumber());
        query.put(mResources.getString(R.string.iap_entry_number), String.valueOf(summary.getEntryNumber()));

            HybrisDelegate.getInstance(mContext).sendRequest(RequestCode.DELETE_ENTRY,
                    new RequestListener() {
                        @Override
                        public void onSuccess(Message msg) {
                            removeItemFromList(summary);
                            Utility.dismissProgressDialog();
                            refreshList(mProductData);
                            checkIfCartIsEmpty();
                        }

                        @Override
                        public void onError(Message msg) {
                            Toast.makeText(mContext, "Delete Request Error" + msg.getData().toString(), Toast.LENGTH_SHORT).show();
                            refreshList(mProductData);
                            Utility.dismissProgressDialog();
                        }
                    }, query);
    }

    private void checkIfCartIsEmpty() {
        if(mProductData.size()<=3){
            Intent intent = new Intent(mContext, EmptyCartActivity.class);
            mContext.startActivity(intent);
        }
    }

    private void removeItemFromList(ShoppingCartData pProductdata) {
        if(mProductData.size()<=4){
            mProductData.removeAll(mProductData);
        }else {
            mProductData.remove(pProductdata);
        }
    }

    public void updateProductQuantity(ShoppingCartData product, final int count) {
        HashMap<String,String> params = new HashMap<String, String>();
        params.put(CartModel.PRODUCT_CODE, product.getCtnNumber());
        params.put(CartModel.PRODUCT_QUANTITY, String.valueOf(count));
        params.put(CartModel.PRODUCT_ENTRYCODE, String.valueOf(product.getEntryNumber()));
        HybrisDelegate.getInstance(mContext)
                .sendRequest(RequestCode.UPDATE_PRODUCT_COUNT, new RequestListener() {
                    @Override
                    public void onSuccess(Message msg) {
                        UpdateCartData data = (UpdateCartData) msg.obj;

                       /* if (data.getEntries() == null) {
                            Intent intent = new Intent(mContext, EmptyCartActivity.class);
                            mContext.startActivity(intent);
                            Utility.dismissProgressDialog();
                            return;
                        }

                        ShoppingCartData item = new ShoppingCartData();
                        item.setQuantity(data.getEntries().get(0).getQuantity());
                        item.setTotalPrice(data.getTotalPrice().getValue());
                        item.setCurrency(data.getTotalPrice().getCurrencyIso());
                        item.setTotalItems(data.getTotalItems());
                        item.setCartNumber(data.getCode());

                        List<Entries> list = data.getEntries();
                        for (int i = 0; i < list.size(); i++) {
                            getProductDetails(item, list.get(i));
                            item.setStockLevel(data.getEntries().get(i).getProduct().getStock()
                                    .getStockLevel());

                        }*/

                        if(!(data.getStatusCode().equalsIgnoreCase("success"))){
                            Toast.makeText(mContext, "No Stock", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Message msg) {
                        Toast.makeText(mContext, "Something went wrong!" + msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        Utility.dismissProgressDialog();
                    }
                }, params);
    }
}
