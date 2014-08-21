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
import android.widget.TextView;
import android.widget.ToggleButton;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.activity.PrivacyPolicyActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.demo.DemoModeTask;
import com.philips.cl.di.dev.pa.ews.EWSConstant;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.registration.UserRegistrationController;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.JSONBuilder;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.dao.DIUserProfile;

public class SettingsFragment extends BaseFragment implements OnClickListener, OnCheckedChangeListener {

	private TextView versionNumber;

	private ToggleButton demoModeTButton;
	private TextView privacyPolicy;

	private TextView diagnosticsLbl;
	private char lineSeparator='\n';

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.settings_fragment, container, false);
		initViews(view);
		return view;
	}

	private void initViews(View view) {

		versionNumber = (TextView) view.findViewById(R.id.tv_version_number);
		versionNumber.setText(getString(R.string.version_number) + " " +((MainActivity) getActivity()).getVersionNumber()); //Should probably change it to version name.

		demoModeTButton = (ToggleButton) view.findViewById(R.id.settings_demo_mode_toggle);

		demoModeTButton.setChecked(PurAirApplication.isDemoModeEnable());
		demoModeTButton.setOnCheckedChangeListener(this);

		privacyPolicy = (TextView) view.findViewById(R.id.tv_privacy_policy);
		privacyPolicy.setOnClickListener(this);

		diagnosticsLbl= (TextView)view.findViewById(R.id.diagnostics_lb);
		diagnosticsLbl.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_privacy_policy:
			getActivity().startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));
			break;

		case R.id.diagnostics_lb:
			diagnosticData();
			break;

		default:
			break;
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (getActivity() == null) return;
		MainActivity mainActivity = (MainActivity) getActivity();
		if (buttonView.getId() == R.id.settings_demo_mode_toggle) {
			ALog.i(ALog.DEMO_MODE, "Demo mode enable: " + isChecked);
			PurAirApplication.setDemoModeEnable(isChecked);
			if (!isChecked) {
				if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ) {
					android.provider.Settings.System.putString(getActivity().getContentResolver(), android.provider.Settings.System.WIFI_USE_STATIC_IP, "0");  
				}
				PurAirDevice purAirDevice = PurifierManager.getInstance().getCurrentPurifier();
				if (purAirDevice != null && purAirDevice.isDemoPurifier()) {
					String dataToSend = JSONBuilder.getDICommUIBuilder(purAirDevice);
					DemoModeTask task = new DemoModeTask(
							null, Utils.getPortUrl(Port.WIFIUI, EWSConstant.PURIFIER_ADHOCIP),dataToSend , "PUT") ;
					task.start();
				}
				PurifierManager.getInstance().setCurrentIndoorViewPagerPosition(0);
				mainActivity.startNormalMode();
			} else {
				PurifierManager.getInstance().setCurrentIndoorViewPagerPosition(1);
				mainActivity.startDemoMode();
			}
			PurifierManager.getInstance().removeCurrentPurifier();
			((MainActivity) getActivity()).setActionBar(new SettingsFragment());
			((MainActivity) getActivity()).onAirPurifierChanged();

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
		String appVersion= getString(R.string.app_version)+((MainActivity) getActivity()).getVersionNumber();
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
