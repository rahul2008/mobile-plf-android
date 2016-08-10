/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.utils.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cdp.registration.ui.traditional.RegistrationActivity;
import com.philips.cdp.registration.ui.utils.FieldsValidator;


//import static org.mockito.Mockito.mock;

public class EmailValidatorTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {
	
	FieldsValidator mEmailValidator = null;

	public EmailValidatorTest() {
		super(RegistrationActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		//mEmailValidator = mock(EmailValidator.class);
	}
	
	
	@SmallTest
	public void testValidatEmailIsNull() {
		boolean result = FieldsValidator.isValidEmail(null);
		assertFalse(result);
	}
		
	public void testValidateEmailIsEmpty() {
		boolean result = FieldsValidator.isValidEmail("");
		assertFalse(result);
	}
	
	public void testValidateEmailCase1() {
		boolean result = FieldsValidator.isValidEmail("sampath.kumar@yahoo");
		assertFalse(result);
	}
	
	public void testValidateEmailCase2() {
		boolean result = FieldsValidator.isValidEmail("sampath.yahoo");
		assertFalse(result);
	}

	// email validation tests
		public void testValidateEmail() {
			boolean result = FieldsValidator.isValidEmail("sampath.kumar@yahoo.com");
			assertTrue(result);
	}
		
	public void testValidateEmailCase3() {
		boolean result = FieldsValidator.isValidEmail("@yahoo.com");
		assertFalse(result);
	}
	
	public void testValidateEmailCase4() {
		boolean result = FieldsValidator.isValidEmail("sam@path@yahoo.com");
		assertFalse(result);
	}
	
	public void testValidateEmailCase5() {
		boolean result = FieldsValidator.isValidEmail("sam;path@yahoo.co.in");
		assertFalse(result);
	}
	
	public void testValidateEmailCase6() {
		boolean result = FieldsValidator.isValidEmail("sam_path@yahoo.co");
		assertTrue(result);
	}
	
	public void testValidateEmailCase7() {
		boolean result = FieldsValidator.isValidEmail("sampath@yah.co.in");
		assertTrue(result);
	}
	
	public void testValidateEmailCase8() {
		boolean result = FieldsValidator.isValidEmail("sam.path@yahoo.co.in.nl");
		assertTrue(result);
	}
	
	public void testValidateEmailCase9() {
		boolean result = FieldsValidator.isValidEmail("sam path@yahoo.com");
		assertFalse(result);
	}
	
	public void testValidateEmailCase10() {
		boolean result = FieldsValidator.isValidEmail("sampath@yahoo.com ");
		assertFalse(result);
	}
	
	public void testValidateEmailCase11() {
		boolean result = FieldsValidator.isValidEmail("sampath@ yahoo.com");
		assertFalse(result);
	}
	
	public void testValidateEmailCase12() {
		boolean result = FieldsValidator.isValidEmail("sampath#yahoo.com");
		assertFalse(result);
	}
	
	// password validation tests
	
	public void testValidatePassword() {
		boolean result = FieldsValidator.isValidPassword("Sams@1234");
		assertTrue(result);
	}
	
	public void testValidatPasswordIsNull() {
		boolean result = FieldsValidator.isValidPassword(null);
		assertFalse(result);
	}
	
	public void testValidatePasswordIsEmpty() {
		boolean result = FieldsValidator.isValidPassword("");
		assertFalse(result);
	}
	
	public void testValidatePasswordCase1() {
		boolean result = FieldsValidator.isValidPassword("sam@12");
		assertFalse(result);
	}
	
	public void testValidatePasswordCase2() {
		boolean result = FieldsValidator.isValidPassword("sam123");
		assertFalse(result);
	}
	
	public void testValidatePasswordCase3() {
		boolean result = FieldsValidator.isValidPassword("Sampathtn");
		assertFalse(result);
	}
	
	public void testValidatePasswordCase4() {
		boolean result = FieldsValidator.isValidPassword("12345678");
		assertFalse(result);
	}
	
	public void testValidatePasswordCase5() {
		boolean result = FieldsValidator.isValidPassword("!@#$%^&*");
		assertFalse(result);
	}
	
	public void testValidatePasswordCase6() {
		boolean result = FieldsValidator.isValidPassword("sams1234");
		assertTrue(result);
	}
	
	public void testValidatePasswordCase7() {
		boolean result = FieldsValidator.isValidPassword("SAMS1234");
		assertTrue(result);
	}
	
	public void testValidatePasswordCase8() {
		boolean result = FieldsValidator.isValidPassword("1234!@#$");
		assertTrue(result);
	}
	
	public void testValidatePasswordCase9() {
		boolean result = FieldsValidator.isValidPassword("sams!@#$");
		assertTrue(result);
	}
	
	public void testValidatePasswordCase10() {
		boolean result = FieldsValidator.isValidPassword("SAMS!@#$");
		assertTrue(result);
	}
	
	public void testValidatePasswordCase11() {
		boolean result = FieldsValidator.isValidPassword("sAM!@#123");
		assertTrue(result);
	}
	public void testIsPasswordLengthMeetsCase12(){
		boolean result = FieldsValidator.isPasswordLengthMeets("sAM!@#123");
		assertTrue(result);
	}

	public void testIsPasswordLengthMeetsCase13(){
		boolean result = FieldsValidator.isPasswordLengthMeets(null);
		assertFalse(result);
	}

	public void testIsNumberPresentCase1()
	{
		boolean result = FieldsValidator.isNumberPresent(null);
		assertFalse(result);
	}
	public void testIsNumberPresentCase2()
	{
		boolean result = FieldsValidator.isNumberPresent("");
		assertFalse(result);
	}
	public void testIsNumberPresentCase3()
	{
		boolean result = FieldsValidator.isNumberPresent("sAM!@#123");
		assertTrue(result);
	}
	public void testIsSymbolsPresentCase1()
	{
		boolean result = FieldsValidator.isSymbolsPresent(null);
		assertFalse(result);
	}
	public void testIsSymbolsPresentCase2()
	{
		boolean result = FieldsValidator.isSymbolsPresent("");
		assertFalse(result);
	}
	public void testIsSymbolsPresentCase3()
	{
		boolean result = FieldsValidator.isSymbolsPresent("sAM!@#123");
		assertTrue(result);
	}
	public void testIsAlphabetPresentCase1()
	{
		boolean result = FieldsValidator.isAlphabetPresent(null);
		assertFalse(result);
	}
	public void testIsAlphabetPresentCase2()
	{
		boolean result = FieldsValidator.isAlphabetPresent("");
		assertFalse(result);
	}
	public void testIsAlphabetPresentCase3()
	{
		boolean result = FieldsValidator.isAlphabetPresent("sAM!@#123");
		assertTrue(result);
	}
	public void testIsValidSerialNoCase1()
	{
		boolean result = FieldsValidator.isValidSerialNo(null);
		assertFalse(result);
	}
	public void testIsValidSerialNoCase2()
	{
		boolean result = FieldsValidator.isValidSerialNo("");
		assertFalse(result);
	}
	public void testIsValidSerialNoCase3()
	{
		boolean result = FieldsValidator.isValidSerialNo("sAM!@#123");
		assertFalse(result);
	}
	public void testIsValidSerialNoCase4()
	{
		boolean result = FieldsValidator.isValidSerialNo("AM1234567890123");
		assertFalse(result);
	}
	
	// name validation tests
	
	public void testValidateName() {
		boolean result = FieldsValidator.isValidName("Sampath kumar");
		assertTrue(result);
	}
	
	public void testValidateNameIsNull() {
		boolean result = FieldsValidator.isValidName(null);
		assertFalse(result);
	}
	
	public void testValidateNameIsEmpty() {
		boolean result = FieldsValidator.isValidName("");
		assertFalse(result);
	}
	
	public void testValidateNameCase2() {
		boolean result = FieldsValidator.isValidName("1234567890");
		assertTrue(result);
	}
	
	public void testValidateNameCase3() {
		boolean result = FieldsValidator.isValidName("(!@#$%^&*)");
		assertTrue(result);
	}
	
	public void testValidateNameCase4() {
		boolean result = FieldsValidator.isValidName("Sampath1234");
		assertTrue(result);
	}
	
	public void testValidateNameCase5() {
		boolean result = FieldsValidator.isValidName("(Sampath%^&*)");
		assertTrue(result);
	}
}
