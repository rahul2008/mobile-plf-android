package com.philips.cl.di.dev.pa.buyonline;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.Bundle;
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
		new NetworkRequests().requestToParse("http://222.73.255.34/philips_new/getproducts.php",new RequestCallback(){
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
	
//	public void requestToParse(final String url, final RequestCallback callback) {
//		requestToParse(url, callback, url);
//	}
	
	public interface RequestCallback {
		public void success(Response response) ;
		public void error(ResponseState state, String message);
		public void complete() ;
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
