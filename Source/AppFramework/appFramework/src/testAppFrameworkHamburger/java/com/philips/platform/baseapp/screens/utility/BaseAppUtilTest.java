/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.utility;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;


public class BaseAppUtilTest extends TestCase {
    BaseAppUtil baseAppUtil;

    @Before
    public void setUp() throws Exception{
        baseAppUtil = new BaseAppUtil();
    }

    @Test
    public void testGetJsonFilePath(){
        assertEquals("/ReferenceApp/appflow.json",baseAppUtil.getJsonFilePath().toString());
    }

    @Test
    public void testReadJsonFileFromSdCard(){
        assertNotNull(baseAppUtil.readJsonFileFromSdCard());
    }
}
