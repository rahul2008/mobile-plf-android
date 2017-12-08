package com.philips.platform.ths.intake;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class THSVitalsWeightValidatorTest {
    THSVitalsWeightValidator thsVitalsWeightValidator;

    @Before
    public void setUp() throws Exception {
        thsVitalsWeightValidator = new THSVitalsWeightValidator();
    }

    @Test
    public void validateCorrectinput() throws Exception {
        boolean validate = thsVitalsWeightValidator.validate("30");
        assertTrue(validate);
    }

    @Test
    public void validateboundryLastinput() throws Exception {
        boolean validate = thsVitalsWeightValidator.validate("500");
        assertFalse(validate);
    }

    @Test
    public void validateboundryFirstinput() throws Exception {
        boolean validate = thsVitalsWeightValidator.validate("0");
        assertFalse(validate);
    }

    @Test(expected = NumberFormatException.class)
    public void validateboundryBigRangeinput() throws Exception {
        boolean validate = thsVitalsWeightValidator.validate("9999999999999999");
        assertFalse(validate);
    }

    @Test
    public void validateboundryNegativeinput() throws Exception {
        boolean validate = thsVitalsWeightValidator.validate("-100");
        assertFalse(validate);
    }

    @Test
    public void validatenullinput() throws Exception {
        boolean validate = thsVitalsWeightValidator.validate(null);
        assertFalse(validate);
    }

    @Test
    public void validateEmptyinput() throws Exception {
        boolean validate = thsVitalsWeightValidator.validate("");
        assertFalse(validate);
    }

}