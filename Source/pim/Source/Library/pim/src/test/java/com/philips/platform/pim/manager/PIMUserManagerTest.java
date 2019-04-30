package com.philips.platform.pim.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pim.listeners.PIMUserProfileDownloadListener;

import junit.framework.TestCase;

import net.openid.appauth.AuthState;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

//TODO: Shashi, Add test cases
@RunWith(PowerMockRunner.class)
public class PIMUserManagerTest extends TestCase {

    private PIMUserManager pimUserManager;
    @Mock
    Context mockContext;
    @Mock
    AppInfraInterface mockAppInfraInterface;
    @Mock
    PIMSettingManager mockPimSettingManager;
    @Mock
    LoggingInterface mockLoggingInterface;
    @Mock
    SharedPreferences mockSharedPreferences;
    @Mock
    AuthState mockAuthState;
    @Mock
    PIMUserProfileDownloadListener mockUserProfileDownloadListener;
    @Mock
    Uri mockUri;


    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
    }
}