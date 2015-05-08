package com.philips.cl.di.dev.pa.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.registration.UserRegistrationController;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.dao.DIUserProfile;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;

public class DiagnosticShareActivity extends Activity implements OnClickListener, IWeiboHandler.Response  {
	
	private char lineSeparator='\n';
	private String userEmail="";
	public static final String SINA_WEIBO_ID = "飞利浦中国";
	private IWeiboShareAPI  mWeiboShareAPI = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diagonistic_share_activity);
		
		ImageButton backButton = (ImageButton) findViewById(R.id.heading_back_imgbtn);
		backButton.setVisibility(View.VISIBLE);
		
		FontTextView heading=(FontTextView) findViewById(R.id.heading_name_tv);
		heading.setText(getString(R.string.diagnostics));
		
		FontTextView sendViaSina = (FontTextView) findViewById(R.id.diagonistic_via_sina);
		FontTextView sendViaMail = (FontTextView) findViewById(R.id.diagonistic_via_mail);
		
		backButton.setOnClickListener(this);
		sendViaSina.setOnClickListener(this);
		sendViaMail.setOnClickListener(this);
		
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, AppConstants.APP_KEY);
		mWeiboShareAPI.registerApp();
		if (savedInstanceState != null) {
			mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
		}
	}
	
	/**
	 * @see {@link Activity#onNewIntent}
	 */	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mWeiboShareAPI.handleWeiboResponse(intent, this);
	}
	
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.heading_back_imgbtn:
			finish();
			break;
		case R.id.diagonistic_via_sina:
			sendMessage();
			break;
		case R.id.diagonistic_via_mail:
			sendMail(diagnosticData(), getString(R.string.contact_philips_support_email), userEmail);
			break;
		default:
			//NOP
			break;
		}
	}
	
	/**
	 * Fetches all required diagnostic data
	 */
	private String diagnosticData(){

		String jainRainUser="App not registered";
		if(UserRegistrationController.getInstance().isUserLoggedIn())
		{
			User user = new User(PurAirApplication.getAppContext());
			DIUserProfile profile = user.getUserInstance(PurAirApplication.getAppContext());
			userEmail=profile.getEmail();
			jainRainUser= getString(R.string.janrain_user)+ userEmail ;
		}
		String appVersion= getString(R.string.app_version)+Utils.getVersionNumber();
		String platform= getString(R.string.mobile_platform) +"Android";
		String osVersion = getString(R.string.sdk_version) + Build.VERSION.RELEASE ;
		String appEui64 = getString(R.string.app_eui64) + CPPController.getInstance(PurAirApplication.getAppContext()).getAppCppId();
		
		List<AirPurifier> purifiers= DiscoveryManager.getInstance().getStoreDevices();

		StringBuilder data= new StringBuilder(getString(R.string.diagnostics_intro));
		data.append(lineSeparator);
		data.append(lineSeparator);
		data.append(jainRainUser);
		data.append(lineSeparator);
		data.append(appVersion);
		data.append(lineSeparator);
		data.append(platform);
		data.append(lineSeparator);
		data.append(osVersion);
		data.append(lineSeparator);
		data.append(appEui64);
		data.append(lineSeparator);
		data.append(lineSeparator);
		for(int i=0; i<purifiers.size(); i++){
			data.append(getString(R.string.purifier)).append(i+1).append(":");
			data.append(lineSeparator);
			data.append(getString(R.string.purifier_name)).append(purifiers.get(i).getName());
			data.append(lineSeparator);
			data.append(getString(R.string.purifier_eui64)).append(purifiers.get(i).getNetworkNode().getCppId());
			data.append(lineSeparator);
			if(purifiers.get(i).getFirmwarePort().getFirmwarePortInfo()!=null){
			data.append(getString(R.string.purifier_firmware_version)).append(purifiers.get(i).getFirmwarePort().getFirmwarePortInfo().getVersion());
			data.append(lineSeparator);
			}
			data.append(lineSeparator);
		}
		return data.toString();
	}

	private void sendMail(String message, String sendTo, String userEmail) {
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, new String[] { sendTo });
		email.putExtra(Intent.EXTRA_SUBJECT, "AC4373/75 diagnostics for "+userEmail);
		email.putExtra(Intent.EXTRA_TEXT, message);
		email.setType("message/rfc822");
		startActivity(Intent.createChooser(email, "Send this mail via:"));
	}

	@Override
	public void onResponse(BaseResponse arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void sendMessage() {

		if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
			/*int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
			if (supportApi >= 10351 ApiUtils.BUILD_INT_VER_2_2) {
				sendMultiMessage();
			} else {*/
			sendMultiMessage();
			/*}
		} else {
			Toast.makeText(this, "Hint", Toast.LENGTH_SHORT).show();
		}*/
		}

	}


	private void sendMultiMessage() {

		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		weiboMessage.textObject = getTextObj();

		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		// 用transaction唯一标识一个请求
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = weiboMessage;
		mWeiboShareAPI.sendRequest(DiagnosticShareActivity.this, request);
		
	}
	private TextObject getTextObj() {
		
		StringBuilder messageBuilder = new StringBuilder(diagnosticData().trim());
		messageBuilder.append("@manzerhassan");
		TextObject textObject = new TextObject();
		textObject.text = messageBuilder.toString();
		return textObject;
	}


}
