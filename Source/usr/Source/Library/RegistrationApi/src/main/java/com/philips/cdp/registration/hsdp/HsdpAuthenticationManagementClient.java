package com.philips.cdp.registration.hsdp;

import android.util.Base64;

import com.philips.cdp.registration.configuration.HSDPConfiguration;
import com.philips.cdp.registration.ui.utils.MapUtils;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.ntputils.ServerTime;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HsdpAuthenticationManagementClient extends HsdpRequestClient {

    private String TAG = HsdpAuthenticationManagementClient.class.getSimpleName();
    private HSDPConfiguration hsdpConfiguration;

    HsdpAuthenticationManagementClient(HSDPConfiguration hsdpConfiguration) {
        super(hsdpConfiguration);
        this.hsdpConfiguration = hsdpConfiguration;
    }

    Map<String, Object> loginSocialProviders(String email, String socialAccessToken, String secret) {
        String apiEndpoint = "/authentication/login/social";
        String queryParams = "applicationName=" + hsdpConfiguration.getHsdpAppName();
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("accessToken", socialAccessToken);
        headers.put("refreshSecret", secret);
        headers.put("Api-version", "2");
        Map<String, String> body = new LinkedHashMap<String, String>();
        body.put("loginId", email);
        Map<String, Object> hsdpResponse = sendSignedRequestForSocialLogin("POST", apiEndpoint, queryParams, headers, body);

        return hsdpResponse;
    }

    public Map<String, Object> logout(String userId, String accessToken) {
        String apiEndpoint = "/authentication/users/" + userId + "/logout";
        String queryParams = "applicationName=" + hsdpConfiguration.getHsdpAppName();
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("accessToken", accessToken);
        try {
            return sendRestRequest("PUT", apiEndpoint, queryParams, headers, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    Map<String, Object> refreshSecret(String userUUID, String accessToken, String refreshSecret) {
        String apiEndpoint = "/authentication/users/" + userUUID + "/refreshAccessToken";
        String queryParams = "applicationName=" + hsdpConfiguration.getHsdpAppName();
        Map<String, String> headers = new LinkedHashMap<String, String>();

        String date = getUTCdatetimeAsString();
        headers.put("refreshSignature", createRefreshSignature(refreshSecret, date, accessToken));
        headers.put("refreshSignatureDate", date);
        headers.put("api-version", "2");
        headers.put("accessToken", accessToken);

        Map<String, Object> hsdpResponse = sendSignedRequestForSocialLogin("POST", apiEndpoint, queryParams, headers, null);
        if (hsdpResponse == null) {
            return null;
        }
        String responseCode = MapUtils.extract(hsdpResponse, "responseCode");
        String message = MapUtils.extract(hsdpResponse, "responseMessage");

        if (!"200".equals(responseCode)) return null;
        return hsdpResponse;
    }

    private String createRefreshSignature(String refresh_Secret, String date, String accessToken) {
        String stringToSign = "refresh_access_token\n" + date + "\n" + accessToken + "\n";
        RLog.d(TAG, "" + "Refresh secret : " + refresh_Secret + " date : " + date + " accessToken : " + accessToken);
        byte[] hash = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            byte[] refreshSecret = refresh_Secret.getBytes("UTF-8");
            SecretKeySpec secret = new SecretKeySpec(refreshSecret, mac.getAlgorithm());
            mac.init(secret);
            hash = mac.doFinal(stringToSign.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            RLog.e(TAG, "Error occurred while creating refresh signature.");
        } catch (UnsupportedEncodingException | InvalidKeyException e) {
            RLog.d(TAG, "Error occurred while creating refresh signature.");
        }
        return Base64.encodeToString(hash, Base64.NO_WRAP);
    }

    private String getUTCdatetimeAsString() {
        return ServerTime.getCurrentUTCTimeWithFormat("yyyy-MM-dd HH:mm:ss");
    }

}

