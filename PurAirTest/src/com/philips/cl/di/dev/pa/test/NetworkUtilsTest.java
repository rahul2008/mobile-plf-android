package com.philips.cl.di.dev.pa.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.philips.cl.di.dev.pa.datamodel.ResponseDto;
import com.philips.cl.di.dev.pa.util.NetworkUtils;

import junit.framework.TestCase;

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
	
	public void testReadFullyNullParam() {
		String readStr = "";
		try {
			readStr = NetworkUtils.readFully(null);
		} catch (UnsupportedEncodingException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
		assertTrue(readStr.isEmpty());
	}
}
