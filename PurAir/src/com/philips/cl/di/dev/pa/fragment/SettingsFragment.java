package com.philips.cl.di.dev.pa.fragment;

import android.content.Intent;
import android.net.Uri;
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
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.demo.DemoMode;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.NetworkUtils;

public class SettingsFragment extends BaseFragment implements OnClickListener, OnCheckedChangeListener {
	
	private ImageView ivRateThisApp, ivSendUsFeedback;
	private TextView tvRateThisApp, tvSendUsFeedback;
	
	private TextView versionNumber;
	private TextView termsAndConditions;
	
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
		
		termsAndConditions = (TextView) view.findViewById(R.id.tv_t_and_c);
		termsAndConditions.setOnClickListener(this);
		
		demoModeTButton = (ToggleButton) view.findViewById(R.id.settings_demo_mode_toggle);
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
		case R.id.tv_t_and_c:
			//TODO : Add Terms and conditions when recieved.
//			getActivity().startActivity(new Intent(getActivity(), TermsAndConditionsActivity.class));
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (getActivity() == null) return;
		if (buttonView.getId() == R.id.settings_demo_mode_toggle) {
			ALog.i(ALog.DEMO_MODE, "Demo mode enable: " + isChecked);
			DemoMode.setDemoModeEnable(isChecked);
			if (!isChecked) {
				PurAirDevice purAirDevice = PurifierManager.getInstance().getCurrentPurifier();
				if (purAirDevice != null && purAirDevice.getName() != null) {
					if (purAirDevice.getName().equals(DemoMode.DEMO)) {
//						DiscoveryManager.getInstance().stop();
						//TODO send put wifi
					}
				}
			}
		}
	}
}
