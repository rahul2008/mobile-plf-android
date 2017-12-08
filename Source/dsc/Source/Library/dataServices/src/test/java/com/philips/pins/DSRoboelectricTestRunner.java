package com.philips.pins;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;

public class DSRoboelectricTestRunner extends RobolectricGradleTestRunner {

    public DSRoboelectricTestRunner(final Class<?> klass) throws InitializationError {
        super(klass);
    }
}
