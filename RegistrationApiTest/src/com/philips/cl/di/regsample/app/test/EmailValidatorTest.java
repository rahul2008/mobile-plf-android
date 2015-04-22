package com.philips.cl.di.regsample.app.test;

import static org.mockito.Mockito.mock;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cl.di.reg.ui.utils.EmailValidator;
import com.philips.cl.di.regsample.app.RegistrationSampleActivity;

public class EmailValidatorTest extends ActivityInstrumentationTestCase2<RegistrationSampleActivity> {
	
	EmailValidator mEmailValidator = null;

	public EmailValidatorTest() {
		super(RegistrationSampleActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		mEmailValidator = mock(EmailValidator.class);
	}
	
	// email validation tests
	public void testValidateEmail() {
		boolean result = EmailValidator.isValidEmail("sampath.kumar@yahoo.com");
		assertTrue(result);
	}
	
	public void testValidatEmailIsNull() {
		boolean result = EmailValidator.isValidEmail(null);
		assertFalse(result);
	}
	
	public void testValidateEmailIsEmpty() {
		boolean result = EmailValidator.isValidEmail("");
		assertFalse(result);
	}
	
	public void testValidateEmailCase1() {
		boolean result = EmailValidator.isValidEmail("sampath.kumar@yahoo");
		assertFalse(result);
	}
	
	public void testValidateEmailCase2() {
		boolean result = EmailValidator.isValidEmail("sampath.yahoo");
		assertFalse(result);
	}
	
	public void testValidateEmailCase3() {
		boolean result = EmailValidator.isValidEmail("@yahoo.com");
		assertFalse(result);
	}
	
	public void testValidateEmailCase4() {
		boolean result = EmailValidator.isValidEmail("sam@path@yahoo.com");
		assertFalse(result);
	}
	
	public void testValidateEmailCase5() {
		boolean result = EmailValidator.isValidEmail("sam;path@yahoo.co.in");
		assertFalse(result);
	}
	
	public void testValidateEmailCase6() {
		boolean result = EmailValidator.isValidEmail("sam_path@yahoo.co");
		assertTrue(result);
	}
	
	public void testValidateEmailCase7() {
		boolean result = EmailValidator.isValidEmail("sampath@yah.co.in");
		assertTrue(result);
	}
	
	public void testValidateEmailCase8() {
		boolean result = EmailValidator.isValidEmail("sam.path@yahoo.co.in.nl");
		assertTrue(result);
	}
	
	public void testValidateEmailCase9() {
		boolean result = EmailValidator.isValidEmail("sam path@yahoo.com");
		assertFalse(result);
	}
	
	public void testValidateEmailCase10() {
		boolean result = EmailValidator.isValidEmail("sampath@yahoo.com ");
		assertFalse(result);
	}
	
	public void testValidateEmailCase11() {
		boolean result = EmailValidator.isValidEmail("sampath@ yahoo.com");
		assertFalse(result);
	}
	
	public void testValidateEmailCase12() {
		boolean result = EmailValidator.isValidEmail("sampath#yahoo.com");
		assertFalse(result);
	}
	
	// password validation tests
	
	public void testValidatePassword() {
		boolean result = EmailValidator.isValidPassword("Sams@1234");
		assertTrue(result);
	}
	
	public void testValidatPasswordIsNull() {
		boolean result = EmailValidator.isValidPassword(null);
		assertFalse(result);
	}
	
	public void testValidatePasswordIsEmpty() {
		boolean result = EmailValidator.isValidPassword("");
		assertFalse(result);
	}
	
	public void testValidatePasswordCase1() {
		boolean result = EmailValidator.isValidPassword("sam@12");
		assertFalse(result);
	}
	
	public void testValidatePasswordCase2() {
		boolean result = EmailValidator.isValidPassword("sam123");
		assertFalse(result);
	}
	
	public void testValidatePasswordCase3() {
		boolean result = EmailValidator.isValidPassword("Sampathtn");
		assertFalse(result);
	}
	
	public void testValidatePasswordCase4() {
		boolean result = EmailValidator.isValidPassword("12345678");
		assertFalse(result);
	}
	
	public void testValidatePasswordCase5() {
		boolean result = EmailValidator.isValidPassword("!@#$%^&*");
		assertFalse(result);
	}
	
	public void testValidatePasswordCase6() {
		boolean result = EmailValidator.isValidPassword("sams1234");
		assertTrue(result);
	}
	
	public void testValidatePasswordCase7() {
		boolean result = EmailValidator.isValidPassword("SAMS1234");
		assertTrue(result);
	}
	
	public void testValidatePasswordCase8() {
		boolean result = EmailValidator.isValidPassword("1234!@#$");
		assertTrue(result);
	}
	
	public void testValidatePasswordCase9() {
		boolean result = EmailValidator.isValidPassword("sams!@#$");
		assertTrue(result);
	}
	
	public void testValidatePasswordCase10() {
		boolean result = EmailValidator.isValidPassword("SAMS!@#$");
		assertTrue(result);
	}
	
	public void testValidatePasswordCase11() {
		boolean result = EmailValidator.isValidPassword("sAM!@#123");
		assertTrue(result);
	}
	
	// name validation tests
	
	public void testValidateName() {
		boolean result = EmailValidator.isValidName("Sampath kumar");
		assertTrue(result);
	}
	
	public void testValidateNameIsNull() {
		boolean result = EmailValidator.isValidName(null);
		assertFalse(result);
	}
	
	public void testValidateNameIsEmpty() {
		boolean result = EmailValidator.isValidName("");
		assertFalse(result);
	}
	
	public void testValidateNameCase2() {
		boolean result = EmailValidator.isValidName("1234567890");
		assertTrue(result);
	}
	
	public void testValidateNameCase3() {
		boolean result = EmailValidator.isValidName("(!@#$%^&*)");
		assertTrue(result);
	}
	
	public void testValidateNameCase4() {
		boolean result = EmailValidator.isValidName("Sampath1234");
		assertTrue(result);
	}
	
	public void testValidateNameCase5() {
		boolean result = EmailValidator.isValidName("(Sampath%^&*)");
		assertTrue(result);
	}
}
