package com.philips.platform.datasync.devicePairing;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface DevicePairingClient {
    @POST("/api/users/{performer_id}/pairing")
    Response pairDevice(@Header("performerId") String userId,
                        @Header("api-version") int apiVersion,
                        @Path("performer_id") String performerId,
                        @Body UCoreDevicePair uCoreDevicePair);

    @DELETE("/api/users/{performer_id}/pairing")
    Response unPairDevice(@Header("performerId") String userId,
                          @Header("api-version") int apiVersion,
                          @Path("performer_id") String performerId,
                          @Query(value = "deviceId") String deviceId);

    @GET("/api/users/{performer_id}/pairing")
    List<String> getPairedDevices(@Header("performerId") String userId,
                                  @Header("api-version") int apiVersion,
                                  @Path("performer_id") String performerId);
}
