/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CommlibUappTest {

    @Test
    public void whenStaticCreated_thenReferenceIsNotLost() throws InterruptedException {
        assertNotNull(CommlibUapp.get());

        System.gc();
        System.runFinalization();

        assertNotNull(CommlibUapp.get());
    }

}