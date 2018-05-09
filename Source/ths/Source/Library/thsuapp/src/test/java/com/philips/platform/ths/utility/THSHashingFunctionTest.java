/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.utility;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class THSHashingFunctionTest {

    THSHashingFunction thsHashingFunction;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        thsHashingFunction = new THSHashingFunction();
    }

    @Test
    public void md5Success() throws Exception {
        String response = thsHashingFunction.md5("Philips");
        assertTrue(response.equals("c1f456ee3fc507d644eb50420511ad91"));
    }

    @Test
    public void md5Failure() throws Exception {
        String response = thsHashingFunction.md5("Philips");
        assertFalse(response.equals("Philips"));
    }

    @Test(expected = NullPointerException.class)
    public void md5Null() throws Exception {
        String response = thsHashingFunction.md5(null);
    }

    @Test
    public void md5Empty() throws Exception {
        String response = thsHashingFunction.md5("");
        assertTrue(response.equals("d41d8cd98f00b204e9800998ecf8427e"));
    }

}