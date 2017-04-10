package cdp.philips.com.mydemoapp.database.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by philips on 4/8/17.
 */

@DatabaseTable
public class TempBlobTable implements Serializable {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = true)
    private String userId;

    @DatabaseField(canBeNull = true)
    private String hsdpObjectId;

    @DatabaseField(canBeNull = true)
    private String contentChecksum;

    @DatabaseField(canBeNull = true)
    private String blobVersion;

    @DatabaseField(canBeNull = true)
    private String creationTimestamp;

    @DatabaseField(canBeNull = true)
    private String lastModifiedTimestamp;
}

