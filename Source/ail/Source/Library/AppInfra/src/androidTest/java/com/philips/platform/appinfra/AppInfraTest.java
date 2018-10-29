/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.appinfra;

import android.content.Context;
import android.util.Log;

import com.philips.platform.appinfra.abtestclient.ABTestClientManager;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
import com.philips.platform.appinfra.appupdate.AppUpdateManager;
import com.philips.platform.appinfra.languagepack.LanguagePackManager;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class AppInfraTest {

    private AppInfra mAppInfra;
    private Context context;

    ABTestClientManager abTestClientManager;

    @Before
    protected void setUp() throws Exception {
        context = getInstrumentation().getContext();
        assertNotNull(context);

        mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
    }

    @Test
    public void testComponentVersion() {
        assertNotNull(mAppInfra.getVersion());
        assertEquals(BuildConfig.VERSION_NAME, mAppInfra.getVersion()); // BuildConfig is dynamically created version file
    }

    @Test
    public void testComponentID() {
        assertNotNull(mAppInfra.getComponentId());
        String appInfraID = "ail";
        assertEquals(appInfraID, mAppInfra.getComponentId()); // ail = AppInfra Language
    }

    @Test
    public void testInitilizationWithNoValues() {
        final AppConfigurationManager configInterface = new AppConfigurationManager(mAppInfra) {
            @Override
            protected JSONObject getMasterConfigFromApp() {
                JSONObject result = null;
                try {
                    String testJson = ConfigValues.testJsonforIntilization();
                    result = new JSONObject(testJson);
                } catch (Exception e) {
                    Log.e(getClass() + "", " error while testing initialization");

                }
                return result;
            }
        };
        mAppInfra = new AppInfra.Builder().setConfig(configInterface).build(context);

        String languagePackConfig = LanguagePackManager.getLanguagePackConfig(configInterface, mAppInfra);
        if (languagePackConfig == null) {
            assertNull(mAppInfra.getLanguagePack());
        }

        Object appUpdateConfig = AppUpdateManager.getAutoRefreshValue(configInterface, mAppInfra);
        if (appUpdateConfig == null) {
            assertNull(mAppInfra.getAppUpdate());
        }
    }

    @Test
    public void testInitializationWithValues() {
        final AppConfigurationManager configInterface = new AppConfigurationManager(mAppInfra) {
            @Override
            protected JSONObject getMasterConfigFromApp() {
                JSONObject result = null;
                try {
                    String testJson = ConfigValues.testJson();
                    result = new JSONObject(testJson);
                } catch (Exception e) {
                    Log.e(getClass() + "", " error while request r-time");

                }
                return result;
            }
        };
        mAppInfra = new AppInfra.Builder().setConfig(configInterface).build(context);

        String languagePackConfig = LanguagePackManager.getLanguagePackConfig(configInterface, mAppInfra);
        if (languagePackConfig != null) {
            mAppInfra.setLanguagePackInterface(new LanguagePackManager(mAppInfra));
        }
        assertNotNull(mAppInfra.getLanguagePack());

        Object appUpdateConfig = AppUpdateManager.getAutoRefreshValue(configInterface, mAppInfra);
        if (appUpdateConfig != null) {
            mAppInfra.setAppupdateInterface(new AppUpdateManager(mAppInfra));
        }

        assertNotNull(mAppInfra.getAppUpdate());
    }
}
