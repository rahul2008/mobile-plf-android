package com.philips.platform.appinfra.appconfig;

import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
import com.philips.platform.appinfra.logging.LoggingInterface;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * AppConfiguration Test Class.
 */
public class AppConfigurationTest extends TestCase {

	AppConfigurationInterface mConfigInterface = null;

	private AppInfra mAppInfra;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		MockitoAnnotations.initMocks(this);
		 mAppInfra = mock(AppInfra.class);
		mConfigInterface = mock(AppConfigurationInterface.class);
		testConfig("Staging");

	}
	public void testConfig(final String value) {

				JSONObject result = null;
				try {
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
							"  }, \n" +
							" \"appinfra\": { \n" +
							"   \"appidentity.micrositeId\" : \"77000\",\n" +
							"    \"tagging.sensitiveData\": [\"bundleId\", \"language\"  ],\n" +
							"  \"appidentity.sector\"  : \"B2C\",\n" +
							" \"appidentity.appState\"  : \"" + value + "\",\n" +
							"\"appidentity.serviceDiscoveryEnvironment\"  : \"Staging\",\n" +
							"\"restclient.cacheSizeInKB\"  : 1024 \n" +
							"} \n" + "}";
					result = new JSONObject(testJson);
				} catch (Exception e) {
				}


;

	}


	public void testGetPropertyForKey() throws IllegalArgumentException {
		try {
			AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface
					.AppConfigurationError();
			when(mAppInfra.getConfigInterface()).thenReturn(mConfigInterface);
			assertNull(mConfigInterface.getPropertyForKey("", "", configError));

		} catch (IllegalArgumentException exception) {
			mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_APP_CONFIGUARTION,
					"Error in GetPropertyForKey");
		}

	}


	public void testSetPropertyForKey() throws IllegalArgumentException {
		try {

			AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();


			configError.setErrorCode(null);// reset error code to null
			when(mAppInfra.getConfigInterface()).thenReturn(mConfigInterface);
			assertFalse(mConfigInterface.setPropertyForKey("appidentity.appState", "appinfra", "val", configError));// Non Existing Group  and  Existing key
			assertEquals(null, configError.getErrorCode());

		} catch (IllegalArgumentException exception) {
			mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_APP_CONFIGUARTION,
					"Error in SetPropertyForKey");
		}
	}

	public void testSetAndGetKey() throws IllegalArgumentException {
		AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
		String existingGroup = "APPINFRA";
		String existingKey = "appidentity.micrositeId";
		mConfigInterface.setPropertyForKey(existingKey, existingGroup, "OldValue", configError);

		// Modify a existing Key
		configError.setErrorCode(null);// reset error code to null
		assertNull(mConfigInterface.getPropertyForKey(existingKey, existingGroup, configError));//  Existing Group and  Existing key
		// make sure AI and MicrositeID exist in configuration file else this test case will fail

	}


	public void testDefaultPropertyForKey() throws IllegalArgumentException {
		try {
			AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface
					.AppConfigurationError();


			configError.setErrorCode(null);// reset error code to null
			assertNull(mConfigInterface.getDefaultPropertyForKey("", "", configError));//  invalid key and invalid Group

			configError.setErrorCode(null);// reset error code to null
			assertNull(mConfigInterface.getDefaultPropertyForKey(null, null, configError));// invalid key and invalid Group


			configError.setErrorCode(null);// reset error code to null
			assertNull(mConfigInterface.getDefaultPropertyForKey(null, "AI", configError)); //  Existing Group but invalid key


			configError.setErrorCode(null);// reset error code to null
			assertNull(mConfigInterface.getDefaultPropertyForKey("incorrectKey", "incorrectGroupKey", configError)); // Non Existing Group  and Non Existing key

			configError.setErrorCode(null);// reset error code to null
			assertNull(mConfigInterface.getDefaultPropertyForKey("incorrectKey", "AI", configError)); //  Existing Group  but Non Existing key

			assertNull(mConfigInterface.getDefaultPropertyForKey("NewKey", "NonExistingGroupKey", configError));// Non Existing Group  and  Existing key


			//String fetch
			configError.setErrorCode(null);// reset error code to null
			assertNull(mConfigInterface.getDefaultPropertyForKey("RegistrationEnvironment", "AI", configError));//  Existing Group and  Existing key

			//Integer fetch
			configError.setErrorCode(null);// reset error code to null
			assertNull(mConfigInterface.getDefaultPropertyForKey("MicrositeID", "AI", configError));//  Existing Group and  Existing key

			//String array fetch
			configError.setErrorCode(null);// reset error code to null
			assertNull(mConfigInterface.getDefaultPropertyForKey("US", "AI", configError));//  Existing Group and  Existing key

			//Integer array fetch
			configError.setErrorCode(null);// reset error code to null
			assertNull(mConfigInterface.getDefaultPropertyForKey("EE", "AI", configError));//  Existing Group and  Existing key

		} catch (IllegalArgumentException exception) {
			mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_APP_CONFIGUARTION,
					"Error in Default PropertyForKey");
		}

	}

    /*public void testMakeKeyUppercase() {
        try {
            AppConfigurationManager  appConfigurationManager =(AppConfigurationManager) mConfigInterface;

            method = appConfigurationManager.getClass().getDeclaredMethod("makeKeyUppercase", new Class[]{JSONObject.class});
            method.setAccessible(true);
            JSONObject jsonObj = new JSONObject(getJSONString());
            method.invoke(mConfigInterface, jsonObj);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppConfig",
                    e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }*/

	public void testgetMasterConfigFromApp() {
		when(mAppInfra.getConfigInterface()).thenReturn(mConfigInterface);
		Method method;
		try {
			method = mConfigInterface.getClass().getDeclaredMethod("getMasterConfigFromApp");
			method.setAccessible(true);
			method.invoke(mConfigInterface);

		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {

		}

	}





	private String testJsonOld() {

		return "{\n" +
				"  \"UR\": {\n" +
				"\n" +
				"    \"DEVELOPMENT\": \"moodifiedData1\",\n" +
				"    \"TESTING\": \"moodifiedData2\",\n" +
				"    \"EVALUATION\": \"4r36zdbeycca933nufcknn2hnpsz6gxu\",\n" +
				"    \"STAGING\": \"f2stykcygm7enbwfw2u9fbg6h6syb8yd\",\n" +
				"    \"PRODUCTION\": \"mz6tg5rqrg4hjj3wfxfd92kjapsrdhy3\"\n" +
				"\n" +
				"  },\n" +
				"  \"AI\": {\n" +
				"    \"MICROSITEID\": 2222,\n" +
				"    \"REGISTRATIONENVIRONMENT\": \"Staging\",\n" +
				"    \"NL\": [\"googleplus\", \"facebook\"  ],\n" +
				"    \"US\": [\"facebook\",\"googleplus\" ],\n" +
				"    \"MAP\": {\"one\": \"123\", \"two\": \"123.45\"},\n" +
				"    \"EE\": [123,234 ]\n" +
				"  }, \n" +
				" \"APPINFRA\": { \n" +
				"   \"APPIDENTITY.NEWKEY1\" : \"101010\",\n" +
				"   \"APPIDENTITY.MICROSITEID\" : \"77000\",\n" +
				"  \"APPIDENTITY.SECTOR\"  : \"B2C\",\n" +
				" \"APPIDENTITY.APPSTATE\"  : \"Staging\",\n" +
				"\"APPIDENTITY.SERVICEDISCOVERYENVIRONMENT\"  : \"Staging\",\n" +
				"\"RESTCLIENT.CACHESIZEINKB\"  : 1024, \n" +
				" \"TAGGING.SENSITIVEDATA\": [\"bundleId, language\"] ,\n" +
				"  \"ABTEST.PRECACHE\":[\"philipsmobileappabtest1content\",\"philipsmobileappabtest1success\"],\n" +
				"    \"CONTENTLOADER.LIMITSIZE\":555,\n" +
				"    \"SERVICEDISCOVERY.PLATFORMMICROSITEID\":\"77000\",\n" +
				"    \"SERVICEDISCOVERY.PLATFORMENVIRONMENT\":\"production\",\n" +
				"    \"SERVICEDISCOVERY.PROPOSITIONENABLED\":\true,\n" +
				"    \"APPCONFIG.CLOUDSERVICEID\":\" appinfra.appconfigdownload\",\n" +
				"  \"TIMESYNC.NTP.HOSTS\":[\"0.pool.ntp.org\",\"1.pool.ntp.org\",\"2.pool.ntp.org\",\"3.pool.ntp.org\",\"0.cn.pool.ntp.org\"],\n" +
				"    \"LANGUAGEPACK.SERVICEID\":\"appinfra.languagepack\",\n" +
				"  \"LOGGING.RELEASECONFIG\": {\"fileName\": \"AppInfraLog\",\"numberOfFiles\": 5,\"fileSizeInBytes\": 50000,\"logLevel\": \"All\",\"fileLogEnabled\": true,\"consoleLogEnabled\": true,\"componentLevelLogEnabled\": false,\"componentIds\": [\"DemoAppInfra\",\"Registration\"]},\n" +
				"  \"LOGGING.DEBUGCONFIG\": {\"fileName\": \"AppInfraLog\",\"numberOfFiles\": 5,\"fileSizeInBytes\": 50000,\"logLevel\": \"All\",\"fileLogEnabled\": true,\"consoleLogEnabled\": true,\"componentLevelLogEnabled\": false,\"componentIds\": [\"DemoAppInfra\",\"Registration\", \"component1\"]}" +
				"  }\n" +
				"}\n";
	}


	// expected modified value
	private JSONObject testJsonNew() {
		String testJsonString = "{\n" +
				"  \"UR\": {\n" +
				"\n" +
				"    \"DEVELOPMENT\": \"moodifiedData1\",\n" +
				"    \"TESTING\": \"moodifiedData2\"\n" +
				"\n" +
				"  },\n" +
				"  \"AI\": {\n" +
				"    \"MICROSITEID\": 2222\n" +

				"  }, \n" +
				" \"APPINFRA\": { \n" +

				"    \"CONTENTLOADER.LIMITSIZE\":555\n" +

				"  }\n" +
				"}\n";

		JSONObject obj = null;
		try {
			obj = new JSONObject(testJsonString);
		} catch (JSONException e) {
		}
		return obj;
	}

	public void testRefreshCloudConfig() {
		AppConfigurationManager appConfigurationManager = (AppConfigurationManager) mAppInfra.getConfigInterface();
		Method method;
		try {
			method = AppConfigurationManager.class.getDeclaredMethod("refreshCloudConfig", AppConfigurationInterface.OnRefreshListener.class);

			method.setAccessible(true);
			AppConfigurationInterface.OnRefreshListener listener = new AppConfigurationInterface.OnRefreshListener() {
				@Override
				public void onError(AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum error, String message) {
					Log.v("refreshCloudConfig", message);
				}

				@Override
				public void onSuccess(REFRESH_RESULT result) {
					Log.v("refreshCloudConfig", result.toString());

				}
			};


		} catch (NoSuchMethodException e) {
			mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_APP_CONFIGUARTION,
					"Error in test RefreshCloudConfig");
		}
	}

	public void testFetchCloudConfig() {
		AppConfigurationManager appConfigurationManager = (AppConfigurationManager) mAppInfra.getConfigInterface();
		Method method;
		try {
			method = AppConfigurationManager.class.getDeclaredMethod("fetchCloudConfig", String.class, AppConfigurationInterface.OnRefreshListener.class);
			method.setAccessible(true);
			AppConfigurationInterface.OnRefreshListener listener = new AppConfigurationInterface.OnRefreshListener() {
				@Override
				public void onError(AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum error, String message) {
					Log.v("refreshCloudConfig", message);
				}

				@Override
				public void onSuccess(REFRESH_RESULT result) {
					Log.v("refreshCloudConfig", result.toString());

				}
			};

		} catch (NoSuchMethodException e) {
			mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_APP_CONFIGUARTION,
					"Error in  test FetchCloudConfig");
		}
	}

	public void testSaveCloudConfig() {
		AppConfigurationManager appConfigurationManager = (AppConfigurationManager) mAppInfra.getConfigInterface();
		Method method;
		try {
			method = AppConfigurationManager.class.getDeclaredMethod("saveCloudConfig", JSONObject.class, String.class);
			method.setAccessible(true);
			JSONObject jObject = new JSONObject();

		} catch (NoSuchMethodException  e) {
			mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_APP_CONFIGUARTION,
					"Error in test  SaveCloudConfig");
		}
	}

	public void testClearCloudConfigFile() {
		AppConfigurationManager appConfigurationManager = (AppConfigurationManager) mAppInfra.getConfigInterface();
		Method method;
		try {
			method = AppConfigurationManager.class.getDeclaredMethod("clearCloudConfigFile");
			method.setAccessible(true);

		} catch (NoSuchMethodException  e) {
			mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_APP_CONFIGUARTION,
					"Error in  test ClearCloudConfigFile");
		}
	}

	public void testresetConfig() {
		AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
		String existingGroup = "APPINFRA";
		String existingKey = "appidentity.micrositeId";
		mConfigInterface.setPropertyForKey(existingKey, existingGroup, "OldValue", configError);

		// Modify a existing Key
		configError.setErrorCode(null);// reset error code to null
		assertNull(mConfigInterface.getPropertyForKey(existingKey, existingGroup, configError));//  Existing Group and  Existing key
		// make sure AI and MicrositeID exist in configuration file else this test case will fail
		configError.setErrorCode(null);// reset error code to null

		mConfigInterface.resetConfig();


		mConfigInterface.setPropertyForKey("b","a","c" ,configError);
		configError.setErrorCode(null);// reset error code to null


		mConfigInterface.resetConfig();
		assertNull(mConfigInterface.getPropertyForKey("b", "a", configError));//  Existing Group and  Existing key

	}
}
