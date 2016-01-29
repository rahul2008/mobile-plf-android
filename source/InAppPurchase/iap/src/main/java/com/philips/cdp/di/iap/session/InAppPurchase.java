/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.philips.cdp.di.iap.activity.IapConstants;
import com.philips.cdp.di.iap.activity.IapSharedPreference;
import com.philips.cdp.di.iap.activity.NetworkConstants;
import com.philips.cdp.di.iap.activity.Product;
import com.philips.cdp.di.iap.activity.Utility;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.prxdatabuilder.ProductSummaryBuilder;
import com.philips.cdp.prxclient.prxdatamodels.summary.Data;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class InAppPurchase {

    private static Context mContext;
    private static TestEnvOAuthHandler authHandler = new TestEnvOAuthHandler();
    private static String TAG = InAppPurchase.class.getName();


    public static int getCartItemCount(Context context, String janRainID, String userID) {
        return HybrisDelegate.getCartItemCount(context, janRainID, userID);
    }


    /**
     * Get the current cart info for the user
     */
    public static void getCurrentCartHybrisServerRequest(final Context context) {

        mContext = context;

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utility.showProgressDialog(context, "Loading Cart");
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Utility.dismissProgressDialog();
                ((AsyncTaskCompleteListener) context).onTaskComplete();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL(NetworkConstants.getCurrentCartUrl);
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    conn.setRequestProperty("Authorization", "Bearer " + authHandler.generateToken(context, "", "")); //Header

                    conn.setSSLSocketFactory(authHandler.buildSslSocketFactory(context));
                    conn.setHostnameVerifier(authHandler.hostnameVerifier);

                    int responseCode = conn.getResponseCode();
                    System.out.println("\nSending request to URL : " + url);
                    System.out.println("Response Code : " + responseCode);

                    if (responseCode == 200) {

                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        parseGetCurrentCartResponse(response.toString());
                    } else if (responseCode == 400) {
                        createCartHybrisServerRequest(mContext);
                    }
                } catch (Exception e) {
                    System.out.println("Exception");

                }
                return null;
            }
        }.execute();
    }

    /**
     * Get Shooping Cart Info from Hybris
     *
     * @return Cart count
     */
    public static void getCartCurrentCartRequest(final Context context, final UpdateProductInfoFromHybris callback, final CartInfo cartInfo) {

        mContext = context;

        new AsyncTask<Void, Void, Void>() {

            HurlStack.UrlRewriter hurl= new HurlStack.UrlRewriter() {
                @Override
                public String rewriteUrl(final String originalUrl) {
                    return NetworkConstants.getCurrentCart;
                }
            };


            HurlStack hurlStack = new HurlStack(hurl,authHandler.buildSslSocketFactory(context)) {
                @Override
                protected HttpURLConnection createConnection(URL url) throws IOException {
                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
                    try {
                        httpsURLConnection.setHostnameVerifier(authHandler.hostnameVerifier);
                        httpsURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        httpsURLConnection.setRequestProperty("Authorization", "Bearer " + authHandler.generateToken(context, "", "")); //Header
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return httpsURLConnection;
                }
            };

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    // Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(context,hurlStack);

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, NetworkConstants.getCurrentCart,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    parseCurrentCartInfo(response,callback,cartInfo);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(TAG,"error = " + error);
                            callback.updateProductInfo(null,null);
                        }
                    });

                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                } catch (Exception e) {
                    System.out.println("Exception");
                    callback.updateProductInfo(null, null);
                }
                return null;
            }
        }.execute();
    }


    public static  void parseCurrentCartInfo(String inputString, final UpdateProductInfoFromHybris callback, CartInfo cartInfo) {
        try {
            JSONObject jsonObject = new JSONObject(inputString);

            getTotalItemAndTotalPrice(jsonObject, cartInfo);
            getEntryDetails(jsonObject,callback,cartInfo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getTotalItemAndTotalPrice(JSONObject jsonObject, CartInfo cartInfo){
        try {
            if (jsonObject.has("totalItems")) {
                cartInfo.totalItems = jsonObject.get("totalItems").toString();
            }

            if (jsonObject.has("totalPriceWithTax")) {
                JSONObject jsonPrice = new JSONObject(jsonObject.get("totalPriceWithTax").toString());
                if (jsonPrice.has("currencyIso")) {
                    cartInfo.currency = jsonPrice.get("currencyIso").toString();
                }
                if (jsonPrice.has("value")) {
                    cartInfo.totalCost = jsonPrice.get("value").toString();
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public static void getEntryDetails(JSONObject jsonObject, final UpdateProductInfoFromHybris callback, CartInfo cartInfo){

        String code = null;
        try{
        if (jsonObject.has("entries")) {
            JSONArray itemList = new JSONArray(jsonObject.get("entries").toString());
            for (int i = 0; i < itemList.length(); i++) {
                JSONObject object = itemList.getJSONObject(i);
                ProductSummary summary = new ProductSummary();
                if (object.has("quantity")) {
                    summary.quantity = object.get("quantity").toString();
                }
                if (object.has("totalPrice")) {
                    JSONObject jsonPrice = new JSONObject(jsonObject.get("totalPrice").toString());
                    if (jsonPrice.has("currencyIso")) {
                        summary.Currency = jsonPrice.get("currencyIso").toString();
                    }
                    if (jsonPrice.has("value")) {
                        summary.price = jsonPrice.get("value").toString();
                    }
                }
                if(object.has("product")) {
                    JSONObject product = new JSONObject(object.get("product").toString());
                    if(product.has("code")){
                        code = product.get("code").toString();
                        summary.productCode = code;
                        Log.i(TAG,"code = " + code);
                    }
                }
                getProductDetailsFromPRX(code,summary,callback,cartInfo);

            }
        }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }




    static void getProductDetailsFromPRX(String code,final ProductSummary productInfo,final UpdateProductInfoFromHybris callback,final CartInfo cartInfo){
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

                Log.d(TAG, "Positive Response Data : " + mAssetModel.isSuccess());

                Data data = mAssetModel.getData();
                data.getImageURL();
                Log.i(TAG, data.getProductTitle());
                Log.i(TAG, data.getProductURL());
                Log.i(TAG, data.getPrice().toString());
                Log.i(TAG, data.getPriority() + "");

                productInfo.ImageURL = data.getImageURL();
                productInfo.productTitle = data.getProductTitle();

                callback.updateProductInfo(productInfo, cartInfo);
            }

            @Override
            public void onResponseError(String error, int code) {
                Log.d(TAG, "Negative Response Data : " + error + " with error code : " + code);
                Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
                callback.updateProductInfo(null,null);
            }
        });

    }



   /*
     * Parse current cart response
     *
     * @param inputString Input string
     */
    public static void parseGetCurrentCartResponse(String inputString) {
        try {
            JSONObject jsonObject = new JSONObject(inputString);

            JSONArray entriesArray = jsonObject.getJSONArray("entries");

            JSONObject quantityJsonObject = entriesArray.getJSONObject(0);

            if (quantityJsonObject.has("quantity")) {
                String mCartCount = quantityJsonObject.getString("quantity");
                new IapSharedPreference(mContext).setString(IapConstants.key.CART_COUNT, mCartCount);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Create Cart Hybris Server Request
     */
    public static void createCartHybrisServerRequest(final Context context) {

        mContext = context;

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL(NetworkConstants.createCartUrl);
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Authorization", "Bearer " + authHandler.generateToken(context, "", "")); //Header

                    conn.setSSLSocketFactory(authHandler.buildSslSocketFactory(context));
                    conn.setHostnameVerifier(authHandler.hostnameVerifier);

                    List<Pair<String, String>> valuePair = new ArrayList<>();
                    valuePair.add(new Pair<String, String>("code", "HX8331_11"));

                    OutputStream os = conn.getOutputStream();

                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getQuery(valuePair));
                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    System.out.println("\nSending request to URL : " + url);
                    System.out.println("Response Code : " + responseCode);
                } catch (Exception e) {
                    System.out.println("Exception");

                }
                return null;
            }
        }.execute();
    }

    /**
     * Add to cart hybris server request
     */
    public static void addToCartHybrisServerRequest(final Context context) {

        mContext = context;

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utility.showProgressDialog(context, "Adding to cart");
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Utility.dismissProgressDialog();
                ((AsyncTaskCompleteListener) context).onTaskComplete();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL(NetworkConstants.addToCartUrl);
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Authorization", "Bearer " + authHandler.generateToken(mContext, "", "")); //Header

                    conn.setSSLSocketFactory(authHandler.buildSslSocketFactory(mContext));
                    conn.setHostnameVerifier(authHandler.hostnameVerifier);

                    List<Pair<String, String>> valuePair = new ArrayList<>();
                    valuePair.add(new Pair<>("code", "HX8331_11"));

                    OutputStream os = conn.getOutputStream();

                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getQuery(valuePair));
                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    System.out.println("\nSending request to URL : " + url);
                    System.out.println("Response Code : " + responseCode);

                    if (responseCode == 200) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        parseAddToCartResponse(response.toString());
                    }
                } catch (Exception e) {
                    System.out.println("Exception");

                }
                return null;
            }
        }.execute();
    }

    /**
     * Parse Add to cart response
     *
     * @param inputString response string
     */
    public static void parseAddToCartResponse(String inputString) {
        try {
            JSONObject jsonObject = new JSONObject(inputString);
            JSONObject entryJsonObject = jsonObject.getJSONObject("entry");

            if (entryJsonObject.has("quantity")) {
                String mCartCount = entryJsonObject.get("quantity").toString();
                new IapSharedPreference(mContext).setString(IapConstants.key.CART_COUNT, mCartCount);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get Query string i.e payload format
     *
     * @param valuePair payload value pair
     * @return query string
     * @throws UnsupportedEncodingException
     */
    private static String getQuery(List<Pair<String, String>> valuePair) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Pair<String, String> pair : valuePair) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.first, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.second, "UTF-8"));
        }

        return result.toString();
    }
}