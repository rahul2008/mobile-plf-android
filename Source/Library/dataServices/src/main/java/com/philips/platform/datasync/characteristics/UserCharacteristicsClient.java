package com.philips.platform.datasync.characteristics;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by indrajitkumar on 1/2/17.
 */

public interface UserCharacteristicsClient {
    @GET("/api/users/{performer_id}/characteristics")
    UCoreCharacteristics getUserCharacteristics(@Path("performer_id") String performerId,
                                               @Header("performerId") String userId, @Header("api-version") int apiVersion);

    @PUT("/api/users/{performer_id}/characteristics")
    Response putUserCharacteristics(@Path("performer_id") String performerId,
                                    @Header("performerId") String userId,
                                    @Body List<UCoreCharacteristics> uCoreCharacteristicsDetails,
                                    @Header("api-version") int apiVersion);
}