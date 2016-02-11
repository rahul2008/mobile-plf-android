package com.philips.dhpclient.util;

import java.util.Arrays;

/**
 * Created by 310190722 on 9/23/2015.
 */
public class Objects {

    public static int hash(Object... values) {
        return Arrays.hashCode(values);
    }

    public static boolean equals(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
}
