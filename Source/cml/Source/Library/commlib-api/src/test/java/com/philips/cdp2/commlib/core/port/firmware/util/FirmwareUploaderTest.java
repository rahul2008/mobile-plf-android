/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware.util;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePort;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties;
import com.philips.cdp2.commlib.core.port.firmware.FirmwareUpdate;
import com.philips.cdp2.commlib.core.util.GsonProvider;

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
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.philips.cdp.dicommclient.util.DICommLog.disableLogging;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortKey.PROGRESS;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortKey.STATE;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.CHECKING;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FirmwareUploaderTest {

    @Mock
    private ExecutorService executorMock;

    @Mock
    private CommunicationStrategy mockCommunicationStrategy;

    @Mock
    private CountDownLatch mockCountDownLatch;

    @Mock
    private FirmwarePort mockFirmwarePort;

    @Mock
    private FirmwareUpdate mockFirmwareUpdate;

    @Mock
    private FirmwarePortProperties mockPortProperties;

    @Mock
    private FirmwareUploader.UploadListener mockUploadListener;

    private byte[] firmwaredata = {0xF, 0xE, 0xD, 0xE, 0xF};
    private final Set<ResponseHandler> handlers = new HashSet<>();

    private FirmwareUploader uploaderUnderTest;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        initMocks(this);
        disableLogging();

        doAnswer(new Answer<Future<Void>>() {
            @Override
            public Future<Void> answer(InvocationOnMock invocation) throws Throwable {
                Callable<Void> callable = invocation.getArgument(0);
                callable.call();

                return mock(Future.class);
            }
        }).when(executorMock).submit(any(Callable.class));

        when(mockFirmwarePort.getPortProperties()).thenReturn(mockPortProperties);
        when(mockPortProperties.getMaxChunkSize()).thenReturn(1);

        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                handlers.clear();
                handlers.add((ResponseHandler) invocation.getArgument(3));
                return null;
            }
        }).when(mockCommunicationStrategy).putProperties(any(Map.class), (String) any(), anyInt(), any(ResponseHandler.class));

        uploaderUnderTest = new FirmwareUploader(mockFirmwarePort, mockCommunicationStrategy, firmwaredata, mockUploadListener) {
            @NonNull
            @Override
            ExecutorService createExecutor() {
                return executorMock;
            }
        };
    }

    @Test
    public void onUpload_progressRunsThrough() throws IOException {
        uploaderUnderTest.start();
        for (int progress = 1; progress < firmwaredata.length; progress++) {
            getNextHandler().onSuccess(createPutPropsReply(progress));
        }

        InOrder inOrder = inOrder(mockUploadListener);
        inOrder.verify(mockUploadListener).onProgress(0);
        inOrder.verify(mockUploadListener).onProgress(20);
        inOrder.verify(mockUploadListener).onProgress(40);
        inOrder.verify(mockUploadListener).onProgress(60);
        inOrder.verify(mockUploadListener).onProgress(80);
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
        return GsonProvider.get().toJson(putReplyMap, Map.class);
    }
}
