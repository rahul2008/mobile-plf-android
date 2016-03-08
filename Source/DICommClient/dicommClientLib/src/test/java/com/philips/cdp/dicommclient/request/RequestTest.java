/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;

public class RequestTest {

    @Test
    public void test_ParseString() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("key", "value");
        String json = Request.convertKeyValuesToJson(data);
        assertEquals("{\"key\":\"value\"}", json);
    }

    @Test
    public void test_ParseStringContainingJson() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("key", "{\"key\":\"value\"}");
        String json = Request.convertKeyValuesToJson(data);
        assertEquals("{\"key\":{\"key\":\"value\"}}", json);
    }

    @Test
    public void test_ParseStringArray() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        String[] array = {"value1", "value2", "value3"};
        data.put("key", array);
        String json = Request.convertKeyValuesToJson(data);
        assertEquals("{\"key\":[\"value1\",\"value2\",\"value3\"]}", json);
    }

    @Test
    public void test_ParseStringArrayContainingNull() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        String[] array = {"value1", null, "value3"};
        data.put("key", array);
        String json = Request.convertKeyValuesToJson(data);
        assertEquals("{\"key\":[\"value1\",\"\",\"value3\"]}", json);
    }

    @Test
    public void test_ParseStringArrayContainingJson() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        String[] array = {"{\"key1\":\"value1\"}", "{\"key2\":\"value2\"}", "{\"key3\":\"value3\"}"};
        data.put("key", array);
        String json = Request.convertKeyValuesToJson(data);
        assertEquals("{\"key\":[{\"key1\":\"value1\"},{\"key2\":\"value2\"},{\"key3\":\"value3\"}]}", json);
    }
}
