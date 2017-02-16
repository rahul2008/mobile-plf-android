/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.util;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.util.GsonProvider;
import com.philips.commlib.core.communication.CommunicationStrategy;
import com.philips.commlib.core.port.firmware.FirmwarePort;
import com.philips.commlib.core.port.firmware.FirmwarePortProperties;
import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static com.philips.cdp.dicommclient.util.DICommLog.disableLogging;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortKey.PROGRESS;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortKey.STATE;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.CHECKING;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FirmwareUploaderTest {

    @Mock
    private CommunicationStrategy mockCommunicationStrategy;
    @Mock
    private CountDownLatch mockCountDownLatch;
    @Mock
    private FirmwarePort mockFirmwarePort;
    @Mock
    private FirmwareUpdatePushLocal mockOperation;
    @Mock
    private FirmwarePortProperties mockPortProperties;

    private byte[] firmwaredata = {0xF, 0xE, 0xD, 0xE, 0xF};
    private final Set<ResponseHandler> handlers = new HashSet<>();

    private FirmwareUploader uploaderUnderTest;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        initMocks(this);
        disableLogging();

        when(mockFirmwarePort.getPortProperties()).thenReturn(mockPortProperties);
        when(mockPortProperties.getMaxChunkSize()).thenReturn(1);

        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                handlers.clear();
                handlers.add(invocation.getArgumentAt(3, ResponseHandler.class));
                return null;
            }
        }).when(mockCommunicationStrategy).putProperties(any(Map.class), anyString(), anyInt(), any(ResponseHandler.class));

        uploaderUnderTest = new FirmwareUploader(mockFirmwarePort, mockCommunicationStrategy, mockOperation, firmwaredata) {
            @NonNull
            @Override
            CountDownLatch createCountDownLatch() {
                return mockCountDownLatch;
            }
        };
    }

    @Test
    public void onUpload_progressRunsThrough() throws IOException {
        uploaderUnderTest.upload();
        for (int progress = 1; progress < firmwaredata.length; progress++) {
            getNextHandler().onSuccess(createPutPropsReply(progress));
        }

        InOrder inOrder = inOrder(mockOperation);
        inOrder.verify(mockOperation).onDownloadProgress(0);
        inOrder.verify(mockOperation).onDownloadProgress(20);
        inOrder.verify(mockOperation).onDownloadProgress(40);
        inOrder.verify(mockOperation).onDownloadProgress(60);
        inOrder.verify(mockOperation).onDownloadProgress(80);
    }

    private ResponseHandler getNextHandler() {
        return handlers.iterator().next();
    }

    private String createPutPropsReply(final int progress) {
        final HashMap<String, Object> putReplyMap = new HashMap<>();
        putReplyMap.put(PROGRESS.toString(), progress);
        if (progress == firmwaredata.length) {
            putReplyMap.put(STATE.toString(), CHECKING.toString());
        }
        return GsonProvider.get().toJson(putReplyMap);
    }
}