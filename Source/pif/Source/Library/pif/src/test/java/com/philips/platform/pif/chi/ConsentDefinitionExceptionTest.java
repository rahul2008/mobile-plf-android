package com.philips.platform.pif.chi;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConsentDefinitionExceptionTest {

    private ConsentDefinitionException subject;

    @Test
    public void itShouldNotModifyErrorMessage() throws Exception {
        subject = new ConsentDefinitionException("oops");
        assertEquals("oops", subject.getMessage());
    }
}