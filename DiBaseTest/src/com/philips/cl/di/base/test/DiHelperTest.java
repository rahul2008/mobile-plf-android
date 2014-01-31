package com.philips.cl.di.base.test;


import com.philips.cl.di.base.DiHelper;
import android.test.AndroidTestCase;


public class DiHelperTest extends AndroidTestCase  {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testJsonString() {
		assertFalse(DiHelper.isJson(""));
	}


}
