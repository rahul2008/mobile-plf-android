package com.philips.cl.di.dev.pa.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ScrollView;

import com.philips.cl.di.dev.pa.view.FontTextView;

public class SupportUtil {
	
	public static void setVisibility(FontTextView ac4373View, FontTextView ac4375View) {
		if (ac4373View.getVisibility() == View.GONE 
				&& ac4375View.getVisibility() == View.GONE) {
			ac4373View.setVisibility(View.VISIBLE);
			ac4375View.setVisibility(View.VISIBLE);
		} else {
			ac4373View.setVisibility(View.GONE);
			ac4375View.setVisibility(View.GONE);
		}
	}
	
	public static void gotoWebsite(Context context, String url) {
		MetricsTracker.trackActionExitLink(url);
		Intent gotoWebisteIntent = new Intent(Intent.ACTION_VIEW);
		gotoWebisteIntent.setData(Uri.parse("http://"+ url));
		context.startActivity(Intent.createChooser(gotoWebisteIntent,""));
	}
	
	public static void gotoPhoneDial(Context context, String phoneNumber) {
		MetricsTracker.trackActionExitLink("phone : " + phoneNumber);
		Intent dialSupportIntent = new Intent(Intent.ACTION_DIAL);
		dialSupportIntent.setData(Uri.parse("tel:" + phoneNumber));
		context.startActivity(Intent.createChooser(dialSupportIntent, "Air Purifier support"));
	}
	
	public static void startNewActivity(Context context, String packageName) {
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		if (intent != null)	{
			/* we found the activity now start the activity */
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} else {
			/* bring user to the market or let them choose an app? */
			intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setData(Uri.parse("market://details?id="+packageName));
			context.startActivity(intent);
		}
	}
	
	public static void srollUpScrollView(final ScrollView scrollView) {
		scrollView.postDelayed(new Runnable() {
			@Override
			public void run() {
				scrollView.fullScroll(View.FOCUS_DOWN);
			}
		}, 50); //50 millisecond
	}

}
