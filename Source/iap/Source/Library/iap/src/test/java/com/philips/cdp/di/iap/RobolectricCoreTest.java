/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class RobolectricCoreTest {

    @Test
    public void testIt() {
        CoreFunction function = new CoreFunction();
        String content = function.getContent(RuntimeEnvironment.application);

        // failing test gives much better feedback
        // to show that all works correctly ;)
        assertThat(content, equalTo("InAppPurchase"));
    }
}
