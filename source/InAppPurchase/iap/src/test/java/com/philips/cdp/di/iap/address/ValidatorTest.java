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
        assertTrue(validator.isValidFirstName("android"));
        assertFalse(validator.isValidFirstName("android1"));
        assertFalse(validator.isValidFirstName(""));
        assertFalse(validator.isValidFirstName(null));
    }

    @Test
    public void testIsValidCountry() {

    }

    @Test
    public void testIsValidEmail() {
    }

    @Test
    public void testIsValidTown() {
    }

    @Test
    public void testIsValidPhoneNumber() {
    }

    @Test
    public void testIsValidAddress() {
    }

    @Test
    public void testIsValidPostalCode() {
    }
}