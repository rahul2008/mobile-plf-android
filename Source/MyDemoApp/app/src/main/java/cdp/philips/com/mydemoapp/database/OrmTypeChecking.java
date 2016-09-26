/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.database;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmTypeChecking {

    public static class OrmTypeException extends Exception {
        public OrmTypeException(final String detailMessage) {
            super(detailMessage);
        }
    }

    static public <T, P> P checkOrmType(T object, Class<P> clazz) throws OrmTypeException {
        if (clazz.isInstance(object)) {
            return clazz.cast(object);
        } else {
            throw new OrmTypeException("Object should be internally type of " + clazz);
        }
    }
}
