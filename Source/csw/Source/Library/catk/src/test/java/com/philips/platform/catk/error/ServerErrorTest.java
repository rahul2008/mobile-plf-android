/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.catk.error;

import com.android.volley.NetworkResponse;
import com.philips.platform.pif.chi.ConsentError;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
public class ServerErrorTest {
    @Test
    public void itShouldParseServerCorrectly() {
        givenJsonError(CORRECT_JSON_ERROR);
        thenConsentErrorCodeIs(ERROR_CODE);
        thenHasServerError();
        thenIncidentIdIs(INCIDENT_ID);
        thenServerErrorCodeIs(ERROR_CODE);
        thenDescriptionIs(DESCRIPTION_TEXT);
    }

    @Test
    public void itShouldCreateEmptyErrorWhenJsonIsEmpty() {
        givenJsonError(EMPTY_JSON_ERROR);
        thenConsentErrorCodeIs(ConsentError.CONSENT_UNKNOWN_SERVER_ERROR);
        thenHasServerError();
        thenHasNoIncidentId();
        thenServerErrorCodeIs(0);
        thenHasNoDescription();
    }

    @Test
    public void itShouldCreateEmptyErrorWhenJsonIsNull() {
        givenNullJsonError();
        thenConsentErrorCodeIs(ConsentError.CONSENT_UNKNOWN_SERVER_ERROR);
        thenHasNoServerError();
    }

    @Test
    public void itShouldCreateEmptyErrorWhenInvalidJson() {
        givenJsonError(INVALID_JSON_ERROR);
        thenConsentErrorCodeIs(ConsentError.CONSENT_UNKNOWN_SERVER_ERROR);
        thenHasNoServerError();
    }

    private void givenJsonError(String jsonError) {
        NetworkResponse networkResponse = new NetworkResponse(jsonError.getBytes());
        givenConsentNetworkError = new ConsentNetworkError(new com.android.volley.ServerError(networkResponse));
    }

    private void givenNullJsonError() {
        NetworkResponse networkResponse = new NetworkResponse(null);
        givenConsentNetworkError = new ConsentNetworkError(new com.android.volley.ServerError(networkResponse));
    }

    private void thenHasServerError() {
        assertNotNull(givenConsentNetworkError.getServerError());
    }

    private void thenHasNoServerError() {
        assertNull(givenConsentNetworkError.getServerError());
    }

    private void thenIncidentIdIs(String incidentId) {
        assertEquals(incidentId, givenConsentNetworkError.getServerError().getIncidentID());
    }

    private void thenServerErrorCodeIs(int errorCode) {
        assertEquals(errorCode, givenConsentNetworkError.getServerError().getErrorCode());
    }

    private void thenDescriptionIs(String description) {
        assertEquals(description, givenConsentNetworkError.getServerError().getDescription());
    }

    private void thenConsentErrorCodeIs(int consentErrorServerError) {
        assertEquals(consentErrorServerError, givenConsentNetworkError.getCatkErrorCode());
    }

    private void thenHasNoDescription() {
        assertNull(givenConsentNetworkError.getServerError().getDescription());
    }

    private void thenHasNoIncidentId() {
        assertNull(givenConsentNetworkError.getServerError().getIncidentID());
    }

    private ConsentNetworkError givenConsentNetworkError;

    private static final String INCIDENT_ID = "d1b2462f-1599-48f8-869b-d454dc94c99d";
    private static final int ERROR_CODE = 104;
    private static final String DESCRIPTION_TEXT = "Invalid data. markErrorAndGetPrevious code:1114 response raw body : {responseCode=1114, responseMessage=Invalid Application}";

    private static final String CORRECT_JSON_ERROR = "{\n" +
            "    \"incidentID\": \"" + INCIDENT_ID + "\",\n" +
            "    \"errorCode\": " + ERROR_CODE + ",\n" +
            "    \"description\": \"" + DESCRIPTION_TEXT + "\"\n" +
            "}";
    private static final String EMPTY_JSON_ERROR = "{}";
    private static final String INVALID_JSON_ERROR = "this is invalid json";
}