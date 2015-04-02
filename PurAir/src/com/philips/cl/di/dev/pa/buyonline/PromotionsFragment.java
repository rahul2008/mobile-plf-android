package com.philips.cl.di.dev.pa.buyonline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import com.philips.cl.di.dev.pa.util.MetricsTracker;

@SuppressWarnings("unchecked")
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
		MetricsTracker.trackPage("Promotions");
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
		new NetworkRequests().requestToParse("http://222.73.255.34/philips_new/getactivity.php", new RequestCallback(){
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
		new NetworkRequests().requestToParse(getParamsUrl("http://222.73.255.34/philips_new/getbannerlist.php", hashMap), new RequestCallback(){
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
