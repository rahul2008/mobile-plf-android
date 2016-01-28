/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

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
import com.philips.cdp.di.iap.activity.ProductSummary;
import com.philips.cdp.di.iap.activity.Utility;

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
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

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
     * Get the cart info for the
     *
     * @return Cart count
     */
    public static void getCartCurrentCartRequest(final Context context, final UpdateProductInfoFromHybris callback, final ProductSummary summary, final CartInfo cartInfo) {

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
                        //     httpsURLConnection.setSSLSocketFactory(InAppPurchase.getSSLSoketFactory(context));
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
               // Utility.showProgressDialog(context, "getting Cart Info");
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
               // Utility.dismissProgressDialog();

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
                                    parseCurrentCartInfo(response,callback,summary,cartInfo);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(TAG,"error = " + error);
                            callback.updateProductInfo(null,null);
                        }

                    }
                    ){
                        @Override
                        public HashMap<String, String> getParams() {
                            HashMap<String, String> params = new HashMap<String, String>();
                            params.put("Authorization", "Bearer 0b732480-9625-4c18-b93b-0e1d7bc66640");
                            return params;
                        }
                    };

// Add the request to the RequestQueue.
                    queue.add(stringRequest);
                } catch (Exception e) {
                    System.out.println("Exception");
                    callback.updateProductInfo(null,null);
                }
                return null;
            }
        }.execute();
    }


    public static  void parseCurrentCartInfo(String inputString, final UpdateProductInfoFromHybris callback, ProductSummary summary, CartInfo cartInfo) {
        try {
            JSONObject jsonObject = new JSONObject(inputString);

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

            if (jsonObject.has("entries")) {
                JSONArray itemList = new JSONArray(jsonObject.get("entries").toString());
                for(int i=0;i<itemList.length();i++){
                    JSONObject object = itemList.getJSONObject(i);
                    if(object.has("quantity")){
                        summary.quantity = object.get("quantity").toString();
                    }
                    if(object.has("totalPrice")){
                        JSONObject jsonPrice = new JSONObject(jsonObject.get("totalPrice").toString());
                        if (jsonPrice.has("currencyIso")) {
                            summary.Currency = jsonPrice.get("currencyIso").toString();
                        }
                        if (jsonPrice.has("value")) {
                            summary.price = jsonPrice.get("value").toString();
                        }
                       // summary.price = object.get("totalPrice").toString();
                    }
                }
                callback.updateProductInfo(summary,cartInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

   /* public static  void parseCurrentCartInfo(String inputString, final UpdateProductInfoFromHybris callback, ProductSummary summary){
        Log.i(TAG,inputString);
        try{
            JSONObject jsonObject = new JSONObject(inputString);
            if (jsonObject.has("totalItems")) {
                summary.quantity = jsonObject.get("totalItems").toString();
            }

            if (jsonObject.has("totalPriceWithTax")) {
                JSONObject jsonPrice = new JSONObject(jsonObject.get("totalPriceWithTax").toString());
                if (jsonPrice.has("currencyIso")) {
                    summary.Currency = jsonPrice.get("currencyIso").toString();
                }
                if (jsonPrice.has("value")) {
                    summary.price = jsonPrice.get("value").toString();
                }
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
      *//*  summary.quantity = 2;
        summary.price = "188";*//*
        callback.updateProductInfo(summary);
    }*/



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