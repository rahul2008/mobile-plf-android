package com.philips.platform.devicepair.ui;

import android.app.Activity;

import com.philips.platform.devicepair.pojo.PairDevice;
import com.philips.platform.devicepair.states.AbstractBaseState;
import com.philips.platform.devicepair.states.PairDeviceState;
import com.philips.platform.devicepair.states.StateContext;
import com.philips.platform.devicepair.states.UnPairDeviceState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PairingFragmentPresenter.class, StateContext.class})
public class PairingFragmentPresenterTest {

    @Test
    public void pairDevice() throws Exception {
        whenPairingDevice();
        thenStateIsChangedTo(PairDeviceState.class);
        thenStateIsStarted();
    }

    @Test
    public void unpairDevice() throws Exception {
        whenUnpairingDevice();
        thenStateIsChangedTo(UnPairDeviceState.class);
        thenStateIsStarted();
    }

    private void whenPairingDevice() throws Exception {
        PowerMockito.whenNew(StateContext.class).withAnyArguments().thenReturn(stateContext);
        pairingFragmentPresenter.pairDevice(pairDevice, iDevicePairingListener);
    }

    private void whenUnpairingDevice() throws Exception {
        PowerMockito.whenNew(StateContext.class).withAnyArguments().thenReturn(stateContext);
        pairingFragmentPresenter.unPairDevice(pairDevice.getDeviceID(), iDevicePairingListener);
    }

    private void thenStateIsChangedTo(Class<?> stateClass) {
        ArgumentCaptor<AbstractBaseState> argument = ArgumentCaptor.forClass(AbstractBaseState.class);
        verify(stateContext).setState(argument.capture());
        assertEquals(stateClass, argument.getValue().getClass());
    }

    private void thenStateIsStarted() {
        verify(stateContext, times(1)).start();
    }

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        stateContext = PowerMockito.mock(StateContext.class);
        pairingFragmentPresenter = new PairingFragmentPresenter(activity);
    }

    @Mock
    private Activity activity;
    @Mock
    private PairDevice pairDevice;
    @Mock
    private IDevicePairingListener iDevicePairingListener;
    private PairingFragmentPresenter pairingFragmentPresenter;
    private StateContext stateContext;
}