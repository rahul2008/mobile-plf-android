/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.registration.ui.utils;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class FieldsValidatorTest {

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void shouldReturnFalse_WhenNull() {
        boolean validEmail = FieldsValidator.isValidEmail(null);
        assertFalse(validEmail);
    }

    @Test
    public void shouldReturnFalse_WhenEmpty() {
        boolean validEmail = FieldsValidator.isValidEmail(" ");
        assertFalse(validEmail);
    }

    @Test
    public void shouldReturnFalse_WhenLengthZero() {
        boolean validEmail = FieldsValidator.isValidEmail("");
        assertFalse(validEmail);
    }

    @Test
    public void shouldReturnFalse_WhenInvalid() {
        boolean validEmail = FieldsValidator.isValidEmail("kdjfajfods");
        assertFalse(validEmail);
    }

    @Test
    public void shouldReturnTrue_WhenValid() {
        boolean validEmail = FieldsValidator.isValidEmail("sample@philips.com");
        Assert.assertTrue(validEmail);
    }

    @Test
    public void shouldReturnTrue_WhenValidSingleChar() {
        boolean validEmail = FieldsValidator.isValidEmail("s@philips.com");
        Assert.assertTrue(validEmail);
    }

    @Test
    public void shouldReturnTrue_WhenValidSingleSpecialChar() {
        boolean validEmail = FieldsValidator.isValidEmail("_@philips.com");
        Assert.assertTrue(validEmail);
    }

    @Test
    public void shouldReturnTrue_WhenValidWithSpecialChar() {
        boolean validEmail = FieldsValidator.isValidEmail("_d@philips.com");
        Assert.assertTrue(validEmail);
    }

    @Test
    public void shouldReturnTrue_WhenValidWithUpperCharMixed() {
        boolean validEmail = FieldsValidator.isValidEmail("UKr@philips.com");
        Assert.assertTrue(validEmail);
    }

    @Test
    public void shouldReturnFalse_WhenValidWithUpperChar() {
        boolean validEmail = FieldsValidator.isValidEmail("UKK@philips.com");
        Assert.assertTrue(validEmail);
    }

    @Test
    public void testMobileNumberNotNull() {
        String verifiedMobileStr = FieldsValidator.getVerifiedMobileNumber("j6j2mrs", "f47ac10b-58cc-4372-a567-0e02b2c3d479");
        assertNotNull(verifiedMobileStr);
    }

    @Test
    public void testMobileNumberisValid() {
        String verifiedMobileStr = FieldsValidator.getVerifiedMobileNumber("f47ac10b-58cc-4372-a567-0e02b2c3d479", "j6j2mrs");
        assertEquals("j6j2mrsf47acb58cc4372a567e2b2c3d", verifiedMobileStr);
    }

    @Test
    public void testValidateEmailIsEmpty() {
        boolean result = FieldsValidator.isValidEmail("");
        assertFalse(result);
    }

    @Test
    public void testValidateEmailCase1() {
        boolean result = FieldsValidator.isValidEmail("sampath.kumar@yahoo");
        assertFalse(result);
    }

    @Test
    public void testValidateEmailCase2() {
        boolean result = FieldsValidator.isValidEmail("sampath.yahoo");
        assertFalse(result);
    }

    @Test
    // email validation tests
    public void testValidateEmail() {
        boolean result = FieldsValidator.isValidEmail("sampath.kumar@yahoo.com");
        assertTrue(result);
    }

    @Test
    public void testValidateEmailCase3() {
        boolean result = FieldsValidator.isValidEmail("@yahoo.com");
        assertFalse(result);
    }

    @Test
    public void testValidateEmailCase4() {
        boolean result = FieldsValidator.isValidEmail("sam@path@yahoo.com");
        assertFalse(result);
    }

    @Test
    public void testValidateEmailCase5() {
        boolean result = FieldsValidator.isValidEmail("sam;path@yahoo.co.in");
        assertFalse(result);
    }

    @Test
    public void testValidateEmailCase6() {
        boolean result = FieldsValidator.isValidEmail("sam_path@yahoo.co");
        assertTrue(result);
    }

    @Test
    public void testValidateEmailCase7() {
        boolean result = FieldsValidator.isValidEmail("sampath@yah.co.in");
        assertTrue(result);
    }

    @Test
    public void testValidateEmailCase8() {
        boolean result = FieldsValidator.isValidEmail("sam.path@yahoo.co.in.nl");
        assertTrue(result);
    }

    @Test
    public void testValidateEmailCase9() {
        boolean result = FieldsValidator.isValidEmail("sam path@yahoo.com");
        assertFalse(result);
    }

    @Test
    public void testValidateEmailCase10() {
        boolean result = FieldsValidator.isValidEmail("sampath@yahoo.com ");
        assertFalse(result);
    }

    @Test
    public void testValidateEmailCase11() {
        boolean result = FieldsValidator.isValidEmail("sampath@ yahoo.com");
        assertFalse(result);
    }

    @Test
    public void testValidateEmailCase12() {
        boolean result = FieldsValidator.isValidEmail("sampath#yahoo.com");
        assertFalse(result);
    }

    // password validation tests
    @Test
    public void testValidatePassword() {
        boolean result = FieldsValidator.isValidPassword("Sams@1234");
        assertTrue(result);
    }

    @Test
    public void testValidatPasswordIsNull() {
        boolean result = FieldsValidator.isValidPassword(null);
        assertFalse(result);
    }

    @Test
    public void testValidatePasswordIsEmpty() {
        boolean result = FieldsValidator.isValidPassword("");
        assertFalse(result);
    }

    @Test
    public void testValidatePasswordCase1() {
        boolean result = FieldsValidator.isValidPassword("sam@12");
        assertFalse(result);
    }

    @Test
    public void testValidatePasswordCase2() {
        boolean result = FieldsValidator.isValidPassword("sam123");
        assertFalse(result);
    }

    @Test
    public void testValidatePasswordCase3() {
        boolean result = FieldsValidator.isValidPassword("Sampathtn");
        assertFalse(result);
    }

    @Test
    public void testValidatePasswordCase4() {
        boolean result = FieldsValidator.isValidPassword("12345678");
        assertFalse(result);
    }

    @Test
    public void testValidatePasswordCase5() {
        boolean result = FieldsValidator.isValidPassword("!@#$%^&*");
        assertFalse(result);
    }

    @Test
    public void testValidatePasswordCase6() {
        boolean result = FieldsValidator.isValidPassword("sams1234");
        assertTrue(result);
    }

    @Test
    public void testValidatePasswordCase7() {
        boolean result = FieldsValidator.isValidPassword("SAMS1234");
        assertTrue(result);
    }

    @Test
    public void testValidatePasswordCase8() {
        boolean result = FieldsValidator.isValidPassword("1234!@#$");
        assertTrue(result);
    }

    @Test
    public void testValidatePasswordCase9() {
        boolean result = FieldsValidator.isValidPassword("sams!@#$");
        assertTrue(result);
    }

    @Test
    public void testValidatePasswordCase10() {
        boolean result = FieldsValidator.isValidPassword("SAMS!@#$");
        assertTrue(result);
    }

    @Test
    public void testValidatePasswordCase11() {
        boolean result = FieldsValidator.isValidPassword("sAM!@#123");
        assertTrue(result);
    }

    @Test
    public void testIsPasswordLengthMeetsCase12() {
        boolean result = FieldsValidator.isPasswordLengthMeets("sAM!@#123");
        assertTrue(result);
    }

    @Test
    public void testIsPasswordLengthMeetsCase13() {
        boolean result = FieldsValidator.isPasswordLengthMeets(null);
        assertFalse(result);
    }

    @Test
    public void testIsNumberPresentCase1() {
        boolean result = FieldsValidator.isNumberPresent(null);
        assertFalse(result);
    }

    @Test
    public void testIsNumberPresentCase2() {
        boolean result = FieldsValidator.isNumberPresent("");
        assertFalse(result);
    }

    @Test
    public void testIsNumberPresentCase3() {
        boolean result = FieldsValidator.isNumberPresent("sAM!@#123");
        assertTrue(result);
    }

    @Test
    public void testIsSymbolsPresentCase1() {
        boolean result = FieldsValidator.isSymbolsPresent(null);
        assertFalse(result);
    }

    @Test
    public void testIsSymbolsPresentCase2() {
        boolean result = FieldsValidator.isSymbolsPresent("");
        assertFalse(result);
    }

    @Test
    public void testIsSymbolsPresentCase3() {
        boolean result = FieldsValidator.isSymbolsPresent("sAM!@#123");
        assertTrue(result);
    }

    @Test
    public void testIsAlphabetPresentCase1() {
        boolean result = FieldsValidator.isAlphabetPresent(null);
        assertFalse(result);
    }

    @Test
    public void testIsAlphabetPresentCase2() {
        boolean result = FieldsValidator.isAlphabetPresent("");
        assertFalse(result);
    }

    @Test
    public void testIsAlphabetPresentCase3() {
        boolean result = FieldsValidator.isAlphabetPresent("sAM!@#123");
        assertTrue(result);
    }

    @Test
    public void testIsValidSerialNoCase1() {
        boolean result = FieldsValidator.isValidSerialNo(null);
        assertFalse(result);
    }

    @Test
    public void testIsValidSerialNoCase2() {
        boolean result = FieldsValidator.isValidSerialNo("");
        assertFalse(result);
    }

    @Test
    public void testIsValidSerialNoCase3() {
        boolean result = FieldsValidator.isValidSerialNo("sAM!@#123");
        assertFalse(result);
    }

    @Test
    public void testIsValidSerialNoCase4() {
        boolean result = FieldsValidator.isValidSerialNo("AM1234567890123");
        assertFalse(result);
    }

    // name validation tests
    @Test
    public void testValidateName() {
        boolean result = FieldsValidator.isValidName("Sampath kumar");
        assertTrue(result);
    }

    @Test
    public void testValidateNameIsNull() {
        boolean result = FieldsValidator.isValidName(null);
        assertFalse(result);
    }

    @Test
    public void testValidateNameIsEmpty() {
        boolean result = FieldsValidator.isValidName("");
        assertFalse(result);
    }

    @Test
    public void testValidateNameCase2() {
        boolean result = FieldsValidator.isValidName("1234567890");
        assertTrue(result);
    }

    @Test
    public void testValidateNameCase3() {
        boolean result = FieldsValidator.isValidName("(!@#$%^&*)");
        assertTrue(result);
    }

    @Test
    public void testValidateNameCase4() {
        boolean result = FieldsValidator.isValidName("Sampath1234");
        assertTrue(result);
    }

    @Test
    public void testValidateNameCase5() {
        boolean result = FieldsValidator.isValidName("(Sampath%^&*)");
        assertTrue(result);
    }

}