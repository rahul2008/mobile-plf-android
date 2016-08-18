package com.philips.cdp.registration.handlers;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.controller.AddConsumerInterest;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraSingleton;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/18/2016.
 */
public class RefreshandUpdateUserHandlerTest extends InstrumentationTestCase {

    @Mock
    RefreshandUpdateUserHandler refreshandUpdateUserHandler;

    @Mock
    UpdateUserRecordHandler updateUserRecordHandler;

    @Mock
    Context context;


    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        context = getInstrumentation().getTargetContext();
        updateUserRecordHandler = new UpdateUserRecordHandler() {
            @Override
            public void updateUserRecordLogin() {

            }

            @Override
            public void updateUserRecordRegister() {

            }
        };
        assertNotNull(updateUserRecordHandler);
        refreshandUpdateUserHandler = new RefreshandUpdateUserHandler(updateUserRecordHandler, context);
        assertNotNull(refreshandUpdateUserHandler);

    }

    @Test
    public void testRefreshAndUpdateUser() throws Exception {
//        User user = new User(context);
//        RefreshUserHandler refreshUserHandler = new RefreshUserHandler() {
//            @Override
//            public void onRefreshUserSuccess() {
//
//            }
//
//            @Override
//            public void onRefreshUserFailed(int error) {
//
//            }
//        };
//
//        AppInfraSingleton.setInstance(new AppInfra.Builder().build(context));
//        refreshandUpdateUserHandler.refreshAndUpdateUser(refreshUserHandler, user, "password");
        assertSame(refreshandUpdateUserHandler.mUpdateUserRecordHandler, updateUserRecordHandler);

    }
}