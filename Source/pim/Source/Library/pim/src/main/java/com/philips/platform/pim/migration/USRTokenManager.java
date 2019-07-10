package com.philips.platform.pim.migration;

import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Pair;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.appinfra.timesync.TimeInterface;
import com.philips.platform.pif.DataInterface.USR.enums.Error;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.RefreshUSRTokenListener;
import com.philips.platform.pim.manager.PIMSettingManager;
import com.philips.platform.pim.rest.PIMRestClient;
import com.philips.platform.pim.rest.RefreshUSRTokenRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;

class USRTokenManager {
    private String TAG = PIMMigrationManager.class.getSimpleName();
    private final String USR_BASEURL = "userreg.janrain.api";
    private String signed_in_user;
    private LoggingInterface mLoggingInterface;
    private AppInfraInterface appInfraInterface;

    public USRTokenManager(AppInfraInterface mAppInfraInterface) {
        this.appInfraInterface = mAppInfraInterface;
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
        signed_in_user = fetchDataFromSecureStorage("jr_capture_signed_in_user");
    }

    void fetchRefreshedAccessToken(RefreshUSRTokenListener refreshUSRTokenListener) {
        downloadUserUrlFromSD(new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                if (urlMap != null && urlMap.get(USR_BASEURL) != null && urlMap.get(USR_BASEURL).getConfigUrls() != null) {
                    ServiceDiscoveryService serviceDiscoveryService = urlMap.get(USR_BASEURL);
                    String baseurl = serviceDiscoveryService.getConfigUrls();
                    String refreshUrl = baseurl + "/oauth/refresh_access_token";
                    String locale = serviceDiscoveryService.getLocale();
                    mLoggingInterface.log(DEBUG, TAG, " downloadUserUrlFromSD onSuccess. Refresh Url : " + baseurl + " Locale : " + locale);
                    refreshUSRAccessToken(refreshUrl, locale, refreshUSRTokenListener);
                    mLoggingInterface.log(DEBUG, TAG, " downloadUserUrlFromSD onSuccess");
                } else {
                    mLoggingInterface.log(DEBUG, TAG, "Migration Failed!! " + " Error in downloadUserUrlFromSD : " + "Not able to fetch config url");
                }
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                mLoggingInterface.log(DEBUG, TAG, "Migration Failed!! " + " Error in downloadUserUrlFromSD : " + message);
            }
        });
    }

    private String fetchDataFromSecureStorage(String jrCaptureSignedInUserKey) {
        return appInfraInterface.getSecureStorage().fetchValueForKey(jrCaptureSignedInUserKey, new SecureStorageInterface.SecureStorageError());
    }

    private void downloadUserUrlFromSD(ServiceDiscoveryInterface.OnGetServiceUrlMapListener serviceUrlMapListener) {
        ArrayList<String> serviceIdList = new ArrayList<>();
        serviceIdList.add(USR_BASEURL);
        appInfraInterface.getServiceDiscovery().getServicesWithCountryPreference(serviceIdList, serviceUrlMapListener, null);
    }


    private String getUSRAccessToken() {
        if (signed_in_user == null)
            return null;
        else {
            try {
                JSONObject jsonObject = new JSONObject(signed_in_user);
                return jsonObject.getString("accessToken");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private String parseLocale(String locale) {
        if (locale.contains("_")) {
            String[] splitLocal = locale.split("_");
            locale = splitLocal[0] + "-" + splitLocal[1];
        }
        return locale;
    }

    private String getRefreshSignature(String date, String accessToken) {
        String refresh_secret = fetchDataFromSecureStorage("jr_capture_refresh_secret");
        if (refresh_secret == null)
            return null;
        String stringToSign = "refresh_access_token\n" + date + "\n" + accessToken + "\n";
        byte[] hash;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            byte[] refreshSecret = refresh_secret.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secret = new SecretKeySpec(refreshSecret, mac.getAlgorithm());
            mac.init(secret);
            hash = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        } catch (InvalidKeyException | NoSuchAlgorithmException var7) {
            throw new RuntimeException("Unexpected", var7);
        }
        return Base64.encodeToString(hash, 2);
    }

    private String getFlowVersion() {
        String fetchedValue = fetchDataFromSecureStorage("jr_capture_flow");
        if (fetchedValue == null)
            return null;

        String formattedString = Html.fromHtml(fetchedValue).toString().replaceAll("\n", "").trim();
        Map<String, String> map = new HashMap<String, String>();

        String[] nameValuePairs = formattedString.split(",");
        for (String nameValuePair : nameValuePairs) {
            String[] nameValue = nameValuePair.split("=");
            try {
                map.put(nameValue[0].trim(), nameValue.length > 1 ?
                        nameValue[1].trim() : "");
            } catch (Exception e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }
        return map.get("version");
    }

    private String getUSRClientID() {
        if (signed_in_user == null)
            return null;
        else {
            try {
                JSONObject jsonObject = new JSONObject(signed_in_user);
                JSONArray clients = jsonObject.getJSONObject("this").getJSONArray("clients");
                return clients.getJSONObject(0).getString("clientId");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Nullable
    protected String getConfigPropertyValue(Object property) {
        if (property == null) {
            return null;
        }
        if (property instanceof String) {
            return (String) property;
        }
        if (property instanceof Map) {
            return getPropertyValueFromMap((Map) property);
        }
        return null;
    }

    private String getPropertyValueFromMap(Map<?, ?> property) {
        String locale = PIMSettingManager.getInstance().getLocale();
        String[] splitLocal = locale.split("-");
        String propertyValue = (String) property.get(splitLocal[1]);
        if (propertyValue == null || propertyValue.isEmpty()) {
            propertyValue = (String) property.get("default");
        }
        PIMSettingManager.getInstance().getLoggingInterface().log(LoggingInterface.LogLevel.DEBUG, TAG, "propertyValue: " + propertyValue);
        return propertyValue;
    }

    public String getClientId() {
        Object clientIdObject = appInfraInterface.getConfigInterface().getPropertyForKey("JanRainConfiguration.RegistrationClientID", "PIM", new AppConfigurationInterface.AppConfigurationError());
        String configPropertyValue = getConfigPropertyValue(clientIdObject);
        PIMSettingManager.getInstance().getLoggingInterface().log(LoggingInterface.LogLevel.DEBUG, TAG, "getclientId: " + configPropertyValue);
        PIMSettingManager.getInstance().getLoggingInterface().log(LoggingInterface.LogLevel.DEBUG, TAG, "hasclientId: " + (configPropertyValue != null));
        return configPropertyValue;
    }

    private <L, R> Collection<L> map(Collection<R> collection, Function<L, R> f) {
        Collection<L> retCollection;
        try {
            retCollection = collection.getClass().newInstance();
        } catch (InstantiationException ignore) {
            retCollection = new ArrayList<L>();
        } catch (IllegalAccessException ignore) {
            retCollection = new ArrayList<L>();
        }

        for (R e : collection) retCollection.add(f.operate(e));

        return retCollection;
    }

    private String paramsToString(Set<Pair<String, String>> bodyParams) {
        Collection<String> paramPairs = map(bodyParams, val -> {
            // return ((String) val.first).concat("=").concat(AndroidUtils.urlEncode((String) val.second));
            try {
                return val.first.concat("=").concat(URLEncoder.encode(val.second, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
        return TextUtils.join("&", paramPairs);
    }

    private void refreshUSRAccessToken(String refreshUrl, String locale, RefreshUSRTokenListener refreshUSRTokenListener) {
        if (signed_in_user == null) {
            mLoggingInterface.log(DEBUG, TAG, "Migration Failed!! " + "Signed_in_user not found");
            return;
        }

        String date = getUTCdatetimeAsString();
        String legacyToken = getUSRAccessToken();
        mLoggingInterface.log(DEBUG, TAG, "USR AccessToken : " + legacyToken);

        HashSet params = new HashSet();
        params.add(new Pair<>("locale", parseLocale(locale)));
        params.add(new Pair<>("signature", getRefreshSignature(date, legacyToken)));
        params.add(new Pair<>("date", date));
        params.add(new Pair<>("flow", "standard"));
        params.add(new Pair<>("flow_version", getFlowVersion()));
        params.add(new Pair<>("access_token", legacyToken));
        params.add(new Pair<>("client_id", getClientId()));

        RefreshUSRTokenRequest refreshLegacyTokenRequest = new RefreshUSRTokenRequest(refreshUrl, paramsToString(params));
        PIMRestClient pimRestClient = new PIMRestClient(PIMSettingManager.getInstance().getRestClient());
        pimRestClient.invokeRequest(refreshLegacyTokenRequest, response -> {
            try {
                JSONObject tokenObject = new JSONObject(response);
                String usrAccessToken = tokenObject.getString("access_token");
                mLoggingInterface.log(DEBUG, TAG, "Refresh USR token success. New Access Token :" + usrAccessToken);
                refreshUSRTokenListener.onRefreshTokenSuccess(usrAccessToken);
                //Perform Id assertion with user access token which
                //performIDAssertion(usrAccessToken);
            } catch (JSONException e) {
                e.printStackTrace();
                refreshUSRTokenListener.onRefreshTokenFailed(new Error(e.hashCode(), e.getMessage()));
            }
        }, error -> {
            refreshUSRTokenListener.onRefreshTokenFailed(new Error(error.hashCode(), error.getLocalizedMessage()));
            mLoggingInterface.log(DEBUG, TAG, "Migration Failed!! " + "Body for refresh token request : " + error.toString());
        });
    }

    private String getUTCdatetimeAsString() {
        String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.ROOT);
        sdf.setTimeZone(TimeZone.getTimeZone(TimeInterface.UTC));
        TimeInterface timeInterface = appInfraInterface.getTime();
        if (timeInterface != null) {
            return sdf.format(timeInterface.getUTCTime());
        }
        return null;
    }

    public boolean isUSRUserAvailable() {
        if (signed_in_user == null)
            return false;
        else
            return true;
    }


    /**
     * A first-class function stand-in
     *
     * @param <L> the return type of the function
     * @param <R> the parameter type of the function
     */
    public interface Function<L, R> {
        /**
         * The function's implementation
         *
         * @param arg
         * @return the function's evaluation on arg
         */
        L operate(R arg);
    }
}
