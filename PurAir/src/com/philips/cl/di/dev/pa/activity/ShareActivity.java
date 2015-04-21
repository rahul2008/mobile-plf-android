package com.philips.cl.di.dev.pa.activity;

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

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.OutdoorAQI;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCity;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCityInfo;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.dashboard.OutdoorWeather;
import com.philips.cl.di.dev.pa.util.DashboardUtil;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;



public class ShareActivity extends BaseActivity implements OnClickListener {
	
	
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share",
			com.umeng.socialize.bean.RequestType.SOCIAL);
	
	
	public static final String APP_ID = "wxdd5f3d69cdf95dbd";
	
	private String url;
	
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.share_activity);
	    
	    ImageButton backButton = (ImageButton) findViewById(R.id.heading_back_imgbtn);
		backButton.setVisibility(View.VISIBLE);
		backButton.setOnClickListener(this);
		
		FontTextView heading=(FontTextView) findViewById(R.id.heading_name_tv);
		heading.setText(getString(R.string.share));
		
		initPage();
		//showImagesavingProgressDialog(R.string.notification_permission_check_msg);
	
	}

	
	private void initPage() {
		findViewById(R.id.share_menu_1).setOnClickListener(this);
		findViewById(R.id.share_menu_2).setOnClickListener(this);
		findViewById(R.id.share_menu_3).setOnClickListener(this);
		findViewById(R.id.share_menu_4).setOnClickListener(this);
	}
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
			UMWXHandler wxCircleHandler = new UMWXHandler(this,APP_ID);
			wxCircleHandler.setToCircle(true);
			wxCircleHandler.addToSocialSDK();

			CircleShareContent circleMedia = new CircleShareContent();
			circleMedia.setShareContent(shareContent);
			circleMedia.setTitle(getString(R.string.air_purifier));
			if (null != bitmap) {
				circleMedia.setShareImage(new UMImage(this, bitmap));
			}
			circleMedia.setTargetUrl(url);
			mController.setShareMedia(circleMedia);
			mController.postShare(this, media, null);
			break;

		case R.id.share_menu_2:

			if (null != bitmap) {
				mController.setShareImage(new UMImage(this, 
						bitmap));
			}

			mController.getConfig().setSsoHandler(new SinaSsoHandler());
			media =  SHARE_MEDIA.WEIXIN;

			UMWXHandler wxHandler = new UMWXHandler(this,APP_ID);
			wxHandler.addToSocialSDK();
			WeiXinShareContent weixinContent = new WeiXinShareContent();
			if (null != shareContent) {
				weixinContent.setShareContent(shareContent);
			}
			weixinContent.setTitle(getString(R.string.air_purifier));
			weixinContent.setTargetUrl(url);
			weixinContent.setShareImage(new UMImage(this, bitmap));
			mController.setShareMedia(weixinContent);
			mController.postShare(this, media, null);
			break;
			
		case R.id.share_menu_3:
			/*mController.getConfig().setSsoHandler(new SinaSsoHandler());
			media =  SHARE_MEDIA.SINA;
			mController.postShare(this, media, null);*/
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

		int position = OutdoorManager.getInstance().getOutdoorViewPagerCurrentPage();
		List<String> userCitiesList = OutdoorManager.getInstance().getUsersCitiesList();
		int size = userCitiesList.size();
		if ( position == size ) {
			return null;
		}
		String areaID = userCitiesList.get(position);
		OutdoorCity city = OutdoorManager.getInstance().getCityData(areaID);
		String cityName = getCityWithRespectToLocale(city.getOutdoorCityInfo());
		OutdoorAQI outdoorAQI = city.getOutdoorAQI();
		OutdoorWeather weather = city.getOutdoorWeather();
		StringBuffer shareContent = new StringBuffer(cityName);
		shareContent.append("\n");
		shareContent.append(getString(R.string.air_quality)+outdoorAQI.getAQI());
		shareContent.append("，").append(outdoorAQI.getAqiTitle()).append("。");
		
		shareContent.append(weather.getTemperature()).append(AppConstants.UNICODE_DEGREE).append(",");
		shareContent.append(getString(weather.getWeatherPhenomena())).append(",");
		shareContent.append(weather.getWindSpeed()).append(R.string.kmph).append(",");
		shareContent.append(getString(weather.getWindDirection())).append(",");
		shareContent.append("。");
		
		shareContent.append(getString(R.string.share_info)+AppConstants.URL_PRODUCT_SHARE+getString(R.string.download));

		try {
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("city", cityName);
			data.put("weather", "温度"+weather.getTemperature()
					+weather);
			data.put("AQI", outdoorAQI.getAqiTitle());
			data.put("tips", outdoorAQI.getAqiSummary()[1]);
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
	
	/*private void regtowx() {
		api = WXAPIFactory.createWXAPI(this, APP_ID, true);
		api.registerApp(APP_ID);
	}*/
	
	
	

}
