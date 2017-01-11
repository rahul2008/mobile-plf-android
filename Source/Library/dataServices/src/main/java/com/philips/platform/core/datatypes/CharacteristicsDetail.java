/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.datatypes;

import java.io.Serializable;
import java.util.Collection;

public interface CharacteristicsDetail extends BaseAppData, Serializable {

    void setType(String type);

    String getType();

    void setValue(String value);

    String getValue();

    Collection<? extends CharacteristicsDetail> getCharacteristicsDetail();

    void setCharacteristicsDetail(CharacteristicsDetail characteristicsDetail);
}
