/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.ChecksumGenerator;
import com.philips.platform.datasync.blob.BlobClient;
import com.philips.platform.datasync.blob.BlobData;
import com.philips.platform.datasync.insights.InsightClient;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;
import com.squareup.okhttp.OkHttpClient;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.inject.Inject;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class UCoreAdapter {

    public static final RestAdapter.LogLevel LOG_LEVEL = RestAdapter.LogLevel.FULL;
    public static final int API_VERSION = 9;
    public static final int API_VERSION_BLOB = 1;
    public static final String API_VERSION_CUSTOM_HEADER = "api-version";
    public static final String APP_AGENT_HEADER = "appAgent";
    public static final String APP_AGENT_HEADER_VALUE = "%s android %s, %s";
    private static final int RED_TIME_OUT = 1; //1 Minute
    private static final int CONNECTION_TIME_OUT = 1; //1 Minute

    @Inject
    UserRegistrationInterface userRegistrationImpl;

    @NonNull
    private final OkHttpClient okHttpClient;
    private Context context;
    private String buildType;

    BlobData blobData;

    @NonNull
    private final OkClientFactory okClientFactory;

    @NonNull
    private final RestAdapter.Builder restAdapterBuilder;

    @Inject
    UCoreAccessProvider accessProvider;

    @Inject
    public UCoreAdapter(
            @NonNull final OkClientFactory okClientFactory,
            @NonNull final RestAdapter.Builder restAdapterBuilder,
            @NonNull final Context context) {
        super();
        DataServicesManager.getInstance().getAppComponant().injectUCoreAdapter(this);
        this.okHttpClient = new OkHttpClient();
        this.okHttpClient.setReadTimeout(RED_TIME_OUT, TimeUnit.MINUTES);
        this.okHttpClient.setConnectTimeout(CONNECTION_TIME_OUT, TimeUnit.MINUTES);
        this.okClientFactory = okClientFactory;
        this.restAdapterBuilder = restAdapterBuilder;
        this.context = context;
    }

    public void setBlobData(BlobData blobData) {
        this.blobData = blobData;
    }

    public <T> T getAppFrameworkClient(Class<T> clientClass, @NonNull final String accessToken, GsonConverter gsonConverter) {

        String url =null;

        if (userRegistrationImpl != null) {

            if (clientClass == InsightClient.class) {
                url = DataServicesManager.getInstance().fetchCoachingServiceUrlFromServiceDiscovery();
            }
            else if (clientClass == BlobClient.class) {
                url = BlobClient.BASE_URL;
            }
            else
                url = DataServicesManager.getInstance().fetchBaseUrlFromServiceDiscovery();

        }

        if (url == null || url.isEmpty()) {
            return null;
        }

        return getClient(clientClass, url, accessToken, gsonConverter);
    }

    void getUrl(int a){
        switch (a){

        }
    }
    public <T> T getClient(final Class<T> clientClass, @NonNull final String baseUrl,
                           @NonNull final String accessToken, @NonNull GsonConverter gsonConverter) {
        OkClient okClient = okClientFactory.create(okHttpClient);

        RequestInterceptor requestInterceptor;

        if (clientClass == BlobClient.class) {
            requestInterceptor = getRequestInterceptorForBlob(accessToken);

        } else {
            requestInterceptor = getRequestInterceptor(accessToken);
        }
        RestAdapter restAdapter = restAdapterBuilder
                .setEndpoint(baseUrl)
                .setRequestInterceptor(requestInterceptor)
                .setClient(okClient)
                .setConverter(gsonConverter)
                .build();

        restAdapter.setLogLevel(LOG_LEVEL);

        return restAdapter.create(clientClass);
    }

    @NonNull
    private RequestInterceptor getRequestInterceptor(final @NonNull String accessToken) {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Content-Type", "application/json");
                request.addHeader("Authorization", "bearer " + accessToken);
                request.addHeader(API_VERSION_CUSTOM_HEADER, String.valueOf(API_VERSION));
                request.addHeader(APP_AGENT_HEADER, getAppAgentHeader());
            }
        };
    }

    @NonNull
    private RequestInterceptor getRequestInterceptorForBlob(final @NonNull String accessToken) {
        final ChecksumGenerator checksum = new ChecksumGenerator();
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                if (blobData != null && blobData.getType() != null)
                    request.addHeader("Content-Type", blobData.getType());
                else
                    request.addHeader("Content-Type", "application/pdf");
                request.addHeader("Authorization", "bearer " + accessToken);
                request.addHeader(API_VERSION_CUSTOM_HEADER, String.valueOf(API_VERSION_BLOB));
                request.addHeader(APP_AGENT_HEADER, getAppAgentHeader());
                request.addHeader("user", accessProvider.getSubjectId());
                try {
                    if(blobData!=null)
                    request.addHeader("Content-MD5",checksum.getMd5OfFile(blobData.getFile()));
                } catch (Exception e) {

                }
            }
        };
    }

    public String getAppAgentHeader() {
        String versionName = "";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
            int indexOf = versionName.indexOf('-');
            if (indexOf != -1) {
                versionName = versionName.substring(0, indexOf);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return String.format(APP_AGENT_HEADER_VALUE, versionName, buildType, getBuildTime());
    }

    private String getBuildTime() {
        String buildTime;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            ZipFile zf = new ZipFile(ai.sourceDir);
            ZipEntry ze = zf.getEntry("META-INF/MANIFEST.MF");
            long time = ze.getTime();
            buildTime = new DateTime(time).toString();
            zf.close();
        } catch (Exception e) {
            buildTime = "unknown";
        }
        return buildTime;
    }
}
