package com.philips.platform.consenthandlerinterface;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConsentErrorTest {

    private ConsentError subject;

    @Test
    public void itShouldStoreErrorMessage() throws Exception {
        subject = new ConsentError("oops", 401);
        assertEquals("oops", subject.getError());
    }

    @Test
    public void itShouldStoreErrorCode() throws Exception {
        subject = new ConsentError("oops", 401);
        assertEquals(401, subject.getErrorCode());
    }

}