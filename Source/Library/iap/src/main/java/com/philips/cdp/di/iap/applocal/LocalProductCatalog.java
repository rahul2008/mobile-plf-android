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
import com.philips.cdp.di.iap.core.ProductCatalogHelper;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogData;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogPresenter;
import com.philips.cdp.di.iap.response.products.Products;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

public class LocalProductCatalog implements ProductCatalogAPI , AbstractModel.DataLoadListener{
    private Context mContext;
    private FragmentManager mFragmentManager;
//    protected ErrorDialogFragment.ErrorDialogListener mErrorDialogListener = new ErrorDialogFragment.ErrorDialogListener() {
//        @Override
//        public void onTryAgainClick() {
//            IAPLog.i(IAPLog.LOG, "onTryAgainClick = " + this.getClass().getSimpleName());
//            // sendShippingAddressesRequest();
//            //  moveToPreviousFragment();
//        }
//    };
    private ProductCatalogHelper mProductCatalogHelper;
    Products mProductCatalog;

    private ArrayList<ProductCatalogData> mProductData;

    public LocalProductCatalog(final Context context, final ProductCatalogPresenter.LoadListener listener, final FragmentManager fragmentManager) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mProductCatalogHelper = new ProductCatalogHelper(context,listener,this);
    }

    @Override
    public void getProductCatalog() {
        loadFromLocal();
    }

    private void loadFromLocal() {
        String locale = HybrisDelegate.getInstance().getStore().getLocale();
        String fileName = locale + ".json";
        mProductCatalog = loadFromAsset(mContext, fileName);
        mProductCatalogHelper.makePrxCall(mProductCatalog);
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

    @Override
    public void onModelDataLoadFinished(final Message msg) {
        if (mProductCatalogHelper.processPRXResponse(msg,mProductCatalog))
            return;

        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }

    @Override
    public void onModelDataError(final Message msg) {
        IAPLog.e(IAPConstant.SHOPPING_CART_PRESENTER, "Error:" + msg.obj);
        IAPLog.d(IAPConstant.SHOPPING_CART_PRESENTER, msg.obj.toString());
        NetworkUtility.getInstance().showErrorMessage(null, msg, mFragmentManager, mContext);
        if(Utility.isProgressDialogShowing()) {
            Utility.dismissProgressDialog();
        }
    }
}
