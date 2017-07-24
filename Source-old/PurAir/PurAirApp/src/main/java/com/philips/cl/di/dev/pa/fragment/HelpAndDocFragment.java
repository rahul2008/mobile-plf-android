package com.philips.cl.di.dev.pa.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.AirQualityActivity;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.activity.OpenSourceLibLicenseActivity;
import com.philips.cl.di.dev.pa.activity.TutorialPagerActivity;
import com.philips.cl.di.dev.pa.buyonline.BuyOnlineFragment;
import com.philips.cl.di.dev.pa.buyonline.ProductRegisterFragment;
import com.philips.cl.di.dev.pa.buyonline.PromotionsFragment;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.demo.DemoModeController;
import com.philips.cl.di.dev.pa.registration.UserRegistrationController;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.SupportUtil;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.dao.DIUserProfile;

public class HelpAndDocFragment extends BaseFragment implements OnClickListener, OnCheckedChangeListener {

	//private char lineSeparator='\n';
	private FontTextView faqAC4373, faqAC4375;
	private FontTextView userManualAC4373, userManualAC4375;
	private FontTextView websiteAC4373, websiteAC4375;
	private ScrollView scrollView;
	private char lineSeparator='\n';
	private String userEmail="";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.help_and_doc_fragment, container, false);
		initializeView(view);
		initDemoMode(view);
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
		FontTextView qrCode=(FontTextView) rootView.findViewById(R.id.qr_Code);
		FontTextView buyOnline=(FontTextView) rootView.findViewById(R.id.buy_online);
		FontTextView promotionalVideo=(FontTextView) rootView.findViewById(R.id.promotional_video);
		FontTextView productRegistration=(FontTextView) rootView.findViewById(R.id.product_registration);
		FontTextView about=(FontTextView) rootView.findViewById(R.id.about);
		FontTextView rateandFeedback=(FontTextView) rootView.findViewById(R.id.rate_feedback);
		FontTextView lblOpensource = (FontTextView) rootView.findViewById(R.id.opensource_lb);
		FontTextView diagnosticLabel = (FontTextView)rootView.findViewById(R.id.lbl_email_us) ;
		diagnosticLabel.setText(getString(R.string.diagnostics) +":");
		faqAC4373 = (FontTextView) rootView.findViewById(R.id.faq_ac4373);
		faqAC4375 = (FontTextView) rootView.findViewById(R.id.faq_ac4375);
		userManualAC4373 = (FontTextView) rootView.findViewById(R.id.user_manual_ac4373);
		userManualAC4375 = (FontTextView) rootView.findViewById(R.id.user_manual_ac4375);
		websiteAC4373 = (FontTextView) rootView.findViewById(R.id.help_contact_website_ac4373);
		websiteAC4375 = (FontTextView) rootView.findViewById(R.id.help_contact_website_ac4375);

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
		qrCode.setOnClickListener(this);
		buyOnline.setOnClickListener(this);
		promotionalVideo.setOnClickListener(this);
		productRegistration.setOnClickListener(this);
		about.setOnClickListener(this);
		rateandFeedback.setOnClickListener(this);

		RelativeLayout diagnostics = (RelativeLayout) rootView.findViewById(R.id.layout_email_us);
		diagnostics.setOnClickListener(this);
		lblOpensource.setOnClickListener(this);

		scrollView = (ScrollView) rootView.findViewById(R.id.help_documentation_scrollview);

		setListener();

		backButton.setOnClickListener(this);
	}	
	
	private void initDemoMode(View view) {
		ToggleButton demoModeTButton = (ToggleButton) view.findViewById(R.id.settings_demo_mode_toggle);

		demoModeTButton.setChecked(PurAirApplication.isDemoModeEnable());
		demoModeTButton.setOnCheckedChangeListener(this);
	}

	private void setListener() {
		faqAC4373.setOnClickListener(this);
		faqAC4375.setOnClickListener(this);
		userManualAC4373.setOnClickListener(this);
		userManualAC4375.setOnClickListener(this);
		websiteAC4373.setOnClickListener(this);
		websiteAC4375.setOnClickListener(this);
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
	public void onClick(View v) {

		MainActivity activity = (MainActivity) getActivity();
		if (activity == null) return;
		switch (v.getId()) {
		case R.id.heading_back_imgbtn:
			activity.showFirstFragment();
			break;
		case R.id.phone_number_one:
			MetricsTracker.trackActionServiceRequest("phone");
			SupportUtil.gotoPhoneDial(getActivity(), getString(R.string.contact_philips_support_phone_num));
			break;
		case R.id.phone_number_two:
			MetricsTracker.trackActionServiceRequest("phone");
			SupportUtil.gotoPhoneDial(getActivity(), getString(R.string.contact_philips_support_phone_num_2));
			break;
		case R.id.layout_email_us:
			MetricsTracker.trackActionServiceRequest("email");
			MetricsTracker.trackActionExitLink(getString(R.string.contact_philips_support_email));
			sendMail(getDiagnosticDataForEmail(), getString(R.string.contact_philips_support_email), userEmail);
			//startActivity(new Intent(getActivity(), DiagnosticShareActivity.class));
			break;
		case R.id.layout_help:
			SupportUtil.setVisibility(websiteAC4373, websiteAC4375);
			SupportUtil.srollUpScrollView(scrollView);
			break;
		case R.id.help_contact_website_ac4373:
			MetricsTracker.trackActionServiceRequest("help_ac4373");
			SupportUtil.gotoWebsite(getActivity(), getString(R.string.contact_philips_support_website_ac4373));
			break;
		case R.id.help_contact_website_ac4375:
			MetricsTracker.trackActionServiceRequest("help_ac4375");
			SupportUtil.gotoWebsite(getActivity(), getString(R.string.contact_philips_support_website_ac4375));
			break;
		case R.id.faq:
			SupportUtil.setVisibility(faqAC4373, faqAC4375);
			break;
		case R.id.faq_ac4373:
			MetricsTracker.trackActionServiceRequest("faq_ac4373");
			SupportUtil.gotoWebsite(getActivity(), getString(R.string.faq_link_ac4373));
			break;
		case R.id.faq_ac4375:
			MetricsTracker.trackActionServiceRequest("faq_ac4375");
			SupportUtil.gotoWebsite(getActivity(), getString(R.string.faq_link_ac4375));
			break;
		case R.id.lbl_user_manual:
			SupportUtil.setVisibility(userManualAC4373, userManualAC4375);
			break;
		case R.id.user_manual_ac4373:
			MetricsTracker.trackActionServiceRequest("user_manual_ac4373");
			SupportUtil.gotoWebsite(getActivity(), getString(R.string.user_manual_link_ac4373));
			break;
		case R.id.user_manual_ac4375:
			MetricsTracker.trackActionServiceRequest("user_manual_ac4375");
			SupportUtil.gotoWebsite(getActivity(), getString(R.string.user_manual_link_ac4375));
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
			SupportUtil.startNewActivity(getActivity(), "com.tencent.mm");
			break;
		case R.id.qr_Code:
			if (getActivity() == null) return;
			MetricsTracker.trackActionServiceRequest("QR_Code");
			SupportUtil.gotoWebsite(getActivity(), AppConstants.APP_DOWNLOAD_LINK );
			break;
		case R.id.rate_feedback:
			if (getActivity() == null) return;
			activity.showFragment(new RateAndFeedbackFragment());
			break;
		case R.id.buy_online:
			activity.showFragment(new BuyOnlineFragment());			
			break;
		case R.id.promotional_video:
			activity.showFragment(new PromotionsFragment());
			break;
		case R.id.product_registration:
			activity.showFragment(new ProductRegisterFragment());
			break;
		case R.id.about:
			activity.showFragment(new AboutFragment());
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (getActivity() == null) return;
		MainActivity mainActivity = (MainActivity) getActivity();
		if (buttonView.getId() == R.id.settings_demo_mode_toggle) {
			new DemoModeController().toggleShopDemoMode(isChecked, mainActivity);
		}
	}
	
	/**
	 * Fetches all required diagnostic data
	 */
	private String getDiagnosticDataForEmail(){

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
		String appEui64 = getString(R.string.app_eui64) + CppController.getInstance().getAppCppId();

		List<? extends DICommAppliance> appliances = DiscoveryManager.getInstance().getAddedAppliances();

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
		for(int i=0; i<appliances.size(); i++){
			data.append(getString(R.string.purifier)).append(i+1).append(":");
			data.append(lineSeparator);
			data.append(getString(R.string.purifier_name)).append(appliances.get(i).getName());
			data.append(lineSeparator);
			data.append(getString(R.string.purifier_eui64)).append(appliances.get(i).getNetworkNode().getCppId());
			data.append(lineSeparator);
			if(appliances.get(i).getFirmwarePort().getPortProperties()!=null){
			data.append(getString(R.string.purifier_firmware_version)).append(appliances.get(i).getFirmwarePort().getPortProperties().getVersion());
			data.append(lineSeparator);
			}
			data.append(lineSeparator);
		}
		return data.toString();
	}

}
