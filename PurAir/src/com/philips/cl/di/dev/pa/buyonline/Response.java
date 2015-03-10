package com.philips.cl.di.dev.pa.buyonline;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class Response {
	
	public enum ResponseState {
		NORMAL,
		ERROR_CONN,
		ERROR_SERVER,
		ERROR_PARSE,
		ERROR_TOKEN_INVALIDATE,
		ERROR_UNKNOWN,
	}
	
	public ResponseState status = ResponseState.NORMAL;
	
	private BaseBean baseBean;

	private Object tag;

	public Response() {}

	public BaseBean getMap() {
		return baseBean;
	}

	public void parse(JSONObject object) throws JSONException {
		baseBean = new BaseBean(object);
	}

	public String getMsg(){
		return TextUtils.isEmpty(baseBean.getStr("reason")) ? baseBean.getStr("msg") : baseBean.getStr("reason");
	}

	public Object getTag(){
		return tag;
	}

	public void setTag(Object tag){
		this.tag = tag;
	}
	
	public boolean success() {
		if (status == ResponseState.NORMAL && ("200".equals(baseBean.getStr("resultcode")) 
				|| "1".equals(baseBean.getStr("code")))) {
			return true;
		} else {
			return false;
		}
	}
}