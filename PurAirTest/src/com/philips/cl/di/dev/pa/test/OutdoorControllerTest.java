package com.philips.cl.di.dev.pa.test;

import junit.framework.TestCase;

import com.philips.cl.di.dev.pa.dashboard.OutdoorController;

public class OutdoorControllerTest extends TestCase {

	// Single city weather
	public void testGenerateCMAUrlKey1() {
		String publicKey = "http://api.fuwu.weather.com.cn/data/?areaid=101010100&type=observe&date=201407081644&appid=0283ef34a38902227fd8";
		String key = OutdoorController.getInstance().generateCMAUrlKey(publicKey);
		assertEquals("DmAcypEGWuWN9Y9bm979s3wMPbs%3D", key);
	}

	// Single city AQI
	public void testGenerateCMAUrlKey2() {
		String publicKey = "http://api.fuwu.weather.com.cn/wis_forcastdata/data/getData.php?areaid=101010100&type=air&date=201406301459&appid=0283ef34a38902227fd8";
		String key = OutdoorController.getInstance().generateCMAUrlKey(publicKey);
		assertEquals("p98DtGR%2Ft01pmT9uq3JsV2dca68%3D", key);
	}

	// Multiple cities AQI
	public void testGenerateCMAUrlKey3() {
		String publicKey = "http://api.fuwu.weather.com.cn/wis_forcastdata/data/getData.php?areaid=101010100,101020100,101280101&type=air&date=201407251151,201407251651&appid=0283ef34a38902227fd8";
		String key = OutdoorController.getInstance().generateCMAUrlKey(publicKey);
		assertEquals("1qqI6MiIQPQ5bH22IpyWc4zYgxU%3D", key);
	}

	// Single city AQI historic data
	public void testGenerateCMAUrlKey4() {
		String publicKey = "http://api.fuwu.weather.com.cn/wis_forcastdata/data/getData.php?areaid=101010100&type=air_his&date=201407101151,201407101651&appid=0283ef34a38902227fd8";
		String key = OutdoorController.getInstance().generateCMAUrlKey(publicKey);
		assertEquals("3R5QQvsQtH1IA0RkR0RbkKD96GE%3D", key);
	}
	
}
