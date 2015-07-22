package com.philips.cl.di.dev.pa.buyonline;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.philips.cl.di.dev.pa.util.ALog;

/**
 * @author dashan
 *
 */
public class HttpSender {

	private static final String HTTP_POST = "tag_http_post";
	private static final String HTTP_GET = "tag_http_get";

	protected void init() {
		HttpManager.getHttpClient(HTTP_POST);
	}

	protected String decodeApiData(String srcStr) {
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

	private Response requestToParse(String method, HttpRequestBase httpRequestBase) {
		Response result = new Response();
		HttpResponse response = null;
		int httpStatus = 0;
		try {
			response = execute(httpRequestBase);
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
			
			ALog.d(ALog.PRODUCT_REGESTRATION, method + " " +httpStatus +" " + buffData);
				
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

	protected Response requestToParseConnKeep(String method, HttpRequestBase httpPost) {
		return requestToParse(method, httpPost);
	}


	private HttpResponse execute(HttpRequestBase httpRequestBase) {
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


	public class Response {
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

		public boolean success() {
			if (status == ResponseState.NORMAL && ("200".equals(baseBean.getStr("resultcode")) 
					|| "1".equals(baseBean.getStr("code")))) {
				return true;
			} else {
				return false;
			}
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


	}


	public enum ResponseState {
		NORMAL, 
		ERROR_CONN, 
		ERROR_SERVER,
		ERROR_PARSE, 
		ERROR_TOKEN_INVALIDATE,
		ERROR_UNKNOWN,
	}
	
	public String sendFile(Map<String, String> map, File file, String action) {
        HttpPost httpPost = createFileHttpPost(map, file, action);
        HttpResponse response = execute(httpPost);
        if (response != null) {
    		Log.d(ALog.PRODUCT_REGESTRATION, "sendFile:"+response.getStatusLine().getStatusCode());
    	}
        if (null != response && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            try {
                String result = EntityUtils.toString(response.getEntity()).trim();
                response.getEntity().consumeContent();
                if (!TextUtils.isEmpty(result)) {
                   return result;
                } else {
                    return "";
                }
            } catch (ParseException e) {
            	e.printStackTrace();
                httpPost.abort();
            } catch (IOException e) {
            	e.printStackTrace();
                httpPost.abort();
            }
        } else {
        	if (response != null) {
        		Log.w("", "sendFile:"+response.getStatusLine().getStatusCode());
        	}
            httpPost.abort();
        }
        return "";
    }

    private HttpPost createFileHttpPost(Map<String, String> map, File file, String action) {
        HttpPost httpPost = new HttpPost(action);

        MultipartEntity me = new MultipartEntity();

        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> kv = it.next();
            ContentBody cbString = null;
            try {
                cbString = new StringBody(kv.getValue(), Charset.forName("UTF_8"));
            } catch (IllegalCharsetNameException e) {
                e.printStackTrace();
            } catch (UnsupportedCharsetException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            me.addPart(kv.getKey(), cbString);
        }
        ContentBody cbFile = new FileBody(file);
        me.addPart("file", cbFile);
        httpPost.setEntity(me);

        return httpPost;
    }

}
