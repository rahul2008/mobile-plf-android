package com.philips.platform.appinfra.appconfiguration;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 310238114 on 8/2/2016.
 */
public class AppConfigurationTest extends MockitoTestCase {

    AppConfigurationInterface mConfigInterface = null;

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
        mConfigInterface = new AppConfigurationManager(mAppInfra) {
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
                    String testJson = "{\n" +
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

        };
        assertNotNull(mConfigInterface);

    }

    public void testGetPropertyForKey() throws AppConfigurationInterface.InvalidArgumentException {
        try {
            AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface
                    .AppConfigurationError();


            configError.setErrorCode(null);// reset error code to null
            assertNull(mConfigInterface.getPropertyForKey("", "", configError));//  invalid key and invalid Group
            assertEquals(AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum.InvalidKey,
                    configError.getErrorCode());

            configError.setErrorCode(null);// reset error code to null
            assertNull(mConfigInterface.getPropertyForKey(null, null, configError));// invalid key and invalid Group
            assertEquals(AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum.InvalidKey, configError.getErrorCode());

            configError.setErrorCode(null);// reset error code to null
            assertNull(mConfigInterface.getPropertyForKey(null,"AI",  configError)); //  Existing Group but invalid key
            assertEquals(AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum.InvalidKey, configError.getErrorCode());

            configError.setErrorCode(null);// reset error code to null
            assertNull(mConfigInterface.getPropertyForKey( "incorrectKey","incorrectGroupKey", configError)); // Non Existing Group  and Non Existing key
            assertEquals(AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum.GroupNotExists, configError.getErrorCode());

            configError.setErrorCode(null);// reset error code to null
            assertNull(mConfigInterface.getPropertyForKey("incorrectKey","AI" , configError)); //  Existing Group  but Non Existing key
            assertEquals(AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum.KeyNotExists, configError.getErrorCode());

            assertNull(mConfigInterface.getPropertyForKey("NewKey","NonExistingGroupKey",  configError));// Non Existing Group  and  Existing key


            //String fetch
            configError.setErrorCode(null);// reset error code to null
            assertNotNull(mConfigInterface.getPropertyForKey("RegistrationEnvironment","AI",  configError));//  Existing Group and  Existing key
            // make sure AI and MicrositeID exist in configuration file else this test case will fail
            assertEquals(null, configError.getErrorCode()); // success

            //Integer fetch
            configError.setErrorCode(null);// reset error code to null
            assertNotNull(mConfigInterface.getPropertyForKey("MicrositeID","AI",  configError));//  Existing Group and  Existing key
            // make sure AI and MicrositeID exist in configuration file else this test case will fail
            assertEquals(null, configError.getErrorCode()); // success

            //String array fetch
            configError.setErrorCode(null);// reset error code to null
            assertNotNull(mConfigInterface.getPropertyForKey( "US","AI", configError));//  Existing Group and  Existing key
            // make sure AI and MicrositeID exist in configuration file else this test case will fail
            assertEquals(null, configError.getErrorCode()); // success

            //Integer array fetch
            configError.setErrorCode(null);// reset error code to null
            assertNotNull(mConfigInterface.getPropertyForKey("EE","AI",  configError));//  Existing Group and  Existing key
            // make sure AI and MicrositeID exist in configuration file else this test case will fail
            assertEquals(null, configError.getErrorCode()); // success

        } catch (AppConfigurationInterface.InvalidArgumentException exception) {
            exception.printStackTrace();
        }

    }


    public void testSetPropertyForKey() throws AppConfigurationInterface.InvalidArgumentException {
        try {

            AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();

//        configError.setErrorCode(null);// reset error code to null
//        assertFalse(mConfigInterface.setPropertyForKey("", "", "", configError));//invalid Group and  invalid key
//        assertEquals(AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum.InvalidKey, configError.getErrorCode());

//        configError.setErrorCode(null);// reset error code to null
//        assertFalse(mConfigInterface.setPropertyForKey(null, null, "null", configError));////invalid Group and  invalid key
//        assertEquals(AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum.InvalidKey, configError.getErrorCode());

//        configError.setErrorCode(null);// reset error code to null
//        assertFalse(mConfigInterface.setPropertyForKey("AI", null, "null", configError)); //  Existing Group and  invalid key
//        assertEquals(AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum.InvalidKey, configError.getErrorCode());

            configError.setErrorCode(null);// reset error code to null
            assertTrue(mConfigInterface.setPropertyForKey("NewKey", "NonExistingGroupKey",  "val", configError));// Non Existing Group  and  Existing key
            assertEquals(null, configError.getErrorCode());

            //String set
            configError.setErrorCode(null);// reset error code to null
            assertTrue(mConfigInterface.setPropertyForKey( "NewKey","AI", "test", configError));//  Existing Group  and Existing key
            assertEquals(null, configError.getErrorCode());

            //Integer set
            configError.setErrorCode(null);// reset error code to null
            assertTrue(mConfigInterface.setPropertyForKey("MicrositeID", "AI",  new Integer(77000), configError));//  Existing Group  and Existing key
            assertEquals(null, configError.getErrorCode());

            //String array set
            List<String> stringArray = new ArrayList<String>();
            stringArray.add("twitter");
            stringArray.add("rss");
            configError.setErrorCode(null);// reset error code to null
            assertTrue(mConfigInterface.setPropertyForKey("US","AI",  stringArray, configError));//  Existing Group  and Existing key
            assertEquals(null, configError.getErrorCode());

            //Integer array set
            List<Integer> integerArray = new ArrayList<Integer>();
            integerArray.add(new Integer(111));
            integerArray.add(new Integer(222));
            configError.setErrorCode(null);// reset error code to null
            assertTrue(mConfigInterface.setPropertyForKey("EE","AI",  integerArray, configError));//  Existing Group  and Existing key
            assertEquals(null, configError.getErrorCode());


        } catch (AppConfigurationInterface.InvalidArgumentException exception) {
            exception.printStackTrace();
        }

    }

    public void testSetAndGetKey() throws AppConfigurationInterface.InvalidArgumentException {
        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
        String existingGroup = "AI";

        // Modify a existing Key
        configError.setErrorCode(null);// reset error code to null
        String existingKey = "MicrositeID";

        assertNotNull(mConfigInterface.getPropertyForKey(existingKey,existingGroup,  configError));//  Existing Group and  Existing key
        // make sure AI and MicrositeID exist in configuration file else this test case will fail
        assertEquals(null, configError.getErrorCode()); // success
        configError.setErrorCode(null);// reset error code to null
        assertTrue(mConfigInterface.setPropertyForKey(existingKey, existingGroup,  "NewValue", configError));//  Existing Group  and Non Existing key


        configError.setErrorCode(null);// reset error code to null
        assertTrue(mConfigInterface.setPropertyForKey( "NewKey","AI", "test", configError));//  Existing Group  and Non Existing key
        assertEquals(null, configError.getErrorCode());
        assertEquals(null, configError.getErrorCode());

        // Add a new String value
        configError.setErrorCode(null);// reset error code to null
        String newlyAddedKey1 = "NewKeyAdded1";
        String newlyAddedValue1 = "New Value";
        assertTrue(mConfigInterface.setPropertyForKey( newlyAddedKey1, existingGroup, newlyAddedValue1, configError));//  Existing Group  and Non Existing key
        assertEquals(null, configError.getErrorCode());
        configError.setErrorCode(null);// reset error code to null
        assertEquals(newlyAddedValue1, mConfigInterface.getPropertyForKey(newlyAddedKey1, existingGroup,  configError));//  Existing Group and  Existing key
        assertEquals(null, configError.getErrorCode()); // success

        // Add a new Integer value
        configError.setErrorCode(null);// reset error code to null
        String newlyAddedKey2 = "NewKeyAdded2";
        Integer integer = new Integer(23);
        assertTrue(mConfigInterface.setPropertyForKey(newlyAddedKey2,existingGroup,  integer, configError));//  Existing Group  and Non Existing key
        assertEquals(null, configError.getErrorCode());
        configError.setErrorCode(null);// reset error code to null
        assertEquals(integer, mConfigInterface.getPropertyForKey(newlyAddedKey2, existingGroup,  configError));//  Existing Group and  Existing key
        assertEquals(null, configError.getErrorCode()); // success

        // Add a new String Arraylist value
        configError.setErrorCode(null);// reset error code to null
        String newlyAddedKey3 = "NewKeyAdded3";
        ArrayList<String> stringArrayList = new ArrayList<String>();
        stringArrayList.add("item1");
        stringArrayList.add("item2");
        assertTrue(mConfigInterface.setPropertyForKey(newlyAddedKey3, existingGroup,  stringArrayList, configError));//  Existing Group  and Non Existing key
        assertEquals(null, configError.getErrorCode());
        configError.setErrorCode(null);// reset error code to null
        assertEquals(stringArrayList, mConfigInterface.getPropertyForKey(newlyAddedKey3, existingGroup,  configError));//  Existing Group and  Existing key
        assertEquals(null, configError.getErrorCode()); // success


        // Add a new Integer ArrayList value
        ArrayList<Integer> integerArrayList = new ArrayList<Integer>();
        integerArrayList.add(new Integer(23));
        integerArrayList.add(new Integer(34));
        integerArrayList.add(new Integer(84));
        configError.setErrorCode(null);// reset error code to null
        String newlyAddedKey4 = "NewKeyAdded4";
        assertTrue(mConfigInterface.setPropertyForKey(newlyAddedKey4, existingGroup,  integerArrayList, configError));//  Existing Group  and Non Existing key
        assertEquals(null, configError.getErrorCode());
        configError.setErrorCode(null);// reset error code to null
        assertEquals(integerArrayList, mConfigInterface.getPropertyForKey(newlyAddedKey4, existingGroup,  configError));//  Existing Group and  Existing key
        assertEquals(null, configError.getErrorCode()); // success
    }

}
