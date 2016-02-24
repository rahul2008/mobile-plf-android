package com.philips.cdp.di.iap.address;

import junit.framework.TestCase;

import org.junit.Test;

public class ValidatorTest extends TestCase {

    private Validator validator = new Validator();

    @Test
    public void testIsValidFirstName() {
        assertTrue(validator.isValidFirstName("android"));
        assertFalse(validator.isValidFirstName("android1"));
        assertFalse(validator.isValidFirstName(""));
        assertFalse(validator.isValidFirstName(null));
    }

    @Test
    public void testIsValidLastName() {
        assertTrue(validator.isValidLastName("android"));
        assertFalse(validator.isValidLastName("android1"));
        assertFalse(validator.isValidLastName(""));
        assertFalse(validator.isValidLastName(null));
    }

    @Test
    public void testIsValidCountry() {
        assertTrue(validator.isValidCountry("US"));
        assertFalse(validator.isValidCountry("in"));
        assertFalse(validator.isValidCountry(""));
        assertFalse(validator.isValidCountry(null));
    }

    @Test
    public void testIsValidEmail() {
        /*assertTrue(validator.isValidEmail("abc@gmail.com"));
        assertFalse(validator.isValidEmail("hgjhj##"));
        assertFalse(validator.isValidEmail(""));
        assertFalse(validator.isValidEmail(null));*/
    }

    @Test
    public void testIsValidTown() {
        assertTrue(validator.isValidTown("bangalore"));
        assertFalse(validator.isValidTown("#%#%#$^"));
        assertFalse(validator.isValidTown(""));
        assertFalse(validator.isValidTown(null));
    }

    @Test
    public void testIsValidPhoneNumber() {
        assertTrue(validator.isValidPhoneNumber("78578758958"));
        assertFalse(validator.isValidPhoneNumber("#%#%#$^"));
        assertFalse(validator.isValidPhoneNumber(""));
        assertFalse(validator.isValidPhoneNumber(null));
    }

    @Test
    public void testIsValidAddress() {
        assertTrue(validator.isValidAddress("gjhgudghjh"));
        assertFalse(validator.isValidAddress("#%#%#$^"));
        assertFalse(validator.isValidAddress(""));
        assertFalse(validator.isValidAddress(null));
    }

    @Test
    public void testIsValidPostalCode() {
        assertTrue(validator.isValidPostalCode("6767678"));
        assertFalse(validator.isValidPostalCode("#%#%#$^"));
        assertFalse(validator.isValidPostalCode(""));
        assertFalse(validator.isValidPostalCode(null));
    }
}