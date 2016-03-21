package com.philips.pins.shinelib.wrappers;

import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityBattery;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNCapabilityBatteryWrapperTest extends SHNCapabilityWrapperTestBase {

    public static final SHNResult EXPECTED_RESULT = SHNResult.SHNOk;
    public static final int BATTERY_LEVEL = 89;

    @Mock
    private SHNCapabilityBattery capabilityMock;

    @Mock
    private SHNIntegerResultListener integerResultListener;

    @Mock
    private SHNResultListener resultListener;

    @Mock
    private SHNCapabilityBattery.SHNCapabilityBatteryListener shnCapabilityBatteryListenerMock;

    @Captor
    ArgumentCaptor<SHNIntegerResultListener> integerResultListenerArgumentCaptor;

    @Captor
    ArgumentCaptor<SHNResultListener> resultListenerArgumentCaptor;

    @Captor
    ArgumentCaptor<Boolean> booleanArgumentCaptor;

    @Captor
    ArgumentCaptor<SHNCapabilityBattery.SHNCapabilityBatteryListener> shnCapabilityBatteryListenerArgumentCaptor;

    private SHNCapabilityBatteryWrapper capabilityWrapper;

    @Before
    public void setUp() {
        initMocks(this);

        capabilityWrapper = new SHNCapabilityBatteryWrapper(capabilityMock, internalHandlerMock, userHandlerMock);
    }

    @Test
    public void shouldReceiveCallToGetBatteryLevelOnInternalThread_whenGetBatteryIsCalledOnWrapper() {
        capabilityWrapper.getBatteryLevel(integerResultListener);

        captureInternalHandlerRunnable().run();

        verify(capabilityMock).getBatteryLevel(integerResultListenerArgumentCaptor.capture());
    }

    @Test
    public void shouldReceiveCorrectResultOnUserThread_whenResultReturnOnInternalThread() {
        shouldReceiveCallToGetBatteryLevelOnInternalThread_whenGetBatteryIsCalledOnWrapper();

        SHNIntegerResultListener internalResultListener = integerResultListenerArgumentCaptor.getValue();

        internalResultListener.onActionCompleted(BATTERY_LEVEL, EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(integerResultListener).onActionCompleted(BATTERY_LEVEL, EXPECTED_RESULT);
    }

    @Test
    public void shouldReceiveCallToSetNotificationOnInternalThread_whenSetNotificationIsCalledOnWrapper() {
        capabilityWrapper.setBatteryLevelNotifications(true, resultListener);

        captureInternalHandlerRunnable().run();

        verify(capabilityMock).setBatteryLevelNotifications(booleanArgumentCaptor.capture(), resultListenerArgumentCaptor.capture());
        assertEquals(true, booleanArgumentCaptor.getValue());
    }

    @Test
    public void shouldReceiveCorrectNotificationResultOnUserThread_whenResultReturnOnInternalThread() {
        shouldReceiveCallToSetNotificationOnInternalThread_whenSetNotificationIsCalledOnWrapper();

        SHNResultListener internalResultListener = resultListenerArgumentCaptor.getValue();

        internalResultListener.onActionCompleted(EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(resultListener).onActionCompleted(EXPECTED_RESULT);
    }

    @Test
    public void shouldPostListenerCallbacksOnUserThread() {
        verify(capabilityMock).setSetSHNCapabilityBatteryListener(shnCapabilityBatteryListenerArgumentCaptor.capture());

        capabilityWrapper.setSetSHNCapabilityBatteryListener(shnCapabilityBatteryListenerMock);
        shnCapabilityBatteryListenerArgumentCaptor.getValue().onBatteryLevelChanged(BATTERY_LEVEL);
        captureUserHandlerRunnable().run();

        verify(shnCapabilityBatteryListenerMock).onBatteryLevelChanged(BATTERY_LEVEL);
    }
}