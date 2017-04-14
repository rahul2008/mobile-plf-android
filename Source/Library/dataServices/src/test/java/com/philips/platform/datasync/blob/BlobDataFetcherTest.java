package com.philips.platform.datasync.blob;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.listeners.BlobDownloadRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.characteristics.UserCharacteristicsFetcher;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import retrofit.converter.GsonConverter;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


public class BlobDataFetcherTest {

    @Mock
    private AppComponent appComponantMock;

    @Mock
    UCoreAdapter uCoreAdapterMock;

    @Mock
    UCoreAccessProvider uCoreAccessProviderMock;

    @Mock
    GsonConverter gsonConverterMock;

    @Mock
    BaseAppDataCreator baseAppDataCreatorMock;

    @Mock
    Eventing eventing;

    BlobDataFetcher blobDataFetcher;

    @Mock
    BlobMetaData blobMetaData;

    @Mock
    BlobClient client;

    @Mock
    BlobDownloadRequestListener blobDownloadRequestListener;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponant(appComponantMock);
        blobDataFetcher = new BlobDataFetcher(uCoreAdapterMock);
        blobDataFetcher.accessProvider = this.uCoreAccessProviderMock;
        blobDataFetcher.gsonConverter = this.gsonConverterMock;
        blobDataFetcher.dataCreator = this.baseAppDataCreatorMock;
    }

    //Not using this - hence writing it as dummy for line coverage
    @Test
    public void ShouldCallFetchDataSince(){
        blobDataFetcher.fetchDataSince(null);
    }

    @Test
    public void ShouldCall_fetchBlobData_success(){
        when(uCoreAccessProviderMock.getAccessToken()).thenReturn("abcd");
        when(uCoreAccessProviderMock.isLoggedIn()).thenReturn(true);
        when(uCoreAdapterMock.getAppFrameworkClient(BlobClient.class,"abcd",gsonConverterMock)).thenReturn(client);
        blobDataFetcher.fetchBlobData(blobMetaData,blobDownloadRequestListener);
    }
}
