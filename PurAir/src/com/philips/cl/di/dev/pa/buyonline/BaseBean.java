package com.philips.cl.di.dev.pa.buyonline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;

public class BaseBean {

    private Map<String, Object> dataMap;

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public boolean isHasData() {
        boolean b = true;
        if (null == dataMap || dataMap.size() == 0) {
            b = false;
        }
        return b;
    }

    public void initData(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        dataMap = parseJSONObject(jsonObject);
    }

    private Map<String, Object> parseJSONObject(JSONObject jsonObject) {
        Map<String, Object> map = null;
        String tempKey = "";
        Iterator<String> iterator = jsonObject.keys();
        if (iterator.hasNext()) {
            map = new HashMap<String, Object>();
        }
        while (iterator.hasNext()) {
            tempKey = (String) iterator.next();
            if (null != jsonObject.optJSONObject(tempKey)) {
                map.put(tempKey, new BaseBean(jsonObject.optJSONObject(tempKey)));
            } else if (null != jsonObject.optJSONArray(tempKey)) {
                map.put(tempKey, parseJSONArray(jsonObject.optJSONArray(tempKey)));
            } else {
                map.put(tempKey, jsonObject.opt(tempKey));
            }
        }
        return map;
    }

    private ArrayList<Object> parseJSONArray(JSONArray jsonArray) {
        ArrayList<Object> list = null;
        if (jsonArray.length() > 0) {
            list = new ArrayList<Object>();
            int len = jsonArray.length();
            for (int i = 0; i < len; i++) {
                if (null != jsonArray.optJSONObject(i)) { // BaseBean
                    list.add(new BaseBean(jsonArray.optJSONObject(i)));
                } else if (null != jsonArray.optJSONArray(i)) { //
                    list.add(parseJSONArray(jsonArray.optJSONArray(i)));
                } else {
                    list.add(jsonArray.opt(i));
                }
            }
        }
        return list;
    }

    public BaseBean() {
        dataMap = new HashMap<String, Object>();
    }

    public BaseBean(JSONObject jsonObject) {
        initData(jsonObject);
    }

    public Object get(String key) {
        if (null == dataMap || dataMap.get(key) == null) {
            return null;
        }
        return dataMap.get(key);
    }

    public String getStr(String key) {
        return null == dataMap.get(key) ? "" : dataMap.get(key).toString();
    }

    public int getInt(String key) {
        int n = 0;
        String str = getStr(key);
        try {
            String tmp = TextUtils.isEmpty(str) ? "0" : str;
            n = Integer.parseInt(TextUtils.isDigitsOnly(tmp) ? tmp : "0");
        } catch (NumberFormatException e) {
            throw new RuntimeException("parse int error [" + str + "]");
        }
        return n;
    }

    public long getLong(String key) {
        long n = 0;
        String str = getStr(key);
        try {
            String tmp =  TextUtils.isEmpty(str) ? "0" : str;
            n = Long.parseLong(TextUtils.isDigitsOnly(tmp) ? tmp : "0");
        } catch (NumberFormatException e) {
            throw new RuntimeException("parse int error [" + str + "]");
        }
        return n;
    }

    public float getFloat(String key) {
        float n = 0;
        String str = getStr(key);
        try {
            String tmp = TextUtils.isEmpty(cleanNull(str)) ? "0" : str;
            n = Float.parseFloat(tmp);
        } catch (NumberFormatException e) {
            throw new RuntimeException("parse float error [" + str + "]");
        }
        return n;
    }
    
    public static String cleanNull(String src) {
		if (isEmpty(src) || "null".equals(src.toLowerCase().trim())) {
			return "";
		}
		return src;
	}
    
    public static boolean isEmpty(String str) {
		if (null == str || "".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

    public void set(String key, Object value) {
        if (null == dataMap) {
            dataMap = new HashMap<String, Object>();
        }
        dataMap.put(key, value);
    }

    public boolean containKey(String key) {
        if (null != dataMap) {
            return dataMap.containsKey(key);
        }
        return false;
    }
}
