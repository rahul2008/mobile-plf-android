package com.philips.platform.core.datatypes;

import java.io.Serializable;
import java.util.Collection;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface Characteristics extends BaseAppData, DateData, Serializable {


    String getCreatorId();

    Collection<? extends CharacteristicsDetail> getCharacteristicsDetails();

    void addCharacteristicsDetail(CharacteristicsDetail characteristicsDetail);

    boolean isSynchronized();

    void setBackEndSynchronized(boolean backEndSynchronized);

}
