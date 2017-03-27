package com.philips.platform.appinfra.languagepack;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.LocaleList;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.languagepack.model.LanguageList;
import com.philips.platform.appinfra.languagepack.model.LanguageModel;
import com.philips.platform.appinfra.languagepack.model.LanguagePackMetadata;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static com.philips.platform.appinfra.languagepack.LanguagePackConstants.LANGUAGE_PACK_CONFIG_SERVICE_ID_KEY;


/**
 * Created by philips on 3/13/17.
 */

public class LanguagePackManager implements LanguagePackInterface {

	private AppInfra mAppInfra;
	private Context mContext;
	private RestInterface mRestInterface;
	private LanguageList mLanguageList;
	private LanguageModel selectedLanguageModel;


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

		mServiceDiscoveryInterface.getServiceUrlWithCountryPreference(languagePackServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
			@Override
			public void onSuccess(URL url) {
				String languagePackConfigURL = url.toString();

				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AILP_URL", url.toString()); // US requirement to show language pack URL

				JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, languagePackConfigURL, null,
						new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AILP_URL", response.toString());
								if (null != response) {
									Gson gson = new Gson();
									mLanguageList = gson.fromJson(response.toString(), LanguageList.class);
									String url = getPreferredLocaleURL();
									downloadLanguagePack(url, aILPRefreshResult);
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
				mRestInterface.getRequestQueue().add(jsonRequest);
			}

			@Override
			public void onError(ERRORVALUES error, String message) {
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO,
						"AILP_URL", " Error Code:" + error.toString() + " , Error Message:" + message);
				String errMsg = " Error Code:" + error + " , Error Message:" + error.toString();
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AILP_URL", errMsg);
				aILPRefreshResult.onError(OnRefreshListener.AILPRefreshResult.RefreshFailed, errMsg);

			}
		});

	}

    private void downloadLanguagePack(String url, final OnRefreshListener aILPRefreshResult) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AILP_URL",
                                "Language Pack Json: " + response.toString());
                        saveFile(response.toString(), LanguagePackConstants.LOCALE_FILE_DOWNLOADED);
                        saveLocaleMetaData(selectedLanguageModel);
                        aILPRefreshResult.onSuccess(OnRefreshListener.AILPRefreshResult.RefreshedFromServer);
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
        mRestInterface.getRequestQueue().add(jsonObjectRequest);
    }

	private void saveLocaleMetaData(LanguageModel languageModel) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(LanguagePackConstants.LOCALE,languageModel.getLocale());
			jsonObject.put(LanguagePackConstants.VERSION,languageModel.getRemoteVersion());
			jsonObject.put(LanguagePackConstants.URL,languageModel.getUrl());
			saveFile(jsonObject.toString(),LanguagePackConstants.LOCALE_FILE_INFO);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	private String getLocaleList() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			return LocaleList.getDefault().toString();
		} else {
			return mAppInfra.getInternationalization().getUILocaleString();
		}
	}

	private String getPreferredLocaleURL() {

		ArrayList<LanguageModel> languageModels = mLanguageList.getLanguages();
		ArrayList<String> deviceLocaleList = new ArrayList<>(Arrays.asList(getLocaleList().split(",")));
		LanguageModel langModel = new LanguageModel();
		for (String deviceLocale : deviceLocaleList) {
			for (LanguageModel model : languageModels) {
				if (model.getLocale().equalsIgnoreCase(deviceLocale)) {
                    selectedLanguageModel = model;
					langModel.setRemoteVersion(model.getRemoteVersion());
					langModel.setLocale(model.getLocale());
					langModel.setUrl(model.getUrl());
					return model.getUrl();
				} else if (model.getLocale().substring(0, 2).intern().equalsIgnoreCase
						(deviceLocale.substring(0, 2).intern())) {
                    selectedLanguageModel = model;
					langModel.setRemoteVersion(model.getRemoteVersion());
					langModel.setLocale(model.getLocale());
					langModel.setUrl(model.getUrl());
					return model.getUrl();
				} else if (deviceLocale.contains(model.getLocale().substring(0, 2))) {
                    selectedLanguageModel = model;
					langModel.setRemoteVersion(model.getRemoteVersion());
					langModel.setLocale(model.getLocale());
					langModel.setUrl(model.getUrl());
					return model.getUrl();
				}

			}
		}


		//	String defaultlocale = new String("en_GB");
//		if (languageModels.contains(defaultlocale)) {
//			int index = languageModels.indexOf("en_GB");
//			return languageModels.get(index).getUrl();
//		}
		return null;
	}

	private File getFilePath(String fileName) {
		ContextWrapper contextWrapper = new ContextWrapper(mAppInfra.getAppInfraContext());
		File directory = contextWrapper.getExternalCacheDir();
		File file = new File(directory, LanguagePackConstants.LANGUAGE_PACK_PATH);
		File jsonFile = new File(file.getPath(), fileName);
		if(!file.exists()) {
			boolean mkdirs = file.mkdirs();
			if(!mkdirs) {
				Log.e(this.getClass() + "", "error in creating folders");
			} else {
				try {
					jsonFile.createNewFile();
				} catch (IOException var5) {
					var5.printStackTrace();
				}
			}
		}

		return jsonFile;
	}

	private void saveFile(String response, String fileName) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(getFilePath(fileName));
			fileWriter.write(response);
			fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null)
					fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String readFile(File file) {
		int length = (int) file.length();
		byte[] bytes = new byte[length];
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileInputStream != null)
					fileInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new String(bytes);
	}

	private void getFile(String fileName) {
		String myData = "";
		try {
			FileInputStream fis = new FileInputStream(getFilePath(fileName));
			DataInputStream in = new DataInputStream(fis);
			BufferedReader br =
					new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				myData = myData + strLine;
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean deleteFile(String fileName) {
		File file = getFilePath(fileName);
		return file.delete();
	}

	public void activate(OnActivateListener onActivateListener) {
		File file = getFilePath(LanguagePackConstants.LOCALE_FILE_INFO);
		Gson gson = new Gson();
		LanguagePackMetadata languagePackMetadata = gson.fromJson(readFile(file), LanguagePackMetadata.class);
		Log.d(getClass() + "", languagePackMetadata.getLocale() + "---" + languagePackMetadata.getUrl() + "-----" + languagePackMetadata.getVersion());
		onActivateListener.onSuccess(file.getAbsolutePath());
	}

}
