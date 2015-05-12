package com.philips.cl.di.dicomm;

import org.mockito.Mockito;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.networknode.NetworkNode.EncryptionKeyUpdatedListener;
import com.philips.cl.di.dicomm.util.MockitoTestCase;

public class NetworkNodeTest extends MockitoTestCase {

    private static final String TEST_KEY = "TEST_KEY";

    EncryptionKeyUpdatedListener listener;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        listener = Mockito.mock(EncryptionKeyUpdatedListener.class);
    }

    public void test_ShouldReturnKey_WhenKeyIsSet_AndGetIsCalled() throws Exception {
        NetworkNode networkNode = new NetworkNode();

        networkNode.setEncryptionKey(TEST_KEY);

        String encryptionKey = networkNode.getEncryptionKey();

        assertEquals(TEST_KEY, encryptionKey);
    }

    public void test_ShouldInformListener_WhenKeyIsSet() throws Exception {
        NetworkNode networkNode = new NetworkNode();

        networkNode.setEncryptionKeyUpdatedListener(listener);

        networkNode.setEncryptionKey(TEST_KEY);

        Mockito.verify(listener).onKeyUpdate();
    }

    public void test_ShouldInformListener_WhenKeyIsReset() throws Exception {
        NetworkNode networkNode = new NetworkNode();

        networkNode.setEncryptionKeyUpdatedListener(listener);

        networkNode.setEncryptionKey(TEST_KEY);
        Mockito.verify(listener, Mockito.times(1)).onKeyUpdate();

        networkNode.setEncryptionKey(null);
        Mockito.verify(listener, Mockito.times(2)).onKeyUpdate();
    }

    public void test_ShouldNotInformListener_WhenTheSameKeyIsSetTwice() throws Exception {
        NetworkNode networkNode = new NetworkNode();

        networkNode.setEncryptionKeyUpdatedListener(listener);

        networkNode.setEncryptionKey(TEST_KEY);
        networkNode.setEncryptionKey(TEST_KEY);
        Mockito.verify(listener, Mockito.times(1)).onKeyUpdate();
    }
}
