package com.philips.cl.di.dev.pa.buyonline;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.BaseActivity;
import com.philips.cl.di.dev.pa.buyonline.BuyOnlineFragment.RequestCallback;
import com.philips.cl.di.dev.pa.buyonline.Response.ResponseState;
import com.philips.cl.di.dev.pa.fragment.AlertDialogFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.AlertDialogBtnInterface;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class FeedbackActivity extends BaseActivity {
	
	private ProgressDialog downloadProgress;
	private AlertDialogFragment downloadFailedDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_activity);
		
		
		setTitleBack();
		setTitleText(getString(R.string.send_us_feedback));
		MetricsTracker.trackPage("SendUsFeedback");

		findViewById(R.id.feedback_submit_tv).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MetricsTracker.trackActionFeedback("FeedbackSubmitClicked");
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
			((FontTextView)view).setText(title);
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
		EditText contentStrEditText = ((EditText)findViewById(R.id.feedback_content_edt));
		contentStrEditText.setTypeface(Fonts.getCentraleSansLight(this));
		String contentStr = contentStrEditText.getText().toString().trim();
		if (contentStr.length() == 0) {
//			toast("è¯·å¡«å†™æ‚¨çš„æ„?è§?");
			MetricsTracker.trackActionUserError("FeedbackError : No content");
			return;
		}

		EditText contactStrEditText = (EditText)findViewById(R.id.feedback_contact_edt);
		contactStrEditText.setTypeface(Fonts.getCentraleSansLight(this));
		String contactStr = contactStrEditText.getText().toString().trim();
		if (contentStr.length() == 0) {
			showErrorDialog(R.string.invalid_input);
			MetricsTracker.trackActionUserError("FeedbackError : No contact info");
			return;
		}
//		showLoading();
		//http://222.73.255.34/philips_new/feedback.php?deviceid=&mobile=&content
		HashMap<String,String> sendData = new HashMap<String, String>();
		sendData.put("deviceid", AppUtils.getDeviceId(PurAirApplication.getAppContext()));
		sendData.put("mobile", contactStr);
		sendData.put("content", contentStr);
		showProgressDialog();
		MetricsTracker.trackActionFeedback("FeedbackSubmitSuccess");
		new NetworkRequests().requestToParse(getParamsUrl("http://222.73.255.34/philips_co/feedback.php", sendData),new RequestCallback(){
			@Override
			public void success(Response response) {
//				super.success(response);
				if (response.success()) {
					MetricsTracker.trackActionFeedback("FeedbackResponseSuccess");
					cancelProgressDialog();
					showErrorDialog(R.string.feedback_sent);
				}else{
					MetricsTracker.trackActionFeedback("FeedbackResponseFailure");
					cancelProgressDialog();
					showErrorDialog(R.string.feedback_not_sent);
				}
			}
			@Override
			public void error(ResponseState state, String message) {
				MetricsTracker.trackActionFeedback("FeedbackResponseFailure");
				cancelProgressDialog();
				showErrorDialog(R.string.feedback_not_sent);
			}
			@Override
			public void complete() {
			}
		});
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