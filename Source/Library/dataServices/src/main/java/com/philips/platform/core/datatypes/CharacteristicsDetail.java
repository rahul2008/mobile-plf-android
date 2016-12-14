package com.philips.platform.core.datatypes;

import java.io.Serializable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface CharacteristicsDetail extends BaseAppData, Serializable {

    void setParent(int parentID);
    int getParent();

    void setType(String type);
    String getType();

    void setValue(String value);
    String getValue();
}
