package cdp.philips.com.mydemoapp.database.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.philips.platform.core.datatypes.ConsentDetailType;

import java.io.Serializable;

import cdp.philips.com.mydemoapp.database.annotations.DatabaseConstructor;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@DatabaseTable
public class OrmConsentDetailType implements Serializable {
    static final long serialVersionUID = 11L;

    @DatabaseField(id = true, canBeNull = false)
    private int id;

    @DatabaseField(canBeNull = false)
    private String description;

    @DatabaseConstructor
    OrmConsentDetailType() {
    }

    public OrmConsentDetailType(final ConsentDetailType consentDetailType) {
        this.id = consentDetailType.getId();
        this.description = consentDetailType.getDescription();
    }

    public ConsentDetailType getType() {
        return ConsentDetailType.fromId(id);
    }

    @Override
    public String toString() {
        return "[OrmConsentDetailType, id=" + id + ", description=" + description + "]";
    }
}
