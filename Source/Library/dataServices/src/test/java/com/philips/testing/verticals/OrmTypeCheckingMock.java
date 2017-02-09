package com.philips.testing.verticals;

/**
 * Created by indrajitkumar on 2/9/17.
 */

public class OrmTypeCheckingMock {
    public static class OrmTypeException extends Exception {
        static final long serialVersionUID = 12L;
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
