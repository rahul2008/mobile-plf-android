/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.philips.icpinterface.DownloadData;
import com.philips.icpinterface.GlobalStore;
import com.philips.icpinterface.SignOn;
import com.philips.icpinterface.data.Commands;
import com.philips.icpinterface.data.Errors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, GlobalStore.class})
public class DefaultCloudControllerTest {

    private DefaultCloudController cloudController;

    @Mock
    private Context context;

    @Mock
    private SignOn signOn;

    @Mock
    private DownloadData downloadDataMock;

    @Mock
    private AssetManager assetManagerMock;

    @Mock
    private GlobalStore globalStoreMock;

    private ByteBuffer downloadDataBuffer;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(Log.class, GlobalStore.class);

        when(GlobalStore.getInstance()).thenReturn(globalStoreMock);
        initBufferUsedForTests();

        cloudController = new DefaultCloudController(context);
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

        cloudController.onICPCallbackEventOccurred(Commands.DOWNLOAD_DATA, Errors.SUCCESS, downloadDataMock);
    }

    @Test
    public void dataDownloadSuccessfullyProcessedWhenBufferIndexNotAtStart() throws Exception {
        downloadDataBuffer.get();

        when(downloadDataMock.getBuffer()).thenReturn(downloadDataBuffer);

        cloudController.onICPCallbackEventOccurred(Commands.DOWNLOAD_DATA, Errors.SUCCESS, downloadDataMock);
    }

    @Test
    public void givenSignOnWasCompleted_whenLocaleIsSet_thenLocaleIsForwardedToIcpClient() {
        cloudController.setSignOn(signOn);

        cloudController.setNewLocale("", "");

        verify(signOn).setNewLocale("", "");
    }

    @Test(expected = IllegalStateException.class)
    public void givenSignOnWasNotCompleted_whenLocaleIsSet_thenIllegalStateExceptionIsThrown() {

        cloudController.setNewLocale("", "");
    }

    @Test
    public void givenACertificateExistsInAssets_whenLoadCertificatesIsCalled_thenThatSingleCertificateIsLoaded() throws Exception {
        String[] fileNames = {"one.cer"};
        byte[] certificateBytes = {1, 3, 3, 7};
        when(context.getAssets()).thenReturn(assetManagerMock);
        when(assetManagerMock.open(fileNames[0])).thenReturn(new ByteArrayInputStream(certificateBytes));
        when(assetManagerMock.list(anyString())).thenReturn(fileNames);

        cloudController.loadCertificates();

        verify(globalStoreMock).setCertificateByteArray(certificateBytes);
    }

    @Test
    public void givenACertificateAndAnotherFileExistsInAssets_whenLoadCertificatesIsCalled_thenOnlyTheSingleCertificateIsLoaded() throws Exception {
        String[] fileNames = {"one.cer", "two.txt"};
        byte[] certificateBytes = {1, 3, 3, 7};
        byte[] txtBytes = {6, 6, 6};
        when(context.getAssets()).thenReturn(assetManagerMock);
        when(assetManagerMock.open(fileNames[0])).thenReturn(new ByteArrayInputStream(certificateBytes));
        when(assetManagerMock.open(fileNames[1])).thenReturn(new ByteArrayInputStream(txtBytes));
        when(assetManagerMock.list(anyString())).thenReturn(fileNames);

        cloudController.loadCertificates();

        verify(globalStoreMock).setCertificateByteArray(certificateBytes);
        verify(globalStoreMock, never()).setCertificateByteArray(txtBytes);
    }

    @Test
    public void givenNoCertificateExistsInAssets_whenLoadCertificatesIsCalled_thenNoCertificateIsLoaded() throws Exception {
        String[] fileNames = {"one.txt"};
        when(context.getAssets()).thenReturn(assetManagerMock);
        when(assetManagerMock.list(anyString())).thenReturn(fileNames);

        cloudController.loadCertificates();

        verify(globalStoreMock, never()).setCertificateByteArray((byte[]) any());
    }

    @Test
    public void givenTwoCertificatesExistInAssets_whenLoadCertificatesIsCalled_thenBothCertificatesAreLoaded() throws Exception {
        String[] fileNames = {"one.cer", "two.cer"};
        byte[] certificateBytes = {1, 3, 3, 7};
        byte[] certificateBytes2 = {6, 6, 6};
        when(context.getAssets()).thenReturn(assetManagerMock);
        when(assetManagerMock.open(fileNames[0])).thenReturn(new ByteArrayInputStream(certificateBytes));
        when(assetManagerMock.open(fileNames[1])).thenReturn(new ByteArrayInputStream(certificateBytes2));
        when(assetManagerMock.list(anyString())).thenReturn(fileNames);

        cloudController.loadCertificates();

        verify(globalStoreMock).setCertificateByteArray(certificateBytes);
        verify(globalStoreMock).setCertificateByteArray(certificateBytes2);
    }
}