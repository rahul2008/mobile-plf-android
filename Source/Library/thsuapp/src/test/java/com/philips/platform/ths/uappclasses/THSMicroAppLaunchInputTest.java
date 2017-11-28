/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.uappclasses;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

public class THSMicroAppLaunchInputTest {

    THSMicroAppLaunchInput mThsMicroAppLaunchInput;

    @Mock
    THSCompletionProtocol thsCompletionProtocolMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mThsMicroAppLaunchInput = new THSMicroAppLaunchInput("Welcome", thsCompletionProtocolMock);
    }

    @Test
    public void getThsCompletionProtocol() throws Exception {
        final THSCompletionProtocol thsCompletionProtocol = mThsMicroAppLaunchInput.getThsCompletionProtocol();
        assertNotNull(thsCompletionProtocol);
        assertThat(thsCompletionProtocol).isInstanceOf(THSCompletionProtocol.class);
    }

}