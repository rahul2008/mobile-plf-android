/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

/**
 * A callback used to receive the result of a firmware image request. Returns {@link SHNResult} and obtained {@link SHNFirmwareInfo}.
 */
public interface SHNFirmwareInfoResultListener extends ResultListener<SHNFirmwareInfo> {
}
