package com.philips.cdp.di.iap.utils;

import com.philips.cdp.di.iap.address.Validator;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * Created by philips on 9/26/17.
 */
public class InputValidatorTest {

    InputValidator inputValidator;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        inputValidator=new InputValidator(Validator.NAME_PATTERN);
    }

    @Test
    public void isValidName() throws Exception {

        inputValidator=new InputValidator(Validator.NAME_PATTERN);
        Assert.assertEquals(true,inputValidator.isValidName("abcd"));
    }

    @Test
    public void isValidEmail() throws Exception {
        inputValidator=new InputValidator(Validator.EMAIL_PATTERN);
        Assert.assertEquals(true,inputValidator.isValidEmail("abcd@gmail.com"));
    }

    @Test
    public void isValidPostalCode() throws Exception {

        inputValidator=new InputValidator(Validator.POSTAL_CODE_PATTERN);
        Assert.assertEquals(true,inputValidator.isValidPostalCode("123456"));
    }

    @Test
    public void isValidPhoneNumber() throws Exception {

        inputValidator=new InputValidator(Validator.PHONE_NUMBER_PATTERN);
        Assert.assertEquals(true,inputValidator.isValidPhoneNumber("8904291902"));
    }

    @Test
    public void isValidAddress() throws Exception {
        inputValidator=new InputValidator(Validator.ADDRESS_PATTERN);
        Assert.assertEquals(true,inputValidator.isValidAddress("Bangalore"));
    }

    @Test
    public void isValidTown() throws Exception {
        inputValidator=new InputValidator(Validator.TOWN_PATTERN);
        Assert.assertEquals(true,inputValidator.isValidTown("Bangalore"));
    }

    @Test
    public void isValidCountry() throws Exception {
        inputValidator=new InputValidator(Validator.COUNTRY_PATTERN);
        Assert.assertEquals(true,inputValidator.isValidCountry("India"));
    }

    @Test
    public void validate() throws Exception {
        inputValidator.validate("India");
    }

}