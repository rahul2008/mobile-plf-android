package com.philips.platform.datasync.blob;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

public interface BlobClient {
    public static final String BASE_URL = "https://binaryhosting-dsstandalone.cloud.pcftest.com";

  /*  @Multipart
    @POST("/blob/item")
    void uploadBlob(@Part("myfile") TypedFile file,
                @Part("description") String description,
                Callback<String> cb);*/

    @POST("/blob/item")
    UcoreBlobResponse uploadBlob(@Body TypedFile file);

    @GET("/blob/item/{itemId}")
    void downloadBlob(@Path("itemId") String itemId);
}
