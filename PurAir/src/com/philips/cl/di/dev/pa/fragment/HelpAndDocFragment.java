package com.philips.cl.di.dev.pa.fragment;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.AirQualityActivity;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.activity.OpenSourceLibLicenseActivity;
import com.philips.cl.di.dev.pa.activity.TutorialPagerActivity;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.registration.UserRegistrationController;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.dao.DIUserProfile;

public class HelpAndDocFragment extends BaseFragment implements OnClickListener{
	
	private char lineSeparator='\n';
	private FontTextView faqAC4373, faqAC4375;
	private FontTextView userManualAC4373, userManualAC4375;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.help_and_doc_fragment, container, false);
		initializeView(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MetricsTracker.trackPage(TrackPageConstants.HELP_AND_DOCUMENTATION);
	}
	private void initializeView(View rootView) {
		ImageButton backButton = (ImageButton) rootView.findViewById(R.id.heading_back_imgbtn);
		backButton.setVisibility(View.VISIBLE);
		FontTextView heading=(FontTextView) rootView.findViewById(R.id.heading_name_tv);
		heading.setText(getString(R.string.list_item_help_and_doc));
		FontTextView lblAppAirQualityExplain=(FontTextView) rootView.findViewById(R.id.app_air_quality_explain);
		FontTextView lblAppTutorial=(FontTextView) rootView.findViewById(R.id.app_tutorial);
		FontTextView lblFAQ= (FontTextView) rootView.findViewById(R.id.faq);
		FontTextView lblUserManual=(FontTextView) rootView.findViewById(R.id.lbl_user_manual);
		FontTextView lblOpensource = (FontTextView) rootView.findViewById(R.id.opensource_lb);
		faqAC4373 = (FontTextView) rootView.findViewById(R.id.faq_ac4373);
		faqAC4375 = (FontTextView) rootView.findViewById(R.id.faq_ac4375);
		userManualAC4373 = (FontTextView) rootView.findViewById(R.id.user_manual_ac4373);
		userManualAC4375 = (FontTextView) rootView.findViewById(R.id.user_manual_ac4375);
		
		lblAppTutorial.setOnClickListener(this);
		lblAppAirQualityExplain.setOnClickListener(this);
		
		TextView phoneNumber1= (TextView) rootView.findViewById(R.id.phone_number_one);
		TextView phoneNumber2= (TextView) rootView.findViewById(R.id.phone_number_two);
		
		phoneNumber2.setText(" / "+getString(R.string.contact_philips_support_phone_num_2));
		phoneNumber1.setOnClickListener(this);
		phoneNumber2.setOnClickListener(this);
		
		RelativeLayout contactUs = (RelativeLayout) rootView.findViewById(R.id.layout_help);
		contactUs.setOnClickListener(this);
		
		RelativeLayout weChat = (RelativeLayout) rootView.findViewById(R.id.layout_we_chat);
		weChat.setOnClickListener(this);
		
		lblFAQ.setOnClickListener(this);
		lblUserManual.setOnClickListener(this);
		
		RelativeLayout diagnostics = (RelativeLayout) rootView.findViewById(R.id.layout_email_us);
		diagnostics.setOnClickListener(this);
		lblOpensource.setOnClickListener(this);
		
		faqAC4373.setOnClickListener(this);
		faqAC4375.setOnClickListener(this);
		userManualAC4373.setOnClickListener(this);
		userManualAC4375.setOnClickListener(this);
		backButton.setOnClickListener(this);
	}	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.heading_back_imgbtn:
			MainActivity activity = (MainActivity) getActivity();
			if (activity != null) {
				activity.showFragment(new AboutFragment());
			}
			break;
		case R.id.phone_number_one:
			//TODO : Move to one place.
			MetricsTracker.trackActionServiceRequest("phone");
			MetricsTracker.trackActionExitLink("phone : " + getString(R.string.contact_philips_support_phone_num));
			Intent dialSupportIntent = new Intent(Intent.ACTION_DIAL);
			dialSupportIntent.setData(Uri.parse("tel:" + getString(R.string.contact_philips_support_phone_num)));
			startActivity(Intent.createChooser(dialSupportIntent, "Air Purifier support"));
			break;
		case R.id.phone_number_two:
			//TODO : Move to one place.
			MetricsTracker.trackActionServiceRequest("phone");
			MetricsTracker.trackActionExitLink("phone : " + getString(R.string.contact_philips_support_phone_num_2));
			Intent callIntent = new Intent(Intent.ACTION_DIAL);
			callIntent.setData(Uri.parse("tel:" + getString(R.string.contact_philips_support_phone_num_2)));
			startActivity(Intent.createChooser(callIntent, "Air Purifier support"));
			break;
		case R.id.layout_email_us:
			MetricsTracker.trackActionServiceRequest("email");
			MetricsTracker.trackActionExitLink(getString(R.string.contact_philips_support_email));
			diagnosticData();
			break;
		case R.id.layout_help:
			MetricsTracker.trackActionServiceRequest("help");
			MetricsTracker.trackActionExitLink(getString(R.string.contact_philips_support_website));
			Intent gotoSupportWebisteIntent = new Intent(Intent.ACTION_VIEW);
			gotoSupportWebisteIntent.setData(Uri.parse("http://"+ getString(R.string.contact_philips_support_website)));
			startActivity(Intent.createChooser(gotoSupportWebisteIntent,""));
			break;
		case R.id.faq:
			setVisibility(faqAC4373, faqAC4375);
			break;
		case R.id.faq_ac4373:
			MetricsTracker.trackActionServiceRequest("faq_ac4373");
			MetricsTracker.trackActionExitLink(getString(R.string.faq_link_ac4373));
			Intent faqAc4373 = new Intent(Intent.ACTION_VIEW);
			faqAc4373.setData(Uri.parse("http://"+ getString(R.string.faq_link_ac4373)));
			startActivity(Intent.createChooser(faqAc4373,""));
			break;
		case R.id.faq_ac4375:
			MetricsTracker.trackActionServiceRequest("faq_ac4375");
			MetricsTracker.trackActionExitLink(getString(R.string.faq_link_ac4375));
			Intent faqAC4375 = new Intent(Intent.ACTION_VIEW);
			faqAC4375.setData(Uri.parse("http://"+ getString(R.string.faq_link_ac4375)));
			startActivity(Intent.createChooser(faqAC4375,""));
			break;
		case R.id.lbl_user_manual:
			setVisibility(userManualAC4373, userManualAC4375);
			break;
		case R.id.user_manual_ac4373:
			MetricsTracker.trackActionServiceRequest("user_manual_ac4373");
			MetricsTracker.trackActionExitLink(getString(R.string.user_manual_link_ac4373));
			Intent manualAc4373 = new Intent(Intent.ACTION_VIEW);
			manualAc4373.setData(Uri.parse("http://"+ getString(R.string.user_manual_link_ac4373)));
			startActivity(Intent.createChooser(manualAc4373,""));
			break;
		case R.id.user_manual_ac4375:
			MetricsTracker.trackActionServiceRequest("user_manual_ac4375");
			MetricsTracker.trackActionExitLink(getString(R.string.user_manual_link_ac4375));
			Intent manualAc4375 = new Intent(Intent.ACTION_VIEW);
			manualAc4375.setData(Uri.parse("http://"+ getString(R.string.user_manual_link_ac4375)));
			startActivity(Intent.createChooser(manualAc4375,""));
			break;
		case R.id.app_tutorial:
			MetricsTracker.trackActionServiceRequest("app_tutorial");
			Intent intentOd = new Intent(getActivity(), TutorialPagerActivity.class);
			startActivity(intentOd);
			break;
		case R.id.app_air_quality_explain:
			MetricsTracker.trackActionServiceRequest("app_air_quality_explain");
			Intent intentAQE = new Intent(getActivity(), AirQualityActivity.class);
			startActivity(intentAQE);
			break;
		case R.id.opensource_lb:
			if (getActivity() == null) return;
			startActivity(new Intent(getActivity(), OpenSourceLibLicenseActivity.class));
			break;
		case R.id.layout_we_chat:
			MetricsTracker.trackActionServiceRequest("we_chat");
			MetricsTracker.trackActionExitLink("we_chat");
			startNewActivity(getActivity(), "com.tencent.mm");
			break;
		default:
			break;
		}
	}
	
	private void setVisibility(FontTextView ac4373View, FontTextView ac4375View) {
		if (ac4373View.getVisibility() == View.GONE 
				&& ac4375View.getVisibility() == View.GONE) {
			ac4373View.setVisibility(View.VISIBLE);
			ac4375View.setVisibility(View.VISIBLE);
		} else {
			ac4373View.setVisibility(View.GONE);
			ac4375View.setVisibility(View.GONE);
		}
	}
	
	private void startNewActivity(Context context, String packageName)
	{
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		if (intent != null)
		{
			/* we found the activity now start the activity */
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
		else
		{
			/* bring user to the market or let them choose an app? */
			intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setData(Uri.parse("market://details?id="+packageName));
			startActivity(intent);
		}
	}
	
	
	/**
	 * Fetches all required diagnostic data
	 */
	public void diagnosticData(){

		String jainRainUser="App not registered";
		String userEmail="";
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
		String appEui64 = getString(R.string.app_eui64) + SessionDto.getInstance().getAppEui64();
		
		List<PurAirDevice> purifiers= DiscoveryManager.getInstance().getStoreDevices();

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
		sendMail(data.toString(), getString(R.string.contact_philips_support_email), userEmail);
	}

	public void sendMail(String message, String sendTo, String userEmail) {
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, new String[] { sendTo });
		email.putExtra(Intent.EXTRA_SUBJECT, "AC4373/75 diagnostics for "+userEmail);
		email.putExtra(Intent.EXTRA_TEXT, message);
		email.setType("message/rfc822");
		startActivity(Intent.createChooser(email, "Send this mail via:"));
	}
	
	
}
