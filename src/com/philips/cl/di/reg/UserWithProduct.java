package com.philips.cl.di.reg;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.janrain.android.Jump;
import com.janrain.android.capture.Capture;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cl.di.localematch.LocaleMatchListener;
import com.philips.cl.di.localematch.LocaleMatchNotifier;
import com.philips.cl.di.localematch.PILLocale;
import com.philips.cl.di.localematch.PILLocaleManager;
import com.philips.cl.di.localematch.enums.LocaleMatchError;
import com.philips.cl.di.localematch.enums.Platform;
import com.philips.cl.di.reg.dao.ProductRegistrationInfo;
import com.philips.cl.di.reg.handlers.ProductRegistrationHandler;
import com.philips.cl.di.reg.settings.JanrainConfigurationSettings;

public class UserWithProduct implements LocaleMatchListener {

	private String LOG_TAG = "UserWithProductRegistration";

	private ProductRegistrationInfo mProdInfo = null;

	private ProductRegistrationHandler mProdRegHandler = null;

	private String mProdRegBaseUrl = null;

	private String mInputLocale = null;
	
	private String PRODUCT_SERIAL_NO = "productSerialNumber";

	private String PRODUCT_PURCHASE_DATE = "purchaseDate";

	private String PRODUCT_REGISTRATION_CHANNEL = "registrationChannel";

	public void getRefreshedAccessToken(
			final ProductRegistrationHandler productRegister) {
		if (Jump.getSignedInUser() != null)
			Jump.getSignedInUser().refreshAccessToken(
					new Capture.CaptureApiRequestCallback() {
						@Override
						public void onSuccess() {
							String accessToken = Jump.getSignedInUser()
									.getAccessToken();
							productRegister.onRegisterSuccess(accessToken);
						}

						@Override
						public void onFailure(CaptureApiError e) {
							productRegister.onRegisterFailedWithFailure(0);
						}
					});
	}

	public void register(ProductRegistrationInfo prodRegInfo,
			final ProductRegistrationHandler productRegisterHandler,
			String locale, Context context) {

		String localeArr[] = locale.split("_");
		String langCode = null;
		String countryCode = null;

		if (localeArr != null && localeArr.length > 0) {
			langCode = localeArr[0].toLowerCase();
			countryCode = localeArr[1].toUpperCase();
		}

		JanrainConfigurationSettings userSettings = JanrainConfigurationSettings
				.getInstance();

		mProdInfo = prodRegInfo;
		mProdRegHandler = productRegisterHandler;
		mProdRegBaseUrl = userSettings.getProductRegisterUrl();
		mInputLocale = locale;

		PILLocaleManager PILLocaleMngr = new PILLocaleManager();
		PILLocaleMngr.init(context, this);
		PILLocaleMngr.refresh(context, langCode, countryCode);

	}

	private void startProdRegAsyncTask(String locale) {
		ProdRegAsyncTask prodRegTask = new ProdRegAsyncTask();

		String prodRegUrl = mProdRegBaseUrl + mProdInfo.getSector() + "/"
				+ locale + "/" + mProdInfo.getCatalog() + "/products/"
				+ mProdInfo.getProductModelNumber() + ".register.type.product?";

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair(PRODUCT_SERIAL_NO,
				mProdInfo.getProductSerialNumber()));
		nameValuePair.add(new BasicNameValuePair(PRODUCT_PURCHASE_DATE, mProdInfo
				.getPurchaseDate()));
		nameValuePair.add(new BasicNameValuePair(PRODUCT_REGISTRATION_CHANNEL,
				mProdInfo.getRegistrationChannel()));

		prodRegTask.url = prodRegUrl;
		prodRegTask.productRegister = mProdRegHandler;
		prodRegTask.accessToken = Jump.getSignedInUser() != null ? Jump
				.getSignedInUser().getAccessToken() : null;
		prodRegTask.nameValuePairs = nameValuePair;
		prodRegTask.execute();
	}

	/*
	 * Async Task to register a product
	 */

	private class ProdRegAsyncTask extends AsyncTask<Void, Void, String> {
		String url;
		List<NameValuePair> nameValuePairs;
		ProductRegistrationHandler productRegister;
		String accessToken;

		@Override
		protected String doInBackground(Void... params) {
			HttpClient httpClient = new HttpClient();
			Log.i(LOG_TAG, "URL = " + url);
			String resultString = httpClient.postData(url, nameValuePairs,
					accessToken);
			return resultString;
		}

		@Override
		protected void onPostExecute(String resultString) {
			super.onPostExecute(resultString);
			if (resultString != null)
				productRegister.onRegisterSuccess(resultString);
			else {
				productRegister.onRegisterFailedWithFailure(0);
			}
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
			String resultString = httpClient.connectWithHttpGet(url,
					accessToken);
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
		unRegisterLocaleMatchListener();
		PILLocaleManager manager = new PILLocaleManager();
		PILLocale pilLocaleInstance = manager
				.currentLocaleWithCountryFallbackForPlatform(locale,
						Platform.PRX, mProdInfo.getSector(),
						mProdInfo.getCatalog());

		if (null != pilLocaleInstance) {
			Log.i(LOG_TAG,
					"UserWithProductRegistration, onLocaleMatchRefreshed  RESULT = "
							+ pilLocaleInstance.getCountrycode()
							+ pilLocaleInstance.getLanguageCode()
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
		Log.i(LOG_TAG,
				"UserWithProductRegistration, onErrorOccurredForLocaleMatch");
		unRegisterLocaleMatchListener();
		String[] inputLocaleArr = mInputLocale.split("_");
		startProdRegAsyncTask(inputLocaleArr[0].toLowerCase() + "_"
				+ inputLocaleArr[1].toUpperCase());

	}

	private void unRegisterLocaleMatchListener() {
		LocaleMatchNotifier notifier = LocaleMatchNotifier.getIntance();
		notifier.unRegisterLocaleMatchChange(this);
	}
}
