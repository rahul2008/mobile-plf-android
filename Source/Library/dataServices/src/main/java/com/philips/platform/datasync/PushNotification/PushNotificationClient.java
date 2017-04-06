package com.philips.platform.datasync.PushNotification;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface PushNotificationClient {

    @POST("/api/users/{performer_id}/push_notifications/registration")
    Response registerDeviceToken(@Path("performer_id") String performerId,
                                 @Header("performerId") String userId,
                                 @Header("api-version") int apiVersion,
                                 @Body UCorePushNotification uCorePushNotification);

    @DELETE("/api/users/{performer_id}/push_notifications/registration")
    Response unRegisterDeviceToken(@Path("performer_id") String performerId,
                                   @Header("performerId") String userID,
                                   @Header("api-version") int apiVersion,
                                   @Query(value = "appVariant") String appVariant,
                                   @Query(value = "token") String appToken);
}
