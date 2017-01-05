/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package cdp.philips.com.mydemoapp.database.table;

import android.support.annotation.NonNull;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.philips.platform.core.datatypes.CharacteristicsDetail;
import com.philips.platform.core.datatypes.Characteristics;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Collection;

import cdp.philips.com.mydemoapp.database.EmptyForeignCollection;
import cdp.philips.com.mydemoapp.database.annotations.DatabaseConstructor;

@DatabaseTable
public class OrmCharacteristics implements Characteristics, Serializable {

    public static final long serialVersionUID = 11L;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = true)
    private String creatorId;

    @DatabaseField(canBeNull = false)
    private DateTime dateTime = new DateTime();

    @DatabaseField(canBeNull = true)
    private boolean mIsSynchronized;


    @ForeignCollectionField(eager = true)
    private ForeignCollection<OrmCharacteristicsDetail> ormCharacteristicsDetails= new EmptyForeignCollection<>();

    @DatabaseConstructor
    OrmCharacteristics() {
    }

    public OrmCharacteristics(@NonNull final String creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    @Override
    public String getCreatorId() {
        return creatorId;
    }

    @Override
    public Collection<? extends CharacteristicsDetail> getCharacteristicsDetails() {
        return ormCharacteristicsDetails;
    }

    @Override
    public void addCharacteristicsDetail(CharacteristicsDetail characteristicsDetail) {
        ormCharacteristicsDetails.add((OrmCharacteristicsDetail) characteristicsDetail);
    }

    @Override
    public boolean isSynchronized() {
        return mIsSynchronized;
    }

    @Override
    public DateTime getDateTime() {
        return dateTime;
    }

    public void setSynchronized(boolean isSynchronized) {
        mIsSynchronized = isSynchronized;
    }

    @Override
    public String toString() {
        return "[OrmConsent, id=" + id + ", creatorId=" + creatorId + ", dateTime=" + dateTime + "]";
    }
}
