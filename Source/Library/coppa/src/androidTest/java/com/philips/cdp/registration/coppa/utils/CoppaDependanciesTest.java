package com.philips.cdp.registration.coppa.utils;

import android.test.InstrumentationTestCase;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.appinfra.timesync.TimeInterface;

/**
 * Created by 310243576 on 8/24/2016.
 */
public class CoppaDependanciesTest extends InstrumentationTestCase {

    CoppaDependancies mCoppaDependancies;




    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        mCoppaDependancies = new CoppaDependancies(new AppInfraInterface() {
            @Override
            public SecureStorageInterface getSecureStorage() {
                return null;
            }

            @Override
            public AppIdentityInterface getAppIdentity() {
                return null;
            }

            @Override
            public InternationalizationInterface getInternationalization() {
                return null;
            }

            @Override
            public LoggingInterface getLogging() {
                return null;
            }

            @Override
            public ServiceDiscoveryInterface getServiceDiscovery() {
                return null;
            }

            @Override
            public AppTaggingInterface getTagging() {
                return null;
            }

            @Override
            public TimeInterface getTime() {
                return null;
            }

            @Override
            public AppConfigurationInterface getConfigInterface() {
                return null;
            }

            @Override
            public RestInterface getRestClient() {
                return null;
            }

        });
    }
    public void testCoppaDependencies(){
        assertNotNull(mCoppaDependancies);
    }
}