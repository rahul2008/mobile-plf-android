/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.datatypes;

import java.io.Serializable;
import java.util.Collection;

public interface UserCharacteristics extends BaseAppData, DateData, Serializable {

    String getCreatorId();

    Collection<? extends Characteristics> getCharacteristicsDetails();

    void addCharacteristicsDetail(Characteristics characteristics);

    boolean isSynchronized();

    void setSynchronized(boolean isSynchronized);

}
