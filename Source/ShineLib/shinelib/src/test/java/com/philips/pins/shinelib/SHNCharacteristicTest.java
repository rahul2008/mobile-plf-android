package com.philips.pins.shinelib;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import com.nitorcreations.junit.runners.NestedRunner;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.helper.Utility;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;

@RunWith(NestedRunner.class)
public class SHNCharacteristicTest {
    private static final UUID CLIENT_CHARACTERISTIC_CONFIG_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    private SHNCharacteristic shnCharacteristic;
    private UUID characteristicUUID;

    @Mock
    private SHNCommandResultReporter resultReporterMock;

    @Mock
    private BluetoothGattDescriptor mockedDescriptor;

    @Mock
    private BTGatt mockedBTGatt;

    @Mock
    private BluetoothGattCharacteristic mockedBluetoothGattCharacteristic;

    @Before
    public void setUp() {
        initMocks(this);

        characteristicUUID = UUID.randomUUID();
        shnCharacteristic = new SHNCharacteristic(characteristicUUID);
    }

    @Test
    public void testGetState() {
        assertEquals(SHNCharacteristic.State.Inactive, shnCharacteristic.getState());
    }

    @Test
    public void testGetUuid() {
        assertEquals(characteristicUUID, shnCharacteristic.getUuid());
    }

    @Test
    public void testConnectToBLELayer() {
        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);
        assertEquals(SHNCharacteristic.State.Active, shnCharacteristic.getState());
    }

    @Test
    public void testDisconnectFromBLELayer() {
        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);
        shnCharacteristic.disconnectFromBLELayer();
        assertEquals(SHNCharacteristic.State.Inactive, shnCharacteristic.getState());
    }

    @Test
    public void testWhenAReadIsRequestedWhenADisconnectOccursThenTheReadCompletesWithAnError() {
        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);
        shnCharacteristic.read(resultReporterMock);
        shnCharacteristic.disconnectFromBLELayer();
        verify(resultReporterMock).reportResult(SHNResult.SHNErrorConnectionLost, null);
    }

    @Test
    public void testWhenAReadIsRequestedWithNoCompletionBlockWhenADisconnectOccursThenNoCompletionBlockIsIgnored() {
        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);
        shnCharacteristic.read(null);
        shnCharacteristic.disconnectFromBLELayer();
    }

    @Test
    public void testWhenAWriteIsRequestedWhenADisconnectOccursThenTheWriteCompletesWithAnError() {
        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);
        shnCharacteristic.write(new byte[]{'d', 'a', 't', 'a'}, resultReporterMock);
        shnCharacteristic.disconnectFromBLELayer();
        verify(resultReporterMock).reportResult(SHNResult.SHNErrorConnectionLost, null);
    }

    @Test
    public void testGetValue() {
        byte[] mockedData = new byte[]{'d', 'a', 't', 'a'};
        doReturn(mockedData).when(mockedBluetoothGattCharacteristic).getValue();

        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);

        byte[] data = shnCharacteristic.getValue();
        assertEquals(mockedData, data);
    }

    @Test
    public void whenWriteIsCalledThenWriteCharacteristicOnBTGattIsCalled() {
        byte[] data = new byte[]{'d', 'a', 't', 'a'};

        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);

        shnCharacteristic.write(data, null);
        verify(mockedBTGatt).writeCharacteristic(mockedBluetoothGattCharacteristic, data);
    }

    @Test
    public void whenReadIsCalledThenReadCharacteristicOnBTGattIsCalled() {
        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);
        shnCharacteristic.read(resultReporterMock);
        verify(mockedBTGatt).readCharacteristic(mockedBluetoothGattCharacteristic);
    }

    @Test
    public void whenAReadRequestCompletesThenTheResultReporterIsCalled() {
        byte[] data = new byte[]{'d', 'a', 't', 'a'};
        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);

        shnCharacteristic.read(resultReporterMock);

        shnCharacteristic.onReadWithData(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, data);

        verify(resultReporterMock).reportResult(SHNResult.SHNOk, data);
    }

    @Test
    public void whenAWriteRequestCompletesThenTheResultReporterIsCalled() {
        byte[] data = new byte[]{'d', 'a', 't', 'a'};

        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);

        shnCharacteristic.write(data, resultReporterMock);

        shnCharacteristic.onWrite(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);

        verify(resultReporterMock).reportResult(SHNResult.SHNOk, null);
    }

    @Test
    public void testOnChanged() {
        byte[] data = new byte[]{'d', 'a', 't', 'a'};
        SHNCharacteristic.SHNCharacteristicChangedListener mockedSHNCharacteristicChangedListener = (SHNCharacteristic.SHNCharacteristicChangedListener) Utility.makeThrowingMock(SHNCharacteristic.SHNCharacteristicChangedListener.class);
        doNothing().when(mockedSHNCharacteristicChangedListener).onCharacteristicChanged(eq(shnCharacteristic), any(byte[].class));

        shnCharacteristic.setShnCharacteristicChangedListener(mockedSHNCharacteristicChangedListener);
        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);

        shnCharacteristic.onChanged(mockedBTGatt, data);
        verify(mockedSHNCharacteristicChangedListener).onCharacteristicChanged(shnCharacteristic, data);
    }

    @Test
    public void testOnDescriptorReadWithData() {
        boolean exceptionCaught = false;
        try {
            shnCharacteristic.onDescriptorReadWithData(null, null, BluetoothGatt.GATT_SUCCESS, null);
        } catch (Exception e) {
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);
    }

    @Test
    public void testOnChangedWithoutListener() {
        final int[] characteristicChangedListenerInvocationCount = {0};
        byte[] data = new byte[]{'d', 'a', 't', 'a'};
        SHNCharacteristic.SHNCharacteristicChangedListener mockedSHNCharacteristicChangedListener = (SHNCharacteristic.SHNCharacteristicChangedListener) Utility.makeThrowingMock(SHNCharacteristic.SHNCharacteristicChangedListener.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                characteristicChangedListenerInvocationCount[0]++;
                return null;
            }
        }).when(mockedSHNCharacteristicChangedListener).onCharacteristicChanged(eq(shnCharacteristic), any(byte[].class));

        shnCharacteristic.setShnCharacteristicChangedListener(mockedSHNCharacteristicChangedListener);
        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);

        shnCharacteristic.onChanged(mockedBTGatt, data);
        verify(mockedSHNCharacteristicChangedListener).onCharacteristicChanged(shnCharacteristic, data);
        assertEquals(1, characteristicChangedListenerInvocationCount[0]); // Added to prove that the doAnswer on the listener works

        shnCharacteristic.setShnCharacteristicChangedListener(null);
        shnCharacteristic.onChanged(mockedBTGatt, data);
        assertEquals(1, characteristicChangedListenerInvocationCount[0]);
    }

    @Test
    public void whenReadIsCalledWhenNotActiveThenReadIsNotAccepted() {
        shnCharacteristic.read(resultReporterMock);
        verify(resultReporterMock).reportResult(SHNResult.SHNErrorInvalidState, null);
    }

    @Test
    public void whenGetValueIsCalledWhenNotActiveThenNullIsReturned() {
        assertNull(shnCharacteristic.getValue());
    }

    @Test
    public void whenWriteIsCalledWhenNotActiveThenInvalidStateIsReported() {
        shnCharacteristic.write(null, resultReporterMock);

        verify(resultReporterMock).reportResult(SHNResult.SHNErrorInvalidState, null);
    }

    @Test
    public void whenAWriteCharacteristicsFailsThenItIsReportedProperly() {
        byte[] data = new byte[]{'d', 'a', 't', 'a'};

        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                shnCharacteristic.onWrite(mockedBTGatt, BluetoothGatt.GATT_FAILURE);
                return null;
            }
        }).when(mockedBTGatt).writeCharacteristic(any(BluetoothGattCharacteristic.class), any(byte[].class));
        shnCharacteristic.write(data, resultReporterMock);
        verify(mockedBTGatt).writeCharacteristic(mockedBluetoothGattCharacteristic, data);

        verify(resultReporterMock).reportResult(SHNResult.SHNErrorInvalidResponse, null);
    }

    @Test
    public void whenAReadCharacteristicsFailsThenItIsReportedProperly() {
        byte[] data = new byte[]{'d', 'a', 't', 'a'};
        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                shnCharacteristic.onReadWithData(mockedBTGatt, BluetoothGatt.GATT_FAILURE, null);
                return null;
            }
        }).when(mockedBTGatt).readCharacteristic(any(BluetoothGattCharacteristic.class));
        shnCharacteristic.read(resultReporterMock);

        verify(resultReporterMock).reportResult(SHNResult.SHNErrorInvalidResponse, null);
    }

    public class WhenTogglingNotifications
    {
        public class AndNotConnectedToBLELayer
        {
            @Before
            public void setUp() {
                shnCharacteristic.setNotification(true, resultReporterMock);
            }

            @Test
            public void itShouldReportErrorInvalidState() {
                verify(resultReporterMock).reportResult(SHNResult.SHNErrorInvalidState, null);
            }
        }

        public class AndConnectedToBLELayer {
            @Before
            public void setUp() {
                shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);
            }

            public class AndSetCharacteristicNotificationFails {
                @Before
                public void setUp() {
                    doReturn(false).when(mockedBTGatt).setCharacteristicNotification(eq(mockedBluetoothGattCharacteristic), anyBoolean());
                    shnCharacteristic.setNotification(true, resultReporterMock);
                }

                @Test
                public void itShouldReportErrorUnsupportedOperation() {
                    verify(resultReporterMock).reportResult(SHNResult.SHNErrorUnsupportedOperation, null);
                }
            }

            public class AndAcquiringTheClientCharacteristicConfigurationDescriptorFails {
                @Before
                public void setUp() {
                    doReturn(true).when(mockedBTGatt).setCharacteristicNotification(eq(mockedBluetoothGattCharacteristic), anyBoolean());
                    doReturn(null).when(mockedBluetoothGattCharacteristic).getDescriptor(eq(CLIENT_CHARACTERISTIC_CONFIG_UUID));
                    shnCharacteristic.setNotification(true, resultReporterMock);
                }

                @Test
                public void itShouldReportErrorUnsupportedOperation() {
                    verify(resultReporterMock).reportResult(SHNResult.SHNErrorUnsupportedOperation, null);
                }
            }

            public class AndBTGattRespondsProperly {
                @Before
                public void setUp() {
                    doReturn(true).when(mockedBTGatt).setCharacteristicNotification(eq(mockedBluetoothGattCharacteristic), anyBoolean());
                    doReturn(mockedDescriptor).when(mockedBluetoothGattCharacteristic).getDescriptor(eq(CLIENT_CHARACTERISTIC_CONFIG_UUID));
                }

                public class AndNotificationsAreEnabled {
                    @Before
                    public void setUp() {
                        shnCharacteristic.setNotification(true, resultReporterMock);
                    }

                    @Test
                    public void itForwardsTheCallToBTGatt() {
                        verify(mockedBTGatt).setCharacteristicNotification(eq(mockedBluetoothGattCharacteristic), eq(true));
                    }

                    @Test
                    public void itAcquiresClientCharacteristicConfigurationDescriptor() {
                        verify(mockedBluetoothGattCharacteristic).getDescriptor(eq(CLIENT_CHARACTERISTIC_CONFIG_UUID));
                    }

                    @Test
                    public void itWritesAppropriatelyTotheClientCharacteristicConfigurationDescriptor() {
                        verify(mockedBTGatt).writeDescriptor(mockedDescriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    }

                    @Test
                    public void itReportsSuccessWhenASuccessfulOnDescriptorWriteIsReceived() {
                        shnCharacteristic.onDescriptorWrite(mockedBTGatt, mockedDescriptor, BluetoothGatt.GATT_SUCCESS);
                        verify(resultReporterMock).reportResult(SHNResult.SHNOk, null);
                    }

                    @Test
                    public void itReportsAnErrorWhenTheConnectionIsLost() {
                        shnCharacteristic.disconnectFromBLELayer();
                        verify(resultReporterMock).reportResult(SHNResult.SHNErrorConnectionLost, null);
                    }

                }

                public class AndNotificationsAreDisabled {
                    @Before
                    public void setUp() {
                        shnCharacteristic.setNotification(false, resultReporterMock);
                    }

                    @Test
                    public void itForwardsTheCallToBTGatt() {
                        verify(mockedBTGatt).setCharacteristicNotification(eq(mockedBluetoothGattCharacteristic), eq(false));
                    }

                    @Test
                    public void itAcquiresClientCharacteristicConfigurationDescriptor() {
                        verify(mockedBluetoothGattCharacteristic).getDescriptor(eq(CLIENT_CHARACTERISTIC_CONFIG_UUID));
                    }

                    @Test
                    public void itWritesAppropriatelyTotheClientCharacteristicConfigurationDescriptor() {
                        verify(mockedBTGatt).writeDescriptor(mockedDescriptor, BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                    }

                    @Test
                    public void itReportsSuccessWhenASuccessfulOnDescriptorWriteIsReceived() {
                        shnCharacteristic.onDescriptorWrite(mockedBTGatt, mockedDescriptor, BluetoothGatt.GATT_SUCCESS);
                        verify(resultReporterMock).reportResult(SHNResult.SHNOk, null);
                    }

                    @Test
                    public void itReportsErrorWhenAnErroneousOnDescriptorWriteIsReceived() {
                        shnCharacteristic.onDescriptorWrite(mockedBTGatt, mockedDescriptor, BluetoothGatt.GATT_FAILURE);
                        verify(resultReporterMock).reportResult(SHNResult.SHNErrorInvalidResponse, null);
                    }
                }
            }

            public class AndWriteDescriptorFails {
                @Before
                public void setUp() {
                    doReturn(true).when(mockedBTGatt).setCharacteristicNotification(eq(mockedBluetoothGattCharacteristic), anyBoolean());
                    doReturn(mockedDescriptor).when(mockedBluetoothGattCharacteristic).getDescriptor(eq(CLIENT_CHARACTERISTIC_CONFIG_UUID));
                    doAnswer(new Answer<Object>() {
                        @Override
                        public Object answer(InvocationOnMock invocation) throws Throwable {
                            shnCharacteristic.onDescriptorWrite(mockedBTGatt, mockedDescriptor, BluetoothGatt.GATT_FAILURE);
                            return null;
                        }
                    }).when(mockedBTGatt).writeDescriptor(any(BluetoothGattDescriptor.class), any(byte[].class));
                }

                public class AndNotificationsAreEnabled {
                    @Before
                    public void setUp() {
                        shnCharacteristic.setNotification(true, resultReporterMock);
                    }

                    @Test
                    public void itReportsFailedWhenAFailedOnDescriptorWriteIsReceived() {
                        verify(resultReporterMock).reportResult(SHNResult.SHNErrorInvalidResponse, null);
                    }
                }
            }
        }
    }

    public class WhenTogglingIndications
    {
        public class AndNotConnectedToBLELayer
        {
            @Before
            public void setUp() {
                shnCharacteristic.setIndication(true, resultReporterMock);
            }

            @Test
            public void itShouldReportErrorInvalidState() {
                verify(resultReporterMock).reportResult(SHNResult.SHNErrorInvalidState, null);
            }
        }

        public class AndConnectedToBLELayer {
            @Before
            public void setUp() {
                shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);
            }

            public class AndSetCharacteristicNotificationFails {
                @Before
                public void setUp() {
                    doReturn(false).when(mockedBTGatt).setCharacteristicNotification(eq(mockedBluetoothGattCharacteristic), anyBoolean());
                    shnCharacteristic.setIndication(true, resultReporterMock);
                }

                @Test
                public void itShouldReportErrorUnsupportedOperation() {
                    verify(resultReporterMock).reportResult(SHNResult.SHNErrorUnsupportedOperation, null);
                }
            }

            public class AndAcquiringTheClientCharacteristicConfigurationDescriptorFails {
                @Before
                public void setUp() {
                    doReturn(true).when(mockedBTGatt).setCharacteristicNotification(eq(mockedBluetoothGattCharacteristic), anyBoolean());
                    doReturn(null).when(mockedBluetoothGattCharacteristic).getDescriptor(eq(CLIENT_CHARACTERISTIC_CONFIG_UUID));
                    shnCharacteristic.setIndication(true, resultReporterMock);
                }

                @Test
                public void itShouldReportErrorUnsupportedOperation() {
                    verify(resultReporterMock).reportResult(SHNResult.SHNErrorUnsupportedOperation, null);
                }
            }

            public class AndBTGattRespondsProperly {
                @Before
                public void setUp() {
                    doReturn(true).when(mockedBTGatt).setCharacteristicNotification(eq(mockedBluetoothGattCharacteristic), anyBoolean());
                    doReturn(mockedDescriptor).when(mockedBluetoothGattCharacteristic).getDescriptor(eq(CLIENT_CHARACTERISTIC_CONFIG_UUID));
                }

                public class AndIndicationsAreEnabled {
                    @Before
                    public void setUp() {
                        shnCharacteristic.setIndication(true, resultReporterMock);
                    }

                    @Test
                    public void itForwardsTheCallToBTGatt() {
                        verify(mockedBTGatt).setCharacteristicNotification(eq(mockedBluetoothGattCharacteristic), eq(true));
                    }

                    @Test
                    public void itAcquiresClientCharacteristicConfigurationDescriptor() {
                        verify(mockedBluetoothGattCharacteristic).getDescriptor(eq(CLIENT_CHARACTERISTIC_CONFIG_UUID));
                    }

                    @Test
                    public void itWritesAppropriatelyTotheClientCharacteristicConfigurationDescriptor() {
                        verify(mockedBTGatt).writeDescriptor(mockedDescriptor, BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                    }

                    @Test
                    public void itReportsSuccessWhenASuccessfulOnDescriptorWriteIsReceived() {
                        shnCharacteristic.onDescriptorWrite(mockedBTGatt, mockedDescriptor, BluetoothGatt.GATT_SUCCESS);
                        verify(resultReporterMock).reportResult(SHNResult.SHNOk, null);
                    }
                }

                public class AndIndicationsAreDisabled {
                    @Before
                    public void setUp() {
                        shnCharacteristic.setIndication(false, resultReporterMock);
                    }

                    @Test
                    public void itForwardsTheCallToBTGatt() {
                        verify(mockedBTGatt).setCharacteristicNotification(eq(mockedBluetoothGattCharacteristic), eq(false));
                    }

                    @Test
                    public void itAcquiresClientCharacteristicConfigurationDescriptor() {
                        verify(mockedBluetoothGattCharacteristic).getDescriptor(eq(CLIENT_CHARACTERISTIC_CONFIG_UUID));
                    }

                    @Test
                    public void itWritesAppropriatelyTotheClientCharacteristicConfigurationDescriptor() {
                        verify(mockedBTGatt).writeDescriptor(mockedDescriptor, BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                    }

                    @Test
                    public void itReportsSuccessWhenASuccessfulOnDescriptorWriteIsReceived() {
                        shnCharacteristic.onDescriptorWrite(mockedBTGatt, mockedDescriptor, BluetoothGatt.GATT_SUCCESS);
                        verify(resultReporterMock).reportResult(SHNResult.SHNOk, null);
                    }

                    @Test
                    public void itReportsErrorWhenAnErroneousOnDescriptorWriteIsReceived() {
                        shnCharacteristic.onDescriptorWrite(mockedBTGatt, mockedDescriptor, BluetoothGatt.GATT_FAILURE);
                        verify(resultReporterMock).reportResult(SHNResult.SHNErrorInvalidResponse, null);
                    }
                }
            }

            public class AndWriteDescriptorFails {
                @Before
                public void setUp() {
                    doReturn(true).when(mockedBTGatt).setCharacteristicNotification(eq(mockedBluetoothGattCharacteristic), anyBoolean());
                    doReturn(mockedDescriptor).when(mockedBluetoothGattCharacteristic).getDescriptor(eq(CLIENT_CHARACTERISTIC_CONFIG_UUID));
                    doAnswer(new Answer<Object>() {
                        @Override
                        public Object answer(InvocationOnMock invocation) throws Throwable {
                            shnCharacteristic.onDescriptorWrite(mockedBTGatt, mockedDescriptor, BluetoothGatt.GATT_FAILURE);
                            return null;
                        }
                    }).when(mockedBTGatt).writeDescriptor(any(BluetoothGattDescriptor.class), any(byte[].class));
                }

                public class AndIndicationsAreEnabled {
                    @Before
                    public void setUp() {
                        shnCharacteristic.setIndication(true, resultReporterMock);
                    }

                    @Test
                    public void itReportsFailedWhenAFailedOnDescriptorWriteIsReceived() {
                        verify(resultReporterMock).reportResult(SHNResult.SHNErrorInvalidResponse, null);
                    }
                }
            }
        }
    }
}