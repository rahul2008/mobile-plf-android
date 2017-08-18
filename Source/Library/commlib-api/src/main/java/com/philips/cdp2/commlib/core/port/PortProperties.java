/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port;

import android.support.annotation.Keep;

/**
 * The base type for all type arguments to {@link com.philips.cdp.dicommclient.port.DICommPort}
 * <p>
 * Used by ProGuard to keep the field names as they are mapped by the Gson library during parsing.
 *
 * @see com.philips.cdp.dicommclient.port.DICommPort
 */
@Keep
public interface PortProperties {
}
