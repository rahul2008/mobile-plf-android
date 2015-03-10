package com.philips.cl.di.dev.pa.buyonline;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class AppUtils{
	public static void startInnerWeb(Activity activity,String url) {
		Intent gotoSupportWebisteIntent = new Intent(Intent.ACTION_VIEW);
		gotoSupportWebisteIntent.setData(Uri.parse(url));
		activity.startActivity(gotoSupportWebisteIntent);
	}

}
