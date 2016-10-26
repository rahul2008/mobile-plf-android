/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.products;

import android.content.Context;
import android.os.Message;

import com.google.gson.Gson;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.integration.IAPListener;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.response.products.Products;
import com.philips.cdp.di.iap.response.products.ProductsEntity;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocalProductCatalog implements ProductCatalogAPI, AbstractModel.DataLoadListener {
    private Context mContext;
    private ProductCatalogHelper mProductCatalogHelper;
    private ProductCatalogPresenter.ProductCatalogListener mProductCatalogListener;
    private Products mProductCatalog;

    public LocalProductCatalog(final Context context, final ProductCatalogPresenter.ProductCatalogListener productCatalogListener) {
        mContext = context;
        mProductCatalogListener = productCatalogListener;

        mProductCatalogHelper = new ProductCatalogHelper(context, productCatalogListener, this);
    }

    @Override
    public boolean getProductCatalog(int currentPage, int pageSize, IAPListener listener) {
        String locale = HybrisDelegate.getInstance().getStore().getLocale();
        String fileName = locale + ".json";
        mProductCatalog = loadFromAssets(mContext, fileName);

        if (mProductCatalog != null) {
            mProductCatalogHelper.sendPRXRequest(mProductCatalog);
            return true;
        }
        return false;
    }

    private Products loadFromAssets(Context mContext, String fileName) {
        InputStream fromAssets = null;
        Reader reader = null;
        Products products = null;
        try {
            fromAssets = readJsonInputStream(mContext, fileName);
            reader = new BufferedReader(new InputStreamReader(fromAssets));
            products = new Gson().fromJson(reader, Products.class);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fromAssets != null) {
                try {
                    fromAssets.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return products;
    }

    private InputStream readJsonInputStream(Context mContext, String fileName) throws IOException {
        return mContext.getResources().getAssets().open(fileName);
    }

    @Override
    public void getCategorizedProductList(final ArrayList<String> productList) {
        mProductCatalog = new Products();
        List<ProductsEntity> productsEntityList = new ArrayList<>();

        if(productList != null){
            for(String productCode : productList){
                ProductsEntity productsEntity = new ProductsEntity();
                productsEntity.setCode(productCode);
                productsEntityList.add(productsEntity);
            }
        }
        mProductCatalog.setProducts(productsEntityList);
        mProductCatalogHelper.sendPRXRequest(mProductCatalog);
    }

    @Override
    public void getCompleteProductList(IAPListener iapListener) {
    }

    @Override
    public void getCatalogCount(IAPListener listener) {
    }

    @Override
    public void onModelDataLoadFinished(final Message msg) {
        if (msg.obj instanceof HashMap) {
            mProductCatalogHelper.processPRXResponse(msg, mProductCatalog, null);
        }

        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }

    @Override
    public void onModelDataError(final Message msg) {
        if (msg.obj instanceof IAPNetworkError)
            mProductCatalogListener.onLoadError((IAPNetworkError) msg.obj);
        else {
            mProductCatalogListener.onLoadError(NetworkUtility.getInstance()
                    .createIAPErrorMessage(mContext.getString(R.string.iap_no_product_available)));
        }
    }
}
