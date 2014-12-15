package com.philips.cl.di.dev.pa.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.dashboard.HomeFragment;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.purifier.PurifierDatabase;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.util.Utils;

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
		MetricsTracker.trackPage(TrackPageConstants.EWS_NEW_PURIFIER_ADDED);
		Button startControlPurifierBtn = (Button) getView().findViewById(R.id.finish_congratulation_btn);
		startControlPurifierBtn.setTypeface(Fonts.getGillsansLight(getActivity()));
		startControlPurifierBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).setTitle(getString(R.string.dashboard_title));
				((MainActivity) getActivity()).showFragment(new HomeFragment());

				Utils.saveAppFirstUse(false);
				
				PurifierDatabase purifierDatabase = new PurifierDatabase();
				purifierDatabase.insertPurAirDevice(PurifierManager.getInstance().getCurrentPurifier());
				List<PurAirDevice> purifiers = DiscoveryManager.getInstance().updateStoreDevices();
				PurifierManager.getInstance().setCurrentIndoorViewPagerPosition(purifiers.size() - 1);
			}
		});
	}
}
