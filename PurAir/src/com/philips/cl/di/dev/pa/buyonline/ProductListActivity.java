package com.philips.cl.di.dev.pa.buyonline;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.BaseActivity;
import com.philips.cl.di.dev.pa.buyonline.BuyOnlineFragment.RequestCallback;
import com.philips.cl.di.dev.pa.buyonline.Response.ResponseState;
import com.philips.cl.di.dev.pa.fragment.AlertDialogFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.AlertDialogBtnInterface;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class ProductListActivity  extends BaseActivity implements OnClickListener {

	private ListView listView;
	private ArrayList<BaseBean> data;
	private LayoutInflater inflater;
	private ProgressDialog downloadProgress;
	private AlertDialogFragment downloadFailedDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productlist_activity);
		inflater = LayoutInflater.from(this);
		initView();
		getData();
	}

	private void initView() {
		((FontTextView) findViewById(R.id.title_text_tv)).setText(getString(R.string.product_list));
		listView = (ListView)findViewById(R.id.productlist_lv);
		((ImageView) findViewById(R.id.title_left_btn)).setOnClickListener(this);
	}

	private void getData(){
		showProgressDialog();
		String url = "http://222.73.255.34/philips_new/getproducts.php";
		new NetworkRequests().requestToParse(url, new RequestCallback(){
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
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PurAirApplication.getAppContext().tmpObj = (BaseBean)parent.getItemAtPosition(position);
				startActivity(new Intent(ProductListActivity.this,ProductRegActivity.class));
			}
		});
	}

	private View createItemView(BaseBean baseBean){
		View view = inflater.inflate(R.layout.productlist_item_view, null);
		ImageView imageView = (ImageView)view.findViewById(R.id.productlist_item_siv);
		ImageLoader.getInstance().displayImage(baseBean.getStr("productimg"), imageView);
		((TextView)view.findViewById(R.id.productlist_item_name_tv)).setText(baseBean.getStr("productname"));
		((TextView)view.findViewById(R.id.productlist_item_typename_tv)).setText(baseBean.getStr("typename"));
		((TextView)view.findViewById(R.id.productlist_item_publishtime_tv)).setText(baseBean.getStr("publishdate"));
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_btn:
			finish();
			break;

		default:
			break;
		}
		
	}
	
	private final static String ERROR_DIALOG_TAG = "error_dialog_tag";
	private void showErrorDialog() {
		/*FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		Fragment fragment = fragmentManager.findFragmentByTag(ERROR_DIALOG_TAG);
		if (fragment != null) {
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.remove(fragment);
			fragmentTransaction.commitAllowingStateLoss();
		}*/
		downloadFailedDialog = AlertDialogFragment.newInstance(R.string.no_network_connection, R.string.ok);
		downloadFailedDialog.show(getSupportFragmentManager(), ERROR_DIALOG_TAG);
		downloadFailedDialog.setOnClickListener(new AlertDialogBtnInterface() {
			
			@Override
			public void onPositiveButtonClicked() {
				cancelProgressDialog();
				downloadFailedDialog.dismiss();
			}
			
			@Override
			public void onNegativeButtonClicked() {/**NOP*/}
		});
	}
	
	private void showProgressDialog() {
		try {
			downloadProgress = new ProgressDialog(this);
			downloadProgress.setMessage(getString(R.string.please_wait));
			downloadProgress.setCancelable(false);
			downloadProgress.show();
		} catch (IllegalStateException e) {
			ALog.e(ALog.PRODUCT_REGESTRATION, "Error: " + e.getMessage());
		}
	}
	
	private void cancelProgressDialog() {
		if (downloadProgress != null && downloadProgress.isShowing()) {
			downloadProgress.cancel();
		}
	}

}
