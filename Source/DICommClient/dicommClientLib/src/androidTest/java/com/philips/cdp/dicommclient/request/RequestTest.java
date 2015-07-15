/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

import java.util.HashMap;

import com.philips.cdp.dicommclient.request.Request;

import junit.framework.TestCase;

public class RequestTest extends TestCase {

	public void test_ParseString() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("key", "value");
		String json = Request.convertKeyValuesToJson(data);
		assertEquals("{\"key\":\"value\"}", json);
	}

	public void test_ParseStringContainingJson() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("key", "{\"key\":\"value\"}");
		String json = Request.convertKeyValuesToJson(data);
		assertEquals("{\"key\":{\"key\":\"value\"}}", json);
	}

	public void test_ParseStringArray() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		String[] array = {"value1","value2","value3"};
		data.put("key", array);
		String json = Request.convertKeyValuesToJson(data);
		assertEquals("{\"key\":[\"value1\",\"value2\",\"value3\"]}", json);
	}

	public void test_ParseStringArrayContainingJson() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		String[] array = {"{\"key1\":\"value1\"}","{\"key2\":\"value2\"}","{\"key3\":\"value3\"}"};
		data.put("key", array);
		String json = Request.convertKeyValuesToJson(data);
		assertEquals("{\"key\":[{\"key1\":\"value1\"},{\"key2\":\"value2\"},{\"key3\":\"value3\"}]}", json);
	}

}
