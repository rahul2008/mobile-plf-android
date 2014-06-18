package com.philips.cl.di.dev.pa.test;

import junit.framework.TestCase;

import com.philips.cl.di.dev.pa.registration.EmailValidator;

public class EmailValidatorTest extends TestCase {
	
	public void testValidate() {
		boolean result = EmailValidator.getInstance().validate("akash@philips.com");
		assertTrue(result);
	}
	
	public void testValidateNull() {
		boolean result = EmailValidator.getInstance().validate(null);
		assertFalse(result);
	}
	
	public void testValidateEmpty() {
		boolean result = EmailValidator.getInstance().validate("");
		assertFalse(result);
	}
	
	public void testValidateNegative1() {
		boolean result = EmailValidator.getInstance().validate("akash@philips");
		assertFalse(result);
	}
	
	public void testValidateNegative2() {
		boolean result = EmailValidator.getInstance().validate("akash.philips");
		assertFalse(result);
	}
	
	public void testValidateNegative3() {
		boolean result = EmailValidator.getInstance().validate("@philips.com");
		assertFalse(result);
	}
	
	public void testValidateNegative4() {
		boolean result = EmailValidator.getInstance().validate("aka@sh@philips.com");
		assertFalse(result);
	}
	
	public void testValidateNegative5() {
		boolean result = EmailValidator.getInstance().validate("aka;sh@philips.co.in");
		assertFalse(result);
	}
	
	public void testValidatePositive1() {
		boolean result = EmailValidator.getInstance().validate("aka_sh@philips.co");
		assertTrue(result);
	}
	
	public void testValidatePositive2() {
		boolean result = EmailValidator.getInstance().validate("akash@ph.co.in");
		assertTrue(result);
	}
	
	public void testValidatePositive3() {
		boolean result = EmailValidator.getInstance().validate("aka.sh@philips.co.in.nl");
		assertTrue(result);
	}
}
