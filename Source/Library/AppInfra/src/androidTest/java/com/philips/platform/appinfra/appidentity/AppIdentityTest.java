package com.philips.platform.appinfra.appidentity;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;

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
                        JSONObject obj = new JSONObject(json);
                        micrositeId = obj.getString("micrositeId");
                        sector = obj.getString("sector");
                        mServiceDiscoveryEnvironment = obj.getString("ServiceDiscoveryEnvironment");
                        mappState = obj.getString("AppState");


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
//        assertNotNull(mAppIdentityManager.getLocalizedAppName());
//        assertNotNull(mAppIdentityManager.getAppName());
//        assertNotNull(mAppIdentityManager.getAppState());
//        assertNotNull(mAppIdentityManager.getAppVersion());
//        assertNotNull(mAppIdentityManager.getMicrositeId());
//        assertNotNull(mAppIdentityManager.getSector());

        assertNotNull(mAppIdentityManager.getLocalizedAppName());
        assertNotNull(mAppIdentityManager.getAppName());
        assertNotNull(mappState);
        assertNotNull(mAppIdentityManager.getAppVersion());
        assertNotNull(mAppIdentityManager.getMicrositeId());
        assertNotNull(mAppIdentityManager.getSector());
    }
}
