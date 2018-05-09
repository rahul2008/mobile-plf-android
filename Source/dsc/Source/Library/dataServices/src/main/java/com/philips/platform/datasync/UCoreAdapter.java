/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.datasync;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.insights.InsightClient;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;
import com.squareup.okhttp.OkHttpClient;

import org.joda.time.DateTime;

import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.inject.Inject;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class UCoreAdapter {

    public static final int API_VERSION = 16;
    private static final String API_VERSION_CUSTOM_HEADER = "api-version";
    private static final String APP_AGENT_HEADER = "appAgent";
    private static final String APP_AGENT_HEADER_VALUE = "%s android %s, %s";
    private static final int RED_TIME_OUT = 1; //1 Minute
    private static final int CONNECTION_TIME_OUT = 1; //1 Minute

    private Context context;
    private String buildType;

    @Inject
    UserRegistrationInterface userRegistrationImpl;

    @NonNull
    protected OkHttpClient okHttpClient;

    @NonNull
    protected OkClientFactory okClientFactory;

    @NonNull
    private final RestAdapter.Builder restAdapterBuilder;

    @Inject
    public UCoreAdapter(
            @NonNull final OkClientFactory okClientFactory,
            @NonNull final RestAdapter.Builder restAdapterBuilder,
            @NonNull final Context context) {
        super();
        DataServicesManager.getInstance().getAppComponent().injectUCoreAdapter(this);
        this.okHttpClient = new OkHttpClient();
        this.okHttpClient.setReadTimeout(RED_TIME_OUT, TimeUnit.MINUTES);
        this.okHttpClient.setConnectTimeout(CONNECTION_TIME_OUT, TimeUnit.MINUTES);
        this.okClientFactory = okClientFactory;
        this.restAdapterBuilder = restAdapterBuilder;
        this.context = context;
    }

    public <T> T getAppFrameworkClient(Class<T> clientClass, @NonNull final String accessToken, GsonConverter gsonConverter) {
        String url;
        if (clientClass == InsightClient.class)
            url = DataServicesManager.getInstance().fetchCoachingServiceUrlFromServiceDiscovery();
        else
            url = DataServicesManager.getInstance().fetchBaseUrlFromServiceDiscovery();

        if (url == null || url.isEmpty()) {
            return null;
        }

        return getClient(clientClass, url, accessToken, gsonConverter);
    }

    public <T> T getClient(final Class<T> clientClass, @NonNull final String baseUrl,
                           @NonNull final String accessToken, @NonNull GsonConverter gsonConverter) {
        OkClient okClient = okClientFactory.create(okHttpClient);

        if (baseUrl == null) return null;
        RestAdapter restAdapter = restAdapterBuilder
                .setEndpoint(baseUrl)
                .setRequestInterceptor(getRequestInterceptor(accessToken))
                .setClient(okClient)
                .setConverter(gsonConverter)
                .build();

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
            //Debug Log
        }

        return String.format(APP_AGENT_HEADER_VALUE, versionName, buildType, getBuildTime());
    }

    protected String getBuildTime() {
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
