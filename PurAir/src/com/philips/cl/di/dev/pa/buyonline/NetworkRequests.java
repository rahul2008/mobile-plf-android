package com.philips.cl.di.dev.pa.buyonline;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.philips.cl.di.dev.pa.buyonline.BuyOnlineFragment.RequestCallback;
import com.philips.cl.di.dev.pa.buyonline.Response.ResponseState;


public class NetworkRequests {

	public void requestToParse(final String url, final RequestCallback callback) {
		requestToParse(url, callback, url);
	}
	
	@SuppressLint("HandlerLeak")
	private void requestToParse(final String url, final RequestCallback callback,final Object tag) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (null == callback) {
					return;
				}
				callback.complete();
				if (null == msg.obj) {
					callback.error(ResponseState.ERROR_UNKNOWN, "error unknown");
					return;
				}
				Response response = (Response) msg.obj;
				if (null == response) {
					callback.error(ResponseState.ERROR_UNKNOWN, "error unknown");
					return;
				}
				response.setTag(tag);
				if (response.success()) {
					callback.success(response);
					return;
				}else{
					if (null != response.getMap()) {
						callback.error(response.status, response.getMsg());
					}else{
						callback.error(response.status, "error unknown");
					}
					return;
				}
			}
		};

		new Thread(new Runnable() {
			@Override
			public void run() {
				Response response = null;
				try {
					response = requestToParseConnKeep(url, createHttpBaseRequest(url, null));
					Message message = new Message();
					message.obj = response;
					handler.sendMessage(message);
					return;
				} catch (Exception e) {
					if (null != callback) {
						Message message = new Message();
						message.what = -1;
						handler.sendMessage(message);
					}
				}
			}
		}).start();
	}
	
	private HttpRequestBase createHttpBaseRequest(String url, Map<String, String> params) {
		HttpGet httpPost = new HttpGet(url);
		httpPost.setHeader("content_type", "text/xml");
		return httpPost;
	}
	
	private Response requestToParseConnKeep(String method, HttpRequestBase httpPost) {
		return requestToParse(method, httpPost);
	}
	
	private Response requestToParse(String method, HttpRequestBase httpRequestBase) {
		Response result = new Response();
		HttpResponse response = null;
		int httpStatus = 0;
		try {
			response = execute(httpRequestBase, method);
			if (null == response) {
				result.status = ResponseState.ERROR_CONN;
				return result;
			}
			httpStatus = response.getStatusLine().getStatusCode();
			String buffData = "";
			try {
				buffData = EntityUtils.toString(response.getEntity()).trim();
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
			if (httpStatus == HttpStatus.SC_OK) {
				result.status = ResponseState.NORMAL;

				if (TextUtils.isEmpty(buffData)) {
					result.status = ResponseState.ERROR_SERVER;
					return result;
				}
				buffData = decodeApiData(buffData);
				if (buffData.startsWith("{")) {
					JSONObject object = new JSONObject(buffData.toString());
					result.parse(object);
				}else {
					result.status = ResponseState.ERROR_SERVER;
				}
			} else { // not 200
				httpRequestBase.abort();
				result.status = ResponseState.ERROR_SERVER;

				return result;
			}
		} catch (Exception e) {
			result.status = ResponseState.ERROR_PARSE;
			httpRequestBase.abort();
		} finally {
			if (null != response) {
				try {
					response.getEntity().consumeContent();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return result;
	}
	
	private String decodeApiData(String srcStr) {
		if (TextUtils.isEmpty(srcStr)) {
			return "";
		}
		byte[] bomByte;
		try {
			bomByte = srcStr.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return "";
		}
		if (null == bomByte) {
			return "";
		}
		if ("EF".equals(Integer.toHexString(bomByte[0] & 0xFF).toUpperCase()) && "BB".equals(Integer.toHexString(bomByte[1] & 0xFF).toUpperCase()) && "BF".equals(Integer.toHexString(bomByte[2] & 0xFF).toUpperCase())) {
			srcStr = new String(bomByte, 3, bomByte.length - 3);
		}
		return srcStr;
	}
	
	private static final String HTTP_POST = "tag_http_post";
	private static final String HTTP_GET = "tag_http_get";
	
	private HttpResponse execute(HttpRequestBase httpRequestBase, String method) {
		HttpResponse response = null;
		if (null == httpRequestBase) {
			return null;
		}
		try {
			if (httpRequestBase instanceof HttpPost) {
				response = HttpManager.getHttpClient(HTTP_POST).execute((HttpPost) httpRequestBase);
			} else if (httpRequestBase instanceof HttpGet) {
				response = HttpManager.getHttpClient(HTTP_GET).execute((HttpGet) httpRequestBase);
			} else {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			httpRequestBase.abort();
		} catch (Exception e) {
			e.printStackTrace();
			httpRequestBase.abort();
		}
		return response;
	}
}
