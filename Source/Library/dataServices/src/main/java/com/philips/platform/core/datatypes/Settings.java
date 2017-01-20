package com.philips.platform.core.datatypes;

import java.io.Serializable;

/**
 * Created by sangamesh on 16/01/17.
 */
public interface Settings extends BaseAppData, Serializable {

    String METRICS="Metrics";
    String LOCALE="Locale";
    public void setType(String type);
    public String getType();
    public void setValue(String value);
    public String getValue();
    public int getId();
}
