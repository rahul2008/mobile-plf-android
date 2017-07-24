package com.philips.cl.di.dev.pa.coapp;

import android.app.AlertDialog;
import android.provider.MediaStore;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cl.di.dev.pa.buyonline.AppUtils;
import com.philips.cl.di.dev.pa.buyonline.CameraUtils;
import com.philips.cl.di.dev.pa.buyonline.ProductRegActivity;

public class CameraUtilsTest extends ActivityInstrumentationTestCase2<ProductRegActivity> {
	
	private ProductRegActivity activity;

	public CameraUtilsTest() {
		super(ProductRegActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		setActivityInitialTouchMode(false);
	}
	
	public void testCreateBuilderWithNullContext() {
		AlertDialog.Builder builder = AppUtils.createBuilder(null);
		assertNull(builder);
	}
	
	public void testCreateBuilderWithAppContext() {
		AlertDialog.Builder builder = AppUtils.createBuilder(activity);
		assertNotNull(builder);
	}
	
	public void testGetPicNullParam() {
		AlertDialog.Builder builder = CameraUtils.getPic(null);
		assertNull(builder);
	}
	
	public void testGetPicActivityParam() {
		AlertDialog.Builder builder = CameraUtils.getPic(activity);
		assertNotNull(builder);
	}
	
	public void testGetFilePathFromContentUriBothNullParam() {
		String path = CameraUtils.getFilePathFromContentUri(null, null);
		assertEquals("", path);
	}
	
	public void testGetFilePathFromContentUriFirstNullParam() {
		String path = CameraUtils.getFilePathFromContentUri(null, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		assertEquals("", path);
	}
	
	public void testGetFilePathFromContentUriSecondNullParam() {
		String path = CameraUtils.getFilePathFromContentUri(activity, null);
		assertEquals("", path);
	}
	
	public void testGetFilePathFromContentUri() {
		String path = CameraUtils.getFilePathFromContentUri(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		assertFalse("".equals(path));
	}

}
