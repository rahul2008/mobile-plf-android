/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.moments;

import org.joda.time.DateTime;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

import static android.os.FileObserver.DELETE;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface MomentsClient {

    @GET("/api/users/{babyId}/moments/_history")
    com.philips.platform.datasync.moments.UCoreMomentsHistory getMomentsHistory(
            @Header("performerId") String userId,
            @Path("babyId") String babyId,
            @Query("_since") DateTime timestamp
    );

    @POST("/api/users/{subjectId}/moments")
    com.philips.platform.datasync.moments.UCoreMomentSaveResponse saveMoment(@Path("subjectId") String subjectId,
                                                                                  @Header("performerId") String userId,
                                                                                  @Body com.philips.platform.datasync.moments.UCoreMoment uCoreMoment);

    @PUT("/api/users/{babyId}/moments/{momentId}")
    Response updateMoment(@Path("babyId") String babyId,
                          @Path("momentId") String momentId,
                          @Header("performerId") String userId,
                          @Body com.philips.platform.datasync.moments.UCoreMoment uCoreMoment);

    @DELETE("/api/users/{babyId}/moments/{momentId}")
    Response deleteMoment(@Path("babyId") String babyId,
                          @Path("momentId") String momentId,
                          @Header("performerId") String userId);

}