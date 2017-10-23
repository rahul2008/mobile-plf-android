/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.datasync.insights;

import retrofit.client.Response;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;
import retrofit.http.Query;

public interface InsightClient {

    @GET("/api/users/{performer_id}/insights/_history")
    UCoreInsightList fetchInsights(@Path("performer_id") String performerId,
                                   @Header("performerId") String userId,
                                   @Header("api_version") int apiVersion,
                                   @Query(value = "_since", encodeValue = false) String timestamp);

    @DELETE("/api/users/{performer_id}/insights/{insight_id}")
    Response deleteInsight(@Path("performer_id") String performerId,
                           @Path("insight_id") String insightID,
                           @Header("performerId") String userID);
}