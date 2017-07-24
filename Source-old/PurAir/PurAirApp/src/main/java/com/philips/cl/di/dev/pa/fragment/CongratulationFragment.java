package com.philips.cl.di.dev.pa.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.HomeFragment;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class CongratulationFragment extends BaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view  = inflater.inflate(R.layout.setup_congratulation, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MetricsTracker.trackPage(TrackPageConstants.NEW_PURIFIER_ADDED);
		
		Button startControlPurifierBtn = (Button) getView().findViewById(R.id.finish_congratulation_btn);
		startControlPurifierBtn.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		startControlPurifierBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).setTitle(getString(R.string.dashboard_title));
				((MainActivity) getActivity()).showFragment(new HomeFragment());

				Utils.saveAppFirstUse(false);
				
				DiscoveryManager<AirPurifier> discoveryManager = (DiscoveryManager<AirPurifier>) DiscoveryManager.getInstance();
				discoveryManager.insertApplianceToDatabase(AirPurifierManager.getInstance().getCurrentPurifier());
				List<AirPurifier> appliances = discoveryManager.updateAddedAppliances();
				AirPurifierManager.getInstance().setCurrentIndoorViewPagerPosition(appliances.size() - 1);
			}
		});
		
		//This call from activity does not have action bar, adding view as action bar.
		Bundle bundle = getArguments();
		if (bundle != null) {
			boolean showHeading = bundle.getBoolean(AppConstants.SHOW_HEADING, false);
			FontTextView headingTV = (FontTextView) getView().findViewById(R.id.heading_name_tv);
			View shadowView = (View) getView().findViewById(R.id.shadowView);
			if (showHeading) {
				headingTV.setVisibility(View.VISIBLE);
				shadowView.setVisibility(View.VISIBLE);
			} else {
				headingTV.setVisibility(View.GONE);
				shadowView.setVisibility(View.GONE);
			}
		}
	}
}
