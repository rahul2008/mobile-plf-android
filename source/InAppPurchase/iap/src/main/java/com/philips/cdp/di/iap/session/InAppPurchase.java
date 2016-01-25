/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;

import com.philips.cdp.di.iap.activity.IapConstants;
import com.philips.cdp.di.iap.activity.IapSharedPreference;
import com.philips.cdp.di.iap.activity.NetworkConstants;
import com.philips.cdp.di.iap.activity.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class InAppPurchase {

    private static Context mContext;
    private static TestEnvOAuthHandler authHandler = new TestEnvOAuthHandler();

    public static int getCartItemCount(Context context, String janRainID, String userID) {
        return HybrisDelegate.getCartItemCount(context, janRainID, userID);
    }

    /**
     * Get the cart info for the
     *
     * @return Cart count
     */
    public static void getCartHybrisServerRequest(final String mCartNumber, final Context context, final AsyncTaskCompleteListener callback) {

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
                callback.onTaskComplete();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL(NetworkConstants.getCartUrl + "/" + mCartNumber);
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
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        parseGetCartResponse(response.toString());
                    }
                } catch (Exception e) {
                    System.out.println("Exception");

                }
                return null;
            }
        }.execute();
    }

    public static void parseGetCartResponse(String inputString) {
        try {
            JSONObject jsonObject = new JSONObject(inputString);

            if (jsonObject.has("totalItems")) {
                String mCartCount = jsonObject.get("totalItems").toString();
                new IapSharedPreference(mContext).setString(IapConstants.key.CART_COUNT, mCartCount);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Create Cart Hybris Server Request
     */
    public static void createCartHybrisServerRequest(final Context context, final AsyncTaskCompleteListener callback) {

        mContext = context;

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Utility.dismissProgressDialog();
                callback.onTaskComplete();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL(NetworkConstants.getCartUrl);
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

                    if (responseCode == 201) {

                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        parseCreateCartResponse(response.toString());
                        addToCartHybrisServerRequest(new IapSharedPreference(context).getString(IapConstants.key.CART_NUMBER), context, ((AsyncTaskCompleteListener)context));
                    }

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    System.out.println(response.toString());
                } catch (Exception e) {
                    System.out.println("Exception");

                }
                return null;
            }
        }.execute();
    }


    /**
     * Parse the get cart response from hybris server
     *
     * @param inputString Input string to parse
     * @return cart number
     */
    public static void parseCreateCartResponse(String inputString) {
        try {
            JSONObject jsonObject = new JSONObject(inputString);

            if (jsonObject.has("code")) {
                String mCartNumber = jsonObject.get("code").toString();
                new IapSharedPreference(mContext).setString(IapConstants.key.CART_NUMBER, mCartNumber);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add to cart hybris server request
     *
     * @param mCartNumber
     */
    public static void addToCartHybrisServerRequest(final String mCartNumber, Context context, final AsyncTaskCompleteListener callback) {

        mContext = context;

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Utility.dismissProgressDialog();
                callback.onTaskComplete();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL(NetworkConstants.addToCartUrl + "/" + mCartNumber + "/entries");
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
                        StringBuffer response = new StringBuffer();

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
     * @return cart count
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