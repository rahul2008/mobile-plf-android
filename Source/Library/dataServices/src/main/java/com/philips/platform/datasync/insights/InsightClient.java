package com.philips.platform.datasync.insights;

import com.philips.platform.datasync.consent.UCoreConsentDetail;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface InsightClient {

    @POST("/api/consent")
    Void saveConsent(@Header("performerId") String userId,
                     @Body List<UCoreConsentDetail> consentDetailList);

    @GET("/api/consent")
    List<UCoreConsentDetail> getConsent(@Header("performerId") String userId,
                                        @Query("name") List<String> namesList,
                                        @Query("deviceIdentificationNumber") List<String> deviceIdentificationList,
                                        @Query("documentVersion") List<String> documentVersionList);
}