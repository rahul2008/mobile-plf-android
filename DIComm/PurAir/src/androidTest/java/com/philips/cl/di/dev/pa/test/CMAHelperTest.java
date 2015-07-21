package com.philips.cl.di.dev.pa.test;

import junit.framework.TestCase;

import com.philips.cl.di.dev.pa.cma.CMAHelper;
import com.philips.cl.di.dev.pa.util.ALog;

public class CMAHelperTest extends TestCase {
	
	private CMAHelper cmaHelper;
	@Override
	protected void setUp() throws Exception {
		cmaHelper = new CMAHelper("0283ef34a38902227fd8","feilipu_test_twoweek");
		super.setUp();
	}
	
	public void testGetUrl1() {
		String targetUrl = "http://api.fuwu.weather.com.cn/data/?areaid=101010100&type=observe&date=201407081644&appid=0283ef&key=DmAcypEGWuWN9Y9bm979s3wMPbs%3D";
		assertEquals(targetUrl, cmaHelper.getURL("http://api.fuwu.weather.com.cn/data/", "101010100", "observe", "201407081644"));
	}

	public void testGetUrl2() {
		String targetUrl = "http://api.fuwu.weather.com.cn/data/?areaid=101010100&type=forecast4d&date=201407081644&appid=0283ef&key=pfgK0WYXLy6K2sUeMfLJFdjwsXk%3D";
		assertEquals(targetUrl, cmaHelper.getURL("http://api.fuwu.weather.com.cn/data/", "101010100", "forecast4d", "201407081644"));
	}

	public void testGetUrl3() {
		String targetUrl = "http://api.fuwu.weather.com.cn/wis_forcastdata/data/getData.php?areaid=101010100&type=air&date=201406301459&appid=0283ef&key=p98DtGR%2Ft01pmT9uq3JsV2dca68%3D";
		assertEquals(targetUrl, cmaHelper.getURL("http://api.fuwu.weather.com.cn/wis_forcastdata/data/getData.php", "101010100", "air", "201406301459"));
	}

	public void testGetUrl4() {
		String targetUrl = "http://api.fuwu.weather.com.cn/wis_forcastdata/data/getData.php?areaid=101010100,101020100,101280101&type=air&date=201407251151,201407251651&appid=0283ef&key=1qqI6MiIQPQ5bH22IpyWc4zYgxU%3D";
		assertEquals(targetUrl, cmaHelper.getURL("http://api.fuwu.weather.com.cn/wis_forcastdata/data/getData.php", "101010100,101020100,101280101", "air", "201407251151,201407251651"));
	}
	
	public void testGetUrlBaseUrlNull() {
		String url = "";
		try {
			url = cmaHelper.getURL("", "101010100,101020100,101280101", "air", "201407251151,201407251651");
		} catch (Exception e) {ALog.e(ALog.ERROR, "Error");}
		assertTrue(url.isEmpty());
	}
	
	public void testGetUrlAreaIDNull() {
		String url = "";
		try {
			url = cmaHelper.getURL("http://api.fuwu.weather.com.cn/wis_forcastdata/data/getData.php", "", "air", "201407251151,201407251651");
		} catch(Exception e) {ALog.e(ALog.ERROR, "Error");}
		assertTrue(url.isEmpty());
	}
	
	public void testGetUrlTypeNull() {
		String url = "";
		try {
			url = cmaHelper.getURL("http://api.fuwu.weather.com.cn/wis_forcastdata/data/getData.php", "101010100,101020100,101280101", "", "201407251151,201407251651");
		} catch(Exception e) {ALog.e(ALog.ERROR, "Error");}
		assertTrue(url.isEmpty());
	}
	
	public void testGetUrlDateNull() {
		String url = "";
		try {
			url = cmaHelper.getURL("http://api.fuwu.weather.com.cn/wis_forcastdata/data/getData.php", "101010100,101020100,101280101", "air", "");
		} catch(Exception e) {ALog.e(ALog.ERROR, "Error");}
		assertTrue(url.isEmpty());
	}
	
	public void testGetUrlInvalidURL1() {
		String url = "";
		try {
			url = cmaHelper.getURL("htp://api.fuwu.weather.com.cn/wis_forcastdata/data/getData.php", "101010100,101020100,101280101", "air", "201407251151");
		} catch(Exception e) {ALog.e(ALog.ERROR, "Error");}
		assertTrue(url.isEmpty());
	}
	
	public void testGetUrlInvalidURL2() {
		String url = "";
		try {
			url = cmaHelper.getURL("httpss://api.fuwu.weather.com.cn/wis_forcastdata/data/getData.php", "101010100,101020100,101280101", "air", "201407251151");
		} catch(Exception e) {ALog.e(ALog.ERROR, "Error");}
		assertTrue(url.isEmpty());
	}
	
	public void testGetUrlInvalidURL3() {
		String url = "";
		try {
			url = cmaHelper.getURL("http://api.fuwu.weather.com.cn/wis_forcastdata/data/getData.", "101010100,101020100,101280101", "air", "201407251151");
		} catch(Exception e) {ALog.e(ALog.ERROR, "Error");}
		assertTrue(url.isEmpty());
	}
	
	public void testGetUrlInvalidURL4() {
		String url = "";
		try {
			url = cmaHelper.getURL("http:///api.fuwu.weather.com.cn/wis_forcastdata/data/getData.php.", "101010100,101020100,101280101", "air", "201407251151");
		} catch(Exception e) {ALog.e(ALog.ERROR, "Error");}
		assertTrue(url.isEmpty());
	}
	
	public void testGetUrlValidURL1() {
		String url = "";
		try {
			url = cmaHelper.getURL("http:///api.fuwu.weather.com.cn/wis_forcastdata/data/getData.php", "101010100,101020100,101280101", "air", "201407251151");
		} catch(Exception e) {ALog.e(ALog.ERROR, "Error");}
		assertFalse(url.isEmpty());
	}
	
	public void testGetUrlValidURL2() {
		String url = "";
		try {
			url = cmaHelper.getURL("http://data.fuwu.weather.com.cn/getareaid/areaid?id=", "101010100,101020100,101280101", "air", "201407251151");
		} catch(Exception e) {ALog.e(ALog.ERROR, "Error");}
		assertFalse(url.isEmpty());
	}
	
	public void testGetUrlValidURL3() {
		String url = "";
		try {
			url = cmaHelper.getURL("http://data.fuwu.weather.com.cn/getareaid/areaid", "101010100,101020100,101280101", "air", "201407251151");
		} catch(Exception e) {ALog.e(ALog.ERROR, "Error");}
		assertFalse(url.isEmpty());
	}
	
	public void testGetUrlInvalidDate1() {
		String url = "";
		try {
			url = cmaHelper.getURL("http://data.fuwu.weather.com.cn/getareaid/areaid", "101010100,101020100,101280101", "air", "1407251151");
		} catch(Exception e) {ALog.e(ALog.ERROR, "Error");}
		assertTrue(url.isEmpty());
	}
	
	public void testGetUrlInvalidDate2() {
		String url = "";
		try {
			url = cmaHelper.getURL("http://data.fuwu.weather.com.cn/getareaid/areaid", "101010100,101020100,101280101", "air", "201413071151");
		} catch(Exception e) {ALog.e(ALog.ERROR, "Error");}
		assertTrue(url.isEmpty());
	}
	
	public void testGetUrlInvalidDate3() {
		String url = "";
		try {
			url = cmaHelper.getURL("http://data.fuwu.weather.com.cn/getareaid/areaid", "101010100,101020100,101280101", "air", "201403072551");
		} catch(Exception e) {ALog.e(ALog.ERROR, "Error"); }
		assertTrue(url.isEmpty());
	}
	
	public void testGetUrlInvalidDate4() {
		String url = "";
		try {
			url = cmaHelper.getURL("http://data.fuwu.weather.com.cn/getareaid/areaid", "101010100,101020100,101280101", "air", "201403072551");
		} catch(Exception e) {ALog.e(ALog.ERROR, "Error"); }
		assertTrue(url.isEmpty());
	}
	
	public void testGetUrlValidDate1() {
		String url = "";
		try {
			url = cmaHelper.getURL("http://data.fuwu.weather.com.cn/getareaid/areaid", "101010100,101020100,101280101", "air", "201403072351");
		} catch(Exception e) {ALog.e(ALog.ERROR, "Error"); }
		assertFalse(url.isEmpty());
	}
	
	
}
