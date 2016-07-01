/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.applocal;

import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentManager;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.core.ProductCatalogAPI;
import com.philips.cdp.di.iap.core.ProductCatalogHelper;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogPresenter;
import com.philips.cdp.di.iap.response.products.Products;
import com.philips.cdp.di.iap.session.IAPHandler;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;

public class LocalProductCatalog implements ProductCatalogAPI, AbstractModel.DataLoadListener {
    private Context mContext;
    private FragmentManager mFragmentManager;
    private ProductCatalogHelper mProductCatalogHelper;
    Products mProductCatalog;
    ProductCatalogPresenter.LoadListener mListener;
    ArrayList<String> mProductList;
    boolean isLocalData;

    public LocalProductCatalog(final Context context, final ProductCatalogPresenter.LoadListener listener, final FragmentManager fragmentManager, final boolean isPlanB) {
        mContext = context;
        mListener = listener;
        mFragmentManager = fragmentManager;
        mProductCatalogHelper = new ProductCatalogHelper(context, listener, this);
        isLocalData = isPlanB;
    }

    @Override
    public boolean getProductCatalog(int currentPage,int pageSize) {
        //return loadFromLocal();
        if (IAPHandler.mProductCTNs != null) {
            mProductCatalogHelper.makePrxCall(IAPHandler.mProductCTNs, null, isLocalData);
            return true;
        } else {
            return false;
        }
    }

//    private boolean loadFromLocal() {
//        String locale = HybrisDelegate.getInstance().getStore().getLocale();
//        String fileName = locale + ".json";
//        mProductCatalog = loadFromAsset(mContext, fileName);
//
//        if (mProductCatalog != null) {
//            mProductCatalogHelper.makePrxCall(mProductCatalog);
//            return true;
//        }
//        return false;
//    }

//    private ArrayList<String> extractProductList(Products products) {
//        ArrayList<String> productsToBeShown = new ArrayList();
//        for (ProductsEntity entry : products.getProducts()) {
//            String ctn = entry.getCode();
//            productsToBeShown.add(ctn);
//
//        }
//        return productsToBeShown;
//    }

//    public Products loadFromAsset(Context context, String fileName) {
//        InputStream fromAsset = null;
//        Reader reader = null;
//        Products products = null;
//        try {
//            fromAsset = readJSONInputStream(context, fileName);
//            reader = new BufferedReader(new InputStreamReader(fromAsset));
//            products = new Gson().fromJson(reader, Products.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (fromAsset != null) {
//                try {
//                    fromAsset.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return products;
//    }

//    public InputStream readJSONInputStream(final Context context, String fileName) throws IOException {
//        return context.getResources().getAssets().open(fileName);
//    }

    @Override
    public void onModelDataLoadFinished(final Message msg) {
        mProductCatalogHelper.processPRXResponse(msg, IAPHandler.mProductCTNs, mProductCatalog);
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }

    @Override
    public void onModelDataError(final Message msg) {
        IAPLog.e(IAPConstant.SHOPPING_CART_PRESENTER, "Error:" + msg.obj);
        IAPLog.d(IAPConstant.SHOPPING_CART_PRESENTER, msg.obj.toString());

        if (msg.obj instanceof IAPNetworkError)
            mListener.onLoadError((IAPNetworkError) msg.obj);
        else {
            mListener.onLoadError(createIAPErrorMessage(mContext.getString(R.string.iap_no_product_available)));
        }
    }

    public IAPNetworkError createIAPErrorMessage(String errorMessage) {
        VolleyError volleyError = new ServerError();
        IAPNetworkError error = new IAPNetworkError(volleyError, -1, null);
        error.setCustomErrorMessage(errorMessage);
        return error;
    }
}
