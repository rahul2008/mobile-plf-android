package com.philips.cl.di.dev.pa.fragment;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.registration.UserRegistrationController;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.dao.DIUserProfile;

public class HelpAndDocFragment extends BaseFragment implements OnClickListener{
	
	private char lineSeparator='\n';
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.help_and_doc_fragment, container, false);
		initializeView(view);
		return view;
	}

	private void initializeView(View rootView) {
		
		TextView lblAppTutorial=(TextView) rootView.findViewById(R.id.app_tutorial);
		TextView lblFAQ= (TextView) rootView.findViewById(R.id.faq);
		TextView lblUserManual=(TextView) rootView.findViewById(R.id.lbl_user_manual);
		TextView lblPhilipsSupport=(TextView) rootView.findViewById(R.id.lbl_philips_support);
		TextView lblCallUs=(TextView) rootView.findViewById(R.id.lbl_call_us);
		TextView lblSupport=(TextView) rootView.findViewById(R.id.lbl_support);	
		//TODO 
//		TextView lblOpensource = (TextView) rootView.findViewById(R.id.opensource_lb);
		
		lblAppTutorial.setTypeface(Fonts.getGillsans(getActivity()));		
		lblFAQ.setTypeface(Fonts.getGillsans(getActivity()));
		lblUserManual.setTypeface(Fonts.getGillsans(getActivity()));
		lblPhilipsSupport.setTypeface(Fonts.getGillsans(getActivity()));
		lblCallUs.setTypeface(Fonts.getGillsans(getActivity()));
		lblSupport.setTypeface(Fonts.getGillsans(getActivity()));	
		//TODO 
//		lblOpensource.setTypeface(Fonts.getGillsans(getActivity()));	
		
		lblAppTutorial.setOnClickListener(this);
		
		RelativeLayout callUs = (RelativeLayout) rootView.findViewById(R.id.layout_call_us);
		callUs.setOnClickListener(this);
		
		RelativeLayout contactUs = (RelativeLayout) rootView.findViewById(R.id.layout_help);
		contactUs.setOnClickListener(this);
		
		lblFAQ.setOnClickListener(this);
		lblUserManual.setOnClickListener(this);
		
		RelativeLayout diagnostics = (RelativeLayout) rootView.findViewById(R.id.layout_email_us);
		diagnostics.setOnClickListener(this);
		//TODO 
//		lblOpensource.setOnClickListener(this);
	}	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_call_us:
			//TODO : Move to one place.
			Intent dialSupportIntent = new Intent(Intent.ACTION_DIAL);
			dialSupportIntent.setData(Uri.parse("tel:" + getString(R.string.contact_philips_support_phone_num)));
			startActivity(Intent.createChooser(dialSupportIntent, "Air Purifier support"));
			break;
			
		case R.id.layout_email_us:
			diagnosticData();
			break;
			
		case R.id.layout_help:
			Intent gotoSupportWebisteIntent = new Intent(Intent.ACTION_VIEW);
			gotoSupportWebisteIntent.setData(Uri.parse("http://" + getString(R.string.contact_philips_support_website)));
			startActivity(gotoSupportWebisteIntent);
			break;
			
		case R.id.faq:
		case R.id.lbl_user_manual:
		case R.id.app_tutorial:
			Intent faq = new Intent(Intent.ACTION_VIEW);
			faq.setData(Uri.parse("http://www.philips.com.cn/AC4373_00/prd"));
			startActivity(faq);
			break;
		//TODO 
//		case R.id.opensource_lb:
//			getActivity().startActivity(new Intent(getActivity(), OpenSourceLibLicensActivity.class));
//			break;
			
		default:
			break;
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

		StringBuilder data= new StringBuilder("This is an automatically generated diagnostic email send by the Philips smart air purifier App. The user of the App has initiated this email and likely requires assistance.");
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
			data.append(getString(R.string.purifier_eui64)).append(purifiers.get(i).getEui64());
			data.append(lineSeparator);
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
