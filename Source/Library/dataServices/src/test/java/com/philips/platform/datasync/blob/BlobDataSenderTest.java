package com.philips.platform.datasync.blob;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.listeners.BlobUploadRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.BlobDataCreater;
import com.philips.platform.datasync.MomentGsonConverter;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedFile;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


public class BlobDataSenderTest {
    @Mock
    private AppComponent appComponantMock;

    @Mock
    UCoreAdapter uCoreAdapterMock;

    BlobDataSender blobDataSender;

    @Mock
    Eventing eventingMock;

    @Mock
    BlobMetaData blobDataMock;

    @Mock
    BlobDataCreater dataCreatorMock;

    @Mock
    UCoreAccessProvider uCoreAccessProviderMock;

    @Mock
    MomentGsonConverter gsonConverterMock;

    @Mock
    BlobUploadRequestListener blobRequestListenerMock;

    @Mock
    BlobClient blobClientMock;

    @Mock
    RetrofitError retrofitError;

    @Mock
    TypedByteArray typedByteArrayMock;

    @Mock
    File file;

    String PATH = "/storage/emulated/0/90.png";

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponant(appComponantMock);
        blobDataSender = new BlobDataSender();
        blobDataSender.uCoreAdapter = this.uCoreAdapterMock;
        blobDataSender.uCoreAccessProvider = this.uCoreAccessProviderMock;
        blobDataSender.gsonConverter = this.gsonConverterMock;
        blobDataSender.dataCreator = this.dataCreatorMock;
        blobDataSender.eventing = this.eventingMock;
    }

    @Test
    public void should_sendDataToBackend_server_returns_null(){
        //Given
        List blobList = new ArrayList();
        BlobData blobData = new BlobData();
        blobData.setBlobRequestListener(blobRequestListenerMock);
        blobData.setFile(file);
        blobData.setType("image/png");
        blobList.add(blobData);

        //When
        when(uCoreAccessProviderMock.isLoggedIn()).thenReturn(true);
        when(uCoreAdapterMock.getAppFrameworkClient(BlobClient.class,uCoreAccessProviderMock.getAccessToken(),gsonConverterMock)).thenReturn(blobClientMock);
        blobDataSender.sendDataToBackend(blobList);

        //Then
        TypedFile typedFile = new TypedFile("image/png",file);
        verify(blobClientMock).uploadBlob(typedFile);
    }

    @Test
    public void should_sendDataToBackend_return_success(){
        //Given
        TypedFile typedFile = new TypedFile("image/png",file);
        UcoreBlobResponse response = new UcoreBlobResponse();
        response.setItemId("1234");
        List blobList = new ArrayList();
        BlobData blobData = new BlobData();
        blobData.setBlobRequestListener(blobRequestListenerMock);
        blobData.setFile(file);
        blobData.setType("image/png");
        blobList.add(blobData);

        //When
        when(uCoreAccessProviderMock.isLoggedIn()).thenReturn(true);
        when(uCoreAdapterMock.getAppFrameworkClient(BlobClient.class,uCoreAccessProviderMock.getAccessToken(),gsonConverterMock)).thenReturn(blobClientMock);
        when(blobClientMock.uploadBlob(typedFile)).thenReturn(response);
        when(dataCreatorMock.createBlobMetaData()).thenReturn(blobDataMock);
        blobDataSender.sendDataToBackend(blobList);

        //Then
        verify(blobClientMock).uploadBlob(typedFile);
    }

    @Test
    public void should_sendDataToBackend_return_if_user_not_logged_in(){
        //Given
        List blobList = new ArrayList();
        BlobData blobData = new BlobData();
        blobData.setBlobRequestListener(blobRequestListenerMock);
        blobData.setFile(file);
        blobData.setType("image/png");
        blobList.add(blobData);

        //When
        when(uCoreAccessProviderMock.isLoggedIn()).thenReturn(false);
        when(uCoreAdapterMock.getAppFrameworkClient(BlobClient.class,uCoreAccessProviderMock.getAccessToken(),gsonConverterMock)).thenReturn(blobClientMock);
        blobDataSender.sendDataToBackend(blobList);

        //Then
        verifyNoMoreInteractions(blobClientMock);
    }

    //This API is not used hence not verifying
    @Test
    public void should_getClassForSyncData_tested(){
        blobDataSender.getClassForSyncData();
    }

    @Test
    public void should_sendDataToBackend_fail_with_retrofit_error() throws Exception {

        //Given
        TypedFile typedFile = new TypedFile("image/png",file);
        List blobList = new ArrayList();
        BlobData blobData = new BlobData();
        blobData.setBlobRequestListener(blobRequestListenerMock);
        blobData.setFile(file);
        blobData.setType("image/png");
        blobList.add(blobData);

        String string = "not able to connect";
        ArrayList<Header> headers = new ArrayList<>();
        headers.add(new Header("test", "test"));

        when((typedByteArrayMock).getBytes()).thenReturn(string.getBytes());
        Response response = new Response("http://localhost", 403, string, headers, typedByteArrayMock);
        when(retrofitError.getResponse()).thenReturn(response);

        when(uCoreAccessProviderMock.isLoggedIn()).thenReturn(true);
        when(uCoreAdapterMock.getAppFrameworkClient(BlobClient.class,uCoreAccessProviderMock.getAccessToken(),gsonConverterMock)).thenReturn(blobClientMock);
        doThrow(retrofitError).when(blobClientMock).uploadBlob(typedFile);
        blobDataSender.sendDataToBackend(blobList);
    }
}

