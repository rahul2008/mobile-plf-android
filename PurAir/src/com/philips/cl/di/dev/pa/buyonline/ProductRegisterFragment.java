package com.philips.cl.di.dev.pa.buyonline;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
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
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.fragment.AlertDialogFragment;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.fragment.HelpAndDocFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.AlertDialogBtnInterface;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class ProductRegisterFragment extends BaseFragment implements View.OnClickListener{

	private ListView listView;
	private ArrayList<BaseBean> data;
	public static boolean isUpdate;
	private ProgressDialog downloadProgress;
	private AlertDialogFragment downloadFailedDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.productreg_fragment, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		MetricsTracker.trackPage("ProductRegistration:RegisteredProducts");
		initView();
		
		isUpdate = true;
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (isUpdate) {
			isUpdate = false;
			getData();
			getBanner();
		}
	}

	public void initView() {
		FontTextView title = (FontTextView) getView().findViewById(R.id.title_text_tv);
		title.setText(getString(R.string.list_item_prod_reg));
		listView = (ListView) getView().findViewById(R.id.productreg_lately_lv);
		((FontTextView) getView().findViewById(R.id.productreg_reg_btn)).setOnClickListener(this);
		((FontTextView) getView().findViewById(R.id.productreg_wining_btn)).setOnClickListener(this);
		((ImageView) getView().findViewById(R.id.title_left_btn)).setOnClickListener(this);
	}


	public void process() {
		getData();
		getBanner();
	}
	
	private void getBanner(){
		String url = "http://222.73.255.34/philips_new/getdefaultproducts.php?deviceid="+AppUtils.getDeviceId(getActivity())+"&os=2";
		new NetworkRequests().requestToParse(url, new RequestCallback(){
			@Override
			public void success(Response response) {
				if (!response.success()) {
					showErrorDialog();
					return;
				}
				BaseBean bannerBean = response.getMap();
				fillBannerView(bannerBean);
			}
			@Override
			public void complete() {/**NOP*/}
			@Override
			public void error(ResponseState state, String message) {
				showErrorDialog();
			}
		});
	}
	
	private void fillBannerView(BaseBean bannerBean){
		if (null == bannerBean || getView() == null || getActivity() == null) {
			return;
		}
		String imgUrl = bannerBean.getStr("img");
		if (!URLUtil.isValidUrl(imgUrl)) {
			return;
		}
		ImageView imgView = (ImageView) getView().findViewById(R.id.productreg_ad_view);
		imgView.setTag(bannerBean.getStr("link"));
		ImageLoader.getInstance().loadImage(imgUrl, new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				if (getView() == null || getActivity() == null) {
					return;
				}
				ImageView adImgView = (ImageView)getView().findViewById(R.id.productreg_ad_view);
				adImgView.setVisibility(View.VISIBLE);
				adImgView.setImageBitmap(loadedImage);
				adImgView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String link = String.valueOf(v.getTag());
						if (!URLUtil.isValidUrl(link)) {
							return;
						}
						AppUtils.startInnerWeb(getActivity(), link);
					}
				});
				
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
			}
		});
		
		
	}

	private void getData(){
		showProgressDialog();
		String url = "http://222.73.255.34/philips_new/getuserproducts.php?deviceid="+AppUtils.getDeviceId(getActivity())+"&os=2";
		new NetworkRequests().requestToParse(url, new RequestCallback(){
			@SuppressWarnings("unchecked")
			@Override
			public void success(Response response) {
				if (!response.success()) {
					showErrorDialog();
					return;
				}
				data = (ArrayList<BaseBean>)(response.getMap().get("data"));
				fillData();
			}
			@Override
			public void complete() {
				cancelProgressDialog();
			}
			@Override
			public void error(ResponseState state, String message) {
				showErrorDialog();
			}
		});
	}


	private void fillData(){
		if (null == data || data.size() == 0 || getActivity() == null || getView() == null) {
			return;
		}
		listView.setAdapter(new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				return createView(getItem(position));
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
		
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				new AlertDialog.Builder(getActivity()).setItems(new String[]{getString(R.string.delete_text)}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							delete(position);
						}
					}
				}).setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
				return true;
			}
		});
	}
	
	private void delete(final int pos){
		if (data == null || data.get(pos) == null) return;
		showProgressDialog();
		String id = data.get(pos).getStr("id");
		MetricsTracker.trackActionDeleteRegisteredProduct(id);
		String url = "http://222.73.255.34/philips_new/deleteuserproduct.php?id="+id;
		new NetworkRequests().requestToParse(url, new RequestCallback(){
			@Override
			public void success(Response response) {
				if (!response.success()) {
					showErrorDialog();
					return;
				}
				data.remove(pos);
				((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
			}
			@Override
			public void complete() {
				cancelProgressDialog();
			}
			@Override
			public void error(ResponseState state, String message) {
				showErrorDialog();
			}
		});
	}


	private View createView(BaseBean baseBean) {
		if (getActivity() == null) return null;
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.userproducts_item, null);
		((TextView)view.findViewById(R.id.userproducts_item_typename_tv)).setText(baseBean.getStr("typename"));
		ImageLoader.getInstance().displayImage(baseBean.getStr("productimg"),((ImageView)view.findViewById(R.id.userproducts_item_siv)));	
		ImageLoader.getInstance().displayImage(baseBean.getStr("upimg"),((ImageView)view.findViewById(R.id.userproducts_invoices_siv)));	
		((TextView)view.findViewById(R.id.userproducts_item_status_tv)).setText("2".equals(baseBean.getStr("status")) ? 
				"" : getString(R.string.pending));
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.productreg_reg_btn:
			startActivity(new Intent(getActivity(), ProductListActivity.class));
			break;
		case R.id.productreg_wining_btn:
			Intent intent = new Intent(getActivity(), WebActivity.class);
			intent.putExtra("url", AppConstants.URL_WINNING_LIST);
			startActivity(intent);
			break;
		case R.id.title_left_btn:
			 onBack();
			break;
		default:
			break;
		}
	}
	
	private void onBack() {
		MainActivity mainActivity = (MainActivity) getActivity();
		if (mainActivity != null) {
			mainActivity.showFragment(new HelpAndDocFragment());
		}
	}
	
	private final static String ERROR_DIALOG_TAG = "error_dialog_tag";
	private void showErrorDialog() {
		if (getActivity() == null) return;
		/*FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		Fragment fragment = fragmentManager.findFragmentByTag(ERROR_DIALOG_TAG);
		if (fragment != null) {
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.remove(fragment);
			fragmentTransaction.commitAllowingStateLoss();
		}*/
		try {
			downloadFailedDialog = AlertDialogFragment.newInstance(R.string.no_network_connection, R.string.ok);
			downloadFailedDialog.show(getActivity().getSupportFragmentManager(), ERROR_DIALOG_TAG);
			downloadFailedDialog.setOnClickListener(new AlertDialogBtnInterface() {
				
				@Override
				public void onPositiveButtonClicked() {
					cancelProgressDialog();
					downloadFailedDialog.dismiss();
				}
				
				@Override
				public void onNegativeButtonClicked() {/**NOP*/}
			});
		} catch (IllegalStateException e) {
			ALog.e(ALog.ERROR, "ProductRegistrationError " + e.getMessage());
		}
	}
	
	private void showProgressDialog() {
		if (getActivity() == null) return;
		try {
			downloadProgress = new ProgressDialog(getActivity());
			downloadProgress.setMessage(getString(R.string.please_wait));
			downloadProgress.setCancelable(false);
			downloadProgress.show();
		} catch (IllegalStateException e) {
			ALog.e(ALog.PRODUCT_REGESTRATION, "Error: " + e.getMessage());
		}
	}
	
	private void cancelProgressDialog() {
		if (getActivity() != null && downloadProgress != null && downloadProgress.isShowing()) {
			downloadProgress.cancel();
		}
	}

}
