/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.testutil;

import org.mockito.MockitoAnnotations;

import android.test.InstrumentationTestCase;

public class MockitoTestCase extends InstrumentationTestCase{
	
	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		MockitoAnnotations.initMocks(this);
		super.setUp();
	}
}
