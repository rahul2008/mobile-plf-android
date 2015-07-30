package com.philips.cdp.dicommclient.discovery;

import junit.framework.TestCase;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DICommClientWrapperTest extends TestCase {

    public void testValidLibVersionReturned() throws Exception {
        String libVersion = DICommClientWrapper.getDICommClientLibVersion();
        String[] versionNrParts = libVersion.split("\\.", 0);
        assertTrue("version doens't contain exactly three parts", versionNrParts.length == 3);
        assertTrue("version major nr not >= 0", Integer.parseInt(versionNrParts[0]) >= 0);
        assertTrue("version minor nr not >= 0", Integer.parseInt(versionNrParts[1]) >= 0);
        assertTrue("version patch nr not >= 0", Integer.parseInt(versionNrParts[2]) >= 0);
    }
}