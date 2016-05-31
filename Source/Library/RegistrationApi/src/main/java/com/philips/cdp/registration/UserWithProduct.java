
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.util.Log;

import com.janrain.android.Jump;
import com.janrain.android.capture.Capture;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.localematch.LocaleMatchListener;
import com.philips.cdp.localematch.PILLocale;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.localematch.enums.LocaleMatchError;
import com.philips.cdp.localematch.enums.Platform;
import com.philips.cdp.registration.dao.ProductRegistrationInfo;
import com.philips.cdp.registration.handlers.ProductRegistrationHandler;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.settings.RegistrationSettings;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserWithProduct extends User implements LocaleMatchListener {

    public UserWithProduct(Context context) {
        super( context);
    }

    private String LOG_TAG = "UserWithProduct";

    private ProductRegistrationInfo mProdInfo = null;

    private ProductRegistrationHandler mProdRegHandler = null;

    private String mProdRegBaseUrl = null;

    private String mInputLocale = null;

    private String PRODUCT_SERIAL_NO = "productSerialNumber";

    private String PRODUCT_PURCHASE_DATE = "purchaseDate";

    private String PRODUCT_REGISTRATION_CHANNEL = "registrationChannel";

    private String MICROSITE_ID;

    private Context mContext;

    public void getRefreshedAccessToken(final ProductRegistrationHandler productRegister) {
        if (Jump.getSignedInUser() != null)
            Jump.getSignedInUser().refreshAccessToken(new Capture.CaptureApiRequestCallback() {

                @Override
                public void onSuccess() {
                    String accessToken = Jump.getSignedInUser().getAccessToken();
                    productRegister.onRegisterSuccess(accessToken);
                }

                @Override
                public void onFailure(CaptureApiError e) {
                    productRegister.onRegisterFailedWithFailure(0);
                }
            });
    }

    public void register(ProductRegistrationInfo prodRegInfo,
                         ProductRegistrationHandler productRegisterHandler, String locale, Context context) {
        mContext = context;
        /** Get microsite id from preference */
        SharedPreferences pref = context.getSharedPreferences(
                RegistrationSettings.REGISTRATION_API_PREFERENCE, 0);
        MICROSITE_ID = pref.getString(RegistrationSettings.MICROSITE_ID, "");
        registerProduct(prodRegInfo, productRegisterHandler, locale, mContext);
    }

    private void registerProduct(final ProductRegistrationInfo prodRegInfo,
                                 final ProductRegistrationHandler productRegisterHandler, final String locale,
                                 Context context) {
        String localeArr[] = locale.split("_");
        String langCode = null;
        String countryCode = null;

        if (localeArr != null && localeArr.length > 1) {
            langCode = localeArr[0].toLowerCase();
            countryCode = localeArr[1].toUpperCase();
        } else {
            langCode = "en";
            countryCode = "US";
        }

       // RegistrationHelper userSettings = RegistrationHelper.getInstance();
        UserRegistrationInitializer userSettings = UserRegistrationInitializer.getInstance();
        mProdInfo = prodRegInfo;
        mProdRegHandler = productRegisterHandler;
        mProdRegBaseUrl = userSettings.getRegistrationSettings().getProductRegisterUrl();
        mInputLocale = locale;

        PILLocaleManager PILLocaleMngr = new PILLocaleManager(context);
        PILLocaleMngr.refresh(this);
    }

    private void startProdRegAsyncTask(String locale) {
        ProdRegAsyncTask prodRegTask = new ProdRegAsyncTask();
        String prodRegUrl = mProdRegBaseUrl + mProdInfo.getSector() + "/" + locale + "/"
                + mProdInfo.getCatalog() + "/products/" + mProdInfo.getProductModelNumber()
                + ".register.type.product?";
        List<Pair<String,String>> params = new ArrayList<>();
        params.add(new Pair(PRODUCT_SERIAL_NO, mProdInfo
                .getProductSerialNumber()));
        params.add(new Pair<String, String>(PRODUCT_PURCHASE_DATE, mProdInfo.getPurchaseDate()));
        params.add(new Pair<String, String>(PRODUCT_REGISTRATION_CHANNEL, "MS" + MICROSITE_ID));


        prodRegTask.url = prodRegUrl;
        prodRegTask.productRegister = mProdRegHandler;
        prodRegTask.locale = locale;
        prodRegTask.prodRegInfo = mProdInfo;
        prodRegTask.accessToken = Jump.getSignedInUser() != null ? Jump.getSignedInUser()
                .getAccessToken() : null;
        prodRegTask.nameValuePairs = params;
        prodRegTask.execute();
    }

	/*
     * Async Task to register a product
	 */

    private class ProdRegAsyncTask extends AsyncTask<Void, Void, String> {

        String url;


        List<Pair<String,String>> nameValuePairs;
        ProductRegistrationHandler productRegister;

        String locale;

        ProductRegistrationInfo prodRegInfo;

        String accessToken;

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new HttpClient();
            Log.i(LOG_TAG, "URL = " + url);
            Log.i(LOG_TAG, "Param = " + nameValuePairs);
            Log.i(LOG_TAG, "AccessToken = " + accessToken);
            //String resultString = httpClient.postData(url, nameValuePairs, accessToken);
            String resultString = httpClient.callPost(url, nameValuePairs, accessToken);
            Log.i(LOG_TAG, "Response = " + resultString);
            return resultString;
        }

        @Override
        protected void onPostExecute(String resultString) {
            super.onPostExecute(resultString);
            processProductRegistrationResponse(resultString);
        }

        private void processProductRegistrationResponse(String resultString) {
            /** Generic error */
            if (null == resultString) {
                productRegister.onRegisterFailedWithFailure(0);
            } else {
                processResponse(resultString);
            }
        }

        private void processResponse(String resultString) {
            try {
                JSONObject productResponse = new JSONObject(resultString);
                if (!productResponse.isNull("data")) {
                    Log.i(LOG_TAG, "Registration success");
                    productRegister.onRegisterSuccess(resultString);
                } else {
                    Log.i(LOG_TAG, "Registration failed");
                    if (productResponse.isNull("ERROR")) {
                        productRegister.onRegisterFailedWithFailure(0);
                    } else {
                        JSONObject errorJSONObj = productResponse.getJSONObject("ERROR");
                        if (errorJSONObj.isNull("errorMessage")) {
                            productRegister.onRegisterFailedWithFailure(0);
                        } else {
                            String errorMsg = errorJSONObj.getString("errorMessage");
                            // MUST CHECK ERRORS ACCORDING TO CODE. PRX
                            // RETURNS '0' FOR ALL. Workaround - Checking
                            // error message
                            if (errorMsg.equalsIgnoreCase("access_token expired")) {
                                refreshAccessTokenAndRegister();
                            } else if (errorMsg.equalsIgnoreCase("malformed access token")) {
                                productRegister.onRegisterFailedWithFailure(8);
                            } else if (errorMsg.equalsIgnoreCase("unknown access token")) {
                                productRegister.onRegisterFailedWithFailure(9);
                            } else {
                                productRegister.onRegisterFailedWithFailure(0);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                productRegister.onRegisterFailedWithFailure(0);
            }
        }

        private void refreshAccessTokenAndRegister() {
            // Refresh access token and proceed for
            // registration
            Log.i(LOG_TAG, "Current access token : " + getAccessToken());
            refreshLoginSession(new RefreshLoginSessionHandler() {

                @Override
                public void onRefreshLoginSessionSuccess() {
                    Log.i(LOG_TAG, "Latest access token : " + getAccessToken());
                    registerProduct(prodRegInfo, productRegister, locale, mContext);
                }

                @Override
                public void onRefreshLoginSessionFailedWithError(int error) {
                    productRegister.onRegisterFailedWithFailure(6);
                }

                @Override
                public void onRefreshLoginSessionInProgress(String message) {
                    //
                }
            });
        }
    }

    // -----------------------------------
    // Retrieving register product
    // -----------------------------------

    public void getRegisteredProducts(String url, String accessToken,
                                      ProductRegistrationHandler productRegister) {
        GetData getBuildType = new GetData();
        getBuildType.url = url;
        getBuildType.accessToken = accessToken;
        getBuildType.productRegister = productRegister;
        getBuildType.execute();
    }

    public class GetData extends AsyncTask<Void, Void, String> {

        String url;

        ProductRegistrationHandler productRegister;

        String accessToken;

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new HttpClient();
           // String resultString = httpClient.connectWithHttpGet(url, accessToken);
            String resultString = httpClient.callGet(url, accessToken);
            return resultString;
        }

        @Override
        protected void onPostExecute(String resultString) {
            super.onPostExecute(resultString);
            if (resultString != null)
                productRegister.onRegisterSuccess(resultString);
            else
                productRegister.onRegisterFailedWithFailure(0);
        }
    }

    @Override
    public void onLocaleMatchRefreshed(String locale) {
        PILLocaleManager manager = new PILLocaleManager(mContext);
        PILLocale pilLocaleInstance = manager.currentLocaleWithCountryFallbackForPlatform(mContext,locale,
                Platform.PRX, mProdInfo.getSector(), mProdInfo.getCatalog());

        if (null != pilLocaleInstance) {
            Log.i(LOG_TAG, "UserWithProductRegistration, onLocaleMatchRefreshed  RESULT = "
                    + pilLocaleInstance.getCountrycode() + pilLocaleInstance.getLanguageCode()
                    + pilLocaleInstance.getLocaleCode());
            startProdRegAsyncTask(pilLocaleInstance.getLocaleCode());

        } else {
            Log.i(LOG_TAG,
                    "UserWithProductRegistration, onLocaleMatchRefreshed from app RESULT = NULL");
            String[] inputLocaleArr = mInputLocale.split("_");
            startProdRegAsyncTask(inputLocaleArr[0].toLowerCase() + "_"
                    + inputLocaleArr[1].toUpperCase());
        }
    }

    @Override
    public void onErrorOccurredForLocaleMatch(LocaleMatchError error) {
        Log.i(LOG_TAG, "UserWithProductRegistration, onErrorOccurredForLocaleMatch");
        String[] inputLocaleArr = mInputLocale.split("_");
        startProdRegAsyncTask(inputLocaleArr[0].toLowerCase() + "_"
                + inputLocaleArr[1].toUpperCase());

    }

}
