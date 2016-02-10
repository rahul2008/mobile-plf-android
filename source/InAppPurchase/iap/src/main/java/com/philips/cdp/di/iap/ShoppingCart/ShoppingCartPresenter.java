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
import com.philips.cdp.di.iap.response.cart.Entry;
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
    private static final String TAG = ShoppingCartPresenter.class.getName();
    Context mContext;
    ArrayList<ShoppingCartData> mProductData;
    private LoadListener mLoadListener;
    private Resources mResources;
    private final String UPDATE = "update";
    private final String ADD = "add";

    public interface LoadListener {
        void onLoadFinished(ArrayList<ShoppingCartData> data);

        void updateStock(boolean isOutOfStock);
    }

    public ShoppingCartPresenter(Context context, LoadListener listener) {
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

    public void getCurrentCartDetails() {
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
                        item.setTotalPrice(data.getTotalPrice().getValue());
                        item.setCurrency(data.getTotalPrice().getCurrencyIso());
                        item.setTotalItems(data.getTotalItems());
                        item.setCartNumber(data.getCode());

                        List<Entries> list = data.getEntries();

                        for (int i = 0; i < list.size(); i++) {

                            int quantity = data.getEntries().get(i).getQuantity();
                            int stockLevel = data.getEntries().get(i).getProduct().getStock()
                                    .getStockLevel();
                            item.setQuantity(quantity);
                            getProductDetails(item, list.get(i));
                            item.setStockLevel(stockLevel);

                            if (mLoadListener != null) {
                                if (quantity > stockLevel) {
                                    mLoadListener.updateStock(true);
                                } else {
                                    mLoadListener.updateStock(false);
                                }
                            }
                        }

                        Utility.dismissProgressDialog();
                    }

                    @Override
                    public void onError(Message msg) {
                        Toast.makeText(mContext, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        Utility.dismissProgressDialog();
                    }
                }, null);
    }

    public void getProductDetails(final ShoppingCartData summary, final Entries entry) {
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

    boolean checkDuplicateValues(ShoppingCartData item) {
        String ctn = item.getCtnNumber();
        for (int i = 0; i < mProductData.size(); i++) {
            if (mProductData.get(i).getCtnNumber().equalsIgnoreCase(ctn)) {
                return true;
            }
        }
        return false;
    }

    void addItem(ShoppingCartData summary) {
        if (!checkDuplicateValues(summary)) {
            mProductData.add(summary);
            addShippingCostRowToTheList();
            refreshList(mProductData);
        }
        Utility.dismissProgressDialog();
    }

    public void refreshList(ArrayList<ShoppingCartData> data) {
        if (mLoadListener != null) {
            mLoadListener.onLoadFinished(data);
        }
    }


    public void deleteProduct(final ShoppingCartData summary) {
        Utility.showProgressDialog(mContext, "Deleting Item");
        Map<String, String> query = new HashMap<>();
        query.put(mResources.getString(R.string.iap_entry_number), String.valueOf(summary.getEntryNumber()));

        HybrisDelegate.getInstance(mContext).sendRequest(RequestCode.DELETE_PRODUCT,
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
                        Toast.makeText(mContext, "Delete Request Error", Toast.LENGTH_SHORT).show();
                        refreshList(mProductData);
                        Utility.dismissProgressDialog();
                    }
                }, query);
    }

    private void checkIfCartIsEmpty() {
        if (mProductData.size() <= 3) {
            Intent intent = new Intent(mContext, EmptyCartActivity.class);
            mContext.startActivity(intent);
        }
    }

    private void removeItemFromList(ShoppingCartData pProductdata) {
        if (mProductData.size() <= 4) {
            mProductData.removeAll(mProductData);
        } else {
            mProductData.remove(pProductdata);
        }
    }

    public void updateProductQuantity(final ArrayList<ShoppingCartData> array, final int position, final int count) {
        mProductData = array;
        ShoppingCartData product = array.get(position);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CartModel.PRODUCT_CODE, product.getCtnNumber());
        params.put(CartModel.PRODUCT_QUANTITY, String.valueOf(count));
        params.put(CartModel.PRODUCT_ENTRYCODE, String.valueOf(product.getEntryNumber()));
        HybrisDelegate.getInstance(mContext)
                .sendRequest(RequestCode.UPDATE_PRODUCT_COUNT, new RequestListener() {
                    @Override
                    public void onSuccess(Message msg) {
                        UpdateCartData data = (UpdateCartData) msg.obj;

                        Entry entry = data.getEntry();
                        ShoppingCartData item = array.get(position);

                        item.setQuantity(entry.getQuantity());
                        item.setTotalPrice(entry.getTotalPrice().getValue());
                        item.setCurrency(entry.getTotalPrice().getCurrencyIso());
                        item.setTotalItems(data.getQuantityAdded());
                        updateItem(item, getPositionOfItem(item));

                        if (mLoadListener != null) {
                            if ((data.getStatusCode().equalsIgnoreCase("success"))) {
                                mLoadListener.updateStock(false);
                            } else {
                                mLoadListener.updateStock(true);
                            }
                        }
                        Utility.dismissProgressDialog();
                    }

                    @Override
                    public void onError(Message msg) {
                        Toast.makeText(mContext, "Something went wrong!" + msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        Utility.dismissProgressDialog();
                    }
                }, params);
    }

    private void updateItem(final ShoppingCartData data, int position) {
        mProductData.set(position, data);
        refreshList(mProductData);
        Utility.dismissProgressDialog();
    }

    private int getPositionOfItem(ShoppingCartData data) {
        int position = 0;
        for (int i = 0; i < mProductData.size(); i++) {
            if (mProductData.get(i).getCtnNumber() != null && mProductData.get(i).getCtnNumber().equalsIgnoreCase(data.getCtnNumber())) {
                position = i;
                break;
            }
        }
        return position;
    }

}
