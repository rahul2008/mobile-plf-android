package com.philips.cl.di.dev.pa.buyonline;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.buyonline.BuyOnlineFragment.RequestCallback;
import com.philips.cl.di.dev.pa.buyonline.Response.ResponseState;
import com.philips.cl.di.dev.pa.fragment.AlertDialogFragment;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.AlertDialogBtnInterface;

public class PromotionsFragment extends BaseFragment implements View.OnClickListener{

	protected static final int ID = 7;

	private MainActivity mainActivity;
	
	private ProgressDialog downloadProgress;
	private AlertDialogFragment downloadFailedDialog;
	private ImageView backImage;
	private ListView listView;
	private ArrayList<BaseBean> data;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.promotions_fragment, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mainActivity = (MainActivity) getActivity();
		initView();
		process();
		super.onActivityCreated(savedInstanceState);
	}

	public void initView() {
		setTitle(getString(R.string.promotional_videos));
		backImage = (ImageView) getView().findViewById(R.id.title_left_btn);
		backImage.setOnClickListener(backButtonListener);
		listView = (ListView)getView().findViewById(R.id.promotions_lv);
		showProgressDialog();
	}

	protected void setTitle(String title){
		View view = getView().findViewById(R.id.title_text_tv);
		if (null != view && view instanceof TextView) {
			((TextView)view).setText(title);
		}
	}

	private OnClickListener backButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mainActivity.onBackPressed();
		}
	};

	private void getData(){
		requestToParse("http://222.73.255.34/philips_new/getactivity.php", new RequestCallback(){
			@Override
			public void success(Response response) {
//				cancelProgressDialog();
				if (!response.success()) {
					return;
				}
				data = (ArrayList<BaseBean>)response.getMap().get("data");
				listView.setAdapter(new BaseAdapter() {
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						return createView(position);
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

			@Override
			public void error(ResponseState state, String message) {
//				cancelProgressDialog();
//				showErrorDialog();
			}

			@Override
			public void complete() {
				cancelProgressDialog();
			}
		});
		
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("_tts", System.currentTimeMillis()+"");
		requestToParse(getParamsUrl("http://222.73.255.34/philips_new/getbannerlist.php", hashMap), new RequestCallback(){
			@Override
			public void success(Response response) {
//				cancelProgressDialog();
				try {
					BaseBean bannerData = ((ArrayList<BaseBean>)(response.getMap().get("data"))).get(0);
					SquareImageView imageView = (SquareImageView)getView().findViewById(R.id.promotions_banner_iv);
					imageView.setTag(bannerData);
					ImageLoader.getInstance().displayImage(bannerData.getStr("bannerimg"), imageView,new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,	FailReason failReason) {
						}

						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//							cancelProgressDialog();
							BaseBean baseBean = (BaseBean)view.getTag();

							getView().findViewById(R.id.promotions_banner_iv).setVisibility(View.VISIBLE);
							getView().findViewById(R.id.promotions_banner_iv).setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									BaseBean baseBean = (BaseBean)v.getTag();
									AppUtils.startInnerWeb(getActivity(), baseBean.getStr("bannerlink"));

								}
							});
						}

						@Override
						public void onLoadingCancelled(String imageUri, View view) {
							cancelProgressDialog();
						}
					});
				} catch (Exception e) {
					cancelProgressDialog();
					e.printStackTrace();
					return;
				}

			}

			@Override
			public void error(ResponseState state, String message) {
				cancelProgressDialog();
				showErrorDialog();
			}

			@Override
			public void complete() {
				cancelProgressDialog();
			}
		});
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
	
	protected Response requestToParseConnKeep(String method, HttpRequestBase httpPost) {
		return requestToParse(method, httpPost);
	}
	
	private HttpRequestBase createHttpBaseRequest(String url, Map<String, String> params) {
		HttpGet httpPost = new HttpGet(url);
		httpPost.setHeader("content_type", "text/xml");
		return httpPost;
	}

	private View createView(int pos) {
		BaseBean bean = data.get(pos);
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.promotions_item_view, null);
		ImageLoader.getInstance().displayImage(bean.getStr("actimg"), (ImageView)(view.findViewById(R.id.promotions_item_img)));
		((TextView)(view.findViewById(R.id.promotions_item_tv))).setText(bean.getStr("desc"));
		view.findViewById(R.id.promotions_item_go_tv).setTag(pos);
		view.findViewById(R.id.promotions_item_go_tv).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseBean baseBean = data.get(Integer.parseInt(String.valueOf(v.getTag())));
				if ("1".equals(baseBean.getStr("type"))) {
					//TODO : Add share page here once it's done.
//					Intent intent = new Intent(getActivity(), OutdoorAirColorIndicationActivity.class);
//					getActivity().startActivity(intent);
				}else if("3".equals(baseBean.getStr("type"))){
//					Intent intent = new Intent(getActivity(),OutdoorAirColorIndicationActivity.class);
					Intent gotoSupportWebisteIntent = new Intent(Intent.ACTION_VIEW);
					gotoSupportWebisteIntent.putExtra("url", baseBean.getStr("shoplink"));
					gotoSupportWebisteIntent.putExtra("sharelink", baseBean.getStr("sharelink"));
					gotoSupportWebisteIntent.putExtra("sharecontent", baseBean.getStr("sharecontent"));
					gotoSupportWebisteIntent.putExtra("shareTitle", baseBean.getStr("activityname"));
					gotoSupportWebisteIntent.putExtra("isshare", true);
//					getActivity().startActivity(intent);
					
					gotoSupportWebisteIntent.setData(Uri.parse(baseBean.getStr("shoplink")));
					getActivity().startActivity(gotoSupportWebisteIntent);
					
				}else{
					AppUtils.startInnerWeb(getActivity(),baseBean.getStr("shoplink"));
				}
			}
		});
		return view;
	}

	public void process() {
		getData();
	}
	
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


	@Override
	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.tools_commentformarket_ll:
//			AppUtils.startMarketCommend(getActivity(), getActivity().getPackageName());
//			break;
//		case R.id.tools_feedback_ll:
//			getActivity().startActivity(new Intent(getActivity(),FeedbackActivity.class));
//			break;
//		default:
//			break;
//		}
	}

}
