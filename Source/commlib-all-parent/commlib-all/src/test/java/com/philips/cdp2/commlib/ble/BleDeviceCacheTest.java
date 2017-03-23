package com.philips.cdp2.commlib.ble;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.pins.shinelib.SHNDevice;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BleDeviceCacheTest {
    private BleDeviceCache bleDeviceCache;

    private static final long DEVICE_EXPIRATION_MILLIS = 1337;

    @Mock
    SHNDevice deviceMock;

    @Mock
    NetworkNode networkNodeMock;

    @Mock
    ScheduledExecutorService scheduledExecutorServiceMock;

    @Mock
    ScheduledFuture scheduledFutureMock;

    @Mock
    BleDeviceCache.ExpirationCallback expirationCallbackMock;

    Callable<Void> scheduledCallable;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        bleDeviceCache = new BleDeviceCache(scheduledExecutorServiceMock);

        when(networkNodeMock.getCppId()).thenReturn("my-cpp-id");
        when(scheduledExecutorServiceMock.schedule(isA(Callable.class), anyLong(), isA(TimeUnit.class))).thenAnswer(new Answer<ScheduledFuture>() {
            @Override
            public ScheduledFuture answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                scheduledCallable = (Callable<Void>) args[0];
                long expirationPeriodMillis = (long) args[1];
                TimeUnit timeUnit = (TimeUnit) args[2];

                assertEquals(DEVICE_EXPIRATION_MILLIS, expirationPeriodMillis);
                assertEquals(TimeUnit.MILLISECONDS, timeUnit);

                return scheduledFutureMock;
            }
        });
    }

    @Test
    public void whenAddingADevice_ThenCacheShouldContainData() throws Exception {
        bleDeviceCache.addDevice(deviceMock, networkNodeMock, null, DEVICE_EXPIRATION_MILLIS);

        assertTrue(bleDeviceCache.contains("my-cpp-id"));
    }

    @Test
    public void whenAddingADevice_AndDeviceIsExpired_ThenCallbackShouldBeInvoked() throws Exception {
        bleDeviceCache.addDevice(deviceMock, networkNodeMock, expirationCallbackMock, DEVICE_EXPIRATION_MILLIS);

        scheduledCallable.call();

        verify(expirationCallbackMock).onCacheExpired(isA(NetworkNode.class));
    }

    @Test
    public void whenAddingADeviceTwice_ThenTheTimerShouldReset() throws Exception {
        bleDeviceCache.addDevice(deviceMock, networkNodeMock, expirationCallbackMock, DEVICE_EXPIRATION_MILLIS);
        Callable firstScheduledCallable = scheduledCallable;
        bleDeviceCache.addDevice(deviceMock, networkNodeMock, expirationCallbackMock, DEVICE_EXPIRATION_MILLIS);
        Callable secondScheduledCallable = scheduledCallable;

        verify(scheduledFutureMock).cancel(true);
        assertNotSame(firstScheduledCallable, secondScheduledCallable);
    }
}