package com.philips.dhpclient;

import java.util.LinkedHashMap;
import java.util.Map;

import com.philips.dhpclient.response.DhpAuthenticationResponse;
import com.philips.dhpclient.response.DhpResponse;
import com.philips.dhpclient.util.MapUtils;

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

    public DhpAuthenticationManagementClient(DhpApiClientConfiguration dhpApiClientConfiguration) {
        super(dhpApiClientConfiguration);
    }

    public DhpAuthenticationResponse authenticate(String username, String password) {
        String apiEndpoint = "/authentication/login";
        String queryParams = "applicationName=" + dhpApplicationName;
        Map<String, String> headers = new LinkedHashMap<String, String>();
        AuthenticationRequestJson request = new AuthenticationRequestJson(username, password);

        DhpResponse dhpResponse = sendSignedRequest("POST", apiEndpoint, queryParams, headers, request);

        if (!"200".equals(dhpResponse.responseCode))
            return new DhpAuthenticationResponse(dhpResponse.rawResponse);

        String accessToken = MapUtils.extract(dhpResponse.rawResponse, "exchange.accessCredential.accessToken");
        String refreshToken = MapUtils.extract(dhpResponse.rawResponse, "exchange.accessCredential.refreshToken");
        String expiresIn = MapUtils.extract(dhpResponse.rawResponse, "exchange.accessCredential.expiresIn");
        String userId = MapUtils.extract(dhpResponse.rawResponse, "exchange.user.userUUID");

        return new DhpAuthenticationResponse(accessToken, refreshToken, Integer.parseInt(expiresIn), userId, dhpResponse.rawResponse);
    }

    public DhpAuthenticationResponse refresh(String userId, String refreshToken) {
        String apiEndpoint = "/authentication/users/" + userId + "/refreshToken";
        String queryParams = "applicationName=" + dhpApplicationName;
        Map<String, String> headers = new LinkedHashMap<String, String>();

        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
        DhpResponse dhpResponse = sendSignedRequest("PUT", apiEndpoint, queryParams, headers, request);

        if (!"200".equals(dhpResponse.responseCode))
            return new DhpAuthenticationResponse(dhpResponse.rawResponse);

        String newAccessToken = MapUtils.extract(dhpResponse.rawResponse, "exchange.accessToken");
        String newRefreshToken = MapUtils.extract(dhpResponse.rawResponse, "exchange.refreshToken");
        String expiresIn = MapUtils.extract(dhpResponse.rawResponse, "exchange.expiresIn");

        return new DhpAuthenticationResponse(newAccessToken, newRefreshToken, Integer.parseInt(expiresIn), userId, dhpResponse.rawResponse);
    }

    public DhpResponse validateToken(String userId, String accessToken) {
        String apiEndpoint = "/authentication/users/" + userId + "/tokenStatus";
        String queryParams = "applicationName=" + dhpApplicationName;
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("accessToken", accessToken);

        return sendRestRequest("GET", apiEndpoint, queryParams, headers, null);
    }

    public DhpResponse logout(String userId, String accessToken) {
        String apiEndpoint = "/authentication/users/" + userId + "/logout";
        String queryParams = "applicationName=" + dhpApplicationName;
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("accessToken", accessToken);

        return sendRestRequest("PUT", apiEndpoint, queryParams, headers, null);
    }
}
