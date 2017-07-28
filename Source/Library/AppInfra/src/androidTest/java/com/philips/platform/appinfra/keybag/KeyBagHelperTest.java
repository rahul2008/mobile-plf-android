/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.keybag;

import com.philips.platform.appinfra.AppInfraInstrumentation;

public class KeyBagHelperTest extends AppInfraInstrumentation {

    KeyBagHelper keyBagHelper;

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
//        assertEquals(seed,"appinfra.languagePack2client_id1");
    }

    public void testGettingIndex() {
        String indexData = "https://www.philips.com/0";
        assertEquals(keyBagHelper.getIndex(indexData), "0");
        indexData = "https://www.philips.com/22";
        assertEquals(keyBagHelper.getIndex(indexData), "22");
        indexData = "";
        assertNull(keyBagHelper.getIndex(indexData));
    }

//    public void testGettingMd5ValueInHex() {
//        String hexData = "b3a5085a2de916729f8e55955ba482656cfc";
//        String md5ValueInHex = keyBagHelper.convertHexDataToString(hexData);
//        assertEquals("Raja Ram Mohan Roy",md5ValueInHex);
//    }
}