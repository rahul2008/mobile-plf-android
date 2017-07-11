package com.philips.platform.ths.intake;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class THSVitalsTemperatureValidatorTest {
    THSVitalsTemperatureValidator thsVitalsTemperatureValidator;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        thsVitalsTemperatureValidator = new THSVitalsTemperatureValidator();
    }

    @Test
    public void validateCorrectinput() throws Exception {
        boolean validate = thsVitalsTemperatureValidator.validate("30.0000000");
        assertTrue(validate);
    }

    @Test
    public void validateboundryLastinput() throws Exception {
        boolean validate = thsVitalsTemperatureValidator.validate("120.888999");
        assertFalse(validate);
    }

    @Test
    public void validateboundryFirstinput() throws Exception {
        boolean validate = thsVitalsTemperatureValidator.validate("0.00000");
        assertFalse(validate);
    }

    @Test
    public void validateboundryBigRangeinput() throws Exception {
        boolean validate = thsVitalsTemperatureValidator.validate("9999999999999999");
        assertFalse(validate);
    }

    @Test
    public void validateboundryNegativeinput() throws Exception {
        boolean validate = thsVitalsTemperatureValidator.validate("-100");
        assertFalse(validate);
    }

    @Test
    public void validatenullinput() throws Exception {
        boolean validate = thsVitalsTemperatureValidator.validate(null);
        assertFalse(validate);
    }

    @Test
    public void validateEmptyinput() throws Exception {
        boolean validate = thsVitalsTemperatureValidator.validate("");
        assertFalse(validate);
    }

}