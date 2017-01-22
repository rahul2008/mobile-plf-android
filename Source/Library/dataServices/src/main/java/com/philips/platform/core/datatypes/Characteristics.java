/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.datatypes;

import java.io.Serializable;
import java.util.Collection;

public interface Characteristics extends BaseAppData, Serializable {

    void setType(String type);

    String getType();

    void setValue(String value);

    String getValue();

    void setParent(int parentId);

    int getParent();

    Collection<? extends Characteristics> getCharacteristicsDetail();

    void setCharacteristicsDetail(Characteristics characteristics);
}
