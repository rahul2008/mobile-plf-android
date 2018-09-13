package com.philips.pins.shinelib.statemachine.state;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.bluetoothwrapper.BTDevice;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.statemachine.SHNDeviceResources;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;
import com.philips.pins.shinelib.tagging.SHNTagger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.security.InvalidParameterException;

import static com.philips.pins.shinelib.SHNCentral.State.SHNCentralStateNotReady;
import static com.philips.pins.shinelib.SHNCentral.State.SHNCentralStateReady;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SHNGattConnectingState.class, Timer.class, SHNTagger.class})
public class SHNGattConnectingStateTest {

    private SHNGattConnectingState gattConnectingState;

    @Mock
    private SHNDeviceStateMachine stateMachine;

    @Mock
    private Context mockedContext;

    @Mock
    private BTDevice mockedBTDevice;

    @Mock
    private SHNCentral mockedSHNCentral;

    @Mock
    private BTGatt.BTGattCallback mockedBtGattCallback;

    @Mock
    private SHNDeviceResources sharedResources;

    @Mock
    private SHNCentral.SHNCentralListener mockedSHNCentralListener;

    @Mock
    private Handler mockedSHNInternalHandler;

    @Mock
    private Timer timerMock;

    @Captor
    private ArgumentCaptor<Runnable> runnableArgumentCaptor;

    @Captor
    private ArgumentCaptor<Runnable> timerRunnableCaptor;

    @Captor
    private ArgumentCaptor<Long> timeoutTimeCaptor;

    private String deviceAddress = "Bogus!";
    
    @Before
    public void setUp() {
        initMocks(this);

        PowerMockito.mockStatic(Timer.class);
        mockStatic(SHNTagger.class);
        PowerMockito.when(Timer.createTimer(timerRunnableCaptor.capture(), timeoutTimeCaptor.capture())).thenReturn(timerMock);

        doReturn(mockedContext).when(mockedSHNCentral).getApplicationContext();
        doReturn(mockedSHNInternalHandler).when(mockedSHNCentral).getInternalHandler();

        doReturn(deviceAddress).when(mockedBTDevice).getAddress();

        doReturn(mockedBTDevice).when(sharedResources).getBtDevice();
        doReturn(mockedSHNCentral).when(sharedResources).getShnCentral();
        doReturn(mockedBtGattCallback).when(sharedResources).getBTGattCallback();
        doReturn(mockedSHNCentralListener).when(sharedResources).getShnCentralListener();

        doReturn(sharedResources).when(stateMachine).getSharedResources();

        gattConnectingState = new SHNGattConnectingState(stateMachine);
    }

    @Test(expected = InvalidParameterException.class)
    public void cannotCreateAGattConnectingStateWithANegativeTimeout() {
        new SHNGattConnectingState(stateMachine, -1);
    }

    @Test
    public void canCreateAGattConnectingStateWithAPositiveTimeout() {
        new SHNGattConnectingState(stateMachine, 3);
    }

    @Test
    public void givenBluetoothIsTurnedOff_whenOnEnterIsCalled_thenConnectGattIsNotCalled() {
        gattConnectingState.onEnter();

        verify(mockedBTDevice, times(0)).connectGatt(nullable(Context.class), anyBoolean(), nullable(SHNCentral.class), nullable(BTGatt.BTGattCallback.class), anyInt());
    }

    @Test
    public void givenBluetoothIsTurnedOff_whenOnEnterIsCalled_thenItSetsTheStateToDisconnecting() {
        gattConnectingState.onEnter();

        verify(stateMachine).setState(any(SHNDisconnectingState.class));
    }

    @Test
    public void givenBluetoothIsTurnedOff_whenOnEnterIsCalled_thenItNotifiesListenersAboutTheConnectionFailure() {
        gattConnectingState.onEnter();

        verify(sharedResources).notifyFailureToListener(SHNResult.SHNErrorBluetoothDisabled);
    }

    @Test
    public void givenBluetoothIsTurnedOn_whenOnEnterIsCalled_thenConnectGattIsCalled() {
        when(mockedSHNCentral.getShnCentralState()).thenReturn(SHNCentralStateReady);

        gattConnectingState.onEnter();

        verify(mockedBTDevice).connectGatt(same(mockedContext), eq(false), same(mockedSHNCentral), same(mockedBtGattCallback), eq(0));
    }

    @Test
    public void givenBluetoothIsTurnedOff_whenOnEnterIsCalled_thenTagIsSentWithProperData() {
        when(mockedSHNCentral.getShnCentralState()).thenReturn(SHNCentralStateNotReady);

        gattConnectingState.onEnter();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verifyStatic(SHNTagger.class, times(1));
        SHNTagger.sendTechnicalError(captor.capture());
        assertEquals("Not ready for connection to the peripheral, Bluetooth is not on.", captor.getValue());
    }

    @Test
    public void givenTheDeviceHasJustBeenDisconnected_whenOnEnterIsCalled_thenItWillPostponeTheConnectCall() {
        long justNow = System.currentTimeMillis();
        doReturn(justNow).when(sharedResources).getLastDisconnectedTimeMillis();

        gattConnectingState.onEnter();

        verify(mockedBTDevice, times(0)).connectGatt(nullable(Context.class), anyBoolean(), nullable(SHNCentral.class), nullable(BTGatt.BTGattCallback.class), anyInt());
        verify(mockedSHNInternalHandler).postDelayed(any(Runnable.class), anyLong());
    }

    @Test
    public void givenTheDeviceHasJustBeenDisconnected_andTheConnectCallHasBeenPostponed_whenThePostponedCallIsExectuted_thenGattConnectIsCalled() {
        long justNow = System.currentTimeMillis();
        long longAgo = justNow - 3000L;
        doReturn(justNow).when(sharedResources).getLastDisconnectedTimeMillis();
        gattConnectingState.onEnter();

        verify(mockedSHNInternalHandler).postDelayed(runnableArgumentCaptor.capture(), anyLong());
        when(mockedSHNCentral.getShnCentralState()).thenReturn(SHNCentralStateReady);
        doReturn(longAgo).when(sharedResources).getLastDisconnectedTimeMillis();
        runnableArgumentCaptor.getValue().run();

        verify(mockedBTDevice).connectGatt(same(mockedContext), anyBoolean(), same(mockedSHNCentral), same(mockedBtGattCallback), anyInt());
    }

    @Test
    public void givenNoBondInitiatorIsRequired_whenGattConnectSucceeds_thenItWillSwitchToTheDiscoveringServicesState() {
        doReturn(SHNDeviceImpl.SHNBondInitiator.NONE).when(sharedResources).getShnBondInitiator();

        gattConnectingState.onConnectionStateChange(null, BluetoothGatt.GATT_SUCCESS, BluetoothProfile.STATE_CONNECTED);

        verify(stateMachine).setState(any(SHNDiscoveringServicesState.class));
    }

    @Test
    public void whenGattConnectSucceedsWithAStatusThatIsNotSuccess_thenItWillSwitchToTheDisconnectingState_AndReportAFailure() {
        gattConnectingState.onConnectionStateChange(null, BluetoothGatt.GATT_READ_NOT_PERMITTED, BluetoothProfile.STATE_CONNECTED);

        verify(stateMachine).setState(any(SHNDisconnectingState.class));
        verify(sharedResources).notifyFailureToListener(SHNResult.SHNErrorConnectionLost);
    }

    @Test
    public void whenGattConnectSucceedsWithAStatusThatIsNotSuccess_thenATagIsSentWithProperData() {

        gattConnectingState.onConnectionStateChange(null, BluetoothGatt.GATT_READ_NOT_PERMITTED, BluetoothProfile.STATE_CONNECTED);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verifyStatic(SHNTagger.class, times(1));
        SHNTagger.sendTechnicalError(captor.capture());
        String result = String.format("Bluetooth GATT connect failure, status [%s]", BluetoothGatt.GATT_READ_NOT_PERMITTED);
        assertEquals(result, captor.getValue());
    }

    @Test
    public void givenABondInitiatorHasBeenSet_whenGattConnectSucceeds_thenItWillSwitchToTheWaitingUntilBondedState() {
        doReturn(SHNDeviceImpl.SHNBondInitiator.APP).when(sharedResources).getShnBondInitiator();

        gattConnectingState.onConnectionStateChange(null, BluetoothGatt.GATT_SUCCESS, BluetoothProfile.STATE_CONNECTED);

        verify(stateMachine).setState(any(SHNWaitingUntilBondedState.class));
    }

    @Test
    public void whenGattConnectFails_thenItWillGoToADisconnectingState_andReportAFailure() {
        gattConnectingState.onConnectionStateChange(null, BluetoothGatt.GATT_FAILURE, BluetoothProfile.STATE_DISCONNECTED);

        verify(stateMachine).setState(any(SHNDisconnectingState.class));
        verify(sharedResources).notifyFailureToListener(SHNResult.SHNErrorInvalidState);
    }

    @Test
    public void whenGattConnectFails_thenATagIsSentWithProperData() {

        gattConnectingState.onConnectionStateChange(null, BluetoothGatt.GATT_FAILURE, BluetoothProfile.STATE_DISCONNECTED);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verifyStatic(SHNTagger.class, times(1));
        SHNTagger.sendTechnicalError(captor.capture());
        assertEquals("Bluetooth GATT disconnected, not retrying to connect.", captor.getValue());
    }

    @Test
    public void givenAConnectingStateHasBeenCreatedWithAConnectTimeout_whenGattConnectFails_thenItWillTryToConnectAgain() {
        long connectTimeOut = 1000L;
        gattConnectingState = new SHNGattConnectingState(stateMachine, connectTimeOut);

        gattConnectingState.onConnectionStateChange(null, BluetoothGatt.GATT_FAILURE, BluetoothProfile.STATE_DISCONNECTED);

        verify(mockedBTDevice).connectGatt(same(mockedContext), anyBoolean(), same(mockedSHNCentral), same(mockedBtGattCallback), anyInt());
    }

    @Test
    public void givenAConnectingStateHasBeenCreatedWithAConnectTimeout_whenConnectTimerExpires_thenItWillGoToADisconnectingState_andReportAFailure() {
        long connectTimeOut = 1000L;
        gattConnectingState = new SHNGattConnectingState(stateMachine, connectTimeOut);

        timerRunnableCaptor.getValue().run();

        verify(stateMachine).setState(isA(SHNDisconnectingState.class));
        verify(sharedResources).notifyFailureToListener(SHNResult.SHNErrorTimeout);
    }

    @Test
    public void whenShineCentralBecomesNotReady_thenItWillGoToADisconnectingState_andReportAFailure() {
        when(mockedSHNCentral.getShnCentralState()).thenReturn(SHNCentralStateNotReady);

        gattConnectingState.onStateUpdated(mockedSHNCentral);

        verify(stateMachine).setState(any(SHNDisconnectingState.class));
        verify(sharedResources).notifyFailureToListener(SHNResult.SHNErrorInvalidState);
    }

    @Test
    public void givenAConnectingStateWithAConnectTimeout_whenConnectingGattIsEntered_thenConnectTimerIsStarted() throws Exception {
        long connectTimeOut = 666L;
        gattConnectingState = new SHNGattConnectingState(stateMachine, connectTimeOut);

        gattConnectingState.onEnter();

        verify(timerMock).restart();
    }

    @Test
    public void whenCreatingAConnectingStateWithoutAConnectTimeout_thenIt() throws Exception {

        PowerMockito.verifyStatic(Timer.class, times(0));
        Timer.createTimer(any(Runnable.class), anyLong());
    }

    @Test
    public void whenCreatingAConnectingStateWithAConnectTimeout_thenItWillPassTheTimeoutToTheConnectTimer() throws Exception {
        long connectTimeOut = 666L;
        gattConnectingState = new SHNGattConnectingState(stateMachine, connectTimeOut);

        assertThat(timeoutTimeCaptor.getValue()).isEqualTo(connectTimeOut);
    }
}
