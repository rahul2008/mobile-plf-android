/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.philips.cl.di.dev.pa;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.OutdoorAQI;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCity;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCityInfo;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.dashboard.OutdoorWeather;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;


public class WBShareActivity extends Activity implements OnClickListener, IWeiboHandler.Response {
	@SuppressWarnings("unused")
	private static final String TAG = "WBShareActivity";

	public static final String KEY_SHARE_TYPE = "key_share_type";
	private IWeiboShareAPI  mWeiboShareAPI = null;
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share",
			com.umeng.socialize.bean.RequestType.SOCIAL);

	public static final String APP_ID = "wxdd5f3d69cdf95dbd";
	private String url = null;

	/**
	 * @see {@link Activity#onCreate}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_activity);

		ImageButton backButton = (ImageButton) findViewById(R.id.heading_back_imgbtn);
		backButton.setVisibility(View.VISIBLE);
		backButton.setOnClickListener(this);

		FontTextView heading=(FontTextView) findViewById(R.id.heading_name_tv);
		heading.setText(getString(R.string.share));

		initPage();
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, AppConstants.APP_KEY);
		mWeiboShareAPI.registerApp();
		if (savedInstanceState != null) {
			mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
		}
		//initialize();
	}


	private void initPage() {
		findViewById(R.id.share_menu_1).setOnClickListener(this);
		findViewById(R.id.share_menu_2).setOnClickListener(this);
		findViewById(R.id.share_menu_3).setOnClickListener(sinaBtnClickListener);
		findViewById(R.id.share_menu_4).setOnClickListener(this);
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
	public void onResponse(BaseResponse baseResp) {
		
	}

	private OnClickListener sinaBtnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			sendMessage();
		}
	};

	@Override
	public void onClick(View v) {

		Bitmap bitmap = BitmapFactory.decodeFile(AppConstants.CACHEDIR_IMG + "PhilipsAir.png");
		SHARE_MEDIA media;
		String shareContent = getShareContent();
		if (url == null || url.isEmpty()) {
			url = AppConstants.URL_PRODUCT_SHARE;
		}

		switch (v.getId()) {
		case R.id.heading_back_imgbtn:
			finish();
			break;
		case R.id.share_menu_1:

			media = SHARE_MEDIA.WEIXIN_CIRCLE;
			@SuppressWarnings("deprecation")
			UMWXHandler wxCircleHandler = new UMWXHandler(this,APP_ID);
			wxCircleHandler.setToCircle(true);
			wxCircleHandler.addToSocialSDK();

			CircleShareContent circleMedia = new CircleShareContent();
			if ( shareContent != null ) {
				circleMedia.setShareContent(shareContent);
			}
			circleMedia.setTitle(getString(R.string.air_purifier));
			if (null != bitmap) {
				circleMedia.setShareImage(new UMImage(this, bitmap));
			}
			circleMedia.setTargetUrl(url);
			mController.setShareMedia(circleMedia);
			mController.postShare(this, media, null);
			break;

		case R.id.share_menu_2:

			media =  SHARE_MEDIA.WEIXIN;

			@SuppressWarnings("deprecation")
			UMWXHandler wxHandler = new UMWXHandler(this,APP_ID);
			wxHandler.addToSocialSDK();
			WeiXinShareContent weixinContent = new WeiXinShareContent();
			if (null != shareContent) {
				weixinContent.setShareContent(shareContent);
			}
			weixinContent.setTitle(getString(R.string.air_purifier));
			if (null != bitmap) {
				weixinContent.setShareImage(new UMImage(this, bitmap));
			}
			weixinContent.setTargetUrl(url);
			mController.setShareMedia(weixinContent);
			mController.postShare(this, media, null);
			break;

		case R.id.share_menu_4:
			openEmailApp(this, "","",new File(AppConstants.CACHEDIR_IMG + "PhilipsAir.png"));
			break;
		
		default:
			break;
		}

	}


	public static void openEmailApp(Context context,String emailAddress,String content,File file) {
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.putExtra(android.content.Intent.EXTRA_EMAIL,
				new String[] { emailAddress });
		intent.putExtra(android.content.Intent.EXTRA_SUBJECT, ""
				);
		intent.putExtra(android.content.Intent.EXTRA_TEXT, content);
		intent.putExtra(android.content.Intent.EXTRA_STREAM, Uri 
				.fromFile(file)); 
		intent.setType("image/png"); 
		context.startActivity(Intent.createChooser(intent, "Mail Chooser"));
	}

	private String getCityWithRespectToLocale(OutdoorCityInfo cityInfo) {
		String cityName = cityInfo.getCityName();
		if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANS")) {
			cityName = cityInfo.getCityNameCN();
		} else if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANT")) {
			cityName = cityInfo.getCityNameTW();
		}
		return cityName;
	}

	private String getShareContent(){

		String errorMessage = getString(R.string.no_data_available);
		int position = OutdoorManager.getInstance().getOutdoorViewPagerCurrentPage();
		List<String> userCitiesList = OutdoorManager.getInstance().getUsersCitiesList();
		int size = userCitiesList.size();
		if ( position == size ) { return errorMessage; }
		String areaID = userCitiesList.get(position);

		if ( areaID == null || areaID.isEmpty()) { return errorMessage; }

		OutdoorCity city = OutdoorManager.getInstance().getCityData(areaID);

		if (city == null) { return errorMessage; }

		String cityName = getCityWithRespectToLocale(city.getOutdoorCityInfo());

		OutdoorAQI outdoorAQI = city.getOutdoorAQI();
		StringBuffer shareContent = new StringBuffer(cityName);
		if ( outdoorAQI != null ) {
			shareContent.append("\n");
			shareContent.append(getString(R.string.air_quality)+outdoorAQI.getAQI());
			shareContent.append(",").append(outdoorAQI.getAqiTitle()).append(".");

		}

		OutdoorWeather weather = city.getOutdoorWeather();
		if (weather != null) { 
			shareContent.append(weather.getTemperature()).append(AppConstants.UNICODE_DEGREE).append(",");
			shareContent.append(getString(weather.getWeatherPhenomena())).append(",");
			shareContent.append(weather.getWindSpeed()).append(getString(R.string.kmph)).append(",");
			shareContent.append(getString(weather.getWindDirection())).append(",");
			shareContent.append(".");

		}
		shareContent.append(getString(R.string.share_info)+AppConstants.URL_PRODUCT_SHARE+getString(R.string.download));

		try {
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("city", cityName);
			data.put("AQI", outdoorAQI.getAqiTitle());
			data.put("tips", outdoorAQI.getAqiSummary()[1]);
			data.put("weather", String.valueOf(weather.getTemperature()));
			url = getParamsUrl("http://philipsair.sinaapp.com/air/?", data);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return shareContent.toString();
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
		weiboMessage.imageObject = getImageObj();

		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		// 用transaction唯一标识一个请求
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = weiboMessage;
		mWeiboShareAPI.sendRequest(WBShareActivity.this, request);
		
	}

	/*private void sendSingleMessage() {

		WeiboMessage weiboMessage = new WeiboMessage();
		weiboMessage.mediaObject = getTextObj();
		weiboMessage.mediaObject = getImageObj();

		SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.message = weiboMessage;

		mWeiboShareAPI.sendRequest(WBShareActivity.this, request);
	}*/

	
	/*private String getSharedText() {
		return getShareContent();
	}*/

	private TextObject getTextObj() {
		TextObject textObject = new TextObject();
		textObject.text = getShareContent();
		return textObject;
	}

	
	private ImageObject getImageObj() {
		ImageObject imageObject = new ImageObject();
		Bitmap bitmap = BitmapFactory.decodeFile(AppConstants.CACHEDIR_IMG + "PhilipsAir.png");
		if (bitmap != null) {
			imageObject.setImageObject(bitmap);
		}
		return imageObject;
	}

	/*private void initialize() {
		boolean isInstalledWeibo = mWeiboShareAPI.isWeiboAppInstalled();
		int supportApiLevel = mWeiboShareAPI.getWeiboAppSupportAPI(); 

	}*/

}
