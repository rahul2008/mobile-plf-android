/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.applocal;

import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentManager;

import com.google.gson.Gson;
import com.philips.cdp.di.iap.core.ProductCatalogAPI;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.productCatalog.PRXBuilderForProductCatalog;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogData;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogPresenter;
import com.philips.cdp.di.iap.response.products.Products;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

public class LocalProductCatalog implements ProductCatalogAPI {
    private Context mContext;
    private ProductCatalogPresenter.LoadListener mLoadListener;
    private FragmentManager mFragmentManager;

    private ArrayList<ProductCatalogData> mProductData;

    public LocalProductCatalog(final Context context, final ProductCatalogPresenter.LoadListener listener, final FragmentManager fragmentManager) {
        mContext = context;
        mLoadListener = listener;
        mFragmentManager = fragmentManager;
    }

    @Override
    public void getProductCatalog() {
        loadFromLocal();
    }

    private void loadFromLocal() {
        String locale = HybrisDelegate.getInstance().getStore().getLocale();
        String fileName = locale+ ".json";
        Products productCatalog = loadFromAsset(mContext, fileName);
        getPRXData(productCatalog);
    }

    public Products loadFromAsset(Context context, String fileName) {
        InputStream fromAsset = null;
        Reader reader = null;
        Products products = null;
        try {
            fromAsset = readJSONInputStream(context, fileName);
            reader = new BufferedReader(new InputStreamReader(fromAsset));
            products = new Gson().fromJson(reader, Products.class);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fromAsset != null) {
                try {
                    fromAsset.close();
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

    public InputStream readJSONInputStream(final Context context, String fileName) throws IOException {
        return context.getResources().getAssets().open(fileName);
    }

    public void getPRXData(Products productCatalog){
        PRXBuilderForProductCatalog builder = new PRXBuilderForProductCatalog(mContext, productCatalog,
                new AbstractModel.DataLoadListener(){

                    @Override
                    public void onModelDataLoadFinished(final Message msg) {
                        if (msg.obj instanceof ArrayList) {
                            mProductData = (ArrayList<ProductCatalogData>) msg.obj;
                            if (mProductData == null || mProductData.size() == 0) {
                                EventHelper.getInstance().notifyEventOccurred(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED);
                                if (Utility.isProgressDialogShowing()) {
                                    Utility.dismissProgressDialog();
                                }
                                return;
                            }
                            //addShippingCostRowToTheList();
                            if (mLoadListener != null) {
                                mLoadListener.onLoadFinished(mProductData);
                            }
                        } else{
                            EventHelper.getInstance().notifyEventOccurred(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED);
                            if(Utility.isProgressDialogShowing())
                                Utility.dismissProgressDialog();
                        }
                        if(Utility.isProgressDialogShowing()) {
                            Utility.dismissProgressDialog();
                        }
                    }

                    @Override
                    public void onModelDataError(final Message msg) {
                        if(Utility.isProgressDialogShowing()) {
                            Utility.dismissProgressDialog();
                        }
                        NetworkUtility.getInstance().showErrorMessage(msg,mFragmentManager,mContext);
                    }
                });
        builder.build();
    }
}
