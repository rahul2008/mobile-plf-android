package com.philips.cdp.dicommclient.port.common;

import junit.framework.TestCase;

import com.philips.cdp.dicommclient.port.common.PairingHandler;

public class PairingHandlerTest extends TestCase {

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

	public void testGetDiffInDays() {
		long rl = PairingHandler.getDiffInDays(0L);
		assertTrue(rl != 0);
	}

}
