/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.keybag;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KeyBagHelperTest extends AppInfraInstrumentation {

    private KeyBagHelper keyBagHelper;
    private ServiceDiscoveryInterface serviceDiscoveryInterfaceMock;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        AppInfra mAppInfra = mock(AppInfra.class);
        serviceDiscoveryInterfaceMock = mock(ServiceDiscoveryInterface.class);
        when(mAppInfra.getServiceDiscovery()).thenReturn(serviceDiscoveryInterfaceMock);
        keyBagHelper = new KeyBagHelper(mAppInfra);
    }

    public void testGettingSeed() {
        String groupId = "appinfra.languagePack2";
        int index = 1;
        String key = "client_id";
        assertNull(keyBagHelper.getSeed("", 0, "test"));
        assertNull(keyBagHelper.getSeed("test", 0, ""));
        assertNull(keyBagHelper.getSeed(null, 0, ""));
        assertNull(keyBagHelper.getSeed("test", 0, null));
        String seed = keyBagHelper.getSeed(groupId, index, key);
        assertTrue(seed.length() == 4);
    }

    public void testGettingIndex() {
        String indexData = "https://www.philips.com/0";
        URL url;
        try {
            url = new URL(indexData);
            assertEquals(keyBagHelper.getKeyBagIndex(url.toString()), "0");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        indexData = "https://www.philips.com/22";
        assertEquals(keyBagHelper.getKeyBagIndex(indexData), "22");
        indexData = "https://www.philips.com/";
        assertNull(keyBagHelper.getKeyBagIndex(indexData));
        indexData = "";
        assertNull(keyBagHelper.getKeyBagIndex(indexData));
        assertNull(keyBagHelper.getKeyBagIndex(null));
    }

    public void testGettingMd5ValueInHex() {
        assertNull(keyBagHelper.getMd5ValueInHex(null));
        assertNotNull(keyBagHelper.getMd5ValueInHex("testing"));
    }

    public void testConvertingToHex() {
        String hexString = "52616a612052616d204d6f68616e20526f79";
        assertEquals(keyBagHelper.convertHexDataToString(hexString), "Raja Ram Mohan Roy");
        String testString = keyBagHelper.convertHexDataToString("c2b3c2a5085a2dc3a91672c29fc28e55c2955bc2a4c282656cc3bc");
        assertNotNull(testString);
    }

    public void testObfuscate() {
        String obfuscate = keyBagHelper.obfuscate("Raja Ram Mohan Roy", 0XAEF7);
        assertFalse(obfuscate.equals("Raja Ram Mohan Roy"));
        assertEquals(keyBagHelper.obfuscate(obfuscate, 0XAEF7), "Raja Ram Mohan Roy");
        assertFalse(keyBagHelper.obfuscate(obfuscate, 0XAEF7).equals("Raja Ram Mohan Roy xxx"));
    }

    public void testGetAppendedServiceIds() {
        String[] serviceIds = {"serviceId1", "serviceId2", "", null};
        ArrayList<String> appendedServiceIds = keyBagHelper.getAppendedServiceIds(Arrays.asList(serviceIds));
        for (int i = 0; i < appendedServiceIds.size(); i++) {
            assertTrue(appendedServiceIds.get(i).contains(".kindex"));
            assertNotNull(appendedServiceIds.get(i));
        }
    }

    public void testGetServiceDiscoveryUrlMap() {
        ServiceDiscoveryInterface.OnGetServiceUrlMapListener onGetServiceUrlMapListener = mock(ServiceDiscoveryInterface.OnGetServiceUrlMapListener.class);
        keyBagHelper.getServiceDiscoveryUrlMap(null, AISDResponse.AISDPreference.AISDCountryPreference, null, onGetServiceUrlMapListener);
        verify(serviceDiscoveryInterfaceMock).getServicesWithCountryPreference(null, onGetServiceUrlMapListener);
        keyBagHelper.getServiceDiscoveryUrlMap(null, AISDResponse.AISDPreference.AISDLanguagePreference, null, onGetServiceUrlMapListener);
        verify(serviceDiscoveryInterfaceMock).getServicesWithLanguagePreference(null, onGetServiceUrlMapListener);

        Map<String, String> replacement = new HashMap<>();
        keyBagHelper.getServiceDiscoveryUrlMap(null, AISDResponse.AISDPreference.AISDCountryPreference, replacement, onGetServiceUrlMapListener);
        verify(serviceDiscoveryInterfaceMock).getServicesWithCountryPreference(null, onGetServiceUrlMapListener, replacement);
        keyBagHelper.getServiceDiscoveryUrlMap(null, AISDResponse.AISDPreference.AISDLanguagePreference, replacement, onGetServiceUrlMapListener);
        verify(serviceDiscoveryInterfaceMock).getServicesWithLanguagePreference(null, onGetServiceUrlMapListener, replacement);
    }

    public void testMappingData() {
        assertNotNull(keyBagHelper.mapData(new JSONObject(),0,"service_id"));
    }

}