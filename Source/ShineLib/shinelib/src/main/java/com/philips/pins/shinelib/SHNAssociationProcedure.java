/*
 * Copyright (c) Koninklijke Philips N.V., 2016, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

/**
 * A placeholder interface for {@link SHNDeviceAssociation}. Provides better type safety over using Object. {@code SHNAssociationProcedure} is reported to the user of API via {@link com.philips.pins.shinelib.SHNDeviceAssociation.SHNDeviceAssociationListener#onAssociationStarted(SHNAssociationProcedure)} callback.
 * Returned instance is not type strong. The API user needs to caste it to the proper class type.
 *
 * @publicApi
 */
public interface SHNAssociationProcedure {

}
