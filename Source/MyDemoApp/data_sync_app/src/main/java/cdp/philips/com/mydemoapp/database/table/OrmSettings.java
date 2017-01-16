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
    private String type;

    @DatabaseField(canBeNull = false)
    private String value;


    @DatabaseConstructor
    OrmSettings() {
    }

    public OrmSettings(final String type,final String value) {
        this.type = type;
        this.value=value;

    }

    @Override
    public int getId() {
        return id;
    }


    @Override
    public void setType(String type) {
        this.type=type;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setValue(String value) {
     this.value=value;
    }

    @Override
    public String getValue() {
        return value;
    }


    @Override
    public String toString() {
        return  type +" "+value;
    }
}
