/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.datatypes;

import java.io.Serializable;

/**
 * Data-Base Interface for creating InsightMetadata Object
 */
public interface InsightMetadata extends Serializable {

    String getKey();

    void setKey(String key);

    String getValue();

    void setValue(String value);

    Insight getInsight();

}
