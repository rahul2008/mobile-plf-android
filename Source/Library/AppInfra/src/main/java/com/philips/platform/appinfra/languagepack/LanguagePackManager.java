/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.languagepack;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.FileUtils;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.languagepack.model.LanguageList;
import com.philips.platform.appinfra.languagepack.model.LanguagePackMetadata;
import com.philips.platform.appinfra.languagepack.model.LanguagePackModel;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import static com.philips.platform.appinfra.languagepack.LanguagePackConstants.LANGUAGE_PACK_CONFIG_SERVICE_ID_KEY;
import static com.philips.platform.appinfra.languagepack.LanguagePackConstants.LOCALE_FILE_ACTIVATED;

/**
 * The Language Pack Manager .
 */
public class LanguagePackManager implements LanguagePackInterface {

	private AppInfra mAppInfra;
	private RestInterface mRestInterface;
	private LanguageList mLanguageList;
	private LanguagePackModel selectedLanguageModel;
	private FileUtils languagePackUtil;
	private Gson gson;
	private Context context;


    @NonNull
    protected ServiceDiscoveryInterface.OnGetServiceUrlListener getServiceDiscoveryListener(final OnRefreshListener aILPRefreshResult) {
        return new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                final String languagePackConfigURL = url.toString();
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_LANGUAGE_PACK, "Langauge pack get Service Discovery Listener OnSuccess URL" + url.toString()); // US requirement to show language pack URL

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, languagePackConfigURL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_LANGUAGE_PACK, "onResponse " + response.toString());
                                languagePackHandler = getHandler(context);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        processForLanguagePack(response, aILPRefreshResult);
                                    }
                                }).start();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        final String errorcode = null != error.networkResponse ? error.networkResponse.statusCode + "" : "";
                        final String errMsg = " Error Code:" + errorcode + " , Error Message:" + error.toString();
                        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_LANGUAGE_PACK, "on Error Response" + errMsg);
                        aILPRefreshResult.onError(OnRefreshListener.AILPRefreshResult.REFRESH_FAILED, errMsg);

                    }
                }, null, null, null);
                mRestInterface.getRequestQueue().add(jsonRequest);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
                        AppInfraLogEventID.AI_LANGUAGE_PACK, " Error Code:" + error.toString() + " , Error Message:" + message);
                final String errMsg = " Error Code:" + error + " , Error Message:" + error.toString();
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_LANGUAGE_PACK, "on Error URL" + errMsg);
                aILPRefreshResult.onError(OnRefreshListener.AILPRefreshResult.REFRESH_FAILED, errMsg);

            }
        };
    }

	private Handler languagePackHandler;

    public LanguagePackManager(AppInfra appInfra) {
        mAppInfra = appInfra;
        mRestInterface = appInfra.getRestClient();
        this.context = appInfra.getAppInfraContext();
        VolleyLog.DEBUG = false;
        mLanguageList = new LanguageList();
        languagePackUtil = new FileUtils(appInfra.getAppInfraContext());
        gson = new Gson();
    }

	/**
	 * Api used to refresh language pack
	 *
	 * @param aILPRefreshResult OnRefresh Listener
	 */
    @Override
    public void refresh(final OnRefreshListener aILPRefreshResult) {
        final String languagePackServiceId = getLanguagePackConfig(mAppInfra.getConfigInterface(), mAppInfra);
        if (null == languagePackServiceId) {
            aILPRefreshResult.onError(OnRefreshListener.AILPRefreshResult.REFRESH_FAILED, "Invalid ServiceID");
        } else {
            ServiceDiscoveryInterface mServiceDiscoveryInterface = mAppInfra.getServiceDiscovery();
            mServiceDiscoveryInterface.getServiceUrlWithCountryPreference(languagePackServiceId, getServiceDiscoveryListener(aILPRefreshResult));
        }
    }


    private Runnable postRefreshSuccess(final OnRefreshListener aILPRefreshResult, final OnRefreshListener.AILPRefreshResult ailpRefreshResult) {
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_LANGUAGE_PACK, "post Refresh Success");
        return new Runnable() {
            @Override
            public void run() {
                if (aILPRefreshResult != null)
                    aILPRefreshResult.onSuccess(ailpRefreshResult);
            }
        };
    }

    private Runnable postRefreshError(final OnRefreshListener aILPRefreshResult, final OnRefreshListener.AILPRefreshResult ailpRefreshResult, final String errorDescription) {
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_LANGUAGE_PACK, "post Refresh Error");
        return new Runnable() {
            @Override
            public void run() {
                if (aILPRefreshResult != null)
                    aILPRefreshResult.onError(ailpRefreshResult, errorDescription);
            }
        };
    }

    private void processForLanguagePack(JSONObject response, OnRefreshListener aILPRefreshResult) {
        mLanguageList = gson.fromJson(response.toString(), LanguageList.class);
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_LANGUAGE_PACK, "process For Language Pack");
        if (null != mLanguageList) {
            final String url = getPreferredLocaleURL();
            if (null == url) {
                languagePackHandler.post(postRefreshError(aILPRefreshResult, OnRefreshListener.AILPRefreshResult.REFRESH_FAILED, "Not able to read overview file"));
            } else {
                final boolean languagePackDownloadRequired = isLanguagePackDownloadRequired(selectedLanguageModel);
                if (languagePackDownloadRequired) {
                    downloadLanguagePack(url, aILPRefreshResult);
                } else {
                    languagePackHandler.post(postRefreshSuccess(aILPRefreshResult, OnRefreshListener.AILPRefreshResult.NO_REFRESH_REQUIRED));
                }
            }
        } else {
            languagePackHandler.post(postRefreshError(aILPRefreshResult, OnRefreshListener.AILPRefreshResult.REFRESH_FAILED, "Not able to read overview file"));
        }
    }

    boolean isLanguagePackDownloadRequired(LanguagePackModel selectedLanguageModel) {
        final File file = languagePackUtil.getFilePath(LanguagePackConstants.LOCALE_FILE_INFO, LanguagePackConstants.LANGUAGE_PACK_PATH);
        final String json = languagePackUtil.readFile(file);
        final LanguagePackMetadata languagePackMetadata = gson.fromJson(json, LanguagePackMetadata.class);
        if (languagePackMetadata == null) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_LANGUAGE_PACK, "is Language Pack Download Required true");
            return true;
        } else if (languagePackMetadata.getUrl().equalsIgnoreCase(selectedLanguageModel.getUrl())
                && (Integer.parseInt(languagePackMetadata.getVersion()) >= Integer.parseInt(selectedLanguageModel.getVersion()))
                && languagePackMetadata.getLocale().equalsIgnoreCase(selectedLanguageModel.getLocale())) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_LANGUAGE_PACK, "is Language Pack Download Required false");
            return false;
        }

        return true;
    }

    Handler getHandler(Context context) {
        return new Handler(context.getMainLooper());
    }

    private void downloadLanguagePack(String url, final OnRefreshListener aILPRefreshResult) {
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_LANGUAGE_PACK, "downloading language pack to fetch Language Pack Json ");
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_LANGUAGE_PACK,
                                "Language Pack Json: " + response.toString());
                        if (null != selectedLanguageModel && null != languagePackHandler) {
                            languagePackUtil.saveFile(response.toString(), LanguagePackConstants.LOCALE_FILE_DOWNLOADED, LanguagePackConstants.LANGUAGE_PACK_PATH);
                            languagePackUtil.saveLocaleMetaData(selectedLanguageModel);
                            languagePackHandler.post(postRefreshSuccess(aILPRefreshResult, OnRefreshListener.AILPRefreshResult.REFRESHED_FROM_SERVER));
                        } else {
                            aILPRefreshResult.onError(OnRefreshListener.AILPRefreshResult.REFRESH_FAILED, OnRefreshListener.AILPRefreshResult.REFRESH_FAILED.toString());

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                final String errorCode = null != error.networkResponse ? error.networkResponse.statusCode + "" : "";
                final String errMsg = " Error Code:" + errorCode + " , Error Message:" + error.toString();
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_LANGUAGE_PACK, errMsg);
                languagePackHandler.post(postRefreshError(aILPRefreshResult, OnRefreshListener.AILPRefreshResult.REFRESH_FAILED, errMsg));
            }
        }, null, null, null);
        mRestInterface.getRequestQueue().add(jsonObjectRequest);
    }

    private String getPreferredLocaleURL() {
        final ArrayList<LanguagePackModel> languagePackModels = mLanguageList.getLanguages();
        if (null != languagePackModels) {
            final LanguagePackModel langModel = new LanguagePackModel();
            final String appLocale = mAppInfra.getInternationalization().getUILocaleString();

            for (LanguagePackModel model : languagePackModels) {
                if (model != null && model.getLocale() != null && appLocale != null) {
                    if (model.getLocale().equalsIgnoreCase(appLocale)) {
                        selectedLanguageModel = model;
                        langModel.setVersion(model.getVersion());
                        langModel.setLocale(model.getLocale());
                        langModel.setUrl(model.getUrl());
                        return model.getUrl();
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void activate(final OnActivateListener onActivateListener) {
        final File file = languagePackUtil.getFilePath(LanguagePackConstants.LOCALE_FILE_INFO, LanguagePackConstants.LANGUAGE_PACK_PATH);
        final LanguagePackMetadata languagePackMetadata = gson.fromJson(languagePackUtil.readFile(file), LanguagePackMetadata.class);
        if (languagePackMetadata != null) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_LANGUAGE_PACK, "Language pack metadata info contains : " + languagePackMetadata.getLocale() + "---" + languagePackMetadata.getUrl() + "-----" + languagePackMetadata.getVersion());
        }

        languagePackHandler = getHandler(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean isRenamed = languagePackUtil.renameOnActivate();
                if (isRenamed) {
                    languagePackHandler.post(postActivateSuccess(onActivateListener));
                } else
                    languagePackHandler.post(postActivateError(onActivateListener, OnActivateListener.AILPActivateResult.NO_UPDATE_STORED, "No Language Pack available"));
            }
        }).start();
    }

    private Runnable postActivateError(final OnActivateListener onActivateListener, final OnActivateListener.AILPActivateResult error, final String errorMessage) {
        return new Runnable() {
            @Override
            public void run() {
                if (onActivateListener != null)
                    onActivateListener.onError(error, errorMessage);
            }
        };
    }

    private Runnable postActivateSuccess(final OnActivateListener onActivateListener) {
        return new Runnable() {
            @Override
            public void run() {
                if (onActivateListener != null) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_LANGUAGE_PACK, "Language Pack Activated path" + languagePackUtil.getFilePath(LOCALE_FILE_ACTIVATED, LanguagePackConstants.LANGUAGE_PACK_PATH).getAbsolutePath());
                    onActivateListener.onSuccess(languagePackUtil.getFilePath(LOCALE_FILE_ACTIVATED, LanguagePackConstants.LANGUAGE_PACK_PATH).getAbsolutePath());
                }
            }
        };
    }


    public static String getLanguagePackConfig(AppConfigurationInterface appConfigurationManager, AppInfra ai) {
        try {
            final AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface
                    .AppConfigurationError();
            return (String) appConfigurationManager.getPropertyForKey
                    (LANGUAGE_PACK_CONFIG_SERVICE_ID_KEY, "APPINFRA", configError);
        } catch (IllegalArgumentException exception) {
            ai.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,
                    AppInfraLogEventID.AI_APPINFRA, "Error in reading LanguagePack  Config "
                            + exception.toString());
        }
        return null;
    }
}