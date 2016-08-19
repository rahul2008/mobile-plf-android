package com.philips.pins.shinelib.wrappers;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.CapabilityBluetoothDirect;
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

public class CapabilityBluetoothDirectWrapperTest extends SHNCapabilityWrapperTestBase {

    public static final SHNResult EXPECTED_RESULT = SHNResult.SHNOk;
    public static final byte[] READ_RAW = new byte[] { 0x42 };
    public static final SHNDataRaw READ_VALUE = new SHNDataRaw(READ_RAW);

    @Mock private CapabilityBluetoothDirect capabilityMock;

    @Mock private ResultListener<SHNDataRaw> rawResultListener;
    @Mock private SHNResultListener resultListener;

    @Mock private CapabilityBluetoothDirect.CharacteristicChangedListener shnCharacteristicChangedListenerMock;

    @Captor ArgumentCaptor<ResultListener<SHNDataRaw>> rawResultListenerArgumentCaptor;

    @Captor ArgumentCaptor<SHNResultListener> resultListenerArgumentCaptor;

    @Captor ArgumentCaptor<Boolean> booleanArgumentCaptor;

    @Captor ArgumentCaptor<CapabilityBluetoothDirect.CharacteristicChangedListener> shnCapabilityGenericListenerArgumentCaptor;

    private CapabilityBluetoothDirectWrapper capabilityWrapper;

    @Before
    public void setUp() {
        initMocks(this);
        capabilityWrapper = new CapabilityBluetoothDirectWrapper(capabilityMock, internalHandlerMock, userHandlerMock);
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

        ResultListener<SHNDataRaw> internalResultListener = rawResultListenerArgumentCaptor.getValue();

        internalResultListener.onActionCompleted(READ_VALUE, EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(rawResultListener).onActionCompleted(READ_VALUE, EXPECTED_RESULT);
    }

    @Test
    public void shouldReceiveCallWriteCharacteristicOnInternalThread_whenWriteCharacteristicIsCalledOnWrapper() {
        final UUID uuid = UUID.randomUUID();
        capabilityWrapper.writeCharacteristic(resultListener, uuid, READ_RAW);
        captureInternalHandlerRunnable().run();

        verify(capabilityMock).writeCharacteristic(resultListenerArgumentCaptor.capture(), eq(uuid), eq(READ_RAW));
    }

    @Test
    public void shouldReceiveCorrectWriteResultOnUserThread_whenResultReturnOnInternalThread() {
        shouldReceiveCallWriteCharacteristicOnInternalThread_whenWriteCharacteristicIsCalledOnWrapper();

        SHNResultListener internalResultListener = resultListenerArgumentCaptor.getValue();

        internalResultListener.onActionCompleted(EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(resultListener).onActionCompleted(EXPECTED_RESULT);
    }

    @Test
    public void shouldReceiveCallSetNotifyOnInternalThread_whenSetNotifyIsCalledOnWrapper() {
        final UUID uuid = UUID.randomUUID();
        capabilityWrapper.setNotifyOnCharacteristicChange(resultListener, uuid, true);
        captureInternalHandlerRunnable().run();

        verify(capabilityMock).setNotifyOnCharacteristicChange(resultListenerArgumentCaptor.capture(), eq(uuid), eq(true));
    }

    @Test
    public void shouldReceiveCorrectNotifyResultOnUserThread_whenResultReturnOnInternalThread() {
        shouldReceiveCallSetNotifyOnInternalThread_whenSetNotifyIsCalledOnWrapper();

        SHNResultListener internalResultListener = resultListenerArgumentCaptor.getValue();

        internalResultListener.onActionCompleted(EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(resultListener).onActionCompleted(EXPECTED_RESULT);
    }

    @Test
    public void shouldPostCharacteristicChangedCallbacksOnUserThread() {
        verify(capabilityMock).setCharacteristicChangedListener(shnCapabilityGenericListenerArgumentCaptor.capture());

        capabilityWrapper.setCharacteristicChangedListener(shnCharacteristicChangedListenerMock);
        shnCapabilityGenericListenerArgumentCaptor.getValue().onCharacteristicChanged(UUID.randomUUID(), READ_RAW, 1);
        captureUserHandlerRunnable().run();

        verify(shnCharacteristicChangedListenerMock).onCharacteristicChanged(any(UUID.class), any(byte[].class), anyInt());
    }
}
