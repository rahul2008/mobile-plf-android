package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.helper.MockedHandler;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNDeviceWrapperTest {

    private static final SHNResult RESULT = SHNResult.SHNErrorConnectionLost;

    private static final UUID SERVICE_UUID = UUID.randomUUID();

    private static final byte[] CHARACTERISTIC_DATA = new byte[] { 0x42 };

    @Mock private SHNDeviceImpl shnDeviceMock;

    @Mock private SHNDevice.SHNDeviceListener shnDeviceListenerMock;

    @Mock private SHNDevice.DiscoveryListener mockDiscoveryListener;

    @Mock private SHNCharacteristic mockCharacteristic;

    @Mock private SHNService mockService;

    @Captor protected ArgumentCaptor<Runnable> runnableCaptor;

    @Captor protected ArgumentCaptor<SHNDevice.SHNDeviceListener> shnDeviceListenerArgumentCaptor;

    @Captor protected ArgumentCaptor<SHNDevice.DiscoveryListener> discoveryListenerArgumentCaptor;

    private SHNDeviceWrapper shnDeviceWrapper;

    private Handler internalHandlerMock;
    private Handler userHandlerMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        MockedHandler handler = new MockedHandler();
        handler.enableImmediateExecuteOnPost(false);
        internalHandlerMock = handler.getMock();

        handler = new MockedHandler();
        handler.enableImmediateExecuteOnPost(false);
        userHandlerMock = handler.getMock();

        SHNDeviceWrapper.setHandlers(internalHandlerMock, userHandlerMock);
        shnDeviceWrapper = new SHNDeviceWrapper(shnDeviceMock);
    }

    @Test
    public void whenCreatedThenDeviceListenerIsAttached() throws Exception {
        verify(shnDeviceMock).registerSHNDeviceListener(shnDeviceListenerArgumentCaptor.capture());
    }

    @Test
    public void whenCreatedThenDisoveryListenerIsAttached() throws Exception {
        verify(shnDeviceMock).registerDiscoveryListener(discoveryListenerArgumentCaptor.capture());
    }

    @Test
    public void whenGetStateIsCalledThenCalledIsPassedToSHNDevice() throws Exception {
        shnDeviceWrapper.getState();

        verify(shnDeviceMock).getState();
    }

    @Test
    public void whenGetAddressIsCalledThenCalledIsPassedToSHNDevice() throws Exception {
        shnDeviceWrapper.getAddress();

        verify(shnDeviceMock).getAddress();
    }

    @Test
    public void whenGetNameIsCalledThenCalledIsPassedToSHNDevice() throws Exception {
        shnDeviceWrapper.getName();

        verify(shnDeviceMock).getName();
    }

    @Test
    public void whenGetDeviceTypeNameIsCalledThenCalledIsPassedToSHNDevice() throws Exception {
        shnDeviceWrapper.getDeviceTypeName();

        verify(shnDeviceMock).getDeviceTypeName();
    }

    @Test
    public void whenGetSupportedCapabilityTypesIsCalledThenCalledIsPassedToSHNDevice() throws Exception {
        shnDeviceWrapper.getSupportedCapabilityTypes();

        verify(shnDeviceMock).getSupportedCapabilityTypes();
    }

    @Test
    public void whenGetCapabilityForTypeIsCalledThenCalledIsPassedToSHNDevice() throws Exception {
        shnDeviceWrapper.getCapabilityForType(SHNCapabilityType.BATTERY);

        verify(shnDeviceMock).getCapabilityForType(SHNCapabilityType.BATTERY);
    }

    @Test
    public void whenConnectIsCalledThenCallIsPostedOnInternalThread() throws Exception {
        shnDeviceWrapper.connect();

        verify(internalHandlerMock).post(runnableCaptor.capture());
    }

    @Test
    public void whenConnectIsCalledThenCallIsPassedToSHNDevice() throws Exception {
        whenConnectIsCalledThenCallIsPostedOnInternalThread();

        runnableCaptor.getValue().run();

        verify(shnDeviceMock).connect();
    }

    @Test
    public void whenConnectWithParametersIsCalledThenCallIsPostedOnInternalThread() throws Exception {
        shnDeviceWrapper.connect(true, 3000);

        verify(internalHandlerMock).post(runnableCaptor.capture());
    }

    @Test
    public void whenConnectWithParametersIsCalledThenCallIsPassedToSHNDevice() throws Exception {
        whenConnectWithParametersIsCalledThenCallIsPostedOnInternalThread();

        runnableCaptor.getValue().run();

        verify(shnDeviceMock).connect(true, 3000);
    }

    @Test
    public void whenDisconnectIsCalledThenCallIsPostedOnInternalThread() throws Exception {
        shnDeviceWrapper.disconnect();

        verify(internalHandlerMock).post(runnableCaptor.capture());
    }

    @Test
    public void whenDisconnectIsCalledThenCallIsPassedToSHNDevice() throws Exception {
        whenDisconnectIsCalledThenCallIsPostedOnInternalThread();

        runnableCaptor.getValue().run();

        verify(shnDeviceMock).disconnect();
    }

    @Test
    public void whenStateIsReceivedThenCallIsPostedOnUserThread() throws Exception {
        whenCreatedThenDeviceListenerIsAttached();

        shnDeviceWrapper.registerSHNDeviceListener(shnDeviceListenerMock);
        shnDeviceListenerArgumentCaptor.getValue().onStateUpdated(shnDeviceMock);

        verify(userHandlerMock).post(runnableCaptor.capture());
        verify(shnDeviceListenerMock, never()).onStateUpdated(any(SHNDevice.class));
    }

    @Test
    public void whenThereAreMultipleListenersThenCallIsPostedOnUserThreadForEachListener() throws Exception {
        whenCreatedThenDeviceListenerIsAttached();

        shnDeviceWrapper.registerSHNDeviceListener(shnDeviceListenerMock);
        SHNDevice.SHNDeviceListener shnDeviceListenerMock2 = mock(SHNDevice.SHNDeviceListener.class);
        SHNDevice.SHNDeviceListener shnDeviceListenerMock3 = mock(SHNDevice.SHNDeviceListener.class);

        shnDeviceWrapper.registerSHNDeviceListener(shnDeviceListenerMock);
        shnDeviceWrapper.registerSHNDeviceListener(shnDeviceListenerMock2);
        shnDeviceWrapper.registerSHNDeviceListener(shnDeviceListenerMock3);

        shnDeviceListenerArgumentCaptor.getValue().onStateUpdated(shnDeviceMock);

        verify(userHandlerMock, times(3)).post(runnableCaptor.capture());
    }

    @Test
    public void whenStateIsReceivedThenUserListenerIsNotified() throws Exception {
        whenStateIsReceivedThenCallIsPostedOnUserThread();

        runnableCaptor.getValue().run();

        verify(shnDeviceListenerMock).onStateUpdated(shnDeviceWrapper);
    }

    @Test
    public void whenFailToConnectIsReceivedThenCallIsPostedOnUserThread() throws Exception {
        whenCreatedThenDeviceListenerIsAttached();

        shnDeviceWrapper.registerSHNDeviceListener(shnDeviceListenerMock);
        shnDeviceListenerArgumentCaptor.getValue().onFailedToConnect(shnDeviceMock, RESULT);

        verify(userHandlerMock).post(runnableCaptor.capture());
    }

    @Test
    public void whenThereAreMultipleListenersThenFailedToConnectCallIsPostedOnUserThreadForEachListener()
            throws Exception {
        whenCreatedThenDeviceListenerIsAttached();

        shnDeviceWrapper.registerSHNDeviceListener(shnDeviceListenerMock);
        SHNDevice.SHNDeviceListener shnDeviceListenerMock2 = mock(SHNDevice.SHNDeviceListener.class);

        shnDeviceWrapper.registerSHNDeviceListener(shnDeviceListenerMock);
        shnDeviceWrapper.registerSHNDeviceListener(shnDeviceListenerMock2);

        shnDeviceListenerArgumentCaptor.getValue().onFailedToConnect(shnDeviceMock, RESULT);

        verify(userHandlerMock, times(2)).post(runnableCaptor.capture());
    }

    @Test
    public void whenFailedToConnectIsReceivedThenUserListenerIsNotified() throws Exception {
        whenFailToConnectIsReceivedThenCallIsPostedOnUserThread();

        runnableCaptor.getValue().run();

        verify(shnDeviceListenerMock).onFailedToConnect(shnDeviceWrapper, RESULT);
    }

    @Test
    public void whenSameListenerIsRegisteredMultipleTimesThenNotificationIsReceivedOnce() throws Exception {
        whenCreatedThenDeviceListenerIsAttached();

        shnDeviceWrapper.registerSHNDeviceListener(shnDeviceListenerMock);
        shnDeviceWrapper.registerSHNDeviceListener(shnDeviceListenerMock);
        shnDeviceWrapper.registerSHNDeviceListener(shnDeviceListenerMock);

        shnDeviceListenerArgumentCaptor.getValue().onFailedToConnect(shnDeviceMock, RESULT);

        verify(userHandlerMock).post(runnableCaptor.capture());
    }

    @Test
    public void whenListenerIsUnregisteredThenNotificationIsNotReceived() throws Exception {
        whenCreatedThenDeviceListenerIsAttached();

        shnDeviceWrapper.registerSHNDeviceListener(shnDeviceListenerMock);
        shnDeviceWrapper.unregisterSHNDeviceListener(shnDeviceListenerMock);

        shnDeviceListenerArgumentCaptor.getValue().onFailedToConnect(shnDeviceMock, RESULT);

        verify(userHandlerMock, never()).post(runnableCaptor.capture());
    }

    @Test
    public void whenListenerIsRemovedThenRemainingListenersAreNotified() throws Exception {
        whenCreatedThenDeviceListenerIsAttached();

        SHNDevice.SHNDeviceListener shnDeviceListenerMock2 = mock(SHNDevice.SHNDeviceListener.class);
        shnDeviceWrapper.registerSHNDeviceListener(shnDeviceListenerMock);
        shnDeviceWrapper.registerSHNDeviceListener(shnDeviceListenerMock2);
        shnDeviceWrapper.unregisterSHNDeviceListener(shnDeviceListenerMock);

        shnDeviceListenerArgumentCaptor.getValue().onFailedToConnect(shnDeviceMock, RESULT);

        verify(userHandlerMock, times(1)).post(runnableCaptor.capture());
    }

    // DiscoveryListener tests
    @Test
    public void whenServiceIsDiscoveredThenCallIsPostedOnUserThread() throws Exception {
        whenCreatedThenDisoveryListenerIsAttached();

        shnDeviceWrapper.registerDiscoveryListener(mockDiscoveryListener);
        discoveryListenerArgumentCaptor.getValue().onServiceDiscovered(SERVICE_UUID, mockService);

        verify(userHandlerMock).post(runnableCaptor.capture());
    }

    @Test
    public void whenCharacteristicIsDiscoveredThenCallIsPostedOnUserThread() throws Exception {
        whenCreatedThenDisoveryListenerIsAttached();

        shnDeviceWrapper.registerDiscoveryListener(mockDiscoveryListener);
        discoveryListenerArgumentCaptor.getValue()
                .onCharacteristicDiscovered(SERVICE_UUID, CHARACTERISTIC_DATA, mockCharacteristic);

        verify(userHandlerMock).post(runnableCaptor.capture());
    }

    @Test
    public void whenDiscoveryListenerIsUnregisteredThenCharacteristicIsNotReceived() throws Exception {
        whenCreatedThenDisoveryListenerIsAttached();

        shnDeviceWrapper.registerDiscoveryListener(mockDiscoveryListener);
        shnDeviceWrapper.unregisterDiscoveryListener(mockDiscoveryListener);

        discoveryListenerArgumentCaptor.getValue()
                .onCharacteristicDiscovered(SERVICE_UUID, CHARACTERISTIC_DATA, mockCharacteristic);

        verify(userHandlerMock, never()).post(runnableCaptor.capture());
    }

    @Test
    public void whenDiscoveryListenerIsUnregisteredThenServiceIsNotReceived() throws Exception {
        whenCreatedThenDisoveryListenerIsAttached();

        shnDeviceWrapper.registerDiscoveryListener(mockDiscoveryListener);
        shnDeviceWrapper.unregisterDiscoveryListener(mockDiscoveryListener);

        discoveryListenerArgumentCaptor.getValue().onServiceDiscovered(SERVICE_UUID, mockService);

        verify(userHandlerMock, never()).post(runnableCaptor.capture());
    }

    @Test
    public void whenDiscoveryListenerIsUnregisteredThenNotificationIsNotReceived() throws Exception {
        whenCreatedThenDisoveryListenerIsAttached();

        shnDeviceWrapper.registerDiscoveryListener(mockDiscoveryListener);
        shnDeviceWrapper.unregisterDiscoveryListener(mockDiscoveryListener);

        discoveryListenerArgumentCaptor.getValue().onServiceDiscovered(SERVICE_UUID, mockService);

        verify(userHandlerMock, never()).post(runnableCaptor.capture());
    }

    @Test
    public void whenDiscoveryListenerIsRemovedThenRemainingListenersAreNotified() throws Exception {
        whenCreatedThenDisoveryListenerIsAttached();

        SHNDevice.DiscoveryListener mockDiscoveryListener2 = mock(SHNDevice.DiscoveryListener.class);
        shnDeviceWrapper.registerDiscoveryListener(mockDiscoveryListener);
        shnDeviceWrapper.registerDiscoveryListener(mockDiscoveryListener2);
        shnDeviceWrapper.unregisterDiscoveryListener(mockDiscoveryListener);

        discoveryListenerArgumentCaptor.getValue().onServiceDiscovered(SERVICE_UUID, mockService);

        verify(userHandlerMock, times(1)).post(runnableCaptor.capture());
    }

    @Test
    public void whenReadRssiIsCalledInternalHandlerIsPosted() throws Exception {
        whenCreatedThenDeviceListenerIsAttached();
        whenCreatedThenDisoveryListenerIsAttached();
        shnDeviceWrapper.readRSSI();
        verify(internalHandlerMock).post(runnableCaptor.capture());
        runnableCaptor.getValue().run();
        verify(shnDeviceMock).readRSSI();
    }
}