/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.aikm;

import android.content.Context;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.aikm.exception.AIKMJsonFileNotFoundException;
import com.philips.platform.appinfra.aikm.model.AIKMService;
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
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GroomHelperTest extends AppInfraInstrumentation {

    private GroomHelper groomHelper;
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
            inputStream = getInstrumentation().getContext().getResources().getAssets().open("AIKMap.json");
        } catch (IOException e) {
            Log.e("error "," while reading json");
        }

        groomHelper = new GroomHelper(mAppInfraMock) {
            @Override
            InputStream getInputStream(Context mContext, String fileName) throws AIKMJsonFileNotFoundException {
                return inputStream;
            }
        };
        groomHelper.init(mAppInfraMock);
    }

    public void testGettingSeed() throws NoSuchAlgorithmException {
        String groupId = "appinfra.languagePack2";
        int index = 1;
        String key = "client_id";
        assertNull(groomHelper.getValue("", 0, "test"));
        assertNull(groomHelper.getValue("test", 0, ""));
        assertNull(groomHelper.getValue(null, 0, ""));
        assertNull(groomHelper.getValue("test", 0, null));
        String seed = groomHelper.getValue(groupId, index, key);
        assertTrue(seed.length() == 4);
    }

    public void testGettingIndex() {
        String indexData = "https://www.philips.com/0";
        URL url;
        try {
            url = new URL(indexData);
            assertEquals(groomHelper.getGroomIndex(url.toString()), "0");
        } catch (MalformedURLException e) {
            Log.e("error "," while fetching url");
        }

        indexData = "https://www.philips.com/22";
        assertEquals(groomHelper.getGroomIndex(indexData), "22");
        indexData = "https://www.philips.com/";
        assertNull(groomHelper.getGroomIndex(indexData));
        indexData = "";
        assertNull(groomHelper.getGroomIndex(indexData));
        assertNull(groomHelper.getGroomIndex(null));
    }

    public void testGettingMd5ValueInHex() throws NoSuchAlgorithmException {
        assertNull(groomHelper.getAilGroomInHex(null));
        assertNotNull(groomHelper.getAilGroomInHex("testing"));
    }

    public void testInit() {
        ExpectedException thrown = ExpectedException.none();
        thrown.expect(AIKMJsonFileNotFoundException.class);
        thrown.expectMessage("AIKeyBag.json file not found in assets folder");
        try {
            groomHelper.init(mAppInfraMock);
        } catch (AIKMJsonFileNotFoundException e) {
            Log.e("error "," aibag.json file not found");
            assertEquals(e.getMessage(), "AIKMap.json file not found in assets folder");
        }
    }

    public void testConvertingToHex() {
        String hexString = "52616a612052616d204d6f68616e20526f79";
        assertEquals(groomHelper.convertData(hexString), "Raja Ram Mohan Roy");
        String testString = groomHelper.convertData("c2b3c2a5085a2dc3a91672c29fc28e55c2955bc2a4c282656cc3bc");
        assertNotNull(testString);
    }

    public void testObfuscate() {
        String obfuscate = groomHelper.ailGroom("Raja Ram Mohan Roy", 0XAEF7);
        assertFalse(obfuscate.equals("Raja Ram Mohan Roy"));
        assertEquals(groomHelper.ailGroom(obfuscate, 0XAEF7), "Raja Ram Mohan Roy");
        assertFalse(groomHelper.ailGroom(obfuscate, 0XAEF7).equals("Raja Ram Mohan Roy xxx"));
    }

    public void testGetAppendedServiceIds() {
        String[] serviceIds = {"serviceId1", "serviceId2", "", null};
        ArrayList<String> appendedServiceIds = groomHelper.getAppendedGrooms(Arrays.asList(serviceIds));
        for (int i = 0; i < appendedServiceIds.size(); i++) {
            assertTrue(appendedServiceIds.get(i).contains(".kindex"));
            assertNotNull(appendedServiceIds.get(i));
        }
    }

    public void testGetServiceDiscoveryUrlMap() {
        ServiceDiscoveryInterface.OnGetServiceUrlMapListener onGetServiceUrlMapListener = mock(ServiceDiscoveryInterface.OnGetServiceUrlMapListener.class);
        groomHelper.getServiceDiscoveryUrlMap(null, AISDResponse.AISDPreference.AISDCountryPreference, null, onGetServiceUrlMapListener);
        verify(serviceDiscoveryInterfaceMock).getServicesWithCountryPreference(null, onGetServiceUrlMapListener);
        groomHelper.getServiceDiscoveryUrlMap(null, AISDResponse.AISDPreference.AISDLanguagePreference, null, onGetServiceUrlMapListener);
        verify(serviceDiscoveryInterfaceMock).getServicesWithLanguagePreference(null, onGetServiceUrlMapListener);

        Map<String, String> replacement = new HashMap<>();
        groomHelper.getServiceDiscoveryUrlMap(null, AISDResponse.AISDPreference.AISDCountryPreference, replacement, onGetServiceUrlMapListener);
        verify(serviceDiscoveryInterfaceMock).getServicesWithCountryPreference(null, onGetServiceUrlMapListener, replacement);
        groomHelper.getServiceDiscoveryUrlMap(null, AISDResponse.AISDPreference.AISDLanguagePreference, replacement, onGetServiceUrlMapListener);
        verify(serviceDiscoveryInterfaceMock).getServicesWithLanguagePreference(null, onGetServiceUrlMapListener, replacement);
    }

    public void testMappingData() throws Exception {
        assertNotNull(groomHelper.mapData(new JSONObject(), 0, "service_id"));
    }

    public void testMapAndValidateKey() throws AIKMJsonFileNotFoundException {
        groomHelper = new GroomHelper(mAppInfraMock) {
            @Override
            Object getAilGroomProperties(String serviceId) {
                return new JSONArray();
            }

            @Override
            InputStream getInputStream(Context mContext, String fileName) throws AIKMJsonFileNotFoundException {
                return inputStream;
            }
        };
        groomHelper.init(mAppInfraMock);
        ServiceDiscoveryService serviceDiscovery = new ServiceDiscoveryService();
        serviceDiscovery.setmError("something went wrong");
        AIKMService aikmService = new AIKMService();
        groomHelper.mapAndValidateGroom(aikmService, null, "0");
        assertEquals(aikmService.getAIKMapError(), AIKMService.AIKMapError.NO_SERVICE_FOUND);

        groomHelper.mapAndValidateGroom(aikmService, "service_id", "string");
        assertEquals(AIKMService.AIKMapError.INVALID_INDEX_URL, aikmService.getAIKMapError());

        groomHelper = new GroomHelper(mAppInfraMock) {
            @Override
            Object getAilGroomProperties(String serviceId) {
                return new JSONObject();
            }
        };
        groomHelper.mapAndValidateGroom(aikmService, "service_id", "1");
        assertEquals(AIKMService.AIKMapError.INVALID_JSON, aikmService.getAIKMapError());

        JSONObject someJsonObject = new JSONObject();
        try {
            someJsonObject.put("clientId", "4c73b365");
            final JSONArray someJsonArray = new JSONArray(someJsonObject);
            groomHelper = new GroomHelper(mAppInfraMock) {
                @Override
                Object getAilGroomProperties(String serviceId) {
                    return someJsonArray;
                }
            };
            groomHelper.mapAndValidateGroom(aikmService, "service_id", "1");
            assertTrue(aikmService.getAIKMap() != null);
            assertEquals(aikmService.getAIKMap().get("clientId"), "test");
        } catch (JSONException e) {
            Log.e("error "," in json structure");
        }
    }


}