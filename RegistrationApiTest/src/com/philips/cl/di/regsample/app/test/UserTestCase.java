package com.philips.cl.di.regsample.app.test;

import org.mockito.Mockito;

import com.janrain.android.Jump;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.controller.RegisterTraditional;
import com.philips.cl.di.reg.handlers.TraditionalRegistrationHandler;
import com.philips.cl.di.reg.handlers.UpdateUserRecordHandler;

public class UserTestCase extends MockedTestCase {

	public UserTestCase(){
		try {
			super.setUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void testUser() throws Exception {

		User result = new User(getInstrumentation().getTargetContext());
		assertNotNull(result);
	}

	public void testUserNullCheck() {

		User result = new User(null);
		assertNull(result);
	}
	
	public void testRegisterUserInfoForTraditionalIsOnSuccess(){
		
		User mockUser = Mockito.mock(User.class);
		TraditionalRegistrationHandler regHandler = Mockito
				.mock(TraditionalRegistrationHandler.class);
		
		UpdateUserRecordHandler updateHandler = Mockito
				.mock(UpdateUserRecordHandler.class);
		
		mockUser.registerNewUserUsingTraditional(RegistrationTestUtility
				.setValuesForTraditionalLogin("Sampath", 
						"sampath.kumar@yahoo.com", "Sams1234", true, false), 
						regHandler);
		Jump.SignInResultHandler mockJump = Mockito.mock(Jump.SignInResultHandler.class);
		mockJump.onSuccess();
		RegisterTraditional handler = new RegisterTraditional(regHandler, getInstrumentation().getTargetContext(), updateHandler);
		
		handler.onSuccess();
		Mockito.verify(regHandler, Mockito.atLeast(1)).onRegisterSuccess();
	}

}
