package com.philips.cl.di.dev.pa.test;

import junit.framework.TestCase;

import com.philips.cl.di.dev.pa.cpp.PairingHandler;

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
	
}
