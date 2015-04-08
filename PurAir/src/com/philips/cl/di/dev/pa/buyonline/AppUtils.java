package com.philips.cl.di.dev.pa.buyonline;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;

public class AppUtils{
	
	public static void startInnerWeb(Activity activity,String url) {
		Intent gotoSupportWebisteIntent = new Intent(Intent.ACTION_VIEW);
		gotoSupportWebisteIntent.setData(Uri.parse(url));
		activity.startActivity(gotoSupportWebisteIntent);
	}
	
	public static void startMarketCommend(Activity activity,String packageName) {
		Intent intentComment = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+packageName));
		if (isIntentAvailable(activity, intentComment)) {
			activity.startActivity(intentComment);
		} /*else {
			            String wapUrl = "http://soft.3g.cn/CommentList.php?tid=21394297263954176";
			            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(wapUrl)));
			getInstance().showToast("您本地没有安装应用市场");	
			createBuilder(activity).setTitle("提示").setMessage("您本地没有安装应用市场").setPositiveButton("知道了", null).show();
		}*/
	}

	private static boolean isIntentAvailable(Context context, Intent intent) {
		final PackageManager packageManager = context.getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
	
	public static String getDeviceId(Context context) {
		String devId = null;
		try {
			TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			devId = telManager.getDeviceId();
		}catch (SecurityException e) {
		}catch (Exception e) {
		}
		return devId;
	}
}
