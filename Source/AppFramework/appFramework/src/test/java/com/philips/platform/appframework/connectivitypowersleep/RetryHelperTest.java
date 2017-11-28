/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RetryHelperTest {
    RetryHelper subject = new RetryHelper();

    @Test
    public void itShouldReturnTrueByDefault() throws Exception {
        assertTrue(subject.canRetry());
    }

    @Test
    public void itShouldReturnTrueWhenHas1Retry() throws Exception {
        subject.canRetry();

        assertTrue(subject.canRetry());
    }

    @Test
    public void itShouldReturnTrueWhenHas2Retry() throws Exception {
        subject.canRetry();
        subject.canRetry();

        assertTrue(subject.canRetry());
    }

    @Test
    public void itShouldReturnFalseWhenHas3Retry() throws Exception {
        subject.canRetry();
        subject.canRetry();
        subject.canRetry();

        assertFalse(subject.canRetry());
    }

    @Test
    public void itShouldReturnFalseWhenReset() throws Exception {
        itShouldReturnFalseWhenHas3Retry();

        subject.reset();

        assertTrue(subject.canRetry());
    }

    @Test
    public void itShouldReturnFalseWhenHasMoreThan3Retry() throws Exception {
        subject.canRetry();
        subject.canRetry();
        subject.canRetry();
        subject.canRetry();

        assertFalse(subject.canRetry());
    }

}