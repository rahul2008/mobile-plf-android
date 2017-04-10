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
    void upload(@Part("myfile") TypedFile file,
                @Part("description") String description,
                Callback<String> cb);*/

    /*@POST("/blob/item")
    UcoreBlobResponse upload(@Body TypedFile file,
                Callback<String> cb);*/

    @POST("/blob/item")
    UcoreBlobResponse upload(@Body TypedFile file);

    @GET("/blob/item/{itemId}/metadata")
    UcoreBlobMetaData fetchMetaData(@Path("itemId") String itemID);
}
