/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.dhpclient;

import java.util.*;

import com.philips.dhpclient.request.DhpUserIdentity;
import com.philips.dhpclient.response.DhpResponse;
import com.philips.dhpclient.response.DhpUserRegistrationResponse;
import com.philips.dhpclient.util.MapUtils;

public class DhpUserManagementClient extends DhpApiClient {

    public static class DhpUserProfileResponse extends DhpResponse {
        public final String responseCode;
        public final DhpUserIdentity userIdentity;

        public DhpUserProfileResponse(String responseCode, DhpUserIdentity userIdentity) {
            super(responseCode, null);

            this.responseCode = responseCode;
            this.userIdentity = userIdentity;
        }
    }

    public DhpUserManagementClient(DhpApiClientConfiguration dhpApiClientConfiguration) {
        super(dhpApiClientConfiguration);
    }

    private void addCountryCodeToHeaders(Map<String, String> headers, String currentLocation) {
        headers.put("Country-Code", currentLocation);
    }

    public DhpUserRegistrationResponse registerUser(DhpUserIdentity registerUserRequest) {
        String apiEndpoint = "/usermanagement/users";
        String queryParams = "applicationName=" + dhpApplicationName;
        Map<String, String> headers = new LinkedHashMap<String, String>();

        addCountryCodeToHeaders(headers, registerUserRequest.profile.currentLocation);
        DhpResponse dhpResponse = sendSignedRequest("POST", apiEndpoint, queryParams, headers, registerUserRequest);

        String userId = MapUtils.extract(dhpResponse.rawResponse, "exchange.user.userUUID");
        return new DhpUserRegistrationResponse(userId, dhpResponse.rawResponse);
    }

    public DhpUserProfileResponse retrieveProfile(String userId, String accessToken) {
        String apiEndpoint = "/usermanagement/users/" + userId + "/profile";
        String queryParams = "applicationName=" + dhpApplicationName;
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("accessToken", accessToken);

        DhpResponse dhpResponse = sendRestRequest("GET", apiEndpoint, queryParams, headers, null);

        String responseCode = MapUtils.extract(dhpResponse.rawResponse, "responseCode");
        String loginId = MapUtils.extract(dhpResponse.rawResponse, "exchange.user.loginId");
        String givenName = MapUtils.extract(dhpResponse.rawResponse, "exchange.user.profile.givenName");
        String gender = MapUtils.extract(dhpResponse.rawResponse, "exchange.user.profile.gender");
        String birthDay = MapUtils.extract(dhpResponse.rawResponse, "exchange.user.profile.birthday");
        String preferredLanguage = MapUtils.extract(dhpResponse.rawResponse, "exchange.user.profile.preferredLanguage");
        String currentLocation = MapUtils.extract(dhpResponse.rawResponse, "exchange.user.profile.currentLocation");
        String familyName = MapUtils.extract(dhpResponse.rawResponse, "exchange.user.profile.familyName");
        String locale = MapUtils.extract(dhpResponse.rawResponse, "exchange.user.profile.locale");
        String timeZone = MapUtils.extract(dhpResponse.rawResponse, "exchange.user.profile.timeZone");
        String country = MapUtils.extract(dhpResponse.rawResponse, "exchange.user.profile.primaryAddress.country");
        Double height = remapZeroOrNegativeToNull(MapUtils.<Double> extract(dhpResponse.rawResponse, "exchange.user.profile.height"));
        Double weight = remapZeroOrNegativeToNull(MapUtils.<Double> extract(dhpResponse.rawResponse, "exchange.user.profile.weight"));
        List<DhpUserIdentity.Photo> photos = getPhotos(dhpResponse.rawResponse);

        return new DhpUserProfileResponse(responseCode, new DhpUserIdentity(loginId, null, new DhpUserIdentity.Profile(
                givenName, null, familyName, birthDay, currentLocation, null, locale, gender, timeZone, preferredLanguage, height, weight, new DhpUserIdentity.Address(country),
                photos
                )));
    }

    private Double remapZeroOrNegativeToNull(Double value) {
        if (value == null || value <= 0.0)
            return null;

        return value;
    }

    public DhpResponse changePassword(String loginId, String currentPassword, String newPassword, String accessToken) {
        String apiEndpoint = "/authentication/credential/changePassword";
        String queryParams = "applicationName=" + dhpApplicationName;
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("accessToken", accessToken);

        Map<String, String> requestBody = new LinkedHashMap<String, String>();
        requestBody.put("loginId", loginId);
        requestBody.put("currentPassword", currentPassword);
        requestBody.put("newPassword", newPassword);

        return sendRestRequest("POST", apiEndpoint, queryParams, headers, requestBody);
    }

    public DhpResponse resetPassword(String loginId) {
        String apiEndpoint = "/authentication/credential/recoverPassword";
        String queryParams = "applicationName=" + dhpApplicationName;
        Map<String, String> headers = new LinkedHashMap<String, String>();

        Map<String, String> requestBody = new LinkedHashMap<String, String>();
        requestBody.put("loginId", loginId);

        return sendSignedRequest("POST", apiEndpoint, queryParams, headers, requestBody);
    }

    public DhpResponse updateProfile(String userId, DhpUserIdentity.Profile userProfile, String accessToken) {
        String apiEndpoint = "/usermanagement/users/" + userId + "/profile";
        String queryParams = "applicationName=" + dhpApplicationName;
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("accessToken", accessToken);
        addCountryCodeToHeaders(headers, userProfile.currentLocation);

        return sendRestRequest("PUT", apiEndpoint, queryParams, headers, userProfile);
    }

    private List<DhpUserIdentity.Photo> getPhotos(Map<String, Object> responseMap) {
        List<Map<String, String>> rawPhotos = MapUtils.extract(responseMap, "exchange.user.profile.photos");

        if (rawPhotos == null)
            return null;

        List<DhpUserIdentity.Photo> photos = new ArrayList<>();

        for (Map<String, String> photoMap : rawPhotos)
            photos.add(new DhpUserIdentity.Photo(photoMap.get("type"), photoMap.get("value")));

        return photos;
    }

    public DhpResponse resendConfirmation(String email) {
        String apiEndpoint = "/usermanagement/users/activate";
        String queryParams = "applicationName=" + dhpApplicationName;
        Map<String, String> headers = new LinkedHashMap<String, String>();

        Map<String, String> requestBody = new LinkedHashMap<String, String>();
        requestBody.put("loginId", email);

        return sendSignedRequest("PUT", apiEndpoint, queryParams, headers, requestBody);
    }
}
