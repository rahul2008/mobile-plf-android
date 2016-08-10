package com.philips.platform.appinfra.appidentity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 310238114 on 6/22/2016.
 */
public class AppIdentityTest extends MockitoTestCase {

    AppIdentityInterface mAppIdentityManager = null;
    // Context context = Mockito.mock(Context.class);

    private Context context;
    AppInfra mAppInfra;
    String mappState;
    String micrositeid;
    String msector;
    String servicediscoveryEnv;
    String appname;
    String localizedappname;
    String appverion;
    JSONObject obj;


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

                String json = "{\n" +
                        "    \"micrositeId\" : \"77000\",\n" +
                        "    \"sector\"  : \"B2C\",\n" +
                        "    \"AppState\"  : \"PRODUCTION\",\n" +
                        "    \"ServiceDiscoveryEnvironment\"  : \"PRODUCTION\"\n" +
                        "}";

                return json;

            }

            @Override
            public String loadJSONFromAsset() {
                String json = getJsonStringFromAsset();
                if (json != null) {
                    try {
                        obj = new JSONObject(json);
                        micrositeid = obj.getString("micrositeId");
                        msector = obj.getString("sector");
                        servicediscoveryEnv = obj.getString("ServiceDiscoveryEnvironment");
                        mappState = obj.getString("AppState");


                        try {
                            PackageInfo pInfo;
                            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

                            appname = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();

                        /* Vertical App should have this string defined for all supported language files
                        *  default <string name="localized_commercial_app_name">AppInfra DemoApp localized</string>
                        * */
                            localizedappname = context.getResources().getString(R.string.localized_commercial_app_name);

                            appverion = String.valueOf(pInfo.versionName);

                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                return json;
            }
        };

        assertNotNull(mAppIdentityManager);
    }

    public void testHappyPath() throws Exception {
        assertNotNull(localizedappname);
        assertNotNull(appname);
        assertNotNull(appverion);
        assertNotNull(micrositeid);
        assertNotNull(msector);
        assertNotNull(servicediscoveryEnv);
        assertNotNull(mappState);
    }

    public void testMicrositeId() {
        assertEquals("micrositeId is equal", micrositeid, "77000");
        assertNotSame("micrositeId doesnt match", micrositeid, "@3434");
    }

    public void testSector() {
        assertEquals("Sector matches ", msector, "B2C");
        assertNotSame("Sector doesnt match ", msector, "@@B2C");
    }

    public void testAppState() {
        assertEquals("Appstate is equal", mappState, "PRODUCTION");
        assertNotSame("Appstate doesnt match", mappState, "PROD");

    }

    public void testServiceDiscoveryEnv() {
        assertEquals("Service Env is equal ", servicediscoveryEnv, "PRODUCTION");
        assertNotSame("Service Env doesnt match", mappState, "PROD");

    }

}
