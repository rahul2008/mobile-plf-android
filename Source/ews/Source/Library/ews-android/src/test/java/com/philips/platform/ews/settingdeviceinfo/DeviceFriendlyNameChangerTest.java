package com.philips.platform.ews.settingdeviceinfo;

import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.DevicePort;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.platform.ews.appliance.EWSGenericAppliance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DeviceFriendlyNameChangerTest {

    @InjectMocks
    private DeviceFriendlyNameChanger subject;

    @Mock
    private EWSGenericAppliance mockAppliance;
    @Mock
    private DevicePort mockDevicePort;
    @Mock
    private DeviceFriendlyNameChanger.Callback mockCallback;

    @Captor
    private ArgumentCaptor<DICommPortListener> portListenerCaptor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(mockAppliance.getDevicePort()).thenReturn(mockDevicePort);
    }

    @Test
    public void itShouldAddListenerToDevicePortWhenChangeIsCalled() throws Exception {
        subject.changeFriendlyName("anyName");

        verify(mockDevicePort).addPortListener(any(DICommPortListener.class));
    }

    @Test
    public void itShouldChangeTheFriendlyNameFromDevicePortWhenChangeIsCalled() throws Exception {
        subject.changeFriendlyName("anyName");

        verify(mockDevicePort).setDeviceName(anyString());
    }

    @Test
    public void itShouldNotChangeTheFriendlyNameFromDevicePortWhenChangeIsCalledButNameIsEmpty() throws Exception {
        subject.changeFriendlyName("");

        verifyZeroInteractions(mockDevicePort);
    }

    @Test
    public void itShouldPassCorrectNewNameWhenChangingDeviceFriendlyName() throws Exception {
        String newName = "newName";
        changeFriendlyName(newName);

        verify(mockDevicePort).setDeviceName(eq(newName));
    }

    @Test
    public void itShouldForwardSuccessToCallbackWhenChangeIsSuccessfulButNameIsEmpty() throws Exception {
        String newName = "";
        changeFriendlyName(newName);
        verify(mockCallback).onFriendlyNameChangingSuccess();
    }

    @Test
    public void itShouldForwardSuccessToCallbackWhenChangeIsSuccessful() throws Exception {
        simulatePutPropsSuccess(mockCallback);

        verify(mockCallback).onFriendlyNameChangingSuccess();
    }

    @Test
    public void itShouldNotForwardSuccessToCallbackWhenChangeIsSuccessfulAndCallbackIsNull() throws Exception {
        simulatePutPropsSuccess(null);

        verifyZeroInteractions(mockCallback);
    }

    @Test
    public void itShouldForwardFailureToCallbackWhenPutPropsFails() throws Exception {
        simulatePutPropsFailure(mockCallback);

        verify(mockCallback).onFriendlyNameChangingFailed();
    }

    @Test
    public void itShouldNotForwardFailureToCallbackWhenPutPropsFailsAndCallbackIsNull() throws Exception {
        simulatePutPropsFailure(null);

        verifyZeroInteractions(mockCallback);
    }

    @Test
    public void itShouldClearCallback() throws Exception {
        subject.changeFriendlyName("anyName");

        subject.clear();

        assertNull(subject.getCallback());
    }

    private void changeFriendlyName(String newName) {
        subject.setNameChangerCallback(mockCallback);
        subject.changeFriendlyName(newName);
    }

    private void simulatePutPropsSuccess(@Nullable DeviceFriendlyNameChanger.Callback mockCallback) {
        subject.changeFriendlyName("anyName");
        subject.setNameChangerCallback(mockCallback);
        verify(mockDevicePort).addPortListener(portListenerCaptor.capture());
        portListenerCaptor.getValue().onPortUpdate(mockDevicePort);
    }

    private void simulatePutPropsFailure(@Nullable DeviceFriendlyNameChanger.Callback mockCallback) {
        subject.changeFriendlyName("anyName");
        subject.setNameChangerCallback(mockCallback);
        verify(mockDevicePort).addPortListener(portListenerCaptor.capture());
        portListenerCaptor.getValue().onPortError(mockDevicePort, Error.CANNOT_CONNECT, "error!!");
    }
}