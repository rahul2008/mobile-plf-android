package com.philips.cl.di.dev.pa.coapp;

import junit.framework.TestCase;
import android.app.AlertDialog;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.buyonline.AppUtils;

public class AppUtilsTest extends TestCase {
	
	public void testCreateBuilderWithNullContext() {
		AlertDialog.Builder builder = AppUtils.createBuilder(null);
		assertNull(builder);
	}
	
	public void testCreateBuilderWithAppContext() {
		AlertDialog.Builder builder = AppUtils.createBuilder(PurAirApplication.getAppContext());
		assertNotNull(builder);
	}

}
