package com.philips.cl.di.dev.pa.buyonline;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.BaseActivity;
import com.philips.cl.di.dev.pa.buyonline.BuyOnlineFragment.RequestCallback;
import com.philips.cl.di.dev.pa.buyonline.Response.ResponseState;
import com.philips.cl.di.dev.pa.fragment.AlertDialogFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.AlertDialogBtnInterface;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class FeedbackActivity extends BaseActivity {
	
	private ProgressDialog downloadProgress;
	private AlertDialogFragment downloadFailedDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_activity);
		setTitleBack();
		setTitleText(getString(R.string.send_us_feedback));

		findViewById(R.id.feedback_submit_tv).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				submit();
			}
		});
	}
	
	private void setTitleBack() {
		View view = findViewById(R.id.title_left_btn);
		if (null != view) {
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
	}
	
	private void setTitleText(String title){
		View view = findViewById(R.id.title_text_tv);
		if (null != view && view instanceof TextView) {
			((TextView)view).setText(title);
		}
	}

	public static String getParamsUrl(String address,Map<String, String> sendData){
		if (!address.contains("?")) address += "?";
		else address += "&";
		Iterator<Map.Entry<String, String>> iterator = sendData.entrySet().iterator();
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		while(iterator.hasNext()){
			Map.Entry<String, String> kv = iterator.next();
			params.add(new BasicNameValuePair(kv.getKey(), kv.getValue()));
		}
		address += URLEncodedUtils.format(params, "UTF-8");
		return address;
	}

	private void submit() {
		String contentStr = ((EditText)findViewById(R.id.feedback_content_edt)).getText().toString().trim();
		if (contentStr.length() == 0) {
//			toast("è¯·å¡«å†™æ‚¨çš„æ„?è§?");
			return;
		}

		String contactStr = ((EditText)findViewById(R.id.feedback_contact_edt)).getText().toString().trim();
		if (contentStr.length() == 0) {
			showErrorDialog(R.string.invalid_input);
			return;
		}
//		showLoading();
		//http://222.73.255.34/philips_new/feedback.php?deviceid=&mobile=&content
		HashMap<String,String> sendData = new HashMap<String, String>();
		sendData.put("deviceid", AppUtils.getDeviceId(PurAirApplication.getAppContext()));
		sendData.put("mobile", contactStr);
		sendData.put("content", contentStr);
		showProgressDialog();
		requestToParse(getParamsUrl("http://222.73.255.34/philips_co/feedback.php", sendData),new RequestCallback(){
			@Override
			public void success(Response response) {
//				super.success(response);
				if (response.success()) {
					cancelProgressDialog();
					showErrorDialog(R.string.feedback_sent);
				}else{
					cancelProgressDialog();
					showErrorDialog(R.string.feedback_not_sent);
				}
			}
			@Override
			public void error(ResponseState state, String message) {
//				super.error(state, message);
				cancelProgressDialog();
				showErrorDialog(R.string.feedback_not_sent);
			}
			@Override
			public void complete() {
//				closeLoading();
			}
		});
	}
	
	public void requestToParse(final String url, final RequestCallback callback) {
		requestToParse(url, callback, url);
	}
	
	@SuppressLint("HandlerLeak")
	public void requestToParse(final String url, final RequestCallback callback,final Object tag) {
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
	
	protected Response requestToParseConnKeep(String method, HttpRequestBase httpPost) {
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
	
	protected String decodeApiData(String srcStr) {
		if (TextUtils.isEmpty(srcStr)) {
			return "";
		}
		// clean UTF-8 BOM EFBBBF
		// http://www.cnblogs.com/chenwenbiao/archive/2011/07/27/2118372.html
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
		//åŽ»æŽ‰éƒ¨åˆ†å­—ç¬¦ä¸²æ�ºå¸¦UTF-8å¤´æ ‡è¯†
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
	
	private void showProgressDialog() {
		try {
			downloadProgress = new ProgressDialog(this);
			downloadProgress.setMessage(getString(R.string.please_wait));
			downloadProgress.setCancelable(false);
			downloadProgress.show();
		} catch (IllegalStateException e) {
			ALog.e(ALog.USER_REGISTRATION, "Error: " + e.getMessage());
		}
	}
	
	private void cancelProgressDialog() {
		if (downloadProgress != null && downloadProgress.isShowing()) {
			downloadProgress.cancel();
		}
	}
	
	private void showErrorDialog(int stringId) {
		downloadFailedDialog = AlertDialogFragment.newInstance(stringId, R.string.ok);
		downloadFailedDialog.show(getSupportFragmentManager(), "dialog");
		downloadFailedDialog.setOnClickListener(new AlertDialogBtnInterface() {
			
			@Override
			public void onPositiveButtonClicked() {
				cancelProgressDialog();
				downloadFailedDialog.dismiss();
				finish();
			}
			
			@Override
			public void onNegativeButtonClicked() {
				//NOP
			}
		});
	}
}