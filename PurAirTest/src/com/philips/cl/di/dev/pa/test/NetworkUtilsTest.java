package com.philips.cl.di.dev.pa.test;

import junit.framework.TestCase;

import com.philips.cl.di.dev.pa.datamodel.ResponseDto;
import com.philips.cl.di.dev.pa.util.NetworkUtils;

public class NetworkUtilsTest extends TestCase {
	
	public void testDownloadUrlOneParamNull() {
		ResponseDto responseDto = NetworkUtils.downloadUrl(null);
		assertNull(responseDto);
	}
	
	public void testDownloadUrlOneParamEmpty() {
		ResponseDto responseDto = NetworkUtils.downloadUrl("");
		assertNull(responseDto);
	}

	public void testDownloadUrlTwoParamNull() {
		ResponseDto responseDto = NetworkUtils.downloadUrl(null, 0);
		assertNull(responseDto);
	}
}
