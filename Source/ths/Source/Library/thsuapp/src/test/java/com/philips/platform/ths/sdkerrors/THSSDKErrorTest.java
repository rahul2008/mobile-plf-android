/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.sdkerrors;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.SDKErrorReason;
import com.americanwell.sdk.entity.SDKResponseSuggestion;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class THSSDKErrorTest {
    THSSDKError mThssdkError;

    @Mock
    SDKError sdkErrorMock;

    @Mock
    SDKResponseSuggestion sdkResponseSuggestionMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mThssdkError = new THSSDKError();
        mThssdkError.setSdkError(sdkErrorMock);
    }

    @Test
    public void getSdkError() throws Exception {
        mThssdkError.setSdkError(sdkErrorMock);
        final SDKError sdkError = mThssdkError.getSdkError();
        assertNotNull(sdkError);
        assertThat(sdkError).isInstanceOf(SDKError.class);
    }

    @Test
    public void getSDKErrorReason() throws Exception {
        mThssdkError.setSdkError(sdkErrorMock);
        when(sdkErrorMock.getSDKErrorReason()).thenReturn(SDKErrorReason.ATTACHMENT_NOT_FOUND);
        String sdkErrorReason = mThssdkError.getSDKErrorReason();
        assertNotNull(sdkErrorReason);
        assertThat(sdkErrorReason).isInstanceOf(String.class);
    }

    @Test
    public void getSDKResponseSuggestion() throws Exception {
        when(sdkErrorMock.getSDKResponseSuggestion()).thenReturn(sdkResponseSuggestionMock);
        final SDKResponseSuggestion sdkResponseSuggestion = mThssdkError.getSDKResponseSuggestion();
        assertNotNull(sdkResponseSuggestion);
        assertThat(sdkResponseSuggestion).isInstanceOf(SDKResponseSuggestion.class);
    }

    @Test
    public void getMessage() throws Exception {
        when(sdkErrorMock.getMessage()).thenReturn("aaa");
        final String message = mThssdkError.getMessage();
        assertNotNull(message);
        assert message.equalsIgnoreCase("aaa");
    }

    @Test
    public void getCsrPhoneNumber() throws Exception {
        when(sdkErrorMock.getCsrPhoneNumber()).thenReturn("123456789");
        final String csrPhoneNumber = mThssdkError.getCsrPhoneNumber();
        assertNotNull(csrPhoneNumber);
        assert csrPhoneNumber.equalsIgnoreCase("123456789");
    }

    @Test
    public void getHttpResponseCode() throws Exception {
        when(sdkErrorMock.getHttpResponseCode()).thenReturn(401);
        final int httpResponseCode = mThssdkError.getHttpResponseCode();
        assert httpResponseCode == 401;
    }

}