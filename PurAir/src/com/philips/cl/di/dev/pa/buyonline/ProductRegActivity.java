package com.philips.cl.di.dev.pa.buyonline;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.BaseActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.fragment.AlertDialogFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.AlertDialogBtnInterface;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class ProductRegActivity extends BaseActivity{
	
	private BaseBean baseBean;
	private Bitmap photo;
	private ProgressDialog downloadProgress;
	private AlertDialogFragment downloadFailedDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productreg_activity);
		MetricsTracker.trackPage("ProductRegistration:RegisterNewProduct");
		baseBean = (BaseBean)PurAirApplication.getAppContext().tmpObj;
		if (null == baseBean) {
			finish();
			return;
		}
		initView();
	}
	
	private void initView() {
		((FontTextView) findViewById(R.id.title_text_tv)).setText(getString(R.string.list_item_prod_reg));
		((ImageView) findViewById(R.id.title_left_btn)).setOnClickListener(backButtonListener);
		findViewById(R.id.productreg_voucher_iv).setOnClickListener(getPicClickListener);
		findViewById(R.id.productreg_buytime_tv).setOnClickListener(datePickerClickListener);
		findViewById(R.id.productreg_buytime_icon_iv).setOnClickListener(datePickerClickListener);
		findViewById(R.id.productreg_reg_tv).setOnClickListener(registrationButtonListener);
		
		ImageView imageView = (ImageView)findViewById(R.id.productreg_siv);
		ImageLoader.getInstance().displayImage(baseBean.getStr("productimg"), imageView);
	}
	
	private OnClickListener datePickerClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			showDataSelDialog();
		}
	};
	
	private OnClickListener getPicClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = CameraUtils.getPic(ProductRegActivity.this);
			if (null != builder) {
				builder.show();
			}
		}
	};
	
	private OnClickListener backButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	
	private OnClickListener registrationButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			submit();
		}
	};
	
	private Handler handler = new Handler();
	
	private void submit() {
		EditText phoneStrEditText = (EditText)findViewById(R.id.productreg_contact_edt);
		phoneStrEditText.setTypeface(Fonts.getCentraleSansLight(this));
		final String phoneStr = phoneStrEditText.getText().toString().trim();
		if (phoneStr.length() == 0) {
			MetricsTracker.trackActionUserError("ProductReg:NoPhoneNumber");
			showErrorDialog(R.string.enter_phone_no);
			return;
		}
		if (null == photo) {
			MetricsTracker.trackActionUserError("ProductReg:NoPurchaseProof");
			showErrorDialog(R.string.enter_prof_of_purchase);
			return;
		}
		final String buytime = ((TextView)findViewById(R.id.productreg_buytime_tv)).getText().toString().trim();
		if (buytime.length() == 0) {
			MetricsTracker.trackActionUserError("ProductReg:NoBuyDate");
			showErrorDialog(R.string.enter_purchase_date);
			return;
		}
		MetricsTracker.trackActionRegisterNewProduct(baseBean.getStr("typename"), phoneStr, buytime);
		showProgressDialog();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					HttpSender httpSender = new HttpSender();
					HashMap<String, String> maps = new HashMap<String, String>();
					maps.put("deviceid", AppUtils.getDeviceId(ProductRegActivity.this));
					maps.put("productid", baseBean.getStr("id"));
					maps.put("phone", phoneStr);
					maps.put("buytime", buytime);
					File file = new File(AppConstants.TMP_OUTPUT_CORP_JPG);
					String result = httpSender.sendFile(maps, file,
							"http://222.73.255.34/philips_new/regproducts.php");
					JSONObject jsonObject= new JSONObject(result);
					final String msg = jsonObject.optString("msg");
					handler.post(new Runnable() {
						@Override
						public void run() {
							ProductRegisterFragment.isUpdate =  true;
							new AlertDialog.Builder(ProductRegActivity.this).setMessage(msg).setTitle(
									getString(R.string.tips)).setPositiveButton(
											getString(R.string.know), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									finish();
								}
							}).setCancelable(false).show();
							cancelProgressDialog();
						}
					});
					System.gc();
				} catch (Exception e) {
					e.printStackTrace();
					handler.post(new Runnable() {
						@Override
						public void run() {
							cancelProgressDialog();
							showErrorDialog(R.string.registration_failed);
						}
					});
				}
			}
		}).start();
	}
	
	
	private DatePickerDialog  datePickerDialog;
	@SuppressLint({ "SimpleDateFormat", "NewApi" })
	private void showDataSelDialog() {
		
		Calendar currentCalendar = Calendar.getInstance();
		final int currentYear = currentCalendar.get(Calendar.YEAR);
	    final int currentMonth = currentCalendar.get(Calendar.MONTH);
	    final int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
		datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(year, monthOfYear, dayOfMonth);
				long selectedMilli = calendar.getTimeInMillis();

				Date datePickerDate = new Date(selectedMilli);
				if (datePickerDate.after(new Date())) {
					datePickerDialog.updateDate(currentYear, currentMonth, currentDay);
					calendar.set(currentYear, currentMonth, currentDay);
					Toast.makeText(getApplicationContext(), getString(R.string.enter_purchase_date_alert), Toast.LENGTH_LONG).show();
				}

				((TextView)findViewById(R.id.productreg_buytime_tv))
					.setText(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
			}
		}, currentYear, currentMonth, currentDay);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
		}
		
		datePickerDialog.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Uri uri = null;
			switch (requestCode) {
			case 1: // Album obtain
				if (data != null) {
					uri = data.getData();
					CameraUtils.startPhotoZoom(this, uri,
							(int) (147 * getResources().getDisplayMetrics().scaledDensity),
							(int) (109 * getResources().getDisplayMetrics().scaledDensity),
							Uri.fromFile(new File(AppConstants.TMP_OUTPUT_CORP_JPG)));
				}
				break;
			case 2: // Get pictures
				File temp = new File(AppConstants.TMP_OUTPUT_JPG);
				uri = Uri.fromFile(temp);
				CameraUtils.startPhotoZoom(this, uri,
						(int) (147 * getResources().getDisplayMetrics().scaledDensity),
						(int) (109 * getResources().getDisplayMetrics().scaledDensity),
						Uri.fromFile(new File(AppConstants.TMP_OUTPUT_CORP_JPG)));
				break;
			case 3:
				setPicToUserView(data);
				break;
			default:
				break;
			}
		}
	}
		
	
	private void setPicToUserView(Intent picdata) {
		photo = BitmapFactory.decodeFile(AppConstants.TMP_OUTPUT_CORP_JPG);
		if (photo != null) {
			((ImageView)findViewById(R.id.productreg_voucher_iv)).setImageBitmap(photo);
		}
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
			}
			
			@Override
			public void onNegativeButtonClicked() {
				//NOP
			}
		});
	}

}
