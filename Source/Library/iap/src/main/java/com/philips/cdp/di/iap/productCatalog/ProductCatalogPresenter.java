/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.productCatalog;

import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentManager;

import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.core.ProductCatalogAPI;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.GetProductCatalogRequest;
import com.philips.cdp.di.iap.prx.PRXDataBuilder;
import com.philips.cdp.di.iap.response.carts.Carts;
import com.philips.cdp.di.iap.response.carts.EntriesEntity;
import com.philips.cdp.di.iap.response.products.Products;
import com.philips.cdp.di.iap.response.products.ProductsEntity;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.prxclient.datamodels.summary.Data;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductCatalogPresenter implements ProductCatalogAPI {

    private Context mContext;
    private LoadListener mLoadListener;
    private HybrisDelegate mHybrisDelegate;
    private StoreSpec mStore;
    private FragmentManager mFragmentManager;
    Products mProductData = null;

    public interface LoadListener {
        void onLoadFinished(ArrayList<ProductCatalogData> data);
    }

    public ProductCatalogPresenter(Context context, LoadListener listener, FragmentManager fragmentManager){
        mContext = context;
        mLoadListener = listener;
        mFragmentManager = fragmentManager;
    }

    public void setHybrisDelegate(HybrisDelegate delegate) {
        mHybrisDelegate = delegate;
    }

    public HybrisDelegate getHybrisDelegate() {
        if (mHybrisDelegate == null) {
            mHybrisDelegate = HybrisDelegate.getInstance(mContext);
        }
        return mHybrisDelegate;
    }

    private StoreSpec getStore() {
        if (mStore == null) {
            mStore = getHybrisDelegate().getStore();
        }
        return mStore;
    }

    public void refreshList(ArrayList<ProductCatalogData> data) {
        if (mLoadListener != null) {
            mLoadListener.onLoadFinished(data);
        }
        if(Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }

    private void sendHybrisRequest(int code, AbstractModel model, RequestListener listener) {
        getHybrisDelegate().sendRequest(code, model, model);
    }


    @Override
    public void getProductCatalog() {
        GetProductCatalogRequest model = new GetProductCatalogRequest(getStore(), null,
                new AbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(Message msg) {

                        if (processHybrisRequestForGetCurrentCartData(msg)) return;

                        if (processPRXResponse(msg)) return;

                        if (Utility.isProgressDialogShowing())
                            Utility.dismissProgressDialog();
                    }

                    private boolean processHybrisRequestForGetCurrentCartData(final Message msg) {
                        if (msg.obj instanceof Products) {
                            mProductData = (Products) msg.obj;
                            if (mProductData != null) {
                                makePrxCall();
                                return true;
                            }
                        }
                        return false;
                    }

                    private boolean processPRXResponse(final Message msg) {
                        if (msg.obj instanceof HashMap) {
                            HashMap<String,SummaryModel> prxModel = (HashMap<String,SummaryModel>)msg.obj;

                            if (checkForEmptyCart(prxModel))
                                return true;

                            ArrayList<ProductCatalogData> products = mergeResponsesFromHybrisAndPRX(mProductData, prxModel);
                            refreshList(products);

                        }else {
                            notifyEmptyCartFragment();
                        }
                        return false;
                    }

                    private void notifyEmptyCartFragment() {
                        EventHelper.getInstance().notifyEventOccurred(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED);
                        if (Utility.isProgressDialogShowing())
                            Utility.dismissProgressDialog();
                    }

                    private boolean checkForEmptyCart(final HashMap<String, SummaryModel> prxModel) {
                        if (prxModel == null || prxModel.size() == 0) {
                            notifyEmptyCartFragment();
                            return true;
                        }
                        return false;
                    }

                    private void makePrxCall() {
                        ArrayList<String> ctnsToBeRequestedForPRX = new ArrayList<>();
                        final List<ProductsEntity> products = mProductData.getProducts();
                        ArrayList<String> productsToBeShown = new ArrayList<>();
                        String ctn;

                        for(ProductsEntity entry:products){
                            ctn = entry.getCode();
                            productsToBeShown.add(ctn);
                            if (!CartModelContainer.getInstance().isPRXDataPresent(ctn)) {
                                ctnsToBeRequestedForPRX.add(entry.getCode());
                            }
                        }
                        if(ctnsToBeRequestedForPRX.size()>0) {
                            PRXDataBuilder builder = new PRXDataBuilder(mContext, ctnsToBeRequestedForPRX,
                                    this);
                            builder.preparePRXDataRequest();
                        }else {
                            HashMap<String, SummaryModel> prxModel = new HashMap<>();
                            for(String ctnPresent: productsToBeShown){
                                prxModel.put(ctnPresent,CartModelContainer.getInstance().getProductDataFromListIfPresent(ctnPresent));
                            }
                            ArrayList<ProductCatalogData> productCatalogDatas = mergeResponsesFromHybrisAndPRX(mProductData,prxModel);
                            refreshList(productCatalogDatas);
                        }
                    }

                    @Override
                    public void onModelDataError(final Message msg) {
                        IAPLog.e(IAPConstant.SHOPPING_CART_PRESENTER, "Error:" + msg.obj);
                        IAPLog.d(IAPConstant.SHOPPING_CART_PRESENTER, msg.obj.toString());
                        NetworkUtility.getInstance().showErrorMessage(msg,mFragmentManager,mContext);
                        if(Utility.isProgressDialogShowing()) {
                            Utility.dismissProgressDialog();
                        }
                    }
                });
        model.setContext(mContext);
        sendHybrisRequest(0, model, model);
    }

    private ArrayList<ProductCatalogData> mergeResponsesFromHybrisAndPRX(final Products productData, final HashMap<String, SummaryModel> prxModel) {
        List<ProductsEntity> entries = productData.getProducts();
        HashMap<String, SummaryModel> list = CartModelContainer.getInstance().getPRXDataObjects();
        ArrayList<ProductCatalogData> products = new ArrayList<>();
        String ctn;
        for(ProductsEntity entry: entries){
            ctn = entry.getCode();
            ProductCatalogData productItem = new ProductCatalogData();
            Data data = null;
            if(prxModel.containsKey(ctn)) {
                data = prxModel.get(ctn).getData();
            }else if(list.containsKey(ctn)){
                data = list.get(ctn).getData();
            }else {
                return products;
            }
            productItem.setImageUrl(data.getImageURL());
            productItem.setProductTitle(data.getProductTitle());
            productItem.setCtnNumber(ctn);
            productItem.setMarketingTextHeader(data.getMarketingTextHeader());
            fillEntryBaseData(entry, productItem, data);
            products.add(productItem);
        }
        return products;
    }

    private void fillEntryBaseData(final ProductsEntity entry, final ProductCatalogData productItem, final Data data) {
        if(entry.getPrice() == null || entry.getDiscountPrice() == null)
            return;
        productItem.setFormatedPrice(entry.getPrice().getFormattedValue());
        productItem.setPriceValue(String.valueOf(entry.getPrice().getValue()));
        if (entry.getDiscountPrice() != null && entry.getDiscountPrice().getFormattedValue() != null
                && !entry.getDiscountPrice().getFormattedValue().isEmpty()) {
            productItem.setDiscountedPrice(entry.getDiscountPrice().getFormattedValue());
        }
    }
}
