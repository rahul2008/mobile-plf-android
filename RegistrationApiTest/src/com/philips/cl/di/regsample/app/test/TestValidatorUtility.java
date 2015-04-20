package com.philips.cl.di.regsample.app.test;

import com.philips.cl.di.reg.ui.utils.ValidatorUtility;

public class TestValidatorUtility extends MockedTestCase{
	
	// email validation tests
	public void testValidateEmail() {
		boolean result = ValidatorUtility.isValidEmail("sampath.kumar@yahoo.com");
		assertTrue(result);
	}
	
	public void testValidatEmailIsNull() {
		boolean result = ValidatorUtility.isValidEmail(null);
		assertFalse(result);
	}
	
	public void testValidateEmailIsEmpty() {
		boolean result = ValidatorUtility.isValidEmail("");
		assertFalse(result);
	}
	
	public void testValidateEmailCase1() {
		boolean result = ValidatorUtility.isValidEmail("sampath.kumar@yahoo");
		assertFalse(result);
	}
	
	public void testValidateEmailCase2() {
		boolean result = ValidatorUtility.isValidEmail("sampath.yahoo");
		assertFalse(result);
	}
	
	public void testValidateEmailCase3() {
		boolean result = ValidatorUtility.isValidEmail("@yahoo.com");
		assertFalse(result);
	}
	
	public void testValidateEmailCase4() {
		boolean result = ValidatorUtility.isValidEmail("sam@path@yahoo.com");
		assertFalse(result);
	}
	
	public void testValidateEmailCase5() {
		boolean result = ValidatorUtility.isValidEmail("sam;path@yahoo.co.in");
		assertFalse(result);
	}
	
	public void testValidateEmailCase6() {
		boolean result = ValidatorUtility.isValidEmail("sam_path@yahoo.co");
		assertTrue(result);
	}
	
	public void testValidateEmailCase7() {
		boolean result = ValidatorUtility.isValidEmail("sampath@yah.co.in");
		assertTrue(result);
	}
	
	public void testValidateEmailCase8() {
		boolean result = ValidatorUtility.isValidEmail("sam.path@yahoo.co.in.nl");
		assertTrue(result);
	}
	
	public void testValidateEmailCase9() {
		boolean result = ValidatorUtility.isValidEmail("sam path@yahoo.com");
		assertFalse(result);
	}
	
	public void testValidateEmailCase10() {
		boolean result = ValidatorUtility.isValidEmail("sampath@yahoo.com ");
		assertFalse(result);
	}
	
	public void testValidateEmailCase11() {
		boolean result = ValidatorUtility.isValidEmail("sampath@ yahoo.com");
		assertFalse(result);
	}
	
	public void testValidateEmailCase12() {
		boolean result = ValidatorUtility.isValidEmail("sampath#yahoo.com");
		assertFalse(result);
	}
	
	// password validation tests
	
	public void testValidatePassword() {
		boolean result = ValidatorUtility.isValidPassword("Sams@1234");
		assertTrue(result);
	}
	
	public void testValidatPasswordIsNull() {
		boolean result = ValidatorUtility.isValidPassword(null);
		assertFalse(result);
	}
	
	public void testValidatePasswordIsEmpty() {
		boolean result = ValidatorUtility.isValidPassword("");
		assertFalse(result);
	}
	
	public void testValidatePasswordCase1() {
		boolean result = ValidatorUtility.isValidPassword("sam@12");
		assertFalse(result);
	}
	
	public void testValidatePasswordCase2() {
		boolean result = ValidatorUtility.isValidPassword("sam123");
		assertFalse(result);
	}
	
	public void testValidatePasswordCase3() {
		boolean result = ValidatorUtility.isValidPassword("Sampathtn");
		assertFalse(result);
	}
	
	public void testValidatePasswordCase4() {
		boolean result = ValidatorUtility.isValidPassword("12345678");
		assertFalse(result);
	}
	
	public void testValidatePasswordCase5() {
		boolean result = ValidatorUtility.isValidPassword("!@#$%^&*");
		assertFalse(result);
	}
	
	public void testValidatePasswordCase6() {
		boolean result = ValidatorUtility.isValidPassword("sams1234");
		assertTrue(result);
	}
	
	public void testValidatePasswordCase7() {
		boolean result = ValidatorUtility.isValidPassword("SAMS1234");
		assertTrue(result);
	}
	
	public void testValidatePasswordCase8() {
		boolean result = ValidatorUtility.isValidPassword("1234!@#$");
		assertTrue(result);
	}
	
	public void testValidatePasswordCase9() {
		boolean result = ValidatorUtility.isValidPassword("sams!@#$");
		assertTrue(result);
	}
	
	public void testValidatePasswordCase10() {
		boolean result = ValidatorUtility.isValidPassword("SAMS!@#$");
		assertTrue(result);
	}
	
	public void testValidatePasswordCase11() {
		boolean result = ValidatorUtility.isValidPassword("sAM!@#123");
		assertTrue(result);
	}
	
	// name validation tests
	
	public void testValidateName() {
		boolean result = ValidatorUtility.isValidName("Sampath kumar");
		assertTrue(result);
	}
	
	public void testValidateNameIsNull() {
		boolean result = ValidatorUtility.isValidName(null);
		assertFalse(result);
	}
	
	public void testValidateNameIsEmpty() {
		boolean result = ValidatorUtility.isValidName("");
		assertFalse(result);
	}
	
	public void testValidateNameCase1() {
		boolean result = ValidatorUtility.isValidName("      ");
		assertFalse(result);
	}
	
	public void testValidateNameCase2() {
		boolean result = ValidatorUtility.isValidName("1234567890");
		assertTrue(result);
	}
	
	public void testValidateNameCase3() {
		boolean result = ValidatorUtility.isValidName("(!@#$%^&*)");
		assertTrue(result);
	}
	
	public void testValidateNameCase4() {
		boolean result = ValidatorUtility.isValidName("Sampath1234");
		assertTrue(result);
	}
	
	public void testValidateNameCase5() {
		boolean result = ValidatorUtility.isValidName("(Sampath%^&*)");
		assertTrue(result);
	}
}
