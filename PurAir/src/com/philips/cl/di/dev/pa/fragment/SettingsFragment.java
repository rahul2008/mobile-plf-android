package com.philips.cl.di.dev.pa.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.demo.DemoModeTask;
import com.philips.cl.di.dev.pa.ews.EWSConstant;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.JSONBuilder;
import com.philips.cl.di.dev.pa.util.NetworkUtils;
import com.philips.cl.di.dev.pa.util.Utils;

public class SettingsFragment extends BaseFragment implements OnClickListener, OnCheckedChangeListener {
	
	private ImageView ivRateThisApp, ivSendUsFeedback;
	private TextView tvRateThisApp, tvSendUsFeedback;
	
	private TextView versionNumber;
	
	private ToggleButton demoModeTButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.settings_fragment, container, false);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		ivRateThisApp = (ImageView) view.findViewById(R.id.iv_rate_this_app);
		ivRateThisApp.setOnClickListener(this);
		
		ivSendUsFeedback = (ImageView) view.findViewById(R.id.iv_send_us_feedback);
		ivSendUsFeedback.setOnClickListener(this);
		
		tvRateThisApp = (TextView) view.findViewById(R.id.tv_rate_this_app);
		tvRateThisApp.setOnClickListener(this);
		
		tvSendUsFeedback = (TextView) view.findViewById(R.id.tv_send_us_feedback);
		tvSendUsFeedback.setOnClickListener(this);
		
		versionNumber = (TextView) view.findViewById(R.id.tv_version_number);
		versionNumber.setText(getString(R.string.version_number) + " " +((MainActivity) getActivity()).getVersionNumber()); //Should probably change it to version name.
		
		demoModeTButton = (ToggleButton) view.findViewById(R.id.settings_demo_mode_toggle);
		
		demoModeTButton.setChecked(PurAirApplication.isDemoModeEnable());
		demoModeTButton.setOnCheckedChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_rate_this_app:
		case R.id.iv_rate_this_app:
			// TODO : Change this, it's a placeholder. We should replace it with getPackageName() once the app is published.
			if(NetworkUtils.isNetworkConnected(getActivity())) {
				String appName = "com.philips.airstudioplus";
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
			} else {
				Toast.makeText(getActivity(), "No Network Rate this app", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.tv_send_us_feedback:
		case R.id.iv_send_us_feedback:
			if(NetworkUtils.isNetworkConnected(getActivity())) {
				Intent feedbackIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","sangamesh.bn@philips.com", null));
				feedbackIntent.putExtra(Intent.EXTRA_SUBJECT, "App feedback");
				feedbackIntent.putExtra(Intent.EXTRA_TEXT, "Give feedback");
				getActivity().startActivity(Intent.createChooser(feedbackIntent, "Air Purifier App Feedback"));
			} else {
				Toast.makeText(getActivity(), "No Network Send us feedback", Toast.LENGTH_SHORT).show();
			}
			
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
}
