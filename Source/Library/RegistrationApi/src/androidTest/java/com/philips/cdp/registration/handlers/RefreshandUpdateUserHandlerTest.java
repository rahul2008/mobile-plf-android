package com.philips.cdp.registration.handlers;

import android.content.Context;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.User;

import org.junit.Before;
import org.mockito.Mock;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;


public class RefreshandUpdateUserHandlerTest extends RegistrationApiInstrumentationBase {

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