package com.philips.cdp.registration.handlers;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.User;

import org.junit.Before;
import org.mockito.Mock;

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
    RefreshUserHandler handler;
    User user;


    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().
                getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        context = getInstrumentation().getTargetContext();
        handler= new RefreshUserHandler() {
            @Override
            public void onRefreshUserSuccess() {

            }

            @Override
            public void onRefreshUserFailed(final int error) {

            }
        };
        user= new User(getInstrumentation().getContext());
        updateUserRecordHandler = new UpdateUserRecordHandler() {
            @Override
            public void updateUserRecordLogin() {

            }

            @Override
            public void updateUserRecordRegister() {

            }
        };
        assertNotNull(updateUserRecordHandler);
        refreshandUpdateUserHandler = new RefreshandUpdateUserHandler(updateUserRecordHandler,
                context);
        assertNotNull(refreshandUpdateUserHandler);

    }




}