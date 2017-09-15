/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.core.datatypes;

import java.io.Serializable;
import java.util.Collection;

/**
 * DataBase Interface for Creating User Characteristics Object
 */
public interface Characteristics extends BaseAppData, Serializable {

    void setType(String type);

    String getType();

    void setValue(String value);

    String getValue();

    Collection<? extends Characteristics> getCharacteristicsDetail();

    void setCharacteristicsDetail(Characteristics characteristics);

    int getParent();
}
