/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller;

import com.philips.icpinterface.DownloadData;
import com.philips.icpinterface.SignOn;
import com.philips.icpinterface.data.Commands;
import com.philips.icpinterface.data.Errors;
import java.nio.ByteBuffer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class DefaultCloudControllerTest {

    private DefaultCloudController subject = new DefaultCloudController();

    @Mock
    private SignOn signOn;

    @Mock
    private DownloadData downloadDataMock;

    private ByteBuffer downloadDataBuffer;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        initBufferUsedForTests();
    }

    private void initBufferUsedForTests() {
        final int DOWNLOAD_DATA_BUFFER_CAPACITY = 1000;
        downloadDataBuffer = ByteBuffer.allocateDirect(DOWNLOAD_DATA_BUFFER_CAPACITY);
        for (int i = 0; i < DOWNLOAD_DATA_BUFFER_CAPACITY; i++) {
            downloadDataBuffer.put((byte) 1);
        }
        downloadDataBuffer.rewind();
    }

    @Test
    public void dataDownloadSuccessfullyProcessed() throws Exception {
        // Buffer set up in setUp(), no other init needed.

        when(downloadDataMock.getBuffer()).thenReturn(downloadDataBuffer);

        subject.onICPCallbackEventOccurred(Commands.DOWNLOAD_DATA, Errors.SUCCESS, downloadDataMock);
    }

    @Test
    public void dataDownloadSuccessfullyProcessedWhenBufferIndexNotAtStart() throws Exception {
        downloadDataBuffer.get();

        when(downloadDataMock.getBuffer()).thenReturn(downloadDataBuffer);

        subject.onICPCallbackEventOccurred(Commands.DOWNLOAD_DATA, Errors.SUCCESS, downloadDataMock);
    }

    @Test
    public void givenSignOnWasCompleted_whenLocaleIsSet_thenLocaleIsForwardedToIcpClient() {
        subject.setSignOn(signOn);

        subject.setNewLocale("", "");

        verify(signOn).setNewLocale("", "");
    }

    @Test(expected = IllegalStateException.class)
    public void givenSignOnWasNotCompleted_whenLocaleIsSet_thenIllegalStateExceptionIsThrown() {

        subject.setNewLocale("", "");
    }

    /*
    Acceptance Criteria
     - given di-comm pairing relation does not exist for old provisioning, when provisioning is run, then the new strategy (fixed evidence) will be used
     - given di-comm pairing relation does exist for old provisioning, when provisioning is run, then old strategy (swapped appId and version) will remain in use
     - given evidence has changed, when provisioning is run, the new strategy will always be used
     */

    //@Test
    //public void givenDiCommPairingRelationshipDoesNotExistForOldProvisioning_whenProvisioningIsRan_thenTheNewStrategyWillBeUsed() {
    //
    //}

    @Test
    public void givenProvisioningStrategyIsStored_whenStartingProvisioning_thenStoredProvisioningStrategyShouldBeUsed() {
        fail();
    }

    @Test
    public void givenProvisioningStrategyIsNotStored_whenStartingProvisioning_thenOldProvisioningStrategyShouldBeUsed() {
        fail();
    }

    @Test
    public void givenOldProvisioningStrategyIsUsed_whenPairingRelationsExist_thenOldProvisioningStrategyWillBeStored() {
        fail();
    }

    @Test
    public void givenOldProvisioningStrategyIsUsed_whenNoPairingRelationsExist_thenNewProvisioningStrategyWillBeStored() {
        fail();
    }

    @Test
    public void givenProvisioningStrategyIsNotStored_whenProvisioningIsFinished_thenProvisioningStrategyIsStored() {
        fail();
    }

    @Test
    public void givenProvisioningEvidenceHasChanged_whenStartingProvisioning_thenNewProvisioningStrategyWillBeStored() {
        fail();
    }
}