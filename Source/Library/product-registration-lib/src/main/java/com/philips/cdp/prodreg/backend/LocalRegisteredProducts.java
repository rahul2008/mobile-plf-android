package com.philips.cdp.prodreg.backend;

import android.content.Context;

import com.google.gson.Gson;
import com.philips.cdp.prodreg.localcache.LocalSharedPreference;
import com.philips.cdp.prodreg.model.RegisteredProduct;
import com.philips.cdp.prodreg.model.TestList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class LocalRegisteredProducts {

    private final Context context;
    private String jsonArray;
    private LocalSharedPreference localSharedPreference;

    public LocalRegisteredProducts(Context context) {
        this.context = context;
        localSharedPreference = new LocalSharedPreference(context);
    }

    public void storeProductLocally(String json) {
        try {
            JSONObject jsonObject = new JSONObject();
            Gson gson = new Gson();
            JSONArray jsonArray = null;
            jsonArray = new JSONArray();
            jsonArray.put(getJsonArray());
            jsonArray.put(json);
            jsonObject.put("registeredProducts", gson.toJson(jsonArray.toString()));
            localSharedPreference.storeData(UserProduct.PRODUCT_REGISTRATION_KEY, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getJsonArray() {
        return localSharedPreference.getData(UserProduct.PRODUCT_REGISTRATION_KEY);
    }

    public ArrayList<RegisteredProduct> getRegisteredProducts() {
        ArrayList<RegisteredProduct> registeredProducts = new ArrayList<>();
        Gson gson = new Gson();
        String s = localSharedPreference.getData(UserProduct.PRODUCT_REGISTRATION_KEY);
        TestList testList = gson.fromJson(s, TestList.class);
        return registeredProducts;
    }
}
