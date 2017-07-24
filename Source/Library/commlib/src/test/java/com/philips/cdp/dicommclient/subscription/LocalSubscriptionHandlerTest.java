/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.subscription;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.security.DISecurity;
import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.cdp.dicommclient.util.WrappedHandler;

import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LocalSubscriptionHandlerTest extends RobolectricTest {

    private static final String APPLIANCE_IP = "198.168.1.145";
    private static final String APPLIANCE_CPPID = "1c5a6bfffe634357";
    private static final String APPLIANCE_KEY = "75B9424B0EA8089428915EB0AA1E4B5E";

    private static final String VALID_ENCRYPTED_LOCALAIRPORTVENT = "kFn051qs6876EV0Q2JItzPE+OUKBRnfUMWFLnbCw1B7yWm0YH8cvZ1yRnolygyCqJqPSD1QGaKZzp6jJ53AfQ5H0i/Xl1Ek3cglWuoeAjpWpL0lWv4hcEb3jgc0x3LUnrrurrlsqhj1w8bcuwWUhrxTFSJqKUGr15E3gRGPkE+lyRJpXb2RoDDgjIL7KwS3Zrre45+UEr9udE8tfqSQILhbPqjfm/7I9KefpKEmHoz3uNkDCvUlvnpyja8gWueBa9Z3LeW2DApHWflvNLHnFEOsH3rgD/XJC2dIrBWn1qQM=";
    private static final String VALID_ENCRYPTED_LOCALFWEVENT = "sBmgcZ7YiMa/eNNbDLrMDyBcdEVzKY6DJq2IYfoUNfZYacDwEsD0dfAvnbSamcUCAiqc6GNGSPndyegm3WKwwwRh52MyQ6rAe2CqvFibPVXuxlEH31qVBnwqOTdU3J363qgHVR8Z3/1FFyHXGy2nN6s7mAO4Z80WMcyIc2jIRGw=";

    private LocalSubscriptionHandler mLocalSubscriptionHandler;
    private NetworkNode mNetworkNode;

    @Mock
    private DISecurity mDISecurity;
    private UdpEventReceiver mUdpEventReceiver;

    private WrappedHandler mSubscriptionEventResponseHandler;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        SubscriptionEventListener mSubscriptionEventListener = mock(SubscriptionEventListener.class);
        mNetworkNode = mock(NetworkNode.class);
        when(mNetworkNode.getIpAddress()).thenReturn(APPLIANCE_IP);
        when(mNetworkNode.getCppId()).thenReturn(APPLIANCE_CPPID);
        when(mNetworkNode.getEncryptionKey()).thenReturn(APPLIANCE_KEY);

        mSubscriptionEventResponseHandler = mock(WrappedHandler.class);

        mUdpEventReceiver = mock(UdpEventReceiver.class);

        mLocalSubscriptionHandler = new LocalSubscriptionHandlerImpl();

        mLocalSubscriptionHandler.enableSubscription(mNetworkNode, mSubscriptionEventListener);
    }

    @Test
    public void testUDPEventReceivedDataNull() {
        mLocalSubscriptionHandler.onUDPEventReceived(null, APPLIANCE_IP);
        verify(mSubscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    @Test
    public void testUDPEventReceivedDataEmptyString() {
        mLocalSubscriptionHandler.onUDPEventReceived("", APPLIANCE_IP);

        verify(mSubscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    @Test
    public void testUDPEventReceivedDataNonDecryptableString() {
        String expected = "dfjalsjdfl";
        mLocalSubscriptionHandler.onUDPEventReceived(expected, APPLIANCE_IP);

        verify(mSubscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    @Test
    public void testUDPEventReceivedIpNull() {
        String expected = VALID_ENCRYPTED_LOCALAIRPORTVENT;
        mLocalSubscriptionHandler.onUDPEventReceived(expected, null);

        verify(mSubscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    @Test
    public void testUDPEventReceivedIpEmptyString() {
        String expected = VALID_ENCRYPTED_LOCALAIRPORTVENT;
        mLocalSubscriptionHandler.onUDPEventReceived(expected, "");

        verify(mSubscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    @Test
    public void testUDPEventReceivedWrongIp() {
        String expected = VALID_ENCRYPTED_LOCALAIRPORTVENT;
        mLocalSubscriptionHandler.onUDPEventReceived(expected, "0.0.0.0");

        verify(mSubscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    @Test
    public void testUDPEncryptedAPEvent() {
        String data = VALID_ENCRYPTED_LOCALAIRPORTVENT;
        when(mDISecurity.decryptData(data)).thenReturn("");

        mLocalSubscriptionHandler.onUDPEventReceived(data, APPLIANCE_IP);

        verify(mSubscriptionEventResponseHandler).post(any(Runnable.class));
    }

    @Test
    public void testUDPEncryptedFWEvent() {
        String data = VALID_ENCRYPTED_LOCALFWEVENT;
        when(mDISecurity.decryptData(data)).thenReturn("");
        mLocalSubscriptionHandler.onUDPEventReceived(data, APPLIANCE_IP);

        //verify(mSubscriptionEventListener).onSubscriptionEventReceived(VALID_DECRYPTED_LOCALFWEVENT);
        verify(mSubscriptionEventResponseHandler).post(any(Runnable.class));
    }

    @Test
    public void testUDPEncryptedAPEventWrongKey() {
        when(mNetworkNode.getEncryptionKey()).thenReturn("726783627");
        String data = VALID_ENCRYPTED_LOCALAIRPORTVENT;
        mLocalSubscriptionHandler.onUDPEventReceived(data, APPLIANCE_IP);

        //verify(mSubscriptionEventListener,never()).onSubscriptionEventReceived(VALID_DECRYPTED_LOCALAIRPORTEVENT);
        // TODO:DICOMM Refactor, we do not check the decrypted data here as before
        verify(mSubscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    private class LocalSubscriptionHandlerImpl extends LocalSubscriptionHandler {

        public LocalSubscriptionHandlerImpl() {
            super(mDISecurity, mUdpEventReceiver);
        }

        @Override
        protected WrappedHandler getSubscriptionEventResponseHandler() {
            return mSubscriptionEventResponseHandler;
        }
    }
}
