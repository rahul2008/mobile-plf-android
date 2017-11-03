/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.moments;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

interface MomentsClient {

    @GET("/api/users/{userId}/moments/_history")
    UCoreMomentsHistory getMomentsHistory(@Header("performerId") String performerId,
                                          @Path("userId") String userId,
                                          @Query(value = "_since", encodeValue = false) String timestamp
    );

    @GET("/api/users/{userId}/moments/_query")
    UCoreMomentsHistory fetchMomentByDateRange(@Header("performerId") String performerId,
                                               @Path("userId") String userId,
                                               @Query(value = "timestampStart", encodeValue = false) String startTimeStamp,
                                               @Query(value = "timestampEnd", encodeValue = false) String endTimeStamp,
                                               @Query(value = "lastModifiedStart", encodeValue = false) String lastModifiedStartTimeStamp,
                                               @Query(value = "lastModifiedEnd", encodeValue = false) String lastModifiedEndTimeStamp,
                                               @Query(value = "limit") int limit
    );

    @POST("/api/users/{subjectId}/moments")
    UCoreMomentSaveResponse saveMoment(@Path("subjectId") String subjectId,
                                       @Header("performerId") String userId,
                                       @Body com.philips.platform.datasync.moments.UCoreMoment uCoreMoment);

    @PUT("/api/users/{userId}/moments/{momentId}")
    Response updateMoment(@Path("userId") String userId,
                          @Path("momentId") String momentId,
                          @Header("performerId") String performerId,
                          @Body com.philips.platform.datasync.moments.UCoreMoment uCoreMoment);

    @DELETE("/api/users/{userId}/moments/{momentId}")
    Response deleteMoment(@Path("userId") String userId,
                          @Path("momentId") String momentId,
                          @Header("performerId") String performerId);
}