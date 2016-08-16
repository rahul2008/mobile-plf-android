package com.philips.pins.shinelib.wrappers;

import com.philips.pins.shinelib.SHNDataRawResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.CapabilityGeneric;
import com.philips.pins.shinelib.datatypes.SHNDataRaw;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class CapabilityGenericWrapperTest extends SHNCapabilityWrapperTestBase {

    public static final SHNResult EXPECTED_RESULT = SHNResult.SHNOk;
    public static final byte[] READ_RAW = new byte[] { 0x42 };
    public static final SHNDataRaw READ_VALUE = new SHNDataRaw(READ_RAW);

    @Mock private CapabilityGeneric capabilityMock;

    @Mock private SHNDataRawResultListener rawResultListener;

    @Mock private SHNResultListener resultListener;

    @Mock private CapabilityGeneric.CapabilityGenericListener shnCapabilityGenericListenerMock;

    @Captor ArgumentCaptor<SHNDataRawResultListener> rawResultListenerArgumentCaptor;

    @Captor ArgumentCaptor<SHNResultListener> resultListenerArgumentCaptor;

    @Captor ArgumentCaptor<Boolean> booleanArgumentCaptor;

    @Captor ArgumentCaptor<CapabilityGeneric.CapabilityGenericListener> shnCapabilityGenericListenerArgumentCaptor;

    private CapabilityGenericWrapper capabilityWrapper;

    @Before
    public void setUp() {
        initMocks(this);
        capabilityWrapper = new CapabilityGenericWrapper(capabilityMock, internalHandlerMock, userHandlerMock);
    }

    @Test
    public void shouldReceiveCallReadCharacteristicOnInternalThread_whenReadCharacteristicIsCalledOnWrapper() {
        final UUID uuid = UUID.randomUUID();
        capabilityWrapper.readCharacteristic(rawResultListener, uuid);
        captureInternalHandlerRunnable().run();

        verify(capabilityMock).readCharacteristic(rawResultListenerArgumentCaptor.capture(), eq(uuid));
    }

    @Test
    public void shouldReceiveCorrectResultOnUserThread_whenResultReturnOnInternalThread() {
        shouldReceiveCallReadCharacteristicOnInternalThread_whenReadCharacteristicIsCalledOnWrapper();

        SHNDataRawResultListener internalResultListener = rawResultListenerArgumentCaptor.getValue();

        internalResultListener.onActionCompleted(READ_VALUE, EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(rawResultListener).onActionCompleted(READ_VALUE, EXPECTED_RESULT);
    }

    @Test
    public void shouldPostReadCompletedCallbacksOnUserThread() {
        verify(capabilityMock).setCapabilityGenericListener(shnCapabilityGenericListenerArgumentCaptor.capture());

        capabilityWrapper.setCapabilityGenericListener(shnCapabilityGenericListenerMock);
        shnCapabilityGenericListenerArgumentCaptor.getValue().onReadCompleted(UUID.randomUUID(), READ_RAW, 1);
        captureUserHandlerRunnable().run();

        verify(shnCapabilityGenericListenerMock).onReadCompleted(any(UUID.class), any(byte[].class), anyInt());
    }

    @Test
    public void shouldReceiveCallWriteCharacteristicOnInternalThread_whenWriteCharacteristicIsCalledOnWrapper() {
        final UUID uuid = UUID.randomUUID();
        capabilityWrapper.writeCharacteristic(rawResultListener, uuid, READ_RAW);
        captureInternalHandlerRunnable().run();

        verify(capabilityMock).writeCharacteristic(rawResultListenerArgumentCaptor.capture(), eq(uuid), eq(READ_RAW));
    }

    @Test
    public void shouldReceiveCorrectWriteResultOnUserThread_whenResultReturnOnInternalThread() {
        shouldReceiveCallWriteCharacteristicOnInternalThread_whenWriteCharacteristicIsCalledOnWrapper();

        SHNDataRawResultListener internalResultListener = rawResultListenerArgumentCaptor.getValue();

        internalResultListener.onActionCompleted(READ_VALUE, EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(rawResultListener).onActionCompleted(READ_VALUE, EXPECTED_RESULT);
    }

    @Test
    public void shouldPostWriteCompletedCallbacksOnUserThread() {
        verify(capabilityMock).setCapabilityGenericListener(shnCapabilityGenericListenerArgumentCaptor.capture());

        capabilityWrapper.setCapabilityGenericListener(shnCapabilityGenericListenerMock);
        shnCapabilityGenericListenerArgumentCaptor.getValue().onWriteCompleted(UUID.randomUUID(), 1);
        captureUserHandlerRunnable().run();

        verify(shnCapabilityGenericListenerMock).onWriteCompleted(any(UUID.class), anyInt());
    }

    @Test
    public void shouldPostCharacteristicChangedCallbacksOnUserThread() {
        verify(capabilityMock).setCapabilityGenericListener(shnCapabilityGenericListenerArgumentCaptor.capture());

        capabilityWrapper.setCapabilityGenericListener(shnCapabilityGenericListenerMock);
        shnCapabilityGenericListenerArgumentCaptor.getValue().onCharacteristicChanged(UUID.randomUUID(), READ_RAW, 1);
        captureUserHandlerRunnable().run();

        verify(shnCapabilityGenericListenerMock).onCharacteristicChanged(any(UUID.class), any(byte[].class), anyInt());
    }
}
