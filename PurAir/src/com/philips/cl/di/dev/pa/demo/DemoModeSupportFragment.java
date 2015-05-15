package com.philips.cl.di.dev.pa.demo;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.ews.EWSBaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.SupportUtil;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class DemoModeSupportFragment extends EWSBaseFragment {
	
	private ButtonClickListener buttonClickListener;
	private FontTextView helpWebsiteAC4373, helpWebsiteAC4375;
	private ScrollView scrollView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		buttonClickListener = new ButtonClickListener();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contact_philips_support, null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MetricsTracker.trackPage(TrackPageConstants.DEMO_SUPPORT);
		DemoModeActivity demoActivity = (DemoModeActivity) getActivity();
		
		if (demoActivity.getApModeFailCounter() > 2 
				|| demoActivity.getStep1FailCounter() > 2
				|| demoActivity.getPowerOnFailCounter() > 2) {
			((FontTextView) getView().findViewById(
					R.id.contact_philips_support_message1)).setText(getActivity().getString(R.string.contact_philips_support_msg1_3attempt));
		}
		
		((DemoModeActivity) getActivity()).setActionbarTitle(DemoModeConstant.DEMO_MODE_STEP_SUPPORT);
		
		scrollView = (ScrollView) getView().findViewById(R.id.contact_philips_support_scrollview);
		
		setBackground(scrollView, R.drawable.ews_nav_bar_2x, Color.BLACK, .1F);
		
		((TextView) getView().findViewById
				(R.id.contact_support_phone)).setOnClickListener(buttonClickListener); 
		TextView phone_two= (TextView) getView().findViewById
				(R.id.contact_support_phone_two); 
		phone_two.setText(" / "+getString(R.string.contact_philips_support_phone_num_2));
		phone_two.setOnClickListener(buttonClickListener);
		RelativeLayout weChat = (RelativeLayout) getView().findViewById(R.id.layout_we_chat);
		weChat.setOnClickListener(buttonClickListener);
		((RelativeLayout) getView().findViewById
				(R.id.contact_support_email_layout)).setOnClickListener(buttonClickListener);  						
		((RelativeLayout)getView().findViewById
				(R.id.contact_support_website_layout)).setOnClickListener(buttonClickListener); 
		helpWebsiteAC4373 = (FontTextView) getView().findViewById(R.id.contact_support_website_ac4373);
		helpWebsiteAC4375 = (FontTextView) getView().findViewById(R.id.contact_support_website_ac4375);
		helpWebsiteAC4373.setOnClickListener(buttonClickListener);
		helpWebsiteAC4375.setOnClickListener(buttonClickListener);
	}
	
	private class ButtonClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.contact_support_phone:
				MetricsTracker.trackActionServiceRequest("phone_1");
				SupportUtil.gotoPhoneDial(getActivity(), getString(R.string.contact_philips_support_phone_num));
				break;
			case R.id.contact_support_phone_two:
				MetricsTracker.trackActionServiceRequest("phone_2");
				SupportUtil.gotoPhoneDial(getActivity(), getString(R.string.contact_philips_support_phone_num_2));
				break;
			case R.id.contact_support_email_layout:
				MetricsTracker.trackActionServiceRequest("email");
				MetricsTracker.trackActionExitLink(getString(R.string.contact_philips_support_email));
				Intent supportEmailIntent = new Intent(
						Intent.ACTION_SENDTO, Uri.fromParts("mailto", getString(R.string.contact_philips_support_email), null));
				supportEmailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support");
				supportEmailIntent.putExtra(Intent.EXTRA_TEXT, "No template");
				startActivity(Intent.createChooser(supportEmailIntent, "Air Purifier support"));
				break;
			case R.id.layout_we_chat:
				MetricsTracker.trackActionServiceRequest("we_chat");
				MetricsTracker.trackActionExitLink("we_chat");
				SupportUtil.startNewActivity(getActivity(), "com.tencent.mm");
				break;
			case R.id.contact_support_website_layout:
				SupportUtil.setVisibility(helpWebsiteAC4373, helpWebsiteAC4375);
				SupportUtil.srollUpScrollView(scrollView);
				break;
			case R.id.contact_support_website_ac4373:
				MetricsTracker.trackActionServiceRequest("web_ac4373");
				SupportUtil.gotoWebsite(getActivity(), getString(R.string.contact_philips_support_website_ac4373));
				break;
			case R.id.contact_support_website_ac4375:
				MetricsTracker.trackActionServiceRequest("web_ac4375");
				SupportUtil.gotoWebsite(getActivity(), getString(R.string.contact_philips_support_website_ac4375));
				break;
			default:
				ALog.i(ALog.DEMO_MODE, "Default...");
				break;
			}
		}
	}
	
}

