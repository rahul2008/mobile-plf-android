package com.philips.platform.pim.manager;

import com.philips.platform.pim.rest.PIMRestClient;
import com.philips.platform.pim.rest.UserProfileRequest;

import junit.framework.TestCase;

import net.openid.appauth.AuthState;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
public class PIMUserManagerTest extends TestCase {

    private PIMUserManager pimUserManager;
    @Mock
    AuthState mockAuthState;
    @Mock
    UserProfileRequest mockUserProfileRequest;
    @Mock
    PIMRestClient mockPimRestClient;

    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        pimUserManager = new PIMUserManager();
    }

    @Test
    public void testInit(){

    }

    @Test
    public void shouldRequestUserProfile() throws Exception {
        whenNew(UserProfileRequest.class).withArguments(mockAuthState).thenReturn(mockUserProfileRequest);
        //whenNew(PIMRestClient)
        //pimUserManager.requestUserProfile(mockAuthState);
    }

    public void tearDown() throws Exception {
    }
}