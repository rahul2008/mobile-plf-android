/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.insights;

import java.util.List;

import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;
import retrofit.http.Query;


public interface InsightClient {

    @GET("/api/users/{performer_id}/insights/_history")
    List<UCoreInsight> fetchInsights(@Path("performer_id") String performerId,
                                     @Header("performerId") String userId,
                                     @Header("api_version") int apiVersion,
                                     @Query(value = "_since", encodeValue = false) String timestamp);

    @DELETE("/api/users/{performer_id}/insights/{insight_id}")
    void deleteInsight(@Path("performer_id") String performerId,
                       @Path("insight_id") String insightID,
                       @Header("performerId") String userID);
}