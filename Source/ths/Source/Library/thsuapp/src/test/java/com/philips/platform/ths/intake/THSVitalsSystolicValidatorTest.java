package com.philips.platform.ths.intake;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class THSVitalsSystolicValidatorTest {
    THSVitalsSystolicValidator thsVitalsSystolicValidator;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        thsVitalsSystolicValidator = new THSVitalsSystolicValidator();
    }

    @Test
    public void validateCorrectinput() throws Exception {
        boolean validate = thsVitalsSystolicValidator.validate("30");
        assertTrue(validate);
    }

    @Test
    public void validateboundryLastinput() throws Exception {
        boolean validate = thsVitalsSystolicValidator.validate("250");
        assertFalse(validate);
    }

    @Test
    public void validateboundryFirstinput() throws Exception {
        boolean validate = thsVitalsSystolicValidator.validate("-1");
        assertFalse(validate);
    }

    @Test(expected = NumberFormatException.class)
    public void validateboundryBigRangeinput() throws Exception {
        boolean validate = thsVitalsSystolicValidator.validate("9999999999999999");
        assertFalse(validate);
    }

    @Test
    public void validateboundryNegativeinput() throws Exception {
        boolean validate = thsVitalsSystolicValidator.validate("-100");
        assertFalse(validate);
    }

    @Test
    public void validatenullinput() throws Exception {
        boolean validate = thsVitalsSystolicValidator.validate(null);
        assertFalse(validate);
    }

    @Test
    public void validateEmptyinput() throws Exception {
        boolean validate = thsVitalsSystolicValidator.validate("");
        assertFalse(validate);
    }
}