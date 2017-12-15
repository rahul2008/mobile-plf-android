package com.philips.platform.consenthandlerinterface;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Entreco on 15/12/2017.
 */
public class ConsentDefinitionExceptionTest {

    private ConsentDefinitionException subject;

    @Test
    public void itShouldNotModifyErrorMessage() throws Exception {
        subject = new ConsentDefinitionException("oops");
        assertEquals("oops", subject.getMessage());
    }
}