/*
 * (C) Koninklijke Philips N.V., 2016, 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.util;

import java.util.concurrent.Executor;

public class UnthreadExecutor implements Executor {

    @Override
    public void execute(final Runnable command) {
        command.run();
    }
}
