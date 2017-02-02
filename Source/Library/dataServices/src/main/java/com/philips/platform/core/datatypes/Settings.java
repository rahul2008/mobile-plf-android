package com.philips.platform.core.datatypes;

import java.io.Serializable;

/**
 * Created by sangamesh on 16/01/17.
 */
public interface Settings extends BaseAppData, Serializable {

     String UNIT ="Unit";
     String LOCALE="Locale";

    String getUnit();
    String getLocale();

    void setUnit(String unit);
    void setLocale(String locale);


    int getId();
    void setID(int id);
}
