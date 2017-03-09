/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.datatypes;

import java.io.Serializable;

public interface InsightMetadata extends Serializable{

    String getKey();

    void setKey(String key);

    String getValue();

    void setValue(String value);

    Insight getInsight();

}
