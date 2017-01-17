/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.servicediscovery.model;

import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.servicediscovery.RequestManager;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by 310238114 on 6/7/2016.
 */
public class ServiceDiscovery {

    private boolean success = false;
    String httpStatus;
    String country;
    private MatchByCountryOrLanguage matchByCountry;
    private MatchByCountryOrLanguage matchByLanguage;
    private AppInfra mAppInfra;

    Error error = null;


    public static class Error {
        private String message;
        private ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES errorvalue = null;

        public Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES e, String m) {
            errorvalue = e;
            message = m;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES getErrorvalue() {
            return errorvalue;
        }

        public void setErrorvalue(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES errorvalues) {
            this.errorvalue = errorvalues;
        }
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }


    public MatchByCountryOrLanguage getMatchByCountry() {
        return matchByCountry;
    }

    public void setMatchByCountry(MatchByCountryOrLanguage matchByCountry) {
        this.matchByCountry = matchByCountry;
    }


    public MatchByCountryOrLanguage getMatchByLanguage() {
        return matchByLanguage;
    }

    public void setMatchByLanguage(MatchByCountryOrLanguage matchByLanguage) {
        this.matchByLanguage = matchByLanguage;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }


    public void parseResponse(AppInfra appInfra, JSONObject response) {
        this.mAppInfra = appInfra;
        try {
            JSONObject payloadJSONObject = response.getJSONObject("payload");
            String country = response.getJSONObject("payload").optString("country");
            Log.i("Response", "" + country);
            this.country = country.toUpperCase();
            parseMatchByCountryJSON(payloadJSONObject.getJSONObject("matchByCountry"));
            parseMatchByLanguageJSON(payloadJSONObject.getJSONObject("matchByLanguage"));
        } catch (JSONException exception) {
            setError();
        }
    }

    private void parseMatchByLanguageJSON(JSONObject response) {
        try {
            matchByLanguage = new MatchByCountryOrLanguage();
            matchByLanguage.setAvailable(response.optBoolean("available"));

            JSONArray resultsLanguageJSONArray = response.optJSONArray("results");
            if (null == resultsLanguageJSONArray) {
                resultsLanguageJSONArray = new JSONArray();
                resultsLanguageJSONArray.put(response.optJSONObject("results"));
            } else if (resultsLanguageJSONArray.length() > 0) {
                matchByLanguage.setLocale(resultsLanguageJSONArray.getJSONObject(0).optString("locale"));
                ArrayList<MatchByCountryOrLanguage.Config> matchByLanguageConfigs = new ArrayList<>();
                JSONArray configLanguageJSONArray = resultsLanguageJSONArray.getJSONObject(0).optJSONArray("configs");
                if (configLanguageJSONArray != null) {
                    for (int configCount = 0; configCount < configLanguageJSONArray.length(); configCount++) {
                        MatchByCountryOrLanguage.Config config = new MatchByCountryOrLanguage.Config();
                        config.parseConfigArray(configLanguageJSONArray.optJSONObject(configCount));
                        matchByLanguageConfigs.add(config);
                    }
                }
                matchByLanguage.setConfigs(matchByLanguageConfigs);
            }
            setMatchByLanguage(matchByLanguage);
        } catch (JSONException exception) {
            setError();
        }
    }

    private void parseMatchByCountryJSON(JSONObject jsonObject) {
        try {
            matchByCountry = new MatchByCountryOrLanguage();
            this.matchByCountry.setAvailable(jsonObject.optBoolean("available"));
            JSONArray resultsJSONArray = jsonObject.optJSONArray("results");
            if (null == resultsJSONArray) {
                resultsJSONArray = new JSONArray();
                resultsJSONArray.put(jsonObject.optJSONObject("results"));
            }
            JSONArray configCountryJSONArray;
            String resConfig = getActualResultsForLocaleList(matchByCountry, resultsJSONArray);
            if (resConfig != null) {
                configCountryJSONArray = new JSONArray(resConfig);
            } else {
                matchByCountry.setLocale(resultsJSONArray.getJSONObject(0).optString("locale")); // return first locale if nothing matches
                configCountryJSONArray = resultsJSONArray.getJSONObject(0).optJSONArray("configs");
            }
            if (configCountryJSONArray != null) {
                for (int configCount = 0; configCount < configCountryJSONArray.length(); configCount++) {
                    MatchByCountryOrLanguage.Config config = new MatchByCountryOrLanguage.Config();
                    config.parseConfigArray(configCountryJSONArray.optJSONObject(configCount));
                    this.matchByCountry.configs.add(config);
                }
            }
            matchByCountry.setConfigs(matchByCountry.configs);
            setMatchByCountry(matchByCountry);
        } catch (JSONException e) {
            setError();
        }

    }

    private String getActualResultsForLocaleList(MatchByCountryOrLanguage matchByCountry,
                                                 JSONArray resultsJSONArray) {
        try {
            ArrayList<String> deviceLocaleList = new ArrayList<>(Arrays.asList(new RequestManager(mAppInfra)
                    .getLocaleList().split(",")));
            for (int i = 0; i < deviceLocaleList.size(); i++) {
                for (int j = 0; j < resultsJSONArray.length(); j++) {
                    String resLocale = resultsJSONArray.getJSONObject(j).optString("locale");
                    String deviceLocale = deviceLocaleList.get(i).replaceAll("[\\[\\]]", ""); // removing extra [] from locale list
                    if (deviceLocale.equals(resLocale)) {
                        matchByCountry.setLocale(resLocale);
                        return resultsJSONArray.getJSONObject(j).optString("configs");
                    } else if (deviceLocale.substring(0, 2).equals(resLocale.substring(0, 2))) { // comparing the language part of the locale
                        matchByCountry.setLocale(resLocale);
                        return resultsJSONArray.getJSONObject(0).optString("configs");
                    }
                }
            }
        } catch (JSONException e) {
            setError();
            e.printStackTrace();
        }
        return null;
    }


    private void setError() {
        Error err = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "Parsing error");
        setSuccess(false);
        setError(err);
    }

}
