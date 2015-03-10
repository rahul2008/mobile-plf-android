package com.philips.cl.di.dev.pa.buyonline;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.buyonline.Response.ResponseState;
import com.philips.cl.di.dev.pa.fragment.AlertDialogFragment;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.AlertDialogBtnInterface;

public class BuyOnlineFragment extends BaseFragment {

	protected static final int ID = 10;
	
	private ListView listView;
	
	private ArrayList<BaseBean> data;
	
	private LayoutInflater inflater;
	private ProgressDialog downloadProgress;
	private ImageView backImage;
	private MainActivity mainActivity;
	private AlertDialogFragment downloadFailedDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.buyonline_fragment, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initView();
		process();
		mainActivity = (MainActivity) getActivity();
		super.onActivityCreated(savedInstanceState);
	}

	
	public void initView() {
		setTitle(getString(R.string.buy_online_lbl));
		backImage = (ImageView) getView().findViewById(R.id.title_left_btn);
		backImage.setOnClickListener(backButtonListener);
		listView = (ListView)getView().findViewById(R.id.buyonline_lv);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
		inflater = LayoutInflater.from(getActivity());
		showProgressDialog();
	}
	
	private OnClickListener backButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			mainActivity.onBackPressed();
		}
	};
	
	private void showProgressDialog() {
		try {
			downloadProgress = new ProgressDialog(getActivity());
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

	public void process() {
		getData();
	}
	
	protected void setTitle(String title){
		View view = getView().findViewById(R.id.title_text_tv);
		if (null != view && view instanceof TextView) {
			((TextView)view).setText(title);
		}
	}
	
	private void getData(){
		requestToParse("http://222.73.255.34/philips_new/getproducts.php",new RequestCallback(){
			@SuppressWarnings("unchecked")
			@Override
			public void success(Response response) {
				if (!response.success()) {
					showErrorDialog();
					return;
				}
				data = (ArrayList<BaseBean>)(response.getMap().get("data"));
				fillData2View();
			}
			@Override
			public void complete() {
				cancelProgressDialog();
			}
			@Override
			public void error(ResponseState state, String message) {
				// TODO Auto-generated method stub
				showErrorDialog();
			}
		});
	}
	
	private void showErrorDialog() {
		downloadFailedDialog = AlertDialogFragment.newInstance(R.string.no_network_connection, R.string.ok);
		downloadFailedDialog.show(getActivity().getSupportFragmentManager(), getTag());
		downloadFailedDialog.setOnClickListener(new AlertDialogBtnInterface() {
			
			@Override
			public void onPositiveButtonClicked() {
				cancelProgressDialog();
				downloadFailedDialog.dismiss();
				mainActivity.onBackPressed();
			}
			
			@Override
			public void onNegativeButtonClicked() {
				//NOP
			}
		});
	}
	
	public void requestToParse(final String url, final RequestCallback callback) {
		requestToParse(url, callback, url);
	}
	
	public interface RequestCallback {
		public void success(Response response) ;
		public void error(ResponseState state, String message);
		public void complete() ;
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
		//去掉部分字符串携带UTF-8头标识
		if ("EF".equals(Integer.toHexString(bomByte[0] & 0xFF).toUpperCase()) && "BB".equals(Integer.toHexString(bomByte[1] & 0xFF).toUpperCase()) && "BF".equals(Integer.toHexString(bomByte[2] & 0xFF).toUpperCase())) {
			srcStr = new String(bomByte, 3, bomByte.length - 3);
		}
		return srcStr;
	}
	
	
	private void fillData2View(){
		if (null == data || data.size() == 0) {
			return;
		}
		listView.setAdapter(new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				return createItemView(getItem(position));
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public BaseBean getItem(int position) {
				return data.get(position);
			}

			@Override
			public int getCount() {
				return data.size();
			}
		});
	}

	private View createItemView(BaseBean baseBean){
		View view = inflater.inflate(R.layout.buyonline_item_view, null);
		ImageView imageView = (ImageView)view.findViewById(R.id.buyonline_item_siv);
		ImageLoader.getInstance().displayImage(baseBean.getStr("productimg"), imageView);
		((TextView)view.findViewById(R.id.buyonline_item_name_tv)).setText(baseBean.getStr("productname"));
		((TextView)view.findViewById(R.id.buyonline_item_typename_tv)).setText(baseBean.getStr("typename"));
		view.findViewById(R.id.buyonline_item_buy_tv).setTag(baseBean.getStr("shoplink"));
		view.findViewById(R.id.buyonline_item_buy_tv).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtils.startInnerWeb(getActivity(), String.valueOf(v.getTag()));
			}
		});
		return view;
	}


}
