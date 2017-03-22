package com.philips.platform.appinfra.languagepack;

import android.content.Context;
import android.os.Build;
import android.os.LocaleList;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.appinfra.languagepack.model.LanguageList;
import com.philips.platform.appinfra.languagepack.model.LanguageModel;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;
import com.philips.platform.appinfra.servicediscovery.RequestManager;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by philips on 3/13/17.
 */

public class LanguagePackManager implements LanguagePackInterface {

	private AppInfra mAppInfra;
	private Context mContext;
	RestInterface mRestInterface;
	private static final String LANGUAGE_PACK_CONFIG_SERVICE_ID_KEY = "LANGUAGEPACK.SERVICEID";
	private JSONObject languagePackOverviewFile;
	private LanguageList mLanguageList;

	public LanguagePackManager(AppInfra appInfra) {
		mAppInfra = appInfra;
		mContext = appInfra.getAppInfraContext();
		mRestInterface = appInfra.getRestClient();
		mLanguageList = new LanguageList();
	}

	@Override
	public void refresh(final OnRefreshListener aILPRefreshResult) {


		AppConfigurationInterface appConfigurationInterface = mAppInfra.getConfigInterface();
		AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
		String languagePackServiceId = (String) appConfigurationInterface.getPropertyForKey(LANGUAGE_PACK_CONFIG_SERVICE_ID_KEY, "APPINFRA", configError);


		ServiceDiscoveryInterface mServiceDiscoveryInterface = mAppInfra.getServiceDiscovery();

      /* mServiceDiscoveryInterface.getServiceUrlWithCountryPreference(languagePackServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                String languagePackConfigURL=url.toString();

                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AILP_URL", url.toString()); // US requirement to show language pack URL

                ////////////start of REST CALL////////////
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
                        languagePackConfigURL, ServiceIDUrlFormatting.SERVICEPREFERENCE.BYCOUNTRY, "", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AILP_URL", response.toString());
                        if(null!=response){
                        languagePackOverviewFile = response;
                            aILPRefreshResult.onSuccess(OnRefreshListener.AILPRefreshResult.RefreshedFromServer);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorcode = null != error.networkResponse ? error.networkResponse.statusCode + "" : "";
                        String errMsg= " Error Code:" + errorcode + " , Error Message:" + error.toString();
                        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AILP_URL",errMsg);
                        aILPRefreshResult.onError(OnRefreshListener.AILPRefreshResult.RefreshFailed, errMsg);
                    }
                });
                ////////////end of REST CALL////////////
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                ;
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AILP_URL", " Error Code:" + error.toString() + " , Error Message:" + message);
            }
        });*/

////////////start of REST CALL WITH MOCK URL////////////
		String languagePackConfigURL = "https://hashim-rest.herokuapp.com/sd/tst/en_IN/appinfra/lp.json";
		mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AILP_URL", languagePackConfigURL);


		JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
				languagePackConfigURL, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AILP_URL", "Overview Json: " + response.toString());
				if (null != response) {
					languagePackOverviewFile = response;

					Gson gson = new Gson();

					mLanguageList = gson.fromJson(response.toString(), LanguageList.class);
					aILPRefreshResult.onSuccess(OnRefreshListener.AILPRefreshResult.RefreshedFromServer);
					String url = getPreferedLocaleURL();

					JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AILP_URL", "Language Pack Json: " + response.toString());
							System.out.println("Language Pack Json" + " " + response.toString());
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {

						}
					}, null, null, null);

				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				String errorcode = null != error.networkResponse ? error.networkResponse.statusCode + "" : "";
				String errMsg = " Error Code:" + errorcode + " , Error Message:" + error.toString();
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AILP_URL", errMsg);
				aILPRefreshResult.onError(OnRefreshListener.AILPRefreshResult.RefreshFailed, errMsg);
			}
		}, null, null, null);
		////////////end of REST CALL WITH MOCK URL////////////

		mRestInterface.getRequestQueue().add(jsonRequest);


	}


	private String getLocaleList() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			return LocaleList.getDefault().toString();
		} else {
			return mAppInfra.getInternationalization().getUILocaleString();
		}
	}


	private String getPreferedLocaleURL() {
		ArrayList<LanguageModel> languageModels = mLanguageList.getLanguages();
		ArrayList<String> deviceLocaleList = new ArrayList<>(Arrays.asList(getLocaleList().split(",")));

		for (LanguageModel model : languageModels) {
			for (String deviceLocale : deviceLocaleList) {
				if (model.getLocale().equalsIgnoreCase(deviceLocale)) {
					return model.getUrl();
				} else if (model.getLocale().substring(0, 2).intern().equalsIgnoreCase(deviceLocale.substring(0, 2).intern())) {
					return model.getUrl();
				}
			}
		}
		String defaultlocale = "en_GB";
		if (languageModels.contains(defaultlocale.trim())) {
			int index = languageModels.indexOf("en_GB");
			return languageModels.get(index).getUrl();
		}
		return null;
	}

	public boolean saveFile(Context context, String mytext) {
		try {
			FileOutputStream fos = context.openFileOutput("file_name" + ".json", Context.MODE_PRIVATE);
			Writer out = new OutputStreamWriter(fos);
			out.write(mytext);
			out.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}


}
