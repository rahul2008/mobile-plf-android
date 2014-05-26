package com.philips.cl.di.dev.pa.test;

import java.lang.reflect.Field;
import com.philips.cl.di.dev.pa.registration.CreateAccountFragment;
import com.philips.cl.di.dev.pa.registration.CreateAccountFragment.ErrorType;
import com.philips.cl.di.dev.pa.util.ALog;

import junit.framework.TestCase;

public class RegistrationTest extends TestCase {
	
	private Field nameField;
	private Field emailField;
	private Field passField;
	private CreateAccountFragment createAccountFragment;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		createAccountFragment = new CreateAccountFragment();
		try {
			nameField = CreateAccountFragment.class.getDeclaredField("mName");
			nameField.setAccessible(true);
			
			emailField = CreateAccountFragment.class.getDeclaredField("mEmail");
			emailField.setAccessible(true);
			
			passField = CreateAccountFragment.class.getDeclaredField("mPassword");
			passField.setAccessible(true);
			
		} catch (NoSuchFieldException e) {
			ALog.e(ALog.USER_REGISTRATION, "Test: " + e.getMessage());
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			ALog.e(ALog.USER_REGISTRATION, "Test: " + e.getMessage());
			fail(e.getMessage());
		}
		
	}

	public void testIsInputValidatedName() {
		try {
			nameField.set(createAccountFragment, "abc");
		} catch (IllegalAccessException e) {
			ALog.e(ALog.USER_REGISTRATION, "Test: " + e.getMessage());
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			ALog.e(ALog.USER_REGISTRATION, "Test: " + e.getMessage());
			fail(e.getMessage());
		}
		
		ErrorType errorType = createAccountFragment.isInputValidated();
		assertTrue(ErrorType.EMAIL == errorType);
	}
	
	public void testIsInputValidatedEmail() {
		try {
			nameField.set(createAccountFragment, "abc");
			emailField.set(createAccountFragment, "abc@gmail.com");
		} catch (IllegalAccessException e) {
			ALog.e(ALog.USER_REGISTRATION, "Test: " + e.getMessage());
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			ALog.e(ALog.USER_REGISTRATION, "Test: " + e.getMessage());
			fail(e.getMessage());
		}
		
		ErrorType errorType = createAccountFragment.isInputValidated();
		assertTrue(ErrorType.PASSWORD == errorType);
	}
	
	public void testIsInputValidatedPassword() {
		try {
			nameField.set(createAccountFragment, "abc");
			emailField.set(createAccountFragment, "abc@gmail.com");
			passField.set(createAccountFragment, "123456");
		} catch (IllegalAccessException e) {
			ALog.e(ALog.USER_REGISTRATION, "Test: " + e.getMessage());
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			ALog.e(ALog.USER_REGISTRATION, "Test: " + e.getMessage());
			fail(e.getMessage());
		}
		
		ErrorType errorType = createAccountFragment.isInputValidated();
		assertTrue(ErrorType.NONE == errorType);
	}
	
	public void testIsInputValidatedNameNull() {
		try {
			nameField.set(createAccountFragment, null);
		} catch (IllegalAccessException e) {
			ALog.e(ALog.USER_REGISTRATION, "Test: " + e.getMessage());
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			ALog.e(ALog.USER_REGISTRATION, "Test: " + e.getMessage());
			fail(e.getMessage());
		}
		
		ErrorType errorType = createAccountFragment.isInputValidated();
		assertTrue(ErrorType.NAME == errorType);
	}
	
	public void testIsInputValidatedNameEmpty() {
		try {
			nameField.set(createAccountFragment, "");
		} catch (IllegalAccessException e) {
			ALog.e(ALog.USER_REGISTRATION, "Test: " + e.getMessage());
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			ALog.e(ALog.USER_REGISTRATION, "Test: " + e.getMessage());
			fail(e.getMessage());
		}
		
		ErrorType errorType = createAccountFragment.isInputValidated();
		assertTrue(ErrorType.NAME == errorType);
	}
	
	public void testIsInputValidatedPasswordNull() {
		try {
			nameField.set(createAccountFragment, "abc");
			emailField.set(createAccountFragment, "abc@gmail.com");
			passField.set(createAccountFragment, null);
		} catch (IllegalAccessException e) {
			ALog.e(ALog.USER_REGISTRATION, "Test: " + e.getMessage());
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			ALog.e(ALog.USER_REGISTRATION, "Test: " + e.getMessage());
			fail(e.getMessage());
		}
		
		ErrorType errorType = createAccountFragment.isInputValidated();
		assertTrue(ErrorType.PASSWORD == errorType);
	}
	
	public void testIsInputValidatedPasswordEmpty() {
		try {
			nameField.set(createAccountFragment, "abc");
			emailField.set(createAccountFragment, "abc@gmail.com");
			passField.set(createAccountFragment, "");
		} catch (IllegalAccessException e) {
			ALog.e(ALog.USER_REGISTRATION, "Test: " + e.getMessage());
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			ALog.e(ALog.USER_REGISTRATION, "Test: " + e.getMessage());
			fail(e.getMessage());
		}
		
		ErrorType errorType = createAccountFragment.isInputValidated();
		assertTrue(ErrorType.PASSWORD == errorType);
	}
	
	public void testIsInputValidatedPasswordLessThanSix() {
		try {
			nameField.set(createAccountFragment, "abc");
			emailField.set(createAccountFragment, "abc@gmail.com");
			passField.set(createAccountFragment, "1234");
		} catch (IllegalAccessException e) {
			ALog.e(ALog.USER_REGISTRATION, "Test: " + e.getMessage());
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			ALog.e(ALog.USER_REGISTRATION, "Test: " + e.getMessage());
			fail(e.getMessage());
		}
		
		ErrorType errorType = createAccountFragment.isInputValidated();
		assertTrue(ErrorType.PASSWORD == errorType);
	}
	
}
 