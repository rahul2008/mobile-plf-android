package com.philips.platform.core.datatypes;

import java.io.Serializable;

/**
 * Created by sangamesh on 16/01/17.
 */
public interface Settings extends BaseAppData, Serializable {

    String METRICS="Metrics";
    String LOCALE="Locale";
     void setType(String type);
     String getType();
     void setValue(String value);
     String getValue();
     int getId();
}
