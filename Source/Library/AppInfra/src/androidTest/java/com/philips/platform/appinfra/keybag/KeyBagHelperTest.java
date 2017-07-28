/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.keybag;

import com.philips.platform.appinfra.AppInfraInstrumentation;

public class KeyBagHelperTest extends AppInfraInstrumentation {

    private KeyBagHelper keyBagHelper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        keyBagHelper = new KeyBagHelper();
    }

    public void testGettingSeed() {
        String groupId = "appinfra.languagePack2";
        int index = 1;
        String key = "client_id";
        assertNull(keyBagHelper.getSeed("", "test", 0));
        assertNull(keyBagHelper.getSeed("test", "", 0));
        assertNull(keyBagHelper.getSeed(null, "", 0));
        assertNull(keyBagHelper.getSeed("test", null, 0));
        String seed = keyBagHelper.getSeed(groupId, key, index);
        assertTrue(seed.length() == 4);
    }

    public void testGettingIndex() {
        String indexData = "https://www.philips.com/0";
        assertEquals(keyBagHelper.getIndex(indexData), "0");
        indexData = "https://www.philips.com/22";
        assertEquals(keyBagHelper.getIndex(indexData), "22");
        indexData = "";
        assertNull(keyBagHelper.getIndex(indexData));
        assertNull(keyBagHelper.getIndex(null));
    }

    public void testGettingMd5ValueInHex() {
       assertNull(keyBagHelper.getMd5ValueInHex(null));
    }

    public void testConvertingToHex() {
        String hexString = "52616a612052616d204d6f68616e20526f79";
        assertEquals(keyBagHelper.convertHexDataToString(hexString),"Raja Ram Mohan Roy");
    }
}