/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform;

import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.ApplianceFactory;
import com.philips.cdp2.commlib.core.configuration.RuntimeConfiguration;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivity.demouapp.RefAppApplianceFactory;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.appframework.flowmanager.listeners.FlowManagerListener;
import com.philips.platform.appframework.stateimpl.DemoDataServicesState;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.appupdate.AppUpdateInterface;
import com.philips.platform.appinfra.consentmanager.ConsentManager;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.appinfra.languagepack.LanguagePackInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.appinfra.timesync.TimeInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.AppFrameworkTagging;
import com.philips.platform.baseapp.base.AppInitializationCallback;
import com.philips.platform.baseapp.screens.inapppurchase.IAPState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationOnBoardingState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationState;
import com.philips.platform.baseapp.screens.utility.RALog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class TestAppFrameworkApplication extends AppFrameworkApplication {

    private UserRegistrationOnBoardingState userRegistrationOnBoardingState;
    private IAPState iapState;

    @Mock
    AppInfra appInfraInterface;

    @Mock
    SecureStorageInterface secureStorageInterface;

    @Mock
    ConsentManagerInterface consentManager;

    @Mock
    AppIdentityInterface appIdentityInterface;

    @Mock
    InternationalizationInterface internationalizationInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryInterface;

    @Mock
    AppTaggingInterface taggingInterface;

    @Mock
    TimeInterface timeInterface;

    @Mock
    AppConfigurationInterface appConfigurationInterface;

    @Mock
    RestInterface restInterface;

    @Mock
    ABTestClientInterface abTestClientInterface;

    @Mock
    LanguagePackInterface languagePackInterface;

    @Mock
    AppUpdateInterface appUpdateInterface;

    @Mock
    private UserRegistrationState userRegistrationState;

    @Mock
    private User user;
	
	CommCentral commCentral;

    @Test
    public void shouldPass() {
        assertTrue(true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        try {
            super.attachBaseContext(base);
        } catch (RuntimeException ignored) {
            RALog.e("TestAppFrameworkApplication", " multidex exception with Roboelectric");
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MockitoAnnotations.initMocks(this);
        appInfraInterface = mock(AppInfra.class);
        when(appInfraInterface.getConfigInterface()).thenReturn(appConfigurationInterface);
        when(appInfraInterface.getTagging()).thenReturn(taggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getAppUpdate()).thenReturn(appUpdateInterface);
        when(appInfraInterface.getAppIdentity()).thenReturn(appIdentityInterface);
        when(appInfraInterface.getAbTesting()).thenReturn(abTestClientInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryInterface);
        when(appInfraInterface.getSecureStorage()).thenReturn(secureStorageInterface);
        when(appInfraInterface.getConsentManager()).thenReturn(consentManager);
        when(appInfraInterface.getTime()).thenReturn(timeInterface);
        when(taggingInterface.createInstanceForComponent(any(String.class),any(String.class))).thenReturn(taggingInterface);
        when(loggingInterface.createInstanceForComponent(any(String.class),any(String.class))).thenReturn(loggingInterface);
        when(appIdentityInterface.getAppState()).thenReturn(AppIdentityInterface.AppState.STAGING);
        when(appInfraInterface.getRestClient()).thenReturn(restInterface);
        when(restInterface.isInternetReachable()).thenReturn(true);
        when(userRegistrationState.getUserObject(any(Context.class))).thenReturn(user);
        when(user.isUserSignIn()).thenReturn(true);
        initializeAppInfra(new AppInitializationCallback.AppInfraInitializationCallback() {
            @Override
            public void onAppInfraInitialization() {
                initialize(new AppInitializationCallback.AppStatesInitializationCallback() {
                    @Override
                    public void onAppStatesInitialization() {

                    }
                });
            }
        });
        AppFrameworkTagging.getInstance().initAppTaggingInterface(this);


        userRegistrationOnBoardingState = new UserRegistrationOnBoardingState();
        userRegistrationOnBoardingState.init(this);
        setTargetFlowManager();

    }

    @Override
    protected AppInfra createAppInfraInstance() {
        return appInfraInterface;
    }

    @Mock
    private DemoDataServicesState mockDSState;

    @Override
    public void initDataServiceState() {
        doNothing().when(mockDSState).init(mock(Context.class));
    }

    @Override
    public DemoDataServicesState getDataServiceState() {
        return mockDSState;
    }
	
	 @Override
    public CommCentral getCommCentralInstance() {
        if(commCentral == null) {
            final RuntimeConfiguration runtimeConfiguration = new RuntimeConfiguration(getApplicationContext(), appInfraInterface);
            final BleTransportContext bleTransportContext = new BleTransportContext(runtimeConfiguration, true);
            ApplianceFactory applianceFactory = mock(RefAppApplianceFactory.class);

            commCentral = new CommCentral(applianceFactory, bleTransportContext);
        }
        return commCentral;
    }



    public IAPState getIap() {
        return iapState;
    }

    public void setIapState(IAPState state) {
        iapState = state;
    }

    @Override
    public void initializeAppInfra(AppInitializationCallback.AppInfraInitializationCallback appInfraInitializationCallback) {
        appInfra= appInfraInterface;
    }

    @Override
    public void initialize(AppInitializationCallback.AppStatesInitializationCallback callback) {
    }

    public void setTargetFlowManager() {
        this.targetFlowManager = new FlowManager();
        this.targetFlowManager.initialize(getApplicationContext(), R.raw.appflow, new FlowManagerListener() {
            @Override
            public void onParseSuccess() {

            }
        });
    }

    @Test
    public void testLanguagePack() {
        appInfraInterface = mock(AppInfra.class);
        languagePackInterface = mock(LanguagePackInterface.class);
        when(appInfraInterface.getLanguagePack()).thenReturn(languagePackInterface);
        languagePackInterface.refresh(new LanguagePackInterface.OnRefreshListener() {
            @Override
            public void onError(AILPRefreshResult ailpRefreshResult, String s) {

            }

            @Override
            public void onSuccess(AILPRefreshResult ailpRefreshResult) {
                assertEquals(ailpRefreshResult, AILPRefreshResult.REFRESHED_FROM_SERVER);

                languagePackInterface.activate(new LanguagePackInterface.OnActivateListener() {
                    @Override
                    public void onSuccess(String s) {
                        assertNotNull(s);
                    }

                    @Override
                    public void onError(AILPActivateResult ailpActivateResult, String s) {

                    }
                });
            }
        });

    }

    @Override
    public UserRegistrationState getUserRegistrationState() {
        return userRegistrationState;
    }
}