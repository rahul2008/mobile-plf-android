/* Copyright (c) Koninklijke Philips N.V. 2018
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.tagging;

import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.logging.LoggingInterface;

import junit.framework.TestCase;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.SEND_DATA;
import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.SUCCESS_MESSAGE;
import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.TECHNICAL_ERROR;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AppInfraTaggingUtilTest extends TestCase {

    private AppInfraTaggingUtil appInfraTaggingUtil;
    private AppTaggingInterface appTaggingInterface;
    private LoggingInterface loggingInterface;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        appTaggingInterface = mock(AppTaggingInterface.class);
        loggingInterface = mock(LoggingInterface.class);
        appInfraTaggingUtil = new AppInfraTaggingUtil(appTaggingInterface, loggingInterface);
    }

    public void testSuccessAction() throws Exception {
        String message = " some message";
        appInfraTaggingUtil.trackSuccessAction(AppInfraTaggingUtil.SERVICE_DISCOVERY, message);
        verify(appTaggingInterface).trackActionWithInfo(SEND_DATA, SUCCESS_MESSAGE,AppInfraTaggingUtil.SERVICE_DISCOVERY.concat(":").concat(message));
        verify(loggingInterface).log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SERVICE_DISCOVERY, AppInfraTaggingUtil.SERVICE_DISCOVERY.concat(":").concat(message));
    }

    public void testErrorAction() throws Exception {
        String message = " some message";
        appInfraTaggingUtil.trackErrorAction(AppInfraTaggingUtil.SERVICE_DISCOVERY, message);
        verify(appTaggingInterface).trackActionWithInfo(SEND_DATA, TECHNICAL_ERROR,"AIL:".concat(AppInfraTaggingUtil.SERVICE_DISCOVERY).concat(":").concat(message));
        verify(loggingInterface).log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SERVICE_DISCOVERY, "AIL:".concat(AppInfraTaggingUtil.SERVICE_DISCOVERY).concat(":").concat(message));
    }

}