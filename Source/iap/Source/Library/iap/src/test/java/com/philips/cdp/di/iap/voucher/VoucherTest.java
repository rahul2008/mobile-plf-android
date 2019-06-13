/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.voucher;

import com.philips.cdp.di.iap.address.Validator;

import junit.framework.TestCase;

import org.junit.Test;

public class VoucherTest extends TestCase {

    private Validator validator = new Validator();

    @Test
    public void testIsValidFirstName() {
        assertTrue(validator.isValidName("android"));
        assertTrue(validator.isValidName("android1"));
        assertFalse(validator.isValidName(""));
        assertFalse(validator.isValidName(null));
    }

    @Test
    public void testIsValidLastName() {
        assertTrue(validator.isValidName("android"));
        assertTrue(validator.isValidName("android1"));
        assertFalse(validator.isValidName(""));
        assertFalse(validator.isValidName(null));
    }

    @Test
    public void testIsValidCountry() {
        assertTrue(validator.isValidCountry("US"));
        assertTrue(validator.isValidCountry("in"));
        assertFalse(validator.isValidCountry(""));
        assertFalse(validator.isValidCountry(null));
    }

    @Test
    public void testIsValidEmail() {
        assertTrue(validator.isValidEmail("abc@gmail.com"));
        assertFalse(validator.isValidEmail("hgjhj##"));
        assertFalse(validator.isValidEmail(""));
        assertFalse(validator.isValidEmail(null));
    }

    @Test
    public void testIsValidTown() {
        assertTrue(validator.isValidTown("bangalore"));
        assertTrue(validator.isValidTown("#%#%#$^"));
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
        assertTrue(validator.isValidAddress("#%#%#$^"));
        assertFalse(validator.isValidAddress(""));
        assertFalse(validator.isValidAddress(null));
    }

    @Test
    public void testIsValidPostalCode() {
        assertTrue(validator.isValidPostalCode("6767678"));
        assertTrue(validator.isValidPostalCode("#%#%#$^"));
        assertFalse(validator.isValidPostalCode(""));
        assertFalse(validator.isValidPostalCode(null));
    }
}