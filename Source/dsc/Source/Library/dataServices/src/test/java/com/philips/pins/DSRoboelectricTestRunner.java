package com.philips.pins;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;

public class DSRoboelectricTestRunner extends RobolectricTestRunner {

    public DSRoboelectricTestRunner(final Class<?> klass) throws InitializationError {
        super(klass);
    }
}
