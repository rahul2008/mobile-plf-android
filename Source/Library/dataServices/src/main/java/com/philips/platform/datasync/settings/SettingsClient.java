package com.philips.platform.datasync.settings;

import com.philips.platform.datasync.characteristics.UCoreUserCharacteristics;
import com.philips.platform.datasync.consent.UCoreConsentDetail;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface SettingsClient {

    @PUT("/api/users/{performer_id}/settings")
    Response updateSettings(
            @Path("performer_id") String performerId,
            @Header("performerId") String userId,
                     @Body UCoreSettings uCoreSettings);

    @GET("/api/users/{performer_id}/settings")
    UCoreSettings getSettings(@Path("performer_id") String performerId,
                                                    @Header("performerId") String userId, @Header("api-version") int apiVersion);
}