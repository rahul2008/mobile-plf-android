package com.philips.pins.shinelib.datatypes;

/**
 * Created by 310188215 on 24/09/15.
 */
public class SHNDataDebugString extends SHNData {
    private final String string;
    private final int level;

    public SHNDataDebugString(int level, String string) {
        this.level = level;
        this.string = string;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.DebugString;
    }

    public int getLevel() {
        return level;
    }

    public String getString() {
        return string;
    }

    @Override
    public String toString() {
        return "DebugString level: " + getLevel() + " \"" + getString() + "\"";
    }
}
