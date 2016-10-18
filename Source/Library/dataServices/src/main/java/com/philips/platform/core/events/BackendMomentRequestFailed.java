package com.philips.platform.core.events;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class BackendMomentRequestFailed extends Event{
        Exception exception;
        public BackendMomentRequestFailed(Exception e){
            super();
            exception = e;
        }

        public Exception getException() {
            return exception;
        }

}
