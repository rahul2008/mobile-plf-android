package com.philips.platform.appinfra.config;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import org.json.JSONObject;

/**
 * Created by 310238114 on 8/2/2016.
 */
public class ConfigTest extends MockitoTestCase {

    ConfigInterface mConfigInterface = null;

    private Context context;
    private AppInfra mAppInfra;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        //mConfigInterface = mAppInfra.getConfigInterface();

          // overriding ConfigManager to get Test JSON data, as AppInfra library does not have uApp configuration file
        mConfigInterface = new ConfigManager(mAppInfra){
            @Override
            protected JSONObject getMasterConfigFromApp() {
                JSONObject result = null;
                try {
                  /*  InputStream mInputStream = mContext.getAssets().open("configuration.json");
                    BufferedReader r = new BufferedReader(new InputStreamReader(mInputStream));
                    StringBuilder total = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        total.append(line).append('\n');
                    }
                    result = new JSONObject(total.toString());*/
                   String testJson ="{\n" +
                           "  \"UR\": {\n" +
                           "\n" +
                           "    \"Development\": \"ad7nn99y2mv5berw5jxewzagazafbyhu\",\n" +
                           "    \"Testing\": \"xru56jcnu3rpf8q7cgnkr7xtf9sh8pp7\",\n" +
                           "    \"Evaluation\": \"4r36zdbeycca933nufcknn2hnpsz6gxu\",\n" +
                           "    \"Staging\": \"f2stykcygm7enbwfw2u9fbg6h6syb8yd\",\n" +
                           "    \"Production\": \"mz6tg5rqrg4hjj3wfxfd92kjapsrdhy3\"\n" +
                           "\n" +
                           "  },\n" +
                           "  \"AI\": {\n" +
                           "    \"MicrositeID\": 77001,\n" +
                           "    \"RegistrationEnvironment\": \"Staging\",\n" +
                           "    \"NL\": [\"googleplus\", \"facebook\"  ],\n" +
                           "    \"US\": [\"facebook\",\"googleplus\" ],\n" +
                           "    \"EE\": [123,234 ]\n" +
                           "  }\n" +
                           "}";
                    result = new JSONObject(testJson);
                    SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
                    ssi.storeValueForKey(ConfigManager.uAPP_CONFIG_FILE, result.toString(), sse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

        };
        assertNotNull(mConfigInterface);

    }

    public void testGetPropertyForKey() throws Exception {
        ConfigInterface.ConfigError configError = new ConfigInterface.ConfigError();

        configError.setErrorCode(null);// reset error code to null
        assertNull(mConfigInterface.getPropertyForKey("", "", configError));//invalid Group and  invalid key
        assertEquals(ConfigInterface.ConfigError.ConfigErrorEnum.InvalidKey,configError.getErrorCode() );

        configError.setErrorCode(null);// reset error code to null
        assertNull(mConfigInterface.getPropertyForKey(null, null, configError));// invalid Group and  invalid key
        assertEquals(ConfigInterface.ConfigError.ConfigErrorEnum.InvalidKey,configError.getErrorCode() );

        configError.setErrorCode(null);// reset error code to null
        assertNull(mConfigInterface.getPropertyForKey("AI", null, configError)); //  Existing Group but invalid key
        assertEquals(ConfigInterface.ConfigError.ConfigErrorEnum.InvalidKey,configError.getErrorCode() );

        configError.setErrorCode(null);// reset error code to null
        assertNull( mConfigInterface.getPropertyForKey("incorrectGroupKey", "incorrectKey", configError)); // Non Existing Group  and Non Existing key
        assertEquals(ConfigInterface.ConfigError.ConfigErrorEnum.GroupNotExists,configError.getErrorCode() );

        configError.setErrorCode(null);// reset error code to null
        assertNull( mConfigInterface.getPropertyForKey("AI", "incorrectKey", configError)); //  Existing Group  but Non Existing key
        assertEquals(ConfigInterface.ConfigError.ConfigErrorEnum.KeyNotExists,configError.getErrorCode() );

        configError.setErrorCode(null);// reset error code to null
        assertNotNull(mConfigInterface.getPropertyForKey("AI", "RegistrationEnvironment", configError));//  Existing Group and  Existing key
        // make sure AI and MicrositeID exist in configuration file else this test case will fail
        assertEquals(null,configError.getErrorCode() ); // success

    }


    public void testSetPropertyForKey() throws Exception {
        ConfigInterface.ConfigError configError = new ConfigInterface.ConfigError();

        configError.setErrorCode(null);// reset error code to null
        assertFalse(mConfigInterface.setPropertyForKey("", "", "", configError));//invalid Group and  invalid key
        assertEquals(ConfigInterface.ConfigError.ConfigErrorEnum.InvalidKey,configError.getErrorCode() );

        configError.setErrorCode(null);// reset error code to null
        assertFalse( mConfigInterface.setPropertyForKey(null, null, "null", configError));////invalid Group and  invalid key
        assertEquals(ConfigInterface.ConfigError.ConfigErrorEnum.InvalidKey,configError.getErrorCode() );

        configError.setErrorCode(null);// reset error code to null
        assertFalse(mConfigInterface.setPropertyForKey("AI", null, "null", configError)); //  Existing Group and  invalid key
        assertEquals(ConfigInterface.ConfigError.ConfigErrorEnum.InvalidKey,configError.getErrorCode() );

        configError.setErrorCode(null);// reset error code to null
        assertFalse(mConfigInterface.setPropertyForKey("NonExistingGroupKey", "NewKey", "test", configError));// Non Existing Group  and  Existing key
        assertEquals(ConfigInterface.ConfigError.ConfigErrorEnum.GroupNotExists,configError.getErrorCode() );

        configError.setErrorCode(null);// reset error code to null
        assertTrue(mConfigInterface.setPropertyForKey("AI", "NewKey", "test", configError));//  Existing Group  and Existing key
        assertEquals(null,configError.getErrorCode() );


    }

    public void testSetAndGetKey() throws Exception{
        ConfigInterface.ConfigError configError = new ConfigInterface.ConfigError();
        String existingGroup = "AI";

        // Modify a existing Key
        configError.setErrorCode(null);// reset error code to null
        String existingKey = "MicrositeID";

        assertNotNull(mConfigInterface.getPropertyForKey(existingGroup, existingKey, configError));//  Existing Group and  Existing key
        // make sure AI and MicrositeID exist in configuration file else this test case will fail
        assertEquals(null,configError.getErrorCode() ); // success
        configError.setErrorCode(null);// reset error code to null
        assertTrue(mConfigInterface.setPropertyForKey(existingGroup, existingKey, "NewValue", configError));//  Existing Group  and Non Existing key


        configError.setErrorCode(null);// reset error code to null
        assertTrue(mConfigInterface.setPropertyForKey("AI", "NewKey", "test", configError));//  Existing Group  and Non Existing key
        assertEquals(null,configError.getErrorCode() );        assertEquals(null,configError.getErrorCode() );

        // Add a new Key
        configError.setErrorCode(null);// reset error code to null
        String newlyAddedKey = "NewKeyAdded";
        String newlyAddedValue = "New Value";
        assertTrue(mConfigInterface.setPropertyForKey(existingGroup, newlyAddedKey, newlyAddedValue, configError));//  Existing Group  and Non Existing key
        assertEquals(null,configError.getErrorCode() );
        configError.setErrorCode(null);// reset error code to null
        assertEquals(newlyAddedValue,mConfigInterface.getPropertyForKey(existingGroup, newlyAddedKey, configError));//  Existing Group and  Existing key
        assertEquals(null,configError.getErrorCode() ); // success



    }

}
