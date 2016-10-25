package com.philips.dhpclient;

import android.util.Base64;

import com.philips.cdp.servertime.ServerTime;
import com.philips.dhpclient.response.DhpAuthenticationResponse;
import com.philips.dhpclient.response.DhpResponse;
import com.philips.dhpclient.util.HsdpLog;
import com.philips.dhpclient.util.MapUtils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class DhpAuthenticationManagementClient extends DhpApiClient {
    static class AuthenticationRequestJson {
        public String loginId;
        public String password;

        public AuthenticationRequestJson(String loginId, String password) {
            this.loginId = loginId;
            this.password = password;
        }
    }

    static class RefreshTokenRequest {
        public String refreshToken;

        public RefreshTokenRequest(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

    static class ResetPasswordRequest {
        public String resetPassword;

        public ResetPasswordRequest(String resetPassword) {
            this.resetPassword = resetPassword;
        }
    }

    public DhpAuthenticationManagementClient(DhpApiClientConfiguration dhpApiClientConfiguration) {
        super(dhpApiClientConfiguration);
    }

    public DhpAuthenticationResponse authenticate(final String username,final String password,final String secret) {
        String apiEndpoint = "/authentication/login";
        String queryParams = "applicationName=" + dhpApplicationName;
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("refreshSecret",secret);
        AuthenticationRequestJson request = new AuthenticationRequestJson(username, password);
        DhpResponse dhpResponse = sendSignedRequest("POST", apiEndpoint, queryParams, headers, request);

        return getDhpAuthenticationResponse(dhpResponse);
    }

    private DhpAuthenticationResponse getDhpAuthenticationResponse(DhpResponse dhpResponse) {
        if(dhpResponse == null){
            return null;
        }

        if (!"200".equals(dhpResponse.responseCode))
            return new DhpAuthenticationResponse(dhpResponse.rawResponse);

        String accessToken = MapUtils.extract(dhpResponse.rawResponse, "exchange.accessCredential.accessToken");
        String refreshToken = MapUtils.extract(dhpResponse.rawResponse, "exchange.accessCredential.refreshToken");
        String expiresIn = MapUtils.extract(dhpResponse.rawResponse, "exchange.accessCredential.expiresIn");
        String userId = MapUtils.extract(dhpResponse.rawResponse, "exchange.user.userUUID");

        return new DhpAuthenticationResponse(accessToken, refreshToken, Integer.parseInt(expiresIn), userId, dhpResponse.rawResponse);
    }

    public String createRefreshSignature(String refresh_Secret, String date, String accessToken) {
        String stringToSign = "refresh_access_token\n" + date + "\n" + accessToken + "\n";
        HsdpLog.d(HsdpLog.HSDP,""+"Refresh secret : "+refresh_Secret + " date : "+date + " accessToken : "+accessToken);
        byte[] hash = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            byte[] refreshSecret =refresh_Secret.getBytes("UTF-8");
            SecretKeySpec secret = new SecretKeySpec(refreshSecret, mac.getAlgorithm());
            mac.init(secret);
            hash = mac.doFinal(stringToSign.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            HsdpLog.e(HsdpLog.EXCEPTION,"Error occurred while creating refresh signature.");
        } catch (UnsupportedEncodingException e) {
            HsdpLog.d(HsdpLog.EXCEPTION,"Error occurred while creating refresh signature.");
        } catch (InvalidKeyException e) {
            HsdpLog.d(HsdpLog.EXCEPTION,"Error occurred while creating refresh signature.");
        }
        return Base64.encodeToString(hash, Base64.NO_WRAP);
    }

    private static String getUTCdatetimeAsString() {
        return ServerTime.getInstance().getCurrentUTCTimeWithFormat("yyyy-MM-dd HH:mm:ss");
    }

    public DhpAuthenticationResponse refresh(String userId, String refreshToken) {
        String apiEndpoint = "/authentication/users/" + userId + "/refreshToken";
        String queryParams = "applicationName=" + dhpApplicationName;
        Map<String, String> headers = new LinkedHashMap<String, String>();

        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
        DhpResponse dhpResponse = sendSignedRequest("PUT", apiEndpoint, queryParams, headers, request);

        if(dhpResponse == null){
            return null;
        }

        if (!"200".equals(dhpResponse.responseCode))
            return new DhpAuthenticationResponse(dhpResponse.rawResponse);

        String newAccessToken = MapUtils.extract(dhpResponse.rawResponse, "exchange.accessToken");
        String newRefreshToken = MapUtils.extract(dhpResponse.rawResponse, "exchange.refreshToken");
        String expiresIn = MapUtils.extract(dhpResponse.rawResponse, "exchange.expiresIn");

        return new DhpAuthenticationResponse(newAccessToken, newRefreshToken, Integer.parseInt(expiresIn), userId, dhpResponse.rawResponse);
    }

    public DhpAuthenticationResponse refreshSecret(String userId,String accessToken,String refreshSecret) {
        String apiEndpoint = "/authentication/users/" + userId + "/refreshAccessToken";
        String queryParams = "applicationName=" + dhpApplicationName;
        Map<String, String> headers = new LinkedHashMap<String, String>();

        String date = getUTCdatetimeAsString();
        headers.put("refreshSignature",createRefreshSignature(refreshSecret,date,accessToken));
        headers.put("refreshSignatureDate",date);
        headers.put("accessToken",accessToken);

        DhpResponse dhpResponse = sendSignedRequest("POST", apiEndpoint, queryParams, headers, null);
        if(dhpResponse == null){
            return null;
        }

        if (!"200".equals(dhpResponse.responseCode))
            return new DhpAuthenticationResponse(dhpResponse.rawResponse);

        String newAccessToken = MapUtils.extract(dhpResponse.rawResponse, "exchange.accessCredential.accessToken");
        String newRefreshToken = MapUtils.extract(dhpResponse.rawResponse, "exchange.refreshToken");
        String expiresIn = MapUtils.extract(dhpResponse.rawResponse, "exchange.accessCredential.expiresIn");

        return new DhpAuthenticationResponse(newAccessToken, newRefreshToken, Integer.parseInt(expiresIn), userId, dhpResponse.rawResponse);
    }

    public DhpAuthenticationResponse resetPassword(String emailAddress) {
        String apiEndpoint = "/authentication/credential/recoverPassword";
        String queryParams = "applicationName=" + dhpApplicationName;
        Map<String, String> headers = new LinkedHashMap<String, String>();

        ResetPasswordRequest request = new ResetPasswordRequest(emailAddress);
        DhpResponse dhpResponse = sendSignedRequest("POST", apiEndpoint, queryParams, headers, request);
        if(dhpResponse == null){
            return null;
        }
        if (!"200".equals(dhpResponse.responseCode))
            return new DhpAuthenticationResponse(dhpResponse.rawResponse);

        String newAccessToken = MapUtils.extract(dhpResponse.rawResponse, "exchange.accessCredential.accessToken");
        String newRefreshToken = MapUtils.extract(dhpResponse.rawResponse, "exchange.refreshToken");
        String expiresIn = MapUtils.extract(dhpResponse.rawResponse, "exchange.accessCredential.expiresIn");

        return new DhpAuthenticationResponse(newAccessToken, newRefreshToken, Integer.parseInt(expiresIn), null, dhpResponse.rawResponse);
    }

    public DhpResponse validateToken(String userId, String accessToken) {
        String apiEndpoint = "/authentication/users/" + userId + "/tokenStatus";
        String queryParams = "applicationName=" + dhpApplicationName;
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("accessToken", accessToken);

        return sendRestRequest("GET", apiEndpoint, queryParams, headers, null);
    }

    public DhpAuthenticationResponse loginSocialProviders(String email, String socialAccessToken,String secret) {
        String apiEndpoint = "/authentication/login/social";
        String queryParams = "applicationName="+ dhpApplicationName;
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("accessToken",socialAccessToken);
        headers.put("refreshSecret",secret);
        Map<String, String> body = new LinkedHashMap<String, String>();
        body.put("loginId",email);
        DhpResponse dhpResponse = sendSignedRequestForSocialLogin("POST", apiEndpoint, queryParams, headers, body);

        return getDhpAuthenticationResponse(dhpResponse);
    }

    public DhpResponse logout(String userId, String accessToken) {
        String apiEndpoint = "/authentication/users/" + userId + "/logout";
        String queryParams = "applicationName=" + dhpApplicationName;
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("accessToken", accessToken);
        return sendRestRequest("PUT", apiEndpoint, queryParams, headers, null);
    }
}
