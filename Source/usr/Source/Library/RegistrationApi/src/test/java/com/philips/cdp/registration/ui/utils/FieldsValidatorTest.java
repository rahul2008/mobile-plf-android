package com.philips.cdp.registration.ui.utils;

import com.philips.cdp.registration.CustomRobolectricRunner;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

@RunWith(CustomRobolectricRunner.class)
public class FieldsValidatorTest {

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void shouldReturnFalse_WhenNull() {
        boolean validEmail = FieldsValidator.isValidEmail(null);
        Assert.assertFalse(validEmail);
    }

    @Test
    public void shouldReturnFalse_WhenEmpty() {
        boolean validEmail = FieldsValidator.isValidEmail(" ");
        Assert.assertFalse(validEmail);
    }
    @Test
    public void shouldReturnFalse_WhenLengthZero() {
        boolean validEmail = FieldsValidator.isValidEmail("");
        Assert.assertFalse(validEmail);
    }
    @Test
    public void shouldReturnFalse_WhenInvalid() {
        boolean validEmail = FieldsValidator.isValidEmail("kdjfajfods");
        Assert.assertFalse(validEmail);
    }
    @Test
    public void shouldReturnFalse_WhenValid() {
        boolean validEmail = FieldsValidator.isValidEmail("sample@philips.com");
        Assert.assertTrue(validEmail);
    }
    @Test
    public void shouldReturnFalse_WhenValidSingleChar() {
        boolean validEmail = FieldsValidator.isValidEmail("s@philips.com");
        Assert.assertTrue(validEmail);
    }
    @Test
    public void shouldReturnFalse_WhenValidSingleSpecialChar() {
        boolean validEmail = FieldsValidator.isValidEmail("_@philips.com");
        Assert.assertTrue(validEmail);
    }
    @Test
    public void shouldReturnFalse_WhenValidWithSpecialChar() {
        boolean validEmail = FieldsValidator.isValidEmail("_d@philips.com");
        Assert.assertTrue(validEmail);
    }
}