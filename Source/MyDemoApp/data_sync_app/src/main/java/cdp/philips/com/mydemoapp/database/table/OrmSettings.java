package cdp.philips.com.mydemoapp.database.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.philips.platform.core.datatypes.Settings;

import java.io.Serializable;

import cdp.philips.com.mydemoapp.database.annotations.DatabaseConstructor;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@DatabaseTable
public class OrmSettings implements Settings, Serializable {

    public static final long serialVersionUID = 11L;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String locale;

    @DatabaseField(canBeNull = false)
    private String unit;


    @DatabaseConstructor
    OrmSettings() {
    }

    public OrmSettings(final String unit, final String locale) {
        this.unit = unit;
        this.locale = locale;

    }

    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public String getLocale() {
        return locale;
    }

    @Override
    public void setUnit(String unit) {
       this.unit=unit;
    }

    @Override
    public void setLocale(String locale) {
       this.locale=locale;
    }

    @Override
    public int getId() {
        return id;
    }


    @Override
    public String toString() {
        return unit + " " + locale;
    }
}
