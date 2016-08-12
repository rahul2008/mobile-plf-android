package com.philips.platform.appinfra.appidentity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 310238114 on 6/22/2016.
 */
public class AppIdentityTest extends MockitoTestCase {

    AppIdentityInterface mAppIdentityManager = null;
    // Context context = Mockito.mock(Context.class);

    private Context context;
    AppInfra mAppInfra;
    String mappState;
    String msector;
    String servicediscoveryEnv;
    String json;
    JSONObject obj;
    List<String> mServiceDiscoveryEnv = Arrays.asList("TEST", "STAGING", "ACCEPTANCE", "PRODUCTION");
    List<String> mAppStateValues = Arrays.asList("DEVELOPMENT", "TEST", "STAGING", "ACCEPTANCE", "PRODUCTION");
    AppIdentityManager appIdentity;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
        // mAppIdentityManager = mAppInfra.getAppIdentity();
        mAppIdentityManager = new AppIdentityManager(mAppInfra) {
            @Override
            protected String getJsonStringFromAsset() {

                json = "{\n" +
                        "    \"micrositeId\" : \"77000\",\n" +
                        "    \"sector\"  : \"B2C\",\n" +
                        "    \"AppState\"  : \"PRODUCTION\",\n" +
                        "    \"ServiceDiscoveryEnvironment\"  : \"PRODUCTION\"\n" +
                        "}";

                return json;

            }


        };
        appIdentity = new AppIdentityManager(mAppInfra);
        String json = appIdentity.getJsonStringFromAsset();
        obj = new JSONObject(json);
        assertNotNull(mAppIdentityManager);
    }

    public void testHappyPath() throws Exception {

        assertNotNull(mAppIdentityManager.getLocalizedAppName());
        assertNotNull(mAppIdentityManager.getAppName());
        assertNotNull(mAppIdentityManager.getAppVersion());
        assertNotNull(mAppIdentityManager.getMicrositeId());
        assertNotNull(mAppIdentityManager.getSector());
        assertNotNull(mAppIdentityManager.getServiceDiscoveryEnvironment());
        assertNotNull(mAppIdentityManager.getAppState());
    }

    public void testgetMicrositeId() {

        assertEquals("micrositeId is equal", mAppIdentityManager.getMicrositeId(), "77000");
        assertNotSame("micrositeId doesnt match", mAppIdentityManager.getMicrositeId(), "@3434");
    }

    public void testgetSector() {
        assertEquals("Sector matches ", mAppIdentityManager.getSector(), "B2C");
        assertNotSame("Sector doesnt match ", mAppIdentityManager.getSector(), "@@B2C");
        try {
            obj.putOpt("sector", "b2b");
            msector = obj.getString("sector");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        assertEquals("Sector matches ", msector, "b2b");
        assertNotSame("Sector doesnt match ", msector, "@@B2C");

        try {
            obj.putOpt("sector", "b2b_Li");
            msector = obj.getString("sector");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        assertEquals("Sector matches ", msector, "b2b_Li");
        assertNotSame("Sector doesnt match ", msector, "@@safd");


    }

    public void testgetAppState() {
        assertEquals("Appstate is equal", mAppIdentityManager.getAppState().toString(), "PRODUCTION");
        assertNotSame("Appstate doesnt match", mAppIdentityManager.getAppState(), "PROD");
        try {
            obj.put("AppState", "TEST");
            mappState = obj.getString("AppState");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        assertEquals("Appstate is equal", mappState, "TEST");
        assertNotSame("Appstate is not equal", mappState, "#FAF");

        try {
            obj.put("AppState", "STAGING");
            mappState = obj.getString("AppState");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        assertEquals("Appstate is equal", mappState, "STAGING");
        assertNotSame("Appstate is not equal", mappState, "STAG");

        try {
            obj.put("AppState", "ACCEPTANCE");
            mappState = obj.getString("AppState");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        assertEquals("Appstate is equal", mappState, "ACCEPTANCE");
        if (mAppStateValues.contains(mappState)) {
            mappState = "acceptance";
        }
        assertEquals("AppState is equal", mappState, "acceptance");
        assertNotSame("Appstate is not equal", mappState, "#@@ACCEP");
    }

    public void testServiceDiscoveryEnv() {
        assertEquals("Service Env is equal ", mAppIdentityManager.getServiceDiscoveryEnvironment(), "PRODUCTION");
        assertNotSame("Service Env doesnt match", mAppIdentityManager.getServiceDiscoveryEnvironment(), "PROD");

        try {
            obj.put("ServiceDiscoveryEnvironment", "TEST");
            servicediscoveryEnv = obj.getString("ServiceDiscoveryEnvironment");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        assertEquals("ServiceDiscovery Environment is equal", servicediscoveryEnv, "TEST");
        assertNotSame("ServiceDiscovery Environment is not equal", servicediscoveryEnv, "#FAF");


        try {
            obj.put("ServiceDiscoveryEnvironment", "STAGING");
            servicediscoveryEnv = obj.getString("ServiceDiscoveryEnvironment");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        assertEquals("ServiceDiscovery Environment is equal", servicediscoveryEnv, "STAGING");
        assertNotSame("ServiceDiscovery Environment is not equal", servicediscoveryEnv, "STAG");

        try {
            obj.put("ServiceDiscoveryEnvironment", "ACCEPTANCE");
            servicediscoveryEnv = obj.getString("ServiceDiscoveryEnvironment");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        assertEquals("ServiceDiscovery Environment is equal", servicediscoveryEnv, "ACCEPTANCE");
        if (mServiceDiscoveryEnv.contains(servicediscoveryEnv)) {
            servicediscoveryEnv = "acceptance";
        }
        assertEquals("ServiceDiscovery Environment is equal", servicediscoveryEnv, "acceptance");
        assertNotSame("ServiceDiscovery Environment is not equal", servicediscoveryEnv, "#@@ACCEP");

    }

    public void testgetAppVersion() {
        assertNotNull(mAppIdentityManager.getAppVersion());
        //assertEquals("Appversion is in proper format", mAppIdentityManager.getAppVersion(), "1.1.0");
        // assertNotSame("Appversion is not in proper format" , appverion ,"!!2.0");
    }


}
