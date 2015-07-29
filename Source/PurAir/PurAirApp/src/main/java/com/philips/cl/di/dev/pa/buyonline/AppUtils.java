package com.philips.cl.di.dev.pa.buyonline;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;

import com.philips.cl.di.dev.pa.R;

public class AppUtils{
	
	public static void startInnerWeb(Activity activity,String url) {
		if (activity != null && url != null) {
			Intent gotoSupportWebisteIntent = new Intent(Intent.ACTION_VIEW);
			gotoSupportWebisteIntent.setData(Uri.parse(url));
			activity.startActivity(gotoSupportWebisteIntent);
		}
	}
	
	public static void startMarketCommend(Activity activity,String packageName) {
		if (activity != null && packageName != null) {
			Intent intentComment = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+packageName));
			if (isIntentAvailable(activity, intentComment)) {
				activity.startActivity(intentComment);
			}
		}
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
	
	@SuppressLint("NewApi")
	public static AlertDialog.Builder createBuilder(Context context){
		if (null == context) {
			return null;
		}
		if (context instanceof Activity) {
			Activity activity = (Activity) context;
			Activity parentAct = activity.getParent();
			if (null == parentAct) {
				parentAct = activity;
			}
			if (android.os.Build.VERSION.SDK_INT >= 11 ) {		//Added in API level 11
				return new AlertDialog.Builder(parentAct,R.style.AppDialog);
			}else{
				return new AlertDialog.Builder(parentAct);
			}
		}else{
			if (android.os.Build.VERSION.SDK_INT >= 11 ) {		//Added in API level 11
				return new AlertDialog.Builder(context,R.style.AppDialog);
			}else{
				return new AlertDialog.Builder(context);
			}
		}
	}
}
