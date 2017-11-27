/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.sdkerrors;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.SDKPasswordError;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class THSSDKPasswordErrorTest {

    THSSDKPasswordError mThssdkPasswordError;

    @Mock
    SDKPasswordError sdkPasswordErrorMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mThssdkPasswordError = new THSSDKPasswordError();
    }

    @Test
    public void getSdkPasswordError() throws Exception {
        mThssdkPasswordError.setSdkPasswordError(sdkPasswordErrorMock);
        final SDKPasswordError sdkPasswordError = mThssdkPasswordError.getSdkPasswordError();
        assertNotNull(sdkPasswordError);
        assertThat(sdkPasswordError).isInstanceOf(SDKPasswordError.class);
    }

    @Test
    public void getPasswordErrors() throws Exception {
        List anyList = new ArrayList();
        anyList.add(sdkPasswordErrorMock);
        mThssdkPasswordError.setSdkPasswordError(sdkPasswordErrorMock);
        when(sdkPasswordErrorMock.getPasswordErrors()).thenReturn(anyList);
        final List<String> passwordErrors = mThssdkPasswordError.getPasswordErrors();
        assertNotNull(passwordErrors);
    }

}