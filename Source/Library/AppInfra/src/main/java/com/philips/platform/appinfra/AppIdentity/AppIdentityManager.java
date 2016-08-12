/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.appidentity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.R;
import com.philips.platform.appinfra.logging.LoggingInterface;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * The type App identity manager.
 */
public class AppIdentityManager implements AppIdentityInterface {

    private AppInfra mAppInfra;
    private Context context;

    private String mAppName;
    private String mAppVersion;
    private String mServiceDiscoveryEnvironment;
    private String mLocalizedAppName;
    private String micrositeId;
    private String sector;
    private String mAppState;

    private List<String> mSectorValues = Arrays.asList("b2b", "b2c", "b2b_Li", "b2b_HC");
    private List<String> mServiceDiscoveryEnv = Arrays.asList("TEST", "STAGING", "ACCEPTANCE", "PRODUCTION");
    private List<String> mAppStateValues = Arrays.asList("DEVELOPMENT", "TEST", "STAGING", "ACCEPTANCE", "PRODUCTION");


    @Override
    public String getAppName() {
        return mAppName;
    }


    @Override
    public String getAppVersion() {
        return mAppVersion;
    }

    @Override
    public AppState getAppState() {
        AppState mAppStateEnum = null;
        if (mAppState.equalsIgnoreCase("DEVELOPMENT")) {
            mAppStateEnum = AppState.DEVELOPMENT;
        }
        if (mAppState.equalsIgnoreCase("TEST")) {
            mAppStateEnum = AppState.TEST;
        }
        if (mAppState.equalsIgnoreCase("STAGING")) {
            mAppStateEnum = AppState.STAGING;
        }
        if (mAppState.equalsIgnoreCase("ACCEPTANCE")) {
            mAppStateEnum = AppState.ACCEPTANCE;
        }
        if (mAppState.equalsIgnoreCase("PRODUCTION")) {
            mAppStateEnum = AppState.PRODUCTION;
        }
        if (mAppStateEnum != null) {
            return mAppStateEnum;
        }

        return mAppStateEnum;
    }

    @Override
    public String getServiceDiscoveryEnvironment() {
        return mServiceDiscoveryEnvironment;
    }


    @Override
    public String getLocalizedAppName() {
        return mLocalizedAppName;
    }


    @Override
    public String getMicrositeId() {
        return micrositeId;
    }


    @Override
    public String getSector() {
        return sector;
    }

    public AppIdentityManager(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        context = mAppInfra.getAppInfraContext();
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

        // Method Loads the json data and can be access through getters
        loadJSONFromAsset();

    }

    // Refactored to support unit test
    protected String getJsonStringFromAsset() {
        String json = null;
        InputStream is = null;
        try {
            is = context.getAssets().open("AppIdentity.json");
            final int size = is.available();
            final byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public String loadJSONFromAsset() {
        String json;
        try {
            json = getJsonStringFromAsset();
            if (json != null) {
                try {
                    final JSONObject obj = new JSONObject(json);
                    validateAppIdentity(obj);


                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "micrositeId", "" + getMicrositeId());
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "sector", "" + getSector());
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AppState", "" + getAppState());
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AppName", "" + getAppName());
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AppVersion", "" + getAppVersion());
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AppLocalizedNAme", "" + getLocalizedAppName());
                } catch (JSONException e) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppIdentity exception",
                            Log.getStackTraceString(e));                }
            }
        } catch (Exception ex) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppIdentity exception",
                    Log.getStackTraceString(ex));
            return null;
        }
        return json;

    }

    private void validateAppIdentity(JSONObject jsonObject) throws JSONException {
        micrositeId = jsonObject.getString("micrositeId");
        sector = jsonObject.getString("sector");
        mServiceDiscoveryEnvironment = jsonObject.getString("ServiceDiscoveryEnvironment");
        mAppState = jsonObject.getString("AppState");

        try {
            if (micrositeId != null && !micrositeId.isEmpty()) {
                if (!micrositeId.matches("[a-zA-Z0-9_.-]+")) {
                    micrositeId = null;
                    Assert.fail("\"micrositeId must not contain special charectors in appIdentityConfig json file\"");
                }
            } else {
                Assert.fail("micrositeId cannot be empty in appIdentityConfig  file");
            }
        } catch (AssertionError error) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppIdentity exception",
                    Log.getStackTraceString(error));

        }

        Set<String> set;
        try {
            set = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
            if (sector != null && !sector.isEmpty()) {
                set.addAll(mSectorValues);
                if (!set.contains(sector)) {
                    sector = null;
                    Assert.fail("\"Sector in appIdentityConfig  file must match one of the following values\" +\n" +
                            "                            \" \\\\n b2b,\\\\n b2c,\\\\n b2b_Li, \\\\n b2b_HC\"");

                }
            } else {
                Assert.fail("\"App Sector cannot be empty in appIdentityConfig json file\"");
            }

        } catch (AssertionError error) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppIdentity exception",
                    Log.getStackTraceString(error));
        }

        try {
            set = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
            if (mAppState != null && !mAppState.isEmpty()) {
                set.addAll(mAppStateValues);
                if (!set.contains(mAppState)) {
                    ///mAppState = null;
                    Assert.fail("\"App State in appIdentityConfig  file must match\" +\n" +
                            "                            \" one of the following values \\\\n TEST,\\\\n DEVELOPMENT,\\\\n STAGING, \\\\n ACCEPTANCE, \\\\n PRODUCTION\"");
                }
            } else {
                Assert.fail("AppState cannot be empty in appIdentityConfig json file");
            }

        } catch (AssertionError error) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppIdentity exception",
                    Log.getStackTraceString(error));
        }


        try {
            set = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
            if (mServiceDiscoveryEnvironment != null && !mServiceDiscoveryEnvironment.isEmpty()) {
                set.addAll(mServiceDiscoveryEnv);
                if (!set.contains(mServiceDiscoveryEnvironment)) {
                    mServiceDiscoveryEnvironment = null;
                    Assert.fail("\"servicediscoveryENV in appIdentityConfig  file must match \" +\n" +
                            "                            \"one of the following values \\n TEST,\\n STAGING, \\n ACCEPTANCE, \\n PRODUCTION\"");
                }
            } else {
                Assert.fail("ServiceDiscovery Environment cannot be empty in appIdentityConfig json file");
            }
        } catch (AssertionError error) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppIdentity exception",
                    Log.getStackTraceString(error));
        }


        try {
            PackageInfo pInfo;
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            mAppName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();

                        /* Vertical App should have this string defined for all supported language files
                        *  default <string name="localized_commercial_app_name">AppInfra DemoApp localized</string>
                        * */
            mLocalizedAppName = context.getResources().getString(R.string.localized_commercial_app_name);


            mAppVersion = String.valueOf(pInfo.versionName);
            if (mAppVersion != null && !mAppVersion.isEmpty()) {
                if (!mAppVersion.matches(" [0-9]+\\.[0-9]+\\.[0-9]+([_-].*)?]")) {
                    Assert.fail("AppVersion should in this format \" [0-9]+\\\\.[0-9]+\\\\.[0-9]+([_-].*)?]\" ");
                }
            } else {
                Assert.fail("Appversion cannot be null");
            }

        } catch (PackageManager.NameNotFoundException | AssertionError e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppIdentity exception",
                    Log.getStackTraceString(e));
        }
    }


}
