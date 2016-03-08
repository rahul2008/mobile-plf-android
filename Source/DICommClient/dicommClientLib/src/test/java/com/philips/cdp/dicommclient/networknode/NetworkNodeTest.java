/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.networknode;

import com.philips.cdp.dicommclient.networknode.NetworkNode.EncryptionKeyUpdatedListener;
import com.philips.cdp.dicommclient.testutil.RobolectricTest;

import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;

public class NetworkNodeTest extends RobolectricTest {

    private static final String TEST_KEY = "TEST_KEY";

    EncryptionKeyUpdatedListener listener;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        listener = Mockito.mock(EncryptionKeyUpdatedListener.class);
    }

    @Test
    public void test_ShouldReturnKey_WhenKeyIsSet_AndGetIsCalled() throws Exception {
        NetworkNode networkNode = new NetworkNode();

        networkNode.setEncryptionKey(TEST_KEY);

        String encryptionKey = networkNode.getEncryptionKey();

        assertEquals(TEST_KEY, encryptionKey);
    }

    @Test
    public void test_ShouldInformListener_WhenKeyIsSet() throws Exception {
        NetworkNode networkNode = new NetworkNode();

        networkNode.setEncryptionKeyUpdatedListener(listener);

        networkNode.setEncryptionKey(TEST_KEY);

        Mockito.verify(listener).onKeyUpdate();
    }

    @Test
    public void test_ShouldInformListener_WhenKeyIsReset() throws Exception {
        NetworkNode networkNode = new NetworkNode();

        networkNode.setEncryptionKeyUpdatedListener(listener);

        networkNode.setEncryptionKey(TEST_KEY);
        Mockito.verify(listener, Mockito.times(1)).onKeyUpdate();

        networkNode.setEncryptionKey(null);
        Mockito.verify(listener, Mockito.times(2)).onKeyUpdate();
    }

    @Test
    public void test_ShouldNotInformListener_WhenTheSameKeyIsSetTwice() throws Exception {
        NetworkNode networkNode = new NetworkNode();

        networkNode.setEncryptionKeyUpdatedListener(listener);

        networkNode.setEncryptionKey(TEST_KEY);
        networkNode.setEncryptionKey(TEST_KEY);
        Mockito.verify(listener, Mockito.times(1)).onKeyUpdate();
    }
}
