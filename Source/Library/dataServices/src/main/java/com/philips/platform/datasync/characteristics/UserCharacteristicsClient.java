/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.characteristics;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface UserCharacteristicsClient {
    @GET("/api/users/{performer_id}/characteristics")
    UCoreUserCharacteristics getUserCharacteristics(@Path("performer_id") String performerId,
                                                    @Header("performerId") String userId, @Header("api-version") int apiVersion);

    @PUT("/api/users/{performer_id}/characteristics")
    Response createOrUpdateUserCharacteristics(@Path("performer_id") String performerId,
                                               @Header("performerId") String userId,
                                               @Body UCoreUserCharacteristics uCoreCharacteristics,
                                               @Header("api-version") int apiVersion);
}