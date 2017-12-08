package com.philips.platform.appinfra;

import android.content.Context;
import android.util.Log;

import com.philips.platform.appinfra.abtestclient.ABTestClientManager;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
import com.philips.platform.appinfra.appupdate.AppUpdateManager;
import com.philips.platform.appinfra.languagepack.LanguagePackManager;

import org.json.JSONObject;

/**
 * AppInfra Test class.
 */

public class AppInfraTest extends AppInfraInstrumentation {



    AppInfra mAppInfra;
	Context context;
	ABTestClientManager abTestClientManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);

        mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
    }

    public void testComponentVersion(){
        assertNotNull(mAppInfra.getVersion());
        assertEquals(BuildConfig.VERSION_NAME,mAppInfra.getVersion()); // BuildConfig is dynamically created version file
    }

    public void testComponentID(){
        assertNotNull(mAppInfra.getComponentId());
        String appInfraID = "ail";
        assertEquals(appInfraID,mAppInfra.getComponentId()); // ail = AppInfra Language
    }

    public void testInitilizationWithNoValues() {
	    final AppConfigurationManager configInterface = new AppConfigurationManager(mAppInfra) {
		    @Override
		    protected JSONObject getMasterConfigFromApp() {
			    JSONObject result = null;
			    try {
				    String testJson = ConfigValues.testJsonforIntilization();
				    result = new JSONObject(testJson);
			    } catch (Exception e) {
					Log.e(getClass()+""," error while testing initialization");

				}
			    return result;
		    }
	    };
	    mAppInfra = new AppInfra.Builder().setConfig(configInterface).build(context);
	    Object abTestConfig = ABTestClientManager.getAbtestConfig(configInterface, mAppInfra);

	    if(abTestConfig == null) {
		    assertTrue(mAppInfra.getAbTesting() == null);
	    }

	    String languagePackConfig = LanguagePackManager.getLanguagePackConfig(configInterface,mAppInfra);
	    if(languagePackConfig == null) {
		    assertTrue(mAppInfra.getLanguagePack() == null);
	    }

	    Object appUpdateConfig = AppUpdateManager.getAutoRefreshValue(configInterface,mAppInfra);
	    if(appUpdateConfig == null) {
		    assertTrue(mAppInfra.getAppUpdate() == null);
	    }
    }

    public void testInitializationWithValues() {

	    final AppConfigurationManager configInterface = new AppConfigurationManager(mAppInfra) {
		    @Override
		    protected JSONObject getMasterConfigFromApp() {
			    JSONObject result = null;
			    try {
				    String testJson = ConfigValues.testJson();
				    result = new JSONObject(testJson);
			    } catch (Exception e) {
					Log.e(getClass()+""," error while request r-time");

				}
			    return result;
		    }
	    };
	    mAppInfra = new AppInfra.Builder().setConfig(configInterface).build(context);

	    Object abTestConfig = ABTestClientManager.getAbtestConfig(configInterface, mAppInfra);
	    if(abTestConfig != null) {
		    mAppInfra.setAbTesting(new ABTestClientManager(mAppInfra));
	    }
	    assertNotNull(mAppInfra.getAbTesting());

	    String languagePackConfig = LanguagePackManager.getLanguagePackConfig(configInterface,mAppInfra);
        if(languagePackConfig != null) {
	        mAppInfra.setLanguagePackInterface(new LanguagePackManager(mAppInfra));
        }
        assertNotNull(mAppInfra.getLanguagePack());

	    Object appUpdateConfig = AppUpdateManager.getAutoRefreshValue(configInterface,mAppInfra);
	    if(appUpdateConfig != null) {
		    mAppInfra.setAppupdateInterface(new AppUpdateManager(mAppInfra));
	    }

	    assertNotNull(mAppInfra.getAppUpdate());
    }
}
