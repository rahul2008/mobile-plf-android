package com.philips.pins;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UGROWRobolectricGradleTestRunner extends RobolectricGradleTestRunner {

    public UGROWRobolectricGradleTestRunner(final Class<?> klass) throws InitializationError {
        super(klass);
    }
}
