/* Copyright (c) Koninklijke Philips N.V. 2018
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.tagging;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static com.philips.platform.appinfra.tagging.AppTaggingConstants.SUCCESS_MESSAGE;
import static com.philips.platform.appinfra.tagging.AppTaggingConstants.TECHNICAL_ERROR;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AppInfraTaggingUtilTest extends TestCase {

    private AppInfraTaggingUtil appInfraTaggingUtil;
    private AppTaggingInterface appTaggingInterface;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        appTaggingInterface = mock(AppTaggingInterface.class);
        appInfraTaggingUtil = new AppInfraTaggingUtil(appTaggingInterface);
    }

    @Test
    public void trackSuccessAction() throws Exception {
        String message = " some message";
        appInfraTaggingUtil.trackSuccessAction(AppInfraTaggingUtil.SERVICE_DISCOVERY, message);
        verify(appTaggingInterface).trackActionWithInfo(AppTaggingConstants.SEND_DATA, SUCCESS_MESSAGE,AppInfraTaggingUtil.SERVICE_DISCOVERY.concat(":").concat(message));
    }

    @Test
    public void trackErrorAction() throws Exception {
        String message = " some message";
        appInfraTaggingUtil.trackErrorAction(AppInfraTaggingUtil.SERVICE_DISCOVERY, message);
        verify(appTaggingInterface).trackActionWithInfo(AppTaggingConstants.SEND_DATA, TECHNICAL_ERROR,"AIL:".concat(AppInfraTaggingUtil.SERVICE_DISCOVERY).concat(":").concat(message));
    }

}