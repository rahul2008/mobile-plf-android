package com.philips.platform.datasync.subjectProfile;

import com.philips.platform.datasync.PushNotification.UCorePushNotification;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;

public interface SubjectProfileClient {

    @POST("/api/subject")
    Response createSubjectProfile(@Header("performerId") String userId,
                                  @Body UCoreCreateSubjectProfileRequest uCoreCreateSubjectProfileRequest);

    @GET("/api/subject/{subject_id}")
    UCoreSubjectProfile getSubjectProfile(@Header("performerId") String userId,
                                          @Path("subject_id") String subjectID);

    @GET("/api/subject")
    UCoreSubjectProfileList getSubjectProfiles(@Header("performerId") String userId);

    @DELETE("/api/subject/{subject_id}")
    Response deleteSubjectProfile(@Header("performerId") String userId,
                                  @Path("subject_id") String subjectID);
}
