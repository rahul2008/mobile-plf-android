package com.philips.pins.shinelib.datatypes;

/**
 * Created by 310188215 on 06/10/15.
 */
public enum SHNActivityType {
    Unspecified,
    Cycle,
    Run,
    Walk,
    Sleep,
    NotWorn;

    public static SHNActivityType valueToActivityType(int activityTypeValue) {
        switch(activityTypeValue) {
            case 0:
                return SHNActivityType.Cycle;
            case 1:
                return SHNActivityType.Run;
            case 2:
                return SHNActivityType.Walk;
            case 3:
                return SHNActivityType.Sleep;
            case 4:
                return SHNActivityType.NotWorn;
            default:
                return SHNActivityType.Unspecified;
        }
    }
}
