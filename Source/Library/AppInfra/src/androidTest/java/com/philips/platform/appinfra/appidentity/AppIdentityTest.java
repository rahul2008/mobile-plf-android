package com.philips.platform.appinfra.appidentity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.R;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by 310238114 on 6/22/2016.
 */
public class AppIdentityTest extends MockitoTestCase {

    AppIdentityInterface mAppIdentityManager = null;
    // Context context = Mockito.mock(Context.class);

    private Context context;
    AppInfra mAppInfra;

    List<String> mServiceDiscoveryEnv = Arrays.asList("TEST", "STAGING", "ACCEPTANCE", "PRODUCTION");
    List<String> mAppStateValues = Arrays.asList("DEVELOPMENT", "TEST", "STAGING", "ACCEPTANCE", "PRODUCTION");
    private List<String> mSectorValues = Arrays.asList("b2b", "b2c", "b2b_Li", "b2b_HC");

    private AppIdentityManager appIdentity;
    private AppConfigurationInterface.AppConfigurationError configError;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
        mAppIdentityManager = new AppIdentityManager(mAppInfra);
        assertNotNull(mAppIdentityManager);
        configError = new AppConfigurationInterface
                .AppConfigurationError();

        assertNotNull(configError);
    }

    public void testHappyPath() throws Exception {
        assertNotNull(mAppIdentityManager.getLocalizedAppName());
        assertNotNull(mAppIdentityManager.getAppName());
        assertNotNull(mAppIdentityManager.getAppVersion());
//        assertNotNull(mAppIdentityManager.getMicrositeId());
//        assertNotNull(mAppIdentityManager.getSector());
   //     assertNotNull(mAppIdentityManager.getServiceDiscoveryEnvironment());
     //   assertNotNull(mAppIdentityManager.getAppState());
    }


    public void testgetAppVersion() {
        assertNotNull(mAppIdentityManager.getAppVersion());
        //assertEquals("Appversion is in proper format", mAppIdentityManager.getAppVersion(), "1.1.0");
        assertNotSame("Appversion is not in proper format", mAppIdentityManager.getAppVersion(), "!!2.0");

    }

    public void testValidateAppState() {
//        String appState;
//
//        mAppInfra.getConfigInterface().setPropertyForKey("AppState", "appinfra_appidentity",
//                "Staging", configError);
//        String defAppState = (String) mAppInfra.getConfigInterface().getDefaultPropertyForKey
//                ("AppState", "appinfra_appidentity", configError);
//        assertNotNull(defAppState);
//        assertEquals("Appstate is staging", defAppState, "Staging");
//
//        if (defAppState.equalsIgnoreCase("production")) // allow manual override only if static appstate != production
//            appState = defAppState;
//        else {
//            Object dynAppState = mAppInfra.getConfigInterface().getPropertyForKey("AppState", "appinfra_appidentity", configError);
//            if (dynAppState != null)
//                appState = dynAppState.toString();
//            else
//                appState = defAppState;
//        }
//        assertNotNull(appState);

        Set<String> set;

        try {
            set = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
            if (mAppIdentityManager.getAppState() != null &&
                    !mAppIdentityManager.getAppState().toString().isEmpty()) {
                set.addAll(mAppStateValues);
                if (!set.contains(mAppIdentityManager.getAppState().toString())) {
                    ///mAppState = null;
                    throw new IllegalArgumentException("\"App State in appIdentityConfig  file must" +
                            " match one of the following values \\\\n TEST,\\\\n DEVELOPMENT,\\\\n " +
                            "STAGING, \\\\n ACCEPTANCE, \\\\n PRODUCTION\"");
                }
            } else {
                throw new IllegalArgumentException("AppState cannot be empty in appIdentityConfig json file");
            }

        } catch (IllegalArgumentException error) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppIdentity exception",
                    Log.getStackTraceString(error));
        }
    }


    public void testValidateServiceDiscoveryEnv() {
//        String servicediscoveryEnv;
//
//        mAppInfra.getConfigInterface().setPropertyForKey("AppState", "appinfra_appidentity",
//                "Staging", configError);
//
//        String defSevicediscoveryEnv = (String) mAppInfra.getConfigInterface().getDefaultPropertyForKey
//                ("ServiceDiscoveryEnvironment", "appinfra_appidentity", configError);
//
//        assertNotNull(defSevicediscoveryEnv);
//        assertEquals("Appstate is staging", defSevicediscoveryEnv, "Staging");
//
//        if (defSevicediscoveryEnv.equalsIgnoreCase("production")) // allow manual override only if static appstate != production
//            servicediscoveryEnv = defSevicediscoveryEnv;
//        else {
//            Object dynServiceDiscoveryEnvironment = mAppInfra.getConfigInterface()
//                    .getPropertyForKey("ServiceDiscoveryEnvironment", "appinfra_appidentity", configError);
//            if (dynServiceDiscoveryEnvironment != null)
//                servicediscoveryEnv = dynServiceDiscoveryEnvironment.toString();
//            else
//                servicediscoveryEnv = defSevicediscoveryEnv;
//        }
//
//        assertNotNull(servicediscoveryEnv);

        Set<String> set;

        try {
            set = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
            if (mAppIdentityManager.getServiceDiscoveryEnvironment() != null &&
                    !mAppIdentityManager.getServiceDiscoveryEnvironment().isEmpty()) {
                set.addAll(mServiceDiscoveryEnv);
                if (!set.contains(mAppIdentityManager.getServiceDiscoveryEnvironment())) {
                    throw new IllegalArgumentException("\"servicediscoveryENV in appIdentityConfig  file must match \" +\n" +
                            "                            \"one of the following values \\n TEST,\\n STAGING, \\n ACCEPTANCE, \\n PRODUCTION\"");
                }
            } else {
                throw new IllegalArgumentException("ServiceDiscovery Environment cannot be empty in appIdentityConfig json file");
            }
        } catch (IllegalArgumentException error) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppIdentity exception",
                    Log.getStackTraceString(error));
        }


    }

    public void testAppversion() {
        try {
            if (mAppIdentityManager.getAppVersion() != null && !mAppIdentityManager.getAppVersion()
                    .isEmpty()) {
                if (!mAppIdentityManager.getAppVersion().matches("[0-9]+\\.[0-9]+\\.[0-9]+([_-].*)?")) {
                    throw new IllegalArgumentException("AppVersion should in this format \" [0-9]+\\\\.[0-9]+\\\\.[0-9]+([_-].*)?]\" ");
                }
            } else {
                throw new IllegalArgumentException("Appversion cannot be null");
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }


    }

    public void testValidateMicroSiteId() {
        try {
            if (mAppIdentityManager.getMicrositeId() != null && !mAppIdentityManager.getMicrositeId().isEmpty()) {
                if (!mAppIdentityManager.getMicrositeId().matches("[a-zA-Z0-9_.-]+")) {
                    throw new IllegalArgumentException("\"micrositeId must not contain special charectors in appIdentityConfig json file\"");
                }
            } else {
                throw new IllegalArgumentException("micrositeId cannot be empty in appIdentityConfig  file");
            }
        } catch (IllegalArgumentException error) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppIdentity exception",
                    Log.getStackTraceString(error));
        }
    }

    public void testValidateSector() {
        Set<String> set;
//        String sector = (String) mAppInfra.getConfigInterface().getDefaultPropertyForKey
//                ("sector", "appinfra_appidentity", configError);
//        assertNotNull(sector);

        try {
            set = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
            if (mAppIdentityManager.getSector() != null && !mAppIdentityManager.getSector().isEmpty()) {
                set.addAll(mSectorValues);
                if (!set.contains(mAppIdentityManager.getSector())) {
                    throw new IllegalArgumentException("\"Sector in appIdentityConfig  file must match one of the following values\" +\n" +
                            "                            \" \\\\n b2b,\\\\n b2c,\\\\n b2b_Li, \\\\n b2b_HC\"");
                }
            } else {
                throw new IllegalArgumentException("\"App Sector cannot be empty in appIdentityConfig json file\"");
            }

        } catch (IllegalArgumentException error) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppIdentity exception",
                    Log.getStackTraceString(error));

        }
    }

}
