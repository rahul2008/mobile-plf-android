package com.philips.platform.datasync.blob;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

public interface BlobClient {
    String BASE_URL = "https://binaryhosting-dsstandalone.cloud.pcftest.com";

    // Keeping this commented as it will be required when multipart data support will be provided by backened
  /*  @Multipart
    @POST("/blob/item")
    void uploadBlob(@Part("myfile") TypedFile file,
                @Part("description") String description,
                Callback<String> cb);*/

    @POST("/blob/item")
    UcoreBlobResponse uploadBlob(@Body TypedFile file);

    @GET("/blob/item/{itemId}/metadata")
    UcoreBlobMetaData fetchMetaData(@Path("itemId") String itemID);

    @GET("/blob/item/{itemId}")
    Response downloadBlob(@Path("itemId") String itemId);
}
