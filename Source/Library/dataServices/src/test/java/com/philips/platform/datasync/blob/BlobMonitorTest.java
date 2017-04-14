package com.philips.platform.datasync.blob;

import com.philips.platform.core.events.CreateBlobRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.listeners.BlobUploadRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.File;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BlobMonitorTest {

    @Mock
    private AppComponent appComponantMock;

    BlobMonitor blobMonitor;

    @Mock
    BlobDataSender blobDataSender;

    @Mock
    BlobDataFetcher blobDataFetcher;

    @Mock
    File fileMock;

    @Mock
    BlobUploadRequestListener blobRequestListener;

    @Mock
    CreateBlobRequest createBlobRequestMock;

    @Mock
    UCoreAccessProvider uCoreAccessProviderMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponant(appComponantMock);
        blobMonitor = new BlobMonitor(blobDataSender,blobDataFetcher);
        blobMonitor.uCoreAccessProvider = uCoreAccessProviderMock;
    }

    @Test
    public void should_onEventAsync_CreateBlobRequest_called(){
        when(createBlobRequestMock.getType()).thenReturn("image/jpg");
        when(createBlobRequestMock.getBlobRequestListener()).thenReturn(blobRequestListener);
        when(createBlobRequestMock.getInputStream()).thenReturn(fileMock);

        blobMonitor.onEventAsync(new CreateBlobRequest(fileMock,"image/jpg",blobRequestListener));
        verify(blobDataSender).sendDataToBackend(any(List.class));

    }

}
