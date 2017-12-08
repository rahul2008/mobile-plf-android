/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import org.junit.Test;

import com.philips.platform.catk.mock.ContextMock;

public class CatkInputsTest {

    @Test
    public void checkContextIsSet() throws Exception {
        CatkInputs catkLaunchInput = new CatkInputs();
        catkLaunchInput.setContext(new ContextMock());
        assertNotNull(catkLaunchInput.getContext());
    }

    @Test
    public void checkContextIsSetNull() throws Exception {
        CatkInputs catkLaunchInput = new CatkInputs();
        catkLaunchInput.setContext(null);
        assertNull(catkLaunchInput.getContext());
    }

    @Test
    public void checkContextIsSetnotAssiged() throws Exception {
        CatkInputs catkLaunchInput = new CatkInputs();;
        assertNull(catkLaunchInput.getContext());
    }

}