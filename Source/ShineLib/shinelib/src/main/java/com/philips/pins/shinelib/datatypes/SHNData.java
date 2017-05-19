/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

/**
 * Interface for data types returned from capabilities.
 * <p/>
 * Is currently an abstract class for historic reasons.
 *
 * @publicApi
 */
public abstract class SHNData {

    /**
     * Get the data type of the class implementing {@code SHNData}.
     *
     * @return data type
     */
    public abstract SHNDataType getSHNDataType();
}
