/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port;

import android.support.annotation.Keep;

/**
 * The base type for all type arguments to {@link com.philips.cdp.dicommclient.port.DICommPort}
 * <p>
 * Used by ProGuard to keep the field names as they are mapped by the Gson library during parsing.
 */
@Keep
public interface PortProperties {
}
