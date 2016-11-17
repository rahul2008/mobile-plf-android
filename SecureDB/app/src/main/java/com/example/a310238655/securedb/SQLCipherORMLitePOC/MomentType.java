package com.example.a310238655.securedb.SQLCipherORMLitePOC;

/**
 * Created by 310238655 on 11/8/2016.
 */

public enum MomentType {

    UNKNOWN(-1, "UNKNOWN"),
    TREATMENT(20,"TREATMENT"),
    USER_INFO(21, "USER_INFO"),
    PHOTO(21, "PHOTO"),
    TEMPERATURE(25,"TEMPERATURE"),;

    private final int id;
    private final String description;

    MomentType(final int id, final String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static MomentType fromId(final int id) {
        final MomentType[] values = MomentType.values();

        for (MomentType item : values) {
            if (id == item.getId()) {
                return item;
            }
        }

        return UNKNOWN;
    }
}
