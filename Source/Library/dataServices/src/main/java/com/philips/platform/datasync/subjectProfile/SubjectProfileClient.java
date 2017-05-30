package com.philips.platform.datasync.subjectProfile;

import com.philips.platform.datasync.PushNotification.UCorePushNotification;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface SubjectProfileClient {

    @POST("/api/subject")
    Response createSubjectProfile(@Body UCoreCreateSubjectProfileRequest uCoreCreateSubjectProfileRequest);

    @GET("/api/subject/{subject_id}")
    UCoreSubjectProfile getSubjectProfile(@Path("subject_id") String subjectID);

    @GET("/api/subject")
    UCoreSubjectProfileList getSubjectProfiles();

    @DELETE("/api/subject/{subject_id}")
    Response deleteSubjectProfile(@Path("subject_id") String subjectID);
}
