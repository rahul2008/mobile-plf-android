/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.keybag;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.keybag.exception.KeyBagJsonFileNotFoundException;
import com.philips.platform.appinfra.keybag.model.AIKMService;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.philips.platform.appinfra.keybag.model.AIKMService.KEY_BAG_ERROR.SERVICE_DISCOVERY_RESPONSE_ERROR;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KeyBagHelperTest extends AppInfraInstrumentation {

    private KeyBagHelper keyBagHelper;
    private ServiceDiscoveryInterface serviceDiscoveryInterfaceMock;
    private AppInfra mAppInfraMock;
    private InputStream inputStream;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mAppInfraMock = mock(AppInfra.class);
        LoggingInterface loggingInterfaceMock = mock(LoggingInterface.class);
        serviceDiscoveryInterfaceMock = mock(ServiceDiscoveryInterface.class);
        when(mAppInfraMock.getServiceDiscovery()).thenReturn(serviceDiscoveryInterfaceMock);
        when(mAppInfraMock.getLogging()).thenReturn(loggingInterfaceMock);

        try {
            inputStream = getInstrumentation().getContext().getResources().getAssets().open("AIKeyBag.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        keyBagHelper = new KeyBagHelper(mAppInfraMock);
        keyBagHelper.init(mAppInfraMock, inputStream);
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

    public void testInit() {
        ExpectedException thrown = ExpectedException.none();
        thrown.expect(KeyBagJsonFileNotFoundException.class);
        thrown.expectMessage("AIKeyBag.json file not found in assets folder");
        try {
            keyBagHelper.init(mAppInfraMock, inputStream);
        } catch (KeyBagJsonFileNotFoundException e) {
            e.printStackTrace();
            assertEquals(e.getMessage(), "AIKeyBag.json file not found in assets folder");
        }
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
        assertNotNull(keyBagHelper.mapData(new JSONObject(), 0, "service_id"));
    }

    public void testMapAndValidateKey() throws KeyBagJsonFileNotFoundException {
        keyBagHelper = new KeyBagHelper(mAppInfraMock) {
            @Override
            Object getPropertiesForKey(String serviceId) {
                return new JSONArray();
            }
        };
        keyBagHelper.init(mAppInfraMock, inputStream);
        ServiceDiscoveryService serviceDiscovery = new ServiceDiscoveryService();
        serviceDiscovery.setmError("something went wrong");
        AIKMService aikmService = new AIKMService();
        keyBagHelper.mapAndValidateKey(aikmService, null, "0");
        assertEquals(aikmService.getKeyBagError(), SERVICE_DISCOVERY_RESPONSE_ERROR);

        keyBagHelper.mapAndValidateKey(aikmService, "service_id", "string");
        assertEquals(AIKMService.KEY_BAG_ERROR.INVALID_INDEX_URL, aikmService.getKeyBagError());

        keyBagHelper = new KeyBagHelper(mAppInfraMock) {
            @Override
            Object getPropertiesForKey(String serviceId) {
                return new JSONObject();
            }
        };
        keyBagHelper.mapAndValidateKey(aikmService, "service_id", "1");
        assertEquals(AIKMService.KEY_BAG_ERROR.INVALID_JSON_STRUCTURE, aikmService.getKeyBagError());

        JSONObject someJsonObject = new JSONObject();
        try {
            someJsonObject.put("clientId", "4c73b365");
            final JSONArray someJsonArray = new JSONArray(someJsonObject);
            keyBagHelper = new KeyBagHelper(mAppInfraMock) {
                @Override
                Object getPropertiesForKey(String serviceId) {
                    return someJsonArray;
                }
            };
            keyBagHelper.mapAndValidateKey(aikmService, "service_id", "1");
            assertTrue(aikmService.getKeyBag() != null);
            assertEquals(aikmService.getKeyBag().get("clientId"), "test");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}