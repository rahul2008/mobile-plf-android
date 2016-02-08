package com.philips.cdp.di.iap.model;

import android.os.Bundle;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.response.cart.GetCartData;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.store.Store;

import java.util.HashMap;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class EntryModel extends AbstractModel{

    //TODO: Merge with CartModel
    public final static String PRODUCT_CODE = "code";
    public final static String ENTRY_CODE = "entrynumber";

    public EntryModel(final Store store, Map<String,String> query) {
        super(store,query);
    }

    @Override
    public String getUrl(final int requestCode) {
        switch (requestCode) {
            case RequestCode.DELETE_ENTRY:
                if (params == null) {
                    throw new RuntimeException("Cart ID and Entry Number has to be supplied");
                }
                String productCode = params.get(PRODUCT_CODE);
                int entryNumber = Integer.parseInt(params.get(ENTRY_CODE));

                return String.format(NetworkConstants.deleteProductEntry, productCode,entryNumber);
        }
        return null;
    }

    @Override
    public Object parseResponse(final int requestCode, final Object response) {
        return null;
    }

    @Override
    public int getMethod(final int requestCode) {
        switch (requestCode) {
            case RequestCode.DELETE_ENTRY:
                return Request.Method.DELETE;
        }
        return 0;
    }

    @Override
    public Map<String, String> requestBody(final int requestCode) {
        if (params != null) {
            if (requestCode == RequestCode.DELETE_ENTRY) {
                return getEntryCartDetails();
            }
        }
        return null;
    }

        @Override
        public String getTestUrl ( final int requestCode){
            switch (requestCode) {
                case RequestCode.DELETE_ENTRY:
                    if (params == null) {
                        throw new RuntimeException("Cart ID and Entry Number has to be supplied");
                    }
                    String productCode = params.get(PRODUCT_CODE);
                    int entryNumber = Integer.parseInt(params.get(ENTRY_CODE));
                    return String.format(NetworkConstants.deleteProductEntry, productCode,String.valueOf(entryNumber));
            }
            return null;
        }


    private Map<String, String> getEntryCartDetails() {
        Map<String, String> params = new HashMap<>();
        params.put(PRODUCT_CODE,params.get(PRODUCT_CODE));
        params.put(ENTRY_CODE,params.get(ENTRY_CODE));
        return params;
    }
}
