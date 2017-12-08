/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.dhpclient;

import com.philips.dhpclient.request.DhpStoreTermsAndConditionsRequest;
import com.philips.dhpclient.response.DhpResponse;
import com.philips.dhpclient.util.Objects;
import com.philips.dhpclient.util.MapUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DhpSubscriptionServiceClient extends DhpApiClient {
    private static final String TERMS_AND_CONDITIONS_DOCUMENT_ID = "1";
    private static final String DHP_RESPONSE_CODE_NO_TERMS_RECORDS_FOUND = "1151";

    public static class DhpTermsAndConditionsResponse extends DhpResponse {
        public final String responseCode;
        public final String acceptedTermsVersion;

        public DhpTermsAndConditionsResponse(String responseCode, String acceptedTermsVersion) {
            super(responseCode, null);

            this.responseCode = responseCode;
            this.acceptedTermsVersion = acceptedTermsVersion;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            DhpTermsAndConditionsResponse that = (DhpTermsAndConditionsResponse) o;
            return responseCode.equals(that.responseCode) &&
                    acceptedTermsVersion.equals(that.acceptedTermsVersion);
        }

        @Override
        public int hashCode() {
            return Objects.hash(responseCode, acceptedTermsVersion);
        }

        @Override
        public String toString() {
            return "DhpTermsAndConditionsResponse{" +
                   "responseCode='" + responseCode + '\'' +
                   ", acceptedTermsVersion='" + acceptedTermsVersion + '\'' +
                   "} " + super.toString();
        }
    }

    public DhpSubscriptionServiceClient(DhpApiClientConfiguration dhpApiClientConfiguration) {
        super(dhpApiClientConfiguration);
    }

    public DhpResponse subscribe(String dhpUserId, String accessToken) {
        String apiEndpoint = "/subscription/applications/" + dhpApplicationName + "/users/" + dhpUserId + "/subscribe";
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("accessToken", accessToken);

        return sendRestRequest("POST", apiEndpoint, "", headers, "");
    }

    public DhpResponse closeAccount(String dhpUserId, boolean wipeData, String accessToken) {
        String apiEndpoint = "/subscription/applications/" + dhpApplicationName + "/users/" + dhpUserId + "/close";
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("accessToken", accessToken);

        Map<String, String> requestBody = new LinkedHashMap<String, String>();
        requestBody.put("deleteDataFlag", wipeData ? "Yes" : "No");

        return sendRestRequest("PUT", apiEndpoint, "", headers, requestBody);
    }

    public DhpResponse storeTermsAndConditionsAccepted(String dhpUserId, String consentCode, String countryCode, String documentVersion) {
        String apiEndpoint = "/subscription/users/" + dhpUserId + "/termsAndConditions";
        Map<String, String> headers = new LinkedHashMap<String, String>();

        DhpStoreTermsAndConditionsRequest request = new DhpStoreTermsAndConditionsRequest(dhpApplicationName, TERMS_AND_CONDITIONS_DOCUMENT_ID, documentVersion,
                countryCode,
                consentCode);
        return sendSignedRequest("PUT", apiEndpoint, "", headers, request);
    }

    public DhpTermsAndConditionsResponse retrieveTermsAndConditionsDocumentVersion(String userId, String accessToken) {
        String apiEndpoint = "/subscription/applications/" + dhpApplicationName + "/users/" + userId + "/termsAndConditions";
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("accessToken", accessToken);
        String queryParams = "consentCode=1&standardObservationName=";

        DhpResponse dhpResponse = sendRestRequest("GET", apiEndpoint, queryParams, headers, null);

        if (dhpResponse.responseCode.equals(DHP_RESPONSE_CODE_NO_TERMS_RECORDS_FOUND)) {
            // Hide response code 1151 from the caller as it's a very inconsistent way to indicate no terms where signed by the user
            // To the caller it's just a successful response without terms signed
            return new DhpTermsAndConditionsResponse(DhpResponse.SUCCESS.responseCode, null);
        }

        String lastAcceptedTermsVersion = getLastAcceptedTermsAndConditions(dhpResponse.rawResponse);
        return new DhpTermsAndConditionsResponse(dhpResponse.responseCode, lastAcceptedTermsVersion);
    }

    private String getLastAcceptedTermsAndConditions(Map<String, Object> responseMap) {
        List<Map<String, Object>> userDocumentsList = MapUtils.extract(responseMap, "exchange.userTermsAndConditionsCollection.userTermsAndConditionsList");

        if (userDocumentsList == null)
            return null;

        String lastAcceptedTermsVersion = null;
        Date currentLastStartDate = null;

        for (Map<String, Object> document : userDocumentsList) {
            if (document.get("applicationName") == null || !document.get("applicationName").equals(dhpApplicationName))
                continue;

            String documentId = (String) document.get("documentId");

            if (documentId == null || !documentId.equals(TERMS_AND_CONDITIONS_DOCUMENT_ID))
                continue;

            String dateStr = (String) document.get("startDate");
            Date startDate = null;
            try {
                startDate = SimpleDateFormat.getDateTimeInstance().parse(dateStr);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Error in date formatting", e);
            }

            if (currentLastStartDate == null || currentLastStartDate.before(startDate)) {
                currentLastStartDate = startDate;
                lastAcceptedTermsVersion = (String) document.get("documentVersion");
            }
        }

        return lastAcceptedTermsVersion;
    }
}
