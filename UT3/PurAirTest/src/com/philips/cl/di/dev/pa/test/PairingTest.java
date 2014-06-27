package com.philips.cl.di.dev.pa.test;

import junit.framework.TestCase;

import com.philips.cl.di.dev.pa.cpp.PairingHandler;
import com.philips.cl.di.dev.pa.util.JSONBuilder;

public class PairingTest extends TestCase {
	
	public void testGenerateRandomSecretKeyNotNull()
	{
		PairingHandler manager=new PairingHandler(null, null);
		String randomKey= manager.generateRandomSecretKey();
		assertNotNull(randomKey);
	}
	
	public void testGenerateRandomSecretKeyNotEqual()
	{
		PairingHandler manager=new PairingHandler(null, null);
		String randomKey= manager.generateRandomSecretKey();
		String randomKey1= manager.generateRandomSecretKey();
		assertFalse(randomKey.equals(randomKey1));
	}

	public void testGetDICOMMPairingJSONWithNull()
	{
		String appEui64 = "57924759837249";
		String expectedResult = "{\"Pair\":[\"AC4373APP\",\""+ appEui64 + "\",\"null\"]}";
		String actualResult=JSONBuilder.getDICOMMPairingJSON(appEui64, null);
		
		assertTrue(expectedResult.equals(actualResult));
	}

	public void testGetDICOMMPairingJSONWithKey()
	{
		String appEui64 = "57924759837249";
		String secretKey = "dfklsjakfjsljfsa";
		String expectedResult = "{\"Pair\":[\"AC4373APP\",\""+ appEui64 + "\",\""+ secretKey + "\"]}";
		String actualResult=JSONBuilder.getDICOMMPairingJSON(appEui64, secretKey);
		
		assertTrue(expectedResult.equals(actualResult));
	}
	
}
